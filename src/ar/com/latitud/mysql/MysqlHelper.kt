package ar.com.latitud.mysql

import ar.com.latitud.db.*
import ar.com.latitud.helper.FDate
import ar.com.latitud.helper.FString
import ar.com.latitud.db.Database
import java.sql.*
import java.util.*
import java.util.Date

class MysqlHelper constructor(confFilePath: String) : Database() {

    private var conn: Connection? = null

    private val isClosed: Boolean
        get() = conn == null || conn!!.isClosed

    init {
        Params.getParams(confFilePath)
        MysqlHelper.host = Params.host
        MysqlHelper.port = Params.port
        MysqlHelper.user = Params.user
        MysqlHelper.pass = Params.pass
        MysqlHelper.database = Params.base
    }

    override fun close() {
        if (!isClosed) conn!!.close()
    }

    override fun open() {
        if (isClosed) conn = DriverManager.getConnection("jdbc:mysql://$host:$port/$database?zeroDateTimeBehavior=convertToNull", user, pass)
    }

    /**
     * Execute sql queries that return a records set
     *
     * @param entityClass class to map
     * @param sql         query
     * @param params      values of where clause
     * @return list of com.abinar.account.entities
     * @throws Exception throw exception on error case
     */
    override fun execQry(entityClass: Class<out Entity<*, *>>, sql: String, vararg params: Any): List<Entity<*, *>> {
        open()
        val ps = conn!!.prepareStatement(sql)
        for (i in params.indices) addParamToPs(ps, i + 1, params[i])
        val c = MySqlCursor(ps.executeQuery())
        val entities = ArrayList<Entity<*, *>>()
        val constructor = entityClass.getDeclaredConstructor(Cursor::class.java)
        while (c.next()) entities.add(constructor.newInstance(c))
        c.close()
        ps.close()
        return entities
    }

    override fun execQry(sql: String, vararg params: Any): Cursor {
        open()
        val ps = conn!!.prepareStatement(sql)
        for (i in params.indices) addParamToPs(ps, i + 1, params[i])
        return MySqlCursor(ps.executeQuery())
    }

    /**
     * Execute sql queries that modify records
     *
     * @param sql    query
     * @param params values of where clause
     * @return amount of modified records
     * @throws Exception throw exception on error case
     */
    override fun execUpd(sql: String, vararg params: Any): Int {
        open()
        val ps = conn!!.prepareStatement(sql)
        for (i in params.indices) addParamToPs(ps, i + 1, params[i])
        val result = ps.executeUpdate()
        ps.close()
        return result
    }

    override fun getAll(entityClass: Class<out Entity<*, *>>, table: String): List<Entity<*, *>> {
        open()
        val ps: PreparedStatement = if (table.contains("."))
            conn!!.prepareStatement(String.format("SELECT * FROM `%s`.`%s`;", table.split(".")[0], table.split(".")[1]))
        else
            conn!!.prepareStatement(String.format("SELECT * FROM `%s`;", table))
        val c = MySqlCursor(ps.executeQuery())
        val entities = ArrayList<Entity<*, *>>()
        val constructor = entityClass.getDeclaredConstructor(Cursor::class.java)
        while (c.next()) entities.add(constructor.newInstance(c))
        c.close()
        ps.close()
        return entities
    }

    override fun search(entityClass: Class<out Entity<*, *>>, table: String, where: String, vararg whereValues: Any): List<Entity<*, *>> {
        open()
        val ps: PreparedStatement = if (table.contains("."))
            conn!!.prepareStatement(String.format("SELECT * FROM `%s`.`%s` WHERE %s;", table.split(".")[0], table.split(".")[1], where))
        else
            conn!!.prepareStatement(String.format("SELECT * FROM `%s` WHERE %s;", table, where))
        for (i in whereValues.indices) addParamToPs(ps, i + 1, whereValues[i])
        val c = MySqlCursor(ps.executeQuery())
        val entities = ArrayList<Entity<*, *>>()
        val constructor = entityClass.getDeclaredConstructor(Cursor::class.java)
        while (c.next()) entities.add(constructor.newInstance(c))
        c.close()
        ps.close()
        return entities
    }

    override fun search(entityClass: Class<out Entity<*, *>>, table: String, columns: Array<String>, where: String, vararg whereValues: Any): List<Entity<*, *>> {
        open()
        val ps = conn!!.prepareStatement(String.format("SELECT %s FROM `%s` WHERE %s;", FString.join(columns, ","), table, where))
        for (i in whereValues.indices) addParamToPs(ps, i + 1, whereValues[i])
        val c = MySqlCursor(ps.executeQuery())
        val entities = ArrayList<Entity<*, *>>()
        val constructor = entityClass.getDeclaredConstructor(Cursor::class.java)
        while (c.next()) entities.add(constructor.newInstance(c))
        c.close()
        ps.close()
        return entities
    }

    override fun get(entity: Entity<*, *>, table: String, where: String, vararg whereValues: Any) {
        open()
        val ps: PreparedStatement = if (table.contains("."))
            conn!!.prepareStatement(String.format("SELECT * FROM `%s`.`%s` WHERE %s;", table.split(".")[0], table.split(".")[1], where))
        else
            conn!!.prepareStatement(String.format("SELECT * FROM `%s` WHERE %s;", table, where))
        for (i in whereValues.indices) addParamToPs(ps, i + 1, whereValues[i])
        val c = MySqlCursor(ps.executeQuery())
        if (c.next()) entity.map(c)
        c.close()
        ps.close()
    }

