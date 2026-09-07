package at.fyayc.emporixapi.media

import kotlinx.serialization.json.JsonObject

data class Media(
    val id: String,
    val url: String?,
    val metadata: MediaMetadataQueryDocument,
    val customAttributes: MediaCustomAttributesQueryDocument,
    val mixins: JsonObject?,
)