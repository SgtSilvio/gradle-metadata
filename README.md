# Metadata for Gradle Projects

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
    id("com.github.sgtsilvio.gradle.metadata") version "0.1.0"
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
            email = "johnl@doe.example"
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