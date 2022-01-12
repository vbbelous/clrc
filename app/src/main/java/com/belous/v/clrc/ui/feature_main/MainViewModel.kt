package com.belous.v.clrc.ui.feature_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.R
import com.belous.v.clrc.data.db.entity.YeelightEntity
import com.belous.v.clrc.data.net.YeelightSource
import com.belous.v.clrc.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainStates: MainStates,
    private val useCases: UseCases
) : ViewModel() {

    private val event = MutableSharedFlow<MainEvent>()

    private val _uiEvent = MutableSharedFlow<MainUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val yeelightEntityList = useCases.getYeelightEntityList()

    val yeelightList = yeelightEntityList.map {
        it.map { yeelightEntity -> useCases.entityToYeelight(yeelightEntity) }
            .sortedBy { yeelight -> yeelight.name }
    }

    private val _foundYeelightList = mutableListOf<YeelightEntity>()
    val foundYeelightEntityList: List<YeelightEntity>
        get() = _foundYeelightList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            event.collectLatest { event ->
                try {
                    mainStates.loadingState.emit(true)
                    when (event) {
                        is MainEvent.Find -> findYeelight()
                        is MainEvent.UpdateAll -> reloadYeelightList()
                        is MainEvent.Save -> saveYeelight(event.yeelightEntity)
                        is MainEvent.Power -> changePower(event.id, event.isPower)
                        is MainEvent.BrightPlus -> increaseBrightness(event.id, event.bright)
                        is MainEvent.BrightMinus -> decreaseBrightness(event.id, event.bright)
                    }
                } catch (e: Exception) {
                    mainStates.eventState.emit(MainStates.EventState.ExceptionEvent(e))
                } finally {
                    mainStates.loadingState.emit(false)
                }
            }
        }
    }

    private suspend fun reloadYeelightList() {
        yeelightEntityList.first().forEach { yeelightEntity ->
            val params = useCases.getYeelightParams(yeelightEntity.ip, yeelightEntity.port)
            useCases.updateYeelightEntity(yeelightEntity, params)
        }
    }

    private suspend fun findYeelight() {
        _foundYeelightList.clear()
        val deviceParamsList = useCases.findNewDevices()
        val existingSerials = yeelightEntityList.first().map { it.serial }
        val yeelightList = deviceParamsList
            .map { useCases.paramsToEntity(it) }
            .filter { yeelightEntity -> !existingSerials.contains(yeelightEntity.serial) }
        if (yeelightList.isNotEmpty()) {
            _foundYeelightList.addAll(yeelightList)
            _uiEvent.emit(MainUiEvent.YeelightFound)
        } else {
            mainStates.eventState.emit(MainStates.EventState.MessageEvent(R.string.device_not_found))
        }
    }

    private suspend fun saveYeelight(yeelightEntity: YeelightEntity) {
        useCases.insertYeelightEntity(yeelightEntity)
    }

    private suspend fun changePower(yeelightId: Int, isPower: Boolean) {
        val method = YeelightSource.SET_POWER
        val args = listOf(if (isPower) "\"off\"" else "\"on\"", "\"smooth\"", "500")
        changeYeelightParam(yeelightId, method, args)
    }

    private suspend fun increaseBrightness(yeelightId: Int, bright: Int) {
        var upBright: Int = bright + 25
        upBright = if (upBright > 100) 100 else upBright
        val method = YeelightSource.SET_BRIGHT
        val args = listOf(upBright.toString(), "\"smooth\"", "500")
        changeYeelightParam(yeelightId, method, args)
    }

    private suspend fun decreaseBrightness(yeelightId: Int, bright: Int) {
        var downBright: Int = bright - 25
        downBright = if (downBright <= 0) 1 else downBright
        val method = YeelightSource.SET_BRIGHT
        val args = listOf(downBright.toString(), "\"smooth\"", "500")
        changeYeelightParam(yeelightId, method, args)
    }

    private suspend fun changeYeelightParam(yeelightId: Int, method: String, args: List<String>) {
        val yeelightEntity = yeelightEntityList.first()
            .first { yeelightEntity -> yeelightEntity.id == yeelightId }
        val params = YeelightSource.setParams(yeelightEntity.ip, yeelightEntity.port, method, args)
        useCases.updateYeelightEntity(yeelightEntity, params)
    }

    sealed class MainUiEvent {
        object YeelightFound : MainUiEvent()
    }

    fun sendEvent(mainEvent: MainEvent) {
        viewModelScope.launch { event.emit(mainEvent) }
    }

    sealed class MainEvent {
        object Find : MainEvent()
        object UpdateAll : MainEvent()
        class Save(val yeelightEntity: YeelightEntity) : MainEvent()
        class Power(val id: Int, val isPower: Boolean) : MainEvent()
        class BrightPlus(val id: Int, val bright: Int) : MainEvent()
        class BrightMinus(val id: Int, val bright: Int) : MainEvent()
    }
}