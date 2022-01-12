package com.belous.v.clrc.ui.feature_yeelight

import androidx.lifecycle.*
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.data.net.YeelightSource
import com.belous.v.clrc.domain.Yeelight
import com.belous.v.clrc.ui.Screen
import com.belous.v.clrc.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YeelightViewModel @Inject constructor(
    private val mainStates: MainStates,
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val yeelightId = savedStateHandle.get<Int>(Screen.YeelightScreen.YEELIGHT_ID)
    private val yeelightEntityFlow = yeelightId?.let { useCases.getYeelightEntity(it) }
    private var yeelightFlowJob: Job? = null

    private val _yeelightData = MutableLiveData<Yeelight>()
    val yeelightData: LiveData<Yeelight>
        get() = _yeelightData

    private val event = MutableSharedFlow<YeelightEvent>()

    init {
        yeelightFlowJob = viewModelScope.launch {
            yeelightEntityFlow?.let {
                it.map { yeelightEntity ->
                    useCases.entityToYeelight(yeelightEntity)
                }.collectLatest { yeelight ->
                    _yeelightData.postValue(yeelight)
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            event.collectLatest { event ->
                try {
                    mainStates.loadingState.emit(true)
                    when (event) {
                        is YeelightEvent.Update -> reloadYeelight()
                        is YeelightEvent.Rename -> renameYeelight(event.name)
                        is YeelightEvent.Delete -> deleteYeelight()
                        is YeelightEvent.Power -> setParams(
                            YeelightSource.SET_POWER,
                            listOf(
                                if (yeelightData.value?.isPower == true) "\"off\"" else "\"on\"",
                                "\"smooth\"",
                                "500"
                            )
                        )
                        is YeelightEvent.Moonlight -> {
                            setParams(
                                YeelightSource.SET_POWER,
                                listOf(
                                    if (yeelightData.value?.isPower == true) "\"on\"" else "\"off\"",
                                    "\"smooth\"",
                                    "500",
                                    (if (yeelightData.value?.isActive == true) "1" else "5")
                                )
                            )
                        }
                        is YeelightEvent.BrightMinus -> {
                            var downBright = (yeelightData.value?.bright ?: 0) - 25
                            downBright = if (downBright <= 0) 1 else downBright
                            setParams(
                                YeelightSource.SET_BRIGHT,
                                listOf(downBright.toString(), "\"smooth\"", "500")
                            )
                        }
                        is YeelightEvent.BrightPlus -> {
                            var upBright = (yeelightData.value?.bright ?: 0) + 25
                            upBright = if (upBright > 100) 100 else upBright
                            setParams(
                                YeelightSource.SET_BRIGHT,
                                listOf(upBright.toString(), "\"smooth\"", "500")
                            )
                        }
                        is YeelightEvent.ChangeBright -> {
                            setParams(
                                YeelightSource.SET_BRIGHT,
                                listOf(event.bright.toString(), "\"smooth\"", "500")
                            )
                        }
                        is YeelightEvent.TempMinus -> {
                            var downTemp = (yeelightData.value?.ct ?: 4200) - 500
                            downTemp = if (downTemp <= 2700) 2700 else downTemp
                            setParams(
                                YeelightSource.SET_CT_ABX,
                                listOf(downTemp.toString(), "\"smooth\"", "500")
                            )
                        }
                        is YeelightEvent.TempPlus -> {
                            var upTemp = (yeelightData.value?.ct ?: 4200) + 500
                            upTemp = if (upTemp > 6500) 6500 else upTemp
                            setParams(
                                YeelightSource.SET_CT_ABX,
                                listOf(upTemp.toString(), "\"smooth\"", "500")
                            )
                        }
                    }
                } catch (e: Exception) {
                    mainStates.eventState.emit(MainStates.EventState.ExceptionEvent(e))
                } finally {
                    mainStates.loadingState.emit(false)
                }
            }
        }
    }

    private suspend fun reloadYeelight() {
        yeelightEntityFlow?.first()?.let {
            val params = useCases.getYeelightParams(it.ip, it.port)
            useCases.updateYeelightEntity(it, params)
        }
    }

    private suspend fun renameYeelight(name: String) {
        yeelightEntityFlow?.first()?.let {
            useCases.updateYeelightEntity(it.copy(name = name))
        }
    }

    private suspend fun deleteYeelight() {
        yeelightFlowJob?.cancel()
        yeelightId?.let {
            useCases.deleteYeelightEntity(it)
        }
    }

    private suspend fun setParams(method: String, args: List<String>) {
        val yeelightEntity = yeelightEntityFlow?.first()
        yeelightEntity?.let {
            val params =
                useCases.setYeelightParams(it.ip, it.port, method, args)
            useCases.updateYeelightEntity(it, params)
        }
    }

    fun sendEvent(yeelightEvent: YeelightEvent) {
        viewModelScope.launch { event.emit(yeelightEvent) }
    }

    sealed class YeelightEvent {
        object Update : YeelightEvent()
        class Rename(val name: String) : YeelightEvent()
        object Delete : YeelightEvent()
        object Power : YeelightEvent()
        object Moonlight : YeelightEvent()
        object BrightPlus : YeelightEvent()
        object BrightMinus : YeelightEvent()
        class ChangeBright(val bright: Int) : YeelightEvent()
        object TempPlus : YeelightEvent()
        object TempMinus : YeelightEvent()
    }
}