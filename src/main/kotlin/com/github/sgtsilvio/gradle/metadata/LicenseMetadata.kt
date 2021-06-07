package com.github.sgtsilvio.gradle.metadata

import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
interface LicenseMetadata {
    val shortName: Property<String>
    val readableName: Property<String>
    val url: Property<String>

    fun apache2()
}