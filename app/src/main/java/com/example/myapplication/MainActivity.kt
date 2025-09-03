package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.screens.LoginScreen
import com.example.myapplication.screens.SignupScreen
import com.example.myapplication.screens.MainScreen

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authManager = AuthManager(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var currentScreen by remember { mutableStateOf("login") }
                
                // Check if user is already logged in
                LaunchedEffect(Unit) {
                    if (authManager.isLoggedIn()) {
                        currentScreen = "main"
                    }
                }
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            "login" -> LoginScreen(
                                authManager = authManager,
                                onSignupClick = { currentScreen = "signup" },
                                onLoginSuccess = { currentScreen = "main" }
                            )
                            "signup" -> SignupScreen(
                                authManager = authManager,
                                onLoginClick = { currentScreen = "login" }
                            )
                            "main" -> MainScreen(
                                authManager = authManager,
                                onLogout = { 
                                    authManager.logout()
                                    currentScreen = "login"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// LoginScreen is now imported from screens package

// SignupScreen is now imported from screens package

// MainScreen is now imported from screens package

// All screens and components are now imported from separate files
