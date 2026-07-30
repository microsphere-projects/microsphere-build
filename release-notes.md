# Release Notes

## v0.2.7

# Release Notes: Version 0.2.7

## Build and Workflow Enhancements
- Refined `maven-publish` workflow steps and permissions. ([#9698043](https://example.com))
- Updated Maven workflows to use single quotes for branches and normalized quotes in workflow branch lists. ([#6c3f8ca](https://example.com), [#9a08665](https://example.com))
- Improved dependabot.yml indentation for update configurations. ([#8f27c01](https://example.com))
- Added a workflow to sync branches from upstream. ([#d730bba](https://example.com))
- Introduced matrix-based approach to merge `main` into branches. ([#95c49bb](https://example.com))
- Enhanced release creation steps, including Copilot-generated release notes. ([#c766467](https://example.com))

## Other Changes
- Regular merges of `main` into the release branch for synchronization. ([Multiple commits with `[skip ci]`](https://example.com)) 

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.2.6...0.2.7## v0.2.8

# Release Notes - Version 0.2.8

## Dependency Updates
- **Checkstyle:** Bumped `com.puppycrawl.tools:checkstyle` from `13.4.0` to `13.4.2`. ([#162](https://github.com/microsphere-projects/commit/d4dc0bb))
- **Maven Wrapper:**  
  - Updated Maven wrapper to version `3.9.15` (switched to Aliyun mirror).  
  - Previous update to Maven wrapper version `3.9.14`.  

## Build and Workflow Enhancements
- Updated Maven wrapper to use **Maven Central** for the wrapper distribution URL.  
- Set workflow permissions and enabled skip-ahead sync for improved CI/CD pipeline performance.  

---

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.2.7...0.2.8## v0.2.9

# Release Notes - Version 0.2.9

## New Features
- **Add Maven Version Sync Workflow**: Introduced a workflow to sync Maven plugin versions from `pom.xml` to `README.md` for consistency. ([#29d120d](https://github.com/microsphere-projects/commit/29d120d))

## Documentations
- **README Update**: Removed issue resolution badges. ([#d11eaf6](https://github.com/microsphere-projects/commit/d11eaf6))

## Dependency Updates
- **JUnit Jupiter**: Upgraded from `6.0.3` to `6.1.0`. ([#42c2166](https://github.com/microsphere-projects/commit/3812b05))
- **Maven Enforcer Plugin**: Upgraded from `3.6.2` to `3.6.3`. ([#78cb281](https://github.com/microsphere-projects/commit/df50480))

## Build and Workflow Enhancements
- Improved Maven workflows in GitHub Actions for better pipeline efficiency. ([#fc78b5a](https://github.com/microsphere-projects/commit/fc78b5a))
- Updated `maven-publish.yml` to align with publishing processes. ([#09c0894](https://github.com/microsphere-projects/commit/09c0894))

---

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.2.8...0.2.9## v0.3.0

# Release Notes for Version 0.3.0

## Dependency Updates
- **Updated:** `org.apache.maven.plugins:maven-surefire-plugin` from `3.5.5` to `3.5.6`. (#167)
- **Updated:** `org.apache.maven.plugins:maven-failsafe-plugin` from `3.5.5` to `3.5.6`. (#166)

## Documentation
- **Synced:** Plugin versions in `README.md` with `pom.xml` for improved consistency. (c6e89fd, 1a25fd6)

## Other Changes
- Internal merges between branches and version bumps after publishing v0.2.9.  

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.2.9...0.3.0## v0.3.1

# Release Notes - Version 0.3.1

## Dependency Updates
- Bumped `com.puppycrawl.tools:checkstyle` from `13.4.2` to `13.5.0`. ([#168](https://github.com/microsphere-projects/pull/168))

## Build and Workflow Enhancements
- Merged `main` into `release` and vice versa to sync branches.  
- Updated version to prepare for post-`0.3.0` development.  

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.0...0.3.1## v0.3.2

```markdown
# Release Notes - Version 0.3.2

## Other Changes
- **Chore**: Bump version to next patch after publishing 0.3.1.

```

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.1...0.3.2## v0.3.3

# Release Notes - Version 0.3.3

## Dependency Updates
- **jacoco-maven-plugin**: Bumped version from `0.8.14` to `0.8.15`. [#169](https://github.com/microsphere-projects/pull/169)

## Build and Workflow Enhancements
- Synchronized `main` and `release` branches. *(No user-facing changes)*

---

No other changes or new features introduced in this release.

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.2...0.3.3## v0.3.4

# Release Notes - Version 0.3.4

## Dependency Updates
- **checkstyle**: Bumped from `13.5.0` to `13.6.0` ([#170](https://github.com/microsphere-projects/pull/170))
- **central-publishing-maven-plugin**: Bumped to `0.11.0` ([#171](https://github.com/microsphere-projects/pull/171))

## Documentation
- Synced plugin versions in `README.md` with `pom.xml` ([c2e952c](https://github.com/microsphere-projects/commit/c2e952c))

## Other Changes
- Version bumped to `0.3.4` with post-release prep ([381324b](https://github.com/microsphere-projects/commit/381324b))  

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.3...0.3.4## v0.3.5

# Release Notes for Version 0.3.5

## Dependency Updates
- Updated `com.puppycrawl.tools:checkstyle` from 13.6.0 to 13.7.0. ([#173](https://github.com/microsphere-projects/pull/173))
- Updated `org.junit.jupiter:junit-jupiter` from 6.1.0 to 6.1.1. ([#172](https://github.com/microsphere-projects/pull/172))

## Documentation
- Synchronized plugin versions in the `README.md` with `pom.xml`. ([3e0f70a](https://github.com/microsphere-projects/commit/3e0f70a))

## Build and Workflow Enhancements
- Merged main branch into release branch to keep branches in sync. ([14bec13](https://github.com/microsphere-projects/commit/14bec13), [8a8b92b](https://github.com/microsphere-projects/commit/8a8b92b))
- Bumped project version to the next patch after publishing v0.3.4. ([4cc0e4c](https://github.com/microsphere-projects/commit/4cc0e4c))

---

**Note:** Minor updates and maintenance-related changes included.

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.4...0.3.5## v0.3.6

# Release Notes - Version 0.3.6

## Build and Workflow Enhancements
- Pass JDK-specific JVM arguments to `javac` for improved compatibility. ([9624c62](#))

## Other Changes
- Merged `main` into `release`. ([4df5818](#))
- Merged `release` back into `main`. ([4e62e6b](#))
- Bumped version to prepare for the next patch release. ([d4470e5](#)) 

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.5...0.3.6## v0.3.7

# Release Notes - Version 0.3.7

## Build and Workflow Enhancements
- Scoped compiler JVM arguments to the Java 8 profile. ([ef6f861](#))

## Other Changes
- Merged `main` into release. ([680e588](#))
- Merged `release` into main. ([d4b488e](#))
- Bumped version to next patch after publishing `0.3.6`. ([0cb5858](#))

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.6...0.3.7## v0.3.8

# Release Notes - Version 0.3.8

## Build and Workflow Enhancements
- Enabled Maven debug logging in CI builds to improve build diagnostics. (#3b42ed0)

## Other Changes
- Refined compiler configuration for Java 17 profile for better compatibility and performance. (#2043c70)
- Version bumped to prepare for future development. (#ddc05e3)

---

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.7...0.3.8## v0.3.9

# Release Notes - Version 0.3.9

## Dependency Updates
- **Checkstyle:** Upgraded from `13.7.0` to `13.8.0`. [#175](https://github.com/microsphere-projects/issues/175)
- **JUnit Jupiter:** Upgraded from `6.1.1` to `6.1.2`. [#174](https://github.com/microsphere-projects/issues/174)

## Build and Workflow Enhancements
- Merged main branch into the release branch to keep versions aligned. [#32be67d](https://github.com/microsphere-projects/commit/32be67d)  

---

No new features, bug fixes, or documentation updates in this release. 

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.8...0.3.9## v0.3.10

# Release Notes - Version 0.3.10

## Dependency Updates
- Bumped `org.apache.maven.plugins:maven-jar-plugin` from `3.5.0` to `3.5.1`. ([#176](https://github.com/microsphere-projects/microsphere/pull/176))

## Build and Workflow Enhancements
- Merged `main` into `release` and vice versa to synchronize changes. ([05b9d6e](https://github.com/microsphere-projects/microsphere/commit/05b9d6e), [9985c83](https://github.com/microsphere-projects/microsphere/commit/9985c83))
- Updated version to prepare for the next patch release after publishing `0.3.9`. ([005ea6b](https://github.com/microsphere-projects/microsphere/commit/005ea6b)) 

---

No new features, bug fixes, documentation, or test improvements included in this release.

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.9...0.3.10