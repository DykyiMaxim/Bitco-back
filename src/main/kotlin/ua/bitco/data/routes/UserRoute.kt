package ua.bitco.data.routes

import authentication.JwtService
import ua.bitco.data.authentication.hash
import ua.bitco.domain.model.LoginRequest
import ua.bitco.domain.model.RegisterRequest
import ua.bitco.domain.model.SimpleRequest
import ua.bitco.domain.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import ua.bitco.data.repository.repo
import ua.bitco.domain.model.ChEmailRequest
import ua.bitco.domain.model.ChNameRequest
import ua.bitco.domain.model.ChPassRequest


const val API_VERSION ="/v1"
const val USERS ="$API_VERSION/users"
const val LOGIN_REQUEST ="$USERS/login"
const val REGISTER_REQUEST ="$USERS/register"
const val CHANGE_NAME_REQUEST ="$USERS/chname"
const val CHANGE_EMAIL_REQUEST ="$USERS/chemail"
const val CHANGE_PASSWORD_REQUEST ="$USERS/chpass"

@Location(REGISTER_REQUEST)
class UserRegisterRoute
@Location(LOGIN_REQUEST)
class UserLoginRoute
@Location(CHANGE_NAME_REQUEST)
class UserCHnameRoute
@Location(CHANGE_EMAIL_REQUEST)
class UserChemailRoute
@Location(CHANGE_PASSWORD_REQUEST)
class UserChPassRoute

fun Route.UserRoutes(
    db: repo,
    jwtService: JwtService,
    hashFunction:(String)->String
) {
    post("/v1/users/register") {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Missing some fields"))
            return@post
        }
        try {
            val user = User(
                registerRequest.email,
                hashFunction(registerRequest.password),
                registerRequest.name
            )
            db.addUser(user = user)
            call.respond(HttpStatusCode.OK, SimpleRequest(true, jwtService.GenerateToken(user)))

        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.Conflict,
                SimpleRequest(false, e.message ?: "Some Problems while registration.")
            )
        }
    }
    post<UserLoginRoute> {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Missing some fields"))
            return@post
        }
        try {

            val user = db.findUserByEmail(loginRequest.email)


            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Bad Email ID"))
            } else {

                if (user.hashPassword== hash(loginRequest.password)) {
                        call.respond(HttpStatusCode.OK, SimpleRequest(true, jwtService.GenerateToken(user)))
                } else {
                    call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Password Incorrect!"))
                }
            }

        } catch (e:Exception){
            call.respond(HttpStatusCode.Conflict, SimpleRequest(false,e.message ?: "Some Problem Occurred!"))
        }
    }

    post<UserCHnameRoute>{
        val ChNameRequest = try{
            call.receive<ChNameRequest>()
        }catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Missing some fields"))
            return@post
        }
        try{
            val query = db.changeUserName(ChNameRequest.old_name,ChNameRequest.newName)
        }catch (e:Exception){call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Missing some fields"))}

    }

    post<UserChemailRoute>{
        val ChEmailRequest = try{
            call.receive<ChEmailRequest>()
        }catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Missing some fields"))
            return@post
        }
        try{
            val query = db.changeUserEmail(ChEmailRequest.old_email,ChEmailRequest.newEmail)
        }catch (e:Exception){call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Missing some fields"))}

    }

    post<UserChPassRoute>{
        val ChPassRequest = try{
            call.receive<ChPassRequest>()
        }catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Missing some fields"))
            return@post
        }
        try{
            val query = db.changeUserPassword(ChPassRequest.old_pass,ChPassRequest.newPass)
        }catch (e:Exception){call.respond(HttpStatusCode.BadRequest, SimpleRequest(false, "Missing some fields"))}

    }

}