package com.example.lunara.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lunara.data.PlanSection
import com.example.lunara.ui.theme.VerdeSalvia
import com.example.lunara.viewmodel.PlanUiState
import com.example.lunara.viewmodel.PlanViewModel


@Composable
fun PlanScreen(viewModel: PlanViewModel) {
    // Recolectamos el estado desde el ViewModel
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Manejamos los diferentes estados de la UI
        when (val state = uiState) {
            is PlanUiState.Loading -> {
                item {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                }
            }

            is PlanUiState.Error -> {
                item {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is PlanUiState.Success -> {
                // --- ÉXITO: Mostramos el plan ---
                item {
                    Text(
                        "¡Hola, ${state.userName}!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    Text(
                        state.plan.titulo,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                item {
                    Text(
                        "Enfoque: ${state.plan.enfoquePrincipal}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center
                   )
                }

                // Mostramos cada sección del plan en una Card
                items(state.plan.secciones) { seccion ->
                    PlanSectionCard(seccion)
                }
            }
        }
    }
}

@Composable
fun PlanSectionCard(seccion: PlanSection) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = seccion.titulo,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            seccion.recomendaciones.forEach { recomendacion ->
                Text(
                    text = "• $recomendacion",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = seccion.mensajeMotivacional,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = VerdeSalvia
            )
        }
    }
}