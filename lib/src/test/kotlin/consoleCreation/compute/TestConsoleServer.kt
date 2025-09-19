package proj.work.consoleCreation.compute

import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.core.transport.Config
import org.openstack4j.model.common.Identifier
import org.openstack4j.openstack.OSFactory
import proj.work.*

val os: OSClientV3 = OSFactory.builderV3()
    .endpoint(identityUrl)
    .credentials(login, password, Identifier.byName(domain))
    .scopeToProject(
        Identifier.byName(project),
        Identifier.byName(domain),
    )
    .withConfig(Config.newConfig().withSSLVerificationDisabled())
    .authenticate()

fun main(args: Array<String>) {
//    createServer(args)
    attachVolume("9da9805d-e0c7-447b-9ced-855ab179621a", "a783cc40-041f-4e08-874c-98d41d3af477")
}

fun createServer(args: Array<String>) {
    val serverCreate = Builders.server()
        .name(args.getOrElse(0) { "DefaultServer" })
        .image(args.getOrElse(1) { "047d1de3-1c1f-45d4-a182-ad4c982daf0b" })
        .flavor(args.getOrElse(2) { "201" })
        .build()

    os.compute().servers().boot(serverCreate)
}

fun attachVolume(serverId: String, volumeId: String) {
    os.compute().servers().attachVolume(serverId, volumeId, "/dev/vda")
}