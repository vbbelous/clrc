package com.belous.v.clrc.ui.feature_yeelight

import androidx.lifecycle.*
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.data.net.YeelightSource
import com.belous.v.clrc.domain.Yeelight
import com.belous.v.clrc.ui.Screen
import com.belous.v.clrc.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YeelightViewModel @Inject constructor(
    private val mainStates: MainStates,
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var yeelightId: Int? = null

    private val _yeelightData = MutableLiveData<Yeelight>()
    val yeelightData: LiveData<Yeelight>
        get() = _yeelightData

    private val event = MutableSharedFlow<YeelightEvent>()

    init {
        yeelightId = savedStateHandle.get<Int>(Screen.YeelightScreen.YEELIGHT_ID)

        viewModelScope.launch { loadYeelight() }
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

    private suspend fun loadYeelight() {
        yeelightId?.let {
            val yeelightEntity = useCases.getYeelightEntity(it)
            val yeelight = useCases.entityToYeelight(yeelightEntity)
            _yeelightData.postValue(yeelight)
        }
    }

    private suspend fun reloadYeelight() {
        yeelightId?.let {
            val yeelightEntity = useCases.getYeelightEntity(it)
            val params =
                useCases.getYeelightParams(yeelightEntity.ip, yeelightEntity.port)
            useCases.updateYeelightEntity(yeelightEntity, params)
            loadYeelight()
        }
    }

    private suspend fun renameYeelight(name: String) {
        yeelightId?.let {
            val yeelightEntity = useCases.getYeelightEntity(it)
            useCases.updateYeelightEntity(yeelightEntity.copy(name = name))
        }
    }

    private suspend fun deleteYeelight() {
        yeelightId?.let {
            useCases.deleteYeelightEntity(it)
        }
    }

    private suspend fun setParams(method: String, args: List<String>) {
        yeelightData.value?.let { yeelight ->
            val params = useCases.setYeelightParams(yeelight.ip, yeelight.port, method, args)
            val yeelightEntity = useCases.getYeelightEntity(yeelight.id)
            useCases.updateYeelightEntity(yeelightEntity, params)
            loadYeelight()
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