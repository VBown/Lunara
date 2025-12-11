package com.example.lunara.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lunara.viewmodel.ChatViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import com.example.lunara.ui.components.LunaraTextField
import com.example.lunara.ui.theme.CoralEnergetico


@Composable
fun ChatbotScreen(viewModel: ChatViewModel) {
    val messages by remember { derivedStateOf { viewModel.messages } }
    val isLoading by viewModel.isLoading.collectAsState()
    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) { listState.animateScrollToItem(messages.size - 1) }
    }

    Scaffold { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(bottom = 80.dp)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                items(messages) { msg -> MessageBubble(message = msg) }
                if (isLoading) {
                    item {
                        Box(contentAlignment = Alignment.BottomStart) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LunaraTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                   "Escribe tu mensaje...",
                    modifier = Modifier.weight(1f),
                  )
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = {
                    viewModel.sendMessage(userInput)
                    userInput = ""
                }, enabled = !isLoading && userInput.isNotBlank()) {
                    Icon(Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar mensaje",
                        tint = CoralEnergetico
                    )
                }
            }
        }
    }
}


