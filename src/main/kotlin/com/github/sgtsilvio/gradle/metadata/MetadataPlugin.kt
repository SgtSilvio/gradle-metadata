package com.github.sgtsilvio.gradle.metadata

import aQute.bnd.gradle.BundleTaskConvention
import com.github.sgtsilvio.gradle.metadata.internal.MetadataExtensionImpl
import com.github.sgtsilvio.gradle.metadata.internal.mergeProviders
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withConvention
import org.gradle.kotlin.dsl.withType

/**
 * @author Silvio Giebl
 */
class MetadataPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val metadata = project.extensions.create(
            MetadataExtension::class,
            "metadata",
            MetadataExtensionImpl::class
        ) as MetadataExtensionImpl

        project.plugins.withId("maven-publish") {
            setPomMetadata(project, metadata)
        }
        project.plugins.withId("biz.aQute.bnd.builder") {
            setBndMetadata(project, metadata)
        }
    }

    private fun setPomMetadata(project: Project, metadata: MetadataExtensionImpl) {
        project.the<PublishingExtension>().publications.withType<MavenPublication> {
            pom {
                name.convention(metadata.readableName)
                description.convention(project.provider { project.description })
                url.convention(metadata.url)
                metadata.withOrganization { organization ->
                    organization {
                        name.convention(organization.name)
                        url.convention(organization.url)
                    }
                }
                licenses {
                    metadata.withLicense { license ->
                        license {
                            name.set(license.readableName)
                            url.set(license.url)
                        }
                    }
                }
                developers {
                    metadata.developers.withDeveloper { developer ->
                        developer {
                            id.set(developer.id)
                            name.set(developer.name)
                            email.set(developer.email)
                        }
                    }
                }
                metadata.withScm { scm ->
                    scm {
                        connection.convention(scm.connection)
                        developerConnection.convention(scm.developerConnection)
                        url.convention(scm.url)
                    }
                }
                metadata.withIssueManagement { issueManagement ->
                    issueManagement {
                        system.convention(issueManagement.system)
                        url.convention(issueManagement.url)
                    }
                }
            }
        }
    }

    private fun setBndMetadata(project: Project, metadata: MetadataExtensionImpl) {
        project.tasks.named(JavaPlugin.JAR_TASK_NAME) {
            withConvention(BundleTaskConvention::class) {
                bnd("Bundle-Name=${project.name}")
                bnd(project.provider { "Bundle-Description=${project.description}" })
                bnd(metadata.moduleName.map { moduleName -> "Automatic-Module-Name=$moduleName" })
                bnd(metadata.moduleName.map { moduleName -> "Bundle-SymbolicName=$moduleName" })
                metadata.withOrganization { organization ->
                    bnd(organization.name.map { name -> "Bundle-Vendor=$name}" })
                }
                metadata.withLicense { license ->
                    bnd(mergeProviders(
                        license.shortName,
                        license.readableName,
                        license.url
                    ) { shortName, readableName, url ->
                        "Bundle-License=$shortName;description=\"$readableName\";link=\"$url\""
                    })
                }
                bnd(metadata.docUrl.map { docUrl -> "Bundle-DocURL=$docUrl" })
                metadata.withScm { scm ->
                    bnd(mergeProviders(
                        scm.url,
                        scm.connection,
                        scm.developerConnection
                    ) { url, connection, devConnection ->
                        "Bundle-SCM=url=\"$url\";connection=\"$connection\";developerConnection=\"$devConnection\""
                    })
                }
            }
        }
    }
}