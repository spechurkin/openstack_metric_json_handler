package me.nn2.libs.services.networking

import me.nn2.libs.data.networking.SubnetData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient

class SubnetService(override val client: OSClient.OSClientV3) : IMetricService {
    fun getSubnets(): List<SubnetData> {
        return convertToDto()
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