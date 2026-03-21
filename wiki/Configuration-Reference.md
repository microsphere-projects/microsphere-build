# Configuration Reference

This page provides a complete reference of all configurable properties, resource settings, and customization options in Microsphere Build.

---

## Core Properties

These properties control fundamental build behavior and can be overridden in child POMs.

### Version and Encoding

| Property | Default | Description |
|---|---|---|
| `revision` | `0.2.5-SNAPSHOT` | Project version (CI-friendly, resolved by Flatten Plugin) |
| `java.version` | `8` | Target Java version; drives `source`, `target`, and `release` settings |
| `project.build.sourceEncoding` | `UTF-8` | Source file encoding |
| `project.reporting.outputEncoding` | `UTF-8` | Report output encoding |
| `resource.delimiter` | `@` | Delimiter for resource filtering (e.g., `@property@`) |

### Compiler Properties

| Property | Default | Description |
|---|---|---|
| `maven.compiler.source` | `${java.version}` | Java source compatibility level |
| `maven.compiler.target` | `${java.version}` | Java bytecode target level |
| `maven.compiler.release` | `${java.version}` (Java 9+ only) | Cross-compilation API level |

**Example — targeting Java 17:**

```xml
<properties>
    <java.version>17</java.version>
    <!-- maven.compiler.source, target, and release are set automatically -->
</properties>
```

---

## Plugin Version Properties

Override any of these to change the version of a managed plugin.

| Property | Default | Plugin |
|---|---|---|
| `maven-compiler-plugin.version` | `3.15.0` | Maven Compiler Plugin |
| `maven-source-plugin.version` | `3.4.0` | Maven Source Plugin |
| `maven-javadoc-plugin.version` | `3.12.0` | Maven JavaDoc Plugin |
| `maven-jar-plugin.version` | `3.5.0` | Maven JAR Plugin |
| `maven-war-plugin.version` | `3.5.1` | Maven WAR Plugin |
| `maven-share-plugin.version` | `3.6.2` | Maven Shade Plugin |
| `maven-deploy-plugin.version` | `3.1.4` | Maven Deploy Plugin |
| `maven-release-plugin.version` | `3.3.1` | Maven Release Plugin |
| `maven-surefire-plugin.version` | `3.5.5` | Maven Surefire Plugin |
| `maven-failsafe-plugin.version` | `3.5.5` | Maven Failsafe Plugin |
| `maven-checkstyle-plugin.version` | `3.6.0` | Maven Checkstyle Plugin |
| `maven-enforcer-plugin.version` | `3.6.2` | Maven Enforcer Plugin |
| `maven-gpg-plugin.version` | `3.2.8` | Maven GPG Plugin |
| `maven-lombok-plugin.version` | `1.18.20.0` | Lombok Maven Plugin |
| `jacoco-maven-plugin.version` | `0.8.14` | JaCoCo Maven Plugin |
| `flatten-maven-plugin.version` | `1.7.3` | Flatten Maven Plugin |
| `sign-maven-plugin.version` | `1.1.0` | Sign Maven Plugin |
| `nexus-staging-maven-plugin.version` | `1.7.0` | Nexus Staging Plugin |
| `asciidoctor-maven-plugin.version` | `3.2.0` | Asciidoctor Maven Plugin |
| `docbkx-maven-plugin.version` | `2.0.17` | Docbkx Maven Plugin |
| `build-helper-maven-plugin.version` | `3.6.1` | Build Helper Maven Plugin |
| `git-commit-id-plugin.version` | `4.9.10` | Git Commit ID Plugin |
| `central-publishing-maven-plugin.version` | `0.10.0` | Central Publishing Plugin |
| `puppycrawl-tools-checkstyle.version` | `13.3.0` | Checkstyle tool (overridden by JDK profiles) |

**Example — upgrading JaCoCo:**

```xml
<properties>
    <jacoco-maven-plugin.version>0.8.15</jacoco-maven-plugin.version>
</properties>
```

---

## Flatten Maven Plugin Properties

These properties control how the Flatten Maven Plugin transforms the POM before installation/deployment.

