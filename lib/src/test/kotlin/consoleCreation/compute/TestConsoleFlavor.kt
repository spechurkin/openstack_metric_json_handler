package proj.work.consoleCreation.compute

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

    val flavorCreate = Builders.flavor()
        .name(args.getOrElse(0) { "DefaultFlavor" })
        .ram(args.getOrElse(1) { "1024" }.toInt())
        .disk(args.getOrElse(2) { "10" }.toInt())
        .ephemeral(args.getOrElse(3) { "20" }.toInt())
        .vcpus(args.getOrElse(4) { "4" }.toInt())
        .build()

    os.compute().flavors().create(flavorCreate)
}