package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.GithubMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

/**
 * @author Silvio Giebl
 */
abstract class GithubMetadataImpl @Inject constructor(
    private val metadata: MetadataExtensionImpl,
    objectFactory: ObjectFactory
) : GithubMetadata {

    final override val org = objectFactory.property<String>()
    final override val repo = objectFactory.property<String>()
    final override val url = org.merge(repo) { org, repo -> "https://github.com/$org/$repo" }
    final override val vcsUrl = url.map { url -> "$url.git" }
    final override val issuesUrl = url.map { url -> "$url/issues" }
    final override val pagesUrl = org.merge(repo) { org, repo -> "https://$org.github.io/$repo/" }

    override fun issues() {
        metadata.issueManagement {
            system.set("GitHub Issues")
            url.set(issuesUrl)
        }
    }

    override fun pages() {
        metadata.docUrl.set(pagesUrl)
    }
}