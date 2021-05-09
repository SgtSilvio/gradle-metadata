package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.DeveloperMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property

/**
 * @author Silvio Giebl
 */
class DeveloperMetadataImpl(objectFactory: ObjectFactory) : DeveloperMetadata {
    override val id = objectFactory.property<String>()
    override val name = objectFactory.property<String>()
    override val email = objectFactory.property<String>()
}