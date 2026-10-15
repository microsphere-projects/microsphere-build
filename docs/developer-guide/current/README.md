# Developer Guide — `release` branch (current)

[English](README.md) | [简体中文](zh/README.md)

This guide is for **maintainers of the `microsphere-build` repository**. It explains the architecture of the parent
POM, analyses `pom.xml` source, documents the design patterns in use, and gives Mermaid flow/sequence diagrams for
the complex build and CI/CD flows.

> Analysed from branch `release` at `0.3.18-SNAPSHOT`. The `release-1.x` branch does not exist, so only the
> `current/` documentation set is provided.

## Documents (one per Maven module + cross-cutting)

| Document | Scope |
|---|---|
| [microsphere-build.md](microsphere-build.md) | Root parent POM — source analysis of `pom.xml`: section layout, effective build lifecycle, profile interaction model, flatten/CI-friendly machinery |
| [microsphere-build-example.md](microsphere-build-example.md) | Example module — how it validates the parent contract; source analysis of `ExampleService` and its tests |
| [build-pipeline.md](build-pipeline.md) | CI/CD — flow diagrams of the seven GitHub Actions workflows and the release publishing sequence |

## Architecture at a glance

```mermaid
flowchart TD
    subgraph repo["microsphere-build repository (release branch)"]
        ROOT["pom.xml — root parent<br/>packaging: pom<br/>version: ${revision} = 0.3.18-SNAPSHOT"]
        EX["microsphere-build-example<br/>packaging: jar<br/>deploy: skipped"]
        WF[".github/workflows<br/>build · publish · release-notes ·<br/>merge · sync · wiki · readme-versions"]
        WRAP[".mvn/wrapper<br/>Maven 3.9.16"]
    end

    DOWNSTREAM["Downstream Microsphere projects<br/>(microsphere-java, microsphere-spring, ...)"]
    CENTRAL[("Maven Central<br/>central.sonatype.com")]
    SNAP[("Snapshot repo<br/>maven-snapshots")]

    ROOT -- "aggregator &lt;modules&gt;<br/>+ &lt;parent&gt; inheritance" --> EX
    ROOT -- "publishes parent POM only" --> CENTRAL
    ROOT -. "snapshots" .-> SNAP
    WF -- "mvn deploy -P publish,ci" --> CENTRAL
    WF -- "mvn test matrix JDK 8–25" --> ROOT
    WRAP -- "build toolchain" --> WF
    DOWNSTREAM -. "&lt;parent&gt; microsphere-build:0.3.x" .-> CENTRAL
```

Key architectural facts:

1. **The product is a POM, not a JAR.** The only artifact consumers obtain is the parent POM itself (plus its
   flattened form). `microsphere-build-example` is deliberately excluded from deployment.
2. **Single source of build truth.** All plugin versions live as `*.version` properties in the root `<properties>`
   block and are referenced only from `<pluginManagement>` — children override by property, never by copy-paste
   plugin XML.
3. **Behaviour is layered by profiles.** Six manual profiles (`publish`, `release`, `ci`, `test`, `coverage`, `docs`)
   attach heavy plugins on demand; six JDK-range profiles (`java8+`, `java9+`, `java11+`, `java9-15`, `java16+`,
   `java17+`) auto-adjust tool versions and JVM flags. See
   [microsphere-build.md § Profile interaction model](microsphere-build.md#profile-interaction-model).
4. **Version is a build parameter.** `${revision}` + Flatten plugin + `-Drevision=` in the publish workflow make the
   POM versionless at rest and versioned in flight.

## Module dependency relationships

Within the reactor:

```mermaid
flowchart LR
    A["microsphere-build (pom)"] -->|parent + module| B["microsphere-build-example (jar)"]
    B -->|test scope| J1["junit-jupiter 6.1.3<br/>(JDK ≥ 17)"]
    B -->|test scope, via java8-16 profile| J2["junit-jupiter 5.14.3<br/>(JDK 8–16)"]
    A -->|pluginManagement deps| C["checkstyle 14.3.0 base<br/>9.3 / 10.26.1 / 11.1.0 per JDK"]
```

External build-tool dependencies of the parent (all in `pluginManagement`; activated per profile as shown in the
[User Guide](../../user-guide/current/microsphere-build.md)):

```mermaid
flowchart TD
    subgraph always["Inherited by every build"]
        COMP[maven-compiler 3.16.0]
        SRC[maven-source 3.4.0]
        FLAT[flatten-maven 1.7.3]
    end
    subgraph publishP["-P publish / release"]
        JAVADOC[maven-javadoc 3.12.0]
        REL[maven-release 3.3.1]
        ENF[maven-enforcer 3.6.3]
        GPG[maven-gpg 3.2.8]
        GIT[git-commit-id 4.9.10]
        CENTRALP[central-publishing 0.11.0]
        NEXUS[nexus-staging 1.7.0]
    end
    subgraph testP["-P test"]
        SUR[maven-surefire 3.6.0]
        FAIL[maven-failsafe 3.6.0]
        CHK[maven-checkstyle 3.6.0]
    end
    subgraph covP["-P coverage"]
        JAC[jacoco 0.8.15]
    end
    subgraph docsP["-P docs"]
        ASC[asciidoctor 3.2.0/2.2.6]
        DBK[docbkx 2.0.17]
        BH[build-helper 3.6.2]
    end
    subgraph ciP["-P ci"]
        SIGN[sign-maven 1.2.0]
    end
    JAC -. "jacoco.argLine →<br/>surefire argLine" .-> SUR
```
