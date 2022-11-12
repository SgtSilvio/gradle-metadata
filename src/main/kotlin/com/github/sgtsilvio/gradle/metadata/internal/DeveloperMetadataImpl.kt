package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.DeveloperMetadata
import javax.inject.Inject

/**
 * @author Silvio Giebl
 */
// TODO remove when bumping min required version to 7.0+
abstract class DeveloperMetadataImpl @Inject constructor(private val name: String) : DeveloperMetadata {
    final override fun getName() = name
}