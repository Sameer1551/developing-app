package com.example.myapplication.models

import java.util.Date

data class WaterQualityReport(
    val id: Long = 0,
    val fullName: String,
    val email: String,
    val phone: String,
    val waterSourceName: String,
    val sourceType: String,
    val location: String,
    val coordinates: String,
    val waterAppearance: String,
    val waterSmell: String,
    val waterTaste: String,
    val visibleParticles: String,
    val waterFlow: String,
    val generalHealthIssues: String,
    val skinProblems: String,
    val stomachProblems: String,
    val additionalNotes: String,
    val photoCount: Int,
    val submittedAt: Date = Date(),
    val status: String = "Pending" // Pending, Reviewed, Resolved
)
