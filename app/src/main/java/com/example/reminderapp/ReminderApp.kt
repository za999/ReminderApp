package com.example.reminderapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ReminderApp: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }


    private fun createNotificationChannel() {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val channelId = "reminders"
        const val channelName = "Reminder notification channel"
        const val importance = NotificationManager.IMPORTANCE_HIGH
    }
}