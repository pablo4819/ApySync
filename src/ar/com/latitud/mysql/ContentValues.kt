package ar.com.latitud.mysql

import ar.com.latitud.db.Values
import java.util.*

class ContentValues : Values {

    private val values: MutableMap<String, Any?>

    init {
        values = HashMap()
    }

    override fun put(key: String, value: String?) {
        values[key] = value
    }

    override fun put(key: String, value: Int?) {
        values[key] = value
    }

    override fun put(key: String, value: Long?) {
        values[key] = value
    }

    override fun put(key: String, value: Double?) {
        values[key] = value
    }

    override fun put(key: String, value: Any?) {
        values[key] = value
    }

    override fun valuesUpdate(): String {
        val result = StringBuilder()
        for (entry in values) {
            if (entry.value != null)
                result.append("`").append(entry.key).append("`='").append(entry.value.toString()).append("',")
            else
                result.append(entry.key).append("=null,")
        }
        return result.substring(0, result.length - 1)
    }

    override fun valuesInsert(): String {
        var columns = StringBuilder()
        var data = StringBuilder()
        for (entry in values) {
            columns.append("`").append(entry.key).append("`,")
            if (entry.value != null)
                data.append("'").append(entry.value.toString()).append("',")
            else
                data.append("null,")
        }
        columns = StringBuilder(columns.substring(0, columns.length - 1))
        data = StringBuilder(data.substring(0, data.length - 1))
        return "($columns) VALUES ($data)"
    }

}
