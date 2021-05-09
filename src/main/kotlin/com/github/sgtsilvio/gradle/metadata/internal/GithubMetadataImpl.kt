package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.GithubMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property

/**
 * @author Silvio Giebl
 */
class GithubMetadataImpl(private val metadataExtension: MetadataExtensionImpl, objectFactory: ObjectFactory) :
    GithubMetadata {

    override val org = objectFactory.property<String>()
    override val repo = objectFactory.property<String>()

    override val url = mergeProviders(org, repo) { org, repo -> "https://github.com/$org/$repo" }

    override val vcsUrl = url.map { url -> "$url.git" }

    override val issuesUrl = url.map { url -> "$url/issues" }

    override val pagesUrl = mergeProviders(org, repo) { org, repo -> "https://$org.github.io/$repo/" }

    override fun issues() {
        metadataExtension.issueManagement {
            system.set("GitHub Issues")
            url.set(issuesUrl)
        }
    }

    override fun pages() {
        metadataExtension.docUrl.set(pagesUrl)
    }
}