# Metadata for Gradle Projects

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.sgtsilvio.gradle.metadata?color=brightgreen&style=for-the-badge)](https://plugins.gradle.org/plugin/io.github.sgtsilvio.gradle.metadata)
[![GitHub](https://img.shields.io/github/license/sgtsilvio/gradle-metadata?color=brightgreen&style=for-the-badge)](LICENSE)
[![GitHub Workflow Status (with branch)](https://img.shields.io/github/actions/workflow/status/sgtsilvio/gradle-metadata/check.yml?branch=main&style=for-the-badge)](https://github.com/SgtSilvio/gradle-metadata/actions/workflows/check.yml?query=branch%3Amain)

Gradle plugin to ease defining project metadata:
- module name
- readable name
- url, docUrl
- organization
- license
- developers
- issue management
- github

## How to Use

```kotlin
plugins {
    id("io.github.sgtsilvio.gradle.metadata") version "0.6.0"
}

metadata {
    moduleName.set("org.example.library")
    readableName.set("Example library")
    license {
        apache2()
    }
    organization {
        name.set("Example Org")
        url.set("https://www.example.org")
    }
    developers {
        register("jdoe") {
            fullName.set("John Doe")
            email.set("john.doe@example.org")
        }
    }
    github {
        org.set("example")
        pages()
        issues()
    }
}
```

## Requirements

- Gradle 7.0 or higher

## Integration with Other Plugins

- `maven-publish`: automatically configures pom metadata
- `biz.aQute.bnd.builder`: automatically configures meta-inf metadata
- `java-gradle-plugin`: automatically configures Gradle plugin metadata
