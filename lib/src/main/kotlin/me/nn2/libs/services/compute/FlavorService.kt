package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.FlavorData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Flavor

class FlavorService(override val client: OSClientV3) : IMetricService {
    fun getFlavors(): List<FlavorData> {
        return convertToDto()
    }

    fun getFlavorIdByName(flavorName: String): String? {
        return client.compute().flavors().list().find { it.name == flavorName }?.id
    }

    fun createFlavor(
        flavorName: String,
        ramMb: Int,
        diskGb: Int,
        ephemeralGb: Int,
        swapMb: Int,
        vcpus: Int,
        rxtxFactor: Float,
        isPublic: Boolean,
    ) {
        val flavorCreate = Builders.flavor()
            .name(flavorName)
            .ram(ramMb)
            .disk(diskGb)
            .ephemeral(ephemeralGb)
            .swap(swapMb)
            .vcpus(vcpus)
            .rxtxFactor(rxtxFactor)
            .isPublic(isPublic)
            .build()

        client.compute().flavors().create(flavorCreate)
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
            rxtx = flavor.rxtxFactor
        )
    }

    private fun convertToDto(): List<FlavorData> {
        return client.compute().flavors().list().map { convertToDto(it) }
    }
}
