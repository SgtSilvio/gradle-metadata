package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.LicenseMetadata

/**
 * @author Silvio Giebl
 */
abstract class LicenseMetadataImpl : LicenseMetadata {
    override fun apache2() {
        shortName.set("Apache-2.0")
        readableName.set("Apache License, Version 2.0")
        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
    }
}