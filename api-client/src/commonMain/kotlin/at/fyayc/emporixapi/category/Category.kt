package at.fyayc.emporixapi.category

import at.fyayc.emporixapi.i18n.TranslatedValue
import at.fyayc.emporixapi.media.Media
import at.fyayc.emporixapi.util.Metadata
import kotlinx.serialization.json.JsonObject

data class Category(
    val id: String,
    val parentId: String?,
    val localizedName: TranslatedValue<String>?,
    val localizedDescription: TranslatedValue<String>?,
    val localizedSlug: TranslatedValue<String>?,
    val code: String?,
    val validity: Validity?,
    val position: Int,
    val published: Boolean,
    val superCategoryIds: Set<String>,
    val ownClassificationMixins: List<OwnClassificationMixin> = emptyList(),
    val classificationMixins: List<ClassificationMixin> = emptyList(),
    val mixins: JsonObject?,
    val metadata: Metadata,
    val media: List<Media>,
)