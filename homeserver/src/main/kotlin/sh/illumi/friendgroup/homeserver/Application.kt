package sh.illumi.friendgroup.homeserver

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import sh.illumi.friendgroup.homeserver.plugins.*
import java.io.File

suspend fun main(args: Array<String>) {
    HomeserverConfig.load(File(args[0]))
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureSockets()
    configureDatabases()
    configureRouting()
}
