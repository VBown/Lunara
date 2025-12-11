package com.example.lunara.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lunara.viewmodel.DashboardUiState
import com.example.lunara.viewmodel.DashboardViewModel
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.lunara.R
import com.example.lunara.data.DailyTask


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        // Header personalizado con el logo horizontal
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // Logo horizontal en lugar de texto
                    Image(
                        painter = painterResource(id = R.drawable.logo_horizontal),
                        contentDescription = "Lunara",
                        modifier = Modifier.height(40.dp),
                        contentScale = ContentScale.Fit
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                when (val state = uiState) {
                    is DashboardUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                        Text("Conectando con tu energía...", modifier = Modifier.padding(top = 16.dp))
                    }
                    is DashboardUiState.Error -> {
                        Text(
                            text = "Ups, algo pasó: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                    is DashboardUiState.Success -> {
                        // Saludo con estilo Lora (Headline)
                        Text(
                            text = state.content.greeting,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(Modifier.height(32.dp))

                        if (state.content.dailyPlan.isNotEmpty()) {
                            Text(
                                text = "Tu plan para hoy",
                                style = MaterialTheme.typography.titleLarge, // Poppins SemiBold
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.secondary // Lila
                            )
                            Spacer(Modifier.height(16.dp))

                            state.content.dailyPlan.forEach { task ->
                                DailyTaskCard(task = task)
                                Spacer(Modifier.height(12.dp))
                            }
                        } else {
                            Text("Aquí verás tu plan diario y progreso.", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyTaskCard(task: DailyTask) {
    val icon = when (task.iconName.lowercase()) {
        "nutrición" -> Icons.Default.Fastfood
        "ejercicio" -> Icons.AutoMirrored.Filled.DirectionsRun
        else -> Icons.Default.SelfImprovement
    }

    // Card con el estilo Lunara
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp), // Elevación suave
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono en Dorado Suave (tertiary)
            Icon(
                imageVector = icon,
                contentDescription = task.title,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
