package at.fyayc.emporixapi.auth

interface TokenStorage {
    suspend fun retrieve(): String?
}

