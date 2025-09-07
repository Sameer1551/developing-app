package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.AuthManager
import com.example.myapplication.navigation.BottomNavigation
import com.example.myapplication.utils.ThemeManager

@Composable
fun MainScreen(
    authManager: AuthManager,
    themeManager: ThemeManager,
    languageManager: com.example.myapplication.utils.LanguageManager,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(2) } // Start with Home tab (index 2)
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        when (selectedTab) {
            0 -> AlertScreen(languageManager = languageManager)
            1 -> AwarenessScreen()
            2 -> HomeScreen(
                authManager = authManager, 
                onLogout = onLogout,
                onNavigateToReport = { selectedTab = 3 },
                onNavigateToAlerts = { selectedTab = 0 },
                onNavigateToWaterQuality = { selectedTab = 5 },
                onNavigateToHistory = { selectedTab = 6 }
            )
            3 -> ReportIssueScreen()
            4 -> ProfileScreen(authManager, themeManager, languageManager, onLogout)
            5 -> WaterQualityScreen()
            6 -> HistoryScreen()
        }
        
        // Bottom Navigation
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}
