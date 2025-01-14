package com.tasquesapp.app.data.network

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor

object ApiClient {
    private const val BASE_URL = "http://192.168.1.129:9000/Application/"
    private val client = OkHttpClient()

    // Función auxiliar para convertir parámetros en una query string
    private fun buildUrlWithQueryParams(endpoint: String, params: Map<String, String>): String {
        val query = params.map { "${it.key}=${it.value}" }
            .joinToString("&") { it }
        return "$BASE_URL$endpoint?$query"
    }

    // Método genérico para POST que envía parámetros como query
    fun post(endpoint: String, params: Map<String, String>, callback: (response: String?, error: String?) -> Unit) {
        val urlWithParams = buildUrlWithQueryParams(endpoint, params)

        // Construye la solicitud
        val request = Request.Builder()
            .url(urlWithParams)
            .post(RequestBody.create(null, "")) // POST vacío
            .build()

        // Ejecuta la solicitud
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        callback(null, "Error: ${it.code}")
                        return
                    }
                    callback(it.body?.string(), null)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(null, "Error: ${e.message}")
            }
        })
    }
}

//object ApiClient {
//    private val loggingInterceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY // Registra la solicitud y la respuesta
//    }
//    // Crear una instancia de OkHttpClient con configuraciones de tiempo de espera
//    private val client = OkHttpClient.Builder()
//        .connectTimeout(10, TimeUnit.SECONDS)  // Tiempo máximo para establecer una conexión
//        .writeTimeout(10, TimeUnit.SECONDS)    // Tiempo máximo para escribir datos
//        .readTimeout(30, TimeUnit.SECONDS)     // Tiempo máximo para leer la respuesta
//        .addInterceptor(loggingInterceptor) // Añadir el interceptor al cliente
//        .build()
//
//    // Metodo para hacer una solicitud GET
//    fun getRequest(url: String, callback: (String?, String?) -> Unit) {
//        val request = Request.Builder().url(url).build() // Construcción de la solicitud GET
//
//        client.newCall(request).enqueue(object : Callback { // Enviar solicitud de forma asíncrona
//            override fun onFailure(call: Call, e: IOException) {
//                callback(null, "Error en la conexión: ${e.message}") // En caso de fallo
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                try {
//                    response.use { // Usamos "use" para cerrar la respuesta automáticamente
//                        if (!response.isSuccessful) { // Verificar si la respuesta fue exitosa
//                            callback(null, "Error en la respuesta: ${response.code}")
//                        } else {
//                            val responseBody = response.body?.string() // Obtener el cuerpo de la respuesta
//                            if (responseBody != null) {
//                                callback(responseBody, null) // Llamar al callback con el cuerpo
//                            } else {
//                                callback(null, "Respuesta vacía") // Caso en que no hay contenido
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//                    callback(null, "Error procesando la respuesta: ${e.message}") // Error al procesar la respuesta
//                }
//            }
//        })
//    }
//
//    // Metodo para hacer una solicitud POST con un cuerpo JSON
//    fun postRequest(url: String, jsonBody: String, callback: (String?, String?) -> Unit) {
//        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), jsonBody) // Crea el cuerpo en formato JSON
//        val request = Request.Builder().url(url).post(body).build() // Construcción de la solicitud POST
//
//        client.newCall(request).enqueue(object : Callback { // Enviar solicitud de forma asíncrona
//            override fun onFailure(call: Call, e: IOException) {
//                callback(null, "Error en la conexión: ${e.message}") // En caso de fallo
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                try {
//                    response.use { // Usamos "use" para cerrar la respuesta automáticamente
//                        if (!response.isSuccessful) { // Verificar si la respuesta fue exitosa
//                            callback(null, "Error en la respuesta: ${response.code}")
//                        } else {
//                            val responseBody = response.body?.string() // Obtener el cuerpo de la respuesta
//                            if (responseBody != null) {
//                                callback(responseBody, null) // Llamar al callback con el cuerpo
//                            } else {
//                                callback(null, "Respuesta vacía") // Caso en que no hay contenido
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//                    callback(null, "Error procesando la respuesta: ${e.message}") // Error al procesar la respuesta
//                }
//            }
//        })
//    }
//}
