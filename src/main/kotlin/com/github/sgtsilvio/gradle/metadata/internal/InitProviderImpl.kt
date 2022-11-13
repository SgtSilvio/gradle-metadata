package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.InitProvider
import org.gradle.api.Action
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory

/**
 * @author Silvio Giebl
 */
class InitProviderImpl<T : Any>(providerFactory: ProviderFactory, valueFactory: () -> T) : InitProvider<T> {

    private var valueFactory: (() -> T)? = valueFactory
    private var value: T? = null
    override val provider: Provider<T> = providerFactory.provider { value }
    private val listeners: MutableList<Action<in T>> = mutableListOf()

    override fun configure(configuration: Action<in T>) {
        var value = value
        if (value == null) {
            value = valueFactory!!.invoke()
            this.value = value
            valueFactory = null
            for (listener in listeners) {
                listener.execute(value)
            }
            listeners.clear()
        }
        configuration.execute(value)
    }

    override fun whenPresent(listener: Action<in T>) {
        val value = value
        if (value == null) {
            listeners.add(listener)
        } else {
            listener.execute(value)
        }
    }
}