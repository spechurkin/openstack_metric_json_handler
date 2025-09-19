package proj.work.consoleCreation.storage

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

    val volumeCreate = Builders.volume()
        .name(args.getOrElse(0) { "DefaultVolume" })
        .description("${args.getOrElse(1) { "20" }.toInt()} gb volume")
        .size(args.getOrElse(1) { "20" }.toInt())
        .build()

    os.blockStorage().volumes().create(volumeCreate)
}