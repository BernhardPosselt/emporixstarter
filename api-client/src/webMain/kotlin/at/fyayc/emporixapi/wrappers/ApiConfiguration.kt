package at.fyayc.emporixapi.wrappers

import kotlinx.js.JsPlainObject

@JsPlainObject
@JsExport
external interface ApiConfiguration {
    val baseUrl: String
    val tenant: String
}