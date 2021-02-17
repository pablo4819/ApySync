package ar.com.latitud.helper

import java.util.Calendar
import java.util.Date

class Cal {
    private var cal: Calendar? = null

    val year: Int
        get() = cal!!.get(Calendar.YEAR)

    val month: Int
        get() = cal!!.get(Calendar.MONTH) + 1

    val day: Int
        get() = cal!!.get(Calendar.DAY_OF_MONTH)

    val hour: Int
        get() = cal!!.get(Calendar.HOUR)

    val minute: Int
        get() = cal!!.get(Calendar.MINUTE)

    val second: Int
        get() = cal!!.get(Calendar.SECOND)

    constructor(date: Date) {
        cal = Calendar.getInstance()
        cal!!.time = date
    }

    constructor() {
        cal = Calendar.getInstance()
    }

}
