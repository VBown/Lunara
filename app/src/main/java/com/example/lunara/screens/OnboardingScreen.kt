package com.example.lunara.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lunara.ui.components.LunaraTextField
import com.example.lunara.ui.components.QuestionTitle
import com.example.lunara.ui.components.RadioGroup
import com.example.lunara.viewmodel.OnboardingViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, onOnboardingComplete: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Bienvenida a Lunara",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = { if (viewModel.currentStep > 0)
                    IconButton(onClick = { viewModel.previousStep() })
                    { Icon(Icons
                        .AutoMirrored.Filled.ArrowBack,
                        "Atrás",
                        tint = MaterialTheme.colorScheme.secondary) } },
                colors = TopAppBarDefaults
                    .topAppBarColors(containerColor = MaterialTheme
                        .colorScheme.background)
            )
        },
        bottomBar = {
            Row(Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(progress = (viewModel.currentStep + 1) / viewModel.totalSteps.toFloat(),
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(MaterialTheme.shapes.small),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme
                        .colorScheme.secondary
                        .copy(alpha = 0.3f))
                Spacer(Modifier.width(16.dp))
                Button(onClick = { if (viewModel
                    .currentStep == viewModel.totalSteps - 1)
                    viewModel.completeOnboarding(onOnboardingComplete) else viewModel.nextStep() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium) {
                    Text(if (viewModel.currentStep == viewModel.totalSteps - 1) "Comenzar" else "Siguiente")
                }
            }
        }
    ) { padding ->
        LazyColumn(Modifier
            .fillMaxSize()
            .padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            item {
                Spacer(Modifier.height(16.dp))
                when (viewModel.currentStep) {
                    0 -> OnboardingStep_Nombre(viewModel)
                    1 -> OnboardingStep_DatosBasicos(viewModel)
                    2 -> OnboardingStep_Sintomas(viewModel)
                    3 -> OnboardingStep_Emocional(viewModel)
                    4 -> OnboardingStep_Habitos(viewModel)
                    5 -> OnboardingStep_Objetivos(viewModel)
                    6 -> OnboardingStep_Resumen(viewModel)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// --- PASOS DEL CUESTIONARIO ---

@Composable
fun OnboardingStep_Nombre(viewModel: OnboardingViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(
            "Hola",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary // Coral Energético
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Empecemos por tu nombre",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary // Lila Sereno
        )
        Spacer(Modifier.height(24.dp))

        // TextField Redondeado
        LunaraTextField(
            value = viewModel.nombre,
            onValueChange = { viewModel.nombre = it },
            label = "Tu nombre")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingStep_DatosBasicos(viewModel: OnboardingViewModel) {
    val calendar = Calendar.getInstance()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Cuéntanos un poco sobre ti",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(Modifier.height(16.dp))

        // TextFields Redondeados
        LunaraTextField(
            value = viewModel.edad,
            onValueChange = { viewModel.edad = it.filter { c -> c.isDigit() } },
            label = "¿Cuál es tu edad actual?",
            keyboardType = KeyboardType.Number
        )

        LunaraTextField(
            value = viewModel.peso,
            onValueChange = { viewModel.peso = it },
            label = "¿Cuál es tu peso actual?",
            keyboardType = KeyboardType.Number)

        LunaraTextField(
            value = viewModel.estatura,
            onValueChange = { viewModel.estatura = it },
            label = "¿Cuál es tu estatura?",
            keyboardType = KeyboardType.Number)

        OutlinedTextField(
            value = viewModel.ultimaMenstruacion,
            onValueChange = {},
            label = { Text("Última menstruación") },
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.DateRange,
                "",
                modifier = Modifier.clickable { showDatePicker = true },
                tint = MaterialTheme.colorScheme.primary) },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary)
        )
    }

    if (showDatePicker) {
        DatePickerDialog(onDismissRequest = { showDatePicker = false },
            confirmButton = { Button(onClick = { showDatePicker = false
                datePickerState.selectedDateMillis?.let { viewModel.ultimaMenstruacion = formatter.format(Date(it)) } }) { Text("Aceptar") } }) { DatePicker(state = datePickerState) }
    }
}

@Composable
fun OnboardingStep_Sintomas(viewModel: OnboardingViewModel) {
    val sintomas = listOf("Sofocos", "Sudoración nocturna", "Cambios de humor", "Insomnio", "Fatiga", "Aumento de Peso")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Estado Hormonal y Síntomas",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(16.dp))
        QuestionTitle("¿En cuál de estas etapas consideras que estás?")
        RadioGroup(listOf("Perimenopausia", "Menopausia", "Postmenopausia"),
            viewModel.etapaHormonal) { viewModel.etapaHormonal = it }
        Spacer(Modifier.height(16.dp))
        QuestionTitle("Califica la intensidad de tus síntomas:")
        sintomas.forEach { s ->
            Text(s,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onBackground)
            RadioGroup(listOf(
                "Ninguno",
                "Leve",
                "Moderado",
                "Severo"),
                viewModel.sintomas[s] ?: "Ninguno",
                true) { i -> viewModel.onSintomaChanged(s,
                i) }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun OnboardingStep_Emocional(viewModel: OnboardingViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Bienestar Emocional",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(16.dp))

        QuestionTitle("¿Cómo describirías tu estado de ánimo general?")
        RadioGroup(
           listOf("Muy bueno y estable", "A veces ansiosa o irritable", "Frecuentemente ansiosa o deprimida"),
            viewModel.estadoAnimoGeneral) { viewModel.estadoAnimoGeneral = it }

        QuestionTitle("¿Has experimentado ansiedad o tristeza persistente?")
        RadioGroup(
          listOf("Sí", "No"),
            if (viewModel.ansiedadPersistente) "Sí" else "No",
             false) { s-> viewModel.ansiedadPersistente = (s == "Sí") }


        QuestionTitle("Calidad de tu sueño (1=Mala, 5=Excelente)")
        Text("${viewModel.calidadSueno.toInt()}", style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = viewModel.calidadSueno,
            onValueChange = { viewModel.calidadSueno = it },
            valueRange = 1f..5f,
            steps = 3, // 3 pasos intermedios (para 1, 2, 3, 4, 5)
            colors = SliderDefaults
                .colors(thumbColor = MaterialTheme
                    .colorScheme.primary,
                    activeTrackColor = MaterialTheme
                        .colorScheme.primary)
        )
    }
}

@Composable
fun OnboardingStep_Habitos(viewModel: OnboardingViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Hábitos de Vida",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(16.dp))

        QuestionTitle("¿Frecuencia de actividad física?")
        RadioGroup(
            listOf("Ninguna", "1-2 veces por semana", "3-4 veces por semana", "5 o más veces por semana"),
          viewModel.frecuenciaActividadFisica)
              { viewModel.frecuenciaActividadFisica = it }


        QuestionTitle("¿Cómo describirías tu alimentación?")
        RadioGroup(
            options = listOf("Muy saludable", "Regular", "Poco saludable"),
           viewModel.calidadAlimentacion)
           { viewModel.calidadAlimentacion = it }


        QuestionTitle("¿Consumes alcohol?")
        RadioGroup(
            options = listOf("Nunca", "Ocasional", "Frecuente"),
            viewModel.consumoAlcohol)
           { viewModel.consumoAlcohol = it }
    }
}

@Composable
fun OnboardingStep_Objetivos(viewModel: OnboardingViewModel) {
    val objetivosList = listOf("Mejorar energía y vitalidad", "Mejorar calidad del sueño", "Controlar Síntomas", "Perder Peso", "Salud Ósea y Muscular", "Manejar Estrés", "Mejorar Autoestima")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Tus Objetivos",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(16.dp))
        QuestionTitle("¿Cuáles de estos aspectos te gustaría mejorar?")

        objetivosList.forEach { objetivo ->
            Row(
                Modifier.fillMaxWidth().selectable(
                    selected = viewModel.objetivos.contains(objetivo),
                    onClick = { viewModel.onObjetivoToggled(objetivo) }
                ).padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = viewModel.objetivos.contains(objetivo),
                    onCheckedChange = { viewModel.onObjetivoToggled(objetivo) }
                )
                Text(objetivo,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(Modifier.height(16.dp))
        LunaraTextField(
            value = viewModel.condicionesMedicas,
            onValueChange = { viewModel.condicionesMedicas = it },
            label = "¿Alguna condición médica a considerar?",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun OnboardingStep_Resumen(viewModel: OnboardingViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("¡Todo listo, ${viewModel.nombre}!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text("Estamos listos para generar tu plan de bienestar personalizado.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(16.dp))
        Text("Etapa: ${viewModel.etapaHormonal}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary)
        Text("Síntomas: ${viewModel.sintomas.size} reportados",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary)
        Text("Objetivos: ${viewModel.objetivos.size} seleccionados",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary)
    }
}




