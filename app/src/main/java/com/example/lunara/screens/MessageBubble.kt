package com.example.lunara.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lunara.data.ChatMessage

@Composable
fun MessageBubble(message: ChatMessage) {
    val align = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val bg = if (message.isFromUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    Box(Modifier.fillMaxWidth(), contentAlignment = align) {
        Card(colors = CardDefaults.cardColors(containerColor = bg),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.widthIn(max = 300.dp)) {
            Text(message.text, Modifier.padding(12.dp))
        }
    }
}
