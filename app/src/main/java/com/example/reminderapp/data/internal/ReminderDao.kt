package com.example.reminderapp.data.internal

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.reminderapp.data.internal.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Upsert
    suspend fun upsertReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE deleted = 0")
    fun getAllRemindersFlow(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE deleted = 0 AND date IS NOT NULL AND date >= :todaysDate AND date < :nextDaysDate")
    fun getAllTodaysRemindersFlow(todaysDate: Long, nextDaysDate: Long) : Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE deleted = 0 AND date is NOT NULL")
    fun getScheduledReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE _ID = :id")
    suspend fun getReminderEntityById(id: Long): ReminderEntity

    @Query("SELECT * FROM reminders ORDER BY _ID DESC LIMIT 1")
    suspend fun getLatestReminderEntity(): ReminderEntity
}