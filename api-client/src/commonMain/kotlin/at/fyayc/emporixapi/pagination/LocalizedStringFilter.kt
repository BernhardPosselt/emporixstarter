package at.fyayc.emporixapi.pagination

import at.fyayc.emporixapi.i18n.LanguageKey

data class LocalizedStringFilter(
    val value: String,
    val locale: LanguageKey? = null,
) {
    val key = value + locale?.let { ".${it.name}" }
}