package ar.com.latitud.db

import ar.com.latitud.sync.model.LogSync
import ar.com.latitud.sync.model.Visita
import ar.com.latitud.helper.Id
import com.google.gson.GsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.ParameterizedType
import java.util.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class Entity<T, C : Entity<T, C>> {

    protected abstract val ID: String

    protected abstract var id: T?

    protected abstract val idColumnName: String

    protected abstract val table: String

    protected abstract val cv: Values

    abstract fun map(c: Cursor)

    fun get(id: T): T? {
        Db.get().get(this, table, "$ID=?", id!!)
        return this.id
    }

    /**
     * Override this method if the inherited class must generate an id at insertion moment.
     * Otherwise this method will return null by default for the Integer and Long types.
     * In case id property is a String type, by default it create a random id using method [ar.com.apidb.helper.Id.gen].
     *
     * @return a value with the type given by T
     */
    fun genId(): T? {
        @Suppress("UNCHECKED_CAST")
        val idClassType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
        @Suppress("UNCHECKED_CAST")
        return when (idClassType) {
            String::class.java -> Id.gen() as T
            Long::class.java -> Id.generateBigIntPrimaryKey() as T
            else -> null
        }
    }

    fun insert(id: T?): T {
        if (id == null) throw Exception("Error inserting class " + javaClass.name + ": Id can't be null")
        if (id is String || id is Int || id is Long) {
            val values = cv
            this.id = id
            values.put(ID, this.id)
            Db.get().insert(table, values)
        } else {
            @Suppress("UNCHECKED_CAST")
            throw Exception(javaClass.simpleName + ": Class type of the property id (" + ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>).simpleName + ") is not contemplated.")
        }
        return this.id!!
    }

    /**
     * Create a record on database on corresponding table to entity.
     * For insertion, value of id is evaluated and if it's not null it create a record normally with that value.
     * Otherwise, it evaluate if method genId() is overwritten for get an appropriate value for id, on negative case an exception is thrown for id type String,
     * for the Integer or Long types, it try create the record for id column with active autoincrement option, otherwise an exception is thrown.
     *
     * @return identifier of entity
     * @throws Exception an exception is thrown when id is not of the String, Integer or Long type.
     * When id is String type, an exception is thrown if its value is null and inherited class doesn't override the method genId().
     * When id is Integer or Long, an exception is thrown if its value is null and id column of table doesn't have autoincrement option activated.
     */
    fun insert(): T {
        @Suppress("UNCHECKED_CAST")
        val idClassType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
        val generatedId = genId()
        val values = cv
        when {
            id != null -> {
                values.put(ID, id)
                Db.get().insert(table, values)
                return id!!
            }
            generatedId == null -> return when (idClassType) {
                String::class.java -> throw Exception("Id can't be null. Must assign a value to id previously or must override method genId() to assign appropriate value to id automatically.")
                Int::class.java -> {
                    @Suppress("UNCHECKED_CAST")
                    id = Db.get().insert(table, values) as T
                    id!!
                }
                Integer::class.java -> {
                    @Suppress("UNCHECKED_CAST")
                    id = Db.get().insert(table, values).toInt() as T
                    id!!
                }
                Long::class.java -> {
                    @Suppress("UNCHECKED_CAST")
                    id = Db.get().insert(table, values) as T
                    id!!
                }
                else -> throw Exception(javaClass.simpleName + ": Class type of the property id (" + idClassType.simpleName + ") is not contemplated.")
            }
            else -> {
                id = generatedId
                values.put(ID, id)
                Db.get().insert(table, values)
                return id!!
            }
        }
    }

    fun update(id: T? = this.id): Int {
        if (id == null) throw Exception("Error updating class " + javaClass.name + ": Id can't be null")
        return Db.get().update(table, cv, "$ID=?", id)
    }

    fun updateVisita(codigoArt: String?): Int {
        if (codigoArt == null) throw Exception("Error updating class : Id can't be null")
        return Db.get().update(Visita.TABLE, cv, "${Visita.VISITA_CODIGO_ART}=?", codigoArt)
    }

    fun delete(id: T? = this.id): Int {
        if (id == null) throw Exception("Error deleting class " + javaClass.name + ": Id can't be null")
        return Db.get().delete(table, "$ID=?", id)
    }

    fun exists(id: T? = this.id): Boolean {
        if (id == null) throw Exception("Error searching existence of class " + javaClass.name + ": Id can't be null")
        return Db.get().exists(table, "$ID=?", id)
    }

    @Suppress("UNCHECKED_CAST")
    val all: List<C>
        get() = Db.get().getAll(javaClass, table) as List<C>

    fun getAll(orderBy: String, ascendingOrder: Boolean): List<C> {
        @Suppress("UNCHECKED_CAST")
        return Db.get().execQry(javaClass, "SELECT * FROM " + writeTable() + " ORDER BY " + orderBy + " " + if (ascendingOrder) "ASC" else "DESC") as List<C>
    }

    fun getAll(ascendingOrder: Boolean, vararg orderByColumns: String): List<C> {
        val builder = StringBuilder()
        for (columnName in orderByColumns) builder.append(",`").append(columnName).append("`")
        val orderBy = if (builder.isNotEmpty()) builder.substring(1) else ""
        @Suppress("UNCHECKED_CAST")
        return Db.get().execQry(javaClass, "SELECT * FROM " + writeTable() + " ORDER BY " + orderBy + " " + if (ascendingOrder) "ASC" else "DESC") as List<C>
    }

    fun getAll(orderBy: String, ascendingOrder: Boolean, lStart: Int, lEnd: Int): List<C> {
        @Suppress("UNCHECKED_CAST")
        return Db.get().execQry(javaClass, "SELECT * FROM " + writeTable() + " ORDER BY " + orderBy + " " + (if (ascendingOrder) "ASC" else "DESC") + " LIMIT " + lStart + "," + lEnd) as List<C>
    }

    /**
     * Do search using operator like for each column with value given by columnsToLookFor
     *
     * @param patternToSearch  string to search
     * @param columnsToLookFor columns to look for the given pattern
     * @return objects list
     * @throws Exception thrown if there is an error on sql executing
     */
    fun like(patternToSearch: String, vararg columnsToLookFor: String): List<C> {
        val builder = StringBuilder()
        val values = ArrayList<String>()
        for (columnName in columnsToLookFor) {
            builder.append(" OR `").append(columnName).append("` LIKE ?")
            values.add("%$patternToSearch%")
        }
        val where = if (builder.isNotEmpty()) builder.substring(4) else ""
        @Suppress("UNCHECKED_CAST")
        return Db.get().search(javaClass, table, where, values.toTypedArray()) as List<C>
    }

    /**
     * Do search using operator like for each column with value given by columnsToLookFor
     *
     * @param orderBy          column under which the order will be defined
     * @param ascendingOrder   true: ascending order - false: descending order
     * @param patternToSearch  string to search
     * @param columnsToLookFor columns to look for the given pattern
     * @return objects list
     * @throws Exception thrown if there is an error on sql executing
     */
    fun like(orderBy: String, ascendingOrder: Boolean, patternToSearch: String, vararg columnsToLookFor: String): List<C> {
        val builder = StringBuilder()
        val values = ArrayList<String>()
        for (columnName in columnsToLookFor) {
            builder.append(" OR `").append(columnName).append("` LIKE ?")
            values.add("%$patternToSearch%")
        }
        val where = if (builder.isNotEmpty()) builder.substring(4) else ""
        @Suppress("UNCHECKED_CAST")
        return Db.get().search(javaClass, table, where + " ORDER BY " + orderBy + " " + if (ascendingOrder) "ASC" else "DESC", values.toTypedArray()) as List<C>
    }

    fun likeOr(filter: Map<String, String>, orderBy: String, ascendingOrder: Boolean): List<C> {
        return like(filter, "OR", orderBy, ascendingOrder)
    }

    fun likeAnd(filter: Map<String, String>, orderBy: String, ascendingOrder: Boolean): List<C> {
        return like(filter, "AND", orderBy, ascendingOrder)
    }

    fun like(filter: Map<String, String>, operator: String, orderBy: String, ascendingOrder: Boolean): List<C> {
        val builder = StringBuilder()
        for (columnName in filter.keys) builder.append(" ").append(operator).append(" `").append(columnName).append("` LIKE ?")
        val params = ArrayList<String>()
        for (value in filter.values) params.add("%$value%")
        val where = if (builder.isNotEmpty()) " WHERE " + builder.substring(4) else ""
        @Suppress("UNCHECKED_CAST")
        return Db.get().execQry(javaClass, "SELECT * FROM " + writeTable() + where + " ORDER BY " + orderBy + " " + if (ascendingOrder) "ASC" else "DESC", params.toTypedArray()) as List<C>
    }

    fun getFilteredBy(filter: Map<String, String>, orderBy: String, ascendingOrder: Boolean): List<C> {
        @Suppress("UNCHECKED_CAST")
        return Db.get().filter(javaClass, "SELECT * FROM $table", filter, orderBy, ascendingOrder) as List<C>
    }

    fun getFilteredBy(filter: Map<String, String>, orderBy: String, ascendingOrder: Boolean, lStart: Int, lEnd: Int): List<C> {
        @Suppress("UNCHECKED_CAST")
        return Db.get().filter(javaClass, "SELECT * FROM $table", filter, orderBy, ascendingOrder, lStart, lEnd) as List<C>
    }

    fun toJson(): String {
        return GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm").create().toJson(this)
    }

    fun fromJson(json: String): C? {
        return try {
            GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm").create().fromJson<C>(json, javaClass)
        } catch (e: Exception) {
            LOG.error(javaClass.simpleName, e)
            null
        }
    }

    private fun writeTable(): String {
        return if (table.contains(".")) "`" + table.split(".")[0] + "`.`" + table.split(".")[1] + "`" else "`$table`"
    }

    fun updateLog(codigo: Long?): Int {
        if (codigo == null) throw Exception("Error updating class : Id can't be null")
        return Db.get().update(LogSync.TABLE, cv, "${LogSync.CODIGO}=?", codigo)
    }

    @Suppress("DEPRECATION")
    companion object {
        @JvmStatic
        val LOG: Logger = LoggerFactory.getLogger(Entity::class.java)

        //const val ID = "visita_id"

        fun newInst(clazz: String): Entity<*, *> {
            return Class.forName(clazz).newInstance() as Entity<*, *>
        }
    }

}