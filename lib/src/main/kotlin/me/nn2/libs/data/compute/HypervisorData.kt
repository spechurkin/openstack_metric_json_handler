package me.nn2.libs.data.compute

import org.openstack4j.model.compute.ext.Hypervisor

data class HypervisorData(
    val id: String,
    val status: String,
    val state: String,
    val cpuInfo: Hypervisor.CPUInfo,
    val virtualCPU: Int,
    val virtualUsedCPU: Int,
    val localDisk: Int,
    val localDiskUsed: Int,
    val freeDisk: Int,
    val leastDiskAvailable: Int,
    val localMemory: Int,
    val localMemoryUsed: Int,
    val freeRam: Int,
    val hostIP: String,
    val hypervisorHostname: String,
    val currentWorkload: Int,
    val runningVM: Int
)