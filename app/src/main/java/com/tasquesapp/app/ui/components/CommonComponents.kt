package com.tasquesapp.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun CustomButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Text(text)
    }
}

@Composable
fun LoadingText(message: String, modifier: Modifier = Modifier) {
    Text(text = message, modifier = modifier)
}