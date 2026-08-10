package at.fyayc.emporixapi.auth

interface DistributedLock {
    suspend fun <T> locking(key: String, operation: suspend () -> T): T
}