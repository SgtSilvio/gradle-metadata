package io.github.sgtsilvio.gradle.metadata

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.Properties

/**
 * @author Silvio Giebl
 */
internal class GradlePluginDevelopmentInteroperabilityTest {

    @Test
    fun test(@TempDir projectDir: File) {
        projectDir.resolve("settings.gradle.kts").writeText(
            """
            rootProject.name = "test"
            """.trimIndent()
        )
        projectDir.resolve("build.gradle.kts").writeText(
            """
            import java.util.Properties
            plugins {
                `java-gradle-plugin`
                id("io.github.sgtsilvio.gradle.metadata")
            }
            group = "com.example"
            version = "1.2.3"
            description = "Test is a generic gradle plugin provided by Example Org"
            metadata {
                readableName.set("Test")
                url.set("www.example.com/test")
                github {}
            }
            gradlePlugin {
                plugins {
                    create("test") {
                        id = "com.example.test"
                        implementationClass = "com.example.test.TestPlugin"
                    }
                    create("test2") {
                        id = "com.example.test2"
                        implementationClass = "com.example.test.Test2Plugin"
                    }
                    create("test3") {
                        id = "com.example.test3"
                        implementationClass = "com.example.test.Test3Plugin"
                        displayName = "Test 3 (custom)"
                        description = "Test 3 description (custom)"
                    }
                }
            }
            tasks.register("gradlePluginProperties") {
                doLast {
                    val properties = Properties()
                    properties["website"] = gradlePlugin.website.get()
                    properties["vcsUrl"] = gradlePlugin.vcsUrl.get()
                    val testPlugin = gradlePlugin.plugins["test"]
                    properties["plugins.test.displayName"] = testPlugin.displayName
                    properties["plugins.test.description"] = testPlugin.description
                    val test2Plugin = gradlePlugin.plugins["test2"]
                    properties["plugins.test2.displayName"] = test2Plugin.displayName
                    properties["plugins.test2.description"] = test2Plugin.description
                    val test3Plugin = gradlePlugin.plugins["test3"]
                    properties["plugins.test3.displayName"] = test3Plugin.displayName
                    properties["plugins.test3.description"] = test3Plugin.description
                    file("gradle-plugin.properties").outputStream().use {
                        properties.store(it, null)
                    }
                }
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("gradlePluginProperties")
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":gradlePluginProperties")?.outcome)

        val properties = Properties()
        projectDir.resolve("gradle-plugin.properties").inputStream().use {
            properties.load(it)
        }
        assertEquals("www.example.com/test", properties["website"])
        assertEquals("https://github.com/example/test.git", properties["vcsUrl"])
        assertEquals("Test", properties["plugins.test.displayName"])
        assertEquals("Test is a generic gradle plugin provided by Example Org", properties["plugins.test.description"])
        assertEquals("Test", properties["plugins.test2.displayName"])
        assertEquals("Test is a generic gradle plugin provided by Example Org", properties["plugins.test2.description"])
        assertEquals("Test 3 (custom)", properties["plugins.test3.displayName"])
        assertEquals("Test 3 description (custom)", properties["plugins.test3.description"])
    }
}