package com.example.reminderapp.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.reminderapp.R
import com.example.reminderapp.domain.model.Reminder
import com.example.reminderapp.presentation.ReminderIntent
import com.example.reminderapp.presentation.ReminderState
import com.example.reminderapp.presentation.ReminderViewModel
import com.example.reminderapp.ui.theme.DarkBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ReminderDisplay(
    backgroundColor: Color,
    onHome: () -> Unit,
    viewModel: ReminderViewModel
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()


    ReminderDisplay(
        state = state.value,
        listOfReminders = state.value.currentListToDisplay,
        onIntent = viewModel::onIntent,
        headerTitle = state.value.headerTitle,
        sheetState = sheetState,
        scope = scope,
        onHome = onHome,
        backgroundColor = backgroundColor
    )
}

@ExperimentalMaterialApi
@Composable
fun ReminderDisplay(
    modifier: Modifier = Modifier,
    state: ReminderState,
    listOfReminders: List<Reminder>,
    onIntent: (ReminderIntent) -> Unit,
    headerTitle: String,
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    onHome: () -> Unit,
    backgroundColor: Color
) {
    val hideBottomSheet: () -> Unit = { scope.launch { sheetState.hide() }}
    val showBottomSheet: () -> Unit = { scope.launch { sheetState.show() }}

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "$headerTitle Reminders",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )},
                backgroundColor = backgroundColor,
                navigationIcon = {
                    IconButton(onClick = onHome) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { values ->
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    androidx.compose.material.Text(
                        text = "Edit Reminder",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    BottomSheetView(
                        state = state,
                        onIntent = onIntent,
                        onCancel = hideBottomSheet,
                        sheetState = sheetState
                    )

                }
            }
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = DarkBlue)
                    .padding(values)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .weight(9.0F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = headerTitle
                            .plus("   ")
                            .plus(listOfReminders.size),
                        modifier = modifier
                            .align(Alignment.Start)
                            .padding(horizontal = 15.dp, vertical = 5.dp),
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = modifier.height(30.dp))

                    Box(
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(15.dp)
                                .background(Color.Transparent),
                            verticalArrangement = Arrangement.spacedBy(space = 15.dp)
                        ) {
                            items(listOfReminders.size) { item ->
                                ReminderCard(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .requiredHeight(125.dp),
                                    reminder = listOfReminders[item],
                                    onIntent = onIntent,
                                    showBottomSheet = showBottomSheet,
                                    sheetState = sheetState
                                )
                            }
                        }

                        if (listOfReminders.isEmpty()) {
                            Box(
                                modifier = modifier
                                    .fillMaxSize()
                                    .background(Color.Transparent)
                                    .padding(all = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No reminders found!",
                                    fontWeight = FontWeight.Light,
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
