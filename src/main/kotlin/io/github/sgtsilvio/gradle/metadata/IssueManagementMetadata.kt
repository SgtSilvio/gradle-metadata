package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
interface IssueManagementMetadata {
    val system: Property<String>
    val url: Property<String>
}