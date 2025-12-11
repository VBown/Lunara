package com.example.lunara.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunara.data.DailyLog
import com.google.firebase.Firebase
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

class TrackingViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore
    private val uid: String get() = auth.currentUser?.uid ?: ""
    private val todayDate: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // --- Estados de la UI ---
    var energyLevel by mutableFloatStateOf(3f)
    var sleepQuality by mutableFloatStateOf(3f)
    val symptoms = mutableStateMapOf<String, Boolean>()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _saveStatus = MutableStateFlow<String?>(null)
    val saveStatus: StateFlow<String?> = _saveStatus.asStateFlow()

    val symptomsList = listOf("Sofocos", "Ansiedad", "Insomnio", "Fatiga", "Cambios de humor")

    init {
        loadLogForToday()
    }

    /** Carga el registro de hoy si ya existe */
    private fun loadLogForToday() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val docRef = db.collection("users").document(uid).collection("dailyLogs").document(todayDate)
                val snapshot = docRef.get().await()
                if (snapshot.exists()) {
                    val log = snapshot.toObject<DailyLog>()
                    if (log != null) {
                        energyLevel = log.energyLevel.toFloat()
                        sleepQuality = log.sleepQuality.toFloat()
                        symptoms.clear()
                        log.symptoms.forEach { symptoms[it] = true }
                    }
                }
            } catch (e: Exception) {
                Log.e("TrackingViewModel", "Error loading today's log", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Guarda el registro del día en Firestore */
    fun saveLog() {
        viewModelScope.launch {
            try {
                val log = DailyLog(
                    date = todayDate,
                    mood = "Estable", // Podríamos añadir un slider para esto
                    energyLevel = energyLevel.toInt(),
                    sleepQuality = sleepQuality.toInt(),
                    symptoms = symptoms.filter { it.value }.keys.toList()
                )

                val docRef = db.collection("users").document(uid).collection("dailyLogs").document(todayDate)
                docRef.set(log).await()
                _saveStatus.value = "¡Registro guardado con éxito!"
            } catch (e: Exception) {
                Log.e("TrackingViewModel", "Error saving log", e)
                _saveStatus.value = "Error al guardar el registro."
            }
        }
    }

    fun onSymptomToggled(symptom: String) {
        symptoms[symptom] = !(symptoms[symptom] ?: false)
    }

    fun clearSaveStatus() {
        _saveStatus.value = null
    }
}