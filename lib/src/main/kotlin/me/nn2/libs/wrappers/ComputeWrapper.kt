package me.nn2.libs.wrappers

import me.nn2.libs.data.compute.FlavorData
import me.nn2.libs.data.compute.ImageData
import me.nn2.libs.data.compute.ServerData
import me.nn2.libs.data.compute.ServerGroupData
import me.nn2.libs.services.compute.FlavorService
import me.nn2.libs.services.compute.ImageService
import me.nn2.libs.services.compute.ServerGroupService
import me.nn2.libs.services.compute.ServerService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.HostAggregate
import org.openstack4j.model.compute.Keypair
import org.openstack4j.model.compute.ext.AvailabilityZone
import org.openstack4j.model.compute.ext.Hypervisor
import org.openstack4j.model.compute.ext.Migration
import org.openstack4j.model.compute.ext.Service
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

    fun getKeypairs(): List<Keypair> {
        return client.compute().keypairs().list()
    }

    fun getServices(): List<Service> {
        return client.compute().services().list()
    }

    fun getZones(): List<AvailabilityZone> {
        return client.compute().zones().list()
    }

    fun getMigrations(): List<Migration> {
        return client.compute().migrations().list()
    }

    fun getHypervisors(): List<Hypervisor> {
        return client.compute().hypervisors().list()
    }

    fun getHostAggregates(): List<HostAggregate> {
        return client.compute().hostAggregates().list()
    }

    fun getServerGroups(): List<ServerGroupData> {
        return ServerGroupService(client).getServerGroup()
    }
}
