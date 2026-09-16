package at.fyayc.batteryincludedindexer

sealed interface IndexMode {
    class Transaction(val id: String) : IndexMode
    enum class NonTransaction : IndexMode {
        PARTIAL,
        FULL,
    }
}