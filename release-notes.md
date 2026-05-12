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

**Full Changelog**: https://github.com/microsphere-projects/microsphere-build/compare/0.2.7...0.2.8