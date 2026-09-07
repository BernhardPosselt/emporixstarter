package at.fyayc.emporixapi.media

import kotlin.time.Instant

data class MediaMetadataQueryDocument(
    val createdAt: Instant?,
    val modifiedAt: Instant?,
)