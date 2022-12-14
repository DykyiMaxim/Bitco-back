package ua.bitco

import authentication.JwtService
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.locations.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.bitco.data.authentication.hash
import ua.bitco.data.repository.DataBaseFactory
import ua.bitco.data.repository.repo
import ua.bitco.plugins.*
import ua.bitco.data.routes.*
import ua.bitco.domain.model.RegisterRequest
import ua.bitco.domain.model.SimpleRequest

fun main(args:Array<String>):Unit = io.ktor.server.netty.EngineMain.main(args)

@OptIn(KtorExperimentalLocationsAPI::class)
@Suppress("unused")
fun Application.module() {
    configureSerialization()
    configureRouting()

    DataBaseFactory.init()
    val db = repo()
    val jwtService = JwtService()
    val hashFunction = {s:String -> hash(s)}






    routing {

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        UserRoutes(db, jwtService, hashFunction)

}
}
