package at.fyayc.emporixapi.products

import kotlinx.js.JsPlainObject

@JsPlainObject
@JsExport
external interface ApiConfiguration {
    val endpoint: String
    val tenant: String
}