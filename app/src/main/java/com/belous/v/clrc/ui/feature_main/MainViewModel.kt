package com.belous.v.clrc.ui.feature_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.R
import com.belous.v.clrc.core.data.db.YeelightDao
import com.belous.v.clrc.core.data.net.YeelightSource
import com.belous.v.clrc.core.domain.Yeelight
import com.belous.v.clrc.core.domain.YeelightParams
import com.belous.v.clrc.core.util.YeelightBuilder
import com.belous.v.clrc.utils.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.*

class MainViewModel(
    private val mainStates: MainStates,
    private val yeelightDao: YeelightDao
) : ViewModel() {

    private val _uiEventFlow = MutableSharedFlow<ListUiEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    private val _loadedYeelightList = MutableLiveData<List<Yeelight>>()
    val loadedYeelightList: LiveData<List<Yeelight>>
        get() = _loadedYeelightList

    private val _foundYeelightList = MutableLiveData<List<Yeelight>>()
    val foundYeelightList: LiveData<List<Yeelight>>
        get() = _foundYeelightList

    init {
        loadYeelightList()
    }

    private fun loadYeelightList() {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            _loadedYeelightList.postValue(yeelightDao.getAll())
        }
    }

    fun reloadYeelightList() {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            val yeelightList = ArrayList<Yeelight>()
            yeelightDao.getAll().forEach { yeelight ->
                val params = YeelightSource.getParams(yeelight.ip, yeelight.port)
                yeelightList.add(yeelight.copy(params = yeelight.params.plus(params)))
            }
            yeelightDao.update(yeelightList)
            _loadedYeelightList.postValue(yeelightList)
        }
    }

    fun findYeelight() {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            val deviceParamsList = YeelightSource.searchDevices()
            val yeelightList = deviceParamsList.map { YeelightBuilder.build(it) }
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

    fun saveYeelight(yeelight: Yeelight) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            yeelightDao.insert(yeelight)
            loadYeelightList()
        }
    }

    fun renameYeelight(yeelight: Yeelight) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            yeelightDao.update(yeelight)
            loadYeelightList()
        }
    }

    fun deleteYeelight(position: Int) {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            loadedYeelightList.value?.get(position)?.let {
                yeelightDao.delete(it)
                loadYeelightList()
            }
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
                    yeelight.id?.let {
                        _uiEventFlow.emit(ListUiEvent.OpenYeelight(it))
                    }
                }
                R.id.power -> args.addAll(
                    listOf(
                        YeelightSource.SET_POWER,
                        if (yeelight.params[YeelightParams.POWER] == "on") "\"off\"" else "\"on\"",
                        "\"smooth\"",
                        "500"
                    )
                )
                R.id.step_down -> {
                    val isActive = yeelight.params[YeelightParams.ACTIVE_MODE] == "1"
                    var downBright: Int =
                        ((if (isActive) yeelight.params[YeelightParams.NL_BR]
                        else yeelight.params[YeelightParams.BRIGHT])?.toInt() ?: 0) - 25
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
                    val isActive = yeelight.params[YeelightParams.ACTIVE_MODE] == "1"
                    var upBright: Int =
                        ((if (isActive) yeelight.params[YeelightParams.NL_BR]
                        else yeelight.params[YeelightParams.BRIGHT])?.toInt() ?: 0) + 25
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
            yeelightDao.update(yeelight.copy(params = yeelight.params.plus(params)))
            loadYeelightList()
        }
    }

    sealed class ListUiEvent {
        object YeelightListFound : ListUiEvent()
        class OpenYeelight(val yeelightId: Int) : ListUiEvent()
    }
}