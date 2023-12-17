package com.example.reminderapp.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.reminderapp.domain.model.Reminder
import com.example.reminderapp.presentation.ReminderIntent
import com.example.reminderapp.util.getLocalDateTimeFromMillisToString

@ExperimentalMaterialApi
@Composable
fun ReminderCard(
    modifier: Modifier = Modifier,
    reminder: Reminder,
    onIntent: (ReminderIntent) -> Unit,
    showBottomSheet: () -> Unit,
    sheetState: ModalBottomSheetState
) {

    var isCheckedAsDone by remember { mutableStateOf(false) }
    //TODO: change to only show time if reminder has a time set, if not then show date (except for daily reminders).
    val date = reminder.date?.toString()
    val time = reminder.time?.toString()

    androidx.compose.material.Card(
        shape = RoundedCornerShape(size = 12.dp),
        backgroundColor = Color.White,
        onClick = {
            onIntent(ReminderIntent.EditingReminder(reminderToBeEdited = reminder))
            if (!sheetState.isVisible) {
                showBottomSheet()
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = reminder.title,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Text(
                        text = reminder.description,
                        fontWeight = FontWeight.Light,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
                Checkbox(
                    checked = isCheckedAsDone,
                    onCheckedChange = { isCheckedAsDone ->
                        onIntent(
                            ReminderIntent.SetReminderAsDone(
                                isCheckedAsDone = isCheckedAsDone,
                                reminder = reminder
                            )
                        )
                    }
                )

            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (date != null) {
                    Text(
                        text = date,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                    )
                }

                if (time != null) {
                    Text(
                        text = time,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                    )
                }
            }


        }
    }
}

//
//@Preview
//@Composable
//fun ReminderCardPreview() {
//
//    ReminderCard(
//        reminder = Reminder(
//            0,
//            "Helloooooooooo",
//            "test",
//            null
//        )
//    )
//}