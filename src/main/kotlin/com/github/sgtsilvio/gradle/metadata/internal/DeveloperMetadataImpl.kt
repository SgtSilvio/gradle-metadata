package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.DeveloperMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
class DeveloperMetadataImpl(objectFactory: ObjectFactory) : DeveloperMetadata {
    override val id: Property<String> = objectFactory.property(String::class.java)
    override val name: Property<String> = objectFactory.property(String::class.java)
    override val email: Property<String> = objectFactory.property(String::class.java)
}