package com.vacaciones.app.games

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign

private const val WS_SIZE = 10

private data class WSWord(
    val word: String,       // ASCII uppercase, sense accents
    val display: String,    // amb accents per a la UI
    val row: Int,
    val col: Int,
    val dr: Int,
    val dc: Int
) {
    fun cells(): List<Pair<Int, Int>> = word.indices.map { i ->
        (row + dr * i) to (col + dc * i)
    }
}

private val WS_WORD_LIST = listOf(
    "BANYADOR" to "BANYADOR",
    "PARASOL"  to "PARASOL",
    "PLATJA"   to "PLATJA",
    "GELAT"    to "GELAT",
    "ESTIU"    to "ESTIU",
    "SORRA"    to "SORRA",
    "BARCA"    to "BARCA",
    "NADAR"    to "NADAR",
    "DOFI"     to "DOFÍ",
    "ONES"     to "ONES"
)

private val WS_DIRS = listOf(
    0 to 1, 0 to -1, 1 to 0, -1 to 0,
    1 to 1, 1 to -1, -1 to 1, -1 to -1
)

private val WS_FILLER = "AAAEEEILLNNOOORRSSST".toList()

private val WS_COLORS = listOf(
    Color(0xCC43A047), Color(0xCC1E88E5), Color(0xCCFB8C00),
    Color(0xCC8E24AA), Color(0xCCD81B60), Color(0xCC00ACC1),
    Color(0xCC7CB342), Color(0xCCF4511E), Color(0xCC3949AB),
    Color(0xCC00897B)
)

private fun generateWordSearch(): Pair<Array<Array<Char>>, List<WSWord>> {
    val grid = Array(WS_SIZE) { Array(WS_SIZE) { ' ' } }
    val placed = mutableListOf<WSWord>()

    for ((word, display) in WS_WORD_LIST.sortedByDescending { it.first.length }) {
        var success = false
        for (attempt in 0 until 200) {
            if (success) break
            val (dr, dc) = WS_DIRS.random()
            val rowMin = if (dr > 0) 0 else if (dr < 0) word.length - 1 else 0
            val rowMax = if (dr > 0) WS_SIZE - word.length else if (dr < 0) WS_SIZE - 1 else WS_SIZE - 1
            val colMin = if (dc > 0) 0 else if (dc < 0) word.length - 1 else 0
            val colMax = if (dc > 0) WS_SIZE - word.length else if (dc < 0) WS_SIZE - 1 else WS_SIZE - 1
            if (rowMax < rowMin || colMax < colMin) continue

            val row = (rowMin..rowMax).random()
            val col = (colMin..colMax).random()

            var valid = true
            for (i in word.indices) {
                val r = row + dr * i; val c = col + dc * i
                if (r !in 0 until WS_SIZE || c !in 0 until WS_SIZE) { valid = false; break }
                val existing = grid[r][c]
                if (existing != ' ' && existing != word[i]) { valid = false; break }
            }
            if (valid) {
                for (i in word.indices) grid[row + dr * i][col + dc * i] = word[i]
                placed += WSWord(word, display, row, col, dr, dc)
                success = true
            }
        }
    }

    for (r in 0 until WS_SIZE) for (c in 0 until WS_SIZE) {
        if (grid[r][c] == ' ') grid[r][c] = WS_FILLER.random()
    }

    return grid to placed
}

private fun Offset.toWSCell(cellPx: Float): Pair<Int, Int>? {
    val r = (y / cellPx).toInt()
    val c = (x / cellPx).toInt()
    return if (r in 0 until WS_SIZE && c in 0 until WS_SIZE) r to c else null
}

private fun computeWSLine(start: Pair<Int, Int>, end: Pair<Int, Int>): List<Pair<Int, Int>> {
    val dr = sign((end.first  - start.first ).toFloat()).toInt()
    val dc = sign((end.second - start.second).toFloat()).toInt()
    val steps = when {
        dr == 0 && dc == 0 -> 0
        dr == 0 -> abs(end.second - start.second)
        dc == 0 -> abs(end.first  - start.first)
        else    -> min(abs(end.first - start.first), abs(end.second - start.second))
    }
    return (0..steps).map { i -> (start.first + dr * i) to (start.second + dc * i) }
}

