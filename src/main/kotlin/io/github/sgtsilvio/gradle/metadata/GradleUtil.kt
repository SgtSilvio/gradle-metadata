package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.provider.Provider

internal fun <A : Any, B : Any, C : Any, R : Any> Provider<A>.zip(
    bProvider: Provider<B>,
    cProvider: Provider<C>,
    combiner: (A, B, C) -> R,
): Provider<R> = zip(bProvider) { a, b -> Pair(a, b) }.zip(cProvider) { ab, c -> combiner(ab.first, ab.second, c) }
