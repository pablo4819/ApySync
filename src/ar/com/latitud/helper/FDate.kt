package ar.com.latitud.helper

import java.sql.Time
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

import ar.com.latitud.helper.FString.padLeft

object FDate {

    private fun pad(number: Int): String {
        return padLeft(number, "0", 2)
    }

    private fun pad(string: String): String {
        return padLeft(string, "0", 2)
    }

    /**
     * Formatea la fecha en la forma dd/mm/aaaa (legible para los humanos)
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatH(date: Date?): String {
        if (date == null) return ""
        val cal = Cal(date)
        return pad(cal.day) + "/" + pad(cal.month) + "/" + cal.year
    }

    /**
     * Formatea la fecha en la forma dd/mm/aaaa (legible para los humanos)
     *
     * @param day   día
     * @param month mes
     * @param year  año
     * @return Cadena formateada, devuelve una cadena vacía si los valores de los parámetros no se pueden convertir a una fecha
     */
    fun formatH(day: Int, month: Int, year: Int): String {
        return pad(day) + "/" + pad(month) + "/" + year
    }

    /**
     * Formatea la fecha en la forma dd/mm/aaaa (legible para los humanos)
     *
     * @param date fecha en el formato universal aaaa-MM-dd hh:mm:ss o formato aaaa-MM-dd
     * @return Cadena formateada, devuelve una cadena vacía si el parámetro es null o no tiene tiene el formato correcto
     */
    fun formatH(date: String?): String {
        if (date == null || date == "") return ""
        val aux = if (date.contains(" ")) date.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() else date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return pad(aux[2]) + "/" + pad(aux[1]) + "/" + aux[0]
    }

    /**
     * Formatea la fecha en la forma dd/mm/aaaa (legible para los humanos)
     *
     * @param milliseconds timestamp
     * @return Cadena formateada, devuelve una cadena vacía si el parámetro es null o no tiene tiene el formato correcto
     */
    fun formatH(milliseconds: Long): String {
        return formatH(Date(milliseconds))
    }

    /**
     * Formatea la fecha en la forma dd/mm/aaaa hh:mm (legible para los humanos)
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatHC(date: Date?): String {
        if (date == null) return ""
        val cal = Cal(date)
        return formatH(date) + " " + pad(cal.hour) + ":" + pad(cal.minute)
    }

    /**
     * Formatea la fecha en la forma dd/mm/aaaa hh:mm (legible para los humanos)
     *
     * @param milliseconds timestamp
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatHC(milliseconds: Long): String {
        return formatHC(Date(milliseconds))
    }

    /**
     * Formatea la fecha en la forma dd/mm/aaaa hh:mm:ss (legible para los humanos)
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatHCS(date: Date?): String {
        if (date == null) return ""
        val calendar = Calendar.getInstance()
        calendar.time = date
        return formatHC(date) + ":" + pad(calendar.get(Calendar.SECOND))
    }

    /**
     * Formatea la fecha en la forma dd/mm/aaaa hh:mm:ss (legible para los humanos)
     *
     * @param milliseconds timestamp
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatHCS(milliseconds: Long): String {
        return formatHCS(Date(milliseconds))
    }

    /**
     * Formatea la fecha al idioma español en la forma [day of month] días del mes de [month name] de [year]
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatHS(date: Date?): String {
        if (date == null) return ""
        val cal = Cal(date)
        return cal.day.toString() + " días del mes de " + getMonthName(cal.month) + " de " + cal.year
    }

    /**
     * Formatea la fecha al idioma español en la forma [day of month] días del mes de [month name] de [year]
     *
     * @param milliseconds timestamp
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatHS(milliseconds: Long): String {
        return formatHS(Date(milliseconds))
    }

    /**
     * Formatea la fecha al idioma español en la forma [day of month] días del mes de [month name] de [year]
     *
     * @param date fecha en formato aaaa-MM-dd hh:mm:ss
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatHS(date: String?): String {
        return if (date == null || date == "") "" else formatHS(parseDate(date))
    }

    /**
     * Formatea la fecha actual en una cadena con el formato aaaammdd_hhmmss para nombres de archivo (FileName)
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatFN(date: Date?): String {
        return if (date == null) "" else formatU(date).replace("-".toRegex(), "").replace(":".toRegex(), "").replace(" ", "_")
    }

    /**
     * Formatea la fecha actual en una cadena con el formato aaaammdd_hhmmss para nombres de archivo (FileName)
     *
     * @param milliseconds timestamp
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatFN(milliseconds: Long): String {
        return formatFN(Date(milliseconds))
    }

    /**
     * Formatea la fecha al idioma español en la forma [day of month] de [month name] de [year] para encabezado de notas (Header of Note)
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatHN(date: Date?): String {
        if (date == null) return ""
        val cal = Cal(date)
        return cal.day.toString() + " de " + getMonthName(cal.month) + " de " + cal.year
    }

    /**
     * Formatea la fecha al idioma español en la forma [day of month] de [month name] de [year] para encabezado de notas (Header of Note)
     *
     * @param milliseconds timestamp
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatHN(milliseconds: Long): String {
        return formatHN(Date(milliseconds))
    }

    /**
     * Traduce el número entero que representa el mes a su nombre en español
     *
     * @param month mes en formato numérico
     * @return string con el nombre del mes
     */
    fun getMonthName(month: Int): String {
        var name = ""
        when (month) {
            1 -> name = "Enero"
            2 -> name = "Febrero"
            3 -> name = "Marzo"
            4 -> name = "Abril"
            5 -> name = "Mayo"
            6 -> name = "Junio"
            7 -> name = "Julio"
            8 -> name = "Agosto"
            9 -> name = "Septiembre"
            10 -> name = "Octubre"
            11 -> name = "Noviembre"
            12 -> name = "Diciembre"
        }
        return name
    }