private fun DrawScope.drawWordSearchGrid(
    grid: Array<Array<Char>>,
    entries: List<WSWord>,
    found: Set<String>,
    selection: List<Pair<Int, Int>>,
    cellPx: Float
) {
    drawRect(Color(0xFFF5F5F5))

    fun center(r: Int, c: Int) = Offset((c + 0.5f) * cellPx, (r + 0.5f) * cellPx)

    // Highlights de paraules trobades
    entries.forEachIndexed { idx, entry ->
        if (entry.word in found) {
            val cells = entry.cells()
            drawLine(
                color = WS_COLORS[idx % WS_COLORS.size],
                start = center(cells.first().first, cells.first().second),
                end   = center(cells.last().first,  cells.last().second),
                strokeWidth = cellPx * 0.82f,
                cap = StrokeCap.Round
            )
        }
    }

    // Highlight de la selecció activa
    if (selection.size == 1) {
        drawCircle(
            color = Color(0x881565C0),
            radius = cellPx * 0.40f,
            center = center(selection[0].first, selection[0].second)
        )
    } else if (selection.size > 1) {
        drawLine(
            color = Color(0x881565C0),
            start = center(selection.first().first, selection.first().second),
            end   = center(selection.last().first,  selection.last().second),
            strokeWidth = cellPx * 0.82f,
            cap = StrokeCap.Round
        )
    }

    // Línies de la graella
    repeat(WS_SIZE + 1) { i ->
        val p = i * cellPx
        drawLine(Color(0x22000000), Offset(p, 0f), Offset(p, WS_SIZE * cellPx), 0.5f)
        drawLine(Color(0x22000000), Offset(0f, p), Offset(WS_SIZE * cellPx, p), 0.5f)
    }

    val foundCells  = entries.filter { it.word in found }.flatMap { it.cells() }.toSet()
    val selectionSet = selection.toSet()

    // Lletres
    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
            textSize = cellPx * 0.52f
        }
        for (r in 0 until WS_SIZE) for (c in 0 until WS_SIZE) {
            val cell = r to c
            paint.color = if (cell in foundCells || cell in selectionSet)
                android.graphics.Color.WHITE
            else
                android.graphics.Color.argb(255, 33, 33, 33)
            val cx = center(r, c)
            val yText = cx.y - (paint.descent() + paint.ascent()) / 2f
            canvas.nativeCanvas.drawText(grid[r][c].toString(), cx.x, yText, paint)
        }
    }
}

@Composable
fun WordSearchGame(onGameComplete: (Int) -> Unit) {
    var gameKey by remember { mutableStateOf(0) }
    key(gameKey) {
        WordSearchContent(
            onGameComplete = onGameComplete,
            onPlayAgain = { gameKey++ }
        )
    }
}

@Composable
private fun WordSearchContent(
    onGameComplete: (Int) -> Unit,
    onPlayAgain: () -> Unit
) {
    val (grid, entries) = remember { generateWordSearch() }
    var found     by remember { mutableStateOf(setOf<String>()) }
    var selection by remember { mutableStateOf(listOf<Pair<Int, Int>>()) }
    var finished  by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!finished) {
                Text(
                    text = "🔍 Sopa de Lletres",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${found.size} / ${entries.size} paraules trobades",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Llista de paraules
                WSWordList(entries = entries, found = found)

                Spacer(modifier = Modifier.height(8.dp))

                // Graella
                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                ) {
                    val cellPx = constraints.maxWidth.toFloat() / WS_SIZE

                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                awaitEachGesture {
                                    val down = awaitFirstDown(requireUnconsumed = false)
                                    down.consume()
                                    val startCell = down.position.toWSCell(cellPx)
                                    startCell?.let { selection = listOf(it) }

                                    drag(down.id) { change ->
                                        change.consume()
                                        if (startCell != null) {
                                            val endCell = change.position.toWSCell(cellPx)
                                            if (endCell != null) {
                                                selection = computeWSLine(startCell, endCell)
                                            }
                                        }
                                    }

                                    // Comprova si la selecció és una paraula
                                    val sel = selection
                                    if (sel.size >= 2) {
                                        val selStr = sel.map { (r, c) -> grid[r][c] }.joinToString("")
                                        val match = entries.firstOrNull { entry ->
                                            entry.word !in found &&
                                            (entry.word == selStr || entry.word == selStr.reversed())
                                        }
                                        if (match != null) {
                                            val newFound = found + match.word
                                            found = newFound
                                            if (newFound.size == entries.size) {
                                                finished = true
                                                onGameComplete(newFound.size * 10)
                                            }
                                        }
                                    }
                                    selection = emptyList()
                                }
                            }
                    ) {
                        drawWordSearchGrid(grid, entries, found, selection, cellPx)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Arrossega el dit per seleccionar una paraula",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

            } else {
                // Pantalla de victòria
                Text(
                    text = "🎉 Has trobat totes les paraules!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("🔍", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Ets un expert en sopes de lletres!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Torna a jugar")
                }
            }
        }
    }
}

@Composable
private fun WSWordList(entries: List<WSWord>, found: Set<String>) {
    val half = (entries.size + 1) / 2
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (col in 0..1) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val from = col * half
                val to   = minOf(from + half, entries.size)
                for (i in from until to) {
                    val entry   = entries[i]
                    val iFound  = entry.word in found
                    val bgColor = if (iFound) WS_COLORS[i % WS_COLORS.size].copy(alpha = 0.18f)
                                  else Color.Transparent
                    Surface(
                        color = bgColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = entry.display,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (iFound) FontWeight.Normal else FontWeight.Bold,
                            textDecoration = if (iFound) TextDecoration.LineThrough else TextDecoration.None,
                            color = if (iFound) MaterialTheme.colorScheme.onSurfaceVariant
                                    else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
