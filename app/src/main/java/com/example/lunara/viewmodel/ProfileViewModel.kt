package com.example.lunara.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunara.data.DailyLog
import com.example.lunara.data.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val profile: UserProfile, val logs: List<DailyLog>) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore
    private val uid: String get() = auth.currentUser?.uid ?: ""

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            if (uid.isBlank()) {
                _uiState.value = ProfileUiState.Error("Usuaria no autenticada.")
                return@launch
            }
            try {
                // 1. Cargar el perfil
                val profileSnapshot = db.collection("users").document(uid).get().await()
                val profile = profileSnapshot.toObject<UserProfile>()

                if (profile == null) {
                    _uiState.value = ProfileUiState.Error("No se pudo cargar el perfil.")
                    return@launch
                }

                // 2. Cargar TODOS los registros diarios (ordenados por fecha, los 30 más recientes)
                val logsSnapshot = db.collection("users").document(uid).collection("dailyLogs")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(30)
                    .get()
                    .await()

                val logs = logsSnapshot.map { it.toObject<DailyLog>() }

                // 3. Emitir estado de éxito con ambos
                _uiState.value = ProfileUiState.Success(profile, logs)

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error al cargar datos del perfil", e)
                _uiState.value = ProfileUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    // Función para cerrar sesión
    fun signOut(onComplete: () -> Unit) {
        viewModelScope.launch {
            auth.signOut()
            onComplete()
        }
    }
}

