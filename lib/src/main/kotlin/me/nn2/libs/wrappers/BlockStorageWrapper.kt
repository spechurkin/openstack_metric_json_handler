package me.nn2.libs.wrappers

import me.nn2.libs.data.storage.VolumeData
import me.nn2.libs.services.storage.VolumeService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.storage.block.VolumeBackup
import org.openstack4j.model.storage.block.VolumeSnapshot
import org.openstack4j.model.storage.block.ext.Service

class BlockStorageWrapper(val client: OSClientV3) {
    fun getVolumes(): List<VolumeData> {
        return VolumeService(client).getVolume()
    }

    fun getBackups(): List<VolumeBackup> {
        return client.blockStorage().backups().list()
    }

    fun getSnapshots(): List<VolumeSnapshot> {
        return client.blockStorage().snapshots().list()
    }

    fun getServices(): List<Service> {
        return client.blockStorage().services().list()
    }
}
