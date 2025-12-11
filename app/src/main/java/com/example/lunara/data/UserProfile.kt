package com.example.lunara.data

data class UserProfile(
    val uid: String = "", // ID de Firebase Auth
    val nombre: String = "",
    // Datos Básicos
    val edad: Int = 40,
    val peso: String = "",
    val estatura: String = "",
    val ultimaMenstruacion: String = "",
    val tieneMenopausia: Boolean = false,
    // Estado Hormonal y Síntomas
    val etapaHormonal: String = "Perimenopausia",
    val sintomas: Map<String, String> = emptyMap(), // Ej: "Sofocos" to "Leve"
    // Estado Emocional
    val estadoAnimoGeneral: String = "Muy bueno y estable",
    val ansiedadPersistente: Boolean = false,
    val calidadSueno: Int = 5, // Escala 1-5
    val tecnicasEstres: String = "Ninguna",
    // Hábitos de Vida
    val frecuenciaActividadFisica: String = "Ninguna",
    val tipoEjercicio: String = "Caminata",
    val calidadAlimentacion: String = "Regular",
    val consumoAlcohol: String = "Nunca",
    val fuma: Boolean = false,
    // Objetivos
    val objetivos: List<String> = emptyList(),
    val condicionesMedicas: String = "",
    // Plan Asignado
    val assignedPlanId: Int = 1
)