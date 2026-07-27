package at.fyayc.emporixapi.oe

import io.ktor.util.reflect.*

abstract class OrchestrationEngineEvent<out T : Any>(
    val id: String,
    val type: String,
    val payload: T,
    val typeInfo: TypeInfo,
)