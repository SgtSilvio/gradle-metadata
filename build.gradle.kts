plugins {
    kotlin("jvm") version "1.3.72"
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.11.0"
}

group = "com.github.sgtsilvio.gradle"
version = "0.1.0"
description = "Gradle plugin to ease defining project metadata (urls, licenses, scm)"

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("biz.aQute.bnd:biz.aQute.bnd.gradle:5.0.1")
}

gradlePlugin {
    plugins {
        create("metadata") {
            id = "com.github.sgtsilvio.gradle.metadata"
            displayName = "Metadata for projects"
            description = project.description
            implementationClass = "com.github.sgtsilvio.gradle.metadata.MetadataPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/SgtSilvio/gradle-metadata"
    vcsUrl = "https://github.com/SgtSilvio/gradle-metadata.git"
    tags = listOf("metadata", "pom", "meta-inf")
}
