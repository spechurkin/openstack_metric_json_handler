package proj.work.consoleCreation.network

import me.nn2.libs.data.networking.SubnetData
import org.openstack4j.model.network.Subnet
import proj.work.consoleCreation.compute.os

fun main() {
    os.networking().network().list().forEach {
        println(it.name + ":= " + getSubnets(it.name) + "\n")
    }

}

fun getSubnets(networkName: String): List<SubnetData> {
    val networkSubnets = os.networking().network().get(getNetworkIdByName(networkName)).subnets
    val allSubnets = mutableListOf<SubnetData>()

    for (subnet in networkSubnets) {
        allSubnets.add(convertToDto(os.networking().subnet().get(subnet)))
    }

    return allSubnets
}

private fun convertToDto(subnet: Subnet): SubnetData {
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