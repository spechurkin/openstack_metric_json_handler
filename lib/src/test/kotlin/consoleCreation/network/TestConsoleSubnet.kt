package proj.work.consoleCreation.network

import org.openstack4j.api.Builders
import proj.work.consoleCreation.compute.os

fun main(args: Array<String>) {
    createSubnet(args)
}

private fun createSubnet(args: Array<String>) {
    val subnet = Builders.subnet()
        .name(args.getOrElse(0) { "DefaultSubnet" })
        .network(os.networking().network().get(args.getOrNull(1)))
        .build()

    os.networking().subnet().create(subnet)
}

fun removeSubnet(subnetName: String) {
    os.networking().subnet().delete(getSubnetIdByName(subnetName))
}

fun getSubnetIdByName(subnetName: String): String {
    return os.networking().subnet().list().find { it.name == subnetName }!!.id
}