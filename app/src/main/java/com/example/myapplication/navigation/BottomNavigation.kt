package com.example.myapplication.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Person

// Bottom Navigation Item data class
data class BottomNavItem(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

@Composable
fun BottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val bottomNavItems = listOf(
        BottomNavItem("Alert", Icons.Outlined.Notifications, "alert"),
        BottomNavItem("Awareness", Icons.Outlined.Lightbulb, "awareness"),
        BottomNavItem("Home", Icons.Filled.Home, "home"),
        BottomNavItem("Report Issue", Icons.Outlined.Report, "report"),
        BottomNavItem("Profile", Icons.Outlined.Person, "profile")
    )
    
    // Custom Stylized Bottom Navigation
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Main navigation bar background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(40.dp)
                )
        )
        
        // Navigation items row with weight-based equal spacing
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Alert Icon with equal weight
            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onTabSelected(0) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Alert",
                            modifier = Modifier.size(24.dp),
                            tint = if (selectedTab == 0) MaterialTheme.colorScheme.onPrimary else Color.White
                        )
                    }
                }
            }
            
            // Awareness Icon with equal weight
            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (selectedTab == 1) MaterialTheme.colorScheme.secondary else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onTabSelected(1) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = "Awareness",
                            modifier = Modifier.size(24.dp),
                            tint = if (selectedTab == 1) MaterialTheme.colorScheme.onSecondary else Color.White
                        )
                    }
                }
            }
            
            // Central Home Button with equal weight (Floating above the bar)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(72.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = (-20).dp)
                        .size(72.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onTabSelected(2) },
                        modifier = Modifier.size(72.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            
            // Report Issue Icon with equal weight
            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (selectedTab == 3) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onTabSelected(3) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Report,
                            contentDescription = "Report Issue",
                            modifier = Modifier.size(24.dp),
                            tint = if (selectedTab == 3) MaterialTheme.colorScheme.onTertiary else Color.White
                        )
                    }
                }
            }
            
            // Profile Icon with equal weight
            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (selectedTab == 4) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onTabSelected(4) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(24.dp),
                            tint = if (selectedTab == 4) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                        )
                    }
                }
            }
        }
    }
}
