package com.github.sgtsilvio.gradle.metadata

/**
 * @author Silvio Giebl
 */
interface GithubMetadata {
    var org: String?
    var repo: String?
    val url: String?
    val vcsUrl: String?
    val pagesUrl: String?
    val issuesUrl: String?

    fun pages()

    fun issues()
}