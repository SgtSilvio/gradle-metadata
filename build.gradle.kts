plugins {
    `kotlin-dsl`
    signing
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.defaults)
    alias(libs.plugins.metadata)
}

group = "io.github.sgtsilvio.gradle"
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

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

testing {
    suites {
        "test"(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit.jupiter)
        }
    }
}
