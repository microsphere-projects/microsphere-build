# Microsphere Build Example —— 用户文档

[English](../microsphere-build-example.md) | [简体中文](microsphere-build-example.md)

- **Group ID**：`io.github.microsphere-projects`（从父 POM 继承）
- **Artifact ID**：`microsphere-build-example`
- **打包方式**：`jar`
- **所分析版本**：`0.3.18-SNAPSHOT`（`release` 分支）
- **角色**：仓库内的构建验证模块。它**不会被发布** —— `maven-deploy-plugin` 配置了 `<skip>true</skip>` ——
  存在的意义就是把从父 POM 继承来的每个 Profile 和插件都实际跑一遍。

可以把本模块当作 `microsphere-build` 子项目的参考模板。

## 1. API 参考

本模块只提供一个很小的类 `io.github.microsphere.example.ExampleService`，用于在构建过程中产生真实的源码、
真实的单元测试和真实的覆盖率。

### `ExampleService`

| 方法 | 签名 | 契约 |
|---|---|---|
| `greet` | `public String greet(String name)` | 返回 `"Hello, <name>!"`。当 `name` 为 `null` 或空字符串时抛出 `IllegalArgumentException`。 |
| `getJavaVersion` | `public String getJavaVersion()` | 返回 JVM 的 `java.specification.version` 系统属性（例如 `"17"`）。在任何受支持的 JDK 上都不会是 `null`。 |

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

## 2. 代码示例（取自模块自身的测试用例）

下面所有片段都逐字取自
`microsphere-build-example/src/test/java/io/github/microsphere/example/ExampleServiceTest.java`，当 `test` Profile
激活时，该文件会被 Surefire（命名规则 `*Test.java`）拾取。

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

`testGetJavaVersion` 就是这个 JDK 矩阵探针：它在每个 JDK（8 → 25）上都能通过，从而说明 JDK 自动激活的那些 Profile
（`java9-15`、`java16+` 等）在每个运行时上都让测试 JVM 保持可用。

## 3. 把本模块当模板使用

新建子项目时可以复制这套结构：

```
my-project/
├── pom.xml                  # <parent> 指向 microsphere-build，版本用 ${revision}
└── src/
    ├── main/java/...        # 源码
    └── test/java/...        # *Test.java / *Tests.java（Surefire 的 includes 规则）
```

本模块的 POM 极为精简 —— 其余一切全部来自继承：

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

> `${revision}` + `<relativePath>` 只在本仓库的 reactor 内有效。已发布的子项目必须把 parent 固定到某个具体的
> 已发布版本。

模块内的覆盖项：

| 元素 | 值 | 原因 |
|---|---|---|
| `junit.version` 属性 | `6.1.3` | 默认值（JDK 17+ 运行时） |
| `java8-16` Profile（自动激活，JDK `[1.8,17)`） | `junit.version=5.14.3` | JUnit 6 需要 Java 17+；在 JDK 8–16 上本模块会降级到 JUnit 5 |
| `maven-deploy-plugin` 的 `<skip>true</skip>` | — | 示例模块绝不能进入 Maven Central |
| 模块自身的 `docs` Profile | asciidoctor 执行，把 `src/docs/asciidoc` 输出到 `target/docs` | 演示父 POM 中受管的 `docs` Profile 该如何接线 |

## 4. 运行本模块的构建

```bash
# 在仓库根目录执行
mvn clean test -P test                      # surefire + checkstyle
mvn clean test -P test,coverage             # 再加上 jacoco（target/site/jacoco/index.html）
mvn clean generate-resources -P docs        # asciidoctor 演示（模块级执行）
```

## 5. 版本信息与兼容性

| 条目 | 值 |
|---|---|
| 模块版本 | 跟随父 POM 的 `revision`（`release` 分支上为 `0.3.18-SNAPSHOT`） |
| 是否部署到仓库 | 否（自提交 `f9a4faf` 起 `skip=true`，该提交包含在 0.3.17 之后的下一个版本中） |
| 其测试支持的 JDK 范围 | 8 – 25（CI 矩阵），JDK < 17 走 JUnit 5.14.3，JDK ≥ 17 走 JUnit 6.1.3 |
| JUnit Jupiter | 6.1.3（JDK 17+）/ 5.14.3（JDK 8–16） |
| API 稳定性 | 本模块是示例而不是库 —— 其中的类**不**承载任何兼容性承诺 |

## 6. 常见问题排查

| 现象 | 原因 / 解决办法 |
|---|---|
| 在 JDK 8–16 上构建时出现 `NoSuchMethodError` / JUnit platform 版本冲突 | `java8-16` Profile 没有激活（Maven 运行在了意料之外的 JDK 上）—— 用 `mvn help:active-profiles` 检查。 |
| Checkstyle 导致示例构建失败 | 测试源码里的违规同样会被检查（父 POM 中 `includeTestSourceDirectory=true`）。 |
| 对该模块执行 `deploy` 似乎什么都没做 | 这是有意为之 —— 部署被跳过了；请改为确认其他模块正常上传。 |

---

索引：[用户文档 README](README.md) ·
父 POM 参考：[microsphere-build.md](microsphere-build.md)
