package at.fyayc.emporixapi.auth

import at.fyayc.emporixapi.http.TokenType

class TokenUnavailable(val type: TokenType) : Exception("Could not locate token $type in storage")