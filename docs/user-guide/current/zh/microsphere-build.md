# Microsphere Build（父 POM）—— 用户文档

[English](../microsphere-build.md) | [简体中文](microsphere-build.md)

- **Group ID**：`io.github.microsphere-projects`
- **Artifact ID**：`microsphere-build`
- **打包方式**：`pom`（父 POM —— 自身不含任何 Java 源码）
- **所分析版本**：`0.3.18-SNAPSHOT`（`release` 分支）；最新已发布版本：`0.3.17`
- **许可证**：Apache License 2.0

父 POM 就是这个项目的「API」。使用方并不会从其中调用 Java 类，而是通过把它声明为自己的 `<parent>`，来继承其中的
**属性**、**插件管理（plugin management）**、**Profile**、**资源过滤**以及**构件分发（distribution）设置**。
本文档逐一说明每个可继承的元素、如何使用与覆盖它们，以及版本兼容范围。

<a id="get-getting-started-with-the-parent-pom"></a>

## 1. 快速上手

### 1.1 前置条件

| 要求 | 最低版本 | 说明 |
|---|---|---|
| JDK | 8 | LTS 8 / 11 / 17 / 21 / 25 均在 CI 中经过测试（Temurin） |
| Apache Maven | 3.9+ | 对 `${revision}` 而言硬性最低要求是 3.5+；本仓库自身的 wrapper 使用 3.9.16 |
| Git | 2.x | 当 `publish`/`release` Profile 运行 Git Commit ID 插件时需要 |

### 1.2 声明 parent

```xml
<parent>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-build</artifactId>
    <version>0.3.17</version>
</parent>
```

### 1.3 选择你的 Java 目标

默认字节码目标是 Java 8。只需覆盖 `java.version` 这一个属性即可整体重新设定目标（`maven.compiler.source`、
`maven.compiler.target`，以及在 JDK 9+ 上的 `maven.compiler.release` 都由它派生）：

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

### 1.4 构建

有三个插件会被继承，并且在**每一次**构建中都会生效 —— 无需任何额外配置：

| 插件 | 版本（0.3.18-SNAPSHOT） | 作用 |
|---|---|---|
| `maven-compiler-plugin` | 3.16.0 | 以 `-parameters` 编译（保留方法参数名） |
| `maven-source-plugin` | 3.4.0 | 在 `package` 阶段附加 `-sources.jar`（`jar-no-fork`） |
| `flatten-maven-plugin` | 1.7.3 | 解析已安装/已部署 POM 中 CI 友好的 `${revision}`（`process-resources`） |

```bash
mvn clean package                 # 编译 + 源码包 + flatten
mvn clean verify -P test          # 再加上单元测试、集成测试、checkstyle
mvn clean test -P test,coverage   # 再加上 JaCoCo 报告，位于 target/site/jacoco/index.html
```

## 2. Profile 参考（可继承的行为 API）

### 2.1 手动激活的 Profile

| Profile | 用途 | 被激活的插件 | 典型命令 |
|---|---|---|---|
| `publish`（0.2.0 起推荐使用） | 通过 Central Publishing Portal 把签名后的制品发布到 Maven Central | javadoc 3.12.0、release 3.3.1、enforcer 3.6.3、gpg 3.2.8、git-commit-id 4.9.10、central-publishing 0.11.0 | `mvn clean deploy -P publish` |
| `release`（遗留） | 通过 OSSRH Staging API（Nexus）发布 —— OSSRH 已于 2025-06-30 停止服务，请优先使用 `publish` | javadoc、release、enforcer、gpg、git-commit-id、nexus-staging 1.7.0 | `mvn clean deploy -P release` |
| `ci` | 在 CI 中基于环境变量对制品签名（无需本地密钥环） | `sign-maven-plugin` 1.2.0 | `mvn clean deploy -P publish,ci -Dgpg.skip=true` |
| `test` | 单元测试 + 集成测试 + 代码风格检查 | surefire 3.6.0、failsafe 3.6.0、checkstyle 3.6.0 | `mvn clean verify -P test` |
| `coverage` | JaCoCo 插桩与报告 | jacoco 0.8.15（`prepare-agent`、`report`） | `mvn clean test -P test,coverage` |
| `docs` | 生成 AsciiDoc / DocBook 文档 | asciidoctor 3.2.0（JDK 8–10 上为 2.2.6）、docbkx 2.0.17、build-helper 3.6.2 | `mvn clean generate-resources -P docs` |

