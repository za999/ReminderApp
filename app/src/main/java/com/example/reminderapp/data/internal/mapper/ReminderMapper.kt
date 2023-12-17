package com.example.reminderapp.data.internal.mapper

import com.example.reminderapp.data.internal.model.ReminderEntity
import com.example.reminderapp.domain.model.Reminder
import com.example.reminderapp.domain.model.ReminderInfo
import com.example.reminderapp.util.fromLocalDateToMillis
import com.example.reminderapp.util.fromLocalTimeToMillis
import com.example.reminderapp.util.getLocalDateFromMillis
import com.example.reminderapp.util.getLocalTimeFromMillis

fun List<ReminderEntity>.toReminderInfo(): ReminderInfo {
     val listOfReminders = this.map {
        Reminder(
            id = it.id,
            title = it.title,
            description = it.description,
            date = it.date?.getLocalDateFromMillis(),
            time = it.time?.getLocalTimeFromMillis(),
            isDone = it.done
        )
    }

    return ReminderInfo(
        reminders = listOfReminders
    )

}

fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        date = this.date?.fromLocalDateToMillis(),
        time = this.time?.fromLocalTimeToMillis(),
        done = this.isDone
    )
}

fun ReminderEntity.toReminder(): Reminder {
    return Reminder(
        id = this.id,
        title = this.title,
        description = this.description,
        date = this.date?.getLocalDateFromMillis(),
        time = this.time?.getLocalTimeFromMillis(),
        isDone = this.done
    )
}