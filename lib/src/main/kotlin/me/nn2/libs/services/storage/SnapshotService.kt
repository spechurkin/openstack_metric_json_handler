package me.nn2.libs.services.storage

import me.nn2.libs.data.storage.SnapshotData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient
import org.openstack4j.model.storage.block.VolumeSnapshot

class SnapshotService(override val client: OSClient.OSClientV3) : IMetricService {
    val volumeService = VolumeService(client)

    fun getSnapshots(): List<SnapshotData> {
        return client.blockStorage().snapshots().list().map {
            convertSnapshotToDto(it)
        }
    }

    fun createSnapshot(snapshotName: String?, description: String?, volumeName: String) {
        val volumeSnapshot = Builders.volumeSnapshot()
            .name(snapshotName)
            .description(description.orEmpty())
            .volume(volumeService.getVolumeIdByName(volumeName))
            .force(true)
            .build()

        client.blockStorage().snapshots().create(volumeSnapshot)
    }

    private fun convertSnapshotToDto(snapshot: VolumeSnapshot): SnapshotData {
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
}