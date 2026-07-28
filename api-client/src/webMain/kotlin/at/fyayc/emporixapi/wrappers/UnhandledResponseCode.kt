package at.fyayc.emporixapi.wrappers

@JsExport
class UnhandledResponseCode(code: Int) : RuntimeException("No response type configured for HTTP status code $code")