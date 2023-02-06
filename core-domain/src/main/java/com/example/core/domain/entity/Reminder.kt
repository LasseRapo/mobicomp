package com.example.core.domain.entity

import java.time.LocalDateTime

data class Reminder(
    val reminderId: Long = 0,
    val title: String,
    val categoryId: Long,
    val date: LocalDateTime
)
