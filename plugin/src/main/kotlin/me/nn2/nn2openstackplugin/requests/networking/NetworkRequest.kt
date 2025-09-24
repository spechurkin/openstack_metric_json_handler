package me.nn2.nn2openstackplugin.requests.networking

import org.openstack4j.model.network.NetworkType

class NetworkRequest(
    val projectName: String,
    val networkName: String,
    val isShared: Boolean,
    val isExternal: Boolean,
    val isAdmin: Boolean,
    val networkType: NetworkType,
    val physicalNetwork: String?,
    val segmentId: String?
) {
    class Builder {
        private var projectName: String = ""
        private var networkName: String = ""
        private var isShared: Boolean = true
        private var isExternal: Boolean = false
        private var isAdmin: Boolean = true
        private var networkType: NetworkType = NetworkType.GRE
        private var physicalNetwork: String? = null
        private var segmentId: String? = null

        fun projectName(projectName: String) = apply { this.projectName = projectName }
        fun networkName(networkName: String) = apply { this.networkName = networkName }
        fun isShared(isShared: Boolean) = apply { this.isShared = isShared }
        fun isExternal(isExternal: Boolean) = apply { this.isExternal = isExternal }
        fun isAdmin(isAdmin: Boolean) = apply { this.isAdmin = isAdmin }
        fun networkType(networkType: String) = apply {
            this.networkType = when (networkType) {
                "Geneve" -> NetworkType.GRE
                "VLAN" -> NetworkType.VLAN
                "Flat" -> NetworkType.FLAT
                else -> NetworkType.GRE
            }
        }

        fun physicalNetwork(physicalNetwork: String?) = apply { this.physicalNetwork = physicalNetwork }
        fun segmentId(segmentId: String?) = apply { this.segmentId = segmentId }

        fun build(): NetworkRequest {
            require(projectName.isNotBlank()) { "projectName is required" }
            require(networkName.isNotBlank()) { "networkName is required" }
            when (networkType) {
                NetworkType.GRE -> {
                    require(segmentId!!.isNotBlank()) { "segmentId is required" }
                }

                NetworkType.VLAN -> {
                    require(segmentId!!.isNotBlank()) { "segmentId is required" }
                    require(physicalNetwork!!.isNotEmpty()) { "physicalNetwork is required" }
                }

                NetworkType.FLAT -> {
                    require(physicalNetwork!!.isNotEmpty()) { "physicalNetwork is required" }
                }

                else -> {
                    require(segmentId!!.isNotBlank()) { "segmentId is required" }
                    require(physicalNetwork!!.isNotEmpty()) { "physicalNetwork is required" }
                }
            }

            return NetworkRequest(
                projectName,
                networkName,
                isShared,
                isExternal,
                isAdmin,
                networkType,
                physicalNetwork,
                segmentId
            )
        }
    }
}