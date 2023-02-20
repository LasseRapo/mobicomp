package com.example.core.data.datasource.reminder

import com.example.core.database.dao.ReminderDao
import com.example.core.database.entity.ReminderEntity
import com.example.core.domain.entity.Reminder
import javax.inject.Inject

class ReminderDataSourceImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderDataSource {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDao.insertOrUpdate(reminder.toEntity())
    }

    override suspend fun editReminder(reminder: Reminder) {
        reminderDao.insertOrUpdate(reminder.toEntity())
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.delete(reminder.toEntity())
    }

    override suspend fun loadReminders(): List<Reminder> {
        return reminderDao.findAll().map {
            it.fromEntity()
        }
    }

    private fun Reminder.toEntity() = ReminderEntity(
        reminderId = this.reminderId,
        message = this.message,
        location_x = this.location_x,
        location_y = this.location_y,
        reminderTime = this.reminderTime,
        creationTime = this.creationTime,
        creatorId = this.creatorId,
        reminderSeen = this.reminderSeen,
    )

    private fun ReminderEntity.fromEntity() = Reminder(
        reminderId = this.reminderId,
        message = this.message,
        location_x = this.location_x,
        location_y = this.location_y,
        reminderTime = this.reminderTime,
        creationTime = this.creationTime,
        creatorId = this.creatorId,
        reminderSeen = this.reminderSeen,
    )
}