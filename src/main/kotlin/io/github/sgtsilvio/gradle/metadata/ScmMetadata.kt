package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
interface ScmMetadata {
    val url: Property<String>
    val connection: Property<String>
    val developerConnection: Property<String>
}