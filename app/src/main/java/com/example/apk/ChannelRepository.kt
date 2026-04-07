package com.example.apk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ChannelRepository {

    private val client = OkHttpClient()

    suspend fun fetchChannels(url: String): List<Channel> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""
            parseM3U(body)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseM3U(content: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        val lines = content.lines()
        var currentName = ""

        for (line in lines) {
            val trimmedLine = line.trim()
            if (trimmedLine.isEmpty()) continue

            if (trimmedLine.startsWith("#EXTINF")) {
                // Intentar extraer el nombre del canal de tvg-name o del final de la línea
                val name = if (trimmedLine.contains("tvg-name=\"")) {
                    trimmedLine.substringAfter("tvg-name=\"").substringBefore("\"")
                } else {
                    trimmedLine.substringAfterLast(",")
                }
                currentName = name.trim().ifEmpty { "Canal Sin Nombre" }
            } else if (!trimmedLine.startsWith("#")) {
                // Si la línea no empieza por #, asumimos que es la URL del canal
                // Aceptamos acestream://, http://, etc.
                channels.add(Channel(currentName, trimmedLine))
                currentName = "" // Reset para el siguiente canal
            }
        }
        return channels
    }
}