package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.KeypairData
import me.nn2.libs.services.AbstractMetricService
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.compute.Keypair

class KeypairService(client: OSClientV3) : AbstractMetricService(client) {
    fun getKeypairs(): List<KeypairData> {
        return convertToDto()
    }

    fun createKeypair(keyName: String, publicKey: String? = null) {
        client.compute().keypairs().create(keyName, publicKey)
    }

    fun deleteKeypair(keyName: String) {
        client.compute().keypairs().delete(keyName)
    }

    fun computeToDto(keypair: Keypair): KeypairData {
        return KeypairData(
            name = keypair.name,
            fingerprint = keypair.fingerprint,
            publicKey = keypair.publicKey
        )
    }

    private fun convertToDto(): List<KeypairData> {
        return client.compute().keypairs().list().map {
            computeToDto(it)
        }
    }
}