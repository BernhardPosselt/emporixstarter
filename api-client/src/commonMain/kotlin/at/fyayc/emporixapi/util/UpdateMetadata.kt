package at.fyayc.emporixapi.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class UpdateMetadata(
    val version: Int?,
    val mixins: JsonObject?,
)