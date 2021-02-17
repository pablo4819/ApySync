package ar.com.latitud.db

abstract class Database {

    abstract fun open()

    abstract fun close()

    abstract fun execQry(entityClass: Class<out Entity<*, *>>, sql: String, vararg params: Any): List<Entity<*, *>>

    abstract fun execQry(sql: String, vararg params: Any): Cursor

    abstract fun execUpd(sql: String, vararg params: Any): Int

    abstract fun execDelete(sql: String, vararg params: Any): Int

    abstract fun getAll(entityClass: Class<out Entity<*, *>>, table: String): List<Entity<*, *>>

    abstract fun search(entityClass: Class<out Entity<*, *>>, table: String, where: String, vararg whereValues: Any): List<Entity<*, *>>

    abstract fun search(entityClass: Class<out Entity<*, *>>, table: String, columns: Array<String>, where: String, vararg whereValues: Any): List<Entity<*, *>>

    abstract operator fun get(entity: Entity<*, *>, table: String, where: String, vararg whereValues: Any)

    abstract fun insert(table: String, values: Values): Long

    abstract fun update(table: String, values: Values, where: String, vararg whereValues: Any): Int

    abstract fun delete(table: String, where: String, vararg whereValues: Any): Int

    abstract fun exists(table: String, where: String, vararg whereValues: Any): Boolean

    abstract fun filter(entityClass: Class<out Entity<*, *>>, selectFrom: String, filter: Map<String, String>): List<Entity<*, *>>

    abstract fun filter(entityClass: Class<out Entity<*, *>>, selectFrom: String, filter: Map<String, String>, orderBy: String, ascendingOrder: Boolean): List<Entity<*, *>>

    abstract fun filter(entityClass: Class<out Entity<*, *>>, selectFrom: String, filter: Map<String, String>, orderBy: String, ascendingOrder: Boolean, lStart: Int, lEnd: Int): List<Entity<*, *>>

    abstract fun filterCount(from: String, filter: Map<String, String>): Int

    abstract fun newValues(): Values

    companion object {

        fun buildWhere(filter: Map<String, String>): String {
            val builder = StringBuilder()
            for (columnName in filter.keys) {
                if (columnName.contains(".")) {
                    when {
                        columnName.contains(">=") -> builder.append(" AND ").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]).append(".`").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace(">=", "")).append("`>=?")
                        columnName.contains(">") -> builder.append(" AND ").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]).append(".`").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace(">", "")).append("`>?")
                        columnName.contains("<=") -> builder.append(" AND ").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]).append(".`").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace("<=", "")).append("`<=?")
                        columnName.contains("<") -> builder.append(" AND ").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]).append(".`").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace("<", "")).append("`<?")
                        else -> builder.append(" AND ").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]).append(".`").append(columnName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]).append("`=?")
                    }
                } else {
                    when {
                        columnName.contains(">=") -> builder.append(" AND `").append(columnName.replace(">=", "")).append("`>=?")
                        columnName.contains(">") -> builder.append(" AND `").append(columnName.replace(">", "")).append("`>?")
                        columnName.contains("<=") -> builder.append(" AND `").append(columnName.replace("<=", "")).append("`<=?")
                        columnName.contains("<") -> builder.append(" AND `").append(columnName.replace("<", "")).append("`<?")
                        else -> builder.append(" AND `").append(columnName).append("`=?")
                    }
                }
            }
            return if (builder.isNotEmpty()) " WHERE " + builder.substring(5) else ""
        }

    }
}
