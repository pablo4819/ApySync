package ar.com.latitud.sync.model

import ar.com.latitud.db.Cursor
import ar.com.latitud.db.Db
import ar.com.latitud.db.Entity
import ar.com.latitud.db.Values
import ar.com.latitud.helper.FDate
import java.util.*

class VisitaIncumplimiento : Entity<Int, VisitaIncumplimiento> {

    enum class Operacion {
        A, M, B
    }

    var visita_incumplimiento_id: Int? = null
    var visita_incumplimiento_visita_id: Int? = null
    var visita_incumplimiento_incumplimiento_id: Int? = null
    var visita_incumplimiento_tipo: Int? = null
    var visita_incumplimiento_nota: String? = null
    var visita_incumplimiento_fechanotificacion: Date? = null
    var visita_incumplimiento_fechaverificacion: Date? = null
    var visita_incumplimiento_fecharegularizacion: Date? = null
    var visita_incumplimiento_tipooperacion: Operacion? = null
    var visita_incumplimiento_cumplido: Int? = null
    var visita_incumplimiento_codigo_art: String? = null

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

    companion object {
        //table definicion
        const val TABLE = "visita_incumplimiento"
        const val VISITA_INCUMPLIMIENTO_ID = "visita_incumplimiento_id"
        const val VISITA_INCUMPLIMIENTO_VISITA_ID = "visita_incumplimiento_visita_id"
        const val VISITA_INCUMPLIMIENTO_INCUMPLIMIENTO_ID = "visita_incumplimiento_incumplimiento_id"
        const val VISITA_INCUMPLIMIENTO_TIPO = "visita_incumplimiento_tipo"
        const val VISITA_INCUMPLIMIENTO_NOTA = "visita_incumplimiento_nota"
        const val VISITA_INCUMPLIMIENTO_FECHANOTIFICACION = "visita_incumplimiento_fechanotificacion"
        const val VISITA_INCUMPLIMIENTO_FECHAVERIFICACION = "visita_incumplimiento_fechaverificacion"
        const val VISITA_INCUMPLIMIENTO_FECHAREGULARIZACION = "visita_incumplimiento_fecharegularizacion"
        const val VISITA_INCUMPLIMIENTO_TIPOOPERACION = "visita_incumplimiento_tipooperacion"
        const val VISITA_INCUMPLIMIENTO_CUMPLIDO = "visita_incumplimiento_cumplido"
        const val VISITA_INCUMPLIMIENTO_CODIGO_ART = "visita_incumplimiento_codigo_art"

        fun getById(visita_incumplimiento_id: Int): List<VisitaIncumplimiento> {
            @Suppress("UNCHECKED_CAST")
            return Db.get().search(Visita::class.java, VisitaIncumplimiento.TABLE, "${VISITA_INCUMPLIMIENTO_ID}=?", visita_incumplimiento_id) as List<VisitaIncumplimiento>
        }

        fun getByCodArtandIncId(codeArt: String, incumplimiento_id: Int): VisitaIncumplimiento? {
            @Suppress("UNCHECKED_CAST")
            val result = Db.get().search(VisitaIncumplimiento::class.java, TABLE, "$VISITA_INCUMPLIMIENTO_CODIGO_ART=? AND $VISITA_INCUMPLIMIENTO_INCUMPLIMIENTO_ID =?", codeArt, incumplimiento_id) as List<VisitaIncumplimiento>
            return if (result.isNotEmpty()) result[0] else null
        }
    }

    override val cv: Values
        get() {
            val values = Db.newCV()
            values.put(VISITA_INCUMPLIMIENTO_VISITA_ID, visita_incumplimiento_visita_id)
            values.put(VISITA_INCUMPLIMIENTO_INCUMPLIMIENTO_ID, visita_incumplimiento_incumplimiento_id)
            values.put(VISITA_INCUMPLIMIENTO_TIPO, visita_incumplimiento_tipo)
            values.put(VISITA_INCUMPLIMIENTO_NOTA, visita_incumplimiento_nota)
            values.put(VISITA_INCUMPLIMIENTO_FECHANOTIFICACION, FDate.formatU(visita_incumplimiento_fechanotificacion))
            values.put(VISITA_INCUMPLIMIENTO_FECHAVERIFICACION, FDate.formatU(visita_incumplimiento_fechaverificacion))
            values.put(VISITA_INCUMPLIMIENTO_FECHAREGULARIZACION, FDate.formatU(visita_incumplimiento_fecharegularizacion))
            values.put(VISITA_INCUMPLIMIENTO_TIPOOPERACION, visita_incumplimiento_tipooperacion)
            values.put(VISITA_INCUMPLIMIENTO_CUMPLIDO, visita_incumplimiento_cumplido)
            values.put(VISITA_INCUMPLIMIENTO_CODIGO_ART, visita_incumplimiento_codigo_art)

            return values
        }

    override fun map(c: Cursor) {
        id = c.getInt(ID)
        visita_incumplimiento_visita_id = c.getInt(VISITA_INCUMPLIMIENTO_VISITA_ID)
        visita_incumplimiento_incumplimiento_id = c.getInt(VISITA_INCUMPLIMIENTO_INCUMPLIMIENTO_ID)
        visita_incumplimiento_tipo = c.getInt(VISITA_INCUMPLIMIENTO_TIPO)
        visita_incumplimiento_nota = c.getString(VISITA_INCUMPLIMIENTO_NOTA)
        visita_incumplimiento_fechanotificacion = FDate.parseDate(c.getString(VISITA_INCUMPLIMIENTO_FECHANOTIFICACION))
        visita_incumplimiento_fechaverificacion = FDate.parseDate(c.getString(VISITA_INCUMPLIMIENTO_FECHAVERIFICACION))
        visita_incumplimiento_fecharegularizacion = FDate.parseDate(c.getString(VISITA_INCUMPLIMIENTO_FECHAREGULARIZACION))
        visita_incumplimiento_tipooperacion = if (c.getString(VISITA_INCUMPLIMIENTO_TIPOOPERACION) != null) Operacion.valueOf(c.getString(VISITA_INCUMPLIMIENTO_TIPOOPERACION)!!) else null
        visita_incumplimiento_cumplido = c.getInt(VISITA_INCUMPLIMIENTO_CUMPLIDO)
        visita_incumplimiento_codigo_art = c.getString(VISITA_INCUMPLIMIENTO_CODIGO_ART)
    }

    fun updateVisitaIncumplimiento(codeArt: String, IncumplimientoId: Int): Int {
        return Db.get().update(TABLE, cv, "${VISITA_INCUMPLIMIENTO_CODIGO_ART}=? " + "and ${VISITA_INCUMPLIMIENTO_INCUMPLIMIENTO_ID}=?", codeArt, IncumplimientoId)
    }

    override var id: Int?
        get() = visita_incumplimiento_id
        set(value) {
            visita_incumplimiento_id = value
        }
    override val idColumnName: String
        get() = VISITA_INCUMPLIMIENTO_ID

    override val table: String
        get() = TABLE
    override val ID: String
        get() = "visita_incumplimiento_id"
}