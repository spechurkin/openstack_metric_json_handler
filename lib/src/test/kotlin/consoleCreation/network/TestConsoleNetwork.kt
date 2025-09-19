package proj.work.consoleCreation.network

import org.openstack4j.api.Builders
import org.openstack4j.core.transport.Config
import org.openstack4j.model.common.Identifier
import org.openstack4j.openstack.OSFactory
import proj.work.*

fun main(args: Array<String>) {
    val os = OSFactory.builderV3()
        .endpoint(identityUrl)
        .credentials(login, password, Identifier.byName(domain))
        .scopeToProject(
            Identifier.byName(project),
            Identifier.byName(domain),
        )
        .withConfig(Config.newConfig().withSSLVerificationDisabled())
        .authenticate()

    val projectId =
        args.getOrElse(1) { os.identity().projects().list().find { project -> project.name == "admin" }?.id }

    val networkCreate = Builders.network()
        .name(args.getOrElse(0) { "DefaultNetwork" })
        .tenantId(projectId)
        .build()

    os.networking().network().create(networkCreate)
}