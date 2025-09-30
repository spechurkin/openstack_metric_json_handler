package me.nn2.libs.wrappers

import me.nn2.libs.data.networking.*
import me.nn2.libs.services.networking.*
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.IPVersionType
import org.openstack4j.model.network.Ipv6AddressMode
import org.openstack4j.model.network.Ipv6RaMode
import org.openstack4j.model.network.NetworkType

class NetworkingWrapper(client: OSClientV3) : AbstractWrapper(client) {
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

    fun updateNetwork(
        networkName: String,
        newName: String,
        isAdmin: Boolean,
        isShared: Boolean,
        isDefault: Boolean
    ) {
        NetworkService(client).updateNetwork(networkName, newName, isAdmin, isShared, isDefault)
    }

    fun deleteNetwork(networkName: String) {
        NetworkService(client).deleteNetwork(networkName)
    }

    fun getSubnets(networkName: String): List<SubnetData> {
        return SubnetService(client).getNetworkSubnets(networkName)
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

    fun removeSubnet(networkName: String, subnetName: String) {
        SubnetService(client).removeSubnet(networkName, subnetName)
    }

    fun getPorts(): List<PortData> {
        return PortService(client).getPorts()
    }

    fun getRouters(): List<RouterData> {
        return RouterService(client).getRouters()
    }

    fun createRouter(
        routerName: String,
        projectName: String,
        isDistributed: Boolean,
        idAdmin: Boolean,
        destination: String?,
        nextHop: String?,
        networkName: String,
        enableSnat: Boolean
    ) {
        RouterService(client).createRouter(
            routerName = routerName,
            projectName = projectName,
            isDistributed = isDistributed,
            idAdmin = idAdmin,
            destination = destination,
            nextHop = nextHop,
            networkName = networkName,
            enableSnat = enableSnat
        )
    }

    fun updateRouter(
        routerName: String,
        newName: String,
        idAdmin: Boolean,
        networkName: String?
    ) {
        RouterService(client).updateRouter(
            routerName,
            newName,
            idAdmin,
            networkName
        )
    }

    fun deleteRouter(routerName: String) {
        RouterService(client).deleteRouter(routerName)
    }

    fun getSecurityGroups(): List<SecurityGroupData> {
        return SecurityGroupService(client).getSecurityGroups()
    }

    fun createSecurityGroup(groupName: String, description: String, projectName: String) {
        SecurityGroupService(client).createSecurityGroup(groupName, description, projectName)
    }

    fun updateSecurityGroup(groupName: String, description: String) {
        SecurityGroupService(client).updateSecurityGroup(groupName, description)
    }

    fun deleteSecurityGroup(groupName: String) {
        SecurityGroupService(client).deleteSecurityGroup(groupName)
    }

    fun getSecurityGroupRules(): List<SecurityRuleData> {
        return SecurityRuleService(client).getSecurityRules()
    }

    fun createSecurityRule(
        projectName: String,
        groupName: String,
        description: String,
        protocol: String,
        remoteIpPrefix: String,
        direction: String,
        etherType: String,
        portRangeMin: Int,
        portRangeMax: Int
    ) {
        SecurityRuleService(client).createSecurityGroupRule(
            projectName,
            groupName,
            description,
            protocol,
            remoteIpPrefix,
            direction,
            etherType,
            portRangeMin,
            portRangeMax
        )
    }

    fun deleteSecurityGroupRule(ruleId: String) {
        SecurityRuleService(client).deleteSecurityGroupRule(ruleId)
    }

    fun getFloatingIps(): List<FloatingIpData> {
        return FloatingIpsService(client).getFloatingIps()
    }
}
