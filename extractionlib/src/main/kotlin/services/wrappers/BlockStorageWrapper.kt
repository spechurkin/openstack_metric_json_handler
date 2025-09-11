package services.wrappers

import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.storage.block.Volume
import org.openstack4j.model.storage.block.VolumeBackup
import org.openstack4j.model.storage.block.VolumeSnapshot
import org.openstack4j.model.storage.block.ext.Service

class BlockStorageWrapper(val client: OSClientV3) {
    fun getVolumes(): List<Volume?>? {
        return client.blockStorage().volumes().list()
    }

    fun getBackups(): List<VolumeBackup?>? {
        return client.blockStorage().backups().list()
    }

    fun getSnapshots(): List<VolumeSnapshot?>? {
        return client.blockStorage().snapshots().list()
    }

    fun getServices(): List<Service?>? {
        return client.blockStorage().services().list()
    }
}