package com.github.sgtsilvio.gradle.metadata.internal

import org.gradle.api.provider.Provider

fun <T1, T2, R> mergeProviders(p1: Provider<T1>, p2: Provider<T2>, merger: (T1, T2) -> R): Provider<R> =
    p1.flatMap { t1 -> p2.map { t2 -> merger.invoke(t1, t2) } }

fun <T1, T2, T3, R> mergeProviders(p1: Provider<T1>, p2: Provider<T2>, p3: Provider<T3>, merger: (T1, T2, T3) -> R):
        Provider<R> = p1.flatMap { t1 -> mergeProviders(p2, p3) { t2, t3 -> merger.invoke(t1, t2, t3) } }