package proj.work.consoleCreation.compute

import kotlinx.coroutines.delay
import me.nn2.libs.data.compute.ServerData
import me.nn2.libs.services.compute.FlavorService
import me.nn2.libs.services.compute.ImageService
import me.nn2.libs.services.networking.NetworkService
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.core.transport.Config
import org.openstack4j.model.common.Identifier
import org.openstack4j.model.compute.Action
import org.openstack4j.model.compute.RebootType
import org.openstack4j.model.compute.Server
import org.openstack4j.model.compute.ServerUpdateOptions
import org.openstack4j.model.compute.actions.BackupOptions
import org.openstack4j.openstack.OSFactory
import proj.work.*

val os: OSClientV3 = OSFactory.builderV3()
    .endpoint(identityUrl)
    .credentials(login, password, Identifier.byName(domain))
    .scopeToProject(
        Identifier.byName(project),
        Identifier.byName(domain),
    )
    .withConfig(Config.newConfig().withSSLVerificationDisabled())
    .authenticate()

suspend fun main() {
    println(getServers())
//    createServer(
//        "Klyde",
//        "Ubuntu Server 25.04",
//        "m1.big",
//        keyPair = "Kyle",
//        networkNames = listOf("MY_NETWORK2")
//    )
//    createServer()
//    attachVolume(getServerIdByName("DefaultServer"), getVolumeIdByName("DefaultVolume"), "/dev/vda")
//    resizeServer(getServerIdByName("DefaultServer"), getFlavorIdByName("DefaultFlavor"))
//    backupServer(getServerIdByName("DefaultServer"), "DefaultBackup")
//    snapshotServer(getServerIdByName("DefaultServer"), "DefaultSnapshot")
//    updateServer(getServerIdByName("DefaultServer"), "Kyle")
//    rebootServer(getServerIdByName("DefaultServer"), RebootType.HARD)
//    println(getFlavorIdByName("DefaultFlavor"))
}

fun getServers(): List<ServerData> {
    return convertToDto()
}

fun getServer(serverName: String): Server? {
    return os.compute().servers().list().firstOrNull { it.name == serverName }
}

fun createServer(
    serverName: String,
    imageName: String,
    flavorName: String,
    adminPass: String = "admin",
    keyPair: String? = null,
    networkNames: List<String>
) {
    val serverCreate = Builders.server()
        .name(serverName)
        .image(ImageService(os).getImage(imageName)!!.id)
        .flavor(FlavorService(os).getFlavor(flavorName)!!.id)
        .networks(networkNames.map { NetworkService(os).getNetwork(it)!!.id })
        .addAdminPass(adminPass)
        .apply {
            keyPair?.let { keypairName(it) }
        }.build()

    os.compute().servers().boot(serverCreate)
}

private fun convertToDto(): List<ServerData> {
    return os.compute().servers().list().map { server ->
        val flavor = server.flavor
        val flavorId = flavor?.id ?: "Не доступен"
        val flavorName = if (flavorId == "Не доступен") {
            flavorId
        } else flavor.name

        val image = server.image
        val imageId = image?.id ?: "-"
        val imageName = if (imageId == "-") {
            imageId
        } else image.name

        ServerData(
            id = server.id,
            name = server.name,
            status = server.status.toString(),
            flavorName = flavorName,
            imageName = imageName,
            addresses = server.addresses.addresses.map {
                it.key + ": " + it.value.map { address -> address.addr }
            },
            securityGroups = server.securityGroups?.map { it.name } ?: listOf(""),
            keyName = server.keyName,
            created = server.created,
            updated = server.updated
        )
    }
}

fun updateServer(serverId: String?, newName: String, ip4Address: String = "", ip6Address: String = "") {
    val options = ServerUpdateOptions.create()
        .name(newName)
    if (ip4Address != "") {
        options.accessIPv4(ip4Address)
    }
    if (ip6Address != "") {
        options.accessIPv6(ip6Address)
    }
    os.compute().servers().update(serverId, options)
}

fun changePass(serverId: String?, pass: String) {
    os.compute().servers().changeAdminPassword(serverId, pass)
}

fun changeServerStatus(serverId: String?, actionType: String) {
    val status: Action = when (actionType) {
        "Pause" -> Action.PAUSE
        "Unpause" -> Action.UNPAUSE
        "Stop" -> Action.STOP
        "Start" -> Action.START
        "Lock" -> Action.LOCK
        "Unlock" -> Action.UNLOCK
        "Suspend" -> Action.SUSPEND
        "Resume" -> Action.RESUME
        "Rescue" -> Action.RESCUE
        "Unrescue" -> Action.UNRESCUE
        "Shelve" -> Action.SHELVE
        "Shelve Offload" -> Action.SHELVE_OFFLOAD
        "Unshelve" -> Action.UNSHELVE
        "Forcedelete" -> Action.FORCEDELETE
        else -> {
            throw IllegalArgumentException("Unknown action type: $actionType")
        }
    }
    os.compute().servers().action(serverId, status)
}

fun attachVolume(serverId: String?, volumeId: String?, deviceName: String) {
    os.compute().servers().attachVolume(serverId, volumeId, deviceName)
}

fun detachVolume(serverId: String?, volumeId: String?) {
    os.compute().servers().detachVolume(serverId, volumeId)
}

suspend fun resizeServer(serverId: String?, flavorId: String?) {
    os.compute().servers().resize(serverId, flavorId)
    delay(5000)
    os.compute().servers().confirmResize(serverId)
}

fun backupServer(serverId: String?, backupName: String) {
    os.compute().servers().backupServer(serverId, BackupOptions.create(backupName))
}

fun snapshotServer(serverId: String?, snapshotName: String) {
    os.compute().servers().createSnapshot(serverId, snapshotName)
}

fun rebootServer(serverId: String?, rebootType: RebootType) {
    os.compute().servers().reboot(serverId, rebootType)
}

fun deleteServer(serverId: String?) {
    os.compute().servers().delete(serverId)
}

fun getServerIdByName(serverName: String): String? {
    return os.compute().servers().list().find { it.name == serverName }?.id
}