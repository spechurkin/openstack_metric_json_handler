package services.wrappers

import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.*

class NetworkingWrapper(val client: OSClientV3) {
    fun getNetworks(): List<Network?>? {
        return client.networking().network().list()
    }

    fun getSubnets(): List<Subnet?>? {
        return client.networking().subnet().list()
    }

    fun getPorts(): List<Port?>? {
        return client.networking().port().list()
    }

    fun getRouters(): List<Router?>? {
        return client.networking().router().list()
    }

    fun getSecurityGroups(): List<SecurityGroup?>? {
        return client.networking().securitygroup().list()
    }

    fun getQuotas(): List<NetQuota?>? {
        return client.networking().quotas().get()
    }

    fun getFloatingIps(): List<NetFloatingIP?>? {
        return client.networking().floatingip().list()
    }
}
