package com.example.core.domain.entity

import java.time.LocalDateTime

data class Reminder(
    val reminderId: Long?,
    val message: String,
    val location_x: Double,
    val location_y: Double,
    val reminderTime: String,
    val creationTime: LocalDateTime,
    val reminderSeen: Long,
    val creatorId: Long,
)