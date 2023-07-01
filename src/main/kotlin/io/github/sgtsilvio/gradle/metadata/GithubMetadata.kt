package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * @author Silvio Giebl
 */
interface GithubMetadata {
    val org: Property<String>
    val repo: Property<String>
    val url: Provider<String>
    val vcsUrl: Provider<String>
    val issuesUrl: Provider<String>
    val pagesUrl: Provider<String>

    fun issues()

    fun pages()
}