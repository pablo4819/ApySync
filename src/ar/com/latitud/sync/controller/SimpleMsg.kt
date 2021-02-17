package ar.com.latitud.sync.controller

import java.nio.charset.StandardCharsets

class SimpleMsg {
    companion object {
        fun create(msg: String): ByteArray {
            return "{\"msg\":\"$msg\"}".toByteArray(StandardCharsets.UTF_8)
        }

        fun create(e: Exception): ByteArray {
            return "{\"msg\":\"${e.message}\"}".toByteArray(StandardCharsets.UTF_8)
        }

        fun unkownFunction(): ByteArray {
            return "{\"msg\":\"Función desconocida\"}".toByteArray(StandardCharsets.UTF_8)
        }
    }
}