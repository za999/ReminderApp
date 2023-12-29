package com.example.reminderapp.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reminderapp.presentation.composables.MainReminderScreen
import com.example.reminderapp.presentation.composables.ReminderDisplay
import com.example.reminderapp.ui.theme.DarkBlue



@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun Navigation(viewModel: ReminderViewModel) {

    val navController = rememberNavController()
    val keyboardController = LocalSoftwareKeyboardController.current
    val navigateBack: () -> Unit = {
        keyboardController?.hide()
        navController.navigateUp()
    }

    NavHost(navController = navController, startDestination = ViewState.MainScreen.route) {
        composable(
            route = ViewState.MainScreen.route
        ) {
            MainReminderScreen(
                backgroundColor = DarkBlue,
                viewModel = viewModel,
                onNavigate = {
                    navController.navigate(ViewState.RemindersScreen.route)
                })
        }
        composable(route = ViewState.RemindersScreen.route) {
            ReminderDisplay(
                backgroundColor = DarkBlue,
                onHome = navigateBack,
                viewModel = viewModel
            )
        }
    }

}