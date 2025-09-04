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
import androidx.compose.foundation.clickable

@Composable
fun HelpSupportScreen(
    onBackPressed: () -> Unit
) {
    var expandedFaq by remember { mutableStateOf<Int?>(null) }
    
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
                text = "Help & Support",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Quick Actions
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                icon = Icons.Outlined.Email,
                title = "Email Support",
                subtitle = "Send us an email",
                modifier = Modifier.weight(1f),
                onClick = { /* Handle email support */ }
            )
            
            QuickActionCard(
                icon = Icons.Outlined.Phone,
                title = "Call Support",
                subtitle = "Speak with us",
                modifier = Modifier.weight(1f),
                onClick = { /* Handle phone support */ }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // FAQ Section
        Text(
            text = "Frequently Asked Questions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        val faqs = listOf(
            FAQItem(
                question = "How do I report a water quality issue?",
                answer = "Go to the Report Issue screen, fill in all required fields including water source details, observations, and any photos. Submit the form and your report will be saved locally."
            ),
            FAQItem(
                question = "What information should I include in my report?",
                answer = "Include your contact details, water source location, visual observations (color, smell, taste), visible particles, and any health concerns. Photos are optional but helpful."
            ),
            FAQItem(
                question = "How do I upload photos with my report?",
                answer = "In the Report Issue screen, tap the 'Upload Photos' section and select photos from your gallery. You can upload multiple photos and remove them if needed."
            ),
            FAQItem(
                question = "Can I edit my profile photo?",
                answer = "Yes! In the Profile screen, tap on your profile photo to open options. You can choose a new photo from your gallery or remove the current one."
            ),
            FAQItem(
                question = "How do I change my notification settings?",
                answer = "Go to Settings from the Profile screen and adjust your notification preferences including push notifications and email alerts."
            ),
            FAQItem(
                question = "Is my data secure?",
                answer = "Yes, all your data is stored locally on your device using secure encryption. We don't collect or transmit your personal information."
            )
        )
        
        faqs.forEachIndexed { index, faq ->
            FAQItem(
                question = faq.question,
                answer = faq.answer,
                expanded = expandedFaq == index,
                onToggle = { 
                    expandedFaq = if (expandedFaq == index) null else index 
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Contact Information
        Text(
            text = "Contact Information",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ContactInfoItem(
                    icon = Icons.Outlined.Email,
                    title = "Email",
                    value = "support@waterqualityapp.com"
                )
                
                ContactInfoItem(
                    icon = Icons.Outlined.Phone,
                    title = "Phone",
                    value = "+1 (555) 123-4567"
                )
                
                ContactInfoItem(
                    icon = Icons.Outlined.Schedule,
                    title = "Support Hours",
                    value = "Monday - Friday: 9:00 AM - 6:00 PM"
                )
                
                ContactInfoItem(
                    icon = Icons.Outlined.LocationOn,
                    title = "Address",
                    value = "123 Water Quality St, City, State 12345"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FAQItem(
    question: String,
    answer: String,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun ContactInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

private data class FAQItem(
    val question: String,
    val answer: String
)
