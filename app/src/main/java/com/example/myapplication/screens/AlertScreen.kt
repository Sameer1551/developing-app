package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
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
fun AlertScreen() {
    val alerts = remember {
        mutableStateListOf(
            Alert(
                id = "1",
                title = "Water Quality Alert",
                message = "High levels of contaminants detected in Sector 5. Please avoid drinking tap water.",
                type = AlertType.EMERGENCY,
                timestamp = Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000), // 2 hours ago
                location = "Sector 5"
            ),
            Alert(
                id = "2",
                title = "Maintenance Notice",
                message = "Scheduled maintenance in your area. Water supply may be interrupted for 2 hours.",
                type = AlertType.WARNING,
                timestamp = Date(System.currentTimeMillis() - 6 * 60 * 60 * 1000), // 6 hours ago
                location = "Your Area"
            ),
            Alert(
                id = "3",
                title = "Quality Check Complete",
                message = "Routine water quality assessment completed. All parameters within safe limits.",
                type = AlertType.SUCCESS,
                timestamp = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000), // 1 day ago
                location = "City Wide"
            ),
            Alert(
                id = "4",
                title = "New Guidelines",
                message = "Updated water conservation guidelines are now available. Check the latest recommendations.",
                type = AlertType.INFO,
                timestamp = Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000), // 2 days ago
                location = "All Areas"
            )
        )
    }

    // Filter state
    val selectedFilter = remember { mutableStateOf(AlertFilter.ALL) }
    
    // Filtered alerts based on selected filter
    val filteredAlerts = remember(selectedFilter.value, alerts) {
        when (selectedFilter.value) {
            AlertFilter.ALL -> alerts
            AlertFilter.EMERGENCY -> alerts.filter { it.type == AlertType.EMERGENCY }
            AlertFilter.WARNING -> alerts.filter { it.type == AlertType.WARNING }
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
            AlertHeaderSection(filteredAlerts.size)
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        item {
            // Quick Stats Section
            AlertStatsSection(alerts)
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        item {
            // Filter Section
            AlertFilterSection(
                selectedFilter = selectedFilter.value,
                onFilterSelected = { filter ->
                    selectedFilter.value = filter
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Filtered Alerts List
        items(filteredAlerts) { alert ->
            AlertCard(alert = alert)
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        item {
            // Settings Section
            AlertSettingsSection()
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

enum class AlertFilter {
    ALL, EMERGENCY, WARNING
}

@Composable
fun AlertHeaderSection(alertCount: Int) {
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
                    text = "Alerts",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "$alertCount active notifications",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun AlertStatsSection(alerts: List<Alert>) {
    Column {
        Text(
            text = "Alert Overview",
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
                title = "Emergency",
                value = alerts.count { it.type == AlertType.EMERGENCY }.toString(),
                icon = Icons.Filled.Warning,
                color = AlertType.EMERGENCY.color,
                modifier = Modifier.weight(1f)
            )
            AlertStatCard(
                title = "Warnings",
                value = alerts.count { it.type == AlertType.WARNING }.toString(),
                icon = Icons.Filled.Info,
                color = AlertType.WARNING.color,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
            AlertStatCard(
                title = "Info",
                value = alerts.count { it.type == AlertType.INFO }.toString(),
                icon = Icons.Filled.Notifications,
                color = AlertType.INFO.color,
                modifier = Modifier.weight(1f)
            )
            AlertStatCard(
                title = "Resolved",
                value = alerts.count { it.type == AlertType.SUCCESS }.toString(),
                icon = Icons.Filled.CheckCircle,
                color = AlertType.SUCCESS.color,
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
    onFilterSelected: (AlertFilter) -> Unit
) {
    Column {
        Text(
            text = "Alert Filter",
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
                label = { Text("All") },
                selected = selectedFilter == AlertFilter.ALL,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                onClick = { onFilterSelected(AlertFilter.EMERGENCY) },
                label = { Text("Emergency") },
                selected = selectedFilter == AlertFilter.EMERGENCY,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                onClick = { onFilterSelected(AlertFilter.WARNING) },
                label = { Text("Warnings") },
                selected = selectedFilter == AlertFilter.WARNING,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AlertCard(alert: Alert) {
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
                        text = formatTimeAgo(alert.timestamp),
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
fun AlertSettingsSection() {
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
                text = "Alert Settings",
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
                        text = "Push Notifications",
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
                        text = "Email Alerts",
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

fun formatTimeAgo(date: Date): String {
    val now = Date()
    val diffInMillis = now.time - date.time
    val diffInHours = diffInMillis / (1000 * 60 * 60)
    val diffInDays = diffInHours / 24
    
    return when {
        diffInHours < 1 -> "Just now"
        diffInHours < 24 -> "${diffInHours}h ago"
        diffInDays < 7 -> "${diffInDays}d ago"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
    }
}
