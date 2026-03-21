# Java Version Compatibility

Microsphere Build is designed to work seamlessly across multiple Java LTS versions. This page explains how JDK version detection works, what changes automatically, and how to configure your project for a specific Java target.

---

## Supported Java Versions

| Java Version | LTS | CI Tested | Status |
|---|---|---|---|
| **8** | ✅ | ✅ | Fully supported (default target) |
| **11** | ✅ | ✅ | Fully supported |
| **17** | ✅ | ✅ | Fully supported |
| **21** | ✅ | ✅ | Fully supported |
| **25** | ✅ | ✅ | Fully supported |

The GitHub Actions CI/CD pipeline tests every commit against all five versions using the Temurin distribution.

---

## Setting the Java Target Version

By default, Microsphere Build targets **Java 8**. To compile for a different version, set the `java.version` property in your POM:

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

This single property controls:

| Maven Property | Derives From | Purpose |
|---|---|---|
| `maven.compiler.source` | `${java.version}` | Source compatibility level |
| `maven.compiler.target` | `${java.version}` | Bytecode target level |
| `maven.compiler.release` | `${java.version}` (Java 9+) | Cross-compilation API level |

**Example — targeting Java 21:**

```xml
<properties>
    <java.version>21</java.version>
</properties>
```

When building with JDK 21, Maven automatically activates the `java8+`, `java9+`, `java11+`, `java16+`, and `java17+` profiles.

---

## JDK-Activated Profiles

Maven detects the JDK version at build time and activates the matching profiles. Multiple profiles can be active simultaneously.

### Profile Activation Matrix

| JDK | `java8+` | `java9+` | `java9-15` | `java11+` | `java16+` | `java17+` |
|---|---|---|---|---|---|---|
| **8** | ✅ | — | — | — | — | — |
| **9** | ✅ | ✅ | ✅ | — | — | — |
| **10** | ✅ | ✅ | ✅ | — | — | — |
| **11** | ✅ | ✅ | ✅ | ✅ | — | — |
| **12–15** | ✅ | ✅ | ✅ | ✅ | — | — |
| **16** | ✅ | ✅ | — | ✅ | ✅ | — |
| **17** | ✅ | ✅ | — | ✅ | ✅ | ✅ |
| **21** | ✅ | ✅ | — | ✅ | ✅ | ✅ |
| **25** | ✅ | ✅ | — | ✅ | ✅ | ✅ |

### What Each Profile Changes

#### `java8+` (JDK ≥ 1.8)

- **Javadoc:** Disables lint checks with `-Xdoclint:none` to avoid build failures from missing/incomplete Javadoc
- **Checkstyle:** Uses Checkstyle tool version **9.3** (last version supporting Java 8 bytecode)
- **Asciidoctor:** Uses Asciidoctor Maven Plugin version **2.2.6**

#### `java9+` (JDK ≥ 9)

- **Compiler:** Sets `maven.compiler.release=${java.version}` for cross-compilation correctness
  - The `--release` flag ensures that even when compiling on a newer JDK, only APIs from the target version are used

#### `java9-15` (JDK 9 through 15)

- **Surefire JVM args:** Adds `--illegal-access=permit`
  - Required for frameworks that use deep reflection on JDK internals
  - This flag was removed in Java 16

#### `java11+` (JDK ≥ 11)

- **Javadoc:** Configures the `source` option to match `${maven.compiler.source}` for correct API link generation
- **Checkstyle:** Upgrades to Checkstyle tool version **10.26.1**
- **Asciidoctor:** Upgrades to Asciidoctor Maven Plugin version **3.2.0**

#### `java16+` (JDK ≥ 16)

- **Surefire JVM args:** Adds module-opening flags:
  ```
  --add-opens=java.base/java.lang=ALL-UNNAMED
  --add-opens=java.base/java.lang.invoke=ALL-UNNAMED
  ```
  - Required because Java 16+ enforces strong encapsulation
  - Allows test frameworks (JUnit, Mockito, etc.) to access internal JDK classes via reflection

