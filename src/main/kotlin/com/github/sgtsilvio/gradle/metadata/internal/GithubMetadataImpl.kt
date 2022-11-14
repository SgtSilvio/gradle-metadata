package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.GithubMetadata
import com.github.sgtsilvio.gradle.metadata.internal.InitProviderImpl.Companion.initProviderFrom
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

/**
 * @author Silvio Giebl
 */
internal abstract class GithubMetadataImpl @Inject constructor(objectFactory: ObjectFactory) : GithubMetadata {

    final override val org = objectFactory.property<String>()
    final override val repo = objectFactory.property<String>()
    final override val url = org.merge(repo) { org, repo -> "https://github.com/$org/$repo" }
    final override val vcsUrl = url.map { url -> "$url.git" }
    val issues = objectFactory.initProviderFrom { url.map { url -> "$url/issues" } }
    private val pages =
        objectFactory.initProviderFrom { org.merge(repo) { org, repo -> "https://$org.github.io/$repo/" } }
    final override val issuesUrl get() = issues.provider
    final override val pagesUrl get() = pages.provider

    override fun issues() = issues.initialize()

    override fun pages() = pages.initialize()
}