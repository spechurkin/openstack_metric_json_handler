package my.proj.work.services.compute

import my.proj.work.services.IMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Flavor

data class FlavorDTO(
    var id: String?,
    var name: String?,
    var ram: Int?,
    var vcpu: Int?,
    var disk: Int?,
    var ephemeral: Int?,
    var swap: Int?
)

class FlavorService(override val client: OSClientV3) : IMetricService {
    fun toJson(): String? {
        return toJson(convertToDto(), "compute/flavors")
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
}