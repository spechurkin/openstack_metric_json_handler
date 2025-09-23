package me.nn2.libs.wrappers

import me.nn2.libs.data.compute.FlavorData
import me.nn2.libs.data.compute.ImageData
import me.nn2.libs.data.compute.ServerData
import me.nn2.libs.services.compute.FlavorService
import me.nn2.libs.services.compute.ImageService
import me.nn2.libs.services.compute.ServerService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.*
import org.openstack4j.model.compute.ext.AvailabilityZone
import org.openstack4j.model.compute.ext.Hypervisor
import org.openstack4j.model.compute.ext.Migration
import org.openstack4j.model.compute.ext.Service

class ComputeWrapper(client: OSClientV3) : AWrapper(client) {
    fun getFlavors(): List<FlavorData> {
        return FlavorService(client).getFlavors()
    }

    fun getImages(): List<ImageData> {
        return ImageService(client).getImages()
    }

    fun getServers(): List<ServerData> {
        return ServerService(client).getServers()
    }

    fun createServer(
        serverName: String,
        imageName: String,
        flavorName: String,
        adminPass: String? = "admin",
        keyPair: String?,
        networkNames: List<String>?
    ): ServerData {
        return ServerService(client).createServer(
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

    fun getFloatingIps(): List<FloatingIP> {
        return client.compute().floatingIps().list()
    }

    fun getHosts(): List<HostResource> {
        return client.compute().host().list()
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

    fun getServerGroups(): List<ServerGroup> {
        return client.compute().serverGroups().list()
    }

    fun getSecurityGroups(): List<SecGroupExtension> {
        return client.compute().securityGroups().list()
    }

    fun getSecurityRules(): List<List<SecGroupExtension.Rule>> {
        return client.compute().securityGroups().list().map { group ->
            group.rules
        }.toList()
    }
}
