package com.github.sgtsilvio.gradle.metadata

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Property

/**
 * @author Silvio Giebl
 */
interface MetadataExtension {
    val moduleName: Property<String>
    val readableName: Property<String>
    val url: Property<String>
    val docUrl: Property<String>
    val organization: InitProvider<OrganizationMetadata>
    val license: InitProvider<LicenseMetadata>
    val developers: NamedDomainObjectContainer<DeveloperMetadata>
    val scm: InitProvider<ScmMetadata>
    val issueManagement: InitProvider<IssueManagementMetadata>
    val github: InitProvider<GithubMetadata>

    fun organization(action: Action<in OrganizationMetadata>)

    fun license(action: Action<in LicenseMetadata>)

    fun scm(action: Action<in ScmMetadata>)

    fun issueManagement(action: Action<in IssueManagementMetadata>)

    fun github(action: Action<in GithubMetadata>)
}