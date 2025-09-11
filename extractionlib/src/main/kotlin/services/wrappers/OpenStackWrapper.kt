package services.wrappers

import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.common.Identifier
import org.openstack4j.openstack.OSFactory

class OpenStackWrapper(
    private val authUrl: String,
    private val username: String,
    private val password: String,
    domain: String,
    project: String
) {
    private val os: OSClientV3 = OSFactory.builderV3()
        .endpoint(authUrl)
        .credentials(username, password, Identifier.byName(domain))
        .scopeToProject(
            Identifier.byName(project),
            Identifier.byName(domain)
        )
        .authenticate()

    fun compute() = ComputeWrapper(os)
    fun identity() = IdentityWrapper(authUrl, username, password)
    fun blockStorage() = BlockStorageWrapper(os)
    fun networking() = NetworkingWrapper(os)
}