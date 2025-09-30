package me.nn2.libs.services.storage

import me.nn2.libs.data.storage.SnapshotData
import me.nn2.libs.services.AbstractMetricService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.storage.block.VolumeSnapshot
import java.text.SimpleDateFormat
import java.util.*

class SnapshotService(client: OSClientV3) : AbstractMetricService(client) {
    private val volumeService = VolumeService(client)

    fun getSnapshots(): List<SnapshotData> {
        return convertSnapshotToDto()
    }

    fun getSnapshot(snapshotName: String): VolumeSnapshot? {
        return client.blockStorage().snapshots().list().firstOrNull { it.name == snapshotName }
    }

    fun createSnapshot(snapshotName: String?, description: String?, volumeName: String) {
        val snapshot =
            snapshotName ?: addSymbolsToCopy("snapshot+${SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())}")
        val volumeSnapshot = Builders.volumeSnapshot()
            .name(snapshot)
            .description(description.orEmpty())
            .volume(volumeService.getVolume(volumeName)!!.id ?: client.blockStorage().snapshots().get(volumeName).id)
            .force(true)
            .build()

        client.blockStorage().snapshots().create(volumeSnapshot)
    }

    fun deleteSnapshot(snapshotName: String) {
        client.blockStorage().snapshots().delete(getSnapshot(snapshotName)!!.id)
    }

    fun convertSnapshotToDto(snapshot: VolumeSnapshot): SnapshotData {
        return SnapshotData(
            id = snapshot.id,
            name = snapshot.name,
            description = snapshot.description,
            volumeId = snapshot.volumeId,
            status = snapshot.status.toString(),
            size = snapshot.size,
            createdAt = snapshot.created,
        )
    }

    private fun convertSnapshotToDto(): List<SnapshotData> {
        return client.blockStorage().snapshots().list().map {
            convertSnapshotToDto(it)
        }
    }
}