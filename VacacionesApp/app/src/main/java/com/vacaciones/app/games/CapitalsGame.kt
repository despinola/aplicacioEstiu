package com.vacaciones.app.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CountryCapital(val country: String, val capital: String, val region: String)

@Composable
fun CapitalsGame(
    region: String = "Europa",
    onGameComplete: (Int) -> Unit
) {
    val capitals = remember { getCapitalsForRegion(region) }
    var currentQuestion by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var gameFinished by remember { mutableStateOf(false) }

    val totalQuestions = 5
    val currentCapital = remember(currentQuestion) {
        capitals.random()
    }
    val options = remember(currentQuestion) {
        val wrongCapitals = capitals.filter { it.capital != currentCapital.capital }.shuffled().take(3)
        (wrongCapitals + currentCapital).shuffled()
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
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "🏛️",
                    fontSize = 60.sp,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = "Quina és la capital de ${currentCapital.country}?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                options.forEach { capital ->
                    val isSelected = selectedAnswer == capital.capital
                    val isCorrect = capital.capital == currentCapital.capital

                    Button(
                        onClick = {
                            if (!showResult) {
                                selectedAnswer = capital.capital
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
                        Text(capital.capital)
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

fun getCapitalsForRegion(region: String): List<CountryCapital> {
    val allCapitals = listOf(
        // Europa
        CountryCapital("Espanya", "Madrid", "Europa"),
        CountryCapital("França", "París", "Europa"),
        CountryCapital("Itàlia", "Roma", "Europa"),
        CountryCapital("Alemanya", "Berlín", "Europa"),
        CountryCapital("Regne Unit", "Londres", "Europa"),
        CountryCapital("Portugal", "Lisboa", "Europa"),
        CountryCapital("Països Baixos", "Amsterdam", "Europa"),
        CountryCapital("Bèlgica", "Brussel·les", "Europa"),
        CountryCapital("Suïssa", "Berna", "Europa"),
        CountryCapital("Grècia", "Atenes", "Europa"),
        CountryCapital("Àustria", "Viena", "Europa"),
        CountryCapital("Suècia", "Estocolm", "Europa"),
        // Amèrica
        CountryCapital("Estats Units", "Washington D.C.", "America"),
        CountryCapital("Canadà", "Ottawa", "America"),
        CountryCapital("Mèxic", "Ciutat de Mèxic", "America"),
        CountryCapital("Brasil", "Brasília", "America"),
        CountryCapital("Argentina", "Buenos Aires", "America"),
        CountryCapital("Xile", "Santiago", "America"),
        CountryCapital("Colòmbia", "Bogotà", "America"),
        CountryCapital("Perú", "Lima", "America"),
        // Àsia
        CountryCapital("Japó", "Tòquio", "Asia"),
        CountryCapital("Xina", "Pequín", "Asia"),
        CountryCapital("Índia", "Nova Delhi", "Asia"),
        CountryCapital("Corea del Sud", "Seül", "Asia"),
        CountryCapital("Tailàndia", "Bangkok", "Asia"),
        CountryCapital("Vietnam", "Hanoi", "Asia")
    )

    return if (region == "Mundo") {
        allCapitals
    } else {
        allCapitals.filter { it.region == region }
    }
}
