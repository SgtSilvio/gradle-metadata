package io.github.sgtsilvio.gradle.metadata.internal

import org.gradle.api.provider.Provider
import org.gradle.util.GradleVersion

internal fun <A : Any, B : Any, R : Any> Provider<A>.merge(bProvider: Provider<B>, merger: (A, B) -> R): Provider<R> {
    return if (supportsZip()) {
        zip(bProvider, merger)
    } else {
        map { a -> merger(a, bProvider.get()) }
    }
}

internal fun <A : Any, B : Any, C : Any, R : Any> Provider<A>.merge(
    bProvider: Provider<B>,
    cProvider: Provider<C>,
    merger: (A, B, C) -> R
): Provider<R> {
    return if (supportsZip()) {
        zip(bProvider) { a, b -> Pair(a, b) }.zip(cProvider) { ab, c -> merger(ab.first, ab.second, c) }
    } else {
        map { a -> merger(a, bProvider.get(), cProvider.get()) }
    }
}

private fun supportsZip() = GradleVersion.current() >= GradleVersion.version("6.6")