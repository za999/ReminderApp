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
    modifier: Modifier,
    reminder: Reminder,
    onEdit: () -> Unit,
    onReminderDone: () -> Unit,
    showBottomSheet: () -> Unit,
    sheetState: ModalBottomSheetState
) {

    val isCheckedAsDone by remember { mutableStateOf(false) }


    androidx.compose.material.Card(
        shape = RoundedCornerShape(size = 12.dp),
        backgroundColor = Color.White,
        onClick = {
            onEdit()
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
                    onCheckedChange = { onReminderDone() }
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
                if (reminder.date != null) {
                    Text(
                        text = reminder.date.toString(),
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                    )
                }

                if (reminder.time != null) {
                    Text(
                        text = reminder.time.toString(),
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}