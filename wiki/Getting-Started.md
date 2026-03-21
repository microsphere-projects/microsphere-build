# Getting Started

This guide walks you through adopting **Microsphere Build** as the parent POM for your Maven project.

---

## Prerequisites

| Requirement | Minimum Version | Notes |
|---|---|---|
| **Java Development Kit (JDK)** | 8 | LTS versions 8, 11, 17, 21, 25 are tested in CI |
| **Apache Maven** | 3.9.x | The Maven Wrapper (`mvnw`) bundles 3.9.13 |
| **Git** | 2.x | Required for the Git Commit ID Plugin |

> **Tip:** You do not need to install Maven globally. The Maven Wrapper scripts (`mvnw` / `mvnw.cmd`) included in the project will download and use the correct Maven version automatically.

---

## Installation

### Step 1 — Set the Parent POM

In your project's root `pom.xml`, declare `microsphere-build` as the parent:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>io.github.microsphere-projects</groupId>
        <artifactId>microsphere-build</artifactId>
        <version>0.2.5-SNAPSHOT</version>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>my-project</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

</project>
```

> Replace the version with the latest release available on [Maven Central](https://central.sonatype.com/artifact/io.github.microsphere-projects/microsphere-build).

### Step 2 — Inherit Default Plugins

Once the parent is set, three plugins are automatically active in every build:

| Plugin | Version | What It Does |
|---|---|---|
| [Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/) | 3.15.0 | Compiles Java sources with the `-parameters` flag |
| [Maven Source Plugin](https://maven.apache.org/plugins/maven-source-plugin/) | 3.4.0 | Attaches a `-sources.jar` during the `package` phase |
| [Flatten Maven Plugin](https://www.mojohaus.org/flatten-maven-plugin/) | 1.7.3 | Flattens the POM for CI-friendly `${revision}` versioning |

No additional configuration is required—just build:

```bash
./mvnw clean package
```

### Step 3 — Choose Your Java Version

By default, the project compiles against **Java 8**. To target a different version, override the `java.version` property:

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

JDK-activated profiles will automatically adjust compiler, Javadoc, Checkstyle, and Surefire settings. See [Java Version Compatibility](Java-Version-Compatibility) for details.

---

## Common Build Commands

### Compile Only

```bash
./mvnw clean compile
```

### Run Unit Tests

```bash
./mvnw clean test
```

### Run Full Test Suite (Unit + Integration + Checkstyle)

```bash
./mvnw clean verify -P test
```

### Generate Code Coverage Report

```bash
./mvnw clean test -P test,coverage
```

After the build, JaCoCo reports are available at `target/site/jacoco/index.html`.

### Generate AsciiDoc Documentation

```bash
./mvnw clean generate-resources -P docs
```

### Publish to Maven Central

```bash
./mvnw clean deploy -P publish
```

> Requires GPG keys and Sonatype credentials. See [CI/CD Integration](CI-CD-Integration) for automation details.

---

## CI-Friendly Versioning

Microsphere Build uses the `${revision}` property with the Flatten Maven Plugin for CI-friendly versioning. The version is defined in the root POM:

```xml
<properties>
    <revision>0.2.5-SNAPSHOT</revision>
</properties>
```

Override it at build time without modifying the POM:

```bash
./mvnw clean package -Drevision=1.0.0
```

The Flatten Maven Plugin ensures that the resolved version appears in the installed/deployed POM, so downstream consumers see the correct version string.

---

## Example Project

The repository includes a minimal example module (`microsphere-build-example`) that demonstrates how the parent POM's profiles and plugins work together.

### Example Service

```java
package io.github.microsphere.example;

public class ExampleService {

    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name must not be null or empty");
        }
        return "Hello, " + name + "!";
    }

    public String getJavaVersion() {
        return System.getProperty("java.specification.version");
    }
}
```

### Example Test

```java
package io.github.microsphere.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExampleServiceTest {

    private final ExampleService service = new ExampleService();

    @Test
    public void testGreet() {
        assertEquals("Hello, World!", service.greet("World"));
    }

    @Test
    public void testGreetWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> service.greet(null));
    }

    @Test
    public void testGetJavaVersion() {
        assertNotNull(service.getJavaVersion());
    }
}
```

### Building the Example

```bash
# Compile and test
./mvnw clean test -P test

# With coverage
./mvnw clean test -P test,coverage

# With docs
./mvnw clean generate-resources -P docs
```

---

## Next Steps

- **[Plugins Management](Plugins-Management)** — Learn about all 20+ managed plugins
- **[Profiles Management](Profiles-Management)** — Understand manual and auto-activated profiles
- **[Java Version Compatibility](Java-Version-Compatibility)** — See JDK-specific settings
- **[Configuration Reference](Configuration-Reference)** — Customize properties and resource filtering
