package at.fyayc.emporixapi.wrappers.oe.events

@JsExport
sealed external interface OEEvent<T : Any> {
    val id: String
    val body: T
}