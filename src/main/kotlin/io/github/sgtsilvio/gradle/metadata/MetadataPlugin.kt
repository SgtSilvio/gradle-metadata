package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

/**
 * @author Silvio Giebl
 */
@Suppress("unused")
class MetadataPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val metadata = project.extensions.create(
            MetadataExtension::class,
            EXTENSION_NAME,
            MetadataExtensionImpl::class,
            project,
        ) as MetadataExtensionImpl

        project.plugins.withId("org.gradle.maven-publish") {
            setPomMetadata(project, metadata)
        }
        project.plugins.withId("biz.aQute.bnd.builder") {
            setBndMetadata(project, metadata)
        }
        project.plugins.withId("org.gradle.java-gradle-plugin") {
            setGradlePluginMetadata(project, metadata)
        }
    }
}

const val EXTENSION_NAME = "metadata"