package com.vacaciones.app.games

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

data class Flag(val emoji: String, val country: String, val region: String)

@Composable
fun FlagsGame(
    region: String = "Europa",
    onGameComplete: (Int) -> Unit
) {
    val flags = remember { getFlagsForRegion(region) }
    var currentQuestion by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var gameFinished by remember { mutableStateOf(false) }

    val totalQuestions = 5
    val currentFlag = remember(currentQuestion) {
        flags.random()
    }
    val options = remember(currentQuestion) {
        val wrongFlags = flags.filter { it.country != currentFlag.country }.shuffled().take(3)
        (wrongFlags + currentFlag).shuffled()
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!gameFinished) {
                Text(
                    text = "Pregunta ${currentQuestion + 1} de $totalQuestions",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentFlag.emoji,
                    fontSize = 80.sp,
                    modifier = Modifier.padding(24.dp)
                )

                Text(
                    text = "¿De qué país es esta bandera?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                options.forEach { flag ->
                    val isSelected = selectedAnswer == flag.country
                    val isCorrect = flag.country == currentFlag.country

                    Button(
                        onClick = {
                            if (!showResult) {
                                selectedAnswer = flag.country
                                showResult = true
                                if (isCorrect) score++
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                showResult && isCorrect -> MaterialTheme.colorScheme.tertiary
                                showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.primary
                            }
                        ),
                        enabled = !showResult
                    ) {
                        Text(flag.country)
                    }
                }

                if (showResult) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (currentQuestion + 1 < totalQuestions) {
                                currentQuestion++
                                selectedAnswer = null
                                showResult = false
                            } else {
                                gameFinished = true
                                onGameComplete(score * 20)
                            }
                        }
                    ) {
                        Text(if (currentQuestion + 1 < totalQuestions) "Siguiente" else "Terminar")
                    }
                }
            } else {
                Text(
                    text = "¡Juego completado!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Puntuación: $score de $totalQuestions",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = if (score == totalQuestions) "🎉 ¡Perfecto!"
                           else if (score >= 3) "👍 ¡Bien hecho!"
                           else "💪 ¡Sigue practicando!",
                    fontSize = 32.sp
                )
            }
        }
    }
}

fun getFlagsForRegion(region: String): List<Flag> {
    val allFlags = listOf(
        // Europa
        Flag("🇪🇸", "España", "Europa"),
        Flag("🇫🇷", "Francia", "Europa"),
        Flag("🇮🇹", "Italia", "Europa"),
        Flag("🇩🇪", "Alemania", "Europa"),
        Flag("🇬🇧", "Reino Unido", "Europa"),
        Flag("🇵🇹", "Portugal", "Europa"),
        Flag("🇳🇱", "Países Bajos", "Europa"),
        Flag("🇧🇪", "Bélgica", "Europa"),
        Flag("🇨🇭", "Suiza", "Europa"),
        Flag("🇬🇷", "Grecia", "Europa"),
        // América
        Flag("🇺🇸", "Estados Unidos", "America"),
        Flag("🇨🇦", "Canadá", "America"),
        Flag("🇲🇽", "México", "America"),
        Flag("🇧🇷", "Brasil", "America"),
        Flag("🇦🇷", "Argentina", "America"),
        Flag("🇨🇱", "Chile", "America"),
        Flag("🇨🇴", "Colombia", "America"),
        Flag("🇵🇪", "Perú", "America"),
        // África
        Flag("🇿🇦", "Sudáfrica", "Africa"),
        Flag("🇪🇬", "Egipto", "Africa"),
        Flag("🇰🇪", "Kenia", "Africa"),
        Flag("🇳🇬", "Nigeria", "Africa"),
        Flag("🇲🇦", "Marruecos", "Africa"),
        // Asia
        Flag("🇯🇵", "Japón", "Asia"),
        Flag("🇨🇳", "China", "Asia"),
        Flag("🇮🇳", "India", "Asia"),
        Flag("🇰🇷", "Corea del Sur", "Asia"),
        Flag("🇹🇭", "Tailandia", "Asia")
    )

    return if (region == "Mundo") {
        allFlags
    } else {
        allFlags.filter { it.region == region }
    }
}
