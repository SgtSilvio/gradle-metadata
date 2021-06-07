package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.*
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.domainObjectContainer
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

/**
 * @author Silvio Giebl
 */
abstract class MetadataExtensionImpl @Inject constructor(
    objectFactory: ObjectFactory,
    providerFactory: ProviderFactory
) : MetadataExtension {

    final override val moduleName = objectFactory.property<String>()
    final override val readableName = objectFactory.property<String>()
    final override val url = objectFactory.property<String>()
    final override val docUrl = objectFactory.property<String>()

    final override val organization = InitProviderImpl(providerFactory) {
        objectFactory.newInstance(OrganizationMetadata::class)
    }

    final override val license: InitProviderImpl<LicenseMetadata> = InitProviderImpl(providerFactory) {
        objectFactory.newInstance(LicenseMetadataImpl::class)
    }

    final override val developers = objectFactory.domainObjectContainer(DeveloperMetadata::class) { name ->
        objectFactory.newInstance(DeveloperMetadataImpl::class, name)
    }

    final override val scm = InitProviderImpl(providerFactory) {
        objectFactory.newInstance(ScmMetadata::class).apply {
            url.convention(github.provider.flatMap { it.vcsUrl })
            connection.convention(github.provider.flatMap { it.vcsUrl.map { url -> "scm:git:$url" } })
            developerConnection.convention(github.provider.flatMap { it.vcsUrl.map { url -> "scm:git:$url" } })
        }
    }

    final override val issueManagement = InitProviderImpl(providerFactory) {
        objectFactory.newInstance(IssueManagementMetadata::class)
    }

    final override val github: InitProviderImpl<GithubMetadata> = InitProviderImpl(providerFactory) {
        objectFactory.newInstance(GithubMetadataImpl::class, this)
    }

    init {
        url.convention(github.provider.flatMap { it.url })
        github.whenPresent { scm.configure {} }
    }

    override fun organization(action: Action<in OrganizationMetadata>) = organization.configure(action)

    override fun license(action: Action<in LicenseMetadata>) = license.configure(action)

    override fun scm(action: Action<in ScmMetadata>) = scm.configure(action)

    override fun issueManagement(action: Action<in IssueManagementMetadata>) = issueManagement.configure(action)

    override fun github(action: Action<in GithubMetadata>) = github.configure(action)
}