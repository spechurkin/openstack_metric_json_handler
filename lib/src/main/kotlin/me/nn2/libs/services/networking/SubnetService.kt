package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.SubnetData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient
import org.openstack4j.model.network.IPVersionType
import org.openstack4j.model.network.Ipv6AddressMode
import org.openstack4j.model.network.Ipv6RaMode

class SubnetService(override val client: OSClient.OSClientV3) : IMetricService {

    private val networkService = NetworkService(client)

    fun getSubnets(): List<SubnetData> {
        return convertToDto()
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
        val subnet = Builders.subnet()
            .name(subnetName)
            .networkId(networkService.getNetworkIdByName(networkName))
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
    }

    private fun convertToDto(): List<SubnetData> {
        return client.networking().subnet().list().map { subnet ->
            SubnetData(
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
}