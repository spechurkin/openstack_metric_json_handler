package me.nn2.libs.wrappers

import me.nn2.libs.data.ServiceData
import me.nn2.libs.data.storage.BackupData
import me.nn2.libs.data.storage.SnapshotData
import me.nn2.libs.data.storage.VolumeData
import me.nn2.libs.data.storage.VolumeTypeData
import me.nn2.libs.services.storage.BackupService
import me.nn2.libs.services.storage.ServiceService
import me.nn2.libs.services.storage.SnapshotService
import me.nn2.libs.services.storage.VolumeService
import org.openstack4j.api.OSClient.OSClientV3

class BlockStorageWrapper(client: OSClientV3) : AbstractWrapper(client) {
    fun getVolumes(): List<VolumeData> {
        return VolumeService(client).getVolumes()
    }

    fun createVolume(volumeName: String?, description: String?, size: Int, volumeType: String?, imageName: String?) {
        VolumeService(client).createVolume(
            volumeName = volumeName,
            description = description,
            size = size,
            volumeType = volumeType,
            imageName = imageName
        )
    }

    fun updateVolume(volumeName: String, newName: String, description: String, newSize: Int) {
        VolumeService(client).updateVolume(volumeName, newName, description, newSize)
    }

    fun deleteVolume(volumeName: String) {
        VolumeService(client).deleteVolume(volumeName)
    }

    fun getVolumeTypes(): List<VolumeTypeData> {
        return VolumeService(client).getVoluteTypes()
    }

    fun createVolumeType(typeName: String, specs: Map<String, String>) {
        VolumeService(client).createVolumeType(typeName, specs)
    }

    fun getBackups(): List<BackupData> {
        return BackupService(client).getBackups()
    }

    fun createBackup(name: String, description: String, volumeName: String) {
        BackupService(client).createBackup(
            name = name,
            description = description,
            volumeName = volumeName
        )
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

    fun deleteSnapshot(snapshotName: String) {
        SnapshotService(client).deleteSnapshot(snapshotName)
    }

    fun getServices(): List<ServiceData> {
        return ServiceService(client).getServices()
    }
}
