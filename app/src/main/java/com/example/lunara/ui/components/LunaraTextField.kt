package com.example.lunara.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.lunara.ui.theme.BlancoHueso
import com.example.lunara.ui.theme.CoralEnergetico
import com.example.lunara.ui.theme.DoradoSuave
import com.example.lunara.ui.theme.LilaLunara
import com.example.lunara.ui.theme.LilaSereno


@Composable
fun LunaraTextField(value: String,
                    onValueChange: (String) -> Unit,
                    label: String,
                    keyboardType: KeyboardType = KeyboardType.Text,
                    modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LilaSereno,
            unfocusedBorderColor = LilaLunara,
            focusedContainerColor = BlancoHueso,
            cursorColor = CoralEnergetico,
            unfocusedTextColor = LilaLunara,
            focusedTextColor = LilaSereno,
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = LilaLunara
        ),
        modifier = modifier.fillMaxWidth()
    )

}