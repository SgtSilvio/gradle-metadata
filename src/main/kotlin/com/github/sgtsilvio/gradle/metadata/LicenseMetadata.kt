package com.github.sgtsilvio.gradle.metadata

import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
interface LicenseMetadata {
    val shortName: Property<String>
    val readableName: Property<String>
    val url: Property<String>

    fun apache2() {
        shortName.set("Apache-2.0")
        readableName.set("Apache License 2.0")
        url.set("https://spdx.org/licenses/Apache-2.0.html")
    }
}