# Microsphere Build

**Microsphere Build** is a Java Maven parent POM that provides centralized build configuration, plugin management, and multi-JDK support for the [Microsphere Projects](https://github.com/microsphere-projects) ecosystem.

By inheriting from `microsphere-build`, child projects such as [Microsphere Java](https://github.com/microsphere-projects/microsphere-java) and [Microsphere Spring](https://github.com/microsphere-projects/microsphere-spring) automatically receive a consistent, production-ready build setup—including compilation, testing, code-style checks, code coverage, documentation generation, artifact signing, and publishing to Maven Central.

| | |
|---|---|
| **Group ID** | `io.github.microsphere-projects` |
| **Artifact ID** | `microsphere-build` |
| **Latest Version** | See [Maven Central](https://central.sonatype.com/artifact/io.github.microsphere-projects/microsphere-build) |
| **License** | Apache License 2.0 |
| **Java Versions** | 8, 11, 17, 21, 25 |
| **Maven Version** | 3.9.13 (via Maven Wrapper) |

---

## Key Features

| Feature | Description |
|---|---|
| **Multi-JDK Support** | Auto-activated profiles for Java 8, 9, 11, 16, 17 and above |
| **Plugin Management** | 20+ pre-configured Maven plugins with consistent versions |
| **Profile Management** | Six manual profiles (`publish`, `release`, `ci`, `test`, `coverage`, `docs`) and six JDK-activated profiles |
| **CI/CD Ready** | GitHub Actions workflows for building, testing, and publishing |
| **Maven Central Publishing** | One-command publishing via the `publish` profile |
| **Code Quality** | Checkstyle, JaCoCo code coverage, and Maven Enforcer integration |
| **Flatten POM** | CI-friendly `${revision}` versioning with Flatten Maven Plugin |

---

## Wiki Pages

| Page | Description |
|---|---|
| [Getting Started](Getting-Started) | Prerequisites, installation, and basic usage examples |
| [Plugins Management](Plugins-Management) | All managed plugins—versions, configuration, and examples |
| [Profiles Management](Profiles-Management) | Manual and JDK-activated profiles with detailed settings |
| [Java Version Compatibility](Java-Version-Compatibility) | JDK support matrix, auto-activated profiles, and version-specific behavior |
| [CI/CD Integration](CI-CD-Integration) | GitHub Actions workflows for building, testing, and publishing |
| [Configuration Reference](Configuration-Reference) | All properties, resource settings, and customization options |

---

## Quick Start

Add the following parent declaration to your root `pom.xml`:

```xml
<parent>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-build</artifactId>
    <version>0.2.5-SNAPSHOT</version>
</parent>
```

Build with the `test` profile:

```bash
./mvnw clean test -P test
```

See [Getting Started](Getting-Started) for full instructions.

---

## Version Compatibility

| Microsphere Build | Minimum Maven | Java (Tested) |
|---|---|---|
| 0.2.x | 3.9.x | 8, 11, 17, 21, 25 |
| 0.1.x | 3.6.x | 8, 11, 17 |

---

## Links

- **Source Code:** <https://github.com/microsphere-projects/microsphere-build>
- **Maven Central:** <https://central.sonatype.com/artifact/io.github.microsphere-projects/microsphere-build>
- **Issue Tracker:** <https://github.com/microsphere-projects/microsphere-build/issues>
- **DeepWiki:** <https://deepwiki.com/microsphere-projects/microsphere-build>
