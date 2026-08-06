package at.fyayc.emporixapi.http

import kotlin.js.JsExport

@JsExport
open class ApiError(override val statusCode: Int, message: String) : StatusCode, RuntimeException(message = message)