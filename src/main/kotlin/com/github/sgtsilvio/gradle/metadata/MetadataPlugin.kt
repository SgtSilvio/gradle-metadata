package com.github.sgtsilvio.gradle.metadata

import aQute.bnd.gradle.BundleTaskConvention
import com.github.sgtsilvio.gradle.metadata.internal.MetadataExtensionImpl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

/**
 * @author Silvio Giebl
 */
class MetadataPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val metadata = project.extensions.create(
            MetadataExtension::class.java,
            "metadata",
            MetadataExtensionImpl::class.java
        ) as MetadataExtensionImpl

        setPomMetadata(project, metadata)
        setBndMetadata(project, metadata)
    }

    private fun setPomMetadata(project: Project, metadata: MetadataExtensionImpl) {
        project.plugins.withId("maven-publish") {
            project.extensions
                .getByType(PublishingExtension::class.java)
                .publications
                .withType(MavenPublication::class.java)
                .configureEach { mavenPublication ->
                    mavenPublication.pom { pom ->
                        pom.name.convention(metadata.readableName)
                        pom.description.convention(project.provider { project.description })
                        pom.url.convention(metadata.url)
                        metadata.withOrganization { organization ->
                            pom.organization { pomOrganization ->
                                pomOrganization.name.convention(organization.name)
                                pomOrganization.url.convention(organization.url)
                            }
                        }
                        pom.licenses { licenses ->
                            metadata.withLicense { license ->
                                licenses.license { pomLicense ->
                                    pomLicense.name.set(license.readableName)
                                    pomLicense.url.set(license.url)
                                }
                            }
                        }
                        pom.developers { developers ->
                            metadata.developers.withDeveloper { developer ->
                                developers.developer { pomDeveloper ->
                                    pomDeveloper.id.set(developer.id)
                                    pomDeveloper.name.set(developer.name)
                                    pomDeveloper.email.set(developer.email)
                                }
                            }
                        }
                        metadata.withScm { scm ->
                            pom.scm { pomScm ->
                                pomScm.connection.convention(scm.connection)
                                pomScm.developerConnection.convention(scm.developerConnection)
                                pomScm.url.convention(scm.url)
                            }
                        }
                        metadata.withIssueManagement { issueManagement ->
                            pom.issueManagement { pomIssueManagement ->
                                pomIssueManagement.system.convention(issueManagement.system)
                                pomIssueManagement.url.convention(issueManagement.url)
                            }
                        }
                    }
                }
        }
    }

    private fun setBndMetadata(project: Project, metadata: MetadataExtension) {
        project.plugins.withId("biz.aQute.bnd.builder") {
            project.afterEvaluate {
                project.tasks.named(JavaPlugin.JAR_TASK_NAME) { task ->
                    val bundleTaskConvention = task.convention.getPlugin(BundleTaskConvention::class.java)
                    bundleTaskConvention.bnd("Bundle-Name=${project.name}")
                    bundleTaskConvention.bnd("Bundle-Description=${project.description}")
                    metadata.moduleName.orNull?.let { moduleName ->
                        bundleTaskConvention.bnd("Automatic-Module-Name=$moduleName")
                        bundleTaskConvention.bnd("Bundle-SymbolicName=$moduleName")
                    }
                    metadata.organization?.let { organization ->
                        bundleTaskConvention.bnd("Bundle-Vendor=${organization.name.get()}")
                    }
                    metadata.license?.let { license ->
                        bundleTaskConvention.bnd(
                            "Bundle-License=${license.shortName.get()};" +
                                    "description=\"${license.readableName.get()}\";" +
                                    "link=\"${license.url.get()}\""
                        )
                    }
                    metadata.docUrl.orNull?.let { docUrl ->
                        bundleTaskConvention.bnd("Bundle-DocURL=$docUrl")
                    }
                    metadata.scm?.let { scm ->
                        bundleTaskConvention.bnd(
                            "Bundle-SCM=url=\"${scm.url.get()}\";" +
                                    "connection=\"${scm.connection.get()}\";" +
                                    "developerConnection=\"${scm.developerConnection.get()}\""
                        )
                    }
                }
            }
        }
    }
}