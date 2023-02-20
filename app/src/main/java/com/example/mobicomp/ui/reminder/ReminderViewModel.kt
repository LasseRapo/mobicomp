package com.example.mobicomp.ui.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.repository.ReminderRepository
import com.example.core.domain.entity.Reminder
import com.example.mobicomp.Graph
import com.example.mobicomp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
): ViewModel() {

    private val _reminderViewState = MutableStateFlow<ReminderViewState>(ReminderViewState.Loading)
    val reminderState: StateFlow<ReminderViewState> = _reminderViewState

    init {
        createNotificationChannel()
        loadReminders()
    }

    fun saveReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.addReminder(reminder)
            notifyUserOfReminder(reminder)
        }
    }

    fun editReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.editReminder(reminder)
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminder)
            loadReminders()
        }
    }

    fun loadReminders() {
        viewModelScope.launch {
            try {
                _reminderViewState.value = ReminderViewState.Loading
                val reminders = reminderRepository.loadReminders()
                _reminderViewState.value = ReminderViewState.Success(reminders)
                println("Loaded reminders: $reminders")
            } catch (e: Exception) {
                _reminderViewState.value = ReminderViewState.Error(e)
            }
        }
    }

    private fun notifyUserOfReminder(reminder : Reminder) {
        val notificationId = 10
        val builder = NotificationCompat.Builder(
            Graph.appContext,
            "channel_id"
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New reminder made")
            .setContentText("Reminder set to ${reminder.reminderTime}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(from(Graph.appContext)) {
            notify(
                notificationId, builder.build()
            )
        }
    }

    private fun createNotificationChannel() {
        val name = "NotificationChannel"
        val descriptionText = "NotificationChannelDescription"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager = Graph.appContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}