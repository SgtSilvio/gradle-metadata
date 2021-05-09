package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.IssueManagementMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property

/**
 * @author Silvio Giebl
 */
class IssueManagementMetadataImpl(objectFactory: ObjectFactory) : IssueManagementMetadata {
    override val system = objectFactory.property<String>()
    override val url = objectFactory.property<String>()
}