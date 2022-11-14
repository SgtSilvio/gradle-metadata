package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.*
import com.github.sgtsilvio.gradle.metadata.internal.InitProviderImpl.Companion.initProvider
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
internal abstract class MetadataExtensionImpl @Inject constructor(
    objectFactory: ObjectFactory,
    providerFactory: ProviderFactory,
) : MetadataExtension {

    final override val moduleName = objectFactory.property<String>()
    final override val readableName = objectFactory.property<String>()
    final override val url = objectFactory.property<String>()
    final override val docUrl = objectFactory.property<String>()

    final override val organization = providerFactory.initProvider {
        objectFactory.newInstance(OrganizationMetadata::class)
    }

    final override val license = providerFactory.initProvider {
        objectFactory.newInstance(LicenseMetadata::class)
    }

    final override val developers = objectFactory.domainObjectContainer(DeveloperMetadata::class) { name ->
        objectFactory.newInstance(DeveloperMetadataImpl::class, name)
    }

    final override val scm = providerFactory.initProvider {
        objectFactory.newInstance(ScmMetadata::class)
    }

    final override val issueManagement = providerFactory.initProvider {
        objectFactory.newInstance(IssueManagementMetadata::class)
    }

    final override val github = providerFactory.initProvider<GithubMetadata> {
        objectFactory.newInstance(GithubMetadataImpl::class)
    }

    init {
        url.convention(github.provider.flatMap { it.url })
        docUrl.convention(github.provider.flatMap { it.pagesUrl })
        scm.whenPresent {
            url.convention(github.provider.flatMap { it.vcsUrl })
            connection.convention(github.provider.flatMap { it.vcsUrl.map { url -> "scm:git:$url" } })
            developerConnection.convention(github.provider.flatMap { it.vcsUrl.map { url -> "scm:git:$url" } })
        }
        issueManagement.whenPresent {
            system.convention(github.provider.flatMap { it.issuesUrl.map { "GitHub Issues" } })
            url.convention(github.provider.flatMap { it.issuesUrl })
        }
        github.whenPresent {
            this as GithubMetadataImpl
            scm.initialize()
            issues.whenPresent {
                issueManagement.initialize()
            }
        }
    }

    override fun organization(action: Action<in OrganizationMetadata>) = organization.configure(action)

    override fun license(action: Action<in LicenseMetadata>) = license.configure(action)

    override fun scm(action: Action<in ScmMetadata>) = scm.configure(action)

    override fun issueManagement(action: Action<in IssueManagementMetadata>) = issueManagement.configure(action)

    override fun github(action: Action<in GithubMetadata>) = github.configure(action)
}