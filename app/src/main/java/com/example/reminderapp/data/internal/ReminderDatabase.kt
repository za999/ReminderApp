package com.example.reminderapp.data.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reminderapp.data.internal.model.ReminderEntity

@Database(entities = [ReminderEntity::class], version = 1)
abstract class ReminderDatabase : RoomDatabase() {

    abstract val reminderDao: ReminderDao
}