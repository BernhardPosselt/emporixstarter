package at.fyayc.emporixapi.auth

interface TokenStorage {
    fun retrieveSessionToken(): String
    fun retrieveServiceToken(): String
}