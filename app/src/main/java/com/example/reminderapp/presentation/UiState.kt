package com.example.reminderapp.presentation

import com.example.reminderapp.domain.model.Reminder
import java.time.LocalDate
import java.time.LocalTime

data class UiState(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val isEditingReminder: Boolean = false,
    val errorMessage: String? = null,
    val currentCategory: ReminderCategory = ReminderCategory.VIEWNONE,
    val currentListToDisplay: List<Reminder> = emptyList(),
    val allReminders: List<Reminder> = emptyList(),
    val dailyReminders: List<Reminder> = emptyList(),
    val scheduledReminders: List<Reminder> = emptyList(),
    val headerTitle: String = ""

)