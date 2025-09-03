package com.example.myapplication.models

data class WaterQualityReport(
    // Personal Information
    val fullName: String,
    val email: String,
    val phone: String,
    val date: String,
    
    // Water Source Information
    val waterSourceName: String,
    val sourceType: String,
    val location: String,
    val coordinates: String? = null,
    
    // Water Observations
    val waterAppearance: String,
    val waterSmell: String,
    val waterTaste: String,
    val visibleParticles: String,
    val waterFlow: String,
    
    // Health Concerns (Optional)
    val generalHealthIssues: String? = null,
    val skinProblems: String? = null,
    val stomachProblems: String? = null,
    
    // Additional Information
    val additionalNotes: String? = null,
    val photos: List<String> = emptyList()
)
