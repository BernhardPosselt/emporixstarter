package at.fyayc.emporixapi.util

data class SortParam(
    val fieldName: String,
    val order: SortOrder? = null,
    val languageIso: LanguageIso? = null,
) {
    fun serialize(): String {
        val lang = languageIso?.let { ".${languageIso.name.lowercase()}" } ?: ""
        val order = order?.let { ":${order.name.lowercase()}" } ?: ""
        return "$fieldName$lang$order"
    }
}