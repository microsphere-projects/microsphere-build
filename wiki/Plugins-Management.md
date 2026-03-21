# Plugins Management

Microsphere Build pre-configures **20+ Maven plugins** in `<pluginManagement>` so that child projects inherit consistent versions and sensible defaults. Plugins are organized into two categories:

1. **Default Inherited Plugins** — active in every build
2. **Profile-Specific Plugins** — activated only when a named Maven profile is enabled

---

## Default Inherited Plugins

These plugins are declared in the `<build><plugins>` section and are always active:

### Maven Compiler Plugin

| | |
|---|---|
| **Group ID** | `org.apache.maven.plugins` |
| **Artifact ID** | `maven-compiler-plugin` |
| **Version** | 3.15.0 |
| **Documentation** | [maven.apache.org](https://maven.apache.org/plugins/maven-compiler-plugin/) |

Compiles Java source files. The `-parameters` compiler argument is enabled by default so that method parameter names are preserved in the bytecode (useful for frameworks that rely on reflection).

**Inherited configuration:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.15.0</version>
    <configuration>
        <compilerArgs>
            <compilerArg>-parameters</compilerArg>
        </compilerArgs>
        <source>${maven.compiler.source}</source>   <!-- defaults to 8 -->
        <target>${maven.compiler.target}</target>    <!-- defaults to 8 -->
        <encoding>${file.encoding}</encoding>
    </configuration>
</plugin>
```

**Example — targeting Java 17:**

```xml
<properties>
    <java.version>17</java.version>
</properties>
<!-- No plugin configuration needed; the parent handles it -->
```

---

### Maven Source Plugin

| | |
|---|---|
| **Group ID** | `org.apache.maven.plugins` |
| **Artifact ID** | `maven-source-plugin` |
| **Version** | 3.4.0 |
| **Documentation** | [maven.apache.org](https://maven.apache.org/plugins/maven-source-plugin/) |

Attaches a `-sources.jar` artifact during the `package` phase using the `jar-no-fork` goal (avoids a duplicate lifecycle fork).

**Inherited configuration:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-source-plugin</artifactId>
    <version>3.4.0</version>
    <executions>
        <execution>
            <id>attach-sources</id>
            <goals>
                <goal>jar-no-fork</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
</plugin>
```

---

### Flatten Maven Plugin

| | |
|---|---|
| **Group ID** | `org.codehaus.mojo` |
| **Artifact ID** | `flatten-maven-plugin` |
| **Version** | 1.7.3 |
| **Documentation** | [mojohaus.org](https://www.mojohaus.org/flatten-maven-plugin/) |

Resolves CI-friendly properties such as `${revision}` in the POM before it is installed or deployed. This is essential for multi-module builds that share a single version property.

**Key settings:**

| Property | Default | Description |
|---|---|---|
| `flatten.dependency.mode` | `all` | Resolve all dependency scopes |
| `flatten.mode` | `resolveCiFriendliesOnly` | Only resolve CI-friendly placeholders |
| `updatePomFile` | `true` | Replace the project POM with the flattened version |
| `flatten.pomElements.parent` | `expand` | Inline the parent into the flattened POM |
| `flatten.pomElements.properties` | `keep` | Preserve properties in the output |
| `flatten.pomElements.dependencies` | `resolve` | Resolve dependency versions |
| `flatten.pomElements.repositories` | `remove` | Remove repository declarations |

---

## Profile-Specific Plugins

The following plugins are configured in `<pluginManagement>` and activated when you enable the corresponding Maven profile.

### Profile: `publish` (Recommended since 0.2.0)

Activated with `-P publish`. Publishes artifacts to Maven Central via the [Central Publishing Portal](https://central.sonatype.org/publish/publish-portal-maven/).

| Plugin | Version | Purpose |
|---|---|---|
| [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) | 3.12.0 | Generates and attaches `-javadoc.jar` |
| [Maven Release Plugin](https://maven.apache.org/plugins/maven-release-plugin/) | 3.3.1 | Automates version bumps, tags, and deploys |
| [Maven Enforcer Plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/) | 3.6.2 | Validates dependency rules |
| [Maven GPG Plugin](https://maven.apache.org/plugins/maven-gpg-plugin/) | 3.2.8 | Signs artifacts with GPG keys |
| [Git Commit ID Plugin](https://github.com/git-commit-id/git-commit-id-maven-plugin) | 4.9.10 | Embeds Git metadata in `git.properties` |
| [Central Publishing Maven Plugin](https://central.sonatype.com/artifact/org.sonatype.central/central-publishing-maven-plugin) | 0.10.0 | Deploys to Maven Central |

**Example:**

```bash
./mvnw clean deploy -P publish
```

> **Note:** As of June 30, 2025, OSSRH has reached end of life. The `publish` profile uses the Central Publishing Portal and is the recommended approach.

---

### Profile: `release` (Legacy)

Activated with `-P release`. Uses the legacy OSSRH Staging API.

| Plugin | Version | Purpose |
|---|---|---|
| [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) | 3.12.0 | Generates and attaches `-javadoc.jar` |
| [Maven Release Plugin](https://maven.apache.org/plugins/maven-release-plugin/) | 3.3.1 | Automates version bumps, tags, and deploys |
| [Maven Enforcer Plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/) | 3.6.2 | Validates dependency rules |
| [Maven GPG Plugin](https://maven.apache.org/plugins/maven-gpg-plugin/) | 3.2.8 | Signs artifacts with GPG keys |
| [Git Commit ID Plugin](https://github.com/git-commit-id/git-commit-id-maven-plugin) | 4.9.10 | Embeds Git metadata |
| [Nexus Staging Maven Plugin](https://github.com/sonatype/nexus-maven-plugins) | 1.7.0 | Stages and closes on OSSRH |

**Example:**

```bash
./mvnw clean deploy -P release
```

> **Deprecated:** Prefer the `publish` profile for new projects. The `release` profile is retained for backward compatibility. See [Publishing By Using the Portal OSSRH Staging API](https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/) for more details.

---

### Profile: `ci`

Activated with `-P ci`. Designed for CI environments that sign artifacts using environment variables instead of local GPG keyrings.

| Plugin | Version | Purpose |
|---|---|---|
| [Sign Maven Plugin](https://www.simplify4u.org/sign-maven-plugin/) | 1.1.0 | Signs artifacts using environment variables |

**Required environment variables:**

| Variable | Description |
|---|---|
| `SIGN_KEY` | Content of the private key (not a file path) |
| `SIGN_KEY_ID` | Long-format key ID |
| `SIGN_KEY_PASS` | Key passphrase |

**Example:**

```bash
export SIGN_KEY="$(cat my-private-key.asc)"
export SIGN_KEY_ID="ABCDEF1234567890"
export SIGN_KEY_PASS="my-passphrase"

./mvnw clean deploy -P publish,ci
```

---

### Profile: `test`

Activated with `-P test`. Runs unit tests, integration tests, and code-style checks.

| Plugin | Version | Purpose |
|---|---|---|
| [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/) | 3.5.5 | Runs unit tests (`*Test.java`, `*Tests.java`) |
| [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/) | 3.5.5 | Runs integration tests (`*IT.java`) |
| [Maven Checkstyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/) | 3.6.0 | Validates code style |

**Surefire configuration details:**

| Setting | Value |
|---|---|
| Included patterns | `**/*Tests.java`, `**/*Test.java` |
| Excluded patterns | `**/Abstract*.java` |
| JVM arguments | `@{jacoco.argLine} ${jvm.argLine}` (supports JaCoCo + JDK-specific flags) |

**Example:**

```bash
# Unit tests + checkstyle
./mvnw clean verify -P test

# Unit + integration tests + checkstyle
./mvnw clean verify -P test
```

---

### Profile: `coverage`

Activated with `-P coverage`. Instruments code with JaCoCo for coverage reporting.

| Plugin | Version | Purpose |
|---|---|---|
| [JaCoCo Maven Plugin](https://www.eclemma.org/jacoco/) | 0.8.14 | Agent instrumentation and report generation |

JaCoCo runs in two phases:
1. **`prepare-agent`** — injects the JaCoCo agent into the JVM arguments
2. **`report`** — generates the HTML/XML coverage report during the `test` phase

**Example:**

```bash
./mvnw clean test -P test,coverage

# View the report
open target/site/jacoco/index.html
```

---

### Profile: `docs`

Activated with `-P docs`. Generates project documentation from AsciiDoc and DocBook sources.

| Plugin | Version | Purpose |
|---|---|---|
| [Asciidoctor Maven Plugin](https://github.com/asciidoctor/asciidoctor-maven-plugin) | 3.2.0 (Java 11+) / 2.2.6 (Java 8+) | Converts AsciiDoc to HTML5 |
| [Docbkx Maven Plugin](https://github.com/mimil/docbkx-tools) | 2.0.17 | Processes DocBook XML |
| [Build Helper Maven Plugin](https://www.mojohaus.org/build-helper-maven-plugin/) | 3.6.1 | Build utilities |

> **Note:** The Asciidoctor plugin version is automatically selected based on the JDK: version 2.2.6 for Java 8–10 and version 3.2.0 for Java 11+.

**Example:**

```bash
./mvnw clean generate-resources -P docs
```

---

## Plugin Management Reference

The following table lists every plugin declared in `<pluginManagement>`:

| Plugin | Version | Default Active | Profile(s) |
|---|---|---|---|
| Maven Compiler Plugin | 3.15.0 | ✅ | — |
| Maven Source Plugin | 3.4.0 | ✅ | — |
| Flatten Maven Plugin | 1.7.3 | ✅ | — |
| Maven JavaDoc Plugin | 3.12.0 | ❌ | `publish`, `release` |
| Maven Release Plugin | 3.3.1 | ❌ | `publish`, `release` |
| Maven Enforcer Plugin | 3.6.2 | ❌ | `publish`, `release` |
| Maven GPG Plugin | 3.2.8 | ❌ | `publish`, `release` |
| Git Commit ID Plugin | 4.9.10 | ❌ | `publish`, `release` |
| Central Publishing Maven Plugin | 0.10.0 | ❌ | `publish` |
| Nexus Staging Maven Plugin | 1.7.0 | ❌ | `release` |
| Sign Maven Plugin | 1.1.0 | ❌ | `ci` |
| Maven Surefire Plugin | 3.5.5 | ❌ | `test` |
| Maven Failsafe Plugin | 3.5.5 | ❌ | `test` |
| Maven Checkstyle Plugin | 3.6.0 | ❌ | `test` |
| JaCoCo Maven Plugin | 0.8.14 | ❌ | `coverage` |
| Asciidoctor Maven Plugin | 3.2.0 / 2.2.6 | ❌ | `docs` |
| Docbkx Maven Plugin | 2.0.17 | ❌ | `docs` |
| Build Helper Maven Plugin | 3.6.1 | ❌ | `docs` |
| Maven JAR Plugin | 3.5.0 | ❌ | (managed) |
| Maven WAR Plugin | 3.5.1 | ❌ | (managed) |
| Maven Shade Plugin | 3.6.2 | ❌ | (managed) |
| Maven Deploy Plugin | 3.1.4 | ❌ | (managed) |
| Lombok Maven Plugin | 1.18.20.0 | ❌ | (managed) |

---

## Overriding Plugin Versions

Child projects can override any managed plugin version by redefining the corresponding property:

```xml
<properties>
    <!-- Upgrade Maven Compiler Plugin -->
    <maven-compiler-plugin.version>3.16.0</maven-compiler-plugin.version>
</properties>
```

See [Configuration Reference](Configuration-Reference) for all available version properties.

---

## Version Compatibility

| Microsphere Build | Maven Compiler | Maven Surefire | JaCoCo | Checkstyle Tool |
|---|---|---|---|---|
| 0.2.5-SNAPSHOT | 3.15.0 | 3.5.5 | 0.8.14 | 9.3 – 13.3.0 (varies by JDK) |
| 0.2.0+ | 3.13.0+ | 3.5.x | 0.8.x | 9.3+ |
