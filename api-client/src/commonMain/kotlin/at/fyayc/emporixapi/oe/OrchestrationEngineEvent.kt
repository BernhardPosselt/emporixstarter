package at.fyayc.emporixapi.oe

import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class OrchestrationEngineEvent<out T : Any> @PublishedApi internal constructor(
    val id: String,
    val type: String,
    val payload: T,
    val typeInfo: TypeInfo,
) {
    companion object {
        inline fun <reified T : Any> create(
            id: String,
            type: String,
            payload: T,
        ) = OrchestrationEngineEvent(
            id = id,
            type = type,
            payload = payload,
            typeInfo = typeInfo<T>()
        )
    }
}