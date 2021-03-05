package com.github.sgtsilvio.gradle.metadata

import org.gradle.api.Action
import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
interface MetadataExtension {
    val moduleName: Property<String>
    val readableName: Property<String>
    val url: Property<String>
    val docUrl: Property<String>
    val organization: OrganizationMetadata?
    val license: LicenseMetadata?
    val developers: DevelopersMetadata
    val scm: ScmMetadata?
    val issueManagement: IssueManagementMetadata?
    val github: GithubMetadata?

    fun organization(action: Action<in OrganizationMetadata>)

    fun license(action: Action<in LicenseMetadata>)

    fun developers(action: Action<in DevelopersMetadata>)

    fun scm(action: Action<in ScmMetadata>)

    fun issueManagement(action: Action<in IssueManagementMetadata>)

    fun github(action: Action<in GithubMetadata>)
}