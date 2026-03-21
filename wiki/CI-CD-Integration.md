# CI/CD Integration

Microsphere Build ships with GitHub Actions workflows for automated building, testing, and publishing. This page describes the workflows, how to configure them, and how to publish releases to Maven Central.

---

## GitHub Actions Workflows

### Maven Build (`maven-build.yml`)

**Triggers:**
- `push` to `main` or `dev` branches
- `pull_request` targeting `main`, `dev`, or `release` branches

**Purpose:** Validates the build across multiple JDK versions and uploads code coverage data to Codecov.

**Matrix:**

| JDK Version | Distribution |
|---|---|
| 8 | Eclipse Temurin |
| 11 | Eclipse Temurin |
| 17 | Eclipse Temurin |
| 21 | Eclipse Temurin |
| 25 | Eclipse Temurin |

**Build command:**

```bash
./mvnw --batch-mode --update-snapshots --file pom.xml test --activate-profiles test,docs,coverage
```

This activates the `test`, `docs`, and `coverage` profiles, which run:
- Unit tests (Surefire)
- Integration tests (Failsafe)
- Code-style checks (Checkstyle)
- Documentation generation (Asciidoctor)
- Code coverage instrumentation and reporting (JaCoCo)

**Coverage upload:**

After the build, the workflow uploads JaCoCo coverage reports to [Codecov](https://app.codecov.io/gh/microsphere-projects/microsphere-build) using the `codecov/codecov-action@v5` action.

**Full workflow:**

```yaml
name: Maven Build

on:
  push:
    branches: [ "main", "dev" ]
  pull_request:
    branches: [ "main", "dev", "release" ]

jobs:
  build:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        java: [ '8', '11', '17', '21', '25' ]
    steps:
      - name: Checkout Source
        uses: actions/checkout@v5

      - name: Setup Java ${{ matrix.java }}
        uses: actions/setup-java@v5
        with:
          distribution: 'temurin'
          java-version: ${{ matrix.java }}

      - name: Build with Maven
        run: ./mvnw --batch-mode --update-snapshots --file pom.xml test --activate-profiles test,docs,coverage

      - name: Upload coverage to Codecov
        uses: codecov/codecov-action@v5
        with:
          token: ${{ secrets.CODECOV_TOKEN }}
          slug: microsphere-projects/microsphere-build
          fail_ci_if_error: false
```

---

### Maven Publish (`maven-publish.yml`)

**Triggers:**
- `push` to the `release` branch
- `workflow_dispatch` with a manual `revision` input

**Purpose:** Publishes artifacts to Maven Central, creates a Git tag and GitHub release, then bumps the version to the next snapshot.

**Jobs:**

#### Job 1: `build`

1. **Validates** the version format (must match `major.minor.patch`)
2. **Configures** Maven with Sonatype credentials
3. **Publishes** with profiles `publish` and `ci`:

```bash
mvn --batch-mode --update-snapshots --file pom.xml \
    -Drevision=${{ inputs.revision }} \
    -Dgpg.skip=true \
    deploy \
    --activate-profiles publish,ci
```

**Required secrets:**

| Secret | Description |
|---|---|
| `OSS_SONATYPE_USERNAME` | Sonatype Central Portal username |
| `OSS_SONATYPE_PASSWORD` | Sonatype Central Portal password/token |
| `OSS_SIGNING_KEY_ID_LONG` | Long-format GPG key ID |
| `OSS_SIGNING_KEY` | Private GPG key content |
| `OSS_SIGNING_PASSWORD` | GPG key passphrase |

**Environment variable mapping:**

| Env Variable | Secret Source |
|---|---|
| `MAVEN_USERNAME` | `OSS_SONATYPE_USERNAME` |
| `MAVEN_PASSWORD` | `OSS_SONATYPE_PASSWORD` |
| `SIGN_KEY_ID` | `OSS_SIGNING_KEY_ID_LONG` |
| `SIGN_KEY` | `OSS_SIGNING_KEY` |
| `SIGN_KEY_PASS` | `OSS_SIGNING_PASSWORD` |

#### Job 2: `release`

After `build` succeeds:

1. **Creates a Git tag** with the version number (e.g., `0.2.4`)
2. **Creates a GitHub Release** (e.g., `v0.2.4`) with auto-generated release notes
3. **Bumps the version** in `pom.xml` to the next patch snapshot (e.g., `0.2.5-SNAPSHOT`)
4. **Commits and pushes** the version bump

---

### Merge Main to Branches (`merge-main-to-branches.yml`)

Synchronizes the `main` branch into development branches to keep them up to date.

---

## Publishing to Maven Central

### Prerequisites

1. **Sonatype Central Portal account** — Register at <https://central.sonatype.com>
2. **Namespace ownership** — Claim the `io.github.microsphere-projects` namespace
3. **GPG key pair** — Generate with `gpg --full-generate-key`
4. **GitHub repository secrets** — Configure the five secrets listed above

### Manual Publishing

#### Step 1 — Configure `settings.xml`

```xml
<settings>
    <servers>
        <server>
            <id>ossrh</id>
            <username>YOUR_SONATYPE_USERNAME</username>
            <password>YOUR_SONATYPE_PASSWORD</password>
        </server>
    </servers>
</settings>
```

#### Step 2 — Deploy

```bash
./mvnw clean deploy -P publish -Drevision=1.0.0
```

This will:
- Compile and test the project
- Generate Javadoc and source JARs
- Sign all artifacts with GPG
- Deploy to Maven Central via the Central Publishing Portal

### Automated Publishing via GitHub Actions

1. Push to the `release` branch, or
2. Trigger the `Maven Publish` workflow manually via the GitHub Actions UI, entering the desired version (e.g., `0.2.4`)

The workflow handles the entire lifecycle: build → sign → publish → tag → release → version bump.

---

## CI Signing with Environment Variables

In CI environments, GPG keys are typically not installed in a keyring. The `ci` profile uses the [Sign Maven Plugin](https://www.simplify4u.org/sign-maven-plugin/) to sign artifacts directly from environment variables.

**How to set up:**

1. Export your private key:
   ```bash
   gpg --armor --export-secret-keys YOUR_KEY_ID > my-key.asc
   ```

2. Store the key content in a CI secret (e.g., `OSS_SIGNING_KEY`)

3. In your workflow:
   ```yaml
   env:
     SIGN_KEY: ${{ secrets.OSS_SIGNING_KEY }}
     SIGN_KEY_ID: ${{ secrets.OSS_SIGNING_KEY_ID_LONG }}
     SIGN_KEY_PASS: ${{ secrets.OSS_SIGNING_PASSWORD }}
   ```

4. Activate both `publish` and `ci` profiles:
   ```bash
   ./mvnw clean deploy -P publish,ci -Dgpg.skip=true
   ```

> The `-Dgpg.skip=true` flag disables the Maven GPG Plugin (which requires a local keyring) while the Sign Maven Plugin handles signing via environment variables.

---

## Codecov Integration

JaCoCo coverage reports are automatically uploaded to Codecov during CI builds.

**Setup:**
1. Add the `CODECOV_TOKEN` secret to your GitHub repository
2. The `maven-build.yml` workflow handles the upload automatically

**Badge:**

```markdown
[![Codecov](https://codecov.io/gh/microsphere-projects/microsphere-build/branch/main/graph/badge.svg)](https://app.codecov.io/gh/microsphere-projects/microsphere-build)
```

---

## Adapting for Your Own Project

If you are creating a new Microsphere ecosystem project:

1. **Copy the workflow files** from `.github/workflows/` to your project
2. **Update the `slug`** in the Codecov step to match your repository
3. **Configure the required secrets** in your repository settings
4. **Set the parent POM** to `microsphere-build`

**Example workflow for a child project:**

```yaml
name: Maven Build

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        java: [ '8', '11', '17', '21', '25' ]
    steps:
      - uses: actions/checkout@v5
      - uses: actions/setup-java@v5
        with:
          distribution: 'temurin'
          java-version: ${{ matrix.java }}
      - run: ./mvnw --batch-mode test -P test,coverage
      - uses: codecov/codecov-action@v5
        with:
          token: ${{ secrets.CODECOV_TOKEN }}
```

---

## Version Compatibility

| Microsphere Build | Publishing Method | CI Signing |
|---|---|---|
| 0.2.0+ | Central Publishing Portal (`publish` profile) | Sign Maven Plugin (`ci` profile) |
| 0.1.x | OSSRH Staging API (`release` profile) | Sign Maven Plugin (`ci` profile) |
