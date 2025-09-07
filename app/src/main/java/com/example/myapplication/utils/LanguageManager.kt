package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "हिंदी")
}

class LanguageManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    
    private var _currentLanguage by mutableStateOf(
        AppLanguage.values().find { it.code == prefs.getString("selected_language", "en") } 
            ?: AppLanguage.ENGLISH
    )
    
    val currentLanguage: AppLanguage
        get() = _currentLanguage
    
    fun setLanguage(language: AppLanguage) {
        _currentLanguage = language
        prefs.edit().putString("selected_language", language.code).apply()
    }
    
    fun getLocalizedString(key: String): String {
        return when (_currentLanguage) {
            AppLanguage.ENGLISH -> getEnglishString(key)
            AppLanguage.HINDI -> getHindiString(key)
        }
    }
    
    private fun getEnglishString(key: String): String {
        return when (key) {
            "settings" -> "Settings"
            "notifications" -> "Notifications"
            "push_notifications" -> "Push Notifications"
            "push_notifications_desc" -> "Receive alerts about water quality issues"
            "email_alerts" -> "Email Alerts"
            "email_alerts_desc" -> "Get email notifications for important updates"
            "privacy_security" -> "Privacy & Security"
            "location_sharing" -> "Location Sharing"
            "location_sharing_desc" -> "Allow location access for accurate reporting"
            "auto_save_reports" -> "Auto Save Reports"
            "auto_save_reports_desc" -> "Automatically save draft reports"
            "appearance" -> "Appearance"
            "dark_mode" -> "Dark Mode"
            "dark_mode_desc" -> "Use dark theme for better visibility"
            "data_management" -> "Data Management"
            "export_data" -> "Export Data"
            "export_data_desc" -> "Download your reports and data"
            "clear_all_data" -> "Clear All Data"
            "clear_all_data_desc" -> "Delete all saved reports and preferences"
            "account" -> "Account"
            "change_password" -> "Change Password"
            "change_password_desc" -> "Update your account password"
            "edit_profile" -> "Edit Profile"
            "edit_profile_desc" -> "Update your personal information"
            "language" -> "Language"
            "language_desc" -> "Choose your preferred language"
            "select_language" -> "Select Language"
            "english" -> "English"
            "hindi" -> "हिंदी"
            
            // Alert Screen
            "alerts" -> "Alert"
            "active_notifications" -> "active notifications"
            "alert_overview" -> "Alert Overview"
            "emergency" -> "Emergency"
            "warnings" -> "Warnings"
            "info" -> "Info"
            "resolved" -> "Resolved"
            "alert_filter" -> "Alert Filter"
            "all" -> "All"
            "alert_settings" -> "Alert Settings"
            "just_now" -> "Just now"
            "hours_ago" -> "h ago"
            "days_ago" -> "d ago"
            
            // Alert Detail Screen
            "alert_details" -> "Alert Details"
            "priority" -> "Priority"
            "status" -> "Status"
            "district" -> "District"
            "location" -> "Location"
            "reporter" -> "Reporter"
            "reporter_contact" -> "Reporter Contact"
            "created_at" -> "Created At"
            "updated_at" -> "Updated At"
            "assigned_to" -> "Assigned To"
            "response_time" -> "Response Time"
            "notes" -> "Notes"
            "attachments" -> "Attachments"
            "close_details" -> "Close Details"
            "contact_reporter" -> "Contact Reporter"
            "view_attachments" -> "View Attachments"
            "no_attachments" -> "No attachments available"
            "no_notes" -> "No notes available"
            
            else -> key
        }
    }
    
    private fun getHindiString(key: String): String {
        return when (key) {
            "settings" -> "सेटिंग्स"
            "notifications" -> "सूचनाएं"
            "push_notifications" -> "पुश सूचनाएं"
            "push_notifications_desc" -> "जल गुणवत्ता मुद्दों के बारे में अलर्ट प्राप्त करें"
            "email_alerts" -> "ईमेल अलर्ट"
            "email_alerts_desc" -> "महत्वपूर्ण अपडेट के लिए ईमेल सूचनाएं प्राप्त करें"
            "privacy_security" -> "गोपनीयता और सुरक्षा"
            "location_sharing" -> "स्थान साझाकरण"
            "location_sharing_desc" -> "सटीक रिपोर्टिंग के लिए स्थान पहुंच की अनुमति दें"
            "auto_save_reports" -> "ऑटो सेव रिपोर्ट्स"
            "auto_save_reports_desc" -> "ड्राफ्ट रिपोर्ट्स को स्वचालित रूप से सेव करें"
            "appearance" -> "दिखावट"
            "dark_mode" -> "डार्क मोड"
            "dark_mode_desc" -> "बेहतर दृश्यता के लिए डार्क थीम का उपयोग करें"
            "data_management" -> "डेटा प्रबंधन"
            "export_data" -> "डेटा निर्यात करें"
            "export_data_desc" -> "अपनी रिपोर्ट्स और डेटा डाउनलोड करें"
            "clear_all_data" -> "सभी डेटा साफ़ करें"
            "clear_all_data_desc" -> "सभी सेव्ड रिपोर्ट्स और प्राथमिकताएं हटाएं"
            "account" -> "खाता"
            "change_password" -> "पासवर्ड बदलें"
            "change_password_desc" -> "अपना खाता पासवर्ड अपडेट करें"
            "edit_profile" -> "प्रोफाइल संपादित करें"
            "edit_profile_desc" -> "अपनी व्यक्तिगत जानकारी अपडेट करें"
            "language" -> "भाषा"
            "language_desc" -> "अपनी पसंदीदा भाषा चुनें"
            "select_language" -> "भाषा चुनें"
            "english" -> "English"
            "hindi" -> "हिंदी"
            
            // Alert Screen
            "alerts" -> "अलर्ट"
            "active_notifications" -> "सक्रिय सूचनाएं"
            "alert_overview" -> "अलर्ट अवलोकन"
            "emergency" -> "आपातकाल"
            "warnings" -> "चेतावनी"
            "info" -> "जानकारी"
            "resolved" -> "हल"
            "alert_filter" -> "अलर्ट फिल्टर"
            "all" -> "सभी"
            "alert_settings" -> "अलर्ट सेटिंग्स"
            "just_now" -> "अभी"
            "hours_ago" -> "घंटे पहले"
            "days_ago" -> "दिन पहले"
            
            // Alert Detail Screen
            "alert_details" -> "अलर्ट विवरण"
            "priority" -> "प्राथमिकता"
            "status" -> "स्थिति"
            "district" -> "जिला"
            "location" -> "स्थान"
            "reporter" -> "रिपोर्टर"
            "reporter_contact" -> "रिपोर्टर संपर्क"
            "created_at" -> "बनाया गया"
            "updated_at" -> "अपडेट किया गया"
            "assigned_to" -> "असाइन किया गया"
            "response_time" -> "प्रतिक्रिया समय"
            "notes" -> "नोट्स"
            "attachments" -> "अटैचमेंट्स"
            "close_details" -> "विवरण बंद करें"
            "contact_reporter" -> "रिपोर्टर से संपर्क करें"
            "view_attachments" -> "अटैचमेंट्स देखें"
            "no_attachments" -> "कोई अटैचमेंट उपलब्ध नहीं"
            "no_notes" -> "कोई नोट्स उपलब्ध नहीं"
            
            else -> key
        }
    }
}
