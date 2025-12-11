package com.example.lunara.viewmodel


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunara.data.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content


class ChatViewModel : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val systemPrompt = """
        Eres 'Lunara', una asistente virtual experta, empática y sabia, especializada en el bienestar 
        integral de mujeres mayores de 40 años durante las etapas de perimenopausia, menopausia y 
        postmenopausia. Tu tono es siempre comprensivo, sereno y motivador.
        Tus respuestas deben ser claras, concisas y estar enfocadas en ofrecer apoyo a través de 
        hábitos saludables, nutrición, ejercicio adaptado y bienestar emocional.
        IMPORTANTE: Nunca debes dar consejos médicos directos ni diagnósticos. Si una usuaria 
        pregunta por un síntoma específico o una condición médica, tu respuesta debe ser siempre 
        recomendarle amablemente que consulte a un profesional de la salud.
    """.trimIndent()

    // Modelo de IA de Gemini
    // 1. Inicializa el modelo de IA
    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash",
            // El system prompt (personalidad) se define aquí
            systemInstruction= content(role = "system") { text(systemPrompt) }
        )

    // 2. Crea el objeto de chat, pasándole el systemPrompt y el historial inicial
    private val chat = model.startChat(
        history = listOf(
            content(role = "model") { text("¡Hola! Soy Lunara, tu asistente de bienestar. Estoy aquí para escucharte y acompañarte. ¿Cómo te sientes hoy?") }
        )
    )
    init {
        // 3. Carga el mensaje inicial desde el historial del chat
        _messages.add(ChatMessage("¡Hola! Soy Lunara, tu asistente de bienestar. Estoy aquí para escucharte y acompañarte. ¿Cómo te sientes hoy?", false))
    }

    fun sendMessage(userInput: String) {
        if (userInput.isBlank()) return

        _messages.add(ChatMessage(userInput, true))
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // 4. Envía el mensaje al objeto 'chat' (no al 'generativeModel')
                val response = chat.sendMessage(userInput)
                val aiResponse = response.text ?: "No he podido procesar eso, intenta de nuevo."
                _messages.add(ChatMessage(aiResponse, false))
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error al llamar a Gemini", e)
                val errorMessage = "Error: ${e.localizedMessage ?: "Problema de conexión desconocido"}"
                _messages.add(ChatMessage(errorMessage, false))
            } finally {
                _isLoading.value = false
            }
        }
    }
}



