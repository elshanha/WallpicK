package com.elshan.wallpick.main.domain.model

data class User(
    val createdAt: Long = System.currentTimeMillis(),
    val email: String?,
    val name: String?,
    val profilePicUrl: String?,
    val uid: String,
)
