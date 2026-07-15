package com.vacaciones.app.games

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class HangmanWord(val word: String, val hint: String)

private val WORDS = listOf(
    HangmanWord("PAPALLONA", "Insecte que vola 🦋"),
    HangmanWord("CASTANYA", "Fruit de tardor 🌰"),
    HangmanWord("GRANOTA", "Animal que salta 🐸"),
    HangmanWord("TORTUGA", "Animal molt lent 🐢"),
    HangmanWord("GIRASOL", "Flor que segueix el sol 🌻"),
    HangmanWord("ELEFANT", "Animal molt gran 🐘"),
    HangmanWord("PARAIGUA", "El fem servir quan plou ☂️"),
    HangmanWord("LLIMONA", "Fruita groga i àcida 🍋"),
    HangmanWord("CARAGOL", "Animal amb closca 🐌"),
    HangmanWord("MUSSOL", "Ocell nocturn 🦉"),
    HangmanWord("ERIÇÓ", "Animal amb punxes 🦔"),
    HangmanWord("ARANYA", "Fa teranyines 🕷️"),
    HangmanWord("CIGALA", "Canta a l'estiu 🎵"),
    HangmanWord("DOFÍ", "Animal marí intel·ligent 🐬"),
    HangmanWord("PINGÜÍ", "Ocell que no vola 🐧"),
    HangmanWord("LLANGARDAIX", "Rèptil petit 🦎"),
    HangmanWord("TARONGER", "Arbre que fa taronges 🍊"),
    HangmanWord("BALENA", "L'animal més gran del mar 🐋"),
    HangmanWord("ESQUIROL", "Menja fruits secs 🐿️"),
    HangmanWord("FORMIGA", "Insecte molt treballador 🐜")
)

private val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toList()
private const val MAX_ERRORS = 6

@Composable
fun HangmanGame(onGameComplete: (Int) -> Unit) {
    var wordEntry by remember { mutableStateOf(WORDS.random()) }
    var guessed by remember { mutableStateOf(setOf<Char>()) }
    var errors by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var won by remember { mutableStateOf(false) }
    var gameFinished by remember { mutableStateOf(false) }

    val word = wordEntry.word
    val normalizedWord = word.map { it.normalize() }
    val revealedAll = normalizedWord.all { it in guessed }

    LaunchedEffect(revealedAll, errors) {
        if (revealedAll && !gameOver) {
            won = true
            gameOver = true
            gameFinished = true
            val score = maxOf(0, 100 - errors * 15)
            onGameComplete(score)
        } else if (errors >= MAX_ERRORS && !gameOver) {
            won = false
            gameOver = true
            gameFinished = true
            onGameComplete(0)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎯 El Penjat",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "❌ Errors: $errors / $MAX_ERRORS",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (errors >= 4) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Gallows drawing
            Canvas(
                modifier = Modifier
                    .size(160.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            ) {
                drawGallows(errors)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hint
            Text(
                text = "Pista: ${wordEntry.hint}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Word display
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                word.forEachIndexed { i, ch ->
                    val normalized = ch.normalize()
                    val isSpace = ch == ' '
                    val revealed = normalized in guessed || isSpace
                    Box(
                        modifier = Modifier
                            .then(if (!isSpace) Modifier.width(24.dp) else Modifier.width(12.dp))
                            .then(
                                if (!isSpace) Modifier.border(
                                    width = 2.dp,
                                    color = if (revealed) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(4.dp)
                                ) else Modifier
                            )
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (revealed && !isSpace) {
                            Text(
                                text = ch.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = if (won && gameOver) Color(0xFF2E7D32)
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!gameOver) {
                // Keyboard
                val rows = listOf(
                    ALPHABET.subList(0, 7),
                    ALPHABET.subList(7, 14),
                    ALPHABET.subList(14, 20),
                    ALPHABET.subList(20, 26)
                )
                rows.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        row.forEach { letter ->
                            val isGuessed = letter in guessed
                            val isInWord = letter in normalizedWord
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(
                                        color = when {
                                            !isGuessed -> MaterialTheme.colorScheme.primary
                                            isInWord -> Color(0xFF2E7D32)
                                            else -> Color(0xFFBDBDBD)
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable(enabled = !isGuessed) {
                                        guessed = guessed + letter
                                        if (letter !in normalizedWord) errors++
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter.toString(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            } else {
                // Result
                Text(
                    text = if (won) "🎉 Molt bé! Has guanyat!" else "💀 Has perdut...",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (won) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                if (!won) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "La paraula era: $word",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {
                    wordEntry = WORDS.random()
                    guessed = setOf()
                    errors = 0
                    gameOver = false
                    won = false
                    gameFinished = false
                }) {
                    Text("Torna a jugar")
                }
            }
        }
    }
}

private fun Char.normalize(): Char = when (this) {
    'À', 'Á', 'Â', 'Ã', 'Ä' -> 'A'
    'È', 'É', 'Ê', 'Ë' -> 'E'
    'Ì', 'Í', 'Î', 'Ï' -> 'I'
    'Ò', 'Ó', 'Ô', 'Õ', 'Ö' -> 'O'
    'Ù', 'Ú', 'Û', 'Ü' -> 'U'
    'Ç' -> 'C'
    'Ñ' -> 'N'
    'L' -> 'L'
    else -> this
}

private fun DrawScope.drawGallows(errors: Int) {
    val w = size.width
    val h = size.height
    val strokeW = 4f
    val color = Color(0xFF5D4037)

    // Base
    drawLine(color, Offset(w * 0.1f, h * 0.92f), Offset(w * 0.9f, h * 0.92f), strokeW, StrokeCap.Round)
    // Pole vertical
    drawLine(color, Offset(w * 0.25f, h * 0.92f), Offset(w * 0.25f, h * 0.06f), strokeW, StrokeCap.Round)
    // Pole horizontal
    drawLine(color, Offset(w * 0.25f, h * 0.06f), Offset(w * 0.62f, h * 0.06f), strokeW, StrokeCap.Round)
    // Rope
    drawLine(color, Offset(w * 0.62f, h * 0.06f), Offset(w * 0.62f, h * 0.18f), strokeW, StrokeCap.Round)

    val cx = w * 0.62f
    val headR = w * 0.09f

    if (errors >= 1) {
        // Head
        drawCircle(color, headR, Offset(cx, h * 0.27f), style = androidx.compose.ui.graphics.drawscope.Stroke(strokeW))
    }
    if (errors >= 2) {
        // Body
        drawLine(color, Offset(cx, h * 0.36f), Offset(cx, h * 0.62f), strokeW, StrokeCap.Round)
    }
    if (errors >= 3) {
        // Left arm
        drawLine(color, Offset(cx, h * 0.42f), Offset(cx - w * 0.12f, h * 0.54f), strokeW, StrokeCap.Round)
    }
    if (errors >= 4) {
        // Right arm
        drawLine(color, Offset(cx, h * 0.42f), Offset(cx + w * 0.12f, h * 0.54f), strokeW, StrokeCap.Round)
    }
    if (errors >= 5) {
        // Left leg
        drawLine(color, Offset(cx, h * 0.62f), Offset(cx - w * 0.12f, h * 0.78f), strokeW, StrokeCap.Round)
    }
    if (errors >= 6) {
        // Right leg
        drawLine(color, Offset(cx, h * 0.62f), Offset(cx + w * 0.12f, h * 0.78f), strokeW, StrokeCap.Round)
    }
}
