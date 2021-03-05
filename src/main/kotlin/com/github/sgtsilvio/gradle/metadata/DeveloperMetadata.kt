package com.github.sgtsilvio.gradle.metadata

import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
interface DeveloperMetadata {
    val id: Property<String>
    val name: Property<String>
    val email: Property<String>
}