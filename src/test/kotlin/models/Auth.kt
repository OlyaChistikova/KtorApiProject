package models

import kotlinx.serialization.Serializable

@Serializable
data class Auth(val username: String, val password: String)
