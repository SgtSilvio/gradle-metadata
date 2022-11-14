package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.InitProvider
import org.gradle.api.Action
import org.gradle.api.provider.ProviderFactory

/**
 * @author Silvio Giebl
 */
internal class InitProviderImpl<T : Any> private constructor(
    providerFactory: ProviderFactory,
    initializer: () -> T,
) : InitProvider<T> {

    companion object {
        fun <T : Any> ProviderFactory.initProvider(initializer: () -> T) = InitProviderImpl(this, initializer)
    }

    private var initializer: (() -> T)? = initializer
    private var value: T? = null
    override val provider = providerFactory.provider<T> { value }
    private var listeners: MutableList<Action<in T>>? = null

    fun initialize(): T {
        var value = value
        if (value == null) {
            value = initializer!!.invoke()
            initializer = null
            this.value = value
            val listeners = listeners
            if (listeners != null) {
                for (listener in listeners) {
                    listener.execute(value)
                }
                this.listeners = null
            }
        }
        return value
    }

    override fun configure(configuration: Action<in T>) = configuration.execute(initialize())

    override fun whenPresent(listener: Action<in T>) {
        val value = value
        if (value == null) {
            var listeners = listeners
            if (listeners == null) {
                listeners = mutableListOf()
                this.listeners = listeners
            }
            listeners.add(listener)
        } else {
            listener.execute(value)
        }
    }
}