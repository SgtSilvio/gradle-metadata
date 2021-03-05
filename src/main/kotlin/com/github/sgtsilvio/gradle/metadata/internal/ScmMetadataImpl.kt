package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.ScmMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
class ScmMetadataImpl(objectFactory: ObjectFactory) : ScmMetadata {
    override val url: Property<String> = objectFactory.property(String::class.java)
    override val connection: Property<String> = objectFactory.property(String::class.java)
    override val developerConnection: Property<String> = objectFactory.property(String::class.java)
}