package me.nn2.libs.services.storage

import me.nn2.libs.data.storage.BackupData
import me.nn2.libs.services.AbstractMetricService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.storage.block.VolumeBackup

class BackupService(client: OSClientV3) : AbstractMetricService(client) {
    private val volumeService = VolumeService(client)

    fun getBackups(): List<BackupData> {
        return convertBackupToDto()
    }

    fun getBackup(backupName: String): VolumeBackup? {
        return client.blockStorage().backups().list().firstOrNull { it.name == backupName }
    }

    fun createBackup(backupName: String, description: String?, volumeName: String) {
        val volumeId = volumeService.getVolume(volumeName)!!.id ?: volumeName

        if (getBackup(backupName) == null) {
            client.blockStorage().backups().create(
                Builders.volumeBackupCreate()
                    .name(backupName)
                    .description(description)
                    .volumeId(volumeId)
                    .build()
            )
        } else {
            createBackup(addSymbolsToCopy(backupName), description, volumeName)
        }
    }

    fun restoreBackup(backupName: String, newName: String?, volumeName: String?) {
        if (newName != null) {
            client.blockStorage().backups().restore(getBackup(backupName)!!.id, newName, null)
        } else {
            client.blockStorage().backups()
                .restore(getBackup(backupName)!!.id, null, VolumeService(client).getVolume(volumeName)!!.id)
        }
    }

    fun deleteBackup(backupName: String) {
        client.blockStorage().backups().delete(getBackup(backupName)!!.id)
    }

    fun convertBackupToDto(backup: VolumeBackup): BackupData {
        return BackupData(
            id = backup.id,
            name = backup.name,
            description = backup.description,
            volumeId = backup.volumeId,
            size = backup.size,
            createdAt = backup.created,
            status = backup.status.toString(),
            failReason = backup.failReason,
        )
    }

    private fun convertBackupToDto(): List<BackupData> {
        return client.blockStorage().backups().list().map {
            convertBackupToDto(it)
        }
    }
}