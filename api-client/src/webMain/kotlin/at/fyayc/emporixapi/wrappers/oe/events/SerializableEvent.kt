package at.fyayc.emporixapi.wrappers.oe.events

interface SerializableEvent<T> {
    fun toKt(): T
}