# Microsphere Build 文档

[English](../README.md) | [简体中文](README.md)

**Microsphere Build** 是一个 Maven 父工程 POM（`packaging: pom`，groupId 为 `io.github.microsphere-projects`，
artifactId 为 `microsphere-build`），为 Microsphere 项目生态提供集中式构建配置：插件管理、Profile 管理、
CI 友好版本号以及多 JDK 支持。

文档按 **发布分支** 与 **读者对象** 两个维度组织。

## 发布分支

| 分支 | 文档目录 | 分析版本 |
|---|---|---|
| `release` | [`current/`](../user-guide/current/zh/README.md) | `0.3.18-SNAPSHOT`（最新已发布版本：`0.3.17`） |
| `release-1.x` | `1.x/` | *尚未创建 —— 本仓库不存在 `release-1.x` 分支* |

> 所有文档仅描述 `release` 分支，不分析 `main` 与 `dev` 分支。

## 文档集合

| 集合 | 位置 | 读者 | 内容 |
|---|---|---|---|
| 用户文档（User Guide） | [`user-guide/current/zh/`](../user-guide/current/zh/README.md) | 继承该父 POM 的项目 | 上手指南、按模块的参考文档：各构件的用法、配置属性、代码示例、版本兼容范围 |
| 开发者文档（Developer Guide） | [`developer-guide/current/zh/`](../developer-guide/current/zh/README.md) | 本仓库维护者 | 架构设计、`pom.xml` 源码分析、设计模式、构建生命周期与 CI/CD 流程的 Mermaid 流程图/时序图 |

每个集合均按 Maven 模块提供独立文档：

- `microsphere-build` —— 父 POM 本身（根模块）
- `microsphere-build-example` —— 示例/构建验证 JAR 模块

## 快速导航

- 用户文档 —— [English index](../user-guide/current/README.md) · [中文索引](../user-guide/current/zh/README.md)
- 开发者文档 —— [English index](../developer-guide/current/README.md) · [中文索引](../developer-guide/current/zh/README.md)
- 仓库根 README —— [../README.md](../README.md)
