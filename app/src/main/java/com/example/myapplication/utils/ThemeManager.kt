package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class ThemeManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "theme_preferences",
        Context.MODE_PRIVATE
    )
    
    private val _isDarkMode = mutableStateOf(getStoredDarkModePreference())
    val isDarkMode: State<Boolean> = _isDarkMode
    
    fun toggleDarkMode() {
        val newValue = !_isDarkMode.value
        _isDarkMode.value = newValue
        saveDarkModePreference(newValue)
    }
    
    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        saveDarkModePreference(enabled)
    }
    
    private fun getStoredDarkModePreference(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false)
    }
    
    private fun saveDarkModePreference(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }
    
    companion object {
        private const val KEY_DARK_MODE = "dark_mode_enabled"
    }
}
