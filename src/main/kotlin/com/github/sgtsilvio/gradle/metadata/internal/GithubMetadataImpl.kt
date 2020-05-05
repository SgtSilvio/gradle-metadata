package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.GithubMetadata

/**
 * @author Silvio Giebl
 */
class GithubMetadataImpl(private val metadataExtension: MetadataExtensionImpl) :
    GithubMetadata {
    override var org: String? = null
    override var repo: String? = null

    override val url: String
        get() = "https://github.com/$org/$repo"

    override val vcsUrl: String?
        get() = "$url.git"

    override val pagesUrl: String
        get() = "https://$org.github.io/$repo/"

    override val issuesUrl: String
        get() = "$url/issues"

    override fun pages() {
        metadataExtension.docUrl = pagesUrl
    }

    override fun issues() {
        metadataExtension.issueManagement.system = "GitHub Issues"
        metadataExtension.issueManagement.url = issuesUrl
    }
}