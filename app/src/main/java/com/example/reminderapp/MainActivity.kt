package com.example.reminderapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reminderapp.domain.model.Reminder
import com.example.reminderapp.presentation.Navigation
import com.example.reminderapp.presentation.ReminderIntent
import com.example.reminderapp.presentation.ReminderState
import com.example.reminderapp.presentation.ReminderViewModel
import com.example.reminderapp.presentation.composables.MainReminderScreen
import com.example.reminderapp.ui.theme.DarkBlue
import com.example.reminderapp.ui.theme.ReminderAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReminderAppTheme {
                Navigation(viewModel = hiltViewModel())

            }
        }
    }



}

