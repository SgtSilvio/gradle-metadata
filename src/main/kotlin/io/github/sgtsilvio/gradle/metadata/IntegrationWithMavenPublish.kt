package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

internal fun setPomMetadata(project: Project, metadata: MetadataExtensionImpl) {
    project.extensions.getByType<PublishingExtension>().publications.withType<MavenPublication>().configureEach {
        pom {
            name.convention(metadata.readableName)
            description.convention(metadata.description)
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
                        name.set(this@whenPresent.shortName)
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
