package at.fyayc.emporixapi.media

data class MediaCustomAttributesQueryDocument(
    val id: String,
    val height: Int?,
    val width: Int?,
    val sizeKB: Double?,
    val type: String?,
    val name: String?,
)