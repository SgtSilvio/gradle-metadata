package io.github.sgtsilvio.gradle.metadata

import aQute.bnd.gradle.BundleTaskExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.configure

internal fun setBndMetadata(project: Project, metadata: MetadataExtensionImpl) {
    project.tasks.named(JavaPlugin.JAR_TASK_NAME) {
        configure<BundleTaskExtension> {
            bnd(metadata.readableName.map { readableName -> "Bundle-Name=$readableName" })
            bnd(metadata.description.map { description -> "Bundle-Description=$description" })
            bnd(metadata.moduleName.map { moduleName -> "Automatic-Module-Name=$moduleName" })
            bnd(metadata.moduleName.map { moduleName -> "Bundle-SymbolicName=$moduleName" })
            metadata.organization.whenPresent { bnd(name.map { name -> "Bundle-Vendor=$name}" }) }
            metadata.license.whenPresent {
                bnd(shortName.zip(fullName, url) { shortName, fullName, url ->
                    "Bundle-License=$shortName;description=\"$fullName\";link=\"$url\""
                })
            }
            bnd(metadata.docUrl.map { docUrl -> "Bundle-DocURL=$docUrl" })
            metadata.scm.whenPresent {
                bnd(url.zip(connection, developerConnection) { url, connection, devConnection ->
                    "Bundle-SCM=url=\"$url\";connection=\"$connection\";developerConnection=\"$devConnection\""
                })
            }
        }
    }
}
