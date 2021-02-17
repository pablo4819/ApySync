package ar.com.latitud.db

interface Cursor {

    fun moveToFirst(): Boolean

    operator fun next(): Boolean

    fun close()

    fun getString(columnName: String): String?

    fun getInt(columnName: String): Int?

    fun getLong(columnName: String): Long?

    fun getBoolean(columnName: String): Boolean?

    fun getDouble(columnName: String): Double?

    fun getBlob(columnName: String): ByteArray
}
