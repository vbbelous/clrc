package com.belous.v.clrc.ui.feature_yeelight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.belous.v.clrc.R
import com.belous.v.clrc.ui.theme.AppTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun YeelightScreen(
    viewModel: YeelightViewModel
) {
    val yeelight by viewModel.yeelightData.observeAsState()

    val isActive = yeelight?.isActive == true
    val isOnline = yeelight?.isOnline == true
    val isPower = yeelight?.isPower == true

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { viewModel.sendEvent(YeelightViewModel.YeelightEvent.Update) }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(AppTheme.colors.itemBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(AppTheme.dimensions.xLarge)
            ) {
                Text(
                    text = if (isOnline) {
                        if (isPower) "${yeelight?.bright}%"
                        else stringResource(id = R.string.off)
                    } else stringResource(id = R.string.offline),
                    modifier = Modifier
                        .align(Alignment.Center),
                    color = AppTheme.colors.primaryText,
                    fontWeight = FontWeight.Normal,
                    fontSize = if (isOnline) 124.sp else 64.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = "${stringResource(id = R.string.cl_temp)} ${yeelight?.ct ?: 4200}K",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        style = AppTheme.typography.mediumNormal,
                        color = AppTheme.colors.secondaryText
                    )

                    Text(
                        text = stringResource(id = R.string.cl_mode),
                        modifier = Modifier.padding(end = AppTheme.dimensions.small),
                        style = AppTheme.typography.mediumNormal,
                        color = AppTheme.colors.secondaryText
                    )
                    Icon(
                        imageVector = if (isActive) Icons.Outlined.DarkMode
                        else Icons.Outlined.LightMode,
                        contentDescription = stringResource(
                            id = if (isActive) R.string.night_mode
                            else R.string.light_mode
                        ),
                        tint = AppTheme.colors.secondaryText
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    Modifier
                        .padding(bottom = AppTheme.dimensions.xSmall)
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    YeelightButton(modifier = Modifier
                        .padding(end = AppTheme.dimensions.xSmall)
                        .fillMaxHeight()
                        .weight(1f),
                        onClickAction = { viewModel.sendEvent(YeelightViewModel.YeelightEvent.Moonlight) }) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.moonlight),
                                modifier = Modifier.padding(bottom = AppTheme.dimensions.small),
                                style = AppTheme.typography.smallNormal,
                                color = AppTheme.colors.secondaryText
                            )
                            Box(
                                modifier = Modifier
                                    .padding(top = AppTheme.dimensions.small)
                                    .clip(RoundedCornerShape(AppTheme.dimensions.small))
                                    .size(
                                        width = AppTheme.dimensions.xxLarge,
                                        height = AppTheme.dimensions.medium
                                    )
                                    .background(AppTheme.colors.secondaryText)
                                    .padding(AppTheme.dimensions.xSmall)
                                    .clip(RoundedCornerShape(AppTheme.dimensions.small))
                                    .background(if (isActive) AppTheme.colors.orange else AppTheme.colors.mainBg)
                            )
                        }
                    }
                    YeelightButton(
                        modifier = Modifier
                            .padding(start = AppTheme.dimensions.xSmall)
                            .weight(1f),
                        onClickAction = { viewModel.sendEvent(YeelightViewModel.YeelightEvent.Power) }) {
                        Text(
                            text = stringResource(id = R.string.power),
                            style = AppTheme.typography.smallNormal,
                            color = if (isOnline) {
                                if (isPower) AppTheme.colors.green
                                else AppTheme.colors.red
                            } else AppTheme.colors.secondaryText
                        )
                    }
                }
                Row(
                    Modifier
                        .padding(vertical = AppTheme.dimensions.xSmall)
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    YeelightButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        onClickAction = { viewModel.sendEvent(YeelightViewModel.YeelightEvent.BrightMinus) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = stringResource(id = R.string.minus),
                            tint = AppTheme.colors.primaryText
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(AppTheme.colors.mainBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.bright),
                            style = AppTheme.typography.smallNormal,
                            color = AppTheme.colors.secondaryText
                        )
                    }
                    YeelightButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        onClickAction = { viewModel.sendEvent(YeelightViewModel.YeelightEvent.BrightPlus) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(id = R.string.plus),
                            tint = AppTheme.colors.primaryText
                        )
                    }
                }
                Row(
                    Modifier
                        .padding(vertical = AppTheme.dimensions.xSmall)
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    for (i in 0..100 step 25) {
                        val value = if (i == 0) 1 else i
                        YeelightButton(
                            modifier = Modifier
                                .padding(
                                    start = if (i == 0) 0.dp else AppTheme.dimensions.xSmall,
                                    end = if (i == 100) 0.dp else AppTheme.dimensions.xSmall
                                )
                                .fillMaxHeight()
                                .weight(1f),
                            onClickAction = {
                                viewModel.sendEvent(
                                    YeelightViewModel.YeelightEvent.ChangeBright(value)
                                )
                            }
                        ) {
                            Text(
                                text = "$value%",
                                style = AppTheme.typography.smallNormal,
                                color = AppTheme.colors.secondaryText
                            )
                        }
                    }
                }
                Row(
                    Modifier
                        .padding(top = AppTheme.dimensions.xSmall)
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    YeelightButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        onClickAction = { viewModel.sendEvent(YeelightViewModel.YeelightEvent.TempMinus) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = stringResource(id = R.string.minus),
                            tint = AppTheme.colors.primaryText
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(AppTheme.colors.mainBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.temp),
                            style = AppTheme.typography.smallNormal,
                            color = AppTheme.colors.secondaryText
                        )
                    }
                    YeelightButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        onClickAction = { viewModel.sendEvent(YeelightViewModel.YeelightEvent.TempPlus) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(id = R.string.plus),
                            tint = AppTheme.colors.primaryText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun YeelightButton(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier.fillMaxHeight(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.mainBg
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