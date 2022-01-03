package com.belous.v.clrc

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class MainStates : ViewModel() {

    val loadingState = MutableStateFlow(false)
    val event = MutableSharedFlow<Event>()

//    fun showMessage(message: String) {
//        viewModelScope.launch {
//            event.emit(Event.MessageEvent(message))
//        }
//    }

//    fun showError(e: Exception) {
//        viewModelScope.launch {
//            event.emit(Event.ExceptionEvent(e))
//        }
//    }

    sealed class Event {
        class MessageEvent(val message: String) : Event()
        class ExceptionEvent(val exception: Exception) : Event()
    }
}