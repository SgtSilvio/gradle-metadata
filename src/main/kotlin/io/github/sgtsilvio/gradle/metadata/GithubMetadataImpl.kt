package io.github.sgtsilvio.gradle.metadata

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

/**
 * @author Silvio Giebl
 */
internal abstract class GithubMetadataImpl @Inject constructor(
    objectFactory: ObjectFactory,
    providerFactory: ProviderFactory,
) : GithubMetadata {

    final override val org = objectFactory.property<String>()
    final override val repo = objectFactory.property<String>()
    final override val url: Provider<String> = org.zip(repo) { org, repo -> "https://github.com/$org/$repo" }
    final override val vcsUrl: Provider<String> = url.map { url -> "$url.git" }
    val issues = providerFactory.initProvider { Issues(this) }
    private val pages = providerFactory.initProvider { Pages(this) }
    final override val issuesUrl: Provider<String> get() = issues.provider.flatMap { it.url }
    final override val pagesUrl: Provider<String> get() = pages.provider.flatMap { it.url }

    override fun issues() {
        issues.initialize()
    }

    override fun pages() {
        pages.initialize()
    }

    class Issues(github: GithubMetadataImpl) {
        val url: Provider<String> = github.url.map { url -> "$url/issues" }
    }

    class Pages(github: GithubMetadataImpl) {
        val url: Provider<String> = github.org.zip(github.repo) { org, repo -> "https://$org.github.io/$repo/" }
    }
}