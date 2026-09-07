package at.fyayc.emporixapi.category

import kotlin.time.Instant

data class Validity(
    val from: Instant,
    val to: Instant,
)