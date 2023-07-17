package io.github.sgtsilvio.gradle.metadata

import javax.inject.Inject

/**
 * @author Silvio Giebl
 */
// TODO remove when bumping min required version to 7.0+
internal abstract class DeveloperMetadataImpl @Inject constructor(private val name: String) : DeveloperMetadata {
    final override fun getName() = name
}