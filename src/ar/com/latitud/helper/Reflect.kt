package ar.com.latitud.helper

object Reflect {

    fun hasProperty(clazz: Class<*>, columnName: String): Boolean {
        for (declaredField in clazz.declaredFields) {
            if (declaredField.name === columnName) return true
        }
        return false
    }

}