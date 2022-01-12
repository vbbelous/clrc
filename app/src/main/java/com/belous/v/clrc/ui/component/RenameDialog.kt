package com.belous.v.clrc.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.belous.v.clrc.R
import com.belous.v.clrc.ui.theme.AppTheme

@Composable
fun RenameDialog(
    dialogState: MutableState<Boolean>,
    name: String,
    action: (String) -> Unit
) {
    val enteredText = remember { mutableStateOf(name) }

    Dialog(onDismissRequest = { dialogState.value = false }) {
        Card(
            elevation = AppTheme.dimensions.small,
            shape = RoundedCornerShape(AppTheme.dimensions.small),
            modifier = Modifier.padding(AppTheme.dimensions.medium)
        ) {
            Column(modifier = Modifier.padding(AppTheme.dimensions.xLarge)) {
                Text(
                    text = stringResource(id = R.string.change_name),
                    color = AppTheme.colors.primaryText,
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.mediumSemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = enteredText.value,
                    onValueChange = { enteredText.value = it },
                    modifier = Modifier.padding(vertical = AppTheme.dimensions.medium),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = AppTheme.colors.secondaryText,
                        focusedBorderColor = AppTheme.colors.disabledText,
                        unfocusedBorderColor = AppTheme.colors.itemBg,
                        cursorColor = AppTheme.colors.disabledText,
                        textColor = AppTheme.colors.primaryText
                    ),
                    textStyle = AppTheme.typography.mediumNormal,
                    label = { Text(text = stringResource(id = R.string.name)) },
                    singleLine = true
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
                        action(enteredText.value)
                        dialogState.value = false
                    }
                }
            }
        }
    }
}