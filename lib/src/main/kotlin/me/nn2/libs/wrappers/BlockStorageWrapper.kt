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

    fun updateVolume(volumeName: String, newName: String?, description: String?, newSize: Int?) {
        VolumeService(client).updateVolume(volumeName, newName, description, newSize)
    }

    fun deleteVolume(volumeName: String) {
        VolumeService(client).deleteVolume(volumeName)
    }

    fun getVolumeTypes(): List<VolumeTypeData> {
        return VolumeService(client).getVoluteTypes()
    }

    fun createVolumeType(typeName: String, specs: Map<String, String>?) {
        VolumeService(client).createVolumeType(typeName, specs)
    }

    fun deleteVolumeType(typeName: String) {
        VolumeService(client).deleteVolumeType(typeName)
    }

    fun getBackups(): List<BackupData> {
        return BackupService(client).getBackups()
    }

    fun createBackup(backupName: String, description: String?, volumeName: String) {
        BackupService(client).createBackup(
            backupName = backupName,
            description = description,
            volumeName = volumeName
        )
    }

    fun restoreBackup(backupName: String, newName: String?, volumeName: String?) {
        BackupService(client).restoreBackup(
            backupName = backupName,
            newName = newName,
            volumeName = volumeName
        )
    }

    fun deleteBackup(backupName: String) {
        BackupService(client).deleteBackup(backupName)
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

    fun updateSnapshot(snapshotName: String, newName: String?, description: String?) {
        SnapshotService(client).updateSnapshot(snapshotName, newName, description)
    }

    fun deleteSnapshot(snapshotName: String) {
        SnapshotService(client).deleteSnapshot(snapshotName)
    }

    fun getServices(): List<ServiceData> {
        return ServiceService(client).getServices()
    }
}
