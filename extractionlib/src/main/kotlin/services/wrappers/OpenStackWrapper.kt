package services.wrappers

import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.core.transport.Config
import org.openstack4j.model.common.Identifier
import org.openstack4j.openstack.OSFactory

class OpenStackWrapper(
    private val authUrl: String,
    private val username: String,
    private val password: String,
    private val domain: String,
    private val project: String
) {
    private fun setClient(): OSClientV3 {
        val os: OSClientV3 = OSFactory.builderV3()
            .endpoint(authUrl)
            .credentials(username, password, Identifier.byName(domain))
            .scopeToProject(
                Identifier.byName(project),
                Identifier.byName(domain)
            )
            .withConfig(Config.newConfig().withSSLVerificationDisabled())
            .authenticate()

        return os
    }

    fun client(): OSClientV3 {
        return setClient()
    }

    fun compute() = ComputeWrapper(client())
    fun identity() = IdentityWrapper(authUrl, username, password)
    fun blockStorage() = BlockStorageWrapper(client())
    fun networking() = NetworkingWrapper(client())
}