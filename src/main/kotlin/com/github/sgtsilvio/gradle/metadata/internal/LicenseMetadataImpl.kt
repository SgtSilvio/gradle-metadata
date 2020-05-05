package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.LicenseMetadata

/**
 * @author Silvio Giebl
 */
class LicenseMetadataImpl : LicenseMetadata {
    override var shortName: String? = null
    override var readableName: String? = null
    override var url: String? = null
}