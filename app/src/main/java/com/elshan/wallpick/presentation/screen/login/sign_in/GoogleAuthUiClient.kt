package com.elshan.wallpick.presentation.screen.login.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.hilt.navigation.compose.hiltViewModel
import com.elshan.wallpick.R
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.viewmodel.MainViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
) {

    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        return try {
            val result = oneTapClient.beginSignIn(buildSignInRequest()).await()
            result?.pendingIntent?.intentSender
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken
            val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
            val user = auth.signInWithCredential(googleCredentials).await().user

            SignInResult(
                data = user?.let {
                    AuthenticatedUser(
                        userData = UserData(
                            userId = it.uid,
                            username = it.displayName,
                            profilePictureUrl = it.photoUrl?.toString(),
                            email = it.email ?: "",
                            phoneNumber = it.phoneNumber ?: "000-000-0000"
                        ),
                        firebaseUser = it
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is FirebaseAuthException -> {
                    println(e.message)
                    // Handle specific Firebase Auth exceptions
                }
                is ApiException -> {
                    println(e.message)
                    // Handle specific API exceptions
                }
                else -> {
                    println(e.message)
                    // Handle generic exceptions
                }
            }
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getSignedInUser(): AuthenticatedUser? {
        val currentUser = auth.currentUser
        return currentUser?.let {
            AuthenticatedUser(
                userData = UserData(
                    userId = it.uid,
                    username = it.displayName,
                    profilePictureUrl = it.photoUrl?.toString(),
                    email = it.email ?: "",
                    phoneNumber = it.phoneNumber ?: "000-000-0000"
                ),
                firebaseUser = it
            )
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}

