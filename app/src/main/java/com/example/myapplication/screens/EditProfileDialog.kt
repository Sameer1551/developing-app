package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.AuthManager
import com.example.myapplication.User
import kotlinx.coroutines.launch

@Composable
fun EditProfileDialog(
    authManager: AuthManager,
    currentUser: User?,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var name by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var mobileNumber by remember { mutableStateOf(currentUser?.mobileNumber ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Update your personal information",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        errorMessage = ""
                    },
                    label = { Text("Full Name") },
                    placeholder = { Text("Enter your full name") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = errorMessage.isNotEmpty() && name.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Mobile Number Field
                OutlinedTextField(
                    value = mobileNumber,
                    onValueChange = { 
                        mobileNumber = it
                        errorMessage = ""
                    },
                    label = { Text("Mobile Number") },
                    placeholder = { Text("Enter your mobile number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = errorMessage.isNotEmpty() && mobileNumber.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        errorMessage = ""
                    },
                    label = { Text("Email Address") },
                    placeholder = { Text("Enter your email address (optional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = errorMessage.isNotEmpty() && email.isNotBlank() && !isValidEmail(email),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Mobile Number Change Warning
                if (mobileNumber != currentUser?.mobileNumber) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "⚠️ Mobile Number Change",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "If you change your mobile number, you'll need to use the new number and your current password to log in next time.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
                
                // Error Message
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Success Message
                if (successMessage.isNotEmpty()) {
                    Text(
                        text = successMessage,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            // Validate form
                            if (name.isBlank()) {
                                errorMessage = "Please enter your full name"
                                return@Button
                            }
                            
                            if (mobileNumber.isBlank()) {
                                errorMessage = "Please enter your mobile number"
                                return@Button
                            }
                            
                            if (email.isNotBlank() && !isValidEmail(email)) {
                                errorMessage = "Please enter a valid email address"
                                return@Button
                            }
                            
                            // Update profile
                            scope.launch {
                                isLoading = true
                                errorMessage = ""
                                
                                val result = authManager.updateProfile(name, mobileNumber, email)
                                
                                when (result) {
                                    is com.example.myapplication.AuthResult.Success -> {
                                        successMessage = result.message
                                        // Close dialog after a short delay
                                        kotlinx.coroutines.delay(2000)
                                        onSuccess()
                                    }
                                    is com.example.myapplication.AuthResult.Error -> {
                                        errorMessage = result.message
                                    }
                                }
                                
                                isLoading = false
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading && name.isNotBlank() && mobileNumber.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Update Profile")
                        }
                    }
                }
            }
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
