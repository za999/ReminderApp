package com.example.reminderapp.di

import android.content.Context
import com.example.reminderapp.AlarmScheduler
import com.example.reminderapp.AlarmSchedulerImpl
import com.example.reminderapp.domain.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IODispatcher


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @IODispatcher
    @Provides
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO


}