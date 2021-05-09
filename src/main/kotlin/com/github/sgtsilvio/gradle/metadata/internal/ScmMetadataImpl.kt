package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.ScmMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property

/**
 * @author Silvio Giebl
 */
class ScmMetadataImpl(objectFactory: ObjectFactory) : ScmMetadata {
    override val url = objectFactory.property<String>()
    override val connection = objectFactory.property<String>()
    override val developerConnection = objectFactory.property<String>()
}