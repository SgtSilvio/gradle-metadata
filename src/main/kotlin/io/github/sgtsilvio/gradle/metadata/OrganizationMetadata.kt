package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
interface OrganizationMetadata {
    val name: Property<String>
    val url: Property<String>
}