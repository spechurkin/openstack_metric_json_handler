package services.compute

import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Flavor
import services.IMetricService

class FlavorService(override val client: OSClientV3) : IMetricService {
    fun getFlavors(): List<FlavorDTO> {
        return convertToDto()
    }

    private fun convertToDto(): List<FlavorDTO> {
        val flavorDto = client.compute().flavors().list().map { flavor ->
            convertToDto(flavor)
        }
        return flavorDto
    }

    fun convertToDto(flavor: Flavor?): FlavorDTO {
        return FlavorDTO(
            id = flavor?.id,
            name = flavor?.name,
            ram = flavor?.ram,
            vcpu = flavor?.vcpus,
            disk = flavor?.disk,
            ephemeral = flavor?.ephemeral,
            swap = flavor?.swap
        )
    }

    data class FlavorDTO(
        var id: String?,
        var name: String?,
        var ram: Int?,
        var vcpu: Int?,
        var disk: Int?,
        var ephemeral: Int?,
        var swap: Int?
    )
}