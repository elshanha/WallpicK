package com.elshan.wallpick.main.data.mappers

import com.elshan.wallpick.main.domain.model.User
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toUser(): User? {
    return try {
        val createdAt = getTimestamp("createdAt") ?: return null
        val email = getString("email") ?: return null
        val name = getString("name") ?: return null
        val profilePicUrl = getString("profilePicUrl") ?: return null
        val uid = getString("uid") ?: return null

        User(createdAt.toDate().time, email, name, profilePicUrl, uid)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}