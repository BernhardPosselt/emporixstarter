package at.fyayc.emporixapi.catalog

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class VisibilityInformation(
    val visible: Boolean,
    val from: LocalDate,
    val to: LocalDate,
)