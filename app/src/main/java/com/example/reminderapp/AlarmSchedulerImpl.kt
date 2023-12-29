package com.example.reminderapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.reminderapp.domain.ReminderRepository
import com.example.reminderapp.domain.model.Reminder
import com.example.reminderapp.util.ResultOf
import com.example.reminderapp.util.fromLocalDateTimeToMillis
import com.example.reminderapp.util.fromLocalDateToMillis
import com.example.reminderapp.util.fromLocalTimeToMillis
import com.example.reminderapp.util.getAlarmTriggerTime
import com.example.reminderapp.util.untilEndOfDay
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

private const val LOG_TAG = "AlarmScheduler"
class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: ReminderRepository,
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override suspend fun shouldSchedule() {
        val latestReminder = getLatestReminder()
        if (latestReminder != null) {
            if (isAnotherDay(latestReminder.date) ||
                isTodayButLaterInTime(latestReminder.date, latestReminder.time) ||
                isAnotherDateWithTime(latestReminder.date, latestReminder.time)
            ) {
                schedule(latestReminder)
            } else {
                return
            }
        } else {
            Log.e(LOG_TAG, "Error occurred fetching latest reminder!")
        }
    }

    override fun cancel(reminder: Reminder) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                reminder.hashCode(), // Requestcode needs to be exact the same as when scheduling Reminder.
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    private suspend fun getLatestReminder(): Reminder? {
        return when(val result = repository.getLatestReminderEntity()) {
            is ResultOf.Success -> result.value
            is ResultOf.Error -> null
        }
    }

    private fun schedule(reminder: Reminder) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("REMINDER_ID", reminder.id)
        }

        val alarm = reminder.date?.getAlarmTriggerTime(reminder.time)!!
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarm,
            PendingIntent.getBroadcast(
                context,
                reminder.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    // If reminder is set another date than today with time.
    private fun isAnotherDateWithTime(selectedDate: LocalDate?, selectedTime: LocalTime?): Boolean {
        if (selectedDate == null || selectedTime == null) return false
        val endOfDay = selectedDate.untilEndOfDay()
        return selectedDate.atTime(selectedTime).fromLocalDateTimeToMillis() > endOfDay
    }


    // If reminder is set today but later in time.
    private fun isTodayButLaterInTime(selectedDate: LocalDate?, selectedTime: LocalTime?): Boolean {
        if (selectedDate == null || selectedTime == null) return false
        val currentTimeMillis = LocalTime.now().fromLocalTimeToMillis()
        val endOfDay = selectedDate.untilEndOfDay()
        return selectedDate.fromLocalDateToMillis() < endOfDay && selectedTime.fromLocalTimeToMillis() > currentTimeMillis
    }

    // If reminder is set just another day than today
    private fun isAnotherDay(selectedDate: LocalDate?): Boolean {
        if (selectedDate == null) return false
        val today = LocalDate.now().fromLocalDateToMillis()
        return selectedDate.fromLocalDateToMillis() > today
    }


}