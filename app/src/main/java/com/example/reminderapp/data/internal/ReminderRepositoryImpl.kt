package com.example.reminderapp.data.internal

import com.example.reminderapp.data.internal.mapper.toReminder
import com.example.reminderapp.data.internal.mapper.toReminderInfo
import com.example.reminderapp.data.internal.model.ReminderEntity
import com.example.reminderapp.di.IODispatcher
import com.example.reminderapp.domain.ReminderRepository
import com.example.reminderapp.domain.model.Reminder
import com.example.reminderapp.domain.model.ReminderInfo
import com.example.reminderapp.util.ResultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val dao: ReminderDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : ReminderRepository {

    override suspend fun insertOrUpdateReminder(reminderEntity: ReminderEntity) {
        dao.upsertReminder(reminderEntity)
    }

    override fun getAllReminders(): Flow<ResultOf<ReminderInfo>> {
        return dao.getAllRemindersFlow()
            .flowOn(ioDispatcher)
            .map { ResultOf.Success(it.toReminderInfo()) }
            .catch { cause: Throwable -> ResultOf.Error(cause) }
    }

    override fun getScheduledReminders(today: Long): Flow<ResultOf<ReminderInfo>> {
        return dao.getScheduledReminders(today)
            .flowOn(ioDispatcher)
            .map { ResultOf.Success(it.toReminderInfo()) }
            .catch { cause: Throwable -> ResultOf.Error(cause) }
    }


    override fun getDailyReminders(
        today: Long,
        tomorrow: Long
    ): Flow<ResultOf<ReminderInfo>> {
        return dao.getAllTodaysRemindersFlow(today, tomorrow)
            .flowOn(ioDispatcher)
            .map { ResultOf.Success(it.toReminderInfo()) }
            .catch { cause: Throwable -> ResultOf.Error(cause) }
    }


    override suspend fun getReminderById(id: Long): ResultOf<Reminder> {
        return try {
            withContext(Dispatchers.IO) {
                ResultOf.Success(
                    value = dao.getReminderEntityById(id)
                        .toReminder()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResultOf.Error(e)
        }
    }

    override suspend fun getLatestReminderEntity(): ResultOf<Reminder> {
        return try {
            withContext(Dispatchers.IO) {
                ResultOf.Success(
                    value = dao.getLatestReminderEntity()
                        .toReminder()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResultOf.Error(e)
        }
    }


}

