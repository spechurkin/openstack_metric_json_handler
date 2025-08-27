package services.compute

import org.openstack4j.api.OSClient.OSClientV3

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