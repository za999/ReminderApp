package com.example.reminderapp

import com.example.reminderapp.domain.model.Reminder

interface AlarmScheduler {
    suspend fun shouldSchedule()
    fun cancel(reminder: Reminder)
}