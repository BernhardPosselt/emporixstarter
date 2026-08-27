package at.fyayc.emporixapi.session

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlin.time.Instant

// TODO: figure out if this metadata is globally valid
@Serializable
data class Metadata(
    val version: Int?,
    val createdAt: Instant?,
    val modifiedAt: Instant?,
    val mixins: JsonObject?,
)