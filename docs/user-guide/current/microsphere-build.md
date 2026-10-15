# Microsphere Build (Parent POM) — User Guide

[English](microsphere-build.md) | [简体中文](zh/microsphere-build.md)

- **Group ID**: `io.github.microsphere-projects`
- **Artifact ID**: `microsphere-build`
- **Packaging**: `pom` (parent POM — contains no Java sources of its own)
- **Version analysed**: `0.3.18-SNAPSHOT` (branch `release`); latest published release: `0.3.17`
- **License**: Apache License 2.0

The parent POM is the "API" of this project. Consumers do not call Java classes from it; they inherit its
**properties**, **plugin management**, **profiles**, **resource filtering** and **distribution settings** by
declaring it as their `<parent>`. This document explains every inheritable element, how to use and override it,
and the version compatibility range.

<a id="get-getting-started-with-the-parent-pom"></a>

## 1. Getting started

### 1.1 Prerequisites

| Requirement | Minimum | Notes |
|---|---|---|
| JDK | 8 | LTS 8 / 11 / 17 / 21 / 25 are tested in CI (Temurin) |
| Apache Maven | 3.9+ | 3.5+ is the hard minimum for `${revision}`; the repo's own wrapper uses 3.9.16 |
| Git | 2.x | Required when the `publish`/`release` profile runs the Git Commit ID plugin |

### 1.2 Declare the parent

```xml
<parent>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-build</artifactId>
    <version>0.3.17</version>
</parent>
```

### 1.3 Choose your Java target

The default bytecode target is Java 8. Override the single `java.version` property to retarget everything
(`maven.compiler.source`, `maven.compiler.target` and, on JDK 9+, `maven.compiler.release` all derive from it):

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

### 1.4 Build

Three plugins are inherited and active in **every** build — no extra configuration is needed:

| Plugin | Version (0.3.18-SNAPSHOT) | Effect |
|---|---|---|
| `maven-compiler-plugin` | 3.16.0 | Compiles with `-parameters` (retains method parameter names) |
| `maven-source-plugin` | 3.4.0 | Attaches `-sources.jar` at `package` (`jar-no-fork`) |
| `flatten-maven-plugin` | 1.7.3 | Resolves CI-friendly `${revision}` in the installed/deployed POM (`process-resources`) |

```bash
mvn clean package                 # compile + sources + flatten
mvn clean verify -P test          # + unit tests, integration tests, checkstyle
mvn clean test -P test,coverage   # + JaCoCo report at target/site/jacoco/index.html
```

## 2. Profiles reference (the inheritable behaviour API)

### 2.1 Manually activated profiles

| Profile | Purpose | Plugins activated | Typical command |
|---|---|---|---|
| `publish` (recommended since 0.2.0) | Publish signed artifacts to Maven Central via the Central Publishing Portal | javadoc 3.12.0, release 3.3.1, enforcer 3.6.3, gpg 3.2.8, git-commit-id 4.9.10, central-publishing 0.11.0 | `mvn clean deploy -P publish` |
| `release` (legacy) | Publish via the OSSRH Staging API (Nexus) — OSSRH reached EOL on 2025-06-30; prefer `publish` | javadoc, release, enforcer, gpg, git-commit-id, nexus-staging 1.7.0 | `mvn clean deploy -P release` |
| `ci` | Artifact signing in CI from environment variables (no local keyring) | `sign-maven-plugin` 1.2.0 | `mvn clean deploy -P publish,ci -Dgpg.skip=true` |
| `test` | Unit + integration tests + code-style checks | surefire 3.6.0, failsafe 3.6.0, checkstyle 3.6.0 | `mvn clean verify -P test` |
| `coverage` | JaCoCo instrumentation and reporting | jacoco 0.8.15 (`prepare-agent`, `report`) | `mvn clean test -P test,coverage` |
| `docs` | AsciiDoc / DocBook documentation generation | asciidoctor 3.2.0 (2.2.6 on JDK 8–10), docbkx 2.0.17, build-helper 3.6.2 | `mvn clean generate-resources -P docs` |

`ci` profile signing requires these environment variables:

| Variable | Description |
|---|---|
| `SIGN_KEY` | Content of the private key (not a file path) |
| `SIGN_KEY_ID` | Long-format key ID |
| `SIGN_KEY_PASS` | Key passphrase |

Profiles can be combined freely, e.g. `mvn clean verify -P test,docs,coverage`.

### 2.2 JDK-auto-activated profiles

These activate from the JDK version detected by Maven — never pass them with `-P`:

| Profile | Activation | Effect |
|---|---|---|
| `java8+` | `[1.8,)` | `<doclint>none</doclint>` for javadoc; Checkstyle tool pinned to 9.3; Asciidoctor 2.2.6 |
| `java9+` | `[9,)` | Sets `maven.compiler.release=${java.version}` (true cross-compilation) |
| `java11+` | `[11,)` | Javadoc `source=${maven.compiler.source}`; Checkstyle tool 10.26.1; Asciidoctor 3.2.0 |
| `java9-15` | `[9,15]` | `jvm.argLine=--illegal-access=permit` for Surefire |
| `java16+` | `[16,)` | `jvm.argLine=--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED`; compiler `fork=true` with the same `-J--add-opens` args |
| `java17+` | `[17,)` | Checkstyle tool 11.1.0 |

