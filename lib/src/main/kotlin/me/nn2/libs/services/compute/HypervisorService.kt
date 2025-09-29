package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.HypervisorData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.compute.ext.Hypervisor

class HypervisorService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getHypervisors(): List<HypervisorData> {
        return convertToDto()
    }

    fun convertToDto(hypervisor: Hypervisor): HypervisorData {
        return HypervisorData(
            hypervisor.id,
            hypervisor.status,
            hypervisor.state,
            hypervisor.cpuInfo,
            hypervisor.virtualCPU,
            hypervisor.virtualUsedCPU,
            hypervisor.localDisk,
            hypervisor.localDiskUsed,
            hypervisor.freeDisk,
            hypervisor.leastDiskAvailable,
            hypervisor.localMemory,
            hypervisor.localMemoryUsed,
            hypervisor.freeRam,
            hypervisor.hostIP,
            hypervisor.hypervisorHostname,
            hypervisor.currentWorkload,
            hypervisor.runningVM
        )
    }

    private fun convertToDto(): List<HypervisorData> {
        return client.compute().hypervisors().list().map {
            convertToDto(it)
        }
    }
}