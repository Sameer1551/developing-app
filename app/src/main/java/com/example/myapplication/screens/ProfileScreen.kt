package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.AuthManager
import com.example.myapplication.data.ProfilePhotoStorage
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign

enum class ProfileScreenType {
    Main,
    Settings,
    HelpSupport,
    About
}

@Composable
fun ProfileScreen(
    authManager: AuthManager,
    onLogout: () -> Unit
) {
    val currentUser = authManager.getCurrentUser()
    val context = LocalContext.current
    val profilePhotoStorage = remember { ProfilePhotoStorage(context) }
    
    // Profile photo state
    var profilePhoto by remember { mutableStateOf<Bitmap?>(profilePhotoStorage.getProfilePhoto()) }
    var showPhotoOptions by remember { mutableStateOf(false) }
    
    // Navigation state
    var currentScreen by remember { mutableStateOf<ProfileScreenType>(ProfileScreenType.Main) }
    
    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            try {
                val inputStream = context.contentResolver.openInputStream(selectedUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    // Resize bitmap to reasonable size for profile photo
                    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
                    profilePhotoStorage.saveProfilePhoto(resizedBitmap)
                    profilePhoto = resizedBitmap
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            ProfileScreenType.Main -> {
                MainProfileContent(
                    currentUser = currentUser,
                    profilePhoto = profilePhoto,
                    showPhotoOptions = showPhotoOptions,
                    onPhotoOptionsChange = { showPhotoOptions = it },
                    onNavigateToSettings = { currentScreen = ProfileScreenType.Settings },
                    onNavigateToHelpSupport = { currentScreen = ProfileScreenType.HelpSupport },
                    onNavigateToAbout = { currentScreen = ProfileScreenType.About },
                    onLogout = onLogout,
                    photoPickerLauncher = photoPickerLauncher,
                    profilePhotoStorage = profilePhotoStorage,
                    onProfilePhotoChange = { profilePhoto = it }
                )
            }
            ProfileScreenType.Settings -> {
                SettingsScreen(
                    onBackPressed = { currentScreen = ProfileScreenType.Main }
                )
            }
            ProfileScreenType.HelpSupport -> {
                HelpSupportScreen(
                    onBackPressed = { currentScreen = ProfileScreenType.Main }
                )
            }
            ProfileScreenType.About -> {
                AboutScreen(
                    onBackPressed = { currentScreen = ProfileScreenType.Main }
                )
            }
        }
    }
}

@Composable
private fun MainProfileContent(
    currentUser: com.example.myapplication.User?,
    profilePhoto: Bitmap?,
    showPhotoOptions: Boolean,
    onPhotoOptionsChange: (Boolean) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHelpSupport: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onLogout: () -> Unit,
    photoPickerLauncher: androidx.activity.result.ActivityResultLauncher<String>,
    profilePhotoStorage: ProfilePhotoStorage,
    onProfilePhotoChange: (Bitmap?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Profile photo with upload functionality
        Box(
            modifier = Modifier
                .size(140.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
                .background(
                    color = if (profilePhoto != null) Color.Transparent else MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                )
                .border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    shape = CircleShape
                )
                .clickable { onPhotoOptionsChange(true) },
            contentAlignment = Alignment.Center
        ) {
            if (profilePhoto != null) {
                // Display uploaded profile photo
                Image(
                    bitmap = profilePhoto.asImageBitmap(),
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                // Edit overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.Black.copy(alpha = 0.3f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Photo",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }
            } else {
                // Default profile icon
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap to upload",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // User details
        currentUser?.let { user ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    ProfileInfoRow("Name", user.fullName)
                    Spacer(modifier = Modifier.height(12.dp))
                    ProfileInfoRow("Mobile", user.mobileNumber)
                    Spacer(modifier = Modifier.height(12.dp))
                    ProfileInfoRow("Status", "Active")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Profile actions
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileActionButton(
                icon = Icons.Outlined.Settings,
                label = "Settings",
                onClick = onNavigateToSettings
            )
            
            ProfileActionButton(
                icon = Icons.AutoMirrored.Outlined.Help,
                label = "Help & Support",
                onClick = onNavigateToHelpSupport
            )
            
            ProfileActionButton(
                icon = Icons.Outlined.Info,
                label = "About",
                onClick = onNavigateToAbout
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Logout button
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = "Logout",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
        
        // Add bottom padding to ensure content doesn't go behind navigation bar
        Spacer(modifier = Modifier.height(120.dp))
    }
    
    // Photo Options Dialog
    if (showPhotoOptions) {
        AlertDialog(
            onDismissRequest = { onPhotoOptionsChange(false) },
            title = {
                Text(
                    text = "Profile Photo",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (profilePhoto != null) 
                            "Change your profile photo" 
                        else 
                            "Add a profile photo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        photoPickerLauncher.launch("image/*")
                        onPhotoOptionsChange(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = "Choose Photo",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Choose Photo")
                }
            },
            dismissButton = {
                if (profilePhoto != null) {
                    TextButton(
                        onClick = {
                            profilePhotoStorage.clearProfilePhoto()
                            onProfilePhotoChange(null)
                            onPhotoOptionsChange(false)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Remove Photo")
                    }
                } else {
                    TextButton(onClick = { onPhotoOptionsChange(false) }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}

// Profile Info Row Component
@Composable
fun ProfileInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

// Profile Action Button Component
@Composable
fun ProfileActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
