package services

import org.openstack4j.api.OSClient.OSClientV3

interface IMetricService {
    val client: OSClientV3
}
