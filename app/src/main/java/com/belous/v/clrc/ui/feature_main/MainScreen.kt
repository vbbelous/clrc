package com.belous.v.clrc.ui.feature_main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.belous.v.clrc.R
import com.belous.v.clrc.ui.theme.AppTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {

    val yeelightList by viewModel.yeelightList.collectAsState(initial = emptyList())
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is MainViewModel.MainUiEvent.YeelightFound -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = AppTheme.colors.itemBg
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.logo),
                    contentDescription = stringResource(id = R.string.logo)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                MainButton(
                    modifier = Modifier.fillMaxHeight(),
                    onClickAction = { viewModel.sendEvent(MainViewModel.MainEvent.UpdateAll) }) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = stringResource(id = R.string.refresh),
                        tint = AppTheme.colors.secondaryText,
                        modifier = Modifier.scale(1.4f)
                    )
                }
            }
        },
        content = {
            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = false),
                onRefresh = { viewModel.sendEvent(MainViewModel.MainEvent.UpdateAll) }) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = AppTheme.dimensions.small)
                ) {
                    items(yeelightList) { yeelight ->
                        Row(
                            modifier = Modifier
                                .padding(bottom = AppTheme.dimensions.xSmall)
                                .fillMaxWidth()
                                .height(AppTheme.dimensions.itemHeight)
                                .background(AppTheme.colors.itemBg)
                                .clickable { /*TODO*/ }
                        ) {
                            MainButton(modifier = Modifier.fillMaxHeight(),
                                onClickAction = {
                                    viewModel.sendEvent(
                                        MainViewModel.MainEvent.Power(yeelight.id, yeelight.isPower)
                                    )
                                }) {
                                Icon(
                                    imageVector =
                                    if (yeelight.isOnline) Icons.Outlined.PowerSettingsNew
                                    else Icons.Outlined.LinkOff,
                                    contentDescription = if (yeelight.isPower) stringResource(id = R.string.on)
                                    else stringResource(id = R.string.off),
                                    tint = if (yeelight.isOnline) {
                                        if (yeelight.isPower) AppTheme.colors.green
                                        else AppTheme.colors.red
                                    } else AppTheme.colors.secondaryText,
                                    modifier = Modifier
                                        .padding(vertical = AppTheme.dimensions.medium)
                                        .align(Alignment.CenterVertically)
                                        .scale(1.4f)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            ) {
                                Text(
                                    text = yeelight.name,
                                    color = AppTheme.colors.primaryText,
                                    style = AppTheme.typography.largeSemiBold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Icon(
                                    imageVector = if (yeelight.isActive) Icons.Outlined.DarkMode
                                    else Icons.Outlined.LightMode,
                                    contentDescription = if (yeelight.isActive) stringResource(id = R.string.night_mode)
                                    else stringResource(id = R.string.light_mode),
                                    modifier = Modifier
                                        .padding(AppTheme.dimensions.small)
                                        .scale(0.8f)
                                        .align(Alignment.Top),
                                    tint = AppTheme.colors.secondaryText
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(0.8f)
                            ) {
                                MainButton(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f), onClickAction = {
                                        viewModel.sendEvent(
                                            MainViewModel.MainEvent.BrightMinus(
                                                yeelight.id,
                                                yeelight.bright
                                            )
                                        )
                                    }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Remove,
                                        contentDescription = stringResource(id = R.string.minus),
                                        tint = AppTheme.colors.primaryText,
                                        modifier = Modifier.scale(2f)
                                    )
                                }

                                Text(
                                    text = "${yeelight.bright}",
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    color = AppTheme.colors.primaryText,
                                    style = AppTheme.typography.largeSemiBold,
                                    textAlign = TextAlign.Center
                                )

                                MainButton(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f), onClickAction = {
                                        viewModel.sendEvent(
                                            MainViewModel.MainEvent.BrightPlus(
                                                yeelight.id,
                                                yeelight.bright
                                            )
                                        )
                                    }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = stringResource(id = R.string.plus),
                                        tint = AppTheme.colors.primaryText,
                                        modifier = Modifier.scale(2f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.itemBg
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = -AppTheme.dimensions.small
        ),
        shape = RoundedCornerShape(0.dp),
        onClick = onClickAction,
        content = content
    )
}