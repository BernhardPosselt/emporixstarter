package at.fyayc.emporixapi.session

import kotlinx.serialization.Serializable
import kotlin.time.Instant

// TODO: figure out if this metadata is globally valid
@Serializable
data class Metadata(
    val version: String?,
    val createdAt: Instant?,
    val modifiedAt: Instant?,
)