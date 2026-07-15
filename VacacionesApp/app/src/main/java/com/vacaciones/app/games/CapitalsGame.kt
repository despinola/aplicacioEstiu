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
                    text = "¿Cuál es la capital de ${currentCapital.country}?",
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

fun getCapitalsForRegion(region: String): List<CountryCapital> {
    val allCapitals = listOf(
        // Europa
        CountryCapital("España", "Madrid", "Europa"),
        CountryCapital("Francia", "París", "Europa"),
        CountryCapital("Italia", "Roma", "Europa"),
        CountryCapital("Alemania", "Berlín", "Europa"),
        CountryCapital("Reino Unido", "Londres", "Europa"),
        CountryCapital("Portugal", "Lisboa", "Europa"),
        CountryCapital("Países Bajos", "Ámsterdam", "Europa"),
        CountryCapital("Bélgica", "Bruselas", "Europa"),
        CountryCapital("Suiza", "Berna", "Europa"),
        CountryCapital("Grecia", "Atenas", "Europa"),
        CountryCapital("Austria", "Viena", "Europa"),
        CountryCapital("Suecia", "Estocolmo", "Europa"),
        // América
        CountryCapital("Estados Unidos", "Washington D.C.", "America"),
        CountryCapital("Canadá", "Ottawa", "America"),
        CountryCapital("México", "Ciudad de México", "America"),
        CountryCapital("Brasil", "Brasilia", "America"),
        CountryCapital("Argentina", "Buenos Aires", "America"),
        CountryCapital("Chile", "Santiago", "America"),
        CountryCapital("Colombia", "Bogotá", "America"),
        CountryCapital("Perú", "Lima", "America"),
        // Asia
        CountryCapital("Japón", "Tokio", "Asia"),
        CountryCapital("China", "Pekín", "Asia"),
        CountryCapital("India", "Nueva Delhi", "Asia"),
        CountryCapital("Corea del Sur", "Seúl", "Asia"),
        CountryCapital("Tailandia", "Bangkok", "Asia"),
        CountryCapital("Vietnam", "Hanói", "Asia")
    )

    return if (region == "Mundo") {
        allCapitals
    } else {
        allCapitals.filter { it.region == region }
    }
}
