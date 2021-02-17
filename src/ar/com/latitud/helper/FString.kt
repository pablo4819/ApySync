package ar.com.latitud.helper

import org.slf4j.LoggerFactory

object FString {

    private val LOG = LoggerFactory.getLogger(FString::class.java)

    /**
     * Rellena la cadena indicada con el caracter especificado en la cantidad especificada a la izquierda
     *
     * @param string    Cadena a rellenar
     * @param character Carácter con el que se va a rellenar
     * @param length    Longitud final de la cadena
     * @return string
     */
    fun padLeft(string: String, character: String, length: Int): String {
        val padding = StringBuilder()
        for (i in 0 until length) padding.append(character)
        return (padding.toString() + string).substring((padding.toString() + string).length - length)
    }

    /**
     * Convierte el número en cadena y la rellena con el caracter especificado en la cantidad especificada a la izquierda
     *
     * @param number    Número a rellenar
     * @param character Carácter con el que se va a rellenar
     * @param length    Longitud final de la cadena
     * @return string
     */
    fun padLeft(number: Int, character: String, length: Int): String {
        return padLeft(number.toString(), character, length)
    }

    fun join(collection: Collection<Any>, glue: String): String {
        val result = StringBuilder()
        for (o in collection) result.append(glue).append(o.toString())
        return if (result.isNotEmpty())
            result.substring(glue.length)
        else
            ""
    }

    fun join(array: Array<Any>, glue: String): String {
        val result = StringBuilder()
        for (o in array) result.append(glue).append(o.toString())
        return if (result.isNotEmpty())
            result.substring(glue.length)
        else
            ""
    }

    fun join(array: Array<String>, glue: String): String {
        val result = StringBuilder()
        for (o in array) result.append(glue).append(o)
        return if (result.isNotEmpty())
            result.substring(glue.length)
        else
            ""
    }

}
