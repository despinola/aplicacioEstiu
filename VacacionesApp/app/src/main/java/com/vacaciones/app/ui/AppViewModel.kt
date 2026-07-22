package com.vacaciones.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vacaciones.app.data.AppRepository
import com.vacaciones.app.data.DailyActivity
import com.vacaciones.app.data.User
import com.vacaciones.app.data.calculatePoints
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _currentActivity = MutableStateFlow<DailyActivity?>(null)
    val currentActivity: StateFlow<DailyActivity?> = _currentActivity.asStateFlow()

    // Punts totals de l'usuari actual (suma de tots els dies del mes)
    val currentUserTotalPoints: StateFlow<Int> = repository
        .getAllActivitiesForMonth(8, 2026)
        .map { activities ->
            val userId = _currentUser.value?.id ?: return@map 0
            activities.filter { it.userId == userId }.sumOf { it.calculatePoints() }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // Rànquing de tots els usuaris amb els seus punts totals
    val allUsersPoints: StateFlow<List<Pair<User, Int>>> = repository
        .getAllActivitiesForMonth(8, 2026)
        .map { activities ->
            val actsByUser = activities.groupBy { it.userId }
            _users.value.map { user ->
                val pts = actsByUser[user.id]?.sumOf { it.calculatePoints() } ?: 0
                user to pts
            }.sortedByDescending { it.second }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            repository.allUsers.collect { userList ->
                _users.value = userList
            }
        }
    }

    fun selectUser(user: User) {
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }

    fun loadActivityForDay(day: Int, month: Int = 8, year: Int = 2026) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                val activity = repository.getActivityForDay(user.id, day, month, year)
                _currentActivity.value = activity ?: DailyActivity(
                    userId = user.id,
                    day = day,
                    month = month,
                    year = year
                )
            }
        }
    }

    fun updateActivity(
        hasRead: Boolean? = null,
        hasBrushedTeeth: Boolean? = null,
        hasWatchedTV: Boolean? = null,
        hasSwimmed: Boolean? = null,
        notes: String? = null,
        gameCompleted: Boolean? = null,
        gameScore: Int? = null,
        photoUri: String? = null,
        tvTime: Int? = null,
        tabletTime: Int? = null,
        hasSetTable: Boolean? = null,
        hasWashedDishes: Boolean? = null,
        hasPreparedBreakfast: Boolean? = null,
        hasSunset: Boolean? = null,
        hasShootingStars: Boolean? = null,
        hasEclipse: Boolean? = null
    ) {
        viewModelScope.launch {
            _currentActivity.value?.let { activity ->
                val updated = activity.copy(
                    hasRead = hasRead ?: activity.hasRead,
                    hasBrushedTeeth = hasBrushedTeeth ?: activity.hasBrushedTeeth,
                    hasWatchedTV = hasWatchedTV ?: activity.hasWatchedTV,
                    hasSwimmed = hasSwimmed ?: activity.hasSwimmed,
                    notes = notes ?: activity.notes,
                    gameCompleted = gameCompleted ?: activity.gameCompleted,
                    gameScore = gameScore ?: activity.gameScore,
                    photoUri = photoUri ?: activity.photoUri,
                    tvTime = tvTime ?: activity.tvTime,
                    tabletTime = tabletTime ?: activity.tabletTime,
                    hasSetTable = hasSetTable ?: activity.hasSetTable,
                    hasWashedDishes = hasWashedDishes ?: activity.hasWashedDishes,
                    hasPreparedBreakfast = hasPreparedBreakfast ?: activity.hasPreparedBreakfast,
                    hasSunset = hasSunset ?: activity.hasSunset,
                    hasShootingStars = hasShootingStars ?: activity.hasShootingStars,
                    hasEclipse = hasEclipse ?: activity.hasEclipse
                )
                repository.insertOrUpdateActivity(updated)
                _currentActivity.value = updated
            }
        }
    }
}

class AppViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
