package at.fyayc.emporixapi.category

data class OwnClassificationMixin(
    val name: String,
    val schemaUrl: String,
    val required: Boolean = false,
)