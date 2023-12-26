package com.example.reminderapp.presentation
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.AlarmScheduler
import com.example.reminderapp.data.internal.mapper.toReminderEntity
import com.example.reminderapp.data.internal.model.ReminderEntity
import com.example.reminderapp.domain.ReminderRepository
import com.example.reminderapp.domain.model.Reminder
import com.example.reminderapp.util.ResultOf
import com.example.reminderapp.util.beginningOfDay
import com.example.reminderapp.util.fromLocalDateToMillis
import com.example.reminderapp.util.fromLocalTimeToMillis
import com.example.reminderapp.util.untilEndOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {

    private val _categoryType = MutableStateFlow(ReminderCategory.VIEWNONE)

    private val _state = MutableStateFlow(ReminderState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ReminderState()
    )

    init {
        updateReminders()
    }


    fun onIntent(intent: ReminderIntent) {
        when(intent) {

            is ReminderIntent.ShowReminders -> {
                _categoryType.value = intent.currentCategory
            }

            is ReminderIntent.SetTitle -> {
                _state.update { it.copy(title = intent.title) }
            }

            is ReminderIntent.SetDescription -> {
                _state.update { it.copy(description = intent.description) }
            }

            is ReminderIntent.SetReminderAsDone -> {
                viewModelScope.launch {
                    if (intent.isCheckedAsDone) {
                        val doneReminder = intent.reminder.copy(isDone = true)
                        mapToEntityAndUpdate(doneReminder)
                    }
                }
            }

            is ReminderIntent.SetDate -> {
                _state.update {
                    it.copy(date = intent.date)
                }
            }

            // TODO: use for timepicker
            is ReminderIntent.SetTime -> {
                _state.update {
                    it.copy(time = intent.time)
                }
            }

            is ReminderIntent.AddingDateAndTime -> {
                _state.update {
                    it.copy(isSettingDateAndTime = intent.addingDateAndTime)
                }
            }

            ReminderIntent.AddingNewReminder -> {
                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        date = null,
                        time = null,
                        isAddingNewReminder = true,
                        isEditingReminder = false,
                    )
                }
            }

            is ReminderIntent.EditingReminder -> {
                _state.update {
                    it.copy(
                        isEditingReminder = true,
                        isAddingNewReminder = false,
                    )
                }
                updateReminderValues(intent.reminderToBeEdited)
            }


            ReminderIntent.SaveReminder -> {
                val id = state.value.id
                val title = state.value.title
                val desc = state.value.description
                val date = state.value.date
                val time = state.value.time

                if (title.isBlank()) {
                    // TODO Update state to error. Show toast (specific error)
                    return
                }

                val reminderEntity = if (state.value.isEditingReminder) {
                    ReminderEntity(
                        id = id,
                        title = title,
                        description = desc,
                        date = date?.fromLocalDateToMillis(),
                        time = time?.fromLocalTimeToMillis()
                    )
                } else {
                    ReminderEntity(
                        title = title,
                        description = desc,
                        date = date?.fromLocalDateToMillis(),
                        time = time?.fromLocalTimeToMillis()
                    )
                }



                val deferred = viewModelScope.async {  repository.insertOrUpdateReminder(reminderEntity) }
                viewModelScope.launch {
                    try {
                        deferred.await()
                        alarmScheduler.shouldSchedule()
                    }catch (e: Exception) {
                        Log.e("TAG", "Failed to save entity in database! $e")
                    }
                }

                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        date = null,
                        time = null,
                    )
                }
            }

            else -> {}
        }
    }


    private fun updateReminderValues(reminder: Reminder) {
        _state.update {
            it.copy(
                id = reminder.id,
                title = reminder.title,
                description = reminder.description,
                date = reminder.date,
                time = reminder.time
            )
        }
    }


    private suspend fun mapToEntityAndUpdate(reminder: Reminder) {
        val reminderEntity = reminder.toReminderEntity()
        repository.insertOrUpdateReminder(reminderEntity = reminderEntity)
    }


    private fun updateReminders() {
        viewModelScope.launch {
            combine(
                _categoryType,
                repository.getAllReminders(),
                repository.getScheduledReminders(
                    LocalDate.now().untilEndOfDay()
                ),
                repository.getDailyReminders(
                    today = LocalDate.now().beginningOfDay(),
                    tomorrow = LocalDate.now().untilEndOfDay()
                )) {category, all, scheduled, daily  ->
                val allReminders = when(all) {
                    is ResultOf.Success -> all.value.reminders
                    is ResultOf.Error -> emptyList()
                }
                val scheduledReminders = when(scheduled) {
                    is ResultOf.Success -> scheduled.value.reminders
                    is ResultOf.Error -> emptyList()
                }
                val dailyReminders =  when(daily) {
                    is ResultOf.Success -> daily.value.reminders
                    is ResultOf.Error -> emptyList()
                }
                val currentListToDisplay = getReminderListOfCategory(
                    category,
                    allReminders,
                    scheduledReminders,
                    dailyReminders
                )
                val headerTitle = getHeaderTitleOfCategory(category)
                _state.update {
                    it.copy(
                        allReminders = allReminders,
                        dailyReminders =  dailyReminders,
                        scheduledReminders = scheduledReminders,
                        currentListToDisplay = currentListToDisplay,
                        headerTitle = headerTitle
                    )
                }
            }.collect()
        }
    }

    private fun getReminderListOfCategory(
        category: ReminderCategory,
        allReminders: List<Reminder>,
        scheduledReminders: List<Reminder>,
        dailyReminders: List<Reminder>
    ): List<Reminder> {
        return when(category) {
            ReminderCategory.VIEWALL -> allReminders
            ReminderCategory.VIEWDAILY -> dailyReminders
            ReminderCategory.VIEWSCHEDULED -> scheduledReminders
            else -> emptyList()
        }
    }
    private fun getHeaderTitleOfCategory(
        category: ReminderCategory
    ): String {
        return when(category) {
            ReminderCategory.VIEWALL -> "All"
            ReminderCategory.VIEWDAILY -> "Daily"
            ReminderCategory.VIEWSCHEDULED -> "Scheduled"
            else -> ""
        }
    }
}