`ci` Profile 的签名需要以下环境变量：

| 变量 | 说明 |
|---|---|
| `SIGN_KEY` | 私钥的内容（不是文件路径） |
| `SIGN_KEY_ID` | 长格式的密钥 ID |
| `SIGN_KEY_PASS` | 密钥口令 |

Profile 之间可以自由组合，例如 `mvn clean verify -P test,docs,coverage`。

### 2.2 由 JDK 自动激活的 Profile

这些 Profile 依据 Maven 检测到的 JDK 版本自动激活 —— 绝不要用 `-P` 手动传入它们：

| Profile | 激活条件 | 作用 |
|---|---|---|
| `java8+` | `[1.8,)` | 为 javadoc 设置 `<doclint>none</doclint>`；Checkstyle 工具固定为 9.3；Asciidoctor 2.2.6 |
| `java9+` | `[9,)` | 设置 `maven.compiler.release=${java.version}`（真正的跨版本编译） |
| `java11+` | `[11,)` | Javadoc `source=${maven.compiler.source}`；Checkstyle 工具 10.26.1；Asciidoctor 3.2.0 |
| `java9-15` | `[9,15]` | 为 Surefire 设置 `jvm.argLine=--illegal-access=permit` |
| `java16+` | `[16,)` | `jvm.argLine=--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED`；编译器 `fork=true` 并带上同样的 `-J--add-opens` 参数 |
| `java17+` | `[17,)` | Checkstyle 工具 11.1.0 |

激活矩阵（各 Profile 是叠加生效的）：

| JDK | `java8+` | `java9+` | `java9-15` | `java11+` | `java16+` | `java17+` |
|---|---|---|---|---|---|---|
| 8 | ✅ | — | — | — | — | — |
| 9–10 | ✅ | ✅ | ✅ | — | — | — |
| 11–15 | ✅ | ✅ | ✅ | ✅ | — | — |
| 16 | ✅ | ✅ | — | ✅ | ✅ | — |
| 17 / 21 / 25 | ✅ | ✅ | — | ✅ | ✅ | ✅ |

## 3. 配置属性参考

在子 POM 的 `<properties>` 中可以覆盖下列任意属性。

### 3.1 核心构建属性

| 属性 | 默认值 | 说明 |
|---|---|---|
| `revision` | 本仓库中为 `0.3.18-SNAPSHOT` | CI 友好的版本占位符；在你的项目中请使用你自己的版本 |
| `java.version` | `8` | 决定 `source`/`target`/`release` |
| `project.build.sourceEncoding` | `UTF-8` | 源码编码 |
| `resource.delimiter` | `@` | 资源过滤分隔符（`@property@`） |
| `jvm.argLine` | *（空；由 JDK Profile 设置）* | 追加到 Surefire `argLine` 之后的额外 JVM 参数 |
| `jacoco.argLine` | *（由 `prepare-agent` 设置）* | JaCoCo agent 的参数槽位 |
| `disable.checks` | `true` | Checkstyle 的跳过开关（受管的那份执行会遵循它） |

### 3.2 插件版本属性

