package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class User(
    val fullName: String,
    val mobileNumber: String,
    val password: String
)

class AuthManager(private val context: Context) {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_user_data",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    suspend fun registerUser(fullName: String, mobileNumber: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                // Validate input
                if (fullName.isBlank() || mobileNumber.isBlank() || password.isBlank()) {
                    return@withContext AuthResult.Error("All fields are required")
                }
                
                if (mobileNumber.length != 10 || !mobileNumber.all { it.isDigit() }) {
                    return@withContext AuthResult.Error("Please enter a valid 10-digit mobile number")
                }
                
                if (password.length < 8) {
                    return@withContext AuthResult.Error("Password must be at least 8 characters long")
                }
                
                // Check if user already exists
                val existingUser = getUserByMobile(mobileNumber)
                if (existingUser != null) {
                    return@withContext AuthResult.Error("User with this mobile number already exists")
                }
                
                // Save user data
                val user = User(fullName, mobileNumber, password)
                saveUser(user)
                
                AuthResult.Success("Registration successful!")
            } catch (e: Exception) {
                AuthResult.Error("Registration failed: ${e.message}")
            }
        }
    }
    
    suspend fun loginUser(mobileNumber: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                // Validate input
                if (mobileNumber.isBlank() || password.isBlank()) {
                    return@withContext AuthResult.Error("Please enter both mobile number and password")
                }
                
                // Get user by mobile number
                val user = getUserByMobile(mobileNumber)
                if (user == null) {
                    return@withContext AuthResult.Error("User not found. Please check your mobile number")
                }
                
                // Validate password
                if (user.password != password) {
                    return@withContext AuthResult.Error("Incorrect password")
                }
                
                // Set current user
                setCurrentUser(user)
                
                AuthResult.Success("Login successful!")
            } catch (e: Exception) {
                AuthResult.Error("Login failed: ${e.message}")
            }
        }
    }
    
    private fun saveUser(user: User) {
        val userKey = "user_${user.mobileNumber}"
        encryptedPrefs.edit()
            .putString("${userKey}_name", user.fullName)
            .putString("${userKey}_mobile", user.mobileNumber)
            .putString("${userKey}_password", user.password)
            .apply()
    }
    
    private fun getUserByMobile(mobileNumber: String): User? {
        val userKey = "user_$mobileNumber"
        val name = encryptedPrefs.getString("${userKey}_name", null)
        val mobile = encryptedPrefs.getString("${userKey}_mobile", null)
        val password = encryptedPrefs.getString("${userKey}_password", null)
        
        return if (name != null && mobile != null && password != null) {
            User(name, mobile, password)
        } else null
    }
    
    private fun setCurrentUser(user: User) {
        encryptedPrefs.edit()
            .putString("current_user_name", user.fullName)
            .putString("current_user_mobile", user.mobileNumber)
            .apply()
    }
    
    fun getCurrentUser(): User? {
        val name = encryptedPrefs.getString("current_user_name", null)
        val mobile = encryptedPrefs.getString("current_user_mobile", null)
        
        return if (name != null && mobile != null) {
            User(name, mobile, "") // Don't return password for security
        } else null
    }
    
    fun logout() {
        encryptedPrefs.edit()
            .remove("current_user_name")
            .remove("current_user_mobile")
            .apply()
    }
    
    fun isLoggedIn(): Boolean {
        return getCurrentUser() != null
    }
}

sealed class AuthResult {
    data class Success(val message: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
