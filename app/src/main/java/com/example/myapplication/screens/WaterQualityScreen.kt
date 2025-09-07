package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.data.ReportStorage
import com.example.myapplication.models.WaterQualityReport
import java.text.SimpleDateFormat
import java.util.*

data class WaterQualityStatus(
    val location: String,
    val overallStatus: String,
    val lastChecked: Date,
    val safetyLevel: String,
    val contaminationLevel: String,
    val phLevel: String,
    val turbidity: String,
    val chlorineLevel: String,
    val bacteriaCount: String,
    val recommendations: List<String>
)

@Composable
fun WaterQualityScreen() {
    val context = LocalContext.current
    val reportStorage = remember { ReportStorage(context) }
    
    // Get recent reports for this location (simulated)
    val recentReports = remember { 
        reportStorage.getAllReports().take(5) // Get last 5 reports
    }
    
    // Simulate water quality data based on recent reports
    val waterQualityData = generateWaterQualityData(recentReports)
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            // Header Section
            WaterQualityHeaderSection(waterQualityData)
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        item {
            // Overall Status Card
            OverallStatusCard(waterQualityData)
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            // Detailed Parameters
            DetailedParametersSection(waterQualityData)
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            // Recent Reports Section
            RecentReportsSection(recentReports)
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            // Recommendations Section
            RecommendationsSection(waterQualityData.recommendations)
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun WaterQualityHeaderSection(data: WaterQualityStatus) {
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
            // Water Drop Icon
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
                        imageVector = Icons.Filled.WaterDrop,
                        contentDescription = "Water Quality",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Header Info
            Column {
                Text(
                    text = "Water Quality Status",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = data.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = "Last updated: ${SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(data.lastChecked)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun OverallStatusCard(data: WaterQualityStatus) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getStatusColor(data.overallStatus).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = getStatusIcon(data.overallStatus),
                contentDescription = "Status",
                modifier = Modifier.size(48.dp),
                tint = getStatusColor(data.overallStatus)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = data.overallStatus,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = getStatusColor(data.overallStatus)
            )
            
            Text(
                text = data.safetyLevel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun DetailedParametersSection(data: WaterQualityStatus) {
    Column {
        Text(
            text = "Water Quality Parameters",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ParameterRow(
                    icon = Icons.Filled.Science,
                    parameter = "pH Level",
                    value = data.phLevel,
                    status = getParameterStatus(data.phLevel, "pH")
                )
                
                ParameterRow(
                    icon = Icons.Filled.Visibility,
                    parameter = "Turbidity",
                    value = data.turbidity,
                    status = getParameterStatus(data.turbidity, "Turbidity")
                )
                
                ParameterRow(
                    icon = Icons.Filled.CleaningServices,
                    parameter = "Chlorine Level",
                    value = data.chlorineLevel,
                    status = getParameterStatus(data.chlorineLevel, "Chlorine")
                )
                
                ParameterRow(
                    icon = Icons.Filled.BugReport,
                    parameter = "Bacteria Count",
                    value = data.bacteriaCount,
                    status = getParameterStatus(data.bacteriaCount, "Bacteria")
                )
                
                ParameterRow(
                    icon = Icons.Filled.Warning,
                    parameter = "Contamination Level",
                    value = data.contaminationLevel,
                    status = getParameterStatus(data.contaminationLevel, "Contamination")
                )
            }
        }
    }
}

@Composable
fun ParameterRow(
    icon: ImageVector,
    parameter: String,
    value: String,
    status: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = parameter,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = parameter,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = getStatusColor(status).copy(alpha = 0.1f)
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.bodySmall,
                color = getStatusColor(status),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun RecentReportsSection(reports: List<WaterQualityReport>) {
    Column {
        Text(
            text = "Recent Reports",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (reports.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "No Reports",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No recent reports found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            reports.forEach { report ->
                ReportCard(report = report)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ReportCard(report: WaterQualityReport) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Report,
                contentDescription = "Report",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = report.waterSourceName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = report.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = getStatusColor(report.status).copy(alpha = 0.1f)
            ) {
                Text(
                    text = report.status,
                    style = MaterialTheme.typography.bodySmall,
                    color = getStatusColor(report.status),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun RecommendationsSection(recommendations: List<String>) {
    Column {
        Text(
            text = "Recommendations",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recommendations.forEach { recommendation ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = "Recommendation",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = recommendation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

// Helper functions
@Composable
private fun generateWaterQualityData(reports: List<WaterQualityReport>): WaterQualityStatus {
    val hasRecentIssues = reports.any { 
        it.waterAppearance.contains("cloudy", ignoreCase = true) ||
        it.waterSmell.contains("unusual", ignoreCase = true) ||
        it.status == "Pending"
    }
    
    return WaterQualityStatus(
        location = "Your Area",
        overallStatus = if (hasRecentIssues) "Needs Attention" else "Good",
        lastChecked = Date(),
        safetyLevel = if (hasRecentIssues) "Use with caution" else "Safe for consumption",
        contaminationLevel = if (hasRecentIssues) "Low to Moderate" else "Minimal",
        phLevel = "7.2",
        turbidity = "2.1 NTU",
        chlorineLevel = "0.8 mg/L",
        bacteriaCount = if (hasRecentIssues) "Elevated" else "Normal",
        recommendations = if (hasRecentIssues) {
            listOf(
                "Consider boiling water before consumption",
                "Use water filters if available",
                "Report any new issues immediately",
                "Avoid drinking directly from tap"
            )
        } else {
            listOf(
                "Water is safe for consumption",
                "Continue regular monitoring",
                "Report any changes in water quality"
            )
        }
    )
}

@Composable
private fun getStatusColor(status: String): androidx.compose.ui.graphics.Color {
    return when (status.lowercase()) {
        "good", "safe", "normal" -> MaterialTheme.colorScheme.primary
        "needs attention", "elevated", "moderate" -> MaterialTheme.colorScheme.error
        "pending" -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.onSurface
    }
}

@Composable
private fun getStatusIcon(status: String): ImageVector {
    return when (status.lowercase()) {
        "good", "safe" -> Icons.Filled.CheckCircle
        "needs attention" -> Icons.Filled.Warning
        "pending" -> Icons.Filled.Info
        else -> Icons.Filled.Help
    }
}

@Composable
private fun getParameterStatus(value: String, parameter: String): String {
    return when (parameter) {
        "pH" -> when {
            value.toDoubleOrNull()?.let { it in 6.5..8.5 } == true -> "Good"
            else -> "Needs Attention"
        }
        "Turbidity" -> when {
            value.contains("Low", ignoreCase = true) -> "Good"
            else -> "Needs Attention"
        }
        "Chlorine" -> when {
            value.contains("0.2", ignoreCase = true) || value.contains("0.8", ignoreCase = true) -> "Good"
            else -> "Needs Attention"
        }
        "Bacteria" -> when {
            value.contains("Normal", ignoreCase = true) -> "Good"
            else -> "Needs Attention"
        }
        "Contamination" -> when {
            value.contains("Minimal", ignoreCase = true) -> "Good"
            else -> "Needs Attention"
        }
        else -> "Unknown"
    }
}