Activation matrix (profiles are additive):

| JDK | `java8+` | `java9+` | `java9-15` | `java11+` | `java16+` | `java17+` |
|---|---|---|---|---|---|---|
| 8 | ✅ | — | — | — | — | — |
| 9–10 | ✅ | ✅ | ✅ | — | — | — |
| 11–15 | ✅ | ✅ | ✅ | ✅ | — | — |
| 16 | ✅ | ✅ | — | ✅ | ✅ | — |
| 17 / 21 / 25 | ✅ | ✅ | — | ✅ | ✅ | ✅ |

## 3. Configuration properties reference

Override any of these in your child POM's `<properties>`.

### 3.1 Core build properties

| Property | Default | Description |
|---|---|---|
| `revision` | `0.3.18-SNAPSHOT` (in this repo) | CI-friendly version placeholder; in your project use your own version |
| `java.version` | `8` | Drives `source`/`target`/`release` |
| `project.build.sourceEncoding` | `UTF-8` | Source encoding |
| `resource.delimiter` | `@` | Resource-filtering delimiter (`@property@`) |
| `jvm.argLine` | *(empty; set by JDK profiles)* | Extra JVM args appended to Surefire's `argLine` |
| `jacoco.argLine` | *(set by `prepare-agent`)* | JaCoCo agent argument slot |
| `disable.checks` | `true` | Checkstyle skip switch (the managed execution honours it) |

### 3.2 Plugin version properties

| Property | Default |
|---|---|
| `maven-compiler-plugin.version` | `3.16.0` |
| `maven-javadoc-plugin.version` | `3.12.0` |
| `maven-source-plugin.version` | `3.4.0` |
| `maven-jar-plugin.version` | `3.5.1` |
| `maven-war-plugin.version` | `3.5.1` |
| `maven-share-plugin.version` (Shade) | `3.6.2` |
| `maven-deploy-plugin.version` | `3.2.0` |
| `maven-release-plugin.version` | `3.3.1` |
| `maven-failsafe-plugin.version` / `maven-surefire-plugin.version` | `3.6.0` |
| `maven-checkstyle-plugin.version` | `3.6.0` |
| `jacoco-maven-plugin.version` | `0.8.15` |
| `maven-lombok-plugin.version` | `1.18.20.0` |
| `maven-enforcer-plugin.version` | `3.6.3` |
| `maven-gpg-plugin.version` | `3.2.8` |
| `sign-maven-plugin.version` | `1.2.0` |
| `nexus-staging-maven-plugin.version` | `1.7.0` |
| `flatten-maven-plugin.version` | `1.7.3` |
| `asciidoctor-maven-plugin.version` | `3.2.0` (2.2.6 on JDK 8–10 via `java8+`) |
| `docbkx-maven-plugin.version` | `2.0.17` |
| `build-helper-maven-plugin.version` | `3.6.2` |
| `puppycrawl-tools-checkstyle.version` | `14.3.0` base, overridden per JDK: 9.3 (`java8+`), 10.26.1 (`java11+`), 11.1.0 (`java17+`) |
| `git-commit-id-plugin.version` | `4.9.10` |
| `central-publishing-maven-plugin.version` | `0.11.0` |

### 3.3 Flatten plugin properties

| Property | Default | Description |
|---|---|---|
| `flatten.mode` | `resolveCiFriendliesOnly` | What to flatten |
| `flatten.dependency.mode` | `all` | Dependency scopes resolved |
| `updatePomFile` | `true` | Installed/deployed POM is replaced by the flattened one |
| `flatten.pomElements.parent` | `expand` | Parent inlined into the flattened POM |
| `flatten.pomElements.properties` | `keep` | Properties preserved |
| `flatten.pomElements.dependencies` | `resolve` | Dependency versions resolved |
| `flatten.pomElements.distributionManagement` / `repositories` | `remove` | Stripped from the published POM |
| `flatten.pomElements.url` / `scm` / `developers` | `resolve` | Inherited from parent when absent in the child |

### 3.4 Repository / publishing properties

| Property | Default |
|---|---|
| `maven-center-repository.host` | `central.sonatype.com` |
| `ossrh-staging-api.base-uri` | `https://ossrh-staging-api.central.sonatype.com` |
| `ossrh-snapshots-repository.uri` | `https://central.sonatype.com/repository/maven-snapshots/` |
| `ossrh-repository.uri` | `${ossrh-staging-api.base-uri}/service/local/staging/deploy/maven2/` |

Snapshots deploy to `maven-snapshots` on Central; the root POM also declares that snapshot repository in
`<repositories>`, so downstream Microsphere projects can consume snapshot builds directly.