| Property | Default | Description |
|---|---|---|
| `flatten.dependency.mode` | `all` | Which dependency scopes to resolve |
| `flatten.mode` | `resolveCiFriendliesOnly` | What to flatten (`resolveCiFriendliesOnly`, `bom`, `defaults`, etc.) |
| `updatePomFile` | `true` | Replace the project POM with the flattened version |
| `flatten.pomElements.parent` | `expand` | How to handle the `<parent>` element (`expand`, `keep`, `remove`) |
| `flatten.pomElements.properties` | `keep` | How to handle `<properties>` (`keep`, `remove`, `interpolate`) |
| `flatten.pomElements.dependencyManagement` | `interpolate` | How to handle `<dependencyManagement>` |
| `flatten.pomElements.dependencies` | `resolve` | How to handle `<dependencies>` |
| `flatten.pomElements.distributionManagement` | `remove` | How to handle `<distributionManagement>` |
| `flatten.pomElements.repositories` | `remove` | How to handle `<repositories>` |

**Example — keeping repositories in the flattened POM:**

```xml
<properties>
    <flatten.pomElements.repositories>keep</flatten.pomElements.repositories>
</properties>
```

---

## Checkstyle Properties

| Property | Default | Description |
|---|---|---|
| `disable.checks` | `true` | When `true`, Checkstyle is skipped. The `test` profile runs Checkstyle with `skip=false`. |
| `maven-checkstyle-plugin.failsOnError` | `true` | Fail the build on Checkstyle errors |
| `maven-checkstyle-plugin.failOnViolation` | `true` | Fail the build on Checkstyle violations |
| `maven-checkstyle-plugin.includeTestSourceDirectory` | `true` | Include test sources in Checkstyle analysis |
| `puppycrawl-tools-checkstyle.version` | `13.3.0` (overridden by JDK profiles) | Checkstyle tool version |

**Checkstyle configuration files:**

| File | Purpose |
|---|---|
| `checkstyle/checkstyle.xml` | Main Checkstyle rules |
| `checkstyle/checkstyle-suppressions.xml` | Suppression rules for known exceptions |

**Example — disabling Checkstyle failure on violations:**

```xml
<properties>
    <maven-checkstyle-plugin.failOnViolation>false</maven-checkstyle-plugin.failOnViolation>
</properties>
```

---

## JVM Argument Properties

| Property | Default | Description |
|---|---|---|
| `jvm.argLine` | *(empty)* | Additional JVM arguments for Surefire/Failsafe (set by JDK profiles) |
| `jacoco.argLine` | *(empty)* | JaCoCo agent arguments (set by JaCoCo `prepare-agent` goal) |

These are combined in the Surefire configuration:

```xml
<argLine>@{jacoco.argLine} ${jvm.argLine}</argLine>
```

**Example — adding custom JVM arguments:**

```xml
<properties>
    <jvm.argLine>-Xmx2g -XX:+UseG1GC</jvm.argLine>
</properties>
```

> **Warning:** If you override `jvm.argLine`, the JDK-profile defaults (`--illegal-access=permit` or `--add-opens`) will be replaced. Append to them instead:
> ```xml
> <jvm.argLine>--add-opens=java.base/java.lang=ALL-UNNAMED -Xmx2g</jvm.argLine>
> ```

---

## Sonar Properties

| Property | Default | Description |
|---|---|---|
| `sonar.java.coveragePlugin` | `jacoco` | Coverage tool for SonarQube |
| `sonar.dynamicAnalysis` | `reuseReports` | Reuse existing coverage reports |
| `sonar.jacoco.reportPath` | `${project.basedir}/../target/jacoco.exec` | Path to JaCoCo execution data |
| `sonar.language` | `java` | Language for SonarQube analysis |

---

## Repository Properties

| Property | Default | Description |
|---|---|---|
| `maven-center-repository.host` | `central.sonatype.com` | Maven Central host |
| `ossrh-snapshots-repository.uri` | `https://central.sonatype.com/repository/maven-snapshots/` | Snapshot repository URL |
| `ossrh-staging-api.base-uri` | `https://ossrh-staging-api.central.sonatype.com` | OSSRH Staging API base URL |
| `ossrh-repository.uri` | `${ossrh-staging-api.base-uri}/service/local/staging/deploy/maven2/` | Release repository URL |

