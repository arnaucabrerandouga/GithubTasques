package com.tasquesapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.tasquesapp.app.ui.screens.LoginScreen
import com.tasquesapp.app.ui.screens.RegisterScreen
import com.tasquesapp.app.ui.screens.SuccessScreen
import com.tasquesapp.app.ui.theme.TasquesAppTheme

// Enum para gestionar los estados de la pantalla
enum class ScreenState {
    LOGIN,
    REGISTER,
    SUCCESS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasquesAppTheme {
                var currentScreen by remember { mutableStateOf(ScreenState.LOGIN) }

                // Controlar la navegación entre Login, Register y Success
                when (currentScreen) {
                    ScreenState.LOGIN -> {
                        LoginScreen(onLoginSuccess = {
                            currentScreen = ScreenState.SUCCESS  // Cambiar a Success después de login
                        })
                    }
                    ScreenState.REGISTER -> {
                        RegisterScreen(onRegisterSuccess = {
                            currentScreen = ScreenState.SUCCESS  // Cambiar a Success después de registro
                        })
                    }
                    ScreenState.SUCCESS -> {
                        SuccessScreen()  // Mostrar pantalla de éxito
                    }
                }

                // Opción para cambiar entre Login y Register
                if (currentScreen == ScreenState.LOGIN) {
                    Button(onClick = { currentScreen = ScreenState.REGISTER }) {
                        Text("¿No tienes cuenta? Regístrate aquí")
                    }
                }
            }
        }
    }
}
