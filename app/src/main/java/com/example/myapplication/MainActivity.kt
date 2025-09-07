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
import com.example.myapplication.utils.ThemeManager
import com.example.myapplication.utils.LanguageManager

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager
    private lateinit var themeManager: ThemeManager
    private lateinit var languageManager: LanguageManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authManager = AuthManager(this)
        themeManager = ThemeManager(this)
        languageManager = LanguageManager(this)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by themeManager.isDarkMode
            
            MyApplicationTheme(darkTheme = isDarkMode) {
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
                                themeManager = themeManager,
                                languageManager = languageManager,
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
