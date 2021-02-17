package ar.com.latitud.mysql

import ar.com.latitud.db.Cursor
import java.sql.ResultSet

class MySqlCursor(private val rs: ResultSet) : Cursor {

    override fun moveToFirst(): Boolean {
        return rs.first()
    }

    override fun next(): Boolean {
        return rs.next()
    }

    override fun close() {
        rs.close()
    }

    override fun getString(columnName: String): String? {
        return rs.getString(columnName)
    }

    override fun getInt(columnName: String): Int? {
        val value = rs.getInt(columnName)
        return if (rs.wasNull()) null else value
    }

    override fun getLong(columnName: String): Long? {
        val value = rs.getLong(columnName)
        return if (rs.wasNull()) null else value
    }

    override fun getBoolean(columnName: String): Boolean? {
        val value = rs.getBoolean(columnName)
        return if (rs.wasNull()) null else value
    }

    override fun getDouble(columnName: String): Double? {
        val value = rs.getDouble(columnName)
        return if (rs.wasNull()) null else value
    }

    override fun getBlob(columnName: String): ByteArray {
        /*Blob blob = rs.getBlob(columnName);
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        blob.free();
        return bytes;*/
        return rs.getBytes(columnName)
    }
}
