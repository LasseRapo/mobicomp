package com.example.core.data.repository

import com.example.core.data.datasource.reminder.ReminderDataSource
import com.example.core.domain.entity.Reminder
import com.example.core.domain.repository.ReminderRepository

class ReminderRepositoryImpl (
    private val reminderDataSource: ReminderDataSource
) : ReminderRepository {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDataSource.addReminder(reminder)
    }
}