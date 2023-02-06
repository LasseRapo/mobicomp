package com.example.mobicomp.ui.home.reminderCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobicomp.data.entity.Reminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class ReminderCardListViewModel : ViewModel() {
    private val _state = MutableStateFlow(ReminderCardViewState())

    val state: StateFlow<ReminderCardViewState>
        get() = _state

    init {
        val list = mutableListOf<Reminder>()
        for (x in 1..20) {
            list.add(
                Reminder(
                    reminderId = x.toLong(),
                    reminderCategory = "School",
                    reminderTitle = "$x reminder",
                    reminderDate = Date()
                )
            )
        }
        viewModelScope.launch {
            _state.value = ReminderCardViewState(
                reminders = list
            )
        }
    }
}

data class ReminderCardViewState(
    val reminders: List<Reminder> = emptyList()
)