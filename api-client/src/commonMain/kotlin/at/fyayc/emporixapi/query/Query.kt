package at.fyayc.emporixapi.query

// TODO: this needs a nice DSL to construct filters
data class Query(private val expression: String) {
    override fun toString(): String {
        return expression
    }
}