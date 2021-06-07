package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.DeveloperMetadata
import javax.inject.Inject

/**
 * @author Silvio Giebl
 */
abstract class DeveloperMetadataImpl @Inject constructor(private val name: String) : DeveloperMetadata {
    final override fun getName() = name
}