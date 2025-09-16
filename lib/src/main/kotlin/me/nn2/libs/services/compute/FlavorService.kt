package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.FlavorData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Flavor

class FlavorService(override val client: OSClientV3) : IMetricService {
    fun getFlavors(): List<FlavorData> {
        return convertToDto()
    }

    private fun convertToDto(): List<FlavorData> {
        return client.compute().flavors().list().map { flavor ->
            convertToDto(flavor)
        }
    }

    fun convertToDto(flavor: Flavor): FlavorData {
        return FlavorData(
            id = flavor.id,
            name = flavor.name,
            ram = flavor.ram,
            vcpu = flavor.vcpus,
            disk = flavor.disk,
            ephemeral = flavor.ephemeral,
            swap = flavor.swap,
        )
    }
}
