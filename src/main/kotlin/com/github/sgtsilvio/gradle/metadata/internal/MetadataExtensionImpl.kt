package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.MetadataExtension

/**
 * @author Silvio Giebl
 */
open class MetadataExtensionImpl : MetadataExtension {
    override var moduleName: String? = null
    override var readableName: String? = null
    override var url: String? = null
    override var docUrl: String? = null
    override val organization = OrganizationMetadataImpl()
    override val license = LicenseMetadataImpl()
    override val developers = DevelopersMetadataImpl()
    override val scm = ScmMetadataImpl()
    override val issueManagement = IssueManagementMetadataImpl()
    override val github = GithubMetadataImpl(this)
}