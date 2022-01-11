package com.belous.v.clrc.ui

sealed class Screen(private val attr: String? = "") {

    val route: String
        get() = this::class.java.simpleName.plus(attr)

    object MainScreen : Screen()
    class YeelightScreen(yeelightId: Int? = null) : Screen(
        "?$YEELIGHT_ID=${yeelightId ?: "{$YEELIGHT_ID}"}"
    ) {
        companion object {
            const val YEELIGHT_ID = "yeelight_id"
        }
    }
}