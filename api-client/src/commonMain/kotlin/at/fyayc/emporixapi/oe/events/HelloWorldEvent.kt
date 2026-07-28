package at.fyayc.emporixapi.oe.events

import io.ktor.util.reflect.*
import kotlinx.serialization.Serializable

class HelloWorldEvent(
    override val id: String,
    override val body: Body,
) : OrchestrationEngineEvent<HelloWorldEvent.Body> {
    override val type = "hello_world"
    override val typeInfo = typeInfo<Body>()

    @Serializable
    data class Body(
        val test: String,
    )
}

