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

    fun createBackup(name: String, description: String, volumeName: String) {
        if (getBackup(name) == null) {
            client.blockStorage().backups().create(
                Builders.volumeBackupCreate()
                    .name(name)
                    .description(description)
                    .volumeId(volumeService.getVolume(volumeName)!!.id)
                    .build()
            )
        } else {
            createBackup(addSymbolsToCopy(name), description, volumeName)
        }
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