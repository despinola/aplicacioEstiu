package com.vacaciones.app.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private enum class TttCell { EMPTY, X, O }
private enum class GameState { PLAYING, WIN_X, WIN_O, DRAW }
private enum class Difficulty { EASY, HARD }

private val WIN_LINES = listOf(
    listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // rows
    listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // cols
    listOf(0, 4, 8), listOf(2, 4, 6)                   // diagonals
)

private fun checkWinner(board: List<TttCell>): TttCell {
    for (line in WIN_LINES) {
        val v = board[line[0]]
        if (v != TttCell.EMPTY && line.all { board[it] == v }) return v
    }
    return TttCell.EMPTY
}

private fun gameState(board: List<TttCell>): GameState {
    return when (checkWinner(board)) {
        TttCell.X -> GameState.WIN_X
        TttCell.O -> GameState.WIN_O
        else -> if (board.none { it == TttCell.EMPTY }) GameState.DRAW else GameState.PLAYING
    }
}

private fun winLine(board: List<TttCell>): List<Int>? =
    WIN_LINES.firstOrNull { line ->
        val v = board[line[0]]
        v != TttCell.EMPTY && line.all { board[it] == v }
    }

// Minimax (O = AI maximizer, X = human minimizer)
private fun minimax(board: List<TttCell>, isMaximizing: Boolean, depth: Int): Int {
    return when (checkWinner(board)) {
        TttCell.O -> 10 - depth
        TttCell.X -> depth - 10
        else -> {
            if (board.none { it == TttCell.EMPTY }) return 0
            val moves = board.indices.filter { board[it] == TttCell.EMPTY }
            if (isMaximizing) {
                moves.maxOf { i ->
                    minimax(board.toMutableList().also { it[i] = TttCell.O }, false, depth + 1)
                }
            } else {
                moves.minOf { i ->
                    minimax(board.toMutableList().also { it[i] = TttCell.X }, true, depth + 1)
                }
            }
        }
    }
}

private fun bestMove(board: List<TttCell>): Int {
    var best = Int.MIN_VALUE
    var move = board.indexOfFirst { it == TttCell.EMPTY }
    board.indices.filter { board[it] == TttCell.EMPTY }.forEach { i ->
        val score = minimax(board.toMutableList().also { it[i] = TttCell.O }, false, 0)
        if (score > best) { best = score; move = i }
    }
    return move
}