---

## Documentation Properties

| Property | Default | Description |
|---|---|---|
| `docs.main` | `${project.artifactId}` | Main documentation identifier |
| `docs.resources.dir` | `${project.build.directory}/build-docs` | Documentation resources directory |
| `docs.classes.dir` | `${project.build.directory}/build-docs-classes` | Documentation classes directory |

---

## Resource Filtering

Microsphere Build configures resource filtering for two directories:

### `src/main/java` Resources

Only `.properties` and `.xml` files in the Java source tree are filtered and included:

```xml
<resource>
    <directory>src/main/java</directory>
    <filtering>true</filtering>
    <includes>
        <include>**/*.properties</include>
        <include>**/*.xml</include>
    </includes>
</resource>
```

### `src/main/resources`

All files in the standard resources directory are filtered:

```xml
<resource>
    <directory>src/main/resources</directory>
    <filtering>true</filtering>
    <includes>
        <include>**/*</include>
    </includes>
</resource>
```

**Resource filtering** replaces `${property}` and `@property@` placeholders in resource files with their resolved values during the build.

**Example — `application.properties`:**

```properties
app.version=@project.version@
app.name=@project.name@
```

After filtering:

```properties
app.version=0.2.5-SNAPSHOT
app.name=Microsphere :: Build :: Example
```

---

## Distribution Management

### Snapshot Repository

```xml
<snapshotRepository>
    <id>ossrh</id>
    <url>https://central.sonatype.com/repository/maven-snapshots/</url>
</snapshotRepository>
```

### Release Repository

```xml
<repository>
    <id>ossrh</id>
    <url>https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/</url>
</repository>
```

> The `publish` profile uses the Central Publishing Maven Plugin instead of direct repository deployment.

---

## SCM Configuration

```xml
<scm>
    <url>git@github.com:microsphere-projects/microsphere-build.git</url>
    <connection>scm:git:${project.scm.url}</connection>
    <developerConnection>scm:git:${project.scm.url}</developerConnection>
    <tag>HEAD</tag>
</scm>
```

Child projects should override the SCM URL to point to their own repository:

```xml
<scm>
    <url>git@github.com:microsphere-projects/my-project.git</url>
    <connection>scm:git:${project.scm.url}</connection>
    <developerConnection>scm:git:${project.scm.url}</developerConnection>
    <tag>HEAD</tag>
</scm>
```

---

## Maven JAR Plugin Configuration

The managed JAR plugin configuration adds manifest entries:

```xml
<archive>
    <addMavenDescriptor>true</addMavenDescriptor>
    <index>true</index>
    <manifest>
        <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>
        <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
    </manifest>
    <manifestEntries>
        <Specification-Version>${project.version}</Specification-Version>
        <Implementation-Version>${project.version}</Implementation-Version>
    </manifestEntries>
</archive>
```

This ensures that every JAR includes specification and implementation version information in its `MANIFEST.MF`.

---

## Git Commit ID Plugin Configuration

When the `publish` or `release` profile is active, the Git Commit ID Plugin generates a `git.properties` file with the following properties:

| Property | Example |
|---|---|
| `git.tags` | `v0.2.4` |
| `git.branch` | `main` |
| `git.build.time` | `2025-06-15T10:30:00+0000` |
| `git.build.user.email` | `developer@example.com` |
| `git.build.version` | `0.2.4` |
| `git.commit.time` | `2025-06-15T10:25:00+0000` |
| `git.commit.id.full` | `abc123def456...` |
| `git.commit.user.email` | `developer@example.com` |
| `git.remote.origin.url` | `git@github.com:microsphere-projects/microsphere-build.git` |

The file is generated at `${project.build.outputDirectory}/git.properties`.

---

## Version Compatibility

| Property / Feature | Since Version |
|---|---|
| `revision` CI-friendly versioning | 0.1.0 |
| `publish` profile | 0.2.0 |
| `central-publishing-maven-plugin` | 0.2.0 |
| JDK 25 support | 0.2.5-SNAPSHOT |
| All JDK-activated profiles | 0.1.0+ |
