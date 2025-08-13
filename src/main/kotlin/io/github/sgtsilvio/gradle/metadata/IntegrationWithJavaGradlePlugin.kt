package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

internal fun setGradlePluginMetadata(project: Project, metadata: MetadataExtensionImpl) {
    val gradlePluginExtension = project.extensions.getByType<GradlePluginDevelopmentExtension>()
    gradlePluginExtension.website.convention(metadata.url)
    gradlePluginExtension.vcsUrl.convention(metadata.scm.provider.flatMap { it.url })
    project.afterEvaluate {
        gradlePluginExtension.plugins.configureEach {
            if (displayName == null) displayName = metadata.readableName.get()
            if (description == null) description = metadata.description.get()
        }
    }
}
