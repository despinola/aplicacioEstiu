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
    val hasWatchedTV: Boolean = false,  // kept for DB compat (v1-v2), unused in UI
    val hasSwimmed: Boolean = false,
    val notes: String = "",
    val gameCompleted: Boolean = false,
    val gameScore: Int = 0,
    val photoUri: String = "",
    // v3: sistema de punts complet
    val tvTime: Int = 0,              // 0=cap, 1=30min(+5), 2=1h+(-5), 3=2h+(-15)
    val tabletTime: Int = 0,          // 0=cap, 1=20min(0), 2=1h+(-30)
    val hasSetTable: Boolean = false,
    val hasWashedDishes: Boolean = false,
    val hasPreparedBreakfast: Boolean = false,
    val hasSunset: Boolean = false,
    val hasShootingStars: Boolean = false,
    val hasEclipse: Boolean = false
)

fun DailyActivity.calculatePoints(): Int {
    var pts = 0
    if (hasRead) pts += 100
    if (hasBrushedTeeth) pts += 15
    pts += when (tvTime) {
        1 -> 5
        2 -> -5
        3 -> -15
        else -> 0
    }
    if (hasSwimmed) pts += 30
    if (hasSetTable) pts += 20
    if (hasWashedDishes) pts += 35
    if (hasPreparedBreakfast) pts += 35
    if (tabletTime == 2) pts -= 30
    if (hasSunset) pts += 50
    if (hasShootingStars) pts += 200
    if (hasEclipse) pts += 400
    pts += gameScore
    return pts
}
