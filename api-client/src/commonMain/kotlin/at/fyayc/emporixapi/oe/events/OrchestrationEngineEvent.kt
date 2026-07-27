package at.fyayc.emporixapi.oe.events

import io.ktor.util.reflect.*

sealed interface OrchestrationEngineEvent<out T : Any> {
    val id: String
    val type: String
    val body: T
    val typeInfo: TypeInfo
}