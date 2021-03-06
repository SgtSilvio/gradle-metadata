# Metadata for Gradle Projects

[![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=brightgreen&label=gradle%20plugin&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fgithub%2Fsgtsilvio%2Fgradle%2Fmetadata%2Fcom.github.sgtsilvio.gradle.metadata.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/com.github.sgtsilvio.gradle.metadata)

Gradle plugin to ease defining project metadata:
- module name
- readable name
- url, docUrl
- organization
- license
- developers
- issue management
- github

## Integration with other plugins

- `maven-publish`: automatically configures pom metadata
- `biz.aQute.bnd.builder`: automatically configures meta-inf metadata

## Example

```kotlin
plugins {
    id("com.github.sgtsilvio.gradle.metadata") version "0.2.0"
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
        developer {
            id.set("jdoe")
            name.set("John Doe")
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
