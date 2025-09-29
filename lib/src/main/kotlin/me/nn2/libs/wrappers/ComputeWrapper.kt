package me.nn2.libs.wrappers

import me.nn2.libs.data.ServiceData
import me.nn2.libs.data.compute.*
import me.nn2.libs.services.compute.*
import org.openstack4j.api.OSClient.OSClientV3
import java.net.URI

class ComputeWrapper(client: OSClientV3) : AWrapper(client) {
    fun getFlavors(): List<FlavorData> {
        return FlavorService(client).getFlavors()
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
        FlavorService(client).createFlavor(
            flavorName,
            ramMb,
            diskGb,
            ephemeralGb,
            swapMb,
            vcpus,
            rxtxFactor,
            isPublic
        )
    }

    fun getImages(): List<ImageData> {
        return ImageService(client).getImages()
    }

    fun createImage(
        imageName: String,
        minRamGb: Int,
        minDiskGb: Int,
        visibility: String,
        urlToImg: String
    ) {
        ImageService(client).createImage(
            imageName,
            minRamGb.toLong(),
            minDiskGb.toLong(),
            visibility,
            URI(urlToImg).toURL()
        )
    }

    fun getServers(): List<ServerData> {
        return ServerService(client).getServers()
    }

    fun createServer(
        serverName: String,
        imageName: String,
        flavorName: String,
        adminPass: String = "admin",
        keyPair: String?,
        networkNames: List<String>
    ) {
        ServerService(client).createServer(
            serverName = serverName,
            imageName = imageName,
            flavorName = flavorName,
            adminPass = adminPass,
            keyPair = keyPair,
            networkNames = networkNames
        )
    }

    fun getKeypairs(): List<KeypairData> {
        return KeypairService(client).getKeypairs()
    }

    fun getServices(): List<ServiceData> {
        return ServiceService(client).getServices()
    }

    fun getZones(): List<ZoneData> {
        return ZoneService(client).getZones()
    }

    fun getMigrations(): List<MigrationData> {
        return MigrationService(client).getMigrations()
    }

    fun getHypervisors(): List<HypervisorData> {
        return HypervisorService(client).getHypervisors()
    }

    fun getHostAggregates(): List<HostAggregateData> {
        return HostAggregateService(client).getHostAggregate()
    }

    fun getServerGroups(): List<ServerGroupData> {
        return ServerGroupService(client).getServerGroup()
    }
}
