package me.nn2.libs.wrappers

import me.nn2.libs.data.networking.NetworkData
import me.nn2.libs.data.networking.SecurityGroupData
import me.nn2.libs.data.networking.SubnetData
import me.nn2.libs.services.networking.NetworkService
import me.nn2.libs.services.networking.SecurityGroupService
import me.nn2.libs.services.networking.SubnetService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.*

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

    fun getPorts(): List<Port> {
        return client.networking().port().list()
    }

    fun getRouters(): List<Router> {
        return client.networking().router().list()
    }

    fun getSecurityGroups(): List<SecurityGroupData> {
        return SecurityGroupService(client).getSecurityGroups()
    }

    fun getSecurityGroupRules(): List<SecurityGroupRule> {
        return client.networking().securityrule().list()
    }

    fun getQuotas(): List<NetQuota> {
        return client.networking().quotas().get()
    }

    fun getFloatingIps(): List<NetFloatingIP> {
        return client.networking().floatingip().list()
    }
}
