package ua.bitco.domain.model

data class RegisterRequest(
    val email:String,
    val name:String,
    val password:String
)
