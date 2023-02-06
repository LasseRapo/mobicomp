package com.example.core.data.datasource.reminder

import com.example.core.domain.entity.Reminder

interface ReminderDataSource {
    suspend fun addReminder(reminder: Reminder)
}