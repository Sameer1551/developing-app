package com.example.myapplication.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.data.AlertDataLoader
import com.example.myapplication.models.*
import com.example.myapplication.utils.LanguageManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class Alert(
    val id: String,
    val title: String,
    val message: String,
    val type: AlertType,
    val timestamp: Date,
    val isRead: Boolean = false,
    val location: String = ""
)

enum class AlertType(val icon: ImageVector, val color: androidx.compose.ui.graphics.Color) {
    EMERGENCY(Icons.Filled.Warning, androidx.compose.ui.graphics.Color(0xFFFF5252)),
    WARNING(Icons.Filled.Info, androidx.compose.ui.graphics.Color(0xFFFF9800)),
    INFO(Icons.Filled.Notifications, androidx.compose.ui.graphics.Color(0xFF2196F3)),
    SUCCESS(Icons.Filled.CheckCircle, androidx.compose.ui.graphics.Color(0xFF4CAF50))
}

@Composable
fun AlertScreen(languageManager: LanguageManager) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // State for dynamic data
    var alertData by remember { mutableStateOf<List<AlertData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // State for expandable details
    var expandedAlertId by remember { mutableStateOf<String?>(null) }
    
    // Filter state
    val selectedFilter = remember { mutableStateOf(AlertFilter.ALL) }
    
    // Load data on first composition
    LaunchedEffect(Unit) {
        val loader = AlertDataLoader(context)
        loader.loadAlertData()
            .onSuccess { response ->
                alertData = response.alerts
                isLoading = false
            }
            .onFailure { error ->
                errorMessage = error.message
                isLoading = false
            }
    }
    
    // Filtered alerts based on selected filter
    val filteredAlerts = remember(selectedFilter.value, alertData) {
        when (selectedFilter.value) {
            AlertFilter.ALL -> alertData
            AlertFilter.EMERGENCY -> alertData.filter { it.priority.toAlertPriority() == AlertPriority.CRITICAL }
            AlertFilter.WARNING -> alertData.filter { it.priority.toAlertPriority() == AlertPriority.HIGH }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            // Header Section
            AlertHeaderSection(filteredAlerts.size, languageManager)
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        when {
            isLoading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            
            errorMessage != null -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage ?: "Unknown error",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            
            else -> {
                item {
                    // Quick Stats Section
                    AlertStatsSection(alertData, languageManager)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                item {
                    // Filter Section
                    AlertFilterSection(
                        selectedFilter = selectedFilter.value,
                        languageManager = languageManager,
                        onFilterSelected = { filter ->
                            selectedFilter.value = filter
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Filtered Alerts List
                items(filteredAlerts) { alert ->
                    AlertCardWithDetails(
                        alert = alert,
                        languageManager = languageManager,
                        isExpanded = expandedAlertId == alert.id,
                        onToggleExpanded = { alertId ->
                            expandedAlertId = if (expandedAlertId == alertId) null else alertId
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        
        item {
            // Settings Section
            AlertSettingsSection(languageManager)
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

enum class AlertFilter {
    ALL, EMERGENCY, WARNING
}

@Composable
fun AlertHeaderSection(alertCount: Int, languageManager: LanguageManager) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Alert Icon
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Alerts",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Header Info
            Column {
                Text(
                    text = languageManager.getLocalizedString("alerts"),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "$alertCount ${languageManager.getLocalizedString("active_notifications")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun AlertStatsSection(alerts: List<AlertData>, languageManager: LanguageManager) {
    Column {
        Text(
            text = languageManager.getLocalizedString("alert_overview"),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AlertStatCard(
                title = languageManager.getLocalizedString("emergency"),
                value = alerts.count { it.priority.toAlertPriority() == AlertPriority.CRITICAL }.toString(),
                icon = Icons.Filled.Warning,
                color = AlertPriority.CRITICAL.color,
                modifier = Modifier.weight(1f)
            )
            AlertStatCard(
                title = languageManager.getLocalizedString("warnings"),
                value = alerts.count { it.priority.toAlertPriority() == AlertPriority.HIGH }.toString(),
                icon = Icons.Filled.Info,
                color = AlertPriority.HIGH.color,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
            AlertStatCard(
                title = languageManager.getLocalizedString("info"),
                value = alerts.count { it.priority.toAlertPriority() == AlertPriority.MEDIUM }.toString(),
                icon = Icons.Filled.Notifications,
                color = AlertPriority.MEDIUM.color,
                modifier = Modifier.weight(1f)
            )
            AlertStatCard(
                title = languageManager.getLocalizedString("resolved"),
                value = alerts.count { it.status.toAlertStatus() == AlertStatus.RESOLVED }.toString(),
                icon = Icons.Filled.CheckCircle,
                color = AlertStatus.RESOLVED.color,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AlertStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun AlertFilterSection(
    selectedFilter: AlertFilter,
    languageManager: LanguageManager,
    onFilterSelected: (AlertFilter) -> Unit
) {
    Column {
        Text(
            text = languageManager.getLocalizedString("alert_filter"),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                onClick = { onFilterSelected(AlertFilter.ALL) },
                label = { Text(languageManager.getLocalizedString("all")) },
                selected = selectedFilter == AlertFilter.ALL,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                onClick = { onFilterSelected(AlertFilter.EMERGENCY) },
                label = { Text(languageManager.getLocalizedString("emergency")) },
                selected = selectedFilter == AlertFilter.EMERGENCY,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                onClick = { onFilterSelected(AlertFilter.WARNING) },
                label = { Text(languageManager.getLocalizedString("warnings")) },
                selected = selectedFilter == AlertFilter.WARNING,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AlertCard(alert: Alert, languageManager: LanguageManager) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (alert.isRead) 
                MaterialTheme.colorScheme.surface 
            else 
                alert.type.color.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Alert Icon
            Icon(
                imageVector = alert.type.icon,
                contentDescription = alert.type.name,
                tint = alert.type.color,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Alert Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = formatTimeAgo(alert.timestamp, languageManager),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = alert.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = alert.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun AlertSettingsSection(languageManager: LanguageManager) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = languageManager.getLocalizedString("alert_settings"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Push Notifications",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = languageManager.getLocalizedString("push_notifications"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Switch(
                    checked = true,
                    onCheckedChange = { },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        checkedTrackColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.3f)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email Alerts",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = languageManager.getLocalizedString("email_alerts"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Switch(
                    checked = false,
                    onCheckedChange = { },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        checkedTrackColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.3f)
                    )
                )
            }
        }
    }
}

fun formatTimeAgo(date: Date, languageManager: LanguageManager): String {
    val now = Date()
    val diffInMillis = now.time - date.time
    val diffInHours = diffInMillis / (1000 * 60 * 60)
    val diffInDays = diffInHours / 24
    
    return when {
        diffInHours < 1 -> languageManager.getLocalizedString("just_now")
        diffInHours < 24 -> "${diffInHours}${languageManager.getLocalizedString("hours_ago")}"
        diffInDays < 7 -> "${diffInDays}${languageManager.getLocalizedString("days_ago")}"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
    }
}

@Composable
fun AlertCardWithDetails(
    alert: AlertData,
    languageManager: LanguageManager,
    isExpanded: Boolean,
    onToggleExpanded: (String) -> Unit
) {
    val alertType = alert.type.toAlertType()
    val alertPriority = alert.priority.toAlertPriority()
    val alertStatus = alert.status.toAlertStatus()
    
    Column {
        // Main Alert Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpanded(alert.id) },
            colors = CardDefaults.cardColors(
                containerColor = if (alertStatus == AlertStatus.RESOLVED) 
                    MaterialTheme.colorScheme.surfaceVariant
                else 
                    alertPriority.color.copy(alpha = 0.05f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Alert Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(alertPriority.color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = alertType.icon,
                        contentDescription = alertType.displayName,
                        tint = alertPriority.color,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Alert Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = alert.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Priority Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = alertPriority.color.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = alertPriority.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = alertPriority.color,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Status Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = alertStatus.color.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = alertStatus.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = alertStatus.color,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        // Expand/Collapse Icon
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Expandable Details Section
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = languageManager.getLocalizedString("alert_details"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Details Grid
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("priority"),
                        value = alertPriority.displayName,
                        valueColor = alertPriority.color
                    )
                    
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("status"),
                        value = alertStatus.displayName,
                        valueColor = alertStatus.color
                    )
                    
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("district"),
                        value = alert.district
                    )
                    
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("location"),
                        value = alert.location
                    )
                    
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("reporter"),
                        value = alert.reporter
                    )
                    
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("reporter_contact"),
                        value = alert.reporterContact
                    )
                    
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("created_at"),
                        value = alert.createdAt
                    )
                    
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("updated_at"),
                        value = alert.updatedAt
                    )
                    
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("assigned_to"),
                        value = alert.assignedTo
                    )
                    
                    AlertDetailRow(
                        label = languageManager.getLocalizedString("response_time"),
                        value = alert.responseTime
                    )
                    
                    // Notes Section
                    if (alert.notes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = languageManager.getLocalizedString("notes"),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        alert.notes.forEach { note ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = "• ",
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = note,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    // Attachments Section
                    if (alert.attachments.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = languageManager.getLocalizedString("attachments"),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        alert.attachments.forEach { attachment ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AttachFile,
                                    contentDescription = "Attachment",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = attachment,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    // Action Buttons
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* Handle contact reporter */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Phone,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(languageManager.getLocalizedString("contact_reporter"))
                        }
                        
                        OutlinedButton(
                            onClick = { /* Handle view attachments */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AttachFile,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(languageManager.getLocalizedString("view_attachments"))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlertDetailRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            modifier = Modifier.weight(1f)
        )
    }
}
