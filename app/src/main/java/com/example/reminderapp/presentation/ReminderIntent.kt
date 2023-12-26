package com.example.reminderapp.presentation

import com.example.reminderapp.domain.model.Reminder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

sealed interface ReminderIntent {
    data class SetTitle(val title: String): ReminderIntent
    data class SetDescription(val description: String): ReminderIntent
    data class SetDate(val date: LocalDate): ReminderIntent
    data class SetTime(val time: LocalTime): ReminderIntent
    data class SetReminderAsDone(val isCheckedAsDone: Boolean, val reminder: Reminder): ReminderIntent
    data class AddingDateAndTime(val addingDateAndTime: Boolean): ReminderIntent
    data class EditingReminder(val reminderToBeEdited: Reminder): ReminderIntent
    object AddingNewReminder: ReminderIntent
    data class ShowReminders(val currentCategory: ReminderCategory): ReminderIntent
    object SaveReminder: ReminderIntent

}