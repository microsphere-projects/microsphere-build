# Profiles Management

Microsphere Build defines **12 Maven profiles** that control which plugins run and how the build behaves. Profiles fall into two categories:

1. **Manual Profiles** — activated explicitly with `-P <name>`
2. **JDK-Activated Profiles** — activated automatically based on the detected JDK version

---

## Manual Profiles

### `publish` (Recommended since 0.2.0)

Publishes signed artifacts to **Maven Central** via the [Central Publishing Portal](https://central.sonatype.org/publish/publish-portal-maven/).

**Activation:**

```bash
./mvnw clean deploy -P publish
```

**Plugins activated:**

| Plugin | Version |
|---|---|
| Maven JavaDoc Plugin | 3.12.0 |
| Maven Release Plugin | 3.3.1 |
| Maven Enforcer Plugin | 3.6.2 |
| Maven GPG Plugin | 3.2.8 |
| Git Commit ID Plugin | 4.9.10 |
| Central Publishing Maven Plugin | 0.10.0 |

**Prerequisites:**
- GPG key installed and configured
- Sonatype credentials in `~/.m2/settings.xml` with server ID `ossrh`
- `autoPublish` is set to `true` by default

**Example `settings.xml`:**

```xml
<settings>
    <servers>
        <server>
            <id>ossrh</id>
            <username>${env.MAVEN_USERNAME}</username>
            <password>${env.MAVEN_PASSWORD}</password>
        </server>
    </servers>
</settings>
```

> **Note:** As of June 30, 2025, OSSRH has reached end of life and has been shut down. All OSSRH namespaces have been migrated to the [Central Publisher Portal](https://central.sonatype.org/pages/ossrh-eol/). The `publish` profile is the recommended approach for all new and existing projects.

---

### `release` (Legacy)

Publishes signed artifacts via the **OSSRH Staging API** (Nexus).

**Activation:**

```bash
./mvnw clean deploy -P release
```

**Plugins activated:**

| Plugin | Version |
|---|---|
| Maven JavaDoc Plugin | 3.12.0 |
| Maven Release Plugin | 3.3.1 |
| Maven Enforcer Plugin | 3.6.2 |
| Maven GPG Plugin | 3.2.8 |
| Git Commit ID Plugin | 4.9.10 |
| Nexus Staging Maven Plugin | 1.7.0 |

> **Deprecated:** This profile is retained for backward compatibility. Prefer `publish` for new work. See [Publishing By Using the Portal OSSRH Staging API](https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/).

---

### `ci`

Designed for **CI/CD environments** where GPG signing uses environment variables rather than a local keyring.

**Activation:**

```bash
./mvnw clean deploy -P publish,ci
```

**Plugins activated:**

| Plugin | Version |
|---|---|
| Sign Maven Plugin | 1.1.0 |

**Required environment variables:**

| Variable | Description | Example |
|---|---|---|
| `SIGN_KEY` | Private key content (not a file path) | `$(cat my-key.asc)` |
| `SIGN_KEY_ID` | Long-format key ID | `ABCDEF1234567890` |
| `SIGN_KEY_PASS` | Key passphrase | `my-secret` |

---

### `test`

Runs unit tests, integration tests, and code-style checks.

**Activation:**

```bash
./mvnw clean verify -P test
```

**Plugins activated:**

| Plugin | Version | Phase |
|---|---|---|
| Maven Surefire Plugin | 3.5.5 | `test` |
| Maven Failsafe Plugin | 3.5.5 | `integration-test`, `verify` |
| Maven Checkstyle Plugin | 3.6.0 | `validate` |

**Checkstyle settings:**

| Property | Default | Description |
|---|---|---|
| `disable.checks` | `true` | Set to `false` to enable Checkstyle (enabled in the `test` profile) |
| `maven-checkstyle-plugin.failsOnError` | `true` | Fail the build on Checkstyle errors |
| `maven-checkstyle-plugin.failOnViolation` | `true` | Fail the build on Checkstyle violations |
| `maven-checkstyle-plugin.includeTestSourceDirectory` | `true` | Also check test sources |

**Checkstyle configuration files:**
- `checkstyle/checkstyle.xml` — main rules
- `checkstyle/checkstyle-suppressions.xml` — suppression rules

---

### `coverage`

Instruments code for **JaCoCo code coverage** reporting.

**Activation:**

```bash
./mvnw clean test -P test,coverage
```

**Plugins activated:**

| Plugin | Version | Goals |
|---|---|---|
| JaCoCo Maven Plugin | 0.8.14 | `prepare-agent`, `report` |

**How it works:**
1. `prepare-agent` injects the JaCoCo Java agent into the Surefire JVM arguments via the `jacoco.argLine` property.
2. After the `test` phase, the `report` goal generates coverage reports.

**Output:**
- HTML report: `target/site/jacoco/index.html`
- XML report: `target/site/jacoco/jacoco.xml`

**Integration with Sonar:**

```xml
<properties>
    <sonar.java.coveragePlugin>jacoco</sonar.java.coveragePlugin>
    <sonar.jacoco.reportPath>${project.basedir}/../target/jacoco.exec</sonar.jacoco.reportPath>
</properties>
```

---

### `docs`

Generates project documentation from **AsciiDoc** and **DocBook XML** sources.

**Activation:**

```bash
./mvnw clean generate-resources -P docs
```

**Plugins activated:**

| Plugin | Version | Goal |
|---|---|---|
| Asciidoctor Maven Plugin | 3.2.0 / 2.2.6 | `process-asciidoc` (HTML5 backend) |
| Docbkx Maven Plugin | 2.0.17 | DocBook XML processing |
| Build Helper Maven Plugin | 3.6.1 | Build utilities |

**Default AsciiDoc source directory:** `src/docs/asciidoc`  
**Default output directory:** `${project.build.directory}/docs`

> **Note:** The Asciidoctor Maven Plugin version is automatically set based on the JDK version — 2.2.6 for Java 8–10, and 3.2.0 for Java 11+.

**Example module configuration:**

```xml
<profile>
    <id>docs</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.asciidoctor</groupId>
                <artifactId>asciidoctor-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>asciidoc-to-html</id>
                        <phase>generate-resources</phase>
                        <goals>
                            <goal>process-asciidoc</goal>
                        </goals>
                        <configuration>
                            <sourceDirectory>${project.basedir}/src/docs/asciidoc</sourceDirectory>
                            <outputDirectory>${project.build.directory}/docs</outputDirectory>
                            <backend>html5</backend>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>
```

---

## JDK-Activated Profiles

These profiles are activated **automatically** based on the JDK version detected by Maven. You do not need to specify them with `-P`.

### `java8+`

| | |
|---|---|
| **Activation** | JDK `[1.8, ∞)` |
| **Effect** | Disables Javadoc lint (`-Xdoclint:none`); sets Checkstyle to 9.3; uses Asciidoctor 2.2.6 |

**Properties set:**

```xml
<puppycrawl-tools-checkstyle.version>9.3</puppycrawl-tools-checkstyle.version>
<asciidoctor-maven-plugin.version>2.2.6</asciidoctor-maven-plugin.version>
```

**Plugin configuration:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <configuration>
        <doclint>none</doclint>
    </configuration>
</plugin>
```

---

### `java9+`

| | |
|---|---|
| **Activation** | JDK `[9, ∞)` |
| **Effect** | Sets `maven.compiler.release` to match `java.version` |

**Properties set:**

```xml
<maven.compiler.release>${java.version}</maven.compiler.release>
```

> This ensures cross-compilation correctness. The `release` flag tells `javac` to compile against the API of the specified Java version, even when running on a newer JDK.

---

### `java11+`

| | |
|---|---|
| **Activation** | JDK `[11, ∞)` |
| **Effect** | Configures Javadoc `source`; upgrades Checkstyle to 10.26.1; uses Asciidoctor 3.2.0 |

**Properties set:**

```xml
<puppycrawl-tools-checkstyle.version>10.26.1</puppycrawl-tools-checkstyle.version>
<asciidoctor-maven-plugin.version>3.2.0</asciidoctor-maven-plugin.version>
```

**Plugin configuration:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <configuration>
        <source>${maven.compiler.source}</source>
    </configuration>
</plugin>
```

---

### `java9-15`

| | |
|---|---|
| **Activation** | JDK `[9, 15]` |
| **Effect** | Adds `--illegal-access=permit` to Surefire JVM arguments |

**Properties set:**

```xml
<jvm.argLine>--illegal-access=permit</jvm.argLine>
```

> This flag allows reflective access to internal JDK classes, which was permitted but deprecated in Java 9–15 and removed in Java 16.

---

### `java16+`

| | |
|---|---|
| **Activation** | JDK `[16, ∞)` |
| **Effect** | Adds `--add-opens` flags for `java.base/java.lang` and `java.base/java.lang.invoke` |

**Properties set:**

```xml
<jvm.argLine>
    --add-opens=java.base/java.lang=ALL-UNNAMED
    --add-opens=java.base/java.lang.invoke=ALL-UNNAMED
</jvm.argLine>
```

> Starting with Java 16, strong encapsulation is enforced by default. These flags open specific packages for deep reflection used by frameworks and testing libraries.

---

### `java17+`

| | |
|---|---|
| **Activation** | JDK `[17, ∞)` |
| **Effect** | Upgrades Checkstyle to 11.1.0 |

**Properties set:**

```xml
<puppycrawl-tools-checkstyle.version>11.1.0</puppycrawl-tools-checkstyle.version>
```

---

## Profile Activation Summary

| Profile | Type | Activation | Key Effect |
|---|---|---|---|
| `publish` | Manual | `-P publish` | Deploy to Maven Central (Central Publishing Portal) |
| `release` | Manual | `-P release` | Deploy to Maven Central (OSSRH Staging API, legacy) |
| `ci` | Manual | `-P ci` | Sign artifacts with env-var-based keys |
| `test` | Manual | `-P test` | Run Surefire, Failsafe, and Checkstyle |
| `coverage` | Manual | `-P coverage` | JaCoCo coverage instrumentation and reporting |
| `docs` | Manual | `-P docs` | AsciiDoc and DocBook documentation generation |
| `java8+` | Auto | JDK ≥ 1.8 | Disable Javadoc lint; Checkstyle 9.3 |
| `java9+` | Auto | JDK ≥ 9 | Set `maven.compiler.release` |
| `java11+` | Auto | JDK ≥ 11 | Javadoc `source` config; Checkstyle 10.26.1 |
| `java9-15` | Auto | JDK 9–15 | `--illegal-access=permit` |
| `java16+` | Auto | JDK ≥ 16 | `--add-opens` for `java.lang` |
| `java17+` | Auto | JDK ≥ 17 | Checkstyle 11.1.0 |

---

## Combining Profiles

Profiles can be combined freely. Common combinations:

```bash
# Test with coverage
./mvnw clean verify -P test,coverage

# Publish with CI signing
./mvnw clean deploy -P publish,ci

# Test with docs and coverage
./mvnw clean verify -P test,docs,coverage
```

---

## Version Compatibility

| Microsphere Build | Profiles Available |
|---|---|
| 0.2.0+ | `publish`, `release`, `ci`, `test`, `coverage`, `docs`, `java8+`, `java9+`, `java11+`, `java9-15`, `java16+`, `java17+` |
| 0.1.x | `release`, `ci`, `test`, `coverage`, `docs`, `java8+`, `java9+`, `java11+` |
