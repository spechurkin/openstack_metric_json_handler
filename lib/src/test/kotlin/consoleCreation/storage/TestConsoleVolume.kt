package proj.work.consoleCreation.storage

import org.openstack4j.api.Builders
import proj.work.consoleCreation.compute.getImageIdByName
import proj.work.consoleCreation.compute.os

fun main(args: Array<String>) {
    createVolume(args)
    createBootVolume(args, getImageIdByName("Ubuntu Server"))
    extendVolume(args.getOrElse(0) { "DefaultVolume" }, 10)
    updateVolume("DefaultVolume", "NewVolume", "My New Volume")
}

fun createVolume(args: Array<String>) {
    val volumeCreate = Builders.volume()
        .name(args.getOrElse(0) { "DefaultVolume" })
        .description("${args.getOrElse(1) { "5" }.toInt()} gb volume")
        .size(args.getOrElse(1) { "5" }.toInt())
        .build()

    os.blockStorage().volumes().create(volumeCreate)
}

fun createBootVolume(args: Array<String>, imageId: String?) {
    val volumeCreate = Builders.volume()
        .name(args.getOrElse(0) { "DefaultVolume" })
        .description("${args.getOrElse(1) { "5" }.toInt()} gb volume")
        .size(args.getOrElse(1) { "5" }.toInt())
        .bootable(true)
        .imageRef(imageId)
        .build()

    os.blockStorage().volumes().create(volumeCreate)
}

fun extendVolume(volumeName: String, newSize: Int) {
    os.blockStorage().volumes().extend(getVolumeIdByName(volumeName), newSize)
}

fun updateVolume(volumeName: String, newName: String, newDescription: String) {
    os.blockStorage().volumes().update(getVolumeIdByName(volumeName), newName, newDescription)
}

fun createSnapshot(volumeName: String, description: String = "snapshot of $volumeName volume") {
    os.blockStorage().snapshots().create(
        Builders.volumeSnapshot()
            .name("$volumeName Snapshot")
            .description(description)
            .volume(getVolumeIdByName(volumeName))
            .build()
    )
}

fun removeVolume(volumeName: String) {
    os.blockStorage().volumes().delete(getVolumeIdByName(volumeName))
}

fun getVolumeIdByName(volumeName: String): String? {
    return os.blockStorage().volumes().list().find { it.name == volumeName }?.id
}