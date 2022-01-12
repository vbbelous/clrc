package com.belous.v.clrc.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.belous.v.clrc.R
import com.belous.v.clrc.ui.theme.AppTheme

@Composable
fun ConfirmDialog(
    dialogState: MutableState<Boolean>,
    title: String = "",
    titleId: Int = 0,
    action: () -> Unit
) {

    Dialog(onDismissRequest = { dialogState.value = false }) {
        Card(
            elevation = AppTheme.dimensions.small,
            shape = RoundedCornerShape(AppTheme.dimensions.small),
            modifier = Modifier.padding(AppTheme.dimensions.medium)
        ) {
            Column(modifier = Modifier.padding(AppTheme.dimensions.xLarge)) {
                Text(
                    text = title.ifEmpty {
                        stringResource(id = if (titleId != 0) titleId else R.string.are_you_sure)
                    },
                    color = AppTheme.colors.primaryText,
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.largeSemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .padding(top = AppTheme.dimensions.xLarge),
                    verticalAlignment = Alignment.Bottom
                ) {
                    AppButton(
                        modifier = Modifier
                            .padding(end = AppTheme.dimensions.small)
                            .weight(1f),
                        text = stringResource(R.string.cancel)
                    ) {
                        dialogState.value = false
                    }
                    AppButton(
                        modifier = Modifier
                            .padding(start = AppTheme.dimensions.small)
                            .weight(1f),
                        text = stringResource(R.string.ok)
                    ) {
                        action()
                        dialogState.value = false
                    }
                }
            }
        }
    }
}