| 属性 | 默认值 |
|---|---|
| `maven-compiler-plugin.version` | `3.16.0` |
| `maven-javadoc-plugin.version` | `3.12.0` |
| `maven-source-plugin.version` | `3.4.0` |
| `maven-jar-plugin.version` | `3.5.1` |
| `maven-war-plugin.version` | `3.5.1` |
| `maven-share-plugin.version`（Shade） | `3.6.2` |
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
| `asciidoctor-maven-plugin.version` | `3.2.0`（JDK 8–10 上经 `java8+` 为 2.2.6） |
| `docbkx-maven-plugin.version` | `2.0.17` |
| `build-helper-maven-plugin.version` | `3.6.2` |
| `puppycrawl-tools-checkstyle.version` | 基础值 `14.3.0`，按 JDK 覆盖：9.3（`java8+`）、10.26.1（`java11+`）、11.1.0（`java17+`） |
| `git-commit-id-plugin.version` | `4.9.10` |
| `central-publishing-maven-plugin.version` | `0.11.0` |

### 3.3 Flatten 插件属性

| 属性 | 默认值 | 说明 |
|---|---|---|
| `flatten.mode` | `resolveCiFriendliesOnly` | 扁平化的范围 |
| `flatten.dependency.mode` | `all` | 解析哪些依赖 scope |
| `updatePomFile` | `true` | 已安装/已部署的 POM 会被扁平化后的 POM 替换 |
| `flatten.pomElements.parent` | `expand` | 将 parent 内联进扁平化后的 POM |
| `flatten.pomElements.properties` | `keep` | 保留属性 |
| `flatten.pomElements.dependencies` | `resolve` | 解析依赖版本 |
| `flatten.pomElements.distributionManagement` / `repositories` | `remove` | 从发布出去的 POM 中剥离 |
| `flatten.pomElements.url` / `scm` / `developers` | `resolve` | 子 POM 中缺失时从父级继承 |

### 3.4 仓库 / 发布属性

| 属性 | 默认值 |
|---|---|
| `maven-center-repository.host` | `central.sonatype.com` |
| `ossrh-staging-api.base-uri` | `https://ossrh-staging-api.central.sonatype.com` |
| `ossrh-snapshots-repository.uri` | `https://central.sonatype.com/repository/maven-snapshots/` |
| `ossrh-repository.uri` | `${ossrh-staging-api.base-uri}/service/local/staging/deploy/maven2/` |

快照（snapshot）会部署到 Central 上的 `maven-snapshots`；根 POM 同时在 `<repositories>` 中声明了该快照仓库，
因此下游 Microsphere 项目可以直接使用快照构建的产物。

## 4. 资源过滤行为

`src/main/java`（仅 `*.properties` / `*.xml`）和 `src/main/resources`（全部文件）都会被过滤。配合
`resource.delimiter=@`，占位符会在构建期被替换掉：

```properties
# 源文件：src/main/resources/application.properties
app.version=@project.version@
```

```properties
# 构建之后（target/classes/application.properties）
app.version=1.0.0
```

> 由于过滤同样作用于 `src/main/java`，放在源码目录旁边的二进制文件或非过滤资源是不会被复制的 —— 请把二进制文件放在
> 一个单独的、未被过滤的资源目录里。

## 5. 继承的 Enforcer 规则

受管的 `maven-enforcer-plugin`（由 `publish`/`release` 激活）会强制以下规则：

- `bannedDependencies`，并开启传递依赖搜索（`searchTransitive=true`）—— 禁用列表需要你自己声明；
- `banDuplicatePomDependencyVersions` —— 同一个依赖不得以不同版本出现两次。

## 6. 用法示例

### 6.1 面向 Java 8、发布到 Central 的库

```xml
<parent>
    <groupId>io.github.microsphere-projects</groupId>
    <artifactId>microsphere-build</artifactId>
    <version>0.3.17</version>
</parent>

<properties>
    <revision>1.2.0-SNAPSHOT</revision>
    <!-- java.version 保持为 8 -->
</properties>
```

```bash
# 本地验证，与 CI 使用同样的门禁
mvn clean verify -P test,coverage

# 发布
mvn clean deploy -P publish,ci -Dgpg.skip=true -Drevision=1.2.0
```

`settings.xml` 中必须定义 server id `ossrh`（`publish` 与遗留的 `release` Profile 都会用到它）：

