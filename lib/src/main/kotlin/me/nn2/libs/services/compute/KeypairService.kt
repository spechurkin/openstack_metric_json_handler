package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.KeypairData
import me.nn2.libs.services.IMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Keypair

class KeypairService(override val client: OSClientV3) : IMetricService {

    fun getKeypairs(): List<KeypairData> {
        return convertToDto()
    }

    fun computeToDto(keypair: Keypair): KeypairData {
        return KeypairData(
            id = keypair.id,
            name = keypair.name,
            fingerprint = keypair.fingerprint,
            user = try {
                client.identity().users().get(keypair.userId).name
            } catch (_: Exception) {
                ""
            },
            publicKey = keypair.publicKey,
            privateKey = keypair.privateKey,
            createdAt = keypair.createdAt,
            updatedAt = keypair.updatedAt,
            isDeleted = keypair.deleted,
            deletedAt = keypair.deletedAt
        )
    }

    private fun convertToDto(): List<KeypairData> {
        return client.compute().keypairs().list().map {
            computeToDto(it)
        }
    }
}