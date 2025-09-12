package me.nn2.libs.services

import org.openstack4j.api.OSClient

interface IMetricService {
    val client: OSClient.OSClientV3
}