package ru.isntrui.sharik.models

import java.util.UUID

data class UserResponse(
    val id: UUID,
    val username: String,
    val firstName: String,
    val lastName: String,
    val avatarURL: String,
)
