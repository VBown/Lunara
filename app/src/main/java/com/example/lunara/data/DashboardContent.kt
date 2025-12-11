package com.example.lunara.data

import kotlinx.serialization.Serializable


@Serializable
data class DashboardContent(
    val greeting: String = "Cargando saludo...",
    val dailyPlan: List<DailyTask> = emptyList()
)