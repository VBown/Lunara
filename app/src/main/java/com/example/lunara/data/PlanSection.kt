package com.example.lunara.data
data class PlanSection(
    val titulo: String, // Ej: "Nutrición", "Ejercicio"
    val recomendaciones: List<String>,
    val mensajeMotivacional: String
)