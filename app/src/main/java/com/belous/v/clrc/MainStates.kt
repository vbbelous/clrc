package com.belous.v.clrc

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class MainStates : ViewModel() {

    val loadingState = MutableStateFlow(false)
    val eventState = MutableSharedFlow<EventState>()

    sealed class EventState {
        class MessageEvent(val messageId: Int) : EventState()
        class ExceptionEvent(val exception: Exception) : EventState()
    }
}