package com.example.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.database.dao.ReminderDao
import com.example.core.database.entity.ReminderEntity

@Database(
    entities = [ReminderEntity::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}