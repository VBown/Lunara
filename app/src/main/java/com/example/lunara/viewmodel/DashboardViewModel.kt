package com.example.lunara.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunara.data.DailyLog
import com.example.lunara.data.DashboardContent
import com.example.lunara.data.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerationConfig
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.serialization.json.Json
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val content: DashboardContent) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore
    private val uid: String get() = auth.currentUser?.uid ?: ""
    private val todayDate: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Modelo de IA de Gemini
    // Definimos la configuración de generación para que devuelva JSON

    private val jsonGenerationConfig = GenerationConfig.Builder()
        .setResponseMimeType("application/json")
        .build()

    // Inicializa el objeto Json para la deserialización
    private val json = Json {
        ignoreUnknownKeys = true // Útil por si el modelo genera campos extra
        prettyPrint = true // Para depuración, puedes quitarlo en producción
        isLenient = true // Para ser más tolerante con el formato JSON
    }

    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            "gemini-2.5-flash",
            generationConfig = jsonGenerationConfig
        )

    init {
        refreshDashboardContent()
    }

    fun refreshDashboardContent() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                // 1. Cargar el perfil de la usuaria (crítico)
                val userProfile = loadUserProfile()
                if (userProfile == null) {
                    _uiState.value = DashboardUiState.Error("Perfil no encontrado.")
                    return@launch
                }

                // 2. Cargar el registro de hoy (opcional)
                val dailyLog = loadDailyLog()

                // 3. Comprobar si hay *algún* registro previo
                val hasPreviousLogs = hasAnyLogs()

                // 4. Generar el contenido del dashboard (saludo Y plan diario)
                val dashboardContent = generateDashboardContent(userProfile, dailyLog, hasPreviousLogs)
                _uiState.value = DashboardUiState.Success(dashboardContent)

            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error al cargar el dashboard", e)
                _uiState.value = DashboardUiState.Error("Error: ${e.localizedMessage ?: "No se pudo cargar el dashboard"}")
            }
        }
    }

    // Función separada para cargar el perfil
    private suspend fun loadUserProfile(): UserProfile? {
        if (uid.isBlank()) return null
        return try {
            db.collection("users").document(uid).get().await().toObject<UserProfile>()
        } catch (e: Exception) {
            Log.e("DashboardViewModel", "Error al cargar perfil", e)
            null
        }
    }

    // Función separada para cargar el registro de hoy
    private suspend fun loadDailyLog(): DailyLog? {
        if (uid.isBlank()) return null
        return try {
            db.collection("users").document(uid).collection("dailyLogs").document(todayDate).get().await().toObject<DailyLog>()
        } catch (e: Exception) {
            Log.e("DashboardViewModel", "Error al cargar registro diario", e)
            null
        }
    }

    // Comprueba si la colección 'dailyLogs' tiene algún documento
    private suspend fun hasAnyLogs(): Boolean {
        if (uid.isBlank()) return false
        return try {
            val logCollection = db.collection("users").document(uid).collection("dailyLogs").limit(1).get().await()
            !logCollection.isEmpty
        } catch (e: Exception) {
            Log.e("DashboardViewModel", "Error al comprobar logs previos", e)
            false
        }
    }

    // Función de IA usando el registro diario
    private suspend fun generateDashboardContent(profile: UserProfile, log: DailyLog?, hasPreviousLogs: Boolean): DashboardContent {

        val primerObjetivo = profile.objetivos.firstOrNull() ?: "sentirse mejor"

        // El prompt ahora es más complejo y pide JSON
        val prompt = when {
            // --- CASO 1: SÍ registró hoy ---
            log != null ->{
          """
            Eres 'Lunara', una asesora virtual empática, motivadora, experta en el bienestar y la salud de mujeres 40+
            La usuaria es ${profile.nombre}, en etapa de ${profile.etapaHormonal}.
            Su objetivo principal es: "$primerObjetivo".
            Ella ya registró sus datos de hoy:
            - Nivel de Energía: ${log.energyLevel}/5
            - Calidad de Sueño: ${log.sleepQuality}/5
            - Síntomas reportados: ${if (log.symptoms.isNotEmpty()) log.symptoms.joinToString(", ") else "ninguno"}.
            
            Genera un JSON con:
            1. Un "greeting" (saludo) preciso que reconozca cómo se siente (ej: "Veo que hoy tu energía está baja...").
            2. Un "dailyPlan" (lista de 3 tareas) con consejos prácticos breves para hoy, relacionados con su registro.
               Cada tarea debe tener "title", "description" y un "iconName" ("Nutrición", "Ejercicio" o "Bienestar").
            Asegúrate de que la respuesta sea un JSON válido y completo.
            No uses emojis.
            """.trimIndent()
        }
            // --- CASO 2: Es el "Día 1" (no hay log de hoy Y no hay logs previos) ---
            !hasPreviousLogs -> {
                """
                Eres 'Lunara', una asesora virtual empática, motivadora, experta en el bienestar y la salud de mujeres 40+
                La usuaria es ${profile.nombre}. Es su primer día en la app.
                Su objetivo principal es: "$primerObjetivo".

                Genera un JSON con:
                1. Un "greeting" (saludo) que diga: "¡Hola ${profile.nombre}! Qué alegría tenerte aquí. Veo que tu objetivo es '$primerObjetivo'. ¡Es un gran primer paso!".
                2. Un "dailyPlan" (lista de 3 tareas) que la inviten a explorar la app:
                   - title: "Explora tu Plan Base", description: "Ve a la pestaña 'Plan' para ver el plan que hemos creado para ti.", iconName: "Bienestar"
                   - title: "Haz tu primer registro", description: "Ve a 'Registro Diario' para contarnos cómo te sientes hoy.", iconName: "Bienestar"
                   - title: "Saluda a Lunara", description: "Ve al 'Chatbot' si tienes cualquier duda. ¡Estoy aquí para ti!", iconName: "Bienestar"
                Asegúrate de que la respuesta sea un JSON válido y completo.
                No uses emojis.
                """.trimIndent()
            }

            // --- CASO 3: Es el "Día 2+" (no hay log de hoy, PERO sí hay logs previos) ---
            else -> {
                """
                Eres 'Lunara', una asesora virtual empática, motivadora, experta en el bienestar y la salud de mujeres 40+
                Saluda a la usuaria por su nombre: ${profile.nombre}.
                Su objetivo principal es: "$primerObjetivo".
                AÚN NO HA REGISTRADO SUS DATOS DE HOY.
                
                Genera un JSON con:
                1. Un "greeting" (saludo) que diga: "¡Hola ${profile.nombre}! Qué bueno verte de nuevo. Tu objetivo es '$primerObjetivo'. Para poder ayudarte a conseguirlo, no olvides registrar tus datos de hoy en la pestaña 'Registro Diario'."
                2. Un "dailyPlan" (lista de 2 tareas) con consejos generales del día:
                   - title: "Hidratación", description: "Recuerda hidratarte bien hoy.", iconName: "Nutrición"
                   - title: "Pausa Activa", description: "Toma una pausa de 5 minutos para respirar.", iconName: "Bienestar"
                Asegúrate de que la respuesta sea un JSON válido y completo.
                No uses emojis.
                """.trimIndent()
            }
        }

        try {
            // 4. Generamos contenido y lo convertimos a nuestra data class
            val response = model.generateContent(prompt)
            val jsonString = response.text // Obtenemos la respuesta como String

            if (jsonString != null) {
            //5. Deserializamos el JSON manualmente
                return json.decodeFromString<DashboardContent>(jsonString)
            } else {
                Log.w("DashboardViewModel", "Gemini no devolvió contenido JSON.")
                return DashboardContent(greeting = "No se pudo obtener respuesta", dailyPlan = emptyList())
            }
        } catch (e: Exception) {
            Log.e("DashboardViewModel", "Error al generar saludo de Gemini", e)
            throw e
        }
    }
}

