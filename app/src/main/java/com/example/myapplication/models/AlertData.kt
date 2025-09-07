package com.example.myapplication.models

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

data class AlertDataResponse(
    val alerts: List<AlertData>,
    val usersByDistrict: Map<String, List<UserData>>
)

data class AlertData(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val priority: String,
    val status: String,
    val district: String,
    val location: String,
    val reporter: String,
    val reporterContact: String,
    val createdAt: String,
    val updatedAt: String,
    val assignedTo: String,
    val responseTime: String,
    val notes: List<String>,
    val attachments: List<String>
)

data class UserData(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String,
    val status: String,
    val state: String,
    val originalRole: String
)

enum class AlertPriority(val displayName: String, val color: androidx.compose.ui.graphics.Color) {
    CRITICAL("Critical", androidx.compose.ui.graphics.Color(0xFFFF5252)),
    HIGH("High", androidx.compose.ui.graphics.Color(0xFFFF9800)),
    MEDIUM("Medium", androidx.compose.ui.graphics.Color(0xFF2196F3)),
    LOW("Low", androidx.compose.ui.graphics.Color(0xFF4CAF50))
}

enum class AlertStatus(val displayName: String, val color: androidx.compose.ui.graphics.Color) {
    ACTIVE("Active", androidx.compose.ui.graphics.Color(0xFFFF5252)),
    IN_PROGRESS("In Progress", androidx.compose.ui.graphics.Color(0xFFFF9800)),
    RESOLVED("Resolved", androidx.compose.ui.graphics.Color(0xFF4CAF50)),
    CLOSED("Closed", androidx.compose.ui.graphics.Color(0xFF9E9E9E))
}

enum class AlertType(val displayName: String, val icon: ImageVector, val color: androidx.compose.ui.graphics.Color) {
    WATER_QUALITY("Water Quality", Icons.Filled.WaterDrop, androidx.compose.ui.graphics.Color(0xFF2196F3)),
    DISEASE_OUTBREAK("Disease Outbreak", Icons.Filled.Warning, androidx.compose.ui.graphics.Color(0xFFFF5252)),
    INFRASTRUCTURE("Infrastructure", Icons.Filled.Build, androidx.compose.ui.graphics.Color(0xFF9C27B0)),
    WEATHER("Weather", Icons.Filled.Cloud, androidx.compose.ui.graphics.Color(0xFF607D8B)),
    HEALTH_EMERGENCY("Health Emergency", Icons.Filled.LocalHospital, androidx.compose.ui.graphics.Color(0xFFE91E63)),
    OTHER("Other", Icons.Filled.Info, androidx.compose.ui.graphics.Color(0xFF9E9E9E))
}

// Extension functions to convert string values to enums
fun String.toAlertPriority(): AlertPriority {
    return when (this.lowercase()) {
        "critical" -> AlertPriority.CRITICAL
        "high" -> AlertPriority.HIGH
        "medium" -> AlertPriority.MEDIUM
        "low" -> AlertPriority.LOW
        else -> AlertPriority.MEDIUM
    }
}

fun String.toAlertStatus(): AlertStatus {
    return when (this.lowercase()) {
        "active" -> AlertStatus.ACTIVE
        "in progress" -> AlertStatus.IN_PROGRESS
        "resolved" -> AlertStatus.RESOLVED
        "closed" -> AlertStatus.CLOSED
        else -> AlertStatus.ACTIVE
    }
}

fun String.toAlertType(): AlertType {
    return when (this.lowercase()) {
        "water quality" -> AlertType.WATER_QUALITY
        "disease outbreak" -> AlertType.DISEASE_OUTBREAK
        "infrastructure" -> AlertType.INFRASTRUCTURE
        "weather" -> AlertType.WEATHER
        "health emergency" -> AlertType.HEALTH_EMERGENCY
        else -> AlertType.OTHER
    }
}

