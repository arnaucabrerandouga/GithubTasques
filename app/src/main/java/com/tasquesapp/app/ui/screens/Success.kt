package com.tasquesapp.app.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier


@Composable
fun SuccessScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text("¡Operación exitosa!", style = MaterialTheme.typography.headlineLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSuccessScreen() {
    SuccessScreen()
}