package com.example.reminderapp.presentation.composables

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.reminderapp.R
import com.example.reminderapp.presentation.ReminderIntent
import com.example.reminderapp.presentation.ReminderState
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

@Composable
fun TimePickerCard(
    onIntent: (ReminderIntent) -> Unit,
    state: ReminderState
) {

    // State that is for the date to show on the button
    var time by remember {
        mutableStateOf("hh:mm")
    }
    // State to open the datepicker or not
    var showTimePickerDialog by remember {
        mutableStateOf(false)
    }


    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar_month_24),
                contentDescription = null
            )
            TextButton(
                colors = ButtonDefaults.textButtonColors(Color.LightGray),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    showTimePickerDialog = true
                }) {// If clicking on the button, we show the datepicker.
                Text(
                    color = Color.Black,
                    text = time // Here we display the date on the actual button
                )
            }
        }
    }

    if (showTimePickerDialog) {
        TimePickerDialog(
            onTimeSelected = { localTime: LocalTime, selectedTime: String ->
                onIntent(ReminderIntent.SetTime(localTime))
                time = selectedTime
            },
            onDismissRequest = { showTimePickerDialog = false }
        )
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onTimeSelected: (LocalTime, String) -> Unit,
    onDismissRequest: () -> Unit
) {

    val timePickerState = rememberTimePickerState(is24Hour = true)



    androidx.compose.material3.AlertDialog(
        modifier = Modifier
            .fillMaxWidth(),
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.White.copy(alpha = 0.4F)
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeInput(state = timePickerState)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // Dismiss button
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = "Cancel")
                }
                // Confirm button
                TextButton(
                    onClick = {
                        onTimeSelected(
                            LocalTime.of(timePickerState.hour, timePickerState.minute) ,
                            LocalTime.of(timePickerState.hour, timePickerState.minute).toString(),
                        )
                        onDismissRequest()
                    }) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}

