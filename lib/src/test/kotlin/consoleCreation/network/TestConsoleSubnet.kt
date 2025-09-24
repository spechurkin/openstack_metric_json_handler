package proj.work.consoleCreation.network

import org.openstack4j.api.Builders
import org.openstack4j.model.network.IPVersionType
import proj.work.consoleCreation.compute.os

fun main(args: Array<String>) {
    createSubnet(args)
}

private fun createSubnet(args: Array<String>) {
    val subnet = Builders.subnet()
        .name(args.getOrElse(0) { "DefaultSubnet" })
        .networkId("09a39f90-a319-4df5-89be-2fc752dd3f77")
        .ipVersion(IPVersionType.V4)
        .cidr("172.29.248.0/22")
        .build()

    os.networking().subnet().create(subnet)
}

fun removeSubnet(subnetName: String) {
    os.networking().subnet().delete(getSubnetIdByName(subnetName))
}

fun getSubnetIdByName(subnetName: String): String {
    return os.networking().subnet().list().find { it.name == subnetName }!!.id
}