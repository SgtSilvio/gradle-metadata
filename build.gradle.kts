@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `kotlin-dsl`
    alias(libs.plugins.plugin.publish)
    alias(libs.plugins.defaults)
    alias(libs.plugins.metadata)
}

group = "com.github.sgtsilvio.gradle"
description = "Gradle plugin to ease defining project metadata (urls, license, scm)"

metadata {
    readableName.set("Metadata for Gradle Projects")
    license {
        apache2()
    }
    developers {
        register("SgtSilvio") {
            fullName.set("Silvio Giebl")
        }
    }
    github {
        org.set("SgtSilvio")
        repo.set("gradle-metadata")
        issues()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.bnd)
}

gradlePlugin {
    plugins {
        create("metadata") {
            id = "$group.$name"
            displayName = metadata.readableName.get()
            description = project.description
            implementationClass = "$group.$name.MetadataPlugin"
        }
    }
}

pluginBundle {
    website = metadata.url.get()
    vcsUrl = metadata.scm.get().url.get()
    tags = listOf("metadata", "pom", "meta-inf")
}
