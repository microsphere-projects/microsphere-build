# Microsphere Build Documentation

[English](README.md) | [简体中文](zh/README.md)

**Microsphere Build** is a Maven parent POM (`packaging: pom`, groupId `io.github.microsphere-projects`,
artifactId `microsphere-build`) that provides centralized build configuration — plugin management, profile
management, CI-friendly versioning and multi-JDK support — for the Microsphere Projects ecosystem.

Documentation is organized by **release branch** and by **audience**:

## Release branches

| Branch | Docs directory | Version analyzed |
|---|---|---|
| `release` | [`current/`](user-guide/current/) | `0.3.18-SNAPSHOT` (latest published release: `0.3.17`) |
| `release-1.x` | `1.x/` | *Not created yet — the `release-1.x` branch does not exist in this repository* |

> All documentation describes the `release` branch only. `main` and `dev` are not documented.

## Documentation sets

| Set | Location | Audience | Contents |
|---|---|---|---|
| User Guide | [`user-guide/current/`](user-guide/current/README.md) | Projects that inherit the parent POM | Adoption guide, per-module reference: how to use each artifact, configuration properties, code examples, version compatibility |
| Developer Guide | [`developer-guide/current/`](developer-guide/current/README.md) | Maintainers of this repository | Architecture design, source analysis of `pom.xml`, design patterns, Mermaid flow/sequence diagrams of build lifecycle and CI/CD pipelines |

Each set contains one document per Maven module:

- `microsphere-build` — the parent POM itself (root module)
- `microsphere-build-example` — the example/validation JAR module

## Quick navigation

- User Guide — [English index](user-guide/current/README.md) · [中文索引](user-guide/current/zh/README.md)
- Developer Guide — [English index](developer-guide/current/README.md) · [中文索引](developer-guide/current/zh/README.md)
- Repository root README — [../README.md](../README.md)
