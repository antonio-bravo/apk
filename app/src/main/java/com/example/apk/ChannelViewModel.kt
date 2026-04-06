package com.example.apk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChannelViewModel : ViewModel() {

    private val repository = ChannelRepository()
    private val _channels = MutableLiveData<List<Channel>>()
    val channels: LiveData<List<Channel>> = _channels

    fun loadChannels() {
        viewModelScope.launch {
            try {
                // Replace with actual GitHub URL
                val url = "https://raw.githubusercontent.com/user/repo/main/lista.m3u"
                val channelList = repository.fetchChannels(url)
                _channels.value = channelList
            } catch (e: Exception) {
                // Handle error
                _channels.value = emptyList()
            }
        }
    }
}