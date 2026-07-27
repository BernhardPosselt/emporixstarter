package at.fyayc.emporixapi.oe.events

import io.ktor.util.reflect.*
import kotlinx.serialization.Serializable


class OtherEvent(
    override val id: String,
    override val body: Body,
) : OrchestrationEngineEvent<OtherEvent.Body> {
    override val type = "other_event"
    override val typeInfo = typeInfo<Body>()

    @Serializable
    data class Body(
        val value: String,
    )
}

