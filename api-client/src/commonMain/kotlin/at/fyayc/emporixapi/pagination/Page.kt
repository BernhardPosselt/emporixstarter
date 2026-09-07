package at.fyayc.emporixapi.pagination

data class Page<T>(
    val values: List<T>,
    val totalCount: Int,
)