    /**
     * Formatea la fecha al formato universal aaaa-mm-dd hh:mm:ss
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatU(date: Date?): String {
        if (date == null) return ""
        val cal = Cal(date)
        return cal.year.toString() + "-" + pad(cal.month) + "-" + pad(cal.day) + " " + pad(cal.hour) + ":" + pad(cal.minute) + ":" + pad(cal.second)
    }

    /**
     * Formatea la fecha al formato universal aaaa-mm-dd hh:mm:ss
     *
     * @param milliseconds timestamp
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatU(milliseconds: Long): String {
        return formatU(Date(milliseconds))
    }

    /**
     * Formatea la fecha al formato aaaa-mm-dd hh:mm:ss
     *
     * @param year   año
     * @param month  mes
     * @param day    día
     * @param hour   hora
     * @param minute minutos
     * @param second segundos
     * @return Cadena formateada, devuelve una cadena vacía si los valores de los parámetros no se pueden convertir a una fecha
     */
    fun formatU(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): String {
        return year.toString() + "-" + pad(month) + "-" + pad(day) + " " + pad(hour) + ":" + pad(minute) + ":" + pad(second)
    }

    /**
     * Formatea la fecha al formato aaaa-mm-dd
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatUDate(date: Date?): String {
        return if (date == null) "" else formatU(date).substring(0, 9)
    }

    /**
     * Formatea la fecha al formato aaaa-mm
     * Devuelve el año y mes de una fecha
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatUYearMonth(date: Date?): String {
        return if (date == null) "" else formatU(date).substring(0, 6)
    }

    /**
     * Formatea la fecha al formato nombre del mes - año
     * Devuelve el año y mes de una fecha
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatUYearMonthString(date: Date?): String {
        if (date == null) return ""
        val cal = Cal(date)
        return getMonthName(cal.month) + " - " + cal.year
    }

    /**
     * Formatea la fecha al formato aaaa-mm-dd
     *
     * @param milliseconds timestamp
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */
    fun formatUDate(milliseconds: Long): String {
        return formatUDate(Date(milliseconds))
    }

    /**
     * Formatea el tiempo al formato hh:mm:ss
     *
     * @param time tiempo a formatear
     * @return Cadena formateada, devuelve una cadena vacía si el tiempo es null
     */
    fun formatUTime(time: Time?): String {
        if (time == null) return ""
        val cal = Cal(time)
        return pad(cal.hour) + ":" + pad(cal.minute) + ":" + pad(cal.second)
    }

    /**
     * Devuelve la fecha actual
     *
     * @return Date con la fecha actual
     */
    fun now(): Date {
        return Date()
    }

    /**
     * Devuelve la fecha actual en una cadena con el formato aaaa-mm-dd hh:mm:ss
     *
     * @return string que contiene la fecha
     */
    fun nowU(): String {
        return formatU(now())
    }

    /**
     * Devuelve la fecha actual en una cadena con el formato completo dd/mm/aaaa hh:mm (legible para los humanos)
     *
     * @return string que contiene la fecha
     */
    fun nowHC(): String {
        return formatHC(now())
    }

    /**
     * Formatea la fecha actual al idioma español en la forma [day of month] días del mes de [month name] de [year]
     *
     * @return Cadena formateada
     */
    fun nowHS(): String {
        return formatHS(now())
    }

    /**
     * Devuelve la fecha actual en una cadena con el formato aaaa-mm-dd
     *
     * @return string que contiene la fecha
     */
    fun nowUDate(): String {
        return formatUDate(now())
    }

    /**
     * Devuelve el tiempo actual en una cadena con el formato hh:mm:ss
     *
     * @return string que contiene la fecha
     */
    fun nowUTime(): String {
        return formatUTime(Time(now().time))
    }

