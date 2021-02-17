package ar.com.latitud.helper

import java.util.Date
import java.util.Random
import java.util.UUID

object Id {

    /**
     * Generates an unique id
     *
     * @return string containing generated unique id
     */
    fun gen(): String {
        val uuid = UUID.randomUUID()
        return (java.lang.Long.toString(uuid.mostSignificantBits, 36) + java.lang.Long.toString(uuid.leastSignificantBits, 36)).replace("-", "")
    }

    fun generateBigIntPrimaryKey(): Long {
        val max = 99999
        val min = 10000
        val rand = Random()
        val randomNum = rand.nextInt(max - min + 1) + min
        val id = Date().time.toString() + "" + randomNum
        return java.lang.Long.parseLong(id)
    }

}
