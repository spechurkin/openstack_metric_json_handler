package services.wrappers

import org.openstack4j.api.OSClient.OSClientV3
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
}