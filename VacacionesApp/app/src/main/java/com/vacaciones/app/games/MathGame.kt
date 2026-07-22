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
import kotlin.random.Random

@Composable
fun MathGame(
    operation: String = "add_subtract",
    onGameComplete: (Int) -> Unit
) {
    var currentQuestion by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var gameFinished by remember { mutableStateOf(false) }

    val totalQuestions = 5

    val (num1, num2, operator, correctAnswer) = remember(currentQuestion) {
        generateMathQuestion(operation)
    }

    val options = remember(currentQuestion) {
        val wrongAnswers = mutableSetOf<Int>()
        while (wrongAnswers.size < 3) {
            val wrong = correctAnswer + Random.nextInt(-10, 11)
            if (wrong != correctAnswer && wrong >= 0) {
                wrongAnswers.add(wrong)
            }
        }
        (wrongAnswers + correctAnswer).shuffled()
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

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Quant és?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$num1 $operator $num2 = ?",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    options.forEach { option ->
                        val isSelected = selectedAnswer == option
                        val isCorrect = option == correctAnswer

                        Button(
                            onClick = {
                                if (!showResult) {
                                    selectedAnswer = option
                                    showResult = true
                                    if (isCorrect) score++
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    showResult && isCorrect -> MaterialTheme.colorScheme.tertiary
                                    showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.primary
                                }
                            ),
                            enabled = !showResult
                        ) {
                            Text(
                                text = option.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (showResult) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (selectedAnswer == correctAnswer) "✅ Correcte!" else "❌ Incorrecte",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedAnswer == correctAnswer)
                            MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.error
                    )
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

data class MathQuestion(val num1: Int, val num2: Int, val operator: String, val answer: Int)

fun generateMathQuestion(operation: String): MathQuestion {
    return when (operation) {
        "multiply" -> {
            val num1 = Random.nextInt(2, 11)
            val num2 = Random.nextInt(2, 11)
            MathQuestion(num1, num2, "×", num1 * num2)
        }
        else -> { // add_subtract
            val num1 = Random.nextInt(10, 51)
            val num2 = Random.nextInt(1, 31)
            if (Random.nextBoolean()) {
                MathQuestion(num1, num2, "+", num1 + num2)
            } else {
                MathQuestion(num1, num2, "-", num1 - num2)
            }
        }
    }
}
