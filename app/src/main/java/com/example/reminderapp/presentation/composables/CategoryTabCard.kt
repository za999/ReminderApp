@file:OptIn(ExperimentalMaterialApi::class)

package com.example.reminderapp.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalMaterialApi
@Composable
fun CategoryTabCard(
    modifier: Modifier = Modifier,
    shape: Shape,
    headerTitle: String,
    amountOfReminders: Int,
    onNavigateToShowReminders: () -> Unit,
    onIntent: () -> Unit,
    backgroundColor: Color = Color.White,
) {
    androidx.compose.material.Card(
        modifier = modifier.clip(shape = shape),
        backgroundColor = backgroundColor,
        onClick = {
            onIntent()
            onNavigateToShowReminders()
        }
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = headerTitle,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = amountOfReminders.toString().plus(" ").plus("reminders"),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )

            // TODO: Add image here on the bottom right corner

        }
    }

}
//
//@Preview
//@Composable
//fun CategoryTabCardPreview() {
//    CategoryTabCard(
//        headerTitle = "All", listOfReminders = emptyList(), shape = RoundedCornerShape(
//            topStart = 10.dp,
//            topEnd = 10.dp,
//            bottomStart = 10.dp,
//            bottomEnd = 10.dp
//        )
//    )
//}