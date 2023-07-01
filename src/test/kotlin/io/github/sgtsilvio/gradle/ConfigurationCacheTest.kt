package io.github.sgtsilvio.gradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

/**
 * @author Silvio Giebl
 */
internal class ConfigurationCacheTest {

    @Test
    fun configurationCacheReused(@TempDir projectDir: File) {
        projectDir.resolve("settings.gradle.kts").writeText(
            """
            rootProject.name = "test"
            """.trimIndent()
        )
        // TODO developers do not work, probably an incompatibility of maven-publish with the configuration cache
        projectDir.resolve("build.gradle.kts").writeText(
            """
            plugins {
                `maven-publish`
                id("io.github.sgtsilvio.gradle.metadata")
            }
            group = "com.example"
            version = "1.2.3"
            description = "Test is a generic library provided by Example Org"
            metadata {
                moduleName.set("com.example.test")
                readableName.set("Test")
                url.set("www.example.com/test")
                docUrl.set("docs.example.com/test")
                organization {
                    name.set("Example Org")
                    url.set("www.example.com")
                }
                license {
                    apache2()
                }
                github {
                    issues()
                }
            }
            publishing.publications.register<MavenPublication>("maven")
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("generatePomFileForMavenPublication", "--configuration-cache")
            .build()

        assertTrue(result.output.contains("Configuration cache entry stored"))
        check(result, projectDir)

        projectDir.resolve("build").deleteRecursively()

        val result2 = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("generatePomFileForMavenPublication", "--configuration-cache")
            .build()

        assertTrue(result2.output.contains("Configuration cache entry reused"))
        check(result2, projectDir)
    }

    private fun check(result: BuildResult, projectDir: File) {
        assertEquals(TaskOutcome.SUCCESS, result.task(":generatePomFileForMavenPublication")?.outcome)
        assertEquals(
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
              <modelVersion>4.0.0</modelVersion>
              <groupId>com.example</groupId>
              <artifactId>test</artifactId>
              <version>1.2.3</version>
              <packaging>pom</packaging>
              <name>Test</name>
              <description>Test is a generic library provided by Example Org</description>
              <url>www.example.com/test</url>
              <organization>
                <name>Example Org</name>
                <url>www.example.com</url>
              </organization>
              <licenses>
                <license>
                  <name>Apache-2.0</name>
                  <url>https://spdx.org/licenses/Apache-2.0.html</url>
                </license>
              </licenses>
              <scm>
                <connection>scm:git:https://github.com/example/test.git</connection>
                <developerConnection>scm:git:https://github.com/example/test.git</developerConnection>
                <url>https://github.com/example/test.git</url>
              </scm>
              <issueManagement>
                <system>GitHub Issues</system>
                <url>https://github.com/example/test/issues</url>
              </issueManagement>
            </project>
            """.trimIndent(),
            projectDir.resolve("build/publications/maven/pom-default.xml").readText().trim(),
        )
    }
}