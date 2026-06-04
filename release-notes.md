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

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.3.0...0.3.1