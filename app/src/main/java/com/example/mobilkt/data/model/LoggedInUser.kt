package com.example.mobilkt.data.model

import com.example.mobilkt.data.GroupEnum

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    var userId: String? = null,
    var displayName: String? = "",
    var group : String? = "USER",
    var email: String? = null,
    var hp: String? = null,
    var password: String? = null,
)