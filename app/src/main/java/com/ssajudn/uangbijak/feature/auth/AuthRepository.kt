package com.ssajudn.uangbijak.feature.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()

            if (result.user != null) {
                Result.success(result.user!!)
            } else {
                Result.failure(Exception("Something went wrong"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, pass: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val user = result.user

            if (user != null) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                user.updateProfile(profileUpdates).await()
                Result.success(user)
            } else {
                Result.failure(Exception("Gagal membuat user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }
}