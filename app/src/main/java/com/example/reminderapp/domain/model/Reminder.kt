package com.example.reminderapp.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class Reminder(
    val id: Long,
    val title: String,
    val description: String,
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val isDone: Boolean = false
)
