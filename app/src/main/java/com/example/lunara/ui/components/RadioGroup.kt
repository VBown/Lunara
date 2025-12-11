package com.example.lunara.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun RadioGroup(
    options: List<String>,
    selected: String,
    isRow: Boolean = false,
    onSelect: (String) -> Unit
) {
    if (isRow)
        Row(Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly) { options.forEach { o ->
            Row(verticalAlignment = Alignment.CenterVertically) { RadioButton(selected == o,
                { onSelect(o) },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary))
                Text(o, style = MaterialTheme.typography.bodySmall) } } }
    else Column { options.forEach { o -> Row(Modifier.fillMaxWidth().selectable(selected == o
    ) { onSelect(o) },
        verticalAlignment = Alignment.CenterVertically) { RadioButton(selected == o, { onSelect(o) },
        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary))
        Text(o,
            style = MaterialTheme.typography.bodyLarge) } } } }