package at.fyayc.emporixapi.category

data class ClassificationMixin(
    val name: String,
    val schemaUrl: String,
    val required: Boolean = false,
    val mixinPath: String?,
    val sourceCategoryId: String?,
)