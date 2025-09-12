package me.nn2.libs

import me.nn2.libs.wrappers.BlockStorageWrapper
import me.nn2.libs.wrappers.ComputeWrapper
import me.nn2.libs.wrappers.IdentityWrapper
import me.nn2.libs.wrappers.NetworkingWrapper
import org.openstack4j.api.OSClient
import org.openstack4j.core.transport.Config
import org.openstack4j.model.common.Identifier
import org.openstack4j.openstack.OSFactory

class OpenStackWrapper(
    private val authUrl: String,
    private val username: String,
    private val password: String,
    private val domain: String,
    private val project: String,
    private val allowInsecure: Boolean // TODO: Найти выход с прямым отключением сертификации
) {
    private fun client(): OSClient.OSClientV3 {
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