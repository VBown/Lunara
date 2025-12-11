package com.example.lunara.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunara.data.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OnboardingViewModel : ViewModel() {
    // 0: Nombre, 1: Datos Básicos, 2: Etapa/Síntomas, 3: Emocional, 4: Hábitos, 5: Objetivos, 6: Resumen
    var currentStep by mutableIntStateOf(0)

    // Instancias de Firebase
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore

    // --- Estados para todas las respuestas del cuestionario ---
    var nombre by mutableStateOf("")
    // Datos Básicos
    var edad by mutableStateOf("")
    var peso by mutableStateOf("")
    var estatura by mutableStateOf("")
    var ultimaMenstruacion by mutableStateOf("")
    var tieneMenopausia by mutableStateOf(false)
    // Estado Hormonal y Síntomas [cite: 41-43]
    var etapaHormonal by mutableStateOf("Perimenopausia")
    val sintomas = mutableStateMapOf<String, String>() // Ej: "Sofocos" -> "Leve"
    // Estado Emocional [cite: 46-54]
    var estadoAnimoGeneral by mutableStateOf("Muy bueno y estable")
    var ansiedadPersistente by mutableStateOf(false)
    var calidadSueno by mutableFloatStateOf(3f) // Slider de 1 a 5
    var tecnicasEstres by mutableStateOf("Nada")
    // Hábitos de Vida [cite: 56-72]
    var frecuenciaActividadFisica by mutableStateOf("Ninguna")
    var tipoEjercicio by mutableStateOf("Caminata")
    var calidadAlimentacion by mutableStateOf("Regular")
    var consumoAlcohol by mutableStateOf("Nunca")
    var fuma by mutableStateOf(false)
    // Objetivos
    val objetivos = mutableStateListOf<String>()
    var condicionesMedicas by mutableStateOf("")

    val totalSteps = 6 // 0 a 6

    fun nextStep() { if (currentStep < totalSteps) currentStep++ }
    fun previousStep() { if (currentStep > 0) currentStep-- }

    fun onSintomaChanged(sintoma: String, intensidad: String) {
        if (intensidad == "Ninguno") {
            sintomas.remove(sintoma)
        } else {
            sintomas[sintoma] = intensidad
        }
    }

    fun onObjetivoToggled(objetivo: String) {
        if (objetivos.contains(objetivo)) {
            objetivos.remove(objetivo)
        } else {
            objetivos.add(objetivo)
        }
    }

    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            // --- 1. DERIVAR variables de clasificación ---

            // Lógica de Severidad
            val severidad = when {
                sintomas.containsValue("Severo") -> "Severo"
                sintomas.containsValue("Moderado") -> "Moderado"
                else -> "Leve"
            }

            // Lógica de Estado Emocional
            val emocional = if (estadoAnimoGeneral == "Frecuentemente ansiosa o deprimida" || ansiedadPersistente) {
                "Vulnerable"
            } else {
                "Estable"
            }

            // Lógica de Actividad Física
            val actividad = when (frecuenciaActividadFisica) {
                "Ninguna", "1-2 veces por semana" -> "Baja"
                "3-4 veces por semana" -> "Moderada"
                "5 o más veces por semana" -> "Alta"
                else -> "Baja"
            }

            // --- 2. Asignar Plan ID ---
            val planId = assignPlanId(
                etapaHormonal = etapaHormonal,
                severidad = severidad,
                emocional = emocional,
                actividad = actividad
            )

            val uid = auth.currentUser?.uid
            if (uid == null) {
                Log.e("OnboardingViewModel", "Error: Usuario no autenticado.")
                return@launch
            }

            // --- 3. Crear Perfil Completo ---
            val userProfile = UserProfile(
                uid = uid,
                nombre = nombre,
                edad = edad.toIntOrNull() ?: 45,
                peso = peso,
                estatura = estatura,
                ultimaMenstruacion = ultimaMenstruacion,
                tieneMenopausia = tieneMenopausia,
                etapaHormonal = etapaHormonal,
                sintomas = sintomas.toMap(), // Convertir a mapa inmutable
                estadoAnimoGeneral = estadoAnimoGeneral,
                ansiedadPersistente = ansiedadPersistente,
                calidadSueno = calidadSueno.toInt(),
                tecnicasEstres = tecnicasEstres,
                frecuenciaActividadFisica = frecuenciaActividadFisica,
                tipoEjercicio = tipoEjercicio,
                calidadAlimentacion = calidadAlimentacion,
                consumoAlcohol = consumoAlcohol,
                fuma = fuma,
                objetivos = objetivos.toList(), // Convertir a lista inmutable
                condicionesMedicas = condicionesMedicas,
                assignedPlanId = planId
            )

            // --- 4. Guardar en Firebase ---
            saveProfileToFirebase(userProfile)
            onComplete()
        }
    }

    private fun assignPlanId(etapaHormonal: String, severidad: String, emocional: String, actividad: String): Int {
        // Lógica de asignación de planes [cite: 257-327]
        return when (etapaHormonal) {
            "Perimenopausia" -> when (severidad) {
                "Severo" if emocional == "Vulnerable" && actividad == "Baja" -> 3
                "Moderado" if emocional == "Vulnerable" -> 2
                else -> 1
            }
            "Menopausia" -> when (severidad) {
                "Severo" if emocional == "Vulnerable" && actividad == "Baja" -> 6
                "Moderado" if emocional == "Vulnerable" -> 5
                else -> 4
            }
            "Postmenopausia" -> when (severidad) {
                "Severo" if emocional == "Vulnerable" && actividad == "Baja" -> 9
                "Moderado" if emocional == "Vulnerable" -> 8
                else -> 7
            }
            else -> 1
        }
    }

    private suspend fun saveProfileToFirebase(profile: UserProfile) {
        try {
            db.collection("users").document(profile.uid).set(profile).await()
            Log.d("OnboardingViewModel", "¡Perfil COMPLETO guardado en Firebase! Plan ID: ${profile.assignedPlanId}")
        } catch (e: Exception) {
            Log.e("OnboardingViewModel", "Error al guardar en Firebase", e)
        }
    }
}
