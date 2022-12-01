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
    website.set(metadata.url)
    vcsUrl.set(metadata.scm.get().url)
    plugins {
        create("metadata") {
            id = "$group.$name"
            implementationClass = "$group.$name.MetadataPlugin"
            displayName = metadata.readableName.get()
            description = project.description
            tags.set(listOf("metadata", "pom", "meta-inf"))
        }
    }
}

testing {
    suites.named<JvmTestSuite>("test") {
        useJUnitJupiter(libs.versions.junit.jupiter)
    }
}
