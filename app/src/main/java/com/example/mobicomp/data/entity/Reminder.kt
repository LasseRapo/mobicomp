package com.example.mobicomp.data.entity

import java.util.*

data class Reminder(
    val reminderId: Long,
    val reminderTitle: String,
    val reminderCategory: String,
    val reminderDate: Date?
)