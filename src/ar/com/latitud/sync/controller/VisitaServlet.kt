package ar.com.latitud.sync.controller

import ar.com.latitud.sync.model.Visita
import ar.com.latitud.db.Entity.Companion.LOG
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang.StringUtils
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class VisitaServlet : HttpServlet() {

    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss")
        gson = gsonBuilder.create()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val path = StringUtils.split(req.pathInfo, '/')
            when {
                path.size == 1 && path[0] == "getall" -> {
                    resp.outputStream.write(gson.toJson(Visita().getAll(Visita.VISITA_FECHAALTA, false)).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 2 && path[0] == "getid" -> {
                    resp.outputStream.write(gson.toJson(Visita.getById(path[1].toInt())).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 2 && path[0] == "getDate" -> {
                    resp.outputStream.write(gson.toJson(Visita.getByDate(path[1])).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 2 && path[0] == "getEvaluador" -> {
                    resp.outputStream.write(gson.toJson(Visita.getByEvaluador(path[1].toString())).toByteArray(StandardCharsets.UTF_8))
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
            val visita = gson.fromJson(req.reader, Visita::class.java)
            if (visita != null) {
                if (Visita.getByCodArt(visita.visita_codigo_art!!) == null) {
                    if (visita.insert() > 0) {
                        resp.outputStream.write(gson.toJson(visita).toByteArray(StandardCharsets.UTF_8))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_OK
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Error al crear la Visita"))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_NOT_FOUND
                    }
                } else {
                    if (visita.updateVisita(visita.visita_codigo_art) > 0) {
                        resp.outputStream.write(gson.toJson(visita).toByteArray(StandardCharsets.UTF_8))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_OK
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Error al actualizar la Visita"))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_NOT_FOUND
                    }
                }
            } else {
                resp.outputStream.write(SimpleMsg.create("Datos incorrectos en la nueva Visita"))
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
                path.size == 2 && path[0] == "deleteVisita" -> {
                    val visita = Visita.getByCodArt(path[1])
                    if (visita != null) {
                        if (visita.delete() > 0) {
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