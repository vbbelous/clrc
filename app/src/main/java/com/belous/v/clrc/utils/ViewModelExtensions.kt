package com.belous.v.clrc.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belous.v.clrc.MainStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun ViewModel.launch(
    loadingState: MutableStateFlow<Boolean>,
    eventFlow: MutableSharedFlow<MainStates.Event>,
    action: suspend () -> Unit
) {
    viewModelScope.launch(Dispatchers.IO) {
        loadingState.value = true
        try {
            action()
        } catch (e: Exception) {
            eventFlow.emit(MainStates.Event.ExceptionEvent(e))
        } finally {
            loadingState.value = false
        }
    }
}