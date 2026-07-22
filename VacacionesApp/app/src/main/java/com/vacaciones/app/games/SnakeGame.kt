package com.vacaciones.app.games

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private const val COLS = 15
private const val ROWS = 15
private const val INITIAL_SPEED_MS = 300L
private const val MIN_SPEED_MS = 120L

private data class Cell(val x: Int, val y: Int)

private enum class Dir { UP, DOWN, LEFT, RIGHT }

private fun Dir.opposite() = when (this) {
    Dir.UP -> Dir.DOWN
    Dir.DOWN -> Dir.UP
    Dir.LEFT -> Dir.RIGHT
    Dir.RIGHT -> Dir.LEFT
}

private fun randomFood(snake: List<Cell>): Cell {
    val free = (0 until COLS).flatMap { x -> (0 until ROWS).map { y -> Cell(x, y) } } - snake.toSet()
    return if (free.isEmpty()) Cell(0, 0) else free.random()
}

@Composable
fun SnakeGame(onGameComplete: (Int) -> Unit) {
    var snake by remember { mutableStateOf(listOf(Cell(7, 7), Cell(6, 7), Cell(5, 7))) }
    var dir by remember { mutableStateOf(Dir.RIGHT) }
    var pendingDir by remember { mutableStateOf(Dir.RIGHT) }
    var food by remember { mutableStateOf(Cell(11, 7)) }
    var score by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var started by remember { mutableStateOf(false) }
    var gameFinished by remember { mutableStateOf(false) }

    val speedMs = (INITIAL_SPEED_MS - score * 8).coerceAtLeast(MIN_SPEED_MS)

    LaunchedEffect(started, gameOver) {
        if (!started || gameOver) return@LaunchedEffect
        while (!gameOver) {
            delay(speedMs)
            dir = pendingDir
            val head = snake.first()
            val newHead = when (dir) {
                Dir.UP -> Cell(head.x, head.y - 1)
                Dir.DOWN -> Cell(head.x, head.y + 1)
                Dir.LEFT -> Cell(head.x - 1, head.y)
                Dir.RIGHT -> Cell(head.x + 1, head.y)
            }
            val hitWall = newHead.x !in 0 until COLS || newHead.y !in 0 until ROWS
            val hitSelf = newHead in snake
            if (hitWall || hitSelf) {
                gameOver = true
                gameFinished = true
                onGameComplete(score * 10)
                return@LaunchedEffect
            }
            val ate = newHead == food
            snake = if (ate) listOf(newHead) + snake else listOf(newHead) + snake.dropLast(1)
            if (ate) {
                score++
                food = randomFood(snake)
            }
        }
    }

    val snakeGreen = Color(0xFF2E7D32)
    val snakeLight = Color(0xFF66BB6A)
    val foodRed = Color(0xFFE53935)
    val gridBg = Color(0xFF1B5E20)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🐍 Joc de la Serp",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "🍎 Punts: $score",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "📏 Longitud: ${snake.size}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Game board
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(gridBg, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cellW = size.width / COLS
                    val cellH = size.height / ROWS
                    val padding = 1.5f

                    // Grid lines (subtle)
                    for (x in 0..COLS) {
                        drawLine(
                            color = Color(0x22FFFFFF),
                            start = Offset(x * cellW, 0f),
                            end = Offset(x * cellW, size.height),
                            strokeWidth = 0.5f
                        )
                    }
                    for (y in 0..ROWS) {
                        drawLine(
                            color = Color(0x22FFFFFF),
                            start = Offset(0f, y * cellH),
                            end = Offset(size.width, y * cellH),
                            strokeWidth = 0.5f
                        )
                    }

                    // Snake body
                    snake.forEachIndexed { i, cell ->
                        drawRoundRect(
                            color = if (i == 0) snakeGreen else snakeLight,
                            topLeft = Offset(cell.x * cellW + padding, cell.y * cellH + padding),
                            size = Size(cellW - padding * 2, cellH - padding * 2),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                        )
                    }

                    // Eyes on head
                    if (snake.isNotEmpty()) {
                        val head = snake.first()
                        val cx = head.x * cellW + cellW / 2
                        val cy = head.y * cellH + cellH / 2
                        val eyeOffset = cellW * 0.18f
                        val eyeR = cellW * 0.08f
                        val (e1, e2) = when (dir) {
                            Dir.RIGHT -> Pair(
                                Offset(cx + eyeOffset, cy - eyeOffset),
                                Offset(cx + eyeOffset, cy + eyeOffset)
                            )
                            Dir.LEFT -> Pair(
                                Offset(cx - eyeOffset, cy - eyeOffset),
                                Offset(cx - eyeOffset, cy + eyeOffset)
                            )
                            Dir.UP -> Pair(
                                Offset(cx - eyeOffset, cy - eyeOffset),
                                Offset(cx + eyeOffset, cy - eyeOffset)
                            )
                            Dir.DOWN -> Pair(
                                Offset(cx - eyeOffset, cy + eyeOffset),
                                Offset(cx + eyeOffset, cy + eyeOffset)
                            )
                        }
                        drawCircle(Color.White, eyeR, e1)
                        drawCircle(Color.White, eyeR, e2)
                    }

                    // Food (apple)
                    drawCircle(
                        color = foodRed,
                        radius = (cellW.coerceAtMost(cellH) / 2) - padding * 2,
                        center = Offset(food.x * cellW + cellW / 2, food.y * cellH + cellH / 2)
                    )
                    // Apple shine
                    drawCircle(
                        color = Color(0x66FFFFFF),
                        radius = (cellW.coerceAtMost(cellH) / 6),
                        center = Offset(
                            food.x * cellW + cellW * 0.35f,
                            food.y * cellH + cellH * 0.35f
                        )
                    )
                }

                // Overlays
                if (!started) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("🐍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { started = true }) {
                            Text("Començar!", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (gameOver) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xCC000000), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "💀 Game Over!",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Puntuació: $score",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = {
                                snake = listOf(Cell(7, 7), Cell(6, 7), Cell(5, 7))
                                dir = Dir.RIGHT
                                pendingDir = Dir.RIGHT
                                food = randomFood(snake)
                                score = 0
                                gameOver = false
                                gameFinished = false
                                started = true
                            }) {
                                Text("Tornar a intentar")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // D-pad controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                DPadButton("▲") {
                    if (pendingDir != Dir.DOWN) pendingDir = Dir.UP
                    if (!started) started = true
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    DPadButton("◀") {
                        if (pendingDir != Dir.RIGHT) pendingDir = Dir.LEFT
                        if (!started) started = true
                    }
                    Spacer(modifier = Modifier.size(56.dp))
                    DPadButton("▶") {
                        if (pendingDir != Dir.LEFT) pendingDir = Dir.RIGHT
                        if (!started) started = true
                    }
                }
                DPadButton("▼") {
                    if (pendingDir != Dir.UP) pendingDir = Dir.DOWN
                    if (!started) started = true
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Menja pomes 🍎 i no xoquis amb les parets ni amb tu mateix",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DPadButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = label, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
