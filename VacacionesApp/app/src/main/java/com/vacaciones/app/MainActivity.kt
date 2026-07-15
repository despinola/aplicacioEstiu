package com.vacaciones.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vacaciones.app.data.AppDatabase
import com.vacaciones.app.data.AppRepository
import com.vacaciones.app.ui.AppViewModel
import com.vacaciones.app.ui.AppViewModelFactory
import com.vacaciones.app.ui.screens.CalendarScreen
import com.vacaciones.app.ui.screens.DayDetailScreen
import com.vacaciones.app.ui.screens.LoginScreen
import com.vacaciones.app.ui.theme.VacacionesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = AppRepository(database.userDao(), database.dailyActivityDao())

        setContent {
            VacacionesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AppViewModel = viewModel(
                        factory = AppViewModelFactory(repository)
                    )
                    VacacionesApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun VacacionesApp(viewModel: AppViewModel) {
    val navController = rememberNavController()
    val currentUser by viewModel.currentUser.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) "login" else "calendar"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onUserSelected = {
                    navController.navigate("calendar") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("calendar") {
            CalendarScreen(
                viewModel = viewModel,
                onDayClick = { day ->
                    navController.navigate("day/$day")
                },
                onLogout = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("calendar") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "day/{day}",
            arguments = listOf(navArgument("day") { type = NavType.IntType })
        ) { backStackEntry ->
            val day = backStackEntry.arguments?.getInt("day") ?: 1
            DayDetailScreen(
                day = day,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
