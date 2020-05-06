plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish")
}

group = "com.github.sgtsilvio.gradle"
version = "${property("version")}"
description = "Gradle plugin to ease defining project metadata (urls, license, scm)"

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
    implementation("biz.aQute.bnd:biz.aQute.bnd.gradle:${property("bnd.gradle.version")}")
}

gradlePlugin {
    plugins {
        create("metadata") {
            id = "com.github.sgtsilvio.gradle.metadata"
            displayName = "Metadata for gradle projects"
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
