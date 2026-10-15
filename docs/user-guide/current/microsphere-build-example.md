# Microsphere Build Example — User Guide

[English](microsphere-build-example.md) | [简体中文](zh/microsphere-build-example.md)

- **Group ID**: `io.github.microsphere-projects` (inherited from parent)
- **Artifact ID**: `microsphere-build-example`
- **Packaging**: `jar`
- **Version analysed**: `0.3.18-SNAPSHOT` (branch `release`)
- **Role**: in-repo validation module. It is **not published** — `maven-deploy-plugin` is configured with
  `<skip>true</skip>` — and exists to exercise every profile and plugin inherited from the parent POM.

Treat this module as the reference template for a child project of `microsphere-build`.

## 1. API reference

The module ships one small class, `io.github.microsphere.example.ExampleService`, used to produce real source,
real unit tests and real coverage during the build.

### `ExampleService`

| Method | Signature | Contract |
|---|---|---|
| `greet` | `public String greet(String name)` | Returns `"Hello, <name>!"`. Throws `IllegalArgumentException` when `name` is `null` or empty. |
| `getJavaVersion` | `public String getJavaVersion()` | Returns the JVM's `java.specification.version` system property (e.g. `"17"`). Never `null` on any supported JDK. |

```java
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

## 2. Code examples (from the module's own test cases)

All snippets below are taken verbatim from
`microsphere-build-example/src/test/java/io/github/microsphere/example/ExampleServiceTest.java`, which is picked up
by Surefire (naming pattern `*Test.java`) when the `test` profile is active.

```java
public class ExampleServiceTest {

    private final ExampleService service = new ExampleService();

    @Test
    public void testGreet() {
        assertEquals("Hello, World!", service.greet("World"));
    }

    @Test
    public void testGreetWithCustomName() {
        assertEquals("Hello, Microsphere!", service.greet("Microsphere"));
    }

    @Test
    public void testGreetWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> service.greet(null));
    }

    @Test
    public void testGreetWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> service.greet(""));
    }

    @Test
    public void testGetJavaVersion() {
        assertNotNull(service.getJavaVersion());
    }
}
```

`testGetJavaVersion` is the JDK-matrix probe: it passes on every JDK (8 → 25) and thereby demonstrates that the
JDK-auto-activated profiles (`java9-15`, `java16+`, …) keep the test JVM usable on each runtime.

## 3. Using this module as a template

Copy the structure for a new child project:

```
my-project/
├── pom.xml                  # <parent> microsphere-build, ${revision} version
└── src/
    ├── main/java/...        # sources
    └── test/java/...        # *Test.java / *Tests.java (Surefire includes)
```

The module's POM is minimal — everything else is inherited:

```xml
<parent>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-build</artifactId>
    <version>${revision}</version>
    <relativePath>../pom.xml</relativePath>
</parent>

<artifactId>microsphere-build-example</artifactId>
<packaging>jar</packaging>

<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

> `${revision}` + `<relativePath>` only works inside this repository's reactor. Published child projects must pin
> the parent to a concrete released version.

Module-local overrides:

| Element | Value | Reason |
|---|---|---|
| `junit.version` property | `6.1.3` | Default (JDK 17+ runtimes) |
| `java8-16` profile (auto, JDK `[1.8,17)`) | `junit.version=5.14.3` | JUnit 6 requires Java 17+; on JDK 8–16 the module downgrades to JUnit 5 |
| `maven-deploy-plugin` `<skip>true</skip>` | — | The example must never reach Maven Central |
| Module `docs` profile | asciidoctor execution with `src/docs/asciidoc` → `target/docs` | Demonstrates the parent-managed `docs` profile wiring |

## 4. Running the module's builds

```bash
# from the repository root
mvn clean test -P test                      # surefire + checkstyle
mvn clean test -P test,coverage             # + jacoco (target/site/jacoco/index.html)
mvn clean generate-resources -P docs        # asciidoctor demo (module-level execution)
```

## 5. Version information and compatibility

| Item | Value |
|---|---|
| Module version | Tracks the parent `revision` (`0.3.18-SNAPSHOT` on `release`) |
| Deployed to repositories | No (`skip=true` since commit `f9a4faf`, included in the next release after 0.3.17) |
| JDK range for its tests | 8 – 25 (CI matrix), via JUnit 5.14.3 on JDK < 17 and JUnit 6.1.3 on JDK ≥ 17 |
| JUnit Jupiter | 6.1.3 (JDK 17+) / 5.14.3 (JDK 8–16) |
| API stability | The module is an example, not a library — its classes carry **no** compatibility promise |

## 6. Troubleshooting

| Symptom | Cause / fix |
|---|---|
| `NoSuchMethodError` / JUnit platform version clash while building on JDK 8–16 | The `java8-16` profile did not activate (Maven ran on an unexpected JDK) — check `mvn help:active-profiles`. |
| Checkstyle fails the example build | Violations in test sources are checked too (`includeTestSourceDirectory=true` in the parent). |
| `deploy` seems to do nothing for this module | Intentional — deployment is skipped; check other modules uploaded instead. |

---

Index: [User Guide README](README.md) ·
Parent POM reference: [microsphere-build.md](microsphere-build.md)
