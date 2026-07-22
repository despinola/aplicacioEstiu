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
                    text = "De quin país és aquesta bandera?",
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
                        Text(if (currentQuestion + 1 < totalQuestions) "Següent" else "Acabar")
                    }
                }
            } else {
                Text(
                    text = "Joc completat!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Puntuació: $score de $totalQuestions",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = if (score == totalQuestions) "🎉 Perfecte!"
                           else if (score >= 3) "👍 Molt bé!"
                           else "💪 Continua practicant!",
                    fontSize = 32.sp
                )
            }
        }
    }
}

fun getFlagsForRegion(region: String): List<Flag> {
    val allFlags = listOf(
        // Europa
        Flag("🇪🇸", "Espanya", "Europa"),
        Flag("🇫🇷", "França", "Europa"),
        Flag("🇮🇹", "Itàlia", "Europa"),
        Flag("🇩🇪", "Alemanya", "Europa"),
        Flag("🇬🇧", "Regne Unit", "Europa"),
        Flag("🇵🇹", "Portugal", "Europa"),
        Flag("🇳🇱", "Països Baixos", "Europa"),
        Flag("🇧🇪", "Bèlgica", "Europa"),
        Flag("🇨🇭", "Suïssa", "Europa"),
        Flag("🇬🇷", "Grècia", "Europa"),
        // Amèrica
        Flag("🇺🇸", "Estats Units", "America"),
        Flag("🇨🇦", "Canadà", "America"),
        Flag("🇲🇽", "Mèxic", "America"),
        Flag("🇧🇷", "Brasil", "America"),
        Flag("🇦🇷", "Argentina", "America"),
        Flag("🇨🇱", "Xile", "America"),
        Flag("🇨🇴", "Colòmbia", "America"),
        Flag("🇵🇪", "Perú", "America"),
        // Àfrica
        Flag("🇿🇦", "Sud-àfrica", "Africa"),
        Flag("🇪🇬", "Egipte", "Africa"),
        Flag("🇰🇪", "Kenya", "Africa"),
        Flag("🇳🇬", "Nigèria", "Africa"),
        Flag("🇲🇦", "Marroc", "Africa"),
        // Àsia
        Flag("🇯🇵", "Japó", "Asia"),
        Flag("🇨🇳", "Xina", "Asia"),
        Flag("🇮🇳", "Índia", "Asia"),
        Flag("🇰🇷", "Corea del Sud", "Asia"),
        Flag("🇹🇭", "Tailàndia", "Asia")
    )

    return if (region == "Mundo") {
        allFlags
    } else {
        allFlags.filter { it.region == region }
    }
}
