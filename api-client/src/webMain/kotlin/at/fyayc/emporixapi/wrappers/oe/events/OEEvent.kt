package at.fyayc.emporixapi.wrappers.oe.events

@JsExport
sealed external interface OEEvent {
    val id: String
    val body: Any
}