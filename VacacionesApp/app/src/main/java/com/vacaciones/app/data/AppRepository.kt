package com.vacaciones.app.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val userDao: UserDao,
    private val dailyActivityDao: DailyActivityDao
) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)

    suspend fun getActivityForDay(userId: Int, day: Int, month: Int, year: Int): DailyActivity? {
        return dailyActivityDao.getActivityForDay(userId, day, month, year)
    }

    fun getActivitiesForMonth(userId: Int, month: Int, year: Int): Flow<List<DailyActivity>> {
        return dailyActivityDao.getActivitiesForMonth(userId, month, year)
    }

    fun getAllActivitiesForMonth(month: Int, year: Int): Flow<List<DailyActivity>> {
        return dailyActivityDao.getAllActivitiesForMonth(month, year)
    }

    suspend fun insertOrUpdateActivity(activity: DailyActivity) {
        val existing = dailyActivityDao.getActivityForDay(
            activity.userId,
            activity.day,
            activity.month,
            activity.year
        )
        if (existing != null) {
            dailyActivityDao.update(activity.copy(id = existing.id))
        } else {
            dailyActivityDao.insert(activity)
        }
    }
}
