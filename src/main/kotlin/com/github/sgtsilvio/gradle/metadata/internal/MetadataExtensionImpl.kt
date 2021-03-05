package com.github.sgtsilvio.gradle.metadata.internal

import com.github.sgtsilvio.gradle.metadata.*
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.util.*

/**
 * @author Silvio Giebl
 */
open class MetadataExtensionImpl(private val objectFactory: ObjectFactory) : MetadataExtension {

    override val moduleName: Property<String> = objectFactory.property(String::class.java)
    override val readableName: Property<String> = objectFactory.property(String::class.java)
    override val url: Property<String> = objectFactory.property(String::class.java)
    override val docUrl: Property<String> = objectFactory.property(String::class.java)

    override var organization: OrganizationMetadataImpl? = null
    private val organizationListeners: MutableList<(OrganizationMetadataImpl) -> Unit> = LinkedList()

    override var license: LicenseMetadataImpl? = null
    private val licenseListeners: MutableList<(LicenseMetadataImpl) -> Unit> = LinkedList()

    override val developers: DevelopersMetadataImpl = DevelopersMetadataImpl(objectFactory)

    override var scm: ScmMetadataImpl? = null
    private val scmListeners: MutableList<(ScmMetadataImpl) -> Unit> = LinkedList()

    override var issueManagement: IssueManagementMetadataImpl? = null
    private val issueManagementListeners: MutableList<(IssueManagementMetadataImpl) -> Unit> = LinkedList()

    override var github: GithubMetadataImpl? = null
    private val githubListeners: MutableList<(GithubMetadataImpl) -> Unit> = LinkedList()

    init {
        withGithub { github ->
            url.convention(github.url)
            scm(Action { scm ->
                scm.url.convention(github.vcsUrl)
                scm.connection.convention(github.vcsUrl.map { url -> "scm:git:$url" })
                scm.developerConnection.convention(github.vcsUrl.map { url -> "scm:git:$url" })
            })
        }
    }

    override fun organization(action: Action<in OrganizationMetadata>) {
        createIfAbsentAndExecuteAction(organization, organizationListeners, action) {
            OrganizationMetadataImpl(objectFactory).also { organization = it }
        }
    }

    fun withOrganization(listener: (OrganizationMetadataImpl) -> Unit) {
        invokeIfElementIsPresentOrAddToListeners(listener, organization, organizationListeners)
    }

    override fun license(action: Action<in LicenseMetadata>) {
        createIfAbsentAndExecuteAction(license, licenseListeners, action) {
            LicenseMetadataImpl(objectFactory).also { license = it }
        }
    }

    fun withLicense(listener: (LicenseMetadataImpl) -> Unit) {
        invokeIfElementIsPresentOrAddToListeners(listener, license, licenseListeners)
    }

    override fun developers(action: Action<in DevelopersMetadata>) {
        action.execute(developers)
    }

    override fun scm(action: Action<in ScmMetadata>) {
        createIfAbsentAndExecuteAction(scm, scmListeners, action) {
            ScmMetadataImpl(objectFactory).also { scm = it }
        }
    }

    fun withScm(listener: (ScmMetadataImpl) -> Unit) {
        invokeIfElementIsPresentOrAddToListeners(listener, scm, scmListeners)
    }

    override fun issueManagement(action: Action<in IssueManagementMetadata>) {
        createIfAbsentAndExecuteAction(issueManagement, issueManagementListeners, action) {
            IssueManagementMetadataImpl(objectFactory).also { issueManagement = it }
        }
    }

    fun withIssueManagement(listener: (IssueManagementMetadataImpl) -> Unit) {
        invokeIfElementIsPresentOrAddToListeners(listener, issueManagement, issueManagementListeners)
    }

    override fun github(action: Action<in GithubMetadata>) {
        createIfAbsentAndExecuteAction(github, githubListeners, action) {
            GithubMetadataImpl(this, objectFactory).also { github = it }
        }
    }

    fun withGithub(listener: (GithubMetadataImpl) -> Unit) {
        invokeIfElementIsPresentOrAddToListeners(listener, github, githubListeners)
    }

    private inline fun <T> createIfAbsentAndExecuteAction(
        element: T?,
        listeners: MutableList<(T) -> Unit>,
        action: Action<in T>,
        creator: () -> T
    ) {
        if (element == null) {
            val newElement = creator.invoke()
            action.execute(newElement)
            for (listener in listeners) {
                listener.invoke(newElement)
            }
            listeners.clear()
        } else {
            action.execute(element)
        }
    }

    private fun <T> invokeIfElementIsPresentOrAddToListeners(
        listener: (T) -> Unit,
        element: T?,
        listeners: MutableList<(T) -> Unit>
    ) {
        if (element == null) {
            listeners.add(listener)
        } else {
            listener.invoke(element)
        }
    }
}