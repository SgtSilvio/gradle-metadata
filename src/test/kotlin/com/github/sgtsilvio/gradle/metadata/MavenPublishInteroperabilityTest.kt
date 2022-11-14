package com.github.sgtsilvio.gradle.metadata

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

/**
 * @author Silvio Giebl
 */
class MavenPublishInteroperabilityTest {

    @TempDir
    private lateinit var projectDir: File

    private fun test(buildFileContent: String, expectedPomFileContent: String) {
        projectDir.resolve("settings.gradle.kts").writeText(
            """
            rootProject.name = "test"
            """.trimIndent()
        )
        projectDir.resolve("build.gradle.kts").writeText(
            """
            |plugins {
            |    `maven-publish`
            |    id("com.github.sgtsilvio.gradle.metadata")
            |}
            |group = "com.example"
            |version = "1.2.3"
            |$buildFileContent
            |publishing.publications.register<MavenPublication>("maven")
            """.trimMargin()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("generatePomFileForMavenPublication")
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":generatePomFileForMavenPublication")?.outcome)
        assertEquals(
            """
            |<?xml version="1.0" encoding="UTF-8"?>
            |<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
            |    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            |  <modelVersion>4.0.0</modelVersion>
            |  <groupId>com.example</groupId>
            |  <artifactId>test</artifactId>
            |  <version>1.2.3</version>
            |  <packaging>pom</packaging>
            |${expectedPomFileContent.prependIndent("  ")}
            |</project>
            """.trimMargin(),
            projectDir.resolve("build/publications/maven/pom-default.xml").readText().trim(),
        )
    }

    @Test
    fun whenOnlyDescriptionSet_thenOnlyDescriptionInPom() {
        test(
            """
            description = "Test is a generic library provided by Example Org"
            """.trimIndent(),
            """
            <description>Test is a generic library provided by Example Org</description>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyReadableNameSet_thenOnlyNameInPom() {
        test(
            """
            metadata {
                readableName.set("Test")
            }
            """.trimIndent(),
            """
            <name>Test</name>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyUrlSet_thenOnlyUrlInPom() {
        test(
            """
            metadata {
                url.set("www.example.com/test")
            }
            """.trimIndent(),
            """
            <url>www.example.com/test</url>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyOrganizationNameSet_thenOnlyOrganizationNameInPom() {
        test(
            """
            metadata {
                organization {
                    name.set("Example Org")
                }
            }
            """.trimIndent(),
            """
            <organization>
              <name>Example Org</name>
            </organization>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyOrganizationUrlSet_thenOnlyOrganizationUrlInPom() {
        test(
            """
            metadata {
                organization {
                    url.set("www.example.com")
                }
            }
            """.trimIndent(),
            """
            <organization>
              <url>www.example.com</url>
            </organization>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyLicenseReadableNameSet_thenOnlyLicenseNameInPom() {
        test(
            """
            metadata {
                license {
                    readableName.set("Apache License, Version 2.0")
                }
            }
            """.trimIndent(),
            """
            <licenses>
              <license>
                <name>Apache License, Version 2.0</name>
              </license>
            </licenses>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyLicenseUrlSet_thenOnlyLicenseUrlInPom() {
        test(
            """
            metadata {
                license {
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            """.trimIndent(),
            """
            <licenses>
              <license>
                <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
              </license>
            </licenses>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyLicenseApacheSet_thenOnlyLicenseInPom() {
        test(
            """
            metadata {
                license {
                    apache2()
                }
            }
            """.trimIndent(),
            """
            <licenses>
              <license>
                <name>Apache License, Version 2.0</name>
                <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
              </license>
            </licenses>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyDeveloperIdSet_thenOnlyDeveloperIdInPom() {
        test(
            """
            metadata {
                developers {
                    register("jdoe")
                }
            }
            """.trimIndent(),
            """
            <developers>
              <developer>
                <id>jdoe</id>
              </developer>
            </developers>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyDeveloperIdAndFullNameSet_thenOnlyDeveloperIdAndNameInPom() {
        test(
            """
            metadata {
                developers {
                    register("jdoe") {
                        fullName.set("John Doe")
                    }
                }
            }
            """.trimIndent(),
            """
            <developers>
              <developer>
                <id>jdoe</id>
                <name>John Doe</name>
              </developer>
            </developers>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyDeveloperIdAndEmailSet_thenOnlyDeveloperIdAndEmailInPom() {
        test(
            """
            metadata {
                developers {
                    register("jdoe") {
                        email.set("john.doe@example.com")
                    }
                }
            }
            """.trimIndent(),
            """
            <developers>
              <developer>
                <id>jdoe</id>
                <email>john.doe@example.com</email>
              </developer>
            </developers>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyMultipleDevelopersSet_thenOnlyMultipleDevelopersInPom() {
        test(
            """
            metadata {
                developers {
                    register("jdoe1") {
                        fullName.set("John Doe1")
                        email.set("john.doe1@example.com")
                    }
                    register("jdoe2") {
                        fullName.set("John Doe2")
                        email.set("john.doe2@example.com")
                    }
                }
            }
            """.trimIndent(),
            """
            <developers>
              <developer>
                <id>jdoe1</id>
                <name>John Doe1</name>
                <email>john.doe1@example.com</email>
              </developer>
              <developer>
                <id>jdoe2</id>
                <name>John Doe2</name>
                <email>john.doe2@example.com</email>
              </developer>
            </developers>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyScmUrlSet_thenOnlyScmUrlInPom() {
        test(
            """
            metadata {
                scm {
                    url.set("https://github.com/example/test.git")
                }
            }
            """.trimIndent(),
            """
            <scm>
              <url>https://github.com/example/test.git</url>
            </scm>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyScmConnectionSet_thenOnlyScmConnectionInPom() {
        test(
            """
            metadata {
                scm {
                    connection.set("scm:git:https://github.com/example/test.git")
                }
            }
            """.trimIndent(),
            """
            <scm>
              <connection>scm:git:https://github.com/example/test.git</connection>
            </scm>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyScmDeveloperConnectionSet_thenOnlyScmDeveloperConnectionInPom() {
        test(
            """
            metadata {
                scm {
                    developerConnection.set("scm:git:https://github.com/example/test.git")
                }
            }
            """.trimIndent(),
            """
            <scm>
              <developerConnection>scm:git:https://github.com/example/test.git</developerConnection>
            </scm>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyIssueManagementSystemSet_thenOnlyIssueManagementSystemInPom() {
        test(
            """
            metadata {
                issueManagement {
                    system.set("GitHub Issues")
                }
            }
            """.trimIndent(),
            """
            <issueManagement>
              <system>GitHub Issues</system>
            </issueManagement>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyIssueManagementUrlSet_thenOnlyIssueManagementUrlInPom() {
        test(
            """
            metadata {
                issueManagement {
                    url.set("https://github.com/example/test/issues")
                }
            }
            """.trimIndent(),
            """
            <issueManagement>
              <url>https://github.com/example/test/issues</url>
            </issueManagement>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyGitHubSet_thenOnlyUrlAndScmInPom() {
        test(
            """
            metadata {
                github {
                    org.set("example")
                    repo.set("test")
                }
            }
            """.trimIndent(),
            """
            <url>https://github.com/example/test</url>
            <scm>
              <connection>scm:git:https://github.com/example/test.git</connection>
              <developerConnection>scm:git:https://github.com/example/test.git</developerConnection>
              <url>https://github.com/example/test.git</url>
            </scm>
            """.trimIndent(),
        )
    }

    @Test
    fun whenGitHubAndUrlSet_thenUrlTakesPrecedenceOverGitHubInPom() {
        test(
            """
            metadata {
                url.set("www.example.com/test")
                github {
                    org.set("example")
                    repo.set("test")
                }
            }
            """.trimIndent(),
            """
            <url>www.example.com/test</url>
            <scm>
              <connection>scm:git:https://github.com/example/test.git</connection>
              <developerConnection>scm:git:https://github.com/example/test.git</developerConnection>
              <url>https://github.com/example/test.git</url>
            </scm>
            """.trimIndent(),
        )
    }

    @Test
    fun whenGitHubAndScmSet_thenScmTakesPrecedenceOverGitHubInPom() {
        test(
            """
            metadata {
                scm {
                    url.set("https://github-custom.com/example/test.git")
                    connection.set("scm:git:https://github-custom.com/example/test.git")
                    developerConnection.set("scm:git:https://github-custom.com/example/test.git")
                }
                github {
                    org.set("example")
                    repo.set("test")
                }
            }
            """.trimIndent(),
            """
            <url>https://github.com/example/test</url>
            <scm>
              <connection>scm:git:https://github-custom.com/example/test.git</connection>
              <developerConnection>scm:git:https://github-custom.com/example/test.git</developerConnection>
              <url>https://github-custom.com/example/test.git</url>
            </scm>
            """.trimIndent(),
        )
    }

    @Test
    fun whenOnlyGitHubWithIssuesSet_thenOnlyUrlAndScmAndIssueManagementInPom() {
        test(
            """
            metadata {
                github {
                    org.set("example")
                    repo.set("test")
                    issues()
                }
            }
            """.trimIndent(),
            """
            <url>https://github.com/example/test</url>
            <scm>
              <connection>scm:git:https://github.com/example/test.git</connection>
              <developerConnection>scm:git:https://github.com/example/test.git</developerConnection>
              <url>https://github.com/example/test.git</url>
            </scm>
            <issueManagement>
              <system>GitHub Issues</system>
              <url>https://github.com/example/test/issues</url>
            </issueManagement>
            """.trimIndent(),
        )
    }

    @Test
    fun whenGitHubWithIssuesAndIssueManagementSet_thenIssueManagementTakesPrecedenceOverGitHubInPom() {
        test(
            """
            metadata {
                issueManagement {
                    system.set("GitHub Custom Issues")
                    url.set("https://github-custom.com/example/test/issues")
                }
                github {
                    org.set("example")
                    repo.set("test")
                    issues()
                }
            }
            """.trimIndent(),
            """
            <url>https://github.com/example/test</url>
            <scm>
              <connection>scm:git:https://github.com/example/test.git</connection>
              <developerConnection>scm:git:https://github.com/example/test.git</developerConnection>
              <url>https://github.com/example/test.git</url>
            </scm>
            <issueManagement>
              <system>GitHub Custom Issues</system>
              <url>https://github-custom.com/example/test/issues</url>
            </issueManagement>
            """.trimIndent(),
        )
    }

    @Test
    fun whenAllPropertiesSet_thenAllPropertiesInPom() {
        test(
            """
            description = "Test is a generic library provided by Example Org"
            metadata {
                readableName.set("Test")
                url.set("www.example.com/test")
                organization {
                    name.set("Example Org")
                    url.set("www.example.com")
                }
                license {
                    apache2()
                }
                developers {
                    register("jdoe") {
                        fullName.set("John Doe")
                        email.set("john.doe@example.com")
                    }
                }
                github {
                    org.set("example")
                    repo.set("test")
                    issues()
                }
            }
            """.trimIndent(),
            """
            <name>Test</name>
            <description>Test is a generic library provided by Example Org</description>
            <url>www.example.com/test</url>
            <organization>
              <name>Example Org</name>
              <url>www.example.com</url>
            </organization>
            <licenses>
              <license>
                <name>Apache License, Version 2.0</name>
                <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
              </license>
            </licenses>
            <developers>
              <developer>
                <id>jdoe</id>
                <name>John Doe</name>
                <email>john.doe@example.com</email>
              </developer>
            </developers>
            <scm>
              <connection>scm:git:https://github.com/example/test.git</connection>
              <developerConnection>scm:git:https://github.com/example/test.git</developerConnection>
              <url>https://github.com/example/test.git</url>
            </scm>
            <issueManagement>
              <system>GitHub Issues</system>
              <url>https://github.com/example/test/issues</url>
            </issueManagement>
            """.trimIndent(),
        )
    }
}