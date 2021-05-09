package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.OrganizationMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property

/**
 * @author Silvio Giebl
 */
class OrganizationMetadataImpl(objectFactory: ObjectFactory) : OrganizationMetadata {
    override val name = objectFactory.property<String>()
    override val url = objectFactory.property<String>()
}