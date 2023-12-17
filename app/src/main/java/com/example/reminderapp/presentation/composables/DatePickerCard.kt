@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reminderapp.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.reminderapp.R
import com.example.reminderapp.presentation.ReminderIntent
import com.example.reminderapp.presentation.ReminderState
import com.example.reminderapp.util.getLocalDateFromMillis
import com.example.reminderapp.util.getLocalDateTimeFromMillisToString
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DatePickerCard(
    onIntent: (ReminderIntent) -> Unit,
    state: ReminderState
) {
    // State that is for the date to show on the button
    var date by remember {
        mutableStateOf("dd/mm/yy")
    }
    // State to open the datepicker or not
    var showDatePickerDialog by remember {
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
                onClick = { showDatePickerDialog = true }) {// If clicking on the button, we show the datepicker.
                Text(
                    color = Color.Black,
                    text = date // Here we display the date on the actual button
                )
            }
        }

    }

    // Show only when datepickerdialog is true
    // We pass two lambda functions that will alter the state of the date and showDatePickerDialog
    // By doing this, we are state hoisting, meaning we create lambda callback functions
    // That alters the states depending on a specific situation, like for example confirming the date
    // Or canceling, which closes the datepicker.
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDateSelected = { localDate: LocalDate, selectedDate: String ->
                onIntent(ReminderIntent.SetDate(localDate))
                date = selectedDate
            },
            onDismissRequest = { showDatePickerDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
     onDateSelected: (LocalDate ,String) -> Unit,
     onDismissRequest: () -> Unit,
) {

    val datePickerState = rememberDatePickerState() //TODO: set initial selectedDatemillis if there is one

    // Converting the selected date to a long and then a string
    val selectedDate = datePickerState.selectedDateMillis.let {
        it?.getLocalDateTimeFromMillisToString()
    } ?: ""

    val selectedDateInLocalDataTime = datePickerState.selectedDateMillis.let {
        it?.getLocalDateFromMillis()
    } ?: LocalDate.now()

    DatePickerDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDateInLocalDataTime ,selectedDate) // Setting new date
                onDismissRequest() // Closing the datepicker dialog
            }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismissRequest() } // Closing the datepicker dialog
            ) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }


}

//@Preview
//@Composable
//fun DatePickerCardPreview() {
//    DatePickerCard()
//}
