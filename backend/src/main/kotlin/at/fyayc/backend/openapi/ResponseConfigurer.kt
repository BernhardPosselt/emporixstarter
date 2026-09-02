package at.fyayc.backend.openapi

import org.springframework.http.MediaType
import kotlin.reflect.KClass

@OpenApiDsl
class ResponseConfigurer<T : Any>(
    var clazz: KClass<T>? = null,
    var description: String? = null,
    var produces: String = MediaType.APPLICATION_JSON_VALUE
)

fun <T : Any> response(
    clazz: KClass<T>? = null,
    block: (ResponseConfigurer<T>.() -> Unit)? = null
): ResponseConfigurer<T> {
    val config = ResponseConfigurer(clazz)
    block?.let { config.it() }
    return config
}
