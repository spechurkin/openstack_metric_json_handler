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
    private val project: String,
    private val allowInsecure: Boolean = true, // TODO: Найти выход с прямым отключением сертификации
) {
    fun client(): OSClientV3 {
        val config =
            if (allowInsecure) {
                Config.newConfig().withSSLVerificationDisabled()
            } else {
                Config.newConfig()
            }
        return OSFactory.builderV3()
            .endpoint(authUrl)
            .credentials(username, password, Identifier.byName(domain))
            .scopeToProject(
                Identifier.byName(project),
                Identifier.byName(domain),
            )
            .withConfig(config)
            .authenticate()
    }

    fun compute() = ComputeWrapper(client())

    fun identity() = IdentityWrapper(authUrl, username, password, project, domain)

    fun blockStorage() = BlockStorageWrapper(client())

    fun networking() = NetworkingWrapper(client())
}
