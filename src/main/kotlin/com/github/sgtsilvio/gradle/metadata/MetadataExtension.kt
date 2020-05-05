package com.github.sgtsilvio.gradle.metadata

import org.gradle.api.Action

/**
 * @author Silvio Giebl
 */
interface MetadataExtension {
    var moduleName: String?
    var readableName: String?
    var url: String?
    var docUrl: String?
    val organization: OrganizationMetadata
    val license: LicenseMetadata
    val developers: DevelopersMetadata
    val scm: ScmMetadata
    val issueManagement: IssueManagementMetadata
    val github: GithubMetadata

    fun organization(action: Action<in OrganizationMetadata>) {
        action.execute(organization)
    }

    fun license(action: Action<in LicenseMetadata>) {
        action.execute(license)
    }

    fun developers(action: Action<in DevelopersMetadata>) {
        action.execute(developers)
    }

    fun scm(action: Action<in ScmMetadata>) {
        action.execute(scm)
    }

    fun issueManagement(action: Action<in IssueManagementMetadata>) {
        action.execute(issueManagement)
    }

    fun github(action: Action<in GithubMetadata>) {
        action.execute(github)
        if (url == null) {
            url = github.url
        }
        scm.url = github.vcsUrl
        scm.connection = "scm:git:git://github.com/${github.org}/${github.repo}.git"
        scm.developerConnection = "scm:git:ssh://git@github.com/${github.org}/${github.repo}.git"
    }
}