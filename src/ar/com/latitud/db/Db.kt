package ar.com.latitud.db

import ar.com.latitud.db.Database
import ar.com.latitud.db.Values

object Db {

    private var db: Database? = null

    fun setPlatform(db: Database) {
        Db.db = db
        db.open()
    }

    fun get(): Database {
        return db!!
    }

    fun newCV(): Values {
        return db!!.newValues()
    }

}