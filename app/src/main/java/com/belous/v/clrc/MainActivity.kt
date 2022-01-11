package com.belous.v.clrc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.belous.v.clrc.ui.Screen
import com.belous.v.clrc.ui.component.ProgressIndicator
import com.belous.v.clrc.ui.feature_main.MainScreen
import com.belous.v.clrc.ui.feature_yeelight.YeelightScreen
import com.belous.v.clrc.ui.theme.CLRCTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val IS_VOTED = "IS_VOTED"
        const val DAY_INSTALLATION = "DAY_INSTALLATION"
    }

    @Inject
    lateinit var mainStates: MainStates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            CLRCTheme {
                NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
                    composable(route = Screen.MainScreen.route) {
                        MainScreen(navController = navController)
                    }
                    composable(route = Screen.YeelightScreen().route,
                        arguments = listOf(
                            navArgument(name = Screen.YeelightScreen.YEELIGHT_ID) {
                                type = NavType.IntType
                                defaultValue = 0
                            }
                        )
                    ) {
                        YeelightScreen()
                    }
                }

                ProgressIndicator(state = mainStates.loadingState.collectAsState(initial = false))
            }
        }
//        showRateDialog(fragmentManager)
    }

//    private fun showRateDialog(fragmentManager: FragmentManager) {
//        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
//        if (!preferences.contains(IS_VOTED)) {
//            if (preferences.getLong(DAY_INSTALLATION, 0) < System.currentTimeMillis()) {
//                RateDialog().show(fragmentManager, null)
//            } else {
//                preferences.edit {
//                    putLong(DAY_INSTALLATION, System.currentTimeMillis() + 259200000L)
//                }
//            }
//        }
//    }
}