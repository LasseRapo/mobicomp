package com.example.core.domain.entity

import java.time.LocalDateTime

data class Reminder(
    val reminderId: Long = 0,
    val message: String,
    val location_x: Double,
    val location_y: Double,
    val reminderTime: LocalDateTime,
    val creationTime: LocalDateTime = LocalDateTime.now(),
    val reminderSeen: Boolean = false,
    val creatorId: Long,
)