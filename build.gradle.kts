import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
    signing
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.defaults)
    alias(libs.plugins.metadata)
}

group = "io.github.sgtsilvio.gradle"

metadata {
    readableName = "Metadata for Gradle Projects"
    description = "Gradle plugin to ease defining project metadata (urls, license, scm)"
    license {
        apache2()
    }
    developers {
        register("SgtSilvio") {
            fullName = "Silvio Giebl"
        }
    }
    github {
        org = "SgtSilvio"
        issues()
    }
}

kotlin {
    jvmToolchain(8)
}

tasks.compileKotlin {
    compilerOptions {
        apiVersion = KotlinVersion.KOTLIN_2_0
        languageVersion = KotlinVersion.KOTLIN_2_0
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
            implementationClass = "$group.$name.MetadataPlugin"
            tags = listOf("metadata", "pom", "meta-inf")
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
            targets.configureEach {
                testTask {
                    javaLauncher = javaToolchains.launcherFor {
                        languageVersion = JavaLanguageVersion.of(17)
                    }
                }
            }
        }
    }
}
