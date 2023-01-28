import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties

fun Any.setPropertyValue(propName: String, value: Any) {
    for (prop in this::class.declaredMemberProperties) {
        if (prop.name == propName) {
            (prop as? KMutableProperty<*>)?.setter?.call(this, value)
        }
    }
}

fun Any.getPropertyValue(propName: String): Any? {
    for (prop in this::class.declaredMemberProperties) {
        if (prop.name == propName) return prop.getter.call(this)
    }
    return null
}
