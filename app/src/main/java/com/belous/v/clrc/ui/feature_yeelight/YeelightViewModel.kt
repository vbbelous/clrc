package com.belous.v.clrc.ui.feature_yeelight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.data.net.YeelightSource
import com.belous.v.clrc.domain.Yeelight
import com.belous.v.clrc.use_case.UseCases
import com.belous.v.clrc.utils.launch
import java.util.*

class YeelightViewModel(
    private val yeelightId: Int,
    private val mainStates: MainStates,
    private val useCases: UseCases
) : ViewModel() {

    private val _yeelightData = MutableLiveData<Yeelight>()
    val yeelightData: LiveData<Yeelight>
        get() = _yeelightData

    init {
        loadYeelight()
    }

    private fun loadYeelight() {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            val yeelightEntity = useCases.getYeelightEntity(yeelightId)
            val yeelight = useCases.entityToYeelight(yeelightEntity)
            _yeelightData.postValue(yeelight)
        }
    }

    fun reloadYeelight() {
        launch(
            loadingState = mainStates.loadingState,
            eventFlow = mainStates.event
        ) {
            val yeelightEntity = useCases.getYeelightEntity(yeelightId)
            val params =
                useCases.getYeelightParams(yeelightEntity.ip, yeelightEntity.port)
            useCases.updateYeelightEntity(yeelightEntity, params)
            loadYeelight()
        }
    }

    fun onSetParams(viewId: Int) {
        yeelightData.value?.let { yeelight ->
            launch(
                loadingState = mainStates.loadingState,
                eventFlow = mainStates.event
            ) {
                val args: Queue<String> = LinkedList()

                when (viewId) {
                    ViewIds.POWER_CHANGE -> args.addAll(
                        listOf(
                            YeelightSource.SET_POWER,
                            (if (yeelight.isPower) "\"off\"" else "\"on\""),
                            "\"smooth\"",
                            "500"
                        )
                    )
                    ViewIds.MODE_CHANGE -> args.addAll(
                        listOf(
                            YeelightSource.SET_POWER,
                            (if (yeelight.isPower) "\"on\"" else "\"off\""),
                            "\"smooth\"",
                            "500",
                            (if (yeelight.isActive) "1" else "5")
                        )
                    )
                    ViewIds.BRIGHT_DOWN -> {
                        var downBright = yeelight.bright - 25
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
                    ViewIds.BRIGHT_UP -> {
                        var upBright = yeelight.bright + 25
                        upBright = if (upBright > 100) 100 else upBright
                        args.addAll(
                            listOf(
                                YeelightSource.SET_BRIGHT, upBright.toString(), "\"smooth\"", "500"
                            )
                        )
                    }
                    ViewIds.BRIGHT_1 ->
                        args.addAll(listOf(YeelightSource.SET_BRIGHT, "1", "\"smooth\"", "500"))
                    ViewIds.BRIGHT_25 ->
                        args.addAll(listOf(YeelightSource.SET_BRIGHT, "25", "\"smooth\"", "500"))
                    ViewIds.BRIGHT_50 ->
                        args.addAll(listOf(YeelightSource.SET_BRIGHT, "50", "\"smooth\"", "500"))
                    ViewIds.BRIGHT_75 ->
                        args.addAll(listOf(YeelightSource.SET_BRIGHT, "75", "\"smooth\"", "500"))
                    ViewIds.BRIGHT_100 ->
                        args.addAll(listOf(YeelightSource.SET_BRIGHT, "100", "\"smooth\"", "500"))

                    ViewIds.TEMP_DOWN -> {
                        var downTemp = yeelight.ct - 500
                        downTemp = if (downTemp <= 2700) 2700 else downTemp
                        args.addAll(
                            listOf(
                                YeelightSource.SET_CT_ABX,
                                downTemp.toString(),
                                "\"smooth\"",
                                "500"
                            )
                        )
                    }
                    ViewIds.TEMP_UP -> {
                        var upTemp = yeelight.ct + 500
                        upTemp = if (upTemp > 6500) 6500 else upTemp
                        args.addAll(
                            listOf(
                                YeelightSource.SET_CT_ABX, upTemp.toString(), "\"smooth\"", "500"
                            )
                        )
                    }
                    else -> {}
                }

                val params = useCases.setYeelightParams(yeelight.ip, yeelight.port, args)
                val yeelightEntity = useCases.getYeelightEntity(yeelightId)
                useCases.updateYeelightEntity(yeelightEntity, params)
                loadYeelight()
            }
        }
    }
}