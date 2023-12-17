package com.example.reminderapp.data.internal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_ID")
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "date")
    val date: Long? = null,
    @ColumnInfo(name = "time")
    val time: Long? = null,
    @ColumnInfo(name = "deleted")
    val done: Boolean = false
)
