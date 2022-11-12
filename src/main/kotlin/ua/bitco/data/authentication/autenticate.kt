package ua.bitco.data.authentication

import io.ktor.util.*
import io.ktor.utils.io.core.*
import java.nio.charset.Charset
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val hashKey = System.getenv("HashKey").toByteArray()
private val  hmacKey = SecretKeySpec(hashKey,"HmacSHA1")

fun hash(password:String):String{
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charset.defaultCharset())))
}