private fun easyMove(board: List<TttCell>): Int {
    val empty = board.indices.filter { board[it] == TttCell.EMPTY }
    // 60% random, 40% minimax — fàcil però no inútil
    return if ((0..9).random() < 6) empty.random() else bestMove(board)
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeGame(onGameComplete: (Int) -> Unit) {
    var board by remember { mutableStateOf(List(9) { TttCell.EMPTY }) }
    var state by remember { mutableStateOf(GameState.PLAYING) }
    var humanTurn by remember { mutableStateOf(true) }
    var thinking by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf(Difficulty.HARD) }
    var humanWins by remember { mutableStateOf(0) }
    var aiWins by remember { mutableStateOf(0) }
    var draws by remember { mutableStateOf(0) }
    var gameFinished by remember { mutableStateOf(false) }
    var rounds by remember { mutableStateOf(0) }

    val winLineAnim by animateFloatAsState(
        targetValue = if (state != GameState.PLAYING) 1f else 0f,
        animationSpec = tween(600),
        label = "winLine"
    )

    // AI move
    LaunchedEffect(humanTurn, state) {
        if (!humanTurn && state == GameState.PLAYING) {
            thinking = true
            delay(500)
            val move = if (difficulty == Difficulty.HARD) bestMove(board) else easyMove(board)
            board = board.toMutableList().also { it[move] = TttCell.O }
            state = gameState(board)
            humanTurn = true
            thinking = false

            if (state != GameState.PLAYING) {
                rounds++
                when (state) {
                    GameState.WIN_O -> aiWins++
                    GameState.WIN_X -> { humanWins++; if (!gameFinished) { gameFinished = true; onGameComplete(if (difficulty == Difficulty.HARD) 100 else 60) } }
                    GameState.DRAW -> draws++
                    else -> {}
                }
            }
        }
    }

    fun reset() {
        board = List(9) { TttCell.EMPTY }
        state = GameState.PLAYING
        humanTurn = true
        thinking = false
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
                text = "❌⭕ Tres en Ratlla",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Difficulty selector
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dificultat:", style = MaterialTheme.typography.bodyMedium)
                FilterChip(
                    selected = difficulty == Difficulty.EASY,
                    onClick = { difficulty = Difficulty.EASY; reset() },
                    label = { Text("Fàcil") }
                )
                FilterChip(
                    selected = difficulty == Difficulty.HARD,
                    onClick = { difficulty = Difficulty.HARD; reset() },
                    label = { Text("Difícil") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Scoreboard
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreBox("Tu ❌", humanWins, Color(0xFF1565C0))
                ScoreBox("Empat", draws, Color(0xFF757575))
                ScoreBox("IA ⭕", aiWins, Color(0xFFB71C1C))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Status
            val statusText = when {
                thinking -> "🤔 L'IA està pensant..."
                state == GameState.WIN_X -> "🎉 Has guanyat!"
                state == GameState.WIN_O -> "🤖 Ha guanyat la IA!"
                state == GameState.DRAW -> "🤝 Empat!"
                humanTurn -> "👆 El teu torn (❌)"
                else -> ""
            }
            Text(
                text = statusText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = when (state) {
                    GameState.WIN_X -> Color(0xFF1565C0)
                    GameState.WIN_O -> Color(0xFFB71C1C)
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Board
            val currentWinLine = winLine(board)
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .padding(4.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawGrid()
                    currentWinLine?.let { drawWinLine(it, winLineAnim) }
                }
                // TttCells
                Column(modifier = Modifier.fillMaxSize()) {
                    for (row in 0..2) {
                        Row(modifier = Modifier.weight(1f)) {
                            for (col in 0..2) {
                                val idx = row * 3 + col
                                TttCellView(
                                    cell = board[idx],
                                    enabled = humanTurn && board[idx] == TttCell.EMPTY && state == GameState.PLAYING,
                                    modifier = Modifier.weight(1f).fillMaxHeight(),
                                    onClick = {
                                        board = board.toMutableList().also { it[idx] = TttCell.X }
                                        val newState = gameState(board)
                                        state = newState
                                        if (newState == GameState.PLAYING) {
                                            humanTurn = false
                                        } else {
                                            rounds++
                                            if (newState == GameState.WIN_X) {
                                                humanWins++
                                                if (!gameFinished) {
                                                    gameFinished = true
                                                    onGameComplete(if (difficulty == Difficulty.HARD) 100 else 60)
                                                }
                                            } else if (newState == GameState.DRAW) {
                                                draws++
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (state != GameState.PLAYING) {
                Button(
                    onClick = { reset() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tornar a jugar")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tu juegas com ❌ · La IA juga com ⭕",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ScoreBox(label: String, value: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TttCellView(
    cell: TttCell,
    enabled: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (cell != TttCell.EMPTY) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cellScale"
    )
    Box(
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        when (cell) {
            TttCell.X -> Canvas(modifier = Modifier.fillMaxSize(0.6f * scale)) {
                val s = size.minDimension
                val stroke = Stroke(width = s * 0.18f, cap = StrokeCap.Round)
                drawLine(Color(0xFF1565C0), Offset(0f, 0f), Offset(s, s), s * 0.18f, StrokeCap.Round)
                drawLine(Color(0xFF1565C0), Offset(s, 0f), Offset(0f, s), s * 0.18f, StrokeCap.Round)
            }
            TttCell.O -> Canvas(modifier = Modifier.fillMaxSize(0.6f * scale)) {
                val r = size.minDimension / 2f
                drawCircle(
                    color = Color(0xFFB71C1C),
                    radius = r * 0.82f,
                    center = Offset(r, r),
                    style = Stroke(width = r * 0.32f)
                )
            }
            TttCell.EMPTY -> {}
        }
    }
}

private fun DrawScope.drawGrid() {
    val w = size.width
    val h = size.height
    val color = Color(0xFF9E9E9E)
    val stroke = w * 0.015f
    val cap = StrokeCap.Round

    // Vertical lines
    drawLine(color, Offset(w / 3f, h * 0.05f), Offset(w / 3f, h * 0.95f), stroke, cap)
    drawLine(color, Offset(w * 2 / 3f, h * 0.05f), Offset(w * 2 / 3f, h * 0.95f), stroke, cap)
    // Horizontal lines
    drawLine(color, Offset(w * 0.05f, h / 3f), Offset(w * 0.95f, h / 3f), stroke, cap)
    drawLine(color, Offset(w * 0.05f, h * 2 / 3f), Offset(w * 0.95f, h * 2 / 3f), stroke, cap)
}

private fun DrawScope.drawWinLine(line: List<Int>, progress: Float) {
    val w = size.width
    val h = size.height
    val cellW = w / 3f
    val cellH = h / 3f

    fun center(idx: Int) = Offset(
        (idx % 3) * cellW + cellW / 2f,
        (idx / 3) * cellH + cellH / 2f
    )

    val start = center(line.first())
    val end = center(line.last())
    val current = Offset(
        start.x + (end.x - start.x) * progress,
        start.y + (end.y - start.y) * progress
    )

    drawLine(
        color = Color(0xFFFF6F00).copy(alpha = progress),
        start = start,
        end = current,
        strokeWidth = w * 0.025f,
        cap = StrokeCap.Round
    )
}
