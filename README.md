# Metadata for Gradle Projects

[![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=brightgreen&label=gradle%20plugin&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fgithub%2Fsgtsilvio%2Fgradle%2Fmetadata%2Fcom.github.sgtsilvio.gradle.metadata.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/com.github.sgtsilvio.gradle.metadata)

Gradle plugin to ease defining project metadata:
- module name
- readable name
- url, docUrl
- organization
- license
- developer
- issue management
- github

## Integration with other plugins

- `maven-publish`: automatically configures pom metadata
- `biz.aQute.bnd.builder`: automatically configures meta-inf metadata

## Example

```kotlin
plugins {
    id("com.github.sgtsilvio.gradle.metadata") version "0.1.1"
}

metadata {
    moduleName = "com.github.sgtsilvio.gradle.metadata"
    readableName = "Metadata for gradle projects"
    license {
        apache2()
    }
    organization {
        name = "testorg"
        url = "https://www.testorg.example/"
    }
    developers {
        developer {
            id = "jdoe"
            name = "John Doe"
            email = "john@doe.example"
        }
    }
    github {
        org = "SgtSilvio"
        repo = "gradle-metadata"
        pages()
        issues()
    }
}
```
