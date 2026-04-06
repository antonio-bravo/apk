package com.example.apk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ChannelRepository {

    private val client = OkHttpClient()

    suspend fun fetchChannels(url: String): List<Channel> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: ""
        parseM3U(body)
    }

    private fun parseM3U(content: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        val lines = content.lines()
        var currentName = ""

        for (line in lines) {
            if (line.startsWith("#EXTINF")) {
                val name = line.substringAfter("tvg-name=\"").substringBefore("\"")
                currentName = name.ifEmpty { "Canal" }
            } else if (line.startsWith("acestream://")) {
                channels.add(Channel(currentName, line))
            }
        }
        return channels
    }
}