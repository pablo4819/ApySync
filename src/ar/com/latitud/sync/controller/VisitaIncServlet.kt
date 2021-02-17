package ar.com.latitud.sync.controller

import ar.com.latitud.sync.model.Visita
import ar.com.latitud.sync.model.VisitaIncumplimiento
import ar.com.latitud.db.Entity.Companion.LOG
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang.StringUtils
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class VisitaIncServlet : HttpServlet() {

    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss")
        gson = gsonBuilder.create()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val path = StringUtils.split(req.pathInfo, '/')
            when {
                path.size == 1 && path[0] == "getAll" -> {
                    resp.outputStream.write(gson.toJson(VisitaIncumplimiento().getAll(VisitaIncumplimiento.VISITA_INCUMPLIMIENTO_INCUMPLIMIENTO_ID, false)).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 2 && path[0] == "getId" -> {
                    resp.outputStream.write(gson.toJson(VisitaIncumplimiento.getById(path[1].toInt())).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 3 && path[0] == "getIncumplimientoId" -> {
                    resp.outputStream.write(gson.toJson(VisitaIncumplimiento.getByCodArtandIncId(path[1].toString(), path[2].toInt())).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
            }
        } catch (e: Exception) {
            LOG.error("Invalid request", e)
            resp.outputStream.write(SimpleMsg.create(e))
            resp.contentType = "application/json"
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val visitaincumplimiento = gson.fromJson(req.reader, VisitaIncumplimiento::class.java)
            if (visitaincumplimiento != null) {
                val aux = VisitaIncumplimiento.getByCodArtandIncId(visitaincumplimiento.visita_incumplimiento_codigo_art!!, visitaincumplimiento.visita_incumplimiento_incumplimiento_id!!)
                if (aux == null) {
                    val visita = Visita.getByCodArt(visitaincumplimiento.visita_incumplimiento_codigo_art!!)
                    if (visita != null) {
                        visitaincumplimiento.visita_incumplimiento_visita_id = visita.visita_id
                        if (visitaincumplimiento.insert() > 0) {
                            resp.outputStream.write(gson.toJson(visitaincumplimiento).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    }
                } else {
                    val visita = Visita.getByCodArt(visitaincumplimiento.visita_incumplimiento_codigo_art!!)
                    if (visita != null) {
                        visitaincumplimiento.visita_incumplimiento_visita_id = visita.visita_id
                        if (visitaincumplimiento.updateVisitaIncumplimiento(visitaincumplimiento.visita_incumplimiento_codigo_art!!, visitaincumplimiento.visita_incumplimiento_incumplimiento_id!!) > 0) {
                            resp.outputStream.write(gson.toJson(visitaincumplimiento).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al actualizar Visita Incumplimiento"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_NOT_FOUND
                        }
                    }
                }
            } else {
                resp.outputStream.write(SimpleMsg.create("Datos incorrectos de los incumplimientos"))
                resp.contentType = "application/json"
                resp.status = HttpServletResponse.SC_NOT_FOUND
            }
        } catch (e: Exception) {
            LOG.error("Invalid request", e)
            resp.outputStream.write(SimpleMsg.create(e))
            resp.contentType = "application/json"
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

    override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val path = StringUtils.split(req.pathInfo, '/')
            when {
                path.size == 3 && path[0] == "deletevisitaincumplimiento" -> {
                    val visitaIncumplimiento = VisitaIncumplimiento.getByCodArtandIncId(path[1].toString(), path[2].toInt())
                    if (visitaIncumplimiento != null) {
                        if (visitaIncumplimiento.delete() > 0) {
                            resp.outputStream.write(SimpleMsg.create("La Visita ha sido eliminada con éxito"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al eliminar la Visita"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Datos incorrectos en la Visita"))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_NOT_FOUND
                    }
                }
            }
        } catch (e: Exception) {
            LOG.error("Invalid request", e)
            resp.outputStream.write(SimpleMsg.create(e))
            resp.contentType = "application/json"
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }
}