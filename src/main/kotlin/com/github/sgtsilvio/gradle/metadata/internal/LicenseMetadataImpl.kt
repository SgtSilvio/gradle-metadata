package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.LicenseMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
class LicenseMetadataImpl(objectFactory: ObjectFactory) : LicenseMetadata {
    override val shortName: Property<String> = objectFactory.property(String::class.java)
    override val readableName: Property<String> = objectFactory.property(String::class.java)
    override val url: Property<String> = objectFactory.property(String::class.java)
}