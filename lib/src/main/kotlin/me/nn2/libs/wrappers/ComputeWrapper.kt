package me.nn2.libs.wrappers

import me.nn2.libs.data.ServiceData
import me.nn2.libs.data.compute.*
import me.nn2.libs.services.compute.*
import org.openstack4j.api.OSClient.OSClientV3
import java.net.URI

class ComputeWrapper(client: OSClientV3) : AbstractWrapper(client) {
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

    fun deleteFlavor(flavorName: String) {
        FlavorService(client).deleteFlavor(flavorName)
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

    fun updateImage(
        imageName: String,
        minRamGb: Long,
        minDiskGb: Long,
        visibility: String
    ): ImageData {
        return ImageService(client).updateImage(imageName, minRamGb, minDiskGb, visibility)
    }

    fun deleteImage(imageName: String) {
        ImageService(client).deleteImage(imageName)
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

    fun updateServer(
        serverName: String,
        ipv4Address: String?,
        ipv6Address: String?,
    ): ServerData {
        return ServerService(client).updateServer(serverName, ipv4Address, ipv6Address)
    }

    fun deleteServer(serverName: String) {
        ServerService(client).deleteServer(serverName)
    }

    fun getKeypairs(): List<KeypairData> {
        return KeypairService(client).getKeypairs()
    }

    fun createKeypair(keyName: String, publicKey: String?) {
        KeypairService(client).createKeypair(keyName, publicKey)
    }

    fun deleteKeypair(keyName: String) {
        KeypairService(client).deleteKeypair(keyName)
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
        return ServerGroupService(client).getServerGroups()
    }

    fun createServerGroup(groupName: String, policy: String) {
        return ServerGroupService(client).createServerGroup(groupName, policy)
    }

    fun deleteServerGroup(groupName: String) {
        return ServerGroupService(client).deleteServerGroup(groupName)
    }
}
