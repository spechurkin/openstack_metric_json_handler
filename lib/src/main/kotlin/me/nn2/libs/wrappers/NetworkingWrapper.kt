package me.nn2.libs.wrappers

import me.nn2.libs.data.networking.*
import me.nn2.libs.services.networking.*
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.IPVersionType
import org.openstack4j.model.network.Ipv6AddressMode
import org.openstack4j.model.network.Ipv6RaMode
import org.openstack4j.model.network.NetworkType

class NetworkingWrapper(client: OSClientV3) : AWrapper(client) {
    fun getNetworks(): List<NetworkData> {
        return NetworkService(client).getNetworks()
    }

    fun createNetwork(
        projectName: String,
        networkName: String,
        isShared: Boolean,
        isExternal: Boolean,
        isAdmin: Boolean,
        networkType: NetworkType,
        physicalNetwork: String?,
        segmentId: String?
    ) {
        NetworkService(client).createNetwork(
            projectName = projectName,
            networkName = networkName,
            isAdmin = isAdmin,
            isShared = isShared,
            isExternal = isExternal,
            networkType = networkType,
            physicalNetwork = physicalNetwork,
            segmentId = segmentId
        )
    }

    fun getSubnets(): List<SubnetData> {
        return SubnetService(client).getSubnets()
    }

    fun addSubnet(
        subnetName: String,
        networkName: String,
        ipVersion: IPVersionType,
        raMode: Ipv6RaMode?,
        addressMode: Ipv6AddressMode,
        cidr: String,
        gateway: String,
        start: String,
        end: String,
        destination: String,
        nextHop: String,
        dnsServer: String
    ) {
        SubnetService(client).addSubnet(
            subnetName = subnetName,
            networkName = networkName,
            ipVersion = ipVersion,
            raMode = raMode,
            addressMode = addressMode,
            cidr = cidr,
            gateway = gateway,
            start = start,
            end = end,
            destination = destination,
            nextHop = nextHop,
            dnsServer = dnsServer
        )
    }

    fun getPorts(): List<PortData> {
        return PortService(client).getPorts()
    }

    fun getRouters(): List<RouterData> {
        return RouterService(client).getRouters()
    }

    fun getSecurityGroups(): List<SecurityGroupData> {
        return SecurityGroupService(client).getSecurityGroups()
    }

    fun getSecurityGroupRules(): List<SecurityRuleData> {
        return SecurityRuleService(client).getSecurityRules()
    }

    fun getFloatingIps(): List<FloatingIpData> {
        return FloatingIpsService(client).getFloatingIps()
    }
}
