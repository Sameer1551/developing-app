package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.AuthManager
import com.example.myapplication.navigation.BottomNavigation

@Composable
fun MainScreen(
    authManager: AuthManager,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(2) } // Start with Home tab (index 2)
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        when (selectedTab) {
            0 -> AlertScreen()
            1 -> AwarenessScreen()
            2 -> HomeScreen(authManager, onLogout)
            3 -> ReportIssueScreen()
            4 -> ProfileScreen(authManager, onLogout)
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
