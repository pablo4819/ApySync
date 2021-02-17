package ar.com.latitud.sync.controller

import ar.com.latitud.sync.model.LogSync
import ar.com.latitud.sync.model.Visita
import ar.com.latitud.db.Entity.Companion.LOG
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang.StringUtils
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogSyncServlet : HttpServlet() {
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
                    resp.outputStream.write(gson.toJson(LogSync().getAll(LogSync.LOGSYNC_ID, false)).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 2 && path[0] == "getid" -> {
                    resp.outputStream.write(gson.toJson(LogSync.getById(path[1].toInt())).toByteArray(StandardCharsets.UTF_8))
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
            val logSync = gson.fromJson(req.reader, LogSync::class.java)
            if (logSync != null) {
                if (LogSync.getByCod(logSync.codigo!!) == null) {
                    if (logSync.insert() > 0) {
                        resp.outputStream.write(gson.toJson(logSync).toByteArray(StandardCharsets.UTF_8))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_OK
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Error al crear la LogSync"))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_NOT_FOUND
                    }
                } else {
                    logSync.updatePub = Date()
                    if (logSync.updateLog(logSync.codigo) > 0) {
                        resp.outputStream.write(gson.toJson(logSync).toByteArray(StandardCharsets.UTF_8))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_OK
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Error al actualizar la LogSync"))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_NOT_FOUND
                    }
                }
            } else {
                resp.outputStream.write(SimpleMsg.create("Datos incorrectos en la nueva LogSync"))
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
        /*try {
            val path = StringUtils.split(req.pathInfo, '/')
            when {
                path.size == 2 && path[0] == "deleteLogSync" -> {
                    val visita = LogSync.getByCodArt(path[1])
                    if (visita != null) {
                        if (visita.delete() > 0) {
                            resp.outputStream.write(SimpleMsg.create("La LogSync ha sido eliminada con éxito"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al eliminar la LogSync"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Datos incorrectos en la LogSync"))
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
        }*/
    }
}