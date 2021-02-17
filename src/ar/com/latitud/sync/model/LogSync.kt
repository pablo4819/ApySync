package ar.com.latitud.sync.model

import ar.com.latitud.db.Cursor
import ar.com.latitud.db.Db
import ar.com.latitud.db.Entity
import ar.com.latitud.db.Values
import ar.com.latitud.helper.FDate
import java.util.*

class LogSync : Entity<Int, LogSync> {

    enum class Accion {
        INSERT, UPDATE, DELETE
    }

    var logsync_id: Int? = null
    var codigo: Long? = null
    var entidad: String? = null
    var datosJson: String? = null
    var usuarioId: Int? = null
    var accion: Accion? = null
    var updateServer: Date? = null
    var updatePub: Date? = null

    constructor()

    constructor(id: Int) {
        try {
            get(id)
        } catch (e: Exception) {
            LOG.error("Error instantiating " + javaClass.simpleName + " class ", e)
        }
    }

    constructor(c: Cursor) {
        try {
            map(c)
        } catch (e: Exception) {
            LOG.error("Error instantiating " + javaClass.simpleName + " class", e)
        }
    }

    override val ID: String
        get() = "logsync_id"
    override var id: Int?
        get() = logsync_id
        set(value) {
            logsync_id = value
        }
    override val idColumnName: String
        get() = ID
    override val table: String
        get() = TABLE
    override val cv: Values
        get() {
            val values = Db.newCV()
            values.put(CODIGO, codigo)
            values.put(ENTIDAD, entidad)
            values.put(DATOSJSON, datosJson)
            values.put(USUARIOID, usuarioId)
            values.put(ACCION, accion)
            values.put(UPDATESERVER, FDate.formatU(updateServer))
            values.put(UPDATEPUB, FDate.formatU(updatePub))
            return values
        }

    override fun map(c: Cursor) {
        id = c.getInt(ID)
        codigo = c.getLong(CODIGO)
        entidad = c.getString(ENTIDAD)
        datosJson = c.getString(DATOSJSON)
        usuarioId = c.getInt(USUARIOID)
        accion = if (c.getString(ACCION) != null) Accion.valueOf(c.getString(ACCION)!!) else null
        updateServer = FDate.parseDate(c.getString(UPDATESERVER))
        updatePub = FDate.parseDate(c.getString(UPDATEPUB))
    }

    companion object {
        // Table definition
        const val TABLE = "logsync"
        const val LOGSYNC_ID = "logsync_id"
        const val CODIGO = "codigo"
        const val ENTIDAD = "entidad"
        const val DATOSJSON = "datosJson"
        const val USUARIOID = "usuarioId"
        const val ACCION = "accion"
        const val UPDATESERVER = "updateServer"
        const val UPDATEPUB = "updatePub"

        fun getById(id: Int): List<LogSync> {
            @Suppress("UNCHECKED_CAST")
            return Db.get().search(LogSync::class.java, TABLE, "$LOGSYNC_ID=?", id) as List<LogSync>
        }

        fun getByCod(codigo: Long): LogSync? {
            @Suppress("UNCHECKED_CAST")
            val result = Db.get().search(LogSync::class.java, LogSync.TABLE, "${LogSync.CODIGO}=?", codigo) as List<LogSync>
            return if (result.isNotEmpty()) result[0] else null
        }
    }


}