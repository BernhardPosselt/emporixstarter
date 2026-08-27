package at.fyayc.emporixapi.i18n

import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.serializer

/**
 * Needed to turn empty strings into nulls
 */
object LanguageAndCountryIsoSerializer :
    JsonTransformingSerializer<LanguageAndCountryIso?>(serializer<LanguageAndCountryIso>().nullable) {
    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonPrimitive && element.content.isBlank()) {
            JsonNull
        } else {
            element
        }
}