```xml
<server>
    <id>ossrh</id>
    <username>${env.MAVEN_USERNAME}</username>
    <password>${env.MAVEN_PASSWORD}</password>
</server>
```

### 6.2 面向 Java 21 的应用

```xml
<properties>
    <java.version>21</java.version>
</properties>
```

无需其他改动：在 JDK 21 上，自动 Profile 会自动设置 `release=21`、为 Surefire 加上模块系统的 `--add-opens`
参数，并把 Checkstyle 切到 11.x。

### 6.3 取自仓库自身测试的代码示例

父 POM 没有运行时 API，因此「用法示例」其实就是构建配置。编译与测试这一契约由 `microsphere-build-example`
实际跑通 —— 参见 [microsphere-build-example.md](microsphere-build-example.md)，其中给出了验证每个继承 Profile 的
Java API 与测试用例示例。

## 7. 版本信息与兼容范围

| Microsphere Build | 重要变更 | 兼容性 |
|---|---|---|
| `0.3.18-SNAPSHOT`（release 分支，尚未发布） | Maven wrapper 3.9.16；在 0.3.17 之上升级插件 | JDK 8–25 |
| `0.3.x` | 插件版本持续上调（compiler 3.16.0、surefire/failsafe 3.6.0、checkstyle 工具基础版本 14.3.0、jacoco 0.8.15、central-publishing 0.11.0） | JDK 8–25，Maven 3.9+ |
| `0.2.0` – `0.2.x` | 新增 `publish` Profile，使用 Central Publishing Maven Plugin（应对 OSSRH 停止服务的迁移） | JDK 8–25（0.2.5 起支持 25） |
| `0.1.x` | 最初的父 POM：`release`/`ci`/`test`/`coverage`/`docs` 以及 JDK 自动 Profile，CI 友好的 `${revision}` | JDK 8–17 时代的工具链 |

**兼容性规则**

- 在同一条小版本线内，父 POM 对使用方是**向后兼容**的：升级只会向前推进插件版本；第 3 节中的属性名仍然是覆盖点。
- **编译目标 vs 构建 JDK**：字节码按 `${java.version}`（默认 8）生成；构建 JDK 必须 ≥ 8。JDK 9–25 通过自动激活的
  Profile 作为构建运行时得到支持。
- Checkstyle 工具版本取决于 JDK（实际生效值为 9.3 / 10.26.1 / 11.1.0）；自行固定
  `puppycrawl-tools-checkstyle.version` 有可能让较旧 JDK 上的构建失败。
- 使用方必须使用 Maven **3.5+**（CI 友好版本化）；推荐使用 **3.9+** 以便与 CI 保持一致。

## 8. 常见问题排查

| 现象 | 原因 / 解决办法 |
|---|---|
| `The following artifacts could not be resolved ... ${revision}` | 你在本仓库之外用 `${revision}` 引用了父 POM —— 改用一个具体的已发布版本（例如 `0.3.17`）。 |
| 在 JDK 8 上构建时报 `Incompatible because this component declares ... Java 11` | 某个插件（例如较新的 checkstyle）需要更新的 JDK；执行 `mvn help:active-profiles` 检查 —— `java8+` 应当把 Checkstyle 固定在 9.3。 |
| 测试中出现 `module java.base does not open java.lang` | 构建运行在 JDK 16+，而 `java16+` 的 arg line 没有生效 —— 确认你没有覆盖 `jvm.argLine`，正确做法是在其后追加你自己的参数。 |
| 测试被意外跳过 | Surefire 只匹配 `**/*Test.java` 和 `**/*Tests.java`，并排除 `**/Abstract*.java`。请重命名文件，或调整 includes。 |
| deploy 因 enforcer 的 `bannedDependencies` 失败 | 存在被禁用的（通常是有漏洞的）传递依赖；用 `mvn dependency:tree` 定位它。 |

---

下一篇：[microsphere-build-example.md](microsphere-build-example.md) ·
[开发者文档](../../../developer-guide/current/zh/README.md)
