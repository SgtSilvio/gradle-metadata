package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.GithubMetadata
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * @author Silvio Giebl
 */
class GithubMetadataImpl(private val metadataExtension: MetadataExtensionImpl, objectFactory: ObjectFactory) :
    GithubMetadata {

    override val org: Property<String> = objectFactory.property(String::class.java)
    override val repo: Property<String> = objectFactory.property(String::class.java)

    override val url: Provider<String> = mergeProviders(org, repo) { org, repo -> "https://github.com/$org/$repo" }

    override val vcsUrl: Provider<String> = url.map { url -> "$url.git" }

    override val issuesUrl: Provider<String> = url.map { url -> "$url/issues" }

    override val pagesUrl: Provider<String> = mergeProviders(org, repo) { org, repo -> "https://$org.github.io/$repo/" }

    override fun issues() {
        metadataExtension.issueManagement { issueManagement ->
            issueManagement.system.set("GitHub Issues")
            issueManagement.url.set(issuesUrl)
        }
    }

    override fun pages() {
        metadataExtension.docUrl.set(pagesUrl)
    }
}