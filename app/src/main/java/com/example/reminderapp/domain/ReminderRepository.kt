package com.example.reminderapp.domain

import com.example.reminderapp.data.internal.model.ReminderEntity
import com.example.reminderapp.domain.model.Reminder
import com.example.reminderapp.domain.model.ReminderInfo
import com.example.reminderapp.util.ResultOf
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    suspend fun insertOrUpdateReminder(reminderEntity: ReminderEntity)

    fun getAllReminders(): Flow<ResultOf<ReminderInfo>>

    fun getScheduledReminders(today: Long): Flow<ResultOf<ReminderInfo>>

    fun getDailyReminders(today: Long, tomorrow: Long): Flow<ResultOf<ReminderInfo>>

    suspend fun getReminderById(id: Long): ResultOf<Reminder>

    suspend fun getLatestReminderEntity(): ResultOf<Reminder>


}