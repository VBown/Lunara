package com.example.lunara.data

import kotlinx.serialization.Serializable

@Serializable
data class DailyTask(
    val title: String,
    val description: String,
    val iconName: String // "Nutrición", "Ejercicio", "Bienestar"
)
