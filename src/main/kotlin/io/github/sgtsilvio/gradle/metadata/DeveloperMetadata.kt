package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.Named
import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
interface DeveloperMetadata : Named {
    val fullName: Property<String>
    val email: Property<String>
}