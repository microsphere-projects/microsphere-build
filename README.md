# Microsphere Build

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/microsphere-projects/microsphere-build)
[![Maven Build](https://github.com/microsphere-projects/microsphere-build/actions/workflows/maven-build.yml/badge.svg)](https://github.com/microsphere-projects/microsphere-build/actions/workflows/maven-build.yml)
![Maven](https://img.shields.io/maven-central/v/io.github.microsphere-projects/microsphere-build.svg)
![License](https://img.shields.io/github/license/microsphere-projects/microsphere-build.svg)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/microsphere-projects/microsphere-build.svg)](http://isitmaintained.com/project/microsphere-projects/microsphere-build "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/microsphere-projects/microsphere-build.svg)](http://isitmaintained.com/project/microsphere-projects/microsphere-build "Percentage of issues still open")

## Introduction

Microsphere Build is a Java Maven parent POM with common build settings, plugins configuration, etc, which is used for 
Microsphere Projects, like [Microsphere Java](https://github.com/microsphere-projects/microsphere-java), 
[Microsphere Spring](https://github.com/microsphere-projects/microsphere-spring), etc.


## Features

Microsphere Build supports the following features:
- Language Support
- Plugins Management
- Profiles Management
- Project Settings

### Language Support

Microsphere Build supports the Maven project building on Java TLS versions:
- 8
- 11
- 17
- 21
- 25

### Plugins Management

#### Default Inherited Plugins

- [Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/) (3.14.1)
- [Maven Source Plugin](https://maven.apache.org/plugins/maven-source-plugin/) (3.4.0)
- [Flatten Maven Plugin](https://www.mojohaus.org/flatten-maven-plugin/) (1.7.3)

#### Profile-Specific Plugins

##### Maven Profile `publish` Plugins (Recommended, since 0.2.0)

- [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) (3.12.0)
- [Maven Release Plugin](https://maven.apache.org/plugins/maven-release-plugin/) (3.3.0)
- [Maven Enforce Plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/) (3.6.2)
- [Maven GPG Plugin](https://maven.apache.org/plugins/maven-gpg-plugin/) (3.2.8)
- [Git Commit Id Plugin](https://github.com/git-commit-id/git-commit-id-maven-plugin) (4.9.10)
- [Central Publishing Maven Plugin](https://central.sonatype.com/artifact/org.sonatype.central/central-publishing-maven-plugin) (0.9.0)

> As of June 30, 2025 OSSRH has reached end of life and has been shut down. All OSSRH namespaces have been migrated to [Central Publisher Portal](https://central.sonatype.org/pages/ossrh-eol/).
> 
> See [Publishing By Using the Maven Plugin](https://central.sonatype.org/publish/publish-portal-maven/).
  
##### Maven Profile `release` Plugins

- [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) (3.12.0)
- [Maven Release Plugin](https://maven.apache.org/plugins/maven-release-plugin/) (3.1.1)
- [Maven Enforce Plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/) (3.6.2)
- [Maven GPG Plugin](https://maven.apache.org/plugins/maven-gpg-plugin/) (3.2.8)
- [Git Commit Id Plugin](https://github.com/git-commit-id/git-commit-id-maven-plugin) (4.9.10)
- [Nexus Maven Plugin](https://github.com/sonatype/nexus-maven-plugins) (1.7.0)

> If you want to release the Java artifacts using OSSRH staging API, please see [Publishing By Using the Portal OSSRH Staging API](https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/).

##### Maven Profile `ci` Plugins

- [Sign Maven Plugin](https://www.simplify4u.org/sign-maven-plugin/) (1.1.0)

##### Maven Profile `test` Plugins

- [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/) (3.5.4)
- [Maven Checkstyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/) (3.6.0)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/) (3.5.4)

##### Maven Profile `coverage` Plugins

- [Maven JaCoCo Plugin](https://www.eclemma.org/jacoco/) (0.8.14)

##### Maven Profile `docs` Plugins

- [Asciidoctor Maven Plugin](https://github.com/asciidoctor/asciidoctor-maven-plugin) (3.2.0)
- [Docbkx Maven Plugin](https://github.com/mimil/docbkx-tools) (2.0.17)
- [Build Helper Maven Plugin](https://www.mojohaus.org/build-helper-maven-plugin/) (3.6.1)

### Profiles Management

- `publish` (Recommended, since 0.2.0)
- `release` (Legacy)
- `ci`
- `test`
- `coverage`
- `docs`
- `java8+` (activated by JDK version)
- `java9+` (activated by JDK version)
- `java11+` (activated by JDK version)
- `java9-15` (activated by JDK version)
- `java16+` (activated by JDK version)
- `java17+` (activated by JDK version)

### 2.4 Project Settings

#### 2.4.1 Default Settings

##### 2.4.1.1 Resources Settings

```xml
<resources>
    <resource>
        <directory>src/main/java</directory>
        <filtering>true</filtering>
        <includes>
            <include>**/*.properties</include>
            <include>**/*.xml</include>
        </includes>
    </resource>
    <resource>
        <directory>src/main/resources</directory>
        <filtering>true</filtering>
        <includes>
            <include>**/*</include>
        </includes>
    </resource>
</resources>
```

#### Profiles Settings

##### Maven Profile `java8+` Settings

[Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) will be added the options `-Xdoclint:none`.

[Maven Checkstyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/)'s version : 9.3


##### Maven Profile `java9+` Settings

[Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)'s property `maven.compiler.release` 
references on the another property `${java.version}`.


#### Maven Profile `java11+` Settings

[Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) will use the configuration `source`
based on the property `${maven.compiler.source}`.

[Maven Checkstyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/)'s version : 10.26.1


#### Maven Profile `java9-15` Settings

[Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/) will append the argument line 
`--illegal-access=permit` for accessing to internal classes


#### Maven Profile `java16+` Settings

[Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/) will append the argument line
`--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED` for accessing to 
JDK modules' classes.


#### Maven Profile `java17+` Settings

[Maven Checkstyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/)'s version : 12.2.0


## Usage

### Java 8+ Maven Project

The root project's pom.xml should set the parent as follows:

```xml
    <parent>
        <groupId>io.github.microsphere-projects</groupId>
        <artifactId>microsphere-build</artifactId>
        <version>0.2.3</version>
    </parent>
```
