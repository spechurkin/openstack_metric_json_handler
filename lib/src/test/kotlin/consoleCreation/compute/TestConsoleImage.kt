package proj.work.consoleCreation.compute

import org.openstack4j.api.Builders.imageV2
import org.openstack4j.model.common.Payloads
import org.openstack4j.model.image.v2.ContainerFormat
import org.openstack4j.model.image.v2.DiskFormat
import org.openstack4j.model.image.v2.Image
import java.io.File
import java.net.URI

fun main(args: Array<String>) {
    createImage(args)
}

fun createImage(
    args: Array<String>,
    urlToImg: String = "https://cloud-images.ubuntu.com/releases/oracular/release/ubuntu-24.10-server-cloudimg-amd64.img"
) {
    val imageCreate = imageV2()
        .name(args.getOrElse(0) { "Ubuntu Server" }.replace("_", " "))
        .containerFormat(ContainerFormat.BARE)
        .diskFormat(DiskFormat.QCOW2)
        .minRam(args.getOrElse(1) { "1" }.toLong())
        .minDisk(args.getOrElse(2) { "5" }.toLong())
        .visibility(Image.ImageVisibility.PUBLIC)
        .build()
    val payload = Payloads.create(
        URI(urlToImg).toURL()
    )

    val image = os.imagesV2().create(imageCreate)

    run {
        os.imagesV2().upload(image?.id, payload, image)
    }
}

fun activate(imageId: String) {
    os.imagesV2().reactivate(imageId)
}

fun deactivate(imageId: String) {
    os.imagesV2().deactivate(imageId)
}

fun download(imageId: String, fileName: String) {
    os.imagesV2().download(imageId, File(fileName))
}

fun removeImage(imageId: String) {
    os.imagesV2().delete(imageId)
}

fun getImageIdByName(imageName: String): String? {
    return os.imagesV2().list(mapOf("name" to imageName)).firstOrNull()?.id
}