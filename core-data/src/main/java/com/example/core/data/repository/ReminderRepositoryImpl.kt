package com.example.core.data.repository

import com.example.core.data.datasource.reminder.ReminderDataSource
import com.example.core.domain.entity.Reminder
import com.example.core.domain.repository.ReminderRepository
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDataSource: ReminderDataSource
) : ReminderRepository {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDataSource.addReminder(reminder)
    }
    override suspend fun editReminder(reminder: Reminder) {
        reminderDataSource.editReminder(reminder)
    }
    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDataSource.deleteReminder(reminder)
    }
    override suspend fun loadReminders(): List<Reminder> {
        return reminderDataSource.loadReminders()
    }
}