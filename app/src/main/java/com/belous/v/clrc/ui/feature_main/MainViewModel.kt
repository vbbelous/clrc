package com.belous.v.clrc.ui.feature_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.R
import com.belous.v.clrc.data.db.entity.YeelightEntity
import com.belous.v.clrc.data.net.YeelightSource
import com.belous.v.clrc.domain.Yeelight
import com.belous.v.clrc.use_case.UseCases
import com.belous.v.clrc.utils.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainStates: MainStates,
    private val useCases: UseCases
) : ViewModel() {

    private val _uiEventFlow = MutableSharedFlow<ListUiEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    private val _yeelightList = MutableLiveData<List<Yeelight>>()
    val yeelightList: LiveData<List<Yeelight>>
        get() = _yeelightList

    private val _foundYeelightList = MutableLiveData<List<YeelightEntity>>()
    val foundYeelightEntityList: LiveData<List<YeelightEntity>>
        get() = _foundYeelightList

    init {
        viewModelScope.launch {
            useCases.getYeelightEntityList().collectLatest { yeelightEntityList ->
                _yeelightList.postValue(yeelightEntityList
                    .map { yeelightEntity -> useCases.entityToYeelight(yeelightEntity) }
                    .sortedBy { it.name })
            }
        }
    }

    fun reloadYeelightList() {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            yeelightList.value?.forEach { yeelight ->
                val yeelightEntity = useCases.getYeelightEntity(yeelight.id)
                val params = useCases.getYeelightParams(yeelightEntity.ip, yeelightEntity.port)
                useCases.updateYeelightEntity(yeelightEntity, params)
            }
        }
    }

    fun findYeelight() {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            val deviceParamsList = useCases.findNewDevices()
            val existingSerials = yeelightList.value?.map { it.serial } ?: emptyList()
            val yeelightList = deviceParamsList
                .map { useCases.paramsToEntity(it) }
                .filter { yeelightEntity -> !existingSerials.contains(yeelightEntity.serial) }
            _foundYeelightList.postValue(yeelightList)
            _uiEventFlow.emit(ListUiEvent.YeelightListFound)
        }
    }

    fun saveYeelight(yeelightEntity: YeelightEntity) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            useCases.insertYeelightEntity(yeelightEntity)
        }
    }

    fun renameYeelight(id: Int, name: String) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            val yeelightEntity = useCases.getYeelightEntity(id)
            useCases.updateYeelightEntity(yeelightEntity.copy(name = name))
        }
    }

    fun deleteYeelight(yeelightId: Int) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            useCases.deleteYeelightEntity(yeelightId)
        }
    }

    fun changeYeelightParam(viewId: Int, yeelight: Yeelight) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            var method = ""
            var args = emptyList<String>()
            when (viewId) {
                -1 -> {
                    yeelight.id.let {
                        _uiEventFlow.emit(ListUiEvent.OpenYeelight(it))
                    }
                }
                R.id.power -> {
                    method = YeelightSource.SET_POWER
                    args = listOf(
                        if (yeelight.isPower) "\"off\"" else "\"on\"",
                        "\"smooth\"",
                        "500"
                    )
                }
                R.id.step_down -> {
                    var downBright: Int = yeelight.bright - 25
                    downBright = if (downBright <= 0) 1 else downBright
                    method = YeelightSource.SET_BRIGHT
                    args = listOf(
                        downBright.toString(),
                        "\"smooth\"",
                        "500"
                    )
                }
                R.id.step_up -> {
                    var upBright: Int = yeelight.bright + 25
                    upBright = if (upBright > 100) 100 else upBright
                    method = YeelightSource.SET_BRIGHT
                    args = listOf(
                        upBright.toString(),
                        "\"smooth\"",
                        "500"
                    )
                }
            }
            val params = YeelightSource.setParams(yeelight.ip, yeelight.port, method, args)
            val yeelightEntity = useCases.getYeelightEntity(yeelight.id)
            useCases.updateYeelightEntity(yeelightEntity, params)
        }
    }

    fun showMessage(message: String) {
        mainStates.showMessage(message)
    }

    sealed class ListUiEvent {
        object YeelightListFound : ListUiEvent()
        class OpenYeelight(val yeelightId: Int) : ListUiEvent()
    }
}