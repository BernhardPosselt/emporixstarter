package at.fyayc.emporixapi.wrappers.oe.events

import kotlinx.js.JsPlainObject

@JsPlainObject
@JsExport
external interface OtherEvent {
    val test: String
}

@JsExport
class OtherEventEvent(
    override val id: String,
    override val body: OtherEvent,
) : OEEvent, SerializableEvent<at.fyayc.emporixapi.oe.events.OtherEvent> {
    @JsExport.Ignore
    override fun toKt() = at.fyayc.emporixapi.oe.events.OtherEvent(
        id = id,
        body = at.fyayc.emporixapi.oe.events.OtherEvent.Body(
            value = body.test,
        )
    )
}