    /**
     * Devuelve la fecha actual en una cadena con el formato dd/mm/aaaa (legible para los humanos)
     *
     * @return string que contiene la fecha
     */
    fun nowH(): String {
        return formatH(now())
    }

    /**
     * Devuelve la fecha actual en una cadena con el formato aaaammdd_hhmmss para nombres de archivo (FileName)
     *
     * @return string que contiene la fecha
     */
    fun nowFN(): String {
        return formatFN(now())
    }

    /**
     * Formatea la fecha actual al idioma español en la forma [day of month] de [month name] del [year] para encabezado de notas (Header Notes)
     *
     * @return Cadena formateada
     */
    fun nowHN(): String {
        return formatHN(now())
    }

    /**
     * Convierte la cadena de fecha a un objeto de tipo Date
     *
     * @param date representación de la fecha en cadena en formato universal aaaa-mm-dd hh:mm:ss o formato aaaa-mm-dd
     * @return Date objeto que contiene la fecha
     */
    fun parseDate(date: String?): Date? {
        if (date == null || date == "") return null
        val calendar = Calendar.getInstance()
        if (date.contains(" ")) {
            val d = date.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val t = date.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            calendar.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]), Integer.parseInt(t[0]), Integer.parseInt(t[1]), java.lang.Double.valueOf(t[2]).toInt())
        } else {
            val d = date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            calendar.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]))
        }
        return calendar.time
    }

    /**
     * Convierte la cadena de fecha a un objeto de tipo Date
     *
     * @param date representación de la fecha en cadena en formato humano dd/mm/aaaa hh:mm:ss o formato dd/mm/aaaa
     * @return Date objeto que contiene la fecha
     */
    fun parseDateH(date: String?): Date? {
        if (date == null || date == "") return null
        val calendar = Calendar.getInstance()
        if (date.contains(" ")) {
            val d = date.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val t = date.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            calendar.set(Integer.parseInt(d[2]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[0]), Integer.parseInt(t[0]), Integer.parseInt(t[1]), java.lang.Double.valueOf(t[2]).toInt())
        } else {
            val d = date.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            calendar.set(Integer.parseInt(d[2]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[0]))
        }
        return calendar.time
    }

    /**
     * Convierte la cadena en formato hh:mm:ss a un objeto de tipo Time
     *
     * @param time representación del tiempo en cadena en formato hh:mm:ss
     * @return Time objeto que contiene el tiempo
     */
    fun parseTime(time: String?): Time? {
        if (time == null || time == "") return null
        return if (time.contains(":")) {
            Time.valueOf(time)
        } else {
            null
        }
    }

    /**
     * Devuelve al año en curso
     *
     * @return int
     */
    fun curYear(): Int {
        return Cal().year
    }

    /**
     * Devuelve el mes del año en curso
     *
     * @return int
     */
    fun curMonth(): Int {
        return Cal().month + 1
    }

    /**
     * Devuelve el día del mes del año en curso
     *
     * @return int
     */
    fun curDay(): Int {
        return Cal().day
    }

    /**
     * Devuelve una fecha a la que se la ha sustraido a la fecha indicada, la cantidad indicada, en la parte de la fecha indicada
     *
     * @param date  Fecha a la que se le agregará el tiempo indicado
     * @param part  Parte de la fecha en la que se agregará el valor deseado
     * @param value Cantidad a sumar en la parte indicada
     * @return Date objeto que contiene la fecha
     */
    fun add(date: Date, part: Part, value: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        when (part) {
            FDate.Part.DAY -> calendar.add(Calendar.DAY_OF_YEAR, value)
            FDate.Part.MONTH -> calendar.add(Calendar.MONTH, value)
            FDate.Part.YEAR -> calendar.add(Calendar.YEAR, value)
            FDate.Part.HOUR -> calendar.add(Calendar.HOUR, value)
            FDate.Part.MINUTE -> calendar.add(Calendar.MINUTE, value)
            FDate.Part.SECOND -> calendar.add(Calendar.SECOND, value)
        }
        return calendar.time
    }

    fun diffInDays(since: Date, until: Date): Long {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        start.time = since
        end.time = until
        return (end.time.time - start.time.time) / (1000 * 60 * 60 * 24)
    }

    /**
     * Obtiene el mes y día de la fecha (legible para los humanos)
     *
     * @param date fecha a formatear
     * @return Cadena formateada, devuelve una cadena vacía si la fecha es null
     */

    fun getMonthDay(date: Date?): String {
        if (date == null) return ""
        val calendar = Calendar.getInstance()
        calendar.time = date
        return pad(calendar.get(Calendar.MONTH) + 1) + "-" + pad(calendar.get(Calendar.DAY_OF_MONTH))
    }

    enum class Part {
        YEAR, MONTH, DAY, HOUR, MINUTE, SECOND
    }

}
