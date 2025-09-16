package me.nn2.libs.services.storage

import me.nn2.libs.data.storage.VolumeData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient

class VolumeService(override val client: OSClient.OSClientV3) : IMetricService {
    fun getVolume(): List<VolumeData> {
        return convertToDto()
    }

    private fun convertToDto(): List<VolumeData> {
        return client.blockStorage().volumes().list().map { volume ->
            VolumeData(
                id = volume.id,
                name = volume.name,
                description = volume.description,
                size = volume.size,
                status = volume.status,
                metadata = volume.metaData.map { (key, value) ->
                    StringBuilder().append(key).append("=").append(value).toString()
                },
                created = volume.created
            )
        }
    }
}