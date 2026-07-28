package at.fyayc.emporixapi.wrappers.oe.events

import at.fyayc.emporixapi.oe.events.OrchestrationEngineEvent

interface SerializableEvent<out T : Any> {
    @JsExport.Ignore
    fun toKt(): OrchestrationEngineEvent<T>
}