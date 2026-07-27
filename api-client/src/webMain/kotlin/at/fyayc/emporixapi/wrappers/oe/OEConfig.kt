package at.fyayc.emporixapi.wrappers.oe

import kotlinx.js.JsPlainObject

@JsPlainObject
@JsExport
external interface OEConfig {
    val baseUrl: String
    val secret: String
    val source: String
}