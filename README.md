# Microsphere Build

[![Maven Build](https://github.com/microsphere-projects/microsphere-build/actions/workflows/maven-build.yml/badge.svg)](https://github.com/microsphere-projects/microsphere-build/actions/workflows/maven-build.yml)
![Maven](https://img.shields.io/maven-central/v/io.github.microsphere-projects/microsphere-build.svg)
![License](https://img.shields.io/github/license/microsphere-projects/microsphere-build.svg)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/microsphere-projects/microsphere-build.svg)](http://isitmaintained.com/project/microsphere-projects/microsphere-build "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/microsphere-projects/microsphere-build.svg)](http://isitmaintained.com/project/microsphere-projects/microsphere-build "Percentage of issues still open")

## 1. Introduction

Microsphere Build is a Java Maven parent POM with common build settings, plugins configuration, etc, which is used for 
Microsphere Projects, like [Microsphere Java](https://github.com/microsphere-projects/microsphere-java), 
[Microsphere Spring](https://github.com/microsphere-projects/microsphere-spring), etc.


## 2. Features

Microsphere Build supports the following features:
- Language Support
- Plugins Management
- Profiles Management
- Project Settings

### 2.1 Language Support

Microsphere Build supports the Maven project building on Java 8+ TLS and Java 17+ TLS.

### 2.2 Plugins Management

#### 2.2.1 Default Inherited Plugins

- [Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)
- [Maven Source Plugin](https://maven.apache.org/plugins/maven-source-plugin/)
- [Flatten Maven Plugin](https://www.mojohaus.org/flatten-maven-plugin/)

#### 2.2.2 Profile-Specific Plugins

##### 2.2.2.1 Maven Profile `release` Plugins

- [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/)
- [Maven Release Plugin](https://maven.apache.org/plugins/maven-release-plugin/)
- [Maven Enforce Plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/)
- [Maven GPG Plugin](https://maven.apache.org/plugins/maven-gpg-plugin/)
- [Git Commit Id Plugin](https://github.com/git-commit-id/git-commit-id-maven-plugin)
- [Nexus Maven Plugin](https://github.com/sonatype/nexus-maven-plugins)

##### 2.2.2.2 Maven Profile `ci` Plugins

- [Sign Maven Plugin](https://www.simplify4u.org/sign-maven-plugin/)

##### 2.2.2.3 Maven Profile `test` Plugins

- [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/)
- [Maven Checkstyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)

##### 2.2.2.4 Maven Profile `coverage` Plugins

- [Maven JaCoCo Plugin](https://www.eclemma.org/jacoco/)

##### 2.2.2.5 Maven Profile `docs` Plugins

- [Asciidoctor Maven Plugin](https://github.com/asciidoctor/asciidoctor-maven-plugin)
- [Docbkx Maven Plugin](https://github.com/mimil/docbkx-tools)
- [Build Helper Maven Plugin](https://www.mojohaus.org/build-helper-maven-plugin/)


### 2.3 Profiles Management

- `release`
- `ci`
- `test`
- `coverage`
- `docs`
- `java8+` (activated by jdk version)
- `java9+` (activated by jdk version)
- `java11` (activated by jdk version)
- `java9-15` (activated by jdk version)
- `java16+` (activated by jdk version)

### 2.4 Project Settings

#### 2.4.1 Defaults Settings

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

#### 2.4.2 Profiles Settings

##### 2.4.2.1 Maven Profile `java8+` Settings

[Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) will be added the options `-Xdoclint:none`.

##### 2.4.2.2 Maven Profile `java9+` Settings

[Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)'s property `maven.compiler.release` 
references on the another property `${java.version}`.

#### 2.4.2.3 Maven Profile `java11` Settings

[Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) will use the configuration `source`
based on the property `${maven.compiler.source}`.

#### 2.4.2.4 Maven Profile `java9-15` Settings

[Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/) will append the argument line 
`--illegal-access=permit` for accessing to internal classes

#### 2.4.2.5 Maven Profile `java16+` Settings

[Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/) will append the argument line
`--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED` for accessing to 
JDK modules' classes.

## 3. Usage

### 3.1 Java 8+ Maven Project

The root project's pom.xml should set the parent as follows:

```xml
    <parent>
        <groupId>io.github.microsphere-projects</groupId>
        <artifactId>microsphere-build</artifactId>
        <version>0.1.0</version>
    </parent>
```

### 3.2 Java 17+ Maven Project

The root project's pom.xml should set the parent as follows:

```xml
    <parent>
        <groupId>io.github.microsphere-projects</groupId>
        <artifactId>microsphere-build</artifactId>
        <version>0.1.1</version>
    </parent>
```