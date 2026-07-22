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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vacaciones.app.data.calculatePoints
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
            // User info + punts del dia
            currentUser?.let { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserAvatar(
                            name = user.name,
                            fallbackEmoji = user.avatar,
                            size = 48.dp,
                            fallbackFontSize = 36.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            currentActivity?.let { act ->
                                val pts = act.calculatePoints()
                                Text(
                                    text = "Punts avui: $pts ⭐",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (pts >= 0) Color(0xFF2E7D32) else Color(0xFFB71C1C),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
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

            // Tasques del dia
            Text(
                text = "✅ Tasques del dia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            currentActivity?.let { activity ->

                // --- Tasques simples (booleà) ---
                TaskCheckItem(
                    emoji = "📖", label = "He llegit", points = 100,
                    checked = activity.hasRead,
                    onCheckedChange = { viewModel.updateActivity(hasRead = it) }
                )
                TaskCheckItem(
                    emoji = "🪥", label = "M'he rentat les dents", points = 15,
                    checked = activity.hasBrushedTeeth,
                    onCheckedChange = { viewModel.updateActivity(hasBrushedTeeth = it) }
                )
                TaskCheckItem(
                    emoji = "🏊", label = "He anat a la piscina", points = 30,
                    checked = activity.hasSwimmed,
                    onCheckedChange = { viewModel.updateActivity(hasSwimmed = it) }
                )
                TaskCheckItem(
                    emoji = "🍽️", label = "He parat la taula", points = 20,
                    checked = activity.hasSetTable,
                    onCheckedChange = { viewModel.updateActivity(hasSetTable = it) }
                )
                TaskCheckItem(
                    emoji = "🧼", label = "He rentat els plats", points = 35,
                    checked = activity.hasWashedDishes,
                    onCheckedChange = { viewModel.updateActivity(hasWashedDishes = it) }
                )
                TaskCheckItem(
                    emoji = "🥐", label = "He preparat l'esmorzar", points = 35,
                    checked = activity.hasPreparedBreakfast,
                    onCheckedChange = { viewModel.updateActivity(hasPreparedBreakfast = it) }
                )
                TaskCheckItem(
                    emoji = "🌅", label = "He vist la posta de sol", points = 50,
                    checked = activity.hasSunset,
                    onCheckedChange = { viewModel.updateActivity(hasSunset = it) }
                )
                TaskCheckItem(
                    emoji = "🌠", label = "He vist estels fugaços", points = 200,
                    checked = activity.hasShootingStars,
                    onCheckedChange = { viewModel.updateActivity(hasShootingStars = it) }
                )

                // Eclipse únicament el dia 12
                if (day == 12) {
                    TaskCheckItem(
                        emoji = "🌑", label = "He vist l'eclipse", points = 400,
                        checked = activity.hasEclipse,
                        onCheckedChange = { viewModel.updateActivity(hasEclipse = it) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- TV (selector multi-opció) ---
                TaskMultiOption(
                    emoji = "📺",
                    label = "Temps de televisió",
                    options = listOf(
                        Triple("Cap", 0, null),
                        Triple("30 min", 1, +5),
                        Triple("+1 hora", 2, -5),
                        Triple("+2 hores", 3, -15)
                    ),
                    selected = activity.tvTime,
                    onSelect = { viewModel.updateActivity(tvTime = it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- Tablet/telèfon (selector multi-opció) ---
                TaskMultiOption(
                    emoji = "📱",
                    label = "Tablet / telèfon",
                    options = listOf(
                        Triple("Cap", 0, null),
                        Triple("20 min", 1, 0),
                        Triple("+1 hora", 2, -30)
                    ),
                    selected = activity.tabletTime,
                    onSelect = { viewModel.updateActivity(tabletTime = it) }
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
fun TaskCheckItem(
    emoji: String,
    label: String,
    points: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val pointsColor = if (points > 0) Color(0xFF2E7D32) else if (points < 0) Color(0xFFB71C1C) else Color(0xFF757575)
    val pointsText = if (points > 0) "+$points ⭐" else if (points < 0) "$points ⭐" else "0 ⭐"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$emoji $label",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = pointsText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = pointsColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskMultiOption(
    emoji: String,
    label: String,
    options: List<Triple<String, Int, Int?>>,   // (label, value, points or null)
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$emoji $label",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            options.forEach { (optLabel, value, points) ->
                val isSelected = selected == value
                val chipColor = when {
                    !isSelected -> MaterialTheme.colorScheme.surfaceVariant
                    points == null || points == 0 -> MaterialTheme.colorScheme.secondaryContainer
                    points > 0 -> Color(0xFFE8F5E9)
                    else -> Color(0xFFFFEBEE)
                }
                val textColor = when {
                    !isSelected -> MaterialTheme.colorScheme.onSurfaceVariant
                    points != null && points > 0 -> Color(0xFF2E7D32)
                    points != null && points < 0 -> Color(0xFFB71C1C)
                    else -> MaterialTheme.colorScheme.onSecondaryContainer
                }
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(value) },
                    label = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(optLabel, style = MaterialTheme.typography.bodySmall)
                            if (points != null) {
                                val pts = if (points > 0) "+$points⭐" else if (points < 0) "$points⭐" else "0⭐"
                                Text(
                                    text = pts,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) textColor else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = chipColor,
                        selectedContainerColor = chipColor
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
