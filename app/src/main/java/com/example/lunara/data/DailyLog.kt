package com.example.lunara.data

data class DailyLog(
    val date: String = "", // ID del documento (ej: "2025-11-14")
    val mood: String = "Estable",
    val sleepQuality: Int = 3,
    val energyLevel: Int = 3,
    val symptoms: List<String> = emptyList()
    // Podemos añadir un campo "note: String" si queremos un diario
)