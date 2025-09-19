package proj.work.consoleCreation.compute

import org.openstack4j.api.Builders.imageV2
import org.openstack4j.core.transport.Config
import org.openstack4j.model.common.Identifier
import org.openstack4j.model.common.Payloads
import org.openstack4j.model.image.v2.ContainerFormat
import org.openstack4j.model.image.v2.DiskFormat
import org.openstack4j.model.image.v2.Image
import org.openstack4j.openstack.OSFactory
import proj.work.*
import java.net.URI

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

    val imageCreate = imageV2()
        .name(args.getOrElse(0) { "Ubuntu Server" }.replace("_", " "))
        .containerFormat(ContainerFormat.BARE)
        .diskFormat(DiskFormat.QCOW2)
        .minRam(args.getOrElse(1) { "512" }.toLong())
        .minDisk(args.getOrElse(2) { "5" }.toLong())
        .visibility(Image.ImageVisibility.PUBLIC)
        .build()
    val payload = Payloads.create(
        URI("https://cloud-images.ubuntu.com/releases/oracular/release/ubuntu-24.10-server-cloudimg-amd64.img")
            .toURL()
    )

    os.imagesV2().create(imageCreate)
    val image: Image? = os.imagesV2().list().find { image -> image.name == imageCreate.name }

    run {
        os.imagesV2().upload(image?.id, payload, image)
    }
}