#### `java17+` (JDK ≥ 17)

- **Checkstyle:** Upgrades to Checkstyle tool version **11.1.0** (requires Java 17+ at runtime)

---

## Checkstyle Version by JDK

Since different Checkstyle releases require different minimum JDK versions, Microsphere Build automatically selects the appropriate version:

| JDK Range | Checkstyle Version | Notes |
|---|---|---|
| 8 – 10 | 9.3 | Last version supporting Java 8 |
| 11 – 16 | 10.26.1 | Requires Java 11+ |
| 17+ | 11.1.0 | Requires Java 17+ |

The base version in the POM (`13.3.0`) is overridden by JDK-activated profiles. The effective version depends on which profiles are active.

---

## Asciidoctor Version by JDK

| JDK Range | Asciidoctor Maven Plugin Version |
|---|---|
| 8 – 10 | 2.2.6 |
| 11+ | 3.2.0 |

---

## Surefire JVM Arguments by JDK

| JDK Range | `jvm.argLine` Value |
|---|---|
| 8 | *(empty)* |
| 9 – 15 | `--illegal-access=permit` |
| 16+ | `--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED` |

The `jvm.argLine` property is appended to the Surefire `argLine` configuration alongside the JaCoCo agent argument:

```xml
<argLine>@{jacoco.argLine} ${jvm.argLine}</argLine>
```

---

## Building with Multiple JDK Versions

### Using the Maven Wrapper

The Maven Wrapper (`mvnw`) does not control which JDK is used—it only controls the Maven version. Set `JAVA_HOME` to switch JDKs:

```bash
# Build with Java 17
export JAVA_HOME=/path/to/jdk-17
./mvnw clean test -P test

# Build with Java 21
export JAVA_HOME=/path/to/jdk-21
./mvnw clean test -P test
```

### CI/CD Matrix Builds

The GitHub Actions workflow tests against multiple JDK versions in a matrix:

```yaml
strategy:
  matrix:
    java: ['8', '11', '17', '21', '25']
steps:
  - uses: actions/setup-java@v5
    with:
      distribution: 'temurin'
      java-version: ${{ matrix.java }}
  - run: ./mvnw --batch-mode test --activate-profiles test,docs,coverage
```

---

## Common Scenarios

### Scenario 1: Library targeting Java 8

```xml
<parent>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-build</artifactId>
    <version>0.2.5-SNAPSHOT</version>
</parent>

<!-- No java.version override needed; defaults to 8 -->
```

Build with any JDK ≥ 8. The `--release` flag (on JDK 9+) ensures no accidental use of newer APIs.

### Scenario 2: Application targeting Java 17

```xml
<parent>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-build</artifactId>
    <version>0.2.5-SNAPSHOT</version>
</parent>

<properties>
    <java.version>17</java.version>
</properties>
```

When built with JDK 17+, the activated profiles set Checkstyle 11.1.0 and add module-opening flags for Surefire.

### Scenario 3: Application targeting Java 21

```xml
<parent>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-build</artifactId>
    <version>0.2.5-SNAPSHOT</version>
</parent>

<properties>
    <java.version>21</java.version>
</properties>
```

Same profiles as Java 17+ apply. JaCoCo, Surefire, and Checkstyle are all configured automatically.

---

## Troubleshooting

### "source/target has been removed" error

If you set `java.version` to a value lower than the minimum supported by your JDK, the compiler may reject it. For example, JDK 21 does not support `--source 6`. Always set `java.version` to 8 or higher.

### "module java.base does not open java.lang" error

Ensure you are building with the `test` profile or that the `java16+` profile is active. These profiles add the required `--add-opens` flags.

### Checkstyle failures on older JDK

If Checkstyle fails with class-version errors, verify that the correct JDK-activated profile is being applied. Run `./mvnw help:active-profiles` to see which profiles are active.
