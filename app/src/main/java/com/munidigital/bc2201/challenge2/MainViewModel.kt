package com.munidigital.bc2201.challenge2

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val defaultMessages = arrayListOf<String>("Yes", "No", "Maybe", "Ask again",
        "I don't think so", "I don't know", "Why are you asking?", "Hello")
    private var _textMessage: MutableLiveData<String> = MutableLiveData()
    private var messages: MutableList<Message> = mutableListOf<Message>()
    private var _msList = MutableLiveData<MutableList<Message>>()

    val msListLiveData: LiveData<MutableList<Message>>
        get() = _msList

    fun sendMessage(text: String){
        _textMessage.value = text
        messages.add(Message("1", text, true))

        _msList.value = messages

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            respond()
        }, 1000)
    }

    private fun respond() {
        val randomNumber = (0 until defaultMessages.size).random()
        messages.add(Message("1", defaultMessages[randomNumber], false))
        _msList.value = messages
    }

    fun saveMessage(message: Message){
        messages.add(message)
    }
}