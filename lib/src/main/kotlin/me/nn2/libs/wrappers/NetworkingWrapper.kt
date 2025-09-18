package me.nn2.libs.wrappers

import me.nn2.libs.data.networking.SecurityGroupData
import me.nn2.libs.data.networking.SubnetData
import me.nn2.libs.services.networking.SecurityGroupService
import me.nn2.libs.services.networking.SubnetService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.*

class NetworkingWrapper(client: OSClientV3) : AWrapper(client) {
    fun getNetworks(): List<Network> {
        return client.networking().network().list()
    }

    fun getSubnets(): List<SubnetData> {
        return SubnetService(client).getSubnets()
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
