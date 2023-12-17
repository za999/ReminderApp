package com.example.reminderapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reminderapp.data.internal.model.ReminderEntity
import com.example.reminderapp.di.IODispatcher
import com.example.reminderapp.domain.ReminderRepository
import com.example.reminderapp.domain.model.Reminder
import com.example.reminderapp.util.ResultOf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderRepository: ReminderRepository
    @Inject @IODispatcher lateinit var ioDispatcher: CoroutineDispatcher

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        val reminderId = intent?.getLongExtra("REMINDER_ID", -1L) ?: return
        Log.wtf("TAG", "onReceive id : $reminderId")
        if (reminderId > -1) {
            GlobalScope.launch(ioDispatcher) {
                try {
                    val currentReminder = when (val result = reminderRepository.getReminderById(id = reminderId)) {
                        is ResultOf.Success -> result.value
                        is ResultOf.Error -> null
                    }
                    sendNotification(context, currentReminder)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(context: Context?, reminder: Reminder?) {
        val tapResultIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            tapResultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = context?.let {
            NotificationCompat.Builder(it, ReminderApp.channelId)
                .setContentTitle(reminder?.title)
                .setContentText(reminder?.description)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        }
        //TODO: Implement permission handling
        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        notification?.let {
            reminder?.let {
                Log.wtf("TAG", "sendNotification: notify")
                notificationManager?.notify(
                    it.hashCode(),
                    notification
                )
            }
        }
    }

    companion object {
        const val NOTIFICATION_REQUEST_CODE = 100
    }
}