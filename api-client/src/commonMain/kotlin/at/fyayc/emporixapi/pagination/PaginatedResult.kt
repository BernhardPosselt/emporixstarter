package at.fyayc.emporixapi.pagination

import kotlinx.coroutines.flow.Flow

data class PaginatedResult<T>(
    val values: Flow<T>,
    val totalCount: Int,
)