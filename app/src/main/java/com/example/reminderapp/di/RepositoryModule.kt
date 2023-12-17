package com.example.reminderapp.di

import com.example.reminderapp.AlarmScheduler
import com.example.reminderapp.AlarmSchedulerImpl
import com.example.reminderapp.data.internal.ReminderRepositoryImpl
import com.example.reminderapp.domain.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindReminderRepository(reminderRepositoryImpl: ReminderRepositoryImpl): ReminderRepository

    @Binds
    @Singleton
    abstract fun bindAlarmScheduler(alarmSchedulerImpl: AlarmSchedulerImpl): AlarmScheduler
}