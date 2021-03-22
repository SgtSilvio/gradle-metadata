package com.github.sgtsilvio.gradle.metadata

import aQute.bnd.gradle.BundleTaskConvention
import com.github.sgtsilvio.gradle.metadata.internal.MetadataExtensionImpl
import com.github.sgtsilvio.gradle.metadata.internal.mergeProviders
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

    private fun setBndMetadata(project: Project, metadata: MetadataExtensionImpl) {
        project.plugins.withId("biz.aQute.bnd.builder") {
            project.tasks.named(JavaPlugin.JAR_TASK_NAME) { task ->
                val bundleTaskConvention = task.convention.getPlugin(BundleTaskConvention::class.java)
                bundleTaskConvention.bnd("Bundle-Name=${project.name}")
                bundleTaskConvention.bnd(project.provider { "Bundle-Description=${project.description}" })
                bundleTaskConvention.bnd(metadata.moduleName.map { moduleName -> "Automatic-Module-Name=$moduleName" })
                bundleTaskConvention.bnd(metadata.moduleName.map { moduleName -> "Bundle-SymbolicName=$moduleName" })
                metadata.withOrganization { organization ->
                    bundleTaskConvention.bnd(organization.name.map { name -> "Bundle-Vendor=$name}" })
                }
                metadata.withLicense { license ->
                    bundleTaskConvention.bnd(
                        mergeProviders(license.shortName, license.readableName, license.url) { short, readable, url ->
                            "Bundle-License=$short;description=\"$readable\";link=\"$url\""
                        })
                }
                bundleTaskConvention.bnd(metadata.docUrl.map { docUrl -> "Bundle-DocURL=$docUrl" })
                metadata.withScm { scm ->
                    bundleTaskConvention.bnd(
                        mergeProviders(scm.url, scm.connection, scm.developerConnection) { url, con, devCon ->
                            "Bundle-SCM=url=\"$url\";connection=\"$con\";developerConnection=\"$devCon\""
                        })
                }
            }
        }
    }
}