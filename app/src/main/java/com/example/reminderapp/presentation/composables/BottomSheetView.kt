package com.example.reminderapp.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reminderapp.presentation.ReminderIntent
import com.example.reminderapp.presentation.ReminderState
import kotlinx.coroutines.CoroutineScope

@ExperimentalMaterialApi
@Composable
fun BottomSheetView(
    modifier: Modifier = Modifier,
    state: ReminderState,
    onIntent: (ReminderIntent) -> Unit,
    onCancel: () -> Unit,
    sheetState: ModalBottomSheetState
) {



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
            DatePickerCard(onIntent = onIntent, state = state)
            TimePickerCard(onIntent = onIntent, state = state)
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
//                    onHandleIntent(ReminderIntent.HideBottomSheet)
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
                },
            )
        }

    }

}
//
//@Preview
//@Composable
//fun ReminderDetailDialogPreview() {
//    BottomSheetView()
//}