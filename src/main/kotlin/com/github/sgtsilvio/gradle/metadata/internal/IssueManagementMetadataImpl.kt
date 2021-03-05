package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.IssueManagementMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
class IssueManagementMetadataImpl(objectFactory: ObjectFactory) : IssueManagementMetadata {
    override val system: Property<String> = objectFactory.property(String::class.java)
    override val url: Property<String> = objectFactory.property(String::class.java)
}