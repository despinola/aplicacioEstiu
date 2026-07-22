package com.vacaciones.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vacaciones.app.ui.AppViewModel
import com.vacaciones.app.games.GameFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailScreen(
    day: Int,
    viewModel: AppViewModel,
    onBackClick: () -> Unit
) {
    val currentActivity by viewModel.currentActivity.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(day) {
        viewModel.loadActivityForDay(day)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dia $day d'Agost",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Tornar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // User info
            currentUser?.let { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserAvatar(
                            name = user.name,
                            fallbackEmoji = user.avatar,
                            size = 48.dp,
                            fallbackFontSize = 36.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Travel day banners
            when (day) {
                1 -> { Spacer(modifier = Modifier.height(16.dp)); TravelDayBanner() }
                2 -> { Spacer(modifier = Modifier.height(16.dp)); GrenobleDay2Banner() }
                3 -> { Spacer(modifier = Modifier.height(16.dp)); MontBlancLagoDay3Banner() }
            }

            // Birthday banner
            val birthdayConfig = when {
                day == 9 && currentUser?.name in listOf("David", "Miriam") -> ANIVERSARI_PARELLA
                day == 15 && currentUser?.name == "Miriam" -> SANT_MIRIAM
                day == 16 && currentUser?.name == "Rita" -> BIRTHDAY_RITA
                day == 18 && currentUser?.name == "Blai" -> BIRTHDAY_BLAI
                else -> null
            }
            birthdayConfig?.let {
                Spacer(modifier = Modifier.height(16.dp))
                BirthdayBanner(config = it)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Game section
            Text(
                text = "🎮 Joc del dia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            GameFactory.getGameForDay(
                day = day,
                onGameComplete = { score ->
                    viewModel.updateActivity(gameCompleted = true, gameScore = score)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Activity checklist
            Text(
                text = "📝 Què has fet avui?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            currentActivity?.let { activity ->
                ChecklistItem(
                    text = "📖 He llegit",
                    checked = activity.hasRead,
                    onCheckedChange = { viewModel.updateActivity(hasRead = it) }
                )

                ChecklistItem(
                    text = "🪥 M'he rentat les dents",
                    checked = activity.hasBrushedTeeth,
                    onCheckedChange = { viewModel.updateActivity(hasBrushedTeeth = it) }
                )

                ChecklistItem(
                    text = "📺 He vist la tele",
                    checked = activity.hasWatchedTV,
                    onCheckedChange = { viewModel.updateActivity(hasWatchedTV = it) }
                )

                ChecklistItem(
                    text = "🏊 He anat a la piscina",
                    checked = activity.hasSwimmed,
                    onCheckedChange = { viewModel.updateActivity(hasSwimmed = it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Notes
                var notes by remember(activity.notes) { mutableStateOf(activity.notes) }

                Text(
                    text = "✍️ Notes del dia",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Què ha passat avui?") },
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.updateActivity(notes = notes) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Desar notes")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Photo section
            currentActivity?.let { activity ->
                PhotoSection(
                    photoUri = activity.photoUri,
                    userName = currentUser?.name ?: "user",
                    day = day,
                    onPhotoSaved = { uri -> viewModel.updateActivity(photoUri = uri) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ChecklistItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
