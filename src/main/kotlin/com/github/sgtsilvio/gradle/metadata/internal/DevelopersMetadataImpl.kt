package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.DeveloperMetadata
import com.github.sgtsilvio.gradle.metadata.DevelopersMetadata
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import java.util.*

/**
 * @author Silvio Giebl
 */
class DevelopersMetadataImpl(private val objectFactory: ObjectFactory) : DevelopersMetadata {

    private val developers: MutableList<DeveloperMetadataImpl> = LinkedList()
    private val developerListeners: MutableList<(DeveloperMetadataImpl) -> Unit> = LinkedList()

    override fun iterator(): Iterator<DeveloperMetadata> = developers.iterator()

    override fun developer(action: Action<in DeveloperMetadata>) {
        val developer = DeveloperMetadataImpl(objectFactory)
        developers.add(developer)
        action.execute(developer)
        for (developerListener in developerListeners) {
            developerListener.invoke(developer)
        }
    }

    fun withDeveloper(listener: (DeveloperMetadataImpl) -> Unit) {
        developerListeners.add(listener)
        for (developer in developers) {
            listener.invoke(developer)
        }
    }
}