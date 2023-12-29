package com.example.reminderapp.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reminderapp.presentation.ReminderIntent
import com.example.reminderapp.presentation.UiState

@ExperimentalMaterialApi
@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    state: UiState,
    onIntent: (ReminderIntent) -> Unit,
    onCancel: () -> Unit,
    sheetState: ModalBottomSheetState,
    headerTitle: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = headerTitle,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = { newTitle -> onIntent(ReminderIntent.SetTitle(newTitle)) },
                placeholder = { Text(text = "Title") }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = { newDesc -> onIntent(ReminderIntent.SetDescription(newDesc)) },
                placeholder = { Text(text = "Description") }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerCard(onIntent = onIntent, date = state.date)
                TimePickerCard(onIntent = onIntent, time = state.time)
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                ExtendedFloatingActionButton(
                    text = { Text(text = "Cancel") },
                    onClick = {
                        if (sheetState.isVisible) {
                            onCancel()
                        }
                    },
                )
                ExtendedFloatingActionButton(
                    text = { Text(text = "Save") },
                    onClick = {
                        onIntent(ReminderIntent.SaveReminder)
                        if (sheetState.isVisible) {
                            onCancel()
                        }
                    }
                )
            }

        }
    }

}
