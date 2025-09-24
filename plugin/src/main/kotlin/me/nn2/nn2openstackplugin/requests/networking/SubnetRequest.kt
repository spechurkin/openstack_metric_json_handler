package me.nn2.nn2openstackplugin.requests.networking

import org.openstack4j.model.network.IPVersionType
import org.openstack4j.model.network.Ipv6AddressMode
import org.openstack4j.model.network.Ipv6RaMode

class SubnetRequest(
    val subnetName: String,
    val networkName: String,
    val ipVersion: IPVersionType,
    val raMode: Ipv6RaMode?,
    val addressMode: Ipv6AddressMode,
    val cidr: String,
    val gateway: String,
    val start: String,
    val end: String,
    val destination: String,
    val nextHop: String,
    val dnsServer: String
) {
    class Builder {
        private var subnetName: String = ""
        private var networkName: String = ""
        private var ipVersion: IPVersionType = IPVersionType.V4
        private var raMode: Ipv6RaMode? = null
        private var addressMode: Ipv6AddressMode = Ipv6AddressMode.NULL
        private var cidr: String = ""
        private var gateway: String = ""
        private var start: String = ""
        private var end: String = ""
        private var destination: String = ""
        private var nextHop: String = ""
        private var dnsServer: String = ""

        fun subnetName(subnetName: String) = apply { this.subnetName = subnetName }
        fun networkName(networkName: String) = apply { this.networkName = networkName }
        fun ipVersion(ipVersion: String) = apply {
            this.ipVersion = when (ipVersion) {
                "IPv4" -> IPVersionType.V4
                "IPv6" -> IPVersionType.V6
                else -> IPVersionType.V4
            }
        }

        fun raMode(raMode: String) = apply {
            this.raMode = when (raMode) {
                "DHCPv6 SLAAC" -> Ipv6RaMode.SLAAC
                "DHCPv6 stateful" -> Ipv6RaMode.DHCPV6_STATEFUL
                "DHCPv6 stateless" -> Ipv6RaMode.DHCPV6_STATELESS
                else -> null
            }
        }

        fun addressMode(addressMode: String) = apply {
            this.addressMode = when (addressMode) {
                "DHCPv6 SLAAC" -> Ipv6AddressMode.SLAAC
                "DHCPv6 stateful" -> Ipv6AddressMode.DHCPV6_STATEFUL
                "DHCPv6 stateless" -> Ipv6AddressMode.DHCPV6_STATELESS
                else -> Ipv6AddressMode.NULL
            }
        }

        fun cidr(cidr: String) = apply { this.cidr = cidr }
        fun gateway(gateway: String) = apply { this.gateway = gateway }
        fun start(start: String) = apply { this.start = start }
        fun end(end: String) = apply { this.end = end }
        fun destination(destination: String) = apply { this.destination = destination }
        fun nextHop(nextHop: String) = apply { this.nextHop = nextHop }
        fun dnsServer(dnsServer: String) = apply { this.dnsServer = dnsServer }

        fun build(): SubnetRequest {
            require(subnetName.isNotBlank()) { "subnetName is required" }
            require(networkName.isNotBlank()) { "networkName is required" }
            require(cidr.isNotBlank()) { "cidr is required" }
            return SubnetRequest(
                subnetName,
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
}
