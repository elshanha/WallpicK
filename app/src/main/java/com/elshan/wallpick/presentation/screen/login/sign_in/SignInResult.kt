package com.elshan.wallpick.presentation.screen.login.sign_in

import com.google.firebase.auth.FirebaseUser

data class SignInResult(
    val data: AuthenticatedUser?,
    val errorMessage: String?,
    val storedUser: (() -> Unit)? = null
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?,
    val email: String,
    val phoneNumber: String?,
)


data class AuthenticatedUser(
    val userData: UserData?,
    val firebaseUser: FirebaseUser?
)
