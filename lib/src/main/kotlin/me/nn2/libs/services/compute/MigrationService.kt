package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.MigrationData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient
import org.openstack4j.model.compute.ext.Migration

class MigrationService(override val client: OSClient.OSClientV3) : IMetricService {

    fun getMigrations(): List<MigrationData> {
        return convertToDto()
    }

    fun convertToDto(migration: Migration): MigrationData {
        return MigrationData(
            id = migration.id,
            status = migration.status.name,
            sourceNode = migration.sourceNode,
            destNode = migration.destNode,
            sourceCompute = migration.sourceCompute,
            destCompute = migration.destCompute,
            destHost = migration.destHost,
            createdAt = migration.createdAt,
            updatedAt = migration.updatedAt
        )
    }

    private fun convertToDto(): List<MigrationData> {
        return client.compute().migrations().list().map {
            convertToDto(it)
        }
    }
}