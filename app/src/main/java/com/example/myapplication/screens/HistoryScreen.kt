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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.data.ReportStorage
import com.example.myapplication.models.WaterQualityReport
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val reportStorage = remember { ReportStorage(context) }
    
    // Get all reports
    val allReports = remember { reportStorage.getAllReports().sortedByDescending { it.submittedAt } }
    
    // Filter state
    var selectedFilter by remember { mutableStateOf(ReportFilter.ALL) }
    
    // Filtered reports based on selected filter
    val filteredReports = remember(selectedFilter, allReports) {
        when (selectedFilter) {
            ReportFilter.ALL -> allReports
            ReportFilter.PENDING -> allReports.filter { it.status == "Pending" }
            ReportFilter.REVIEWED -> allReports.filter { it.status == "Reviewed" }
            ReportFilter.RESOLVED -> allReports.filter { it.status == "Resolved" }
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
            HistoryHeaderSection(allReports.size)
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        item {
            // Statistics Section
            HistoryStatsSection(allReports)
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        item {
            // Filter Section
            HistoryFilterSection(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Reports List
        if (filteredReports.isEmpty()) {
            item {
                EmptyReportsCard()
            }
        } else {
            items(filteredReports) { report ->
                ReportHistoryCard(report = report)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

enum class ReportFilter {
    ALL, PENDING, REVIEWED, RESOLVED
}

@Composable
fun HistoryHeaderSection(totalReports: Int) {
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
            // History Icon
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
                        imageVector = Icons.Filled.History,
                        contentDescription = "History",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Header Info
            Column {
                Text(
                    text = "Report History",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "$totalReports total reports submitted",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun HistoryStatsSection(reports: List<WaterQualityReport>) {
    Column {
        Text(
            text = "Report Statistics",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HistoryStatCard(
                title = "Pending",
                value = reports.count { it.status == "Pending" }.toString(),
                icon = Icons.Filled.Schedule,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            HistoryStatCard(
                title = "Reviewed",
                value = reports.count { it.status == "Reviewed" }.toString(),
                icon = Icons.Filled.Visibility,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HistoryStatCard(
                title = "Resolved",
                value = reports.count { it.status == "Resolved" }.toString(),
                icon = Icons.Filled.CheckCircle,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
            HistoryStatCard(
                title = "Total",
                value = reports.size.toString(),
                icon = Icons.Filled.Assessment,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun HistoryStatCard(
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
fun HistoryFilterSection(
    selectedFilter: ReportFilter,
    onFilterSelected: (ReportFilter) -> Unit
) {
    Column {
        Text(
            text = "Filter Reports",
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
                onClick = { onFilterSelected(ReportFilter.ALL) },
                label = { Text("All") },
                selected = selectedFilter == ReportFilter.ALL,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                onClick = { onFilterSelected(ReportFilter.PENDING) },
                label = { Text("Pending") },
                selected = selectedFilter == ReportFilter.PENDING,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                onClick = { onFilterSelected(ReportFilter.REVIEWED) },
                label = { Text("Reviewed") },
                selected = selectedFilter == ReportFilter.REVIEWED,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                onClick = { onFilterSelected(ReportFilter.RESOLVED) },
                label = { Text("Resolved") },
                selected = selectedFilter == ReportFilter.RESOLVED,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ReportHistoryCard(report: WaterQualityReport) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = report.waterSourceName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                Surface(
                    shape = RoundedCornerShape(12.dp),
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
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = report.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Water Quality Observations
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                QualityIndicator(
                    icon = Icons.Filled.Visibility,
                    label = "Appearance",
                    value = report.waterAppearance
                )
                QualityIndicator(
                    icon = Icons.Filled.Air,
                    label = "Smell",
                    value = report.waterSmell
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Footer Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Submitted: ${SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(report.submittedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                if (report.photoCount > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Photo,
                            contentDescription = "Photos",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${report.photoCount} photo(s)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            
            // Additional Notes (if any)
            if (report.additionalNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notes: ${report.additionalNotes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun QualityIndicator(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EmptyReportsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Assignment,
                contentDescription = "No Reports",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No Reports Found",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "You haven't submitted any water quality reports yet. Start by reporting an issue to help improve water quality in your area.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Helper function
@Composable
private fun getStatusColor(status: String): androidx.compose.ui.graphics.Color {
    return when (status.lowercase()) {
        "pending" -> MaterialTheme.colorScheme.secondary
        "reviewed" -> MaterialTheme.colorScheme.primary
        "resolved" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.onSurface
    }
}
