package com.example.core.domain.repository

import com.example.core.domain.entity.Reminder

interface ReminderRepository {
    suspend fun addReminder(reminder: Reminder)
}