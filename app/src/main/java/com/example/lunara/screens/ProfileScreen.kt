package com.example.lunara.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunara.data.DailyLog
import com.example.lunara.data.UserProfile
import com.example.lunara.ui.theme.CoralEnergetico
import com.example.lunara.viewmodel.ProfileUiState
import com.example.lunara.viewmodel.ProfileViewModel
import java.util.Locale


@Composable
fun ProfileScreen(viewModel: ProfileViewModel, onSignOut: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Text("Mi Perfil y Progreso",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary)
        }

        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                item {
                    Spacer(Modifier.height(32.dp))
                    CircularProgressIndicator()
                }
            }
            is ProfileUiState.Error -> {
                item {
                    Text(text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error)
                }
            }
            is ProfileUiState.Success -> {
                val profile = state.profile
                val logs = state.logs

                // --- Sección de Perfil ---
                item {
                    ProfileInfoCard(profile = profile)
                }

                // --- Sección de Progreso (Gráficos) ---
                item {
                    ProgressCard(logs = logs)
                }

                // --- Botón de Cerrar Sesión ---
                item {
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.signOut(onSignOut) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CoralEnergetico)
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoCard(profile: UserProfile) {
    Card(modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)) {
        Column(modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(profile.nombre,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primaryContainer)
            Text("Etapa: ${profile.etapaHormonal}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Plan Asignado: #${profile.assignedPlanId}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Objetivo Principal: ${profile.objetivos.firstOrNull() ?: "No definido"}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary)
            // Aquí se podría añadir un botón para "Editar Perfil"
        }
    }
}

@Composable
fun ProgressCard(logs: List<DailyLog>) {
    Card(modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)) {
        Column(modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Tu Progreso",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primaryContainer)

            if (logs.isEmpty()) {
                Text("Aún no hay suficientes datos para mostrar tu progreso. ¡Sigue registrando!", style = MaterialTheme.typography.bodyMedium)
            } else {
                // Promedios
                val avgEnergy = logs.map { it.energyLevel }.average()
                val avgSleep = logs.map { it.sleepQuality }.average()
                Text("Energía (Promedio): ${String.format(Locale.US, "%.1f", avgEnergy)} / 5",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Sueño (Promedio): ${String.format(Locale.US, "%.1f", avgSleep)} / 5",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(Modifier.height(16.dp))

                // Gráfico simple de los últimos 7 días de energía
                Text("Energía (Últimos 7 días):",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface)
                val energyData = logs.take(7).map { it.energyLevel }.reversed() // Tomamos los 7 más recientes y los invertimos
                SimpleBarChart(data = energyData)
            }
        }
    }
}

// --- COMPOSABLE PARA GRÁFICO SIMPLE ---
@Composable
fun SimpleBarChart(data: List<Int>) {
    val maxVal = 5f // Nuestro valor máximo es 5

    Row(
        modifier = Modifier.fillMaxWidth().height(120.dp).padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { value ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(((value / maxVal) * 100).dp) // Altura proporcional
                        .background(MaterialTheme.colorScheme.primary)
                        .clip(MaterialTheme.shapes.small)
                )
                Text(text = "$value", fontSize = 12.sp)
            }
        }
    }
}