## 4. Resource filtering behaviour

Both `src/main/java` (only `*.properties` / `*.xml`) and `src/main/resources` (all files) are filtered. With
`resource.delimiter=@`, placeholders are substituted at build time:

```properties
# src/main/resources/application.properties (source)
app.version=@project.version@
```

```properties
# after the build (target/classes/application.properties)
app.version=1.0.0
```

> Because filtering applies to `src/main/java`, binary files or non-filtered resources placed next to sources are
> simply not copied — keep binaries in a separate, unfiltered resource directory.

## 5. Enforcer rules inherited

The managed `maven-enforcer-plugin` (activated by `publish`/`release`) enforces:

- `bannedDependencies` with transitive search (`searchTransitive=true`) — declare your banned list;
- `banDuplicatePomDependencyVersions` — the same dependency must not appear twice with different versions.

## 6. Usage examples

### 6.1 Library targeting Java 8, published to Central

```xml
<parent>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-build</artifactId>
    <version>0.3.17</version>
</parent>

<properties>
    <revision>1.2.0-SNAPSHOT</revision>
    <!-- java.version stays 8 -->
</properties>
```

```bash
# local verification, same gate as CI
mvn clean verify -P test,coverage

# release
mvn clean deploy -P publish,ci -Dgpg.skip=true -Drevision=1.2.0
```

`settings.xml` must define server id `ossrh` (used by both `publish` and the legacy `release` profile):

```xml
<server>
    <id>ossrh</id>
    <username>${env.MAVEN_USERNAME}</username>
    <password>${env.MAVEN_PASSWORD}</password>
</server>
```

### 6.2 Application targeting Java 21

```xml
<properties>
    <java.version>21</java.version>
</properties>
```

No other change is needed: on JDK 21 the auto profiles set `release=21`, module `--add-opens` Surefire flags, and
Checkstyle 11.x automatically.

### 6.3 Code examples from the repository's own tests

The parent POM has no runtime API, so "usage examples" are build configurations. The compile-and-test contract is
exercised by `microsphere-build-example` — see
[microsphere-build-example.md](microsphere-build-example.md) for the Java API and test-case examples that validate
every inherited profile.

## 7. Version information and compatibility range

| Microsphere Build | Notable changes | Compatibility |
|---|---|---|
| `0.3.18-SNAPSHOT` (release branch, unreleased) | Maven wrapper 3.9.16; plugin upgrades on top of 0.3.17 | JDK 8–25 |
| `0.3.x` | Continuous plugin version bumps (compiler 3.16.0, surefire/failsafe 3.6.0, checkstyle tool 14.3.0 base, jacoco 0.8.15, central-publishing 0.11.0) | JDK 8–25, Maven 3.9+ |
| `0.2.0` – `0.2.x` | Added `publish` profile with Central Publishing Maven Plugin (OSSRH EOL migration) | JDK 8–25 (25 from 0.2.5) |
| `0.1.x` | Initial parent: `release`/`ci`/`test`/`coverage`/`docs` and JDK auto profiles, CI-friendly `${revision}` | JDK 8–17 era toolchain |

**Compatibility rules**

- The parent is **backward compatible** for consumers within a minor line: upgrades move plugin versions forward
  only; the property names in §3 remain the override points.
- **Compile target vs build JDK**: bytecode is produced for `${java.version}` (default 8); the build JDK must be
  ≥ 8. JDKs 9–25 are supported as build runtimes through the auto-activated profiles.
- Checkstyle tool version is JDK-dependent (9.3 / 10.26.1 / 11.1.0 effective values); pinning
  `puppycrawl-tools-checkstyle.version` yourself may break older JDK builds.
- Consumers must use Maven **3.5+** (CI-friendly versioning); **3.9+** is recommended to match CI.

## 8. Troubleshooting

| Symptom | Cause / fix |
|---|---|
| `The following artifacts could not be resolved ... ${revision}` | You referenced the parent with `${revision}` from outside this repository — use a concrete released version (e.g. `0.3.17`). |
| `Incompatible because this component declares ... Java 11` while building on JDK 8 | A plugin (e.g. recent checkstyle) requires a newer JDK; check `mvn help:active-profiles` — `java8+` should pin Checkstyle 9.3. |
| `module java.base does not open java.lang` in tests | Build is on JDK 16+ without the `java16+` arg line taking effect — ensure you did not override `jvm.argLine` and append your flags instead. |
| Tests skipped unexpectedly | Surefire only picks up `**/*Test.java` and `**/*Tests.java`, and excludes `**/Abstract*.java`. Rename or adjust the includes. |
| Deploy fails on enforcer `bannedDependencies` | A banned (typically vulnerable) transitive dependency is present; `mvn dependency:tree` to locate it. |

---

Next: [microsphere-build-example.md](microsphere-build-example.md) ·
[Developer Guide](../../developer-guide/current/README.md)
