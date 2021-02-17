package ar.com.latitud.mysql

import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.util.*

object Params {

    private val LOG = LoggerFactory.getLogger(Params::class.java)

    var webport: Int = 0
        private set
    var webportSsl: Int = 0
        private set
    lateinit var host: String
        private set
    var port: Int = 0
        private set
    lateinit var user: String
        private set
    lateinit var pass: String
        private set
    lateinit var base: String
        private set

    fun getParams(filePath: String): Boolean {
        return try {
            val fis = FileInputStream(File(filePath))
            val props = Properties()
            props.load(fis)
            webport = Integer.valueOf(props.getProperty("WEBPORT", "0"))
            webportSsl = Integer.valueOf(props.getProperty("WEBPORT_SSL", "0"))
            host = props.getProperty("HOST", "")
            port = Integer.valueOf(props.getProperty("PORT", "0"))
            user = props.getProperty("USER", "")
            pass = props.getProperty("PASS", "")
            base = props.getProperty("BASE", "")
            fis.close()
            true
        } catch (e: Exception) {
            LOG.error("Error getting parameters", e)
            false
        }
    }

}