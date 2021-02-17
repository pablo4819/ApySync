package ar.com.latitud.db

interface Values {

    fun put(key: String, value: String?)

    fun put(key: String, value: Int?)

    fun put(key: String, value: Long?)

    fun put(key: String, value: Double?)

    fun put(key: String, value: Any?)

    fun valuesUpdate(): String

    fun valuesInsert(): String

}
