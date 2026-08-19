package at.fyayc.backend.emporixapi

import jakarta.servlet.http.HttpSession
import kotlin.reflect.KProperty

class SessionBacked<T>(
    val httpSession: HttpSession,
    val key: String,
) {
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return httpSession.getAttribute(key) as T
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        httpSession.setAttribute(key, value)
    }
}

fun <T> HttpSession.property(key: String) = SessionBacked<T>(this, key)

