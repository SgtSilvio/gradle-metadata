package com.github.sgtsilvio.gradle.metadata

import org.gradle.api.Action

/**
 * @author Silvio Giebl
 */
interface DevelopersMetadata : Iterable<DeveloperMetadata> {
    fun developer(action: Action<in DeveloperMetadata>)
}