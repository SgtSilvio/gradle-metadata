package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.ScmMetadata

/**
 * @author Silvio Giebl
 */
class ScmMetadataImpl : ScmMetadata {
    override var url: String? = null
    override var connection: String? = null
    override var developerConnection: String? = null
}