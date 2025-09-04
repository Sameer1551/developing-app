package com.example.myapplication.screens

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

@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var emailAlertsEnabled by remember { mutableStateOf(false) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var autoSaveEnabled by remember { mutableStateOf(true) }
    var locationSharingEnabled by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Notifications Section
        SettingsSection(title = "Notifications") {
            SettingsSwitchItem(
                icon = Icons.Outlined.Notifications,
                title = "Push Notifications",
                subtitle = "Receive alerts about water quality issues",
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            
            SettingsSwitchItem(
                icon = Icons.Outlined.Email,
                title = "Email Alerts",
                subtitle = "Get email notifications for important updates",
                checked = emailAlertsEnabled,
                onCheckedChange = { emailAlertsEnabled = it }
            )
        }
        
        // Privacy & Security Section
        SettingsSection(title = "Privacy & Security") {
            SettingsSwitchItem(
                icon = Icons.Outlined.LocationOn,
                title = "Location Sharing",
                subtitle = "Allow location access for accurate reporting",
                checked = locationSharingEnabled,
                onCheckedChange = { locationSharingEnabled = it }
            )
            
            SettingsSwitchItem(
                icon = Icons.Outlined.Save,
                title = "Auto Save Reports",
                subtitle = "Automatically save draft reports",
                checked = autoSaveEnabled,
                onCheckedChange = { autoSaveEnabled = it }
            )
        }
        
        // Appearance Section
        SettingsSection(title = "Appearance") {
            SettingsSwitchItem(
                icon = Icons.Outlined.DarkMode,
                title = "Dark Mode",
                subtitle = "Use dark theme for better visibility",
                checked = darkModeEnabled,
                onCheckedChange = { darkModeEnabled = it }
            )
        }
        
        // Data Management Section
        SettingsSection(title = "Data Management") {
            SettingsActionItem(
                icon = Icons.Outlined.Download,
                title = "Export Data",
                subtitle = "Download your reports and data",
                onClick = { /* Handle export */ }
            )
            
            SettingsActionItem(
                icon = Icons.Outlined.Delete,
                title = "Clear All Data",
                subtitle = "Delete all saved reports and preferences",
                onClick = { /* Handle data clear */ }
            )
        }
        
        // Account Section
        SettingsSection(title = "Account") {
            SettingsActionItem(
                icon = Icons.Outlined.Password,
                title = "Change Password",
                subtitle = "Update your account password",
                onClick = { /* Handle password change */ }
            )
            
            SettingsActionItem(
                icon = Icons.Outlined.Person,
                title = "Edit Profile",
                subtitle = "Update your personal information",
                onClick = { /* Handle profile edit */ }
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
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
