package com.vacaciones.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyActivityDao {
    @Query("SELECT * FROM daily_activities WHERE userId = :userId AND day = :day AND month = :month AND year = :year")
    suspend fun getActivityForDay(userId: Int, day: Int, month: Int, year: Int): DailyActivity?

    @Query("SELECT * FROM daily_activities WHERE userId = :userId AND month = :month AND year = :year")
    fun getActivitiesForMonth(userId: Int, month: Int, year: Int): Flow<List<DailyActivity>>

    @Query("SELECT * FROM daily_activities WHERE month = :month AND year = :year")
    fun getAllActivitiesForMonth(month: Int, year: Int): Flow<List<DailyActivity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: DailyActivity)

    @Update
    suspend fun update(activity: DailyActivity)

    @Query("DELETE FROM daily_activities")
    suspend fun deleteAll()
}
