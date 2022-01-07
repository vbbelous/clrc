package com.belous.v.clrc.ui.feature_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.R
import com.belous.v.clrc.data.db.entity.YeelightEntity
import com.belous.v.clrc.data.net.YeelightSource
import com.belous.v.clrc.domain.Yeelight
import com.belous.v.clrc.use_case.UseCases
import com.belous.v.clrc.utils.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.*

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
        loadYeelightList()
    }

    private fun loadYeelightList() {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            _yeelightList.postValue(
                useCases.getYeelightEntityList()
                    .map { useCases.entityToYeelight(it) }
                    .sortedBy { it.name })
        }
    }

    fun reloadYeelightList() {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            useCases.getYeelightEntityList().forEach { yeelightEntity ->
                val params = useCases.getYeelightParams(yeelightEntity.ip, yeelightEntity.port)
                useCases.updateYeelightEntity(yeelightEntity, params)
            }
            loadYeelightList()
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

//    fun findYeelight() {
//        viewModelScope.launch {
//            val yeelightList = mutableListOf<Yeelight>()
//            val stringList = listOf(
//                "color_mode=2, sat=0, bright=20, rgb=0, ct=4000, name=, hue=0, model=ceiling2, id=0x0000000003b5a3f4, power=off, fw_ver=34, support=get_prop set_default set_power toggle set_bright set_scene cron_add cron_get cron_del start_cf stop_cf set_ct_abx set_name set_adjust adjust_bright adjust_ct, Location=yeelight://192.168.30.101:55443",
//                "color_mode=2, sat=0, bright=20, rgb=0, ct=4389, name=, hue=0, model=ct_bulb, id=0x0000000005e7788b, power=off, fw_ver=35, support=get_prop set_default set_power toggle set_bright start_cf stop_cf set_scene cron_add cron_get cron_del set_ct_abx set_adjust adjust_bright adjust_ct set_music set_name, Location=yeelight://192.168.30.104:55443"
//            )
//            stringList.forEach { str ->
//                val params = HashMap<String, String>()
//                str.split(", ").forEach { j ->
//                    val k = j.split("=")
//                    if (k.size > 1) {
//                        params[k[0]] = k[1]
//                    } else {
//                        params[k[0]] = ""
//                    }
//                }
//                yeelightList.add(YeelightBuilder.build(params))
//            }
//            _foundYeelightList.postValue(yeelightList)
//            _uiEventFlow.emit(ListUiEvent.YeelightListFound)
//        }
//    }

    fun saveYeelight(yeelightEntity: YeelightEntity) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            useCases.insertYeelightEntity(yeelightEntity)
            loadYeelightList()
        }
    }

    fun renameYeelight(id: Int, name: String) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            val yeelightEntity = useCases.getYeelightEntity(id)
            useCases.updateYeelightEntity(yeelightEntity.copy(name = name))
            loadYeelightList()
        }
    }

    fun deleteYeelight(yeelightId: Int) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            useCases.deleteYeelightEntity(yeelightId)
            loadYeelightList()
        }
    }

    fun changeYeelightParam(viewId: Int, yeelight: Yeelight) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            val args: Queue<String> = LinkedList()
            when (viewId) {
                -1 -> {
                    yeelight.id.let {
                        _uiEventFlow.emit(ListUiEvent.OpenYeelight(it))
                    }
                }
                R.id.power -> args.addAll(
                    listOf(
                        YeelightSource.SET_POWER,
                        if (yeelight.isPower) "\"off\"" else "\"on\"",
                        "\"smooth\"",
                        "500"
                    )
                )
                R.id.step_down -> {
                    var downBright: Int = yeelight.bright - 25
                    downBright = if (downBright <= 0) 1 else downBright
                    args.addAll(
                        listOf(
                            YeelightSource.SET_BRIGHT,
                            downBright.toString(),
                            "\"smooth\"",
                            "500"
                        )
                    )
                }
                R.id.step_up -> {
                    var upBright: Int = yeelight.bright + 25
                    upBright = if (upBright > 100) 100 else upBright
                    args.addAll(
                        listOf(
                            YeelightSource.SET_BRIGHT,
                            upBright.toString(),
                            "\"smooth\"",
                            "500"
                        )
                    )
                }
            }
            val params = YeelightSource.setParams(yeelight.ip, yeelight.port, args)
            val yeelightEntity = useCases.getYeelightEntity(yeelight.id)
            useCases.updateYeelightEntity(yeelightEntity, params)
            loadYeelightList()
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