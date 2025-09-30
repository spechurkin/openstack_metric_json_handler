package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.MigrationData
import me.nn2.libs.services.AbstractMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.ext.Migration

class MigrationService(client: OSClientV3) : AbstractMetricService(client) {
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