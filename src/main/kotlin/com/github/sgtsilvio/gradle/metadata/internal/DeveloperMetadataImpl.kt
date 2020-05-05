package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.DeveloperMetadata

/**
 * @author Silvio Giebl
 */
class DeveloperMetadataImpl : DeveloperMetadata {
    override var id: String? = null
    override var name: String? = null
    override var email: String? = null
}