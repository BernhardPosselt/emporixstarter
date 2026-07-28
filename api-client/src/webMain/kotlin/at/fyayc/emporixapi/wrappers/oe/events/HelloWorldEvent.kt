package at.fyayc.emporixapi.wrappers.oe.events

import at.fyayc.emporixapi.oe.events.HelloWorldEvent
import kotlinx.js.JsPlainObject

@JsPlainObject
@JsExport
external interface HelloWorld {
    val test: String
}

@JsExport
class HelloWorldEvent(
    override val id: String,
    override val body: HelloWorld,
) : OEEvent<HelloWorld>, SerializableEvent<HelloWorldEvent.Body> {
    @JsExport.Ignore
    override fun toKt() = HelloWorldEvent(
        id = id,
        body = HelloWorldEvent.Body(
            test = body.test,
        )
    )
}