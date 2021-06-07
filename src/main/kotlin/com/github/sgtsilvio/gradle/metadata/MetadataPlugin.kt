package com.github.sgtsilvio.gradle.metadata

import aQute.bnd.gradle.BundleTaskConvention
import com.github.sgtsilvio.gradle.metadata.internal.MetadataExtensionImpl
import com.github.sgtsilvio.gradle.metadata.internal.merge
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
                metadata.organization.whenPresent {
                    organization {
                        name.convention(this@whenPresent.name)
                        url.convention(this@whenPresent.url)
                    }
                }
                licenses {
                    metadata.license.whenPresent {
                        license {
                            name.set(this@whenPresent.readableName)
                            url.set(this@whenPresent.url)
                        }
                    }
                }
                developers {
                    metadata.developers.all {
                        developer {
                            id.set(this@all.name)
                            name.set(this@all.fullName)
                            email.set(this@all.email)
                        }
                    }
                }
                metadata.scm.whenPresent {
                    scm {
                        connection.convention(this@whenPresent.connection)
                        developerConnection.convention(this@whenPresent.developerConnection)
                        url.convention(this@whenPresent.url)
                    }
                }
                metadata.issueManagement.whenPresent {
                    issueManagement {
                        system.convention(this@whenPresent.system)
                        url.convention(this@whenPresent.url)
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
                metadata.organization.whenPresent { bnd(name.map { name -> "Bundle-Vendor=$name}" }) }
                metadata.license.whenPresent {
                    bnd(shortName.merge(readableName, url) { shortName, readableName, url ->
                        "Bundle-License=$shortName;description=\"$readableName\";link=\"$url\""
                    })
                }
                bnd(metadata.docUrl.map { docUrl -> "Bundle-DocURL=$docUrl" })
                metadata.scm.whenPresent {
                    bnd(url.merge(connection, developerConnection) { url, connection, devConnection ->
                        "Bundle-SCM=url=\"$url\";connection=\"$connection\";developerConnection=\"$devConnection\""
                    })
                }
            }
        }
    }
}