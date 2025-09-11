package localtest

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import me.nn2.nn2openstackplugin.config.OpenStackConfig
import me.nn2.nn2openstackplugin.config.OpenStackManager

data class OpenStackSession(val config: OpenStackConfig)

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { gson() }
        install(Sessions) {
            cookie<OpenStackSession>("openstack_session")
        }
        // Для хранения состояния в приложении (альтернатива глобальной переменной)
        val manager = OpenStackManager()

        routing {
            // POST конфигурации: сохраняем в manager и создаём сессию
            post("/_openstack/config") {
                val cfg = call.receive<OpenStackConfig>()
                manager.setConfig(cfg)
                call.sessions.set(OpenStackSession(cfg))
                call.respond(mapOf("result" to "ok"))
            }

            // GET конфигурации из сессии или менеджера
            get("/_openstack/config") {
                val session = call.sessions.get<OpenStackSession>()
                val cfg = session?.config ?: manager.getConfig()
                if (cfg == null) call.respond(HttpStatusCode.NotFound, mapOf("error" to "Config not set"))
                else call.respond(cfg.copy(password = "***"))
            }
            // identity users
            get("/_openstack/identity/users") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val json = manager.wrapper().identity().getUsers()
                call.respondText(json, ContentType.Application.Json)
            }

            get("/_openstack/identity/projects") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val json = manager.wrapper().identity().getProjects()
                call.respondText(json, ContentType.Application.Json)
            }

            // compute servers
            get("/_openstack/compute/servers") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val servers = manager.wrapper().compute().getServers()
                call.respond(servers)
            }

            get("/_openstack/compute/images") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val servers = manager.wrapper().compute().getImages()
                call.respond(servers)
            }

            // compute flavors
            get("/_openstack/compute/flavors") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val flavors = manager.wrapper().compute().getFlavors()
                call.respond(flavors)
            }

            // networking networks
            get("/_openstack/networking/networks") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val nets = manager.wrapper().networking().getNetworks() ?: emptyList<Any>()
                call.respond(nets)
            }

            // networking ports
            get("/_openstack/networking/ports") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val ports = manager.wrapper().networking().getPorts() ?: emptyList<Any>()
                call.respond(ports)
            }

            // networking subnets
            get("/_openstack/networking/subnets") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val subs = manager.wrapper().networking().getSubnets() ?: emptyList<Any>()
                call.respond(subs)
            }

            // block storage volumes
            get("/_openstack/block/volumes") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val vols = manager.wrapper().blockStorage().getVolumes() ?: emptyList<Any>()
                call.respond(vols)
            }

            // block storage snapshots
            get("/_openstack/block/snapshots") {
                val session = call.sessions.get<OpenStackSession>()
                if (session != null) {
                    manager.setConfig(session.config)
                }
                val snaps = manager.wrapper().blockStorage().getSnapshots() ?: emptyList<Any>()
                call.respond(snaps)
            }
        }
    }.start(wait = true)
}