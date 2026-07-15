package com.vacaciones.app.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private const val DIFF_COLS = 4

private val leftImage = listOf(
    listOf("🌞", "☁️", "🌈", "🐦"),
    listOf("🏖️", "🌊", "🌊", "🏖️"),
    listOf("🦀", "🐠", "🐚", "🦀"),
    listOf("🌴", "🏄", "🏄", "🌴"),
    listOf("🍦", "👒", "🌺", "🍉")
)

private val rightImage = listOf(
    listOf("🌞", "🌟", "🌈", "🦅"),   // difs en col 1 y col 3
    listOf("🏖️", "🐋", "🌊", "🏖️"),  // dif en col 1
    listOf("🦀", "🐠", "🦑", "🦀"),   // dif en col 2
    listOf("🌴", "🚤", "🏄", "🌴"),   // dif en col 1
    listOf("🍦", "🎩", "🌸", "🍉")    // difs en col 1 y col 2
)

private val diffPositions: Set<Int> = setOf(
    0 * DIFF_COLS + 1,  // (0,1): ☁️ → 🌟
    0 * DIFF_COLS + 3,  // (0,3): 🐦 → 🦅
    1 * DIFF_COLS + 1,  // (1,1): 🌊 → 🐋
    2 * DIFF_COLS + 2,  // (2,2): 🐚 → 🦑
    3 * DIFF_COLS + 1,  // (3,1): 🏄 → 🚤
    4 * DIFF_COLS + 1,  // (4,1): 👒 → 🎩
    4 * DIFF_COLS + 2   // (4,2): 🌺 → 🌸
)

@Composable
fun FindDifferencesGame(onGameComplete: (Int) -> Unit) {
    var found by remember { mutableStateOf(setOf<Int>()) }
    var wrongTap by remember { mutableStateOf<Int?>(null) }
    var errors by remember { mutableStateOf(0) }
    var gameFinished by remember { mutableStateOf(false) }

    LaunchedEffect(wrongTap) {
        if (wrongTap != null) {
            delay(600)
            wrongTap = null
        }
    }

    LaunchedEffect(found) {
        if (found.size == diffPositions.size) {
            gameFinished = true
            val score = maxOf(0, 100 - errors * 10)
            onGameComplete(score)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!gameFinished) {
                Text(
                    text = "🔎 Encuentra las 7 Diferencias",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "✅ ${found.size} / 7",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "❌ Errores: $errors",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (errors > 3) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Toca las diferencias en la imagen de la DERECHA",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    DiffImagePanel(
                        label = "Imagen 1",
                        scene = leftImage,
                        interactable = false
                    )
                    DiffImagePanel(
                        label = "Imagen 2 👆",
                        scene = rightImage,
                        interactable = true,
                        found = found,
                        wrongTap = wrongTap,
                        onCellTap = { pos ->
                            if (pos !in found) {
                                if (pos in diffPositions) {
                                    found = found + pos
                                } else {
                                    errors++
                                    wrongTap = pos
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(diffPositions.size) { i ->
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(
                                    color = if (i < found.size) Color(0xFF00AA44)
                                    else MaterialTheme.colorScheme.outline,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            } else {
                Text(
                    text = "🎉 ¡Las encontraste todas!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = when {
                        errors == 0 -> "🏆 ¡Sin ningún error! ¡Campeón!"
                        errors <= 3 -> "🌟 ¡Muy bien! Solo $errors errores"
                        else -> "👍 ¡Lo conseguiste! $errors errores"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "🔎", fontSize = 80.sp)
            }
        }
    }
}

@Composable
fun DiffImagePanel(
    label: String,
    scene: List<List<String>>,
    interactable: Boolean,
    found: Set<Int> = emptySet(),
    wrongTap: Int? = null,
    onCellTap: (Int) -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            scene.forEachIndexed { row, rowItems ->
                Row {
                    rowItems.forEachIndexed { col, emoji ->
                        val pos = row * DIFF_COLS + col
                        val isFound = pos in found
                        val isWrong = wrongTap == pos
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .then(
                                    if (interactable) {
                                        Modifier.clickable(enabled = !isFound) { onCellTap(pos) }
                                    } else Modifier
                                )
                                .background(
                                    color = when {
                                        isFound -> Color(0x8800CC44)
                                        isWrong -> Color(0x88FF4444)
                                        else -> Color.Transparent
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
