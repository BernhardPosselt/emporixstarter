package at.fyayc.emporixapi.i18n

import io.ktor.http.*

data class AcceptLanguage(
    val string: String,
) {
    companion object {
        fun all() = AcceptLanguage("*")
    }
}

fun HeadersBuilder.acceptLanguage(language: AcceptLanguage) {
    append("Accept-Language", language.string)
}