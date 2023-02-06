package com.example.core.data.datasource.reminder

import com.example.core.database.dao.ReminderDao
import com.example.core.database.entity.ReminderEntity
import com.example.core.domain.entity.Reminder

class ReminderDataSourceImpl(
    private val reminderDao: ReminderDao
) : ReminderDataSource {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDao.insertOrUpdate(reminder.toEntity())
    }

    private fun Reminder.toEntity() = ReminderEntity(
        reminderId = this.reminderId,
        categoryId = this.categoryId,
        title = this.title,
        date = this.date
    )
    private fun ReminderEntity.fromEntity() = Reminder(
        reminderId = this.reminderId,
        categoryId = this.categoryId,
        title = this.title,
        date = this.date
    )
}