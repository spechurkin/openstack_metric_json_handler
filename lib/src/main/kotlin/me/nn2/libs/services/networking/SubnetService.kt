package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.SubnetData
import me.nn2.libs.services.AbstractMetricService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.IPVersionType
import org.openstack4j.model.network.Ipv6AddressMode
import org.openstack4j.model.network.Ipv6RaMode
import org.openstack4j.model.network.Subnet

class SubnetService(client: OSClientV3) : AbstractMetricService(client) {
    private val networkService = NetworkService(client)

    fun getNetworkSubnets(networkName: String): List<SubnetData> {
        val networkSubnets = client.networking().network().get(networkService.getNetwork(networkName)!!.id).subnets
        val allSubnets = mutableListOf<SubnetData>()

        for (subnet in networkSubnets) {
            allSubnets.add(convertToDto(client.networking().subnet().get(subnet)))
        }

        return allSubnets
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
        if (!getNetworkSubnets(networkName).map { it.name }.contains(networkName)) {
            val subnet = Builders.subnet()
                .name(subnetName)
                .networkId(networkService.getNetwork(networkName)!!.id)
                .ipVersion(ipVersion)
                .ipv6RaMode(raMode)
                .ipv6AddressMode(addressMode)
                .cidr(cidr)
                .gateway(gateway)
                .addPool(start, end)
                .addHostRoute(destination, nextHop)
                .addDNSNameServer(dnsServer)
                .build()

            client.networking().subnet().create(subnet)
        } else {
            addSubnet(
                addSymbolsToCopy(subnetName),
                networkName,
                ipVersion,
                raMode,
                addressMode,
                cidr,
                gateway,
                start,
                end,
                destination,
                nextHop,
                dnsServer
            )
        }
    }

    fun removeSubnet(networkName: String, subnetName: String) {
        val subnets = getNetworkSubnets(networkName)
        val concreteSubnet = subnets.find { it.name == subnetName }!!.id

        client.networking().subnet().delete(concreteSubnet)
    }

    fun convertToDto(subnet: Subnet): SubnetData {
        return SubnetData(
            id = subnet.id,
            name = subnet.name,
            ipVersion = subnet.ipVersion.name,
            dns = subnet.dnsNames,
            pools = subnet.allocationPools.map { mapEntry ->
                StringBuilder().append("start: ").append(mapEntry.start).append(", end: ").append(mapEntry.end)
                    .toString()
            },
            routes = subnet.hostRoutes.map { hostRoute ->
                hostRoute.destination
            },
            gateway = subnet.gateway,
            _cidr = subnet.cidr,
            ipV6AddressMode = subnet.ipv6AddressMode?.ipv6AddressMode,
            ipV6RaMode = subnet.ipv6RaMode?.ipv6RaMode
        )
    }
}