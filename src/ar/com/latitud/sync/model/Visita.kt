package ar.com.latitud.sync.model

import ar.com.latitud.db.Cursor
import ar.com.latitud.db.Db
import ar.com.latitud.db.Entity
import ar.com.latitud.db.Values
import ar.com.latitud.helper.FDate
import java.util.*

class Visita : Entity<Int, Visita> {

    enum class Operacion {
        A, M, B
    }

    var visita_id: Int? = null
    var visita_establecimiento_id: Int? = null
    var visita_fechaalta: Date? = null
    var visita_fechaactualizacion: Date? = null
    var visita_nota: String? = null
    var visita_tipointervencion: String? = null
    var visita_tipooperacion: Operacion? = null
    var visita_tipo: Int? = null
    var visita_entrevistadonombre: String? = null
    var visita_entrevistadocargo: String? = null
    var visita_dueno: Int? = null
    var visita_nombredueno: String? = null
    var visita_evaluador: String? = null
    var visita_shtnombre: String? = null
    var visita_estado: Int? = null
    var visita_sincronizada: Int? = null
    var visita_presentada: Int? = null
    var visita_presentada_users_id: Int? = null
    var visita_tituloitem_codigo: Int? = null
    var visita_agrupacion_id: Int? = null
    var visita_rar_contacto: String? = null
    var visita_rar_tel: String? = null
    var visita_rar_email: String? = null
    var visita_rar_resp: String? = null
    var visita_rar_resp_dni: Int? = null
    var visita_cap_tema: String? = null
    var visita_motivo: Int? = null
    var visita_resultado: Int? = null
    var visita_nrovisita: Int? = null
    var visita_rechazo_id: Int? = null
    var visita_fgen: Date? = null
    var visita_fmod: Date? = null
    var visita_codigo: Int? = null
    var visita_efectiva: Int? = null
    var visita_v_tipo_id: Int? = null
    var visita_tipo_resultado_id: Int? = null
    var visita_codigoagendavisita: Int? = null
    var visita_observacion: String? = null
    var visita_result_error: String? = null
    var visita_result_fault: String? = null
    var visita_codigo_art: String? = null

    constructor()

