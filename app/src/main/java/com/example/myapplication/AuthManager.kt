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
    val password: String,
    val email: String = ""
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
    
    suspend fun registerUser(fullName: String, mobileNumber: String, password: String, email: String = ""): AuthResult {
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
                val user = User(fullName, mobileNumber, password, email)
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
            .putString("${userKey}_email", user.email)
            .apply()
    }
    
    private fun getUserByMobile(mobileNumber: String): User? {
        val userKey = "user_$mobileNumber"
        val name = encryptedPrefs.getString("${userKey}_name", null)
        val mobile = encryptedPrefs.getString("${userKey}_mobile", null)
        val password = encryptedPrefs.getString("${userKey}_password", null)
        val email = encryptedPrefs.getString("${userKey}_email", "")
        
        return if (name != null && mobile != null && password != null) {
            User(name, mobile, password, email ?: "")
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
            // Get full user data including email
            val fullUser = getUserByMobile(mobile)
            User(name, mobile, "", fullUser?.email ?: "") // Don't return password for security
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
    
    suspend fun changePassword(oldPassword: String, newPassword: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                // Get current user
                val currentUser = getCurrentUser()
                if (currentUser == null) {
                    return@withContext AuthResult.Error("No user logged in")
                }
                
                // Get full user data including password
                val fullUser = getUserByMobile(currentUser.mobileNumber)
                if (fullUser == null) {
                    return@withContext AuthResult.Error("User data not found")
                }
                
                // Validate old password
                if (fullUser.password != oldPassword) {
                    return@withContext AuthResult.Error("Current password is incorrect")
                }
                
                // Validate new password
                val passwordValidation = validatePassword(newPassword)
                if (!passwordValidation.isValid) {
                    return@withContext AuthResult.Error(passwordValidation.errorMessage)
                }
                
                // Update password
                val updatedUser = fullUser.copy(password = newPassword)
                saveUser(updatedUser)
                
                AuthResult.Success("Password changed successfully!")
            } catch (e: Exception) {
                AuthResult.Error("Password change failed: ${e.message}")
            }
        }
    }
    
    private fun validatePassword(password: String): PasswordValidation {
        if (password.length < 8) {
            return PasswordValidation(false, "Password must be at least 8 characters long")
        }
        
        if (!password.any { it.isDigit() }) {
            return PasswordValidation(false, "Password must contain at least one number")
        }
        
        if (!password.any { it.isLetter() }) {
            return PasswordValidation(false, "Password must contain at least one letter")
        }
        
        val specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?"
        if (!password.any { it in specialChars }) {
            return PasswordValidation(false, "Password must contain at least one special character")
        }
        
        return PasswordValidation(true, "")
    }
    
    suspend fun updateProfile(
        newName: String, 
        newMobileNumber: String, 
        newEmail: String
    ): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                // Get current user
                val currentUser = getCurrentUser()
                if (currentUser == null) {
                    return@withContext AuthResult.Error("No user logged in")
                }
                
                // Get full user data including password
                val fullUser = getUserByMobile(currentUser.mobileNumber)
                if (fullUser == null) {
                    return@withContext AuthResult.Error("User data not found")
                }
                
                // Validate input
                if (newName.isBlank()) {
                    return@withContext AuthResult.Error("Name cannot be empty")
                }
                
                if (newMobileNumber.isBlank()) {
                    return@withContext AuthResult.Error("Mobile number cannot be empty")
                }
                
                // Check if mobile number is being changed
                val isMobileNumberChanged = newMobileNumber != currentUser.mobileNumber
                
                // If mobile number is changed, check if new mobile number already exists
                if (isMobileNumberChanged) {
                    val existingUser = getUserByMobile(newMobileNumber)
                    if (existingUser != null) {
                        return@withContext AuthResult.Error("Mobile number already exists")
                    }
                }
                
                // Validate email format if provided
                if (newEmail.isNotBlank() && !isValidEmail(newEmail)) {
                    return@withContext AuthResult.Error("Please enter a valid email address")
                }
                
                // If mobile number changed, we need to migrate user data
                if (isMobileNumberChanged) {
                    // Remove old user data
                    val oldUserKey = "user_${currentUser.mobileNumber}"
                    encryptedPrefs.edit()
                        .remove("${oldUserKey}_name")
                        .remove("${oldUserKey}_mobile")
                        .remove("${oldUserKey}_password")
                        .remove("${oldUserKey}_email")
                        .apply()
                }
                
                // Create updated user
                val updatedUser = fullUser.copy(
                    fullName = newName,
                    mobileNumber = newMobileNumber,
                    email = newEmail
                )
                
                // Save updated user
                saveUser(updatedUser)
                
                // Update current user session
                setCurrentUser(updatedUser)
                
                AuthResult.Success("Profile updated successfully!")
            } catch (e: Exception) {
                AuthResult.Error("Profile update failed: ${e.message}")
            }
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

data class PasswordValidation(
    val isValid: Boolean,
    val errorMessage: String
)

sealed class AuthResult {
    data class Success(val message: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
