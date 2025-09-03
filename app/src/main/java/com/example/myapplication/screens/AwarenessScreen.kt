package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Info
import com.example.myapplication.components.AwarenessCardComponent
import com.example.myapplication.models.AwarenessCard

@Composable
fun AwarenessScreen() {
    val awarenessCards = remember {
        listOf(
            AwarenessCard(
                id = "water_purification",
                title = "Safe Water Storage and Purification Methods",
                category = "Water Purification",
                type = "Guide",
                difficulty = "Beginner",
                duration = "8 min read",
                rating = 4.8,
                views = 1247,
                description = "Comprehensive guide to storing and purifying water to prevent waterborne diseases. Covers boiling, chlorination, and filtration techniques.",
                tags = listOf("water purification", "storage", "prevention")
            ),
            AwarenessCard(
                id = "boiling_water",
                title = "Boiling Water: The Most Effective Purification Method",
                category = "Water Purification",
                type = "Video",
                difficulty = "Beginner",
                duration = "4 min",
                rating = 4.9,
                views = 2156,
                description = "Step-by-step guide to properly boil water for safe consumption. Learn the correct temperature and duration for maximum effectiveness.",
                tags = listOf("boiling", "purification", "temperature", "safety")
            ),
            AwarenessCard(
                id = "cholera_prevention",
                title = "Cholera: Waterborne Disease Prevention Guide",
                category = "Waterborne Diseases",
                type = "Infographic",
                difficulty = "Intermediate",
                duration = "N/A",
                rating = 4.7,
                views = 1893,
                description = "Comprehensive infographic covering cholera symptoms, transmission through contaminated water, prevention methods, and emergency response procedures.",
                tags = listOf("cholera", "waterborne", "symptoms", "prevention", "emergency")
            ),
            AwarenessCard(
                id = "emergency_water_treatment",
                title = "Emergency Water Treatment During Disasters",
                category = "Emergency Water",
                type = "Video",
                difficulty = "Intermediate",
                duration = "15 min",
                rating = 4.9,
                views = 3421,
                description = "Essential water treatment procedures for emergency situations including natural disasters, power outages, and water supply contamination.",
                tags = listOf("emergency", "disaster", "water treatment", "survival")
            ),
            AwarenessCard(
                id = "water_conservation",
                title = "Water Conservation: Community Action Plan",
                category = "Water Conservation",
                type = "Article",
                difficulty = "Beginner",
                duration = "10 min read",
                rating = 4.6,
                views = 1567,
                description = "Complete guide to water conservation practices for communities. Includes rainwater harvesting, efficient usage, and sustainable practices.",
                tags = listOf("conservation", "rainwater", "efficiency", "sustainability")
            ),
            AwarenessCard(
                id = "dengue_prevention",
                title = "Dengue Prevention: Eliminating Standing Water",
                category = "Waterborne Diseases",
                type = "Infographic",
                difficulty = "Intermediate",
                duration = "N/A",
                rating = 4.5,
                views = 1342,
                description = "Strategic infographic showing how communities can prevent dengue fever by eliminating standing water sources and proper water management.",
                tags = listOf("dengue", "standing water", "mosquito control", "prevention")
            ),
            AwarenessCard(
                id = "water_quality_testing_community",
                title = "Water Quality Testing: DIY Methods for Communities",
                category = "Water Testing",
                type = "Article",
                difficulty = "Beginner",
                duration = "12 min read",
                rating = 4.8,
                views = 987,
                description = "Learn simple methods to test water quality at home and in communities using household items and when to seek professional testing.",
                tags = listOf("water testing", "DIY", "quality assessment", "community")
            ),
            AwarenessCard(
                id = "water_infrastructure_maintenance",
                title = "Community Water Infrastructure Maintenance",
                category = "Water Infrastructure",
                type = "Guide",
                difficulty = "Intermediate",
                duration = "20 min read",
                rating = 4.7,
                views = 2341,
                description = "Essential guide for maintaining community water systems including pipes, storage tanks, and distribution networks to prevent contamination.",
                tags = listOf("infrastructure", "maintenance", "pipes", "storage tanks")
            ),
            AwarenessCard(
                id = "water_quality_testing_diy",
                title = "Water Quality Testing: DIY Methods",
                category = "Water Testing",
                type = "Video",
                difficulty = "Intermediate",
                duration = "7 min",
                rating = 4.4,
                views = 1123,
                description = "Learn simple methods to test water quality at home using household items and when to seek professional testing.",
                tags = listOf("water testing", "DIY", "quality assessment", "safety")
            ),
            AwarenessCard(
                id = "solar_water_disinfection",
                title = "Solar Water Disinfection (SODIS) Method",
                category = "Water Purification",
                type = "Guide",
                difficulty = "Beginner",
                duration = "6 min read",
                rating = 4.6,
                views = 892,
                description = "Learn how to use sunlight to disinfect water in clear plastic bottles. An effective, low-cost method for water purification.",
                tags = listOf("SODIS", "solar", "disinfection", "low-cost")
            ),
            AwarenessCard(
                id = "typhoid_prevention",
                title = "Typhoid Fever: Water Contamination Prevention",
                category = "Waterborne Diseases",
                type = "Infographic",
                difficulty = "Intermediate",
                duration = "8 min read",
                rating = 4.5,
                views = 1456,
                description = "Complete guide to preventing typhoid fever through proper water treatment, sanitation practices, and community awareness.",
                tags = listOf("typhoid", "water contamination", "sanitation", "prevention")
            ),
            AwarenessCard(
                id = "rainwater_harvesting",
                title = "Rainwater Harvesting: Community Implementation",
                category = "Water Conservation",
                type = "Guide",
                difficulty = "Advanced",
                duration = "15 min read",
                rating = 4.7,
                views = 1123,
                description = "Step-by-step guide to implementing rainwater harvesting systems in communities for sustainable water supply and conservation.",
                tags = listOf("rainwater", "harvesting", "community", "sustainability")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = "Awareness",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = "Health Awareness",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Learn about health practices, disease prevention, and wellness tips",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Cards List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(awarenessCards) { card ->
                AwarenessCardComponent(
                    card = card,
                    onClick = {
                        // Handle card click - could navigate to detail screen
                        // For now, just a placeholder
                    }
                )
            }
            
            // Add bottom padding to ensure content doesn't go behind navigation bar
            item {
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}
