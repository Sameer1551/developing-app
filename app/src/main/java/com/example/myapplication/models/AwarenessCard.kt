package com.example.myapplication.models

data class AwarenessCard(
    val id: String,
    val title: String,
    val category: String,
    val type: String,
    val difficulty: String,
    val duration: String,
    val rating: Double,
    val views: Int,
    val description: String,
    val tags: List<String>
)
