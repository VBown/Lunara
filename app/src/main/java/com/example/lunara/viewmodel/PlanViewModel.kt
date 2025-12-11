package com.example.lunara.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunara.data.PlanBase
import com.example.lunara.data.UserProfile
import com.example.lunara.repository.PlanRepository
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

sealed class PlanUiState {
    data object Loading : PlanUiState()
    data class Success(val plan: PlanBase, val userName: String) : PlanUiState()
    data class Error(val message: String) : PlanUiState()
}

class PlanViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<PlanUiState>(PlanUiState.Loading)
    val uiState: StateFlow<PlanUiState> = _uiState.asStateFlow()

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore

    init {
        loadUserPlan()
    }

    private fun loadUserPlan() {
        viewModelScope.launch {
            _uiState.value = PlanUiState.Loading
            try {
                val uid = auth.currentUser?.uid
                if (uid == null) {
                    _uiState.value = PlanUiState.Error("Usuaria no encontrada.")
                    return@launch
                }

                // 1. Cargar el perfil de la usuaria desde Firestore
                val document = db.collection("users").document(uid).get().await()
                val userProfile = document.toObject<UserProfile>()

                if (userProfile == null) {
                    _uiState.value = PlanUiState.Error("Perfil no encontrado.")
                    return@launch
                }

                // 2. Obtener el ID del plan asignado
                val planId = userProfile.assignedPlanId

                // 3. Cargar el plan base desde el repositorio local
                val plan = PlanRepository.getPlanById(planId)

                if (plan == null) {
                    _uiState.value = PlanUiState.Error("Plan no encontrado (ID: $planId).")
                    return@launch
                }

                // 4. Éxito: Enviar el plan y el nombre a la UI
                _uiState.value = PlanUiState.Success(plan, userProfile.nombre)

            } catch (e: Exception) {
                Log.e("PlanViewModel", "Error al cargar el plan", e)
                _uiState.value = PlanUiState.Error("Error al cargar tu plan: ${e.message}")
            }
        }
    }
}