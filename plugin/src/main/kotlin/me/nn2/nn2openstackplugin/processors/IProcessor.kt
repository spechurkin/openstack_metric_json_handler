package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import org.opensearch.rest.RestChannel
import org.opensearch.rest.RestRequest

interface IProcessor {
    fun process(wrapper: OpenStackWrapper, channel: RestChannel, request: RestRequest?)
}