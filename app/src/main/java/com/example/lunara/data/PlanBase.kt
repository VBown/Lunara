package com.example.lunara.data

data class PlanBase(
    val id: Int,
    val titulo: String,
    val enfoquePrincipal: String,
    val secciones: List<PlanSection>
)