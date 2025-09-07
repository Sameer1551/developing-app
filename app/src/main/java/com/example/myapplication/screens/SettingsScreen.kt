package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.utils.ThemeManager
import com.example.myapplication.utils.LanguageManager
import com.example.myapplication.AuthManager
import android.util.Log

@Composable
fun SettingsScreen(
    themeManager: ThemeManager,
    authManager: AuthManager,
    languageManager: LanguageManager,
    onBackPressed: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var emailAlertsEnabled by remember { mutableStateOf(false) }
    var autoSaveEnabled by remember { mutableStateOf(true) }
    var locationSharingEnabled by remember { mutableStateOf(true) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    
    // Get current user
    val currentUser = authManager.getCurrentUser()
    
    // Get dark mode state from ThemeManager
    val isDarkMode by themeManager.isDarkMode
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Sticky Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = languageManager.getLocalizedString("settings"),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Notifications Section
            SettingsSection(title = languageManager.getLocalizedString("notifications")) {
                SettingsSwitchItem(
                    icon = Icons.Outlined.Notifications,
                    title = languageManager.getLocalizedString("push_notifications"),
                    subtitle = languageManager.getLocalizedString("push_notifications_desc"),
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
                
                SettingsSwitchItem(
                    icon = Icons.Outlined.Email,
                    title = languageManager.getLocalizedString("email_alerts"),
                    subtitle = languageManager.getLocalizedString("email_alerts_desc"),
                    checked = emailAlertsEnabled,
                    onCheckedChange = { emailAlertsEnabled = it }
                )
            }
            
            // Privacy & Security Section
            SettingsSection(title = languageManager.getLocalizedString("privacy_security")) {
                SettingsSwitchItem(
                    icon = Icons.Outlined.LocationOn,
                    title = languageManager.getLocalizedString("location_sharing"),
                    subtitle = languageManager.getLocalizedString("location_sharing_desc"),
                    checked = locationSharingEnabled,
                    onCheckedChange = { locationSharingEnabled = it }
                )
                
                SettingsSwitchItem(
                    icon = Icons.Outlined.Save,
                    title = languageManager.getLocalizedString("auto_save_reports"),
                    subtitle = languageManager.getLocalizedString("auto_save_reports_desc"),
                    checked = autoSaveEnabled,
                    onCheckedChange = { autoSaveEnabled = it }
                )
            }
            
            // Appearance Section
            SettingsSection(title = languageManager.getLocalizedString("appearance")) {
                SettingsSwitchItem(
                    icon = Icons.Outlined.DarkMode,
                    title = languageManager.getLocalizedString("dark_mode"),
                    subtitle = languageManager.getLocalizedString("dark_mode_desc"),
                    checked = isDarkMode,
                    onCheckedChange = { 
                        themeManager.setDarkMode(it)
                    }
                )
            }
            
            // Language Section
            SettingsSection(title = languageManager.getLocalizedString("language")) {
                LanguageSelectionItem(
                    languageManager = languageManager,
                    onLanguageChange = { newLanguage ->
                        languageManager.setLanguage(newLanguage)
                    }
                )
            }
            
            // Data Management Section
            SettingsSection(title = languageManager.getLocalizedString("data_management")) {
                SettingsActionItem(
                    icon = Icons.Outlined.Download,
                    title = languageManager.getLocalizedString("export_data"),
                    subtitle = languageManager.getLocalizedString("export_data_desc"),
                    onClick = { /* Handle export */ }
                )
                
                SettingsActionItem(
                    icon = Icons.Outlined.Delete,
                    title = languageManager.getLocalizedString("clear_all_data"),
                    subtitle = languageManager.getLocalizedString("clear_all_data_desc"),
                    onClick = { /* Handle data clear */ }
                )
            }
            
            // Account Section
            SettingsSection(title = languageManager.getLocalizedString("account")) {
                SettingsActionItem(
                    icon = Icons.Outlined.Password,
                    title = languageManager.getLocalizedString("change_password"),
                    subtitle = languageManager.getLocalizedString("change_password_desc"),
                    onClick = { 
                        Log.d("SettingsScreen", "Change Password button clicked")
                        showChangePasswordDialog = true 
                    }
                )
                
                SettingsActionItem(
                    icon = Icons.Outlined.Person,
                    title = languageManager.getLocalizedString("edit_profile"),
                    subtitle = languageManager.getLocalizedString("edit_profile_desc"),
                    onClick = { 
                        Log.d("SettingsScreen", "Edit Profile button clicked")
                        showEditProfileDialog = true 
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Add bottom padding to prevent content from being hidden behind bottom navigation
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
    
    // Change Password Dialog
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            authManager = authManager,
            onDismiss = { showChangePasswordDialog = false },
            onSuccess = { 
                showChangePasswordDialog = false
                // Optionally show a success message or log out user
            }
        )
    }
    
    // Edit Profile Dialog
    if (showEditProfileDialog) {
        EditProfileDialog(
            authManager = authManager,
            currentUser = currentUser,
            onDismiss = { showEditProfileDialog = false },
            onSuccess = { 
                showEditProfileDialog = false
                // Profile updated successfully
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                content()
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

@Composable
private fun SettingsActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.small
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Navigate",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun LanguageSelectionItem(
    languageManager: LanguageManager,
    onLanguageChange: (com.example.myapplication.utils.AppLanguage) -> Unit
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { showLanguageDialog = true }
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.small
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Language,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = languageManager.getLocalizedString("select_language"),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = languageManager.getLocalizedString("language_desc"),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        
        Text(
            text = languageManager.currentLanguage.displayName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Navigate",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
    
    // Language Selection Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text(
                    text = languageManager.getLocalizedString("select_language"),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    com.example.myapplication.utils.AppLanguage.values().forEach { language ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    onLanguageChange(language)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = languageManager.currentLanguage == language,
                                onClick = { 
                                    onLanguageChange(language)
                                    showLanguageDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = language.displayName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
