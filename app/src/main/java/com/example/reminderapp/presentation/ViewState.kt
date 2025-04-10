package com.example.reminderapp.presentation

sealed class ViewState(val route: String) {
    object MainScreen: ViewState("main_screen")
    object RemindersScreen: ViewState("reminders_screen")
}
