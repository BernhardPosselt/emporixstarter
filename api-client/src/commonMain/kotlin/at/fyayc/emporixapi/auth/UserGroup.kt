package at.fyayc.emporixapi.auth

import at.fyayc.emporixapi.i18n.TranslatedValue
import at.fyayc.emporixapi.session.Metadata
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class UserGroup(
    val id: String,
    val name: TranslatedValue<String>,
    val description: TranslatedValue<String>?,
    val vendorId: String?,
    val accessControls: List<String> = emptyList(),
    val templates: List<String> = emptyList(),
    val code: String?,
    val userType: UserType = UserType.CUSTOMER,
    val b2b: JsonObject?,
    val restrictions: List<String> = emptyList(),
    val mixins: JsonObject?,
    val metadata: Metadata,
)