package com.tasquesapp.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tasquesapp.app.data.network.ApiClient
import com.tasquesapp.app.ui.components.CustomButton
import org.json.JSONObject

//@Composable
//fun RegisterScreen(onRegisterSuccess: () -> Unit) {
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var role by remember { mutableStateOf("") }
//    var profile by remember { mutableStateOf("") }
//    var message by remember { mutableStateOf("Completa los datos para registrarte.") }
//    var isLoading by remember { mutableStateOf(false) }
//
//    // Constantes para mejorar la mantenibilidad
//    val registerUrl = "http://192.168.1.129:9000/Application/registrarUsuarioAndroid"
//    val successKey = "success"
//    val messageKey = "message"
//
//    // Validación básica antes de enviar la solicitud
//    val isFormValid = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && role.isNotEmpty()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Nombre") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
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
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = role,
//            onValueChange = { role = it },
//            label = { Text("Rol (e.g., PADRE o HIJO)") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = profile,
//            onValueChange = { profile = it },
//            label = { Text("Perfil (opcional)") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Si el formulario es válido y no se está cargando, mostrar el botón
//        if (isFormValid && !isLoading) {
//            Button(
//                onClick = {
//                    val json = JSONObject().apply {
//                        put("nombre", name)
//                        put("correoElectronico", email)
//                        put("contraseña", password)
//                        put("rol", role)
//                        put("perfil", profile)
//                    }
//
//                    isLoading = true // Cambiar el estado a cargando
//
//                    ApiClient.postRequest(registerUrl, json.toString()) { response, error ->
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
//                                message = if (success) {
//                                    "Registro exitoso: $mensaje" // Asignar el mensaje en caso de éxito
//                                } else {
//                                    mensaje // Asignar el mensaje en caso de error
//                                }
//
//                                if (success) {
//                                    onRegisterSuccess() // Llamar a la función para navegar a la pantalla de éxito
//                                }
//
//                            } catch (e: Exception) {
//                                message = "Error al procesar la respuesta."
//                            }
//                        } ?: run {
//                            message = "Error desconocido al procesar la respuesta."
//                        }
//                    }
//                },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = isFormValid && !isLoading // Deshabilitar el botón mientras se está cargando
//            ) {
//                Text("Registrarse")
//            }
//        } else {
//            // Mientras se carga, mostrar un indicador de progreso
//            if (isLoading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(text = message)
//    }
//}

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var profile by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Completa los datos para registrarte.") }
    var isLoading by remember { mutableStateOf(false) }

    // Constantes para mejorar la mantenibilidad
    val successKey = "success"
    val messageKey = "message"

    // Validación básica antes de enviar la solicitud
    val isFormValid = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && role.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
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
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = role,
            onValueChange = { role = it },
            label = { Text("Rol (e.g., PADRE o HIJO)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = profile,
            onValueChange = { profile = it },
            label = { Text("Perfil (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isFormValid) {
                    val params = mutableMapOf(
                        "nombre" to name,
                        "correoElectronico" to email,
                        "contraseña" to password,
                        "rol" to role
                    )
                    if (profile.isNotEmpty()) {
                        params["perfil"] = profile
                    }

                    isLoading = true // Cambiar el estado a cargando

                    ApiClient.post("registrarUsuarioAndroid", params) { response, error ->
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

                                message = if (success) {
                                    "Registro exitoso: $mensaje"
                                } else {
                                    mensaje
                                }

                                if (success) {
                                    onRegisterSuccess()
                                }

                            } catch (e: Exception) {
                                message = "Error al procesar la respuesta."
                            }
                        } ?: run {
                            message = "Error desconocido al procesar la respuesta."
                        }
                    }
                } else {
                    message = "Por favor, completa todos los campos obligatorios."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid && !isLoading
        ) {
            Text("Registrarse")
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
fun PreviewRegisterScreen() {
    RegisterScreen(onRegisterSuccess = {
        // Este es el callback que cambia la pantalla a SUCCESS
    })
}
