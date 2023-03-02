package com.example.mobicomp.ui.reminder

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.core.domain.repository.ReminderRepository
import com.example.core.domain.entity.Reminder
import com.example.mobicomp.Graph
import com.example.mobicomp.utils.ReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
): ViewModel() {
    private var _loaded: Boolean
    private val _listState = MutableStateFlow(ReminderListState())
    val listState: StateFlow<ReminderListState>
        get() = _listState
    private val _reminderViewState = MutableStateFlow<ReminderViewState>(ReminderViewState.Loading)
    val reminderState: StateFlow<ReminderViewState> = _reminderViewState

    init {
        loadReminders()
    }

    fun saveReminder(navController: NavController, reminder: Reminder) {
        viewModelScope.launch {
            val id = reminderRepository.addReminder(reminder)

            if (reminder.reminderTime.isBefore(LocalDateTime.now())) {
                reminderRepository.setReminderSeen(id, true)
            }
            setReminder(reminder)
            delay(80)
            navController.popBackStack()
        }
    }

    fun editReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.editReminder(reminder)
        }
    }

    fun deleteReminder(reminder: Reminder, tabName: String) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminder)
            reloadReminders(tabName)
        }
    }

    fun loadReminders() {
        viewModelScope.launch {
            try {
                _reminderViewState.value = ReminderViewState.Loading
                val reminders = reminderRepository.loadReminders()
                _reminderViewState.value = ReminderViewState.Success(reminders)
                _listState.value = ReminderListState(reminders = reminders, tabs = listOf("Occurred", "Scheduled", "All"))
                println("Loaded reminders: $reminders")
            } catch (e: Exception) {
                _reminderViewState.value = ReminderViewState.Error(e)
            }
        }
    }

    private suspend fun _reloadReminders(all: Boolean) {
        val list: List<Reminder> = if (all) {
            reminderRepository.loadReminders()
        } else {
            reminderRepository.loadSeenReminders(false)
        }
        val occurredList: List<Reminder> = list.filter { it.reminderTime.isBefore(LocalDateTime.now()) }
        val listButSorted: List<Reminder> = list.sortedByDescending { it.reminderTime }
        _reminderViewState.value = ReminderViewState.Success(listButSorted)
        _listState.value = ReminderListState(
            reminders = listButSorted,
            tabs = listOf("Occurred", "Scheduled", "All")
        )
        _listState.value = _listState.value.copy(reminders = occurredList, tabs = listOf("Occurred", "Scheduled", "All"))
    }

    fun reloadReminders(tabName: String) {
        if (tabName == "Occurred" || tabName == "Scheduled" || tabName == "All") {
            viewModelScope.launch {
                _reloadReminders(true)
            }
        }
    }

    private fun setReminder(reminder: Reminder) {
        val timeZoneId = ZoneId.systemDefault()
        val timeNow = Calendar.getInstance()
        val reminderDate = Date.from(reminder.reminderTime.atZone(timeZoneId).toInstant())
        val reminderTime = Calendar.getInstance()
        reminderTime.time = reminderDate
        val reminderDelay = reminderTime.timeInMillis/1000L - timeNow.timeInMillis/1000L
        val reminderWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(reminderDelay, TimeUnit.SECONDS)
            .setInputData(workDataOf(
                "reminderTime" to reminder.reminderTime.toString(),
                "message" to reminder.message
            )).build()

        WorkManager.getInstance(Graph.appContext).enqueue(reminderWorkRequest)
        Toast.makeText(Graph.appContext, "New reminder set", Toast.LENGTH_SHORT).show()
    }

    fun setReminderAsSeen(reminderId: Long) {
        viewModelScope.launch {
            reminderRepository.setReminderSeen(reminderId, true)
        }
    }


    init {
        _loaded = false
    }
}

data class ReminderListState(
    val reminders: List<Reminder> = emptyList(),
    val tabs: List<String> = emptyList()
)