package me.nn2.nn2openstackplugin.processors

import me.nn2.libs.OpenStackWrapper
import org.opensearch.rest.RestChannel

interface IProcessor {
    fun process(metric: String, wrapper: OpenStackWrapper, channel: RestChannel)
}