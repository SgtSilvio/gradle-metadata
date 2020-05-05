package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.DeveloperMetadata
import com.github.sgtsilvio.gradle.metadata.DevelopersMetadata
import org.gradle.api.Action
import java.util.*

/**
 * @author Silvio Giebl
 */
class DevelopersMetadataImpl : DevelopersMetadata, LinkedList<DeveloperMetadata>() {
    override fun developer(action: Action<in DeveloperMetadata>) {
        val developer = DeveloperMetadataImpl()
        add(developer)
        action.execute(developer)
    }
}