    override fun insert(table: String, values: Values): Long {
        open()
        var id: Long = -1
        val stmt = conn!!.createStatement()
        if (table.contains("."))
            stmt.executeUpdate(String.format("INSERT INTO `%s`.`%s` %s;", table.split(".")[0], table.split(".")[1], values.valuesInsert()), Statement.RETURN_GENERATED_KEYS)
        else
            stmt.executeUpdate(String.format("INSERT INTO `%s` %s;", table, values.valuesInsert()), Statement.RETURN_GENERATED_KEYS)
        val generatedKeys = stmt.generatedKeys
        if (generatedKeys.next()) id = generatedKeys.getLong(1)
        generatedKeys.close()
        stmt.close()
        return id
    }

    override fun update(table: String, values: Values, where: String, vararg whereValues: Any): Int {
        open()
        val ps = if (table.contains("."))
            conn!!.prepareStatement(String.format("UPDATE `%s`.`%s` SET %s WHERE %s;", table.split(".")[0], table.split(".")[1], values.valuesUpdate(), where))
        else
            conn!!.prepareStatement(String.format("UPDATE `%s` SET %s WHERE %s;", table, values.valuesUpdate(), where))
        for (i in whereValues.indices) addParamToPs(ps, i + 1, whereValues[i])
        val result = ps.executeUpdate()
        ps.close()
        return result
    }

    override fun execDelete(sql: String, vararg params: Any): Int {
        open()
        val ps = conn!!.prepareStatement(sql)
        for (i in params.indices) addParamToPs(ps, i + 1, params[i])
        val result = ps.executeUpdate()
        ps.close()
        return result
    }

    override fun delete(table: String, where: String, vararg whereValues: Any): Int {
        open()
        val ps = if (table.contains("."))
            conn!!.prepareStatement(String.format("DELETE FROM `%s`.`%s` WHERE %s;", table.split(".")[0], table.split(".")[1], where))
        else
            conn!!.prepareStatement(String.format("DELETE FROM `%s` WHERE %s;", table, where))
        for (i in whereValues.indices) addParamToPs(ps, i + 1, whereValues[i])
        val result = ps.executeUpdate()
        ps.close()
        return result
    }

    override fun exists(table: String, where: String, vararg whereValues: Any): Boolean {
        open()
        val ps = if (table.contains("."))
            conn!!.prepareStatement(String.format("SELECT * FROM `%s`.`%s` WHERE %s;", table.split(".")[0], table.split(".")[1], where))
        else
            conn!!.prepareStatement(String.format("SELECT * FROM `%s` WHERE %s;", table, where))
        for (i in whereValues.indices) addParamToPs(ps, i + 1, whereValues[i])
        val rs = ps.executeQuery()
        val result = rs.next()
        rs.close()
        ps.close()
        return result
    }

    override fun filter(entityClass: Class<out Entity<*, *>>, selectFrom: String, filter: Map<String, String>): List<Entity<*, *>> {
        return execQry(entityClass, selectFrom + " " + Database.buildWhere(filter), *filter.values.toTypedArray())
    }

    override fun filter(entityClass: Class<out Entity<*, *>>, selectFrom: String, filter: Map<String, String>, orderBy: String, ascendingOrder: Boolean): List<Entity<*, *>> {
        return execQry(entityClass, selectFrom + " " + Database.buildWhere(filter) + " ORDER BY " + orderBy + " " + if (ascendingOrder) "ASC" else "DESC", *filter.values.toTypedArray())
    }

    override fun filter(entityClass: Class<out Entity<*, *>>, selectFrom: String, filter: Map<String, String>, orderBy: String, ascendingOrder: Boolean, lStart: Int, lEnd: Int): List<Entity<*, *>> {
        return execQry(entityClass, selectFrom + " " + Database.buildWhere(filter) + " ORDER BY " + orderBy + " " + (if (ascendingOrder) "ASC" else "DESC") + " LIMIT " + lStart + "," + lEnd, *filter.values.toTypedArray())
    }

    override fun filterCount(from: String, filter: Map<String, String>): Int {
        var result = 0
        val cursor = Db.get().execQry("SELECT count(*) count FROM " + from + " " + Database.buildWhere(filter), *filter.values.toTypedArray())
        if (cursor.next()) result = cursor.getInt("count")!!
        cursor.close()
        return result
    }

    override fun newValues(): Values {
        return ContentValues()
    }

    @Throws(SQLException::class)
    private fun addParamToPs(ps: PreparedStatement, paramIndex: Int, paramValue: Any) {
        when (paramValue) {
            is String -> ps.setString(paramIndex, paramValue)
            is Int -> ps.setInt(paramIndex, paramValue)
            is Double -> ps.setDouble(paramIndex, paramValue)
            is Long -> ps.setLong(paramIndex, paramValue)
            is Float -> ps.setFloat(paramIndex, paramValue)
            is Boolean -> ps.setBoolean(paramIndex, paramValue)
            is Date -> ps.setString(paramIndex, FDate.formatU(paramValue))
            else -> throw SQLException(javaClass.simpleName + ": Class type of the property id (" + paramValue.javaClass.name + ") is not contemplated.")
        }
    }

    companion object {

        private var host: String? = null
        private var port: Int = 0
        private var user: String? = null
        private var pass: String? = null
        private var database: String? = null
    }

}