plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish")
}

group = "com.github.sgtsilvio.gradle"
description = "Gradle plugin to ease defining project metadata (urls, license, scm)"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("biz.aQute.bnd:biz.aQute.bnd.gradle:${property("bnd.gradle.version")}")
}

gradlePlugin {
    plugins {
        create("metadata") {
            id = "$group.$name"
            displayName = "Metadata for gradle projects"
            description = project.description
            implementationClass = "$group.metadata.MetadataPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/SgtSilvio/gradle-metadata"
    vcsUrl = "https://github.com/SgtSilvio/gradle-metadata.git"
    tags = listOf("metadata", "pom", "meta-inf")
}