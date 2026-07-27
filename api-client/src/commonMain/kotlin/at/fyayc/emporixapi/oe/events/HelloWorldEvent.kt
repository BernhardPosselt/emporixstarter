package at.fyayc.emporixapi.oe.events

import at.fyayc.emporixapi.oe.OrchestrationEngineEvent
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class HelloWorld(
    val test: String,
)

@JsExport
class HelloWorldEvent(
    id: String,
    payload: HelloWorld,
): OrchestrationEngineEvent<HelloWorld>(
    id = id,
    type = "hello_world",
    payload = payload,
    typeInfo = typeInfo<HelloWorld>()
)

