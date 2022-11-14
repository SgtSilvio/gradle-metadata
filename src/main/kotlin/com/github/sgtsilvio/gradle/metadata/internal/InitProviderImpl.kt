package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.InitProvider
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * @author Silvio Giebl
 */
class InitProviderImpl<T : Any> private constructor(
    objectFactory: ObjectFactory,
    valueType: Class<T>,
    initializer: (Property<T>) -> Unit,
) : InitProvider<T> {

    companion object {
        fun <T : Any> ObjectFactory.initProvider(valueType: Class<T>, initializer: () -> T) =
            InitProviderImpl(this, valueType) { it.set(initializer.invoke()) }

        fun <T : Any> ObjectFactory.initProviderFrom(valueType: Class<T>, initializer: () -> Provider<out T>) =
            InitProviderImpl(this, valueType) { it.set(initializer.invoke()) }

        inline fun <reified T : Any> ObjectFactory.initProvider(noinline initializer: () -> T) =
            initProvider(T::class.java, initializer)

        inline fun <reified T : Any> ObjectFactory.initProviderFrom(noinline initializer: () -> Provider<out T>) =
            initProviderFrom(T::class.java, initializer)
    }

    private val property = objectFactory.property(valueType)
    override val provider: Provider<T> get() = property
    private var initializer: ((Property<T>) -> Unit)? = initializer
    private var listeners: MutableList<() -> Unit>? = null

    fun initialize() {
        val initializer = initializer
        if (initializer != null) {
            initializer.invoke(property)
            this.initializer = null
            val listeners = listeners
            if (listeners != null) {
                for (listener in listeners) {
                    listener.invoke()
                }
                this.listeners = null
            }
        }
    }

    fun whenInitialized(listener: () -> Unit) {
        val initializer = initializer
        if (initializer != null) {
            var listeners = listeners
            if (listeners == null) {
                listeners = mutableListOf()
                this.listeners = listeners
            }
            listeners.add(listener)
        } else {
            listener.invoke()
        }
    }

    override fun configure(configuration: Action<in T>) {
        initialize()
        configuration.execute(property.get())
    }

    override fun whenPresent(listener: Action<in T>) = whenInitialized { listener.execute(property.get()) }
}