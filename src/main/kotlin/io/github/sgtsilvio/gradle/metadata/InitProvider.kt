package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.Action
import org.gradle.api.provider.Provider

/**
 * Provider for an object that is lazily initialized when [configure] is called for the first time.
 * The object is only initialized once and never becomes uninitialized afterwards.
 *
 * @param T type of the provided object.
 * @author Silvio Giebl
 */
interface InitProvider<T : Any> {
    val provider: Provider<T>
    val isPresent: Boolean
    fun get(): T
    fun getOrNull(): T?

    /**
     * Configure the object, initializing it when not yet initialized.
     *
     * @param configuration the configuration action which receives the initialized object.
     */
    fun configure(configuration: Action<in T>)

    /**
     * Execute a listener when the object is initialized -
     * immediately when already initialized, otherwise later once initialized.
     * Multiple listeners are called in insertion order.
     *
     * @param listener the listener action which receives the initialized object.
     */
    fun whenPresent(listener: Action<in T>)
}