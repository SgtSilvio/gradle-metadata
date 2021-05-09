package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.LicenseMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property

/**
 * @author Silvio Giebl
 */
class LicenseMetadataImpl(objectFactory: ObjectFactory) : LicenseMetadata {
    override val shortName = objectFactory.property<String>()
    override val readableName = objectFactory.property<String>()
    override val url = objectFactory.property<String>()
}