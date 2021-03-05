package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.OrganizationMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
class OrganizationMetadataImpl(objectFactory: ObjectFactory) : OrganizationMetadata {
    override val name: Property<String> = objectFactory.property(String::class.java)
    override val url: Property<String> = objectFactory.property(String::class.java)
}