    constructor(id: Int) {
        try {
            get(id)
        } catch (e: Exception) {
            LOG.error("Error instantiating " + javaClass.simpleName + " class", e)
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
        get() = "visita_id"

    override var id: Int?
        get() = visita_id
        set(value) {
            visita_id = value
        }
    override val idColumnName: String
        get() = VISITA_ID
    override val table: String
        get() = TABLE
    override val cv: Values
        get() {
            val values = Db.newCV()
            values.put(VISITA_ESTABLECIMIENTO_ID, visita_establecimiento_id)
            values.put(VISITA_FECHAALTA, FDate.formatU(visita_fechaalta))
            values.put(VISITA_FECHAACTUALIZACION, FDate.formatU(visita_fechaactualizacion))
            values.put(VISITA_NOTA, visita_nota)
            values.put(VISITA_TIPOINTERVENCION, visita_tipointervencion)
            values.put(VISITA_TIPOOPERACION, visita_tipooperacion)
            values.put(VISITA_TIPO, visita_tipo)
            values.put(VISITA_ENTREVISTADONOMBRE, visita_entrevistadonombre)
            values.put(VISITA_ENTREVISTADOCARGO, visita_entrevistadocargo)
            values.put(VISITA_DUENO, visita_dueno)
            values.put(VISITA_NOMBREDUENO, visita_nombredueno)
            values.put(VISITA_EVALUADOR, visita_evaluador)
            values.put(VISITA_SHTNOMBRE, visita_shtnombre)
            values.put(VISITA_ESTADO, visita_estado)
            values.put(VISITA_SINCRONIZADA, "1")
            values.put(VISITA_PRESENTADA, visita_presentada)
            values.put(VISITA_PRESENTADA_USERS_ID, visita_presentada_users_id)
            values.put(VISITA_TITULOITEM_CODIGO, visita_tituloitem_codigo)
            values.put(VISITA_AGRUPACION_ID, visita_agrupacion_id)
            values.put(VISITA_RAR_CONTACTO, visita_rar_contacto)
            values.put(VISITA_RAR_TEL, visita_rar_tel)
            values.put(VISITA_RAR_EMAIL, visita_rar_email)
            values.put(VISITA_RAR_RESP, visita_rar_resp)
            values.put(VISITA_RAR_RESP_DNI, visita_rar_resp_dni)
            values.put(VISITA_CAP_TEMA, visita_cap_tema)
            values.put(VISITA_MOTIVO, visita_motivo)
            values.put(VISITA_RESULTADO, visita_resultado)
            values.put(VISITA_NROVISITA, visita_nrovisita)
            values.put(VISITA_RECHAZO_ID, visita_rechazo_id)
            values.put(VISITA_FGEN, FDate.formatU(visita_fgen))
            values.put(VISITA_FMOD, FDate.formatU(visita_fmod))
            values.put(VISITA_CODIGO, visita_codigo)
            values.put(VISITA_EFECTIVA, visita_efectiva)
            values.put(VISITA_V_TIPO_ID, visita_v_tipo_id)
            values.put(VISITA_TIPO_RESULTADO_ID, visita_tipo_resultado_id)
            values.put(VISITA_CODIGOAGENDAVISITA, visita_codigoagendavisita)
            values.put(VISITA_OBSERVACION, visita_observacion)
            values.put(VISITA_RESULT_ERROR, visita_result_error)
            values.put(VISITA_RESULT_FAULT, visita_result_fault)
            values.put(VISITA_CODIGO_ART, visita_codigo_art)
            return values
        }

    override fun map(c: Cursor) {
        id = c.getInt(ID)
        visita_establecimiento_id = c.getInt(VISITA_ESTABLECIMIENTO_ID)
        visita_fechaalta = FDate.parseDate(c.getString(VISITA_FECHAALTA))
        visita_fechaactualizacion = FDate.parseDate(c.getString(VISITA_FECHAACTUALIZACION))
        visita_nota = c.getString(VISITA_NOTA)
        visita_tipointervencion = c.getString(VISITA_TIPOINTERVENCION)
        visita_tipooperacion = if (c.getString(VISITA_TIPOOPERACION) != null) Operacion.valueOf(c.getString(VISITA_TIPOOPERACION)!!) else null
        visita_tipo = c.getInt(VISITA_TIPO)
        visita_entrevistadonombre = c.getString(VISITA_ENTREVISTADONOMBRE)
        visita_entrevistadocargo = c.getString(VISITA_ENTREVISTADOCARGO)
        visita_dueno = c.getInt(VISITA_DUENO)
        visita_nombredueno = c.getString(VISITA_NOMBREDUENO)
        visita_evaluador = c.getString(VISITA_EVALUADOR)
        visita_shtnombre = c.getString(VISITA_SHTNOMBRE)
        visita_estado = c.getInt(VISITA_ESTADO)
        visita_sincronizada = c.getInt(VISITA_SINCRONIZADA)
        visita_presentada = c.getInt(VISITA_PRESENTADA)
        visita_presentada_users_id = c.getInt(VISITA_PRESENTADA_USERS_ID)
        visita_tituloitem_codigo = c.getInt(VISITA_TITULOITEM_CODIGO)
        visita_agrupacion_id = c.getInt(VISITA_AGRUPACION_ID)
        visita_rar_contacto = c.getString(VISITA_RAR_CONTACTO)
        visita_rar_tel = c.getString(VISITA_RAR_TEL)
        visita_rar_email = c.getString(VISITA_RAR_EMAIL)
        visita_rar_resp = c.getString(VISITA_RAR_RESP)
        visita_rar_resp_dni = c.getInt(VISITA_RAR_RESP_DNI)
        visita_cap_tema = c.getString(VISITA_CAP_TEMA)
        visita_motivo = c.getInt(VISITA_MOTIVO)
        visita_resultado = c.getInt(VISITA_RESULTADO)
        visita_nrovisita = c.getInt(VISITA_NROVISITA)
        visita_rechazo_id = c.getInt(VISITA_RECHAZO_ID)
        visita_fgen = FDate.parseDate(c.getString(VISITA_FGEN))
        visita_fmod = FDate.parseDate(c.getString(VISITA_FMOD))
        visita_codigo = c.getInt(VISITA_CODIGO)
        visita_efectiva = c.getInt(VISITA_EFECTIVA)
        visita_v_tipo_id = c.getInt(VISITA_V_TIPO_ID)
        visita_tipo_resultado_id = c.getInt(VISITA_TIPO_RESULTADO_ID)
        visita_codigoagendavisita = c.getInt(VISITA_CODIGOAGENDAVISITA)
        visita_observacion = c.getString(VISITA_OBSERVACION)
        visita_result_error = c.getString(VISITA_RESULT_ERROR)
        visita_result_fault = c.getString(VISITA_RESULT_FAULT)
        visita_codigo_art = c.getString(VISITA_CODIGO_ART)
    }

    companion object {
        // Table definition
        const val TABLE = "visita"
        const val VISITA_ID = "visita_id"
        const val VISITA_ESTABLECIMIENTO_ID = "visita_establecimiento_id"
        const val VISITA_FECHAALTA = "visita_fechaalta"
        const val VISITA_FECHAACTUALIZACION = "visita_fechaactualizacion"
        const val VISITA_NOTA = "visita_nota"
        const val VISITA_TIPOINTERVENCION = "visita_tipointervencion"
        const val VISITA_TIPOOPERACION = "visita_tipooperacion"
        const val VISITA_TIPO = "visita_tipo"
        const val VISITA_ENTREVISTADONOMBRE = "visita_entrevistadonombre"
        const val VISITA_ENTREVISTADOCARGO = "visita_entrevistadocargo"
        const val VISITA_DUENO = "visita_dueno"
        const val VISITA_NOMBREDUENO = "visita_nombredueno"
        const val VISITA_EVALUADOR = "visita_evaluador"
        const val VISITA_SHTNOMBRE = "visita_shtnombre"
        const val VISITA_ESTADO = "visita_estado"
        const val VISITA_SINCRONIZADA = "visita_sincronizada"
        const val VISITA_PRESENTADA = "visita_presentada"
        const val VISITA_PRESENTADA_USERS_ID = "visita_presentada_users_id"
        const val VISITA_TITULOITEM_CODIGO = "visita_tituloitem_codigo"
        const val VISITA_AGRUPACION_ID = "visita_agrupacion_id"
        const val VISITA_RAR_CONTACTO = "visita_rar_contacto"
        const val VISITA_RAR_TEL = "visita_rar_tel"
        const val VISITA_RAR_EMAIL = "visita_rar_email"
        const val VISITA_RAR_RESP = "visita_rar_resp"
        const val VISITA_RAR_RESP_DNI = "visita_rar_resp_dni"
        const val VISITA_CAP_TEMA = "visita_cap_tema"
        const val VISITA_MOTIVO = "visita_motivo"
        const val VISITA_RESULTADO = "visita_resultado"
        const val VISITA_NROVISITA = "visita_nrovisita"
        const val VISITA_RECHAZO_ID = "visita_rechazo_id"
        const val VISITA_FGEN = "visita_fgen"
        const val VISITA_FMOD = "visita_fmod"
        const val VISITA_CODIGO = "visita_codigo"
        const val VISITA_EFECTIVA = "visita_efectiva"
        const val VISITA_V_TIPO_ID = "visita_v_tipo_id"
        const val VISITA_TIPO_RESULTADO_ID = "visita_tipo_resultado_id"
        const val VISITA_CODIGOAGENDAVISITA = "visita_codigoagendavisita"
        const val VISITA_OBSERVACION = "visita_observacion"
        const val VISITA_RESULT_ERROR = "visita_result_error"
        const val VISITA_RESULT_FAULT = "visita_result_fault"
        const val VISITA_CODIGO_ART = "visita_codigo_art"

        fun getById(visita_id: Int): List<Visita> {
            @Suppress("UNCHECKED_CAST")
            return Db.get().search(Visita::class.java, TABLE, "$VISITA_ID=?", visita_id) as List<Visita>
        }

        fun getByDate(alta: String): List<Visita> {
            @Suppress("UNCHECKED_CAST")
            return Db.get().search(Visita::class.java, TABLE, "$VISITA_FECHAALTA LIKE ?", alta) as List<Visita>
        }

        fun getByEvaluador(evaluador: String): List<Visita> {
            @Suppress("UNCHECKED_CAST")
            return Db.get().search(Visita::class.java, TABLE, "$VISITA_EVALUADOR = ?", evaluador) as List<Visita>
        }

        fun orderByDate(): List<Visita> {
            @Suppress("UNCHECKED_CAST")
            return Db.get().search(Visita::class.java, TABLE, "ORDER BY $VISITA_FECHAALTA DESC") as List<Visita>
        }

        fun getByCodArt(codigoArt: String): Visita? {
            @Suppress("UNCHECKED_CAST")
            val result = Db.get().search(Visita::class.java, TABLE, "$VISITA_CODIGO_ART=?", codigoArt) as List<Visita>
            return if (result.isNotEmpty()) result[0] else null
        }
    }
}