package me.nn2.libs.wrappers

import me.nn2.libs.data.storage.SnapshotData
import me.nn2.libs.data.storage.VolumeData
import me.nn2.libs.data.storage.VolumeTypeData
import me.nn2.libs.services.storage.SnapshotService
import me.nn2.libs.services.storage.VolumeService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.storage.block.VolumeBackup
import org.openstack4j.model.storage.block.ext.Service

class BlockStorageWrapper(client: OSClientV3) : AWrapper(client) {
    fun getVolumes(): List<VolumeData> {
        return VolumeService(client).getVolume()
    }

    fun createVolume(volumeName: String, description: String?, size: Int, volumeType: String, imageName: String?) {
        VolumeService(client).createVolume(
            volumeName = volumeName,
            description = description,
            size = size,
            volumeType = volumeType,
            imageName = imageName
        )
    }

    fun getVolumeTypes(): List<VolumeTypeData> {
        return VolumeService(client).getVoluteTypes()
    }

    fun getBackups(): List<VolumeBackup> {
        return client.blockStorage().backups().list()
    }

    fun getSnapshots(): List<SnapshotData> {
        return SnapshotService(client).getSnapshots()
    }

    fun createSnapshot(snapshotName: String?, description: String?, volumeName: String) {
        SnapshotService(client).createSnapshot(
            snapshotName = snapshotName,
            description = description,
            volumeName = volumeName
        )
    }

    fun getServices(): List<Service> {
        return client.blockStorage().services().list()
    }
}
