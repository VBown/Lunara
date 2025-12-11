package com.example.lunara.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lunara.viewmodel.TrackingViewModel
import com.example.lunara.ui.components.CheckboxRow
import com.example.lunara.ui.components.QuestionTitle
import com.example.lunara.ui.components.TrackingSlider


@Composable
fun TrackingScreen(viewModel: TrackingViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    val saveStatus by viewModel.saveStatus.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar Snackbar cuando saveStatus cambia
    LaunchedEffect(saveStatus) {
        saveStatus?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSaveStatus() // Limpiar el estado
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text("Registro Diario",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center)

                    Text("¿Cómo te sientes hoy?",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center)
                    Spacer(Modifier.height(16.dp))
                }

                // Nivel de Energía
                item {
                    TrackingSlider(
                        title = "Nivel de Energía (1=Bajo, 5=Alto)",
                        value = viewModel.energyLevel
                    ) { viewModel.energyLevel = it }
                }

                // Calidad de Sueño
                item {
                    TrackingSlider(
                        title = "Calidad de Sueño (1=Mala, 5=Excelente)",
                        value = viewModel.sleepQuality
                    ) { viewModel.sleepQuality = it }
                }

                // Síntomas
                item {
                    QuestionTitle("¿Síntomas de hoy?")
                    Spacer(Modifier.height(8.dp))
                }
                items(viewModel.symptomsList) { sintoma ->
                    CheckboxRow(
                        label = sintoma,
                       viewModel.symptoms[sintoma] ?: false
                    ) { viewModel.onSymptomToggled(sintoma) }
                }

                // Botón de Guardar
                item {
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { viewModel.saveLog() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium) {
                        Text("Guardar Registro del Día")
                    }
                    Spacer(Modifier.height(16.dp)) // Espacio al final
                }
            }
        }
    }
}