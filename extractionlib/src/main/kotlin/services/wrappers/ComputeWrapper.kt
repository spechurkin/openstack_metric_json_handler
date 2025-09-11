package services.wrappers

import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.*
import org.openstack4j.model.compute.ext.AvailabilityZone
import org.openstack4j.model.compute.ext.Hypervisor
import org.openstack4j.model.compute.ext.Migration
import org.openstack4j.model.compute.ext.Service
import services.compute.FlavorService
import services.compute.ImageService
import services.compute.ServerService

class ComputeWrapper(val client: OSClientV3) {
    fun getFlavors(): List<FlavorService.FlavorDTO> {
        return FlavorService(client).getFlavors()
    }

    fun getImages(): List<ImageService.ImageDTO> {
        return ImageService(client).getImages()
    }

    fun getServers(): List<ServerService.ServerDTO> {
        return ServerService(client).getServers()
    }

    fun getKeypairs(): List<Keypair?>? {
        return client.compute().keypairs().list()
    }

    fun getServices(): List<Service?>? {
        return client.compute().services().list()
    }

    fun getFloatingIps(): List<FloatingIP?>? {
        return client.compute().floatingIps().list()
    }

    fun getHosts(): List<HostResource?>? {
        return client.compute().host().list()
    }

    fun getZones(): List<AvailabilityZone?>? {
        return client.compute().zones().list()
    }

    fun getMigrations(): List<Migration?>? {
        return client.compute().migrations().list()
    }

    fun getHypervisors(): List<Hypervisor?>? {
        return client.compute().hypervisors().list()
    }

    fun getHostAggregates(): List<HostAggregate?>? {
        return client.compute().hostAggregates().list()
    }

    fun getServerGroups(): List<ServerGroup?>? {
        return client.compute().serverGroups().list()
    }

    fun getSecurityGroups(): List<SecGroupExtension?>? {
        return client.compute().securityGroups().list()
    }

    fun getSecurityRules(): List<List<SecGroupExtension.Rule?>?> {
        return client.compute().securityGroups().list().map { group ->
            group.rules
        }.toList()
    }
}