package com.vacaciones.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vacaciones.app.data.AppRepository
import com.vacaciones.app.data.DailyActivity
import com.vacaciones.app.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _currentActivity = MutableStateFlow<DailyActivity?>(null)
    val currentActivity: StateFlow<DailyActivity?> = _currentActivity.asStateFlow()

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
        gameScore: Int? = null
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
                    gameScore = gameScore ?: activity.gameScore
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
