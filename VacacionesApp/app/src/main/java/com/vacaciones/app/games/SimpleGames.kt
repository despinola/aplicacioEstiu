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

// Placeholder games - estos se pueden implementar más adelante con más detalle

// SnakeGame está implementado en SnakeGame.kt

// MemoryGame ahora está implementado en MemoryGame.kt

@Composable
fun WordSearchGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🔍 Sopa de Letras", "Encuentra las palabras", onGameComplete)
}

// HangmanGame està implementat a HangmanGame.kt

@Composable
fun ConnectDotsGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("⚫ Unir Puntos", "Conecta los números", onGameComplete)
}

@Composable
fun PuzzleGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🧩 Puzzle", "Completa el puzzle", onGameComplete)
}

@Composable
fun AnimalTriviaGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🦁 Trivia de Animales", "¿Cuánto sabes?", onGameComplete)
}

// FindDifferencesGame está implementado en FindDifferencesGame.kt

@Composable
fun MazeGame(level: Int = 1, onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🌀 Laberinto ${if (level > 1) "Nivel $level" else ""}", "Encuentra la salida", onGameComplete)
}

@Composable
fun ColorGuessGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🎨 Adivina el Color", "¿Qué color es?", onGameComplete)
}

@Composable
fun SequenceGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🔢 Secuencias", "Completa la secuencia", onGameComplete)
}

@Composable
fun SimonSaysGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🎵 Simon Dice", "Repite la secuencia", onGameComplete)
}

@Composable
fun WordChainGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🔗 Palabras Encadenadas", "Encadena palabras", onGameComplete)
}

@Composable
fun TangramGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🔺 Tangram", "Forma la figura", onGameComplete)
}

@Composable
fun SportsQuizGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("⚽ Quiz de Deportes", "¿Cuánto sabes?", onGameComplete)
}

@Composable
fun SortNumbersGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("📊 Ordena Números", "Del menor al mayor", onGameComplete)
}

@Composable
fun TicTacToeGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("❌⭕ Tres en Raya", "Juega contra la IA", onGameComplete)
}

@Composable
fun RiddlesGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🤔 Adivinanzas", "Resuelve las adivinanzas", onGameComplete)
}

@Composable
fun MatchImagesGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🖼️ Empareja Imágenes", "Encuentra las parejas", onGameComplete)
}

@Composable
fun DisneyQuizGame(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🏰 Quiz Disney", "Películas y personajes", onGameComplete)
}

@Composable
fun SurpriseGameFinale(onGameComplete: (Int) -> Unit) {
    PlaceholderGame("🎉 ¡SORPRESA FINAL!", "¡Has completado todo el mes!", onGameComplete)
}

@Composable
fun PlaceholderGame(
    title: String,
    description: String,
    onGameComplete: (Int) -> Unit
) {
    var completed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!completed) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "🎮",
                    fontSize = 80.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        completed = true
                        onGameComplete(100)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Completar Juego")
                }
            } else {
                Text(
                    text = "✅ ¡Completado!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "🎉",
                    fontSize = 64.sp
                )
            }
        }
    }
}
