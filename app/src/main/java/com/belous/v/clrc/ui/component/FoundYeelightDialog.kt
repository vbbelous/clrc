package com.belous.v.clrc.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.belous.v.clrc.R
import com.belous.v.clrc.data.db.entity.YeelightEntity
import com.belous.v.clrc.ui.theme.AppTheme

@Composable
fun FoundYeelightDialog(
    dialogTitle: String,
    dialogState: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    yeelightEntityList: List<YeelightEntity>,
    onSelectAction: (YeelightEntity) -> Unit
) {
    val selectedYeelightEntity = remember { mutableStateOf(-1) }
    val enteredText = remember { mutableStateOf("") }
    val saveButtonState = derivedStateOf { selectedYeelightEntity.value != -1 }

    Dialog(onDismissRequest = { dialogState.value = false }) {
        Card(
            elevation = AppTheme.dimensions.small,
            shape = RoundedCornerShape(AppTheme.dimensions.small),
            modifier = modifier
                .padding(AppTheme.dimensions.medium)
                .height(368.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = AppTheme.dimensions.xxLarge,
                        vertical = AppTheme.dimensions.large
                    )
            ) {
                Text(
                    text = dialogTitle,
                    color = AppTheme.colors.primaryText,
                    style = AppTheme.typography.mediumNormal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.mainBg)
                )

                Column(
                    modifier = Modifier
                        .padding(vertical = AppTheme.dimensions.large)
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(state = rememberScrollState(), enabled = true)
                ) {
                    yeelightEntityList.forEachIndexed(action = { index, yeelightEntity ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (index == selectedYeelightEntity.value) AppTheme.colors.itemBg
                                    else Color.Transparent
                                )
                                .clickable {
                                    selectedYeelightEntity.value = index
                                    if (enteredText.value.isEmpty()) {
                                        enteredText.value = yeelightEntity.name
                                    }
                                }
                                .padding(AppTheme.dimensions.medium)) {
                            Text(
                                text = "${index + 1}.",
                                modifier = Modifier.padding(end = AppTheme.dimensions.small),
                                color = AppTheme.colors.primaryText,
                                style = AppTheme.typography.mediumNormal
                            )
                            Text(
                                text = yeelightEntity.name,
                                color = AppTheme.colors.primaryText,
                                style = AppTheme.typography.mediumNormal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis

                            )
                        }
                    })
                }

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
                        .fillMaxWidth()
                ) {
                    AppButton(
                        text = stringResource(id = R.string.cancel),
                        modifier = Modifier
                            .padding(end = AppTheme.dimensions.medium)
                            .weight(1f)
                    ) {
                        dialogState.value = false
                    }
                    AppButton(
                        text = stringResource(id = R.string.save),
                        modifier = Modifier
                            .padding(start = AppTheme.dimensions.medium)
                            .weight(1f),
                        enabled = saveButtonState.value
                    ) {
                        onSelectAction(
                            yeelightEntityList[selectedYeelightEntity.value].copy(name = enteredText.value)
                        )
                        dialogState.value = false
                    }
                }
            }
        }
    }
}