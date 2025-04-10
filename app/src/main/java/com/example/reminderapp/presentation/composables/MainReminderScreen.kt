@file:OptIn(ExperimentalMaterialApi::class)

package com.example.reminderapp.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.reminderapp.presentation.ReminderCategory
import com.example.reminderapp.presentation.ReminderIntent
import com.example.reminderapp.presentation.UiState
import com.example.reminderapp.presentation.ReminderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainReminderScreen(
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel,
    backgroundColor: Color,
    onNavigate: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()



    MainReminderScreen(
        modifier = modifier,
        backgroundColor,
        viewModel::onIntent,
        state = state.value,
        sheetState = sheetState,
        scope = scope,
        onNavigate = onNavigate

    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainReminderScreen(
    modifier: Modifier,
    backgroundColor: Color,
    onIntent: (ReminderIntent) -> Unit,
    state: UiState,
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    onNavigate: () -> Unit
) {

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            BottomSheetContent(
                modifier = modifier,
                state = state,
                onIntent = onIntent,
                onCancel = { scope.launch { sheetState.hide() } },
                sheetState = sheetState,
                headerTitle = "New Reminder"
            )
        },
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topEnd = 12.dp,
            topStart = 12.dp
        ),
        sheetElevation = 8.dp
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Reminder App",
                            color = Color.White,
                            modifier = modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    backgroundColor = backgroundColor,
                )
                Column(
                    modifier = modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .offset(x = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CategoryTabCard(
                            headerTitle = "Today",
                            amountOfReminders = state.dailyReminders.size,
                            modifier = modifier
                                .height(75.dp)
                                .weight(1f),
                            shape = RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomStart = 10.dp,
                                bottomEnd = 10.dp
                            ),
                            onIntent = {onIntent(ReminderIntent.ShowReminders(ReminderCategory.VIEWDAILY))},
                            onNavigateToShowReminders = onNavigate
                        )
                        CategoryTabCard(
                            headerTitle = "Scheduled",
                            amountOfReminders = state.scheduledReminders.size,
                            modifier = modifier
                                .height(75.dp)
                                .weight(1f),
                            shape = RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomStart = 10.dp,
                                bottomEnd = 10.dp
                            ),
                            onIntent = {onIntent(ReminderIntent.ShowReminders(ReminderCategory.VIEWSCHEDULED))},
                            onNavigateToShowReminders = onNavigate
                        )
                    }

                    Spacer(modifier = modifier.height(10.dp))

                    CategoryTabCard(
                        headerTitle = "All",
                        amountOfReminders = state.allReminders.size,
                        modifier = modifier
                            .height(75.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 10.dp,
                            bottomStart = 10.dp,
                            bottomEnd = 10.dp
                        ),
                        onIntent = {onIntent(ReminderIntent.ShowReminders(ReminderCategory.VIEWALL))},
                        onNavigateToShowReminders = onNavigate
                    )
                }

                ExtendedFloatingActionButton(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.End),
                    containerColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    text = { Text(text = "Create Reminder", fontWeight = FontWeight.Bold) },
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Create Reminder") },
                    onClick = {
                        onIntent(ReminderIntent.AddingNewReminder)
                        if (!sheetState.isVisible) {
                            scope.launch { sheetState.show() }
                        }
                    }
                )
            }
        }
    }
}

