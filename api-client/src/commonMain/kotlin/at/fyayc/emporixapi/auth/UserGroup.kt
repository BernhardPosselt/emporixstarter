package at.fyayc.emporixapi.auth

import at.fyayc.emporixapi.session.Metadata
import at.fyayc.emporixapi.util.TranslatedString
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class UserGroup(
    val id: String,
    val name: TranslatedString,
    val description: TranslatedString?,
    val vendorId: String?,
    val accessControls: List<String> = emptyList(),
    val templates: List<String> = emptyList(),
    val code: String?,
    val userType: UserType = UserType.CUSTOMER,
    val b2b: JsonObject?,
    val restrictions: List<String>,
    val mixins: JsonObject,
    val metadata: Metadata,
)