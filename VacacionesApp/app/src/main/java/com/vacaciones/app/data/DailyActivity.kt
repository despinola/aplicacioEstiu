package com.vacaciones.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_activities")
data class DailyActivity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val hasRead: Boolean = false,
    val hasBrushedTeeth: Boolean = false,
    val hasWatchedTV: Boolean = false,
    val hasSwimmed: Boolean = false,
    val notes: String = "",
    val gameCompleted: Boolean = false,
    val gameScore: Int = 0
)
