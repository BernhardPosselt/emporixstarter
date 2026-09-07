package at.fyayc.emporixapi.i18n

data class AcceptLanguage(
    val string: String,
) {
    companion object {
        fun all() = AcceptLanguage("*")
    }
}