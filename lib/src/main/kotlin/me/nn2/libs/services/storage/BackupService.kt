package me.nn2.libs.services.storage

import me.nn2.libs.data.storage.BackupData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.storage.block.VolumeBackup

class BackupService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getBackups(): List<BackupData> {
        return convertBackupToDto()
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