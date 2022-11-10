package ua.bitco

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ua.bitco.plugins.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
