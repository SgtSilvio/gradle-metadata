package com.github.sgtsilvio.gradle.metadata

import aQute.bnd.gradle.BundleTaskConvention
import com.github.sgtsilvio.gradle.metadata.internal.MetadataExtensionImpl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

/**
 * @author Silvio Giebl
 */
class MetadataPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val metadata = project.extensions.create(MetadataExtension::class.java, "metadata", MetadataExtensionImpl::class.java)
        project.afterEvaluate {
            setPomMetadata(it, metadata)
            setBndMetadata(it, metadata)
        }
    }

    private fun setPomMetadata(project: Project, metadata: MetadataExtension) {
        project.plugins.withId("maven-publish") {
            project.extensions
                    .getByType(PublishingExtension::class.java)
                    .publications
                    .withType(MavenPublication::class.java)
                    .configureEach { mavenPublication ->
                        mavenPublication.pom { pom ->
                            pom.name.set(metadata.readableName)
                            pom.description.set(project.description)
                            pom.url.set(metadata.url)
                            pom.organization { organization ->
                                organization.name.set(metadata.organization.name)
                                organization.url.set(metadata.organization.url)
                            }
                            pom.licenses { licenses ->
                                licenses.license { license ->
                                    license.name.set(metadata.license.readableName)
                                    license.url.set(metadata.license.url)
                                }
                            }
                            pom.developers { developers ->
                                metadata.developers.forEach {
                                    developers.developer { developer ->
                                        developer.id.set(it.id)
                                        developer.name.set(it.name)
                                        developer.email.set(it.email)
                                    }
                                }
                            }
                            pom.scm { scm ->
                                scm.connection.set(metadata.scm.connection)
                                scm.developerConnection.set(metadata.scm.developerConnection)
                                scm.url.set(metadata.scm.url)
                            }
                            pom.issueManagement { issueManagement ->
                                issueManagement.system.set(metadata.issueManagement.system)
                                issueManagement.url.set(metadata.issueManagement.url)
                            }
                        }
                    }
        }
    }

    private fun setBndMetadata(project: Project, metadata: MetadataExtension) {
        project.plugins.withId("biz.aQute.bnd.builder") {
            project.tasks.named("jar") { task ->
                task.convention.getPlugin(BundleTaskConvention::class.java).bnd(
                        "Automatic-Module-Name=${metadata.moduleName}",
                        "Bundle-Name=${project.name}",
                        "Bundle-SymbolicName=${metadata.moduleName}",
                        "Bundle-Description=${project.description}",
                        "Bundle-Vendor=${metadata.organization.name}",
                        "Bundle-License=${metadata.license.shortName};" +
                                "description=\"${metadata.license.readableName}\";" +
                                "link=\"${metadata.license.url}\"",
                        "Bundle-DocURL=${metadata.docUrl}",
                        "Bundle-SCM=url=\"${metadata.scm.url}\";" +
                                "connection=\"${metadata.scm.connection}\";" +
                                "developerConnection=\"${metadata.scm.developerConnection}\"")
            }
        }
    }
}