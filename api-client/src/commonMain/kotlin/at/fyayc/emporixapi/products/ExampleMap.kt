package at.fyayc.emporixapi.products

import kotlin.js.JsExport

@JsExport
fun returnAMap(): Map<String, String> {
    logThisCall()
    return mapOf("test" to "test")
}

expect fun logThisCall()