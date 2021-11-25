# Metadata for Gradle Projects

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/com.github.sgtsilvio.gradle.metadata?color=brightgreen&style=for-the-badge)](https://plugins.gradle.org/plugin/com.github.sgtsilvio.gradle.metadata)

Gradle plugin to ease defining project metadata:
- module name
- readable name
- url, docUrl
- organization
- license
- developers
- issue management
- github

## Integration with Other Plugins

- `maven-publish`: automatically configures pom metadata
- `biz.aQute.bnd.builder`: automatically configures meta-inf metadata

## How to Use

```kotlin
plugins {
    id("com.github.sgtsilvio.gradle.metadata") version "0.3.0"
}

metadata {
    moduleName.set("com.github.sgtsilvio.gradle.metadata")
    readableName.set("Metadata for gradle projects")
    license {
        apache2()
    }
    organization {
        name.set("testorg")
        url.set("https://www.testorg.example/")
    }
    developers {
        register("jdoe") {
            fullName.set("John Doe")
            email.set("john@doe.example")
        }
    }
    github {
        org.set("SgtSilvio")
        repo.set("gradle-metadata")
        pages()
        issues()
    }
}
```

## Requirements

- Gradle 6.0 or higher