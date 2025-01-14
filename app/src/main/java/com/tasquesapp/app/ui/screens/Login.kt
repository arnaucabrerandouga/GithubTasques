package com.tasquesapp.app.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tasquesapp.app.data.network.ApiClient
import org.json.JSONObject


//@Composable
//fun LoginScreen(onLoginSuccess: (String) -> Unit) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var message by remember { mutableStateOf("Por favor, inicia sesión.") }
//    var isLoading by remember { mutableStateOf(false) }
//
//    // Constantes para mejorar la mantenibilidad
//    val loginUrl = "http://192.168.1.129:9000/Application/loginAndroid"
//    val successKey = "success"
//    val messageKey = "message"
//    val dataKey = "data"
//    val roleKey = "rol"
//
//    // Validación básica antes de enviar la solicitud
//    val isFormValid = email.isNotEmpty() && password.isNotEmpty()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Correo Electrónico") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Contraseña") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation() // Para ocultar la contraseña
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                if (isFormValid) {
//                    // Iniciar el proceso de inicio de sesión
//                    Log.d("LoginRequest", "Email: $email, Password: $password")
//
//                    val json = JSONObject().apply {
//                        put("correoElectronico", email) // Cambiar a "correoElectronico"
//                        put("contraseña", password)      // Cambiar a "contraseña"
//                    }
//
//                    isLoading = true // Cambiar el estado a cargando
//
//                    ApiClient.postRequest(loginUrl, json.toString()) { response, error ->
//                        isLoading = false // Cambiar el estado a no cargando
//
//                        if (error != null) {
//                            message = "Error: $error"
//                            return@postRequest
//                        }
//
//                        response?.let {
//                            try {
//                                val jsonResponse = JSONObject(it)
//                                val success = jsonResponse.optBoolean(successKey, false)
//                                val mensaje = jsonResponse.optString(messageKey, "Respuesta desconocida.")
//
//                                if (success) {
//                                    val userData = jsonResponse.optJSONObject(dataKey)
//                                    val userRole = userData?.optString(roleKey) ?: "Sin rol"
//                                    message = "Bienvenido, rol: $userRole"
//                                    onLoginSuccess(userRole) // Llamar a la función que cambiará la pantalla
//                                } else {
//                                    message = mensaje
//                                }
//                            } catch (e: Exception) {
//                                message = "Error al procesar la respuesta."
//                            }
//                        } ?: run {
//                            message = "Error desconocido al procesar la respuesta."
//                        }
//                    }
//                } else {
//                    message = "Por favor, completa todos los campos."
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = isFormValid && !isLoading // Deshabilitar el botón mientras se está cargando
//        ) {
//            Text("Iniciar Sesión")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (isLoading) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//        } else {
//            Text(text = message)
//        }
//    }
//}

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Por favor, inicia sesión.") }
    var isLoading by remember { mutableStateOf(false) }

    // Constantes para mejorar la mantenibilidad
    val loginUrl = "http://192.168.1.129:9000/Application/loginAndroid"
    val successKey = "success"
    val messageKey = "message"
    val dataKey = "data"
    val roleKey = "rol"

    // Validación básica antes de enviar la solicitud
    val isFormValid = email.isNotEmpty() && password.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation() // Para ocultar la contraseña
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isFormValid) {
                    Log.d("LoginRequest", "Email: $email, Password: $password")

                    // Crear el mapa de parámetros
                    val params = mapOf(
                        "correoElectronico" to email, // Cambiar a "correoElectronico"
                        "contraseña" to password      // Cambiar a "contraseña"
                    )

                    isLoading = true // Cambiar el estado a cargando

                    ApiClient.post("loginAndroid", params) { response, error ->
                        isLoading = false // Cambiar el estado a no cargando

                        if (error != null) {
                            message = "Error: $error"
                            return@post
                        }

                        response?.let {
                            try {
                                val jsonResponse = JSONObject(it)
                                val success = jsonResponse.optBoolean(successKey, false)
                                val mensaje = jsonResponse.optString(messageKey, "Respuesta desconocida.")

                                if (success) {
                                    val userData = jsonResponse.optJSONObject(dataKey)
                                    val userRole = userData?.optString(roleKey) ?: "Sin rol"
                                    message = "Bienvenido, rol: $userRole"
                                    onLoginSuccess(userRole) // Llamar a la función que cambiará la pantalla
                                } else {
                                    message = mensaje
                                }
                            } catch (e: Exception) {
                                message = "Error al procesar la respuesta."
                            }
                        } ?: run {
                            message = "Error desconocido al procesar la respuesta."
                        }
                    }
                } else {
                    message = "Por favor, completa todos los campos."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid && !isLoading // Deshabilitar el botón mientras se está cargando
        ) {
            Text("Iniciar Sesión")
        }


        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Text(text = message)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onLoginSuccess = { role ->
        // Simula un comportamiento tras un login exitoso
    })
}


