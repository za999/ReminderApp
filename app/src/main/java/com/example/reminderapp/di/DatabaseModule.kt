package com.example.reminderapp.di

import android.content.Context
import androidx.room.Room
import com.example.reminderapp.data.internal.ReminderDao
import com.example.reminderapp.data.internal.ReminderDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideReminderDao(reminderDatabase: ReminderDatabase): ReminderDao =
        reminderDatabase.reminderDao


    @Provides
    @Singleton
    fun provideReminderDatabase(@ApplicationContext appContext: Context): ReminderDatabase =
        Room.databaseBuilder(
            appContext,
            ReminderDatabase::class.java,
            "ReminderData.db"
        ).build()

}