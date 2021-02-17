package ar.com.latitud.sync.controller

import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.util.*

object WebParams {

    private val LOG = LoggerFactory.getLogger(WebParams::class.java)

    var webport: Int = 0
        private set
    var webportSsl: Int = 0
        private set

    fun getParams(filePath: String): Boolean {
        try {
            val `is` = FileInputStream(File(filePath))
            val props = Properties()
            props.load(`is`)
            webport = Integer.valueOf(props.getProperty("WEBPORT", "0"))
            webportSsl = Integer.valueOf(props.getProperty("WEBPORT_SSL", "0"))
            `is`.close()
            return true
        } catch (e: Exception) {
            LOG.error("Error getting parameters", e)
            return false
        }
    }

}