package com.belous.v.clrc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.belous.v.clrc.ui.Screen
import com.belous.v.clrc.ui.component.EventSnackbar
import com.belous.v.clrc.ui.component.ProgressIndicator
import com.belous.v.clrc.ui.feature_main.MainScreen
import com.belous.v.clrc.ui.feature_yeelight.YeelightScreen
import com.belous.v.clrc.ui.theme.CLRCTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainStates: MainStates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val scaffoldState = rememberScaffoldState()
            val navController = rememberNavController()

            LaunchedEffect(key1 = Unit) {
                mainStates.eventState.collectLatest { event ->
                    when (event) {
                        is MainStates.EventState.ExceptionEvent -> {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = event.exception.message
                                    ?: getString(R.string.unexpected_error),
                                actionLabel = getString(R.string.ok),
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        is MainStates.EventState.MessageEvent -> {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = getString(event.messageId),
                                actionLabel = getString(R.string.ok),
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                    }
                }
            }

            CLRCTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = { SnackbarHost(it) { data -> EventSnackbar(data) } }
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = Screen.MainScreen.route
                    ) {
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
                            YeelightScreen(navController = navController)
                        }
                    }
                }

                ProgressIndicator(state = mainStates.loadingState.collectAsState(initial = false))
            }
        }
    }
}