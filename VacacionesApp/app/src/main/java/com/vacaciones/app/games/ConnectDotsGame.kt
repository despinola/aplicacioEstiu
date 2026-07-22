package com.vacaciones.app.games

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sqrt

private data class CDPuzzle(
    val name: String,
    val emoji: String,
    val dots: List<Pair<Float, Float>>,
    val lineColor: Color,
    val bgColor: Color,
    val closePath: Boolean = true
)

// 10 dots traçant la silueta d'una estrella de 5 puntes
private val CD_STAR = CDPuzzle(
    name = "Estrella",
    emoji = "⭐",
    lineColor = Color(0xFFFF8F00),
    bgColor = Color(0xFFE8EAF6),
    dots = listOf(
        0.50f to 0.08f,  //  1 punta superior
        0.60f to 0.36f,  //  2 interior dret-superior
        0.90f to 0.37f,  //  3 punta dreta
        0.66f to 0.55f,  //  4 interior dret-inferior
        0.75f to 0.84f,  //  5 punta inferior-dreta
        0.50f to 0.67f,  //  6 interior inferior
        0.25f to 0.84f,  //  7 punta inferior-esquerra
        0.34f to 0.55f,  //  8 interior esquerra-inferior
        0.10f to 0.37f,  //  9 punta esquerra
        0.40f to 0.36f,  // 10 interior esquerra-superior
    )
)

// 14 dots traçant la silueta d'un dofí
private val CD_DOLPHIN = CDPuzzle(
    name = "Dofí",
    emoji = "🐬",
    lineColor = Color(0xFF1565C0),
    bgColor = Color(0xFFE1F5FE),
    dots = listOf(
        0.88f to 0.52f,  //  1 punta del morro
        0.78f to 0.36f,  //  2 bec superior
        0.68f to 0.26f,  //  3 cim del cap
        0.60f to 0.24f,  //  4 base anterior de l'aleta dorsal
        0.50f to 0.08f,  //  5 punta de l'aleta dorsal
        0.40f to 0.24f,  //  6 base posterior de l'aleta dorsal
        0.28f to 0.32f,  //  7 esquena
        0.14f to 0.38f,  //  8 peduncle caudal superior
        0.04f to 0.20f,  //  9 lòbul caudal superior
        0.12f to 0.50f,  // 10 escotadura caudal
        0.04f to 0.72f,  // 11 lòbul caudal inferior
        0.16f to 0.62f,  // 12 peduncle caudal inferior
        0.42f to 0.72f,  // 13 ventre
        0.76f to 0.66f,  // 14 mandíbula inferior
    )
)

private val CD_PUZZLES = listOf(CD_STAR, CD_DOLPHIN)
private const val CD_THRESHOLD = 0.12f

@Composable
fun ConnectDotsGame(onGameComplete: (Int) -> Unit) {
    var puzzleIndex by remember { mutableStateOf(0) }
    var totalScore  by remember { mutableStateOf(0) }

    if (puzzleIndex >= CD_PUZZLES.size) {
        CDVictoryScreen(onGameComplete = { onGameComplete(totalScore) })
    } else {
        key(puzzleIndex) {
            CDPuzzleScreen(
                puzzle      = CD_PUZZLES[puzzleIndex],
                puzzleIndex = puzzleIndex,
                total       = CD_PUZZLES.size
            ) { pts ->
                totalScore += pts
                puzzleIndex++
            }
        }
    }
}

@Composable
private fun CDPuzzleScreen(
    puzzle: CDPuzzle,
    puzzleIndex: Int,
    total: Int,
    onPuzzleComplete: (Int) -> Unit
) {
    val nextDotState = remember { mutableStateOf(0) }
    val nextDot by nextDotState
    val isComplete = nextDot >= puzzle.dots.size

    val infiniteTransition = rememberInfiniteTransition(label = "cdPulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue  = 1.20f,
        animationSpec = infiniteRepeatable(
            tween(550, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "cdPulse"
    )

    LaunchedEffect(isComplete) {
        if (isComplete) {
            delay(1100L)
            onPuzzleComplete(50)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${puzzle.emoji} Unir Punts — ${puzzle.name}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = if (!isComplete) "Toca el punt ${nextDot + 1} de ${puzzle.dots.size}"
                       else "✅ ${puzzle.name} completada!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Dibuix ${puzzleIndex + 1} de $total",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))

            BoxWithConstraints(Modifier.fillMaxWidth().aspectRatio(1f)) {
                val sizePx = constraints.maxWidth.toFloat()
                Canvas(
                    Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { tap ->
                                val cur = nextDotState.value
                                if (cur >= puzzle.dots.size) return@detectTapGestures
                                val (fx, fy) = puzzle.dots[cur]
                                val dx = tap.x / sizePx - fx
                                val dy = tap.y / sizePx - fy
                                if (sqrt(dx * dx + dy * dy) <= CD_THRESHOLD) {
                                    nextDotState.value = cur + 1
                                }
                            }
                        }
                ) {
                    drawCDPuzzle(puzzle, nextDot, sizePx, pulse)
                }
            }

            Spacer(Modifier.height(6.dp))
            Text(
                text = "Toca els punts en ordre ☝️",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun DrawScope.drawCDPuzzle(
    puzzle: CDPuzzle,
    nextDot: Int,
    sizePx: Float,
    pulse: Float
) {
    drawRect(puzzle.bgColor)

    val dotR   = sizePx * 0.040f
    val lineW  = sizePx * 0.013f

    fun px(fx: Float, fy: Float) = Offset(fx * sizePx, fy * sizePx)

    // Línies de connexió completades
    for (i in 0 until nextDot - 1) {
        val (x1, y1) = puzzle.dots[i]
        val (x2, y2) = puzzle.dots[i + 1]
        drawLine(puzzle.lineColor, px(x1, y1), px(x2, y2), lineW, cap = StrokeCap.Round)
    }
    // Tancament quan tot connectat
    if (nextDot >= puzzle.dots.size && puzzle.closePath) {
        val (x1, y1) = puzzle.dots.last()
        val (x2, y2) = puzzle.dots.first()
        drawLine(puzzle.lineColor, px(x1, y1), px(x2, y2), lineW, cap = StrokeCap.Round)
    }

    // Punts i números
    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            isAntiAlias = true
            typeface    = Typeface.DEFAULT_BOLD
            textAlign   = Paint.Align.CENTER
        }

        puzzle.dots.forEachIndexed { i, (fx, fy) ->
            val cx = fx * sizePx
            val cy = fy * sizePx

            when {
                i < nextDot -> {
                    // Punt connectat: cercle ple del color del puzzle
                    drawCircle(puzzle.lineColor, dotR, Offset(cx, cy))
                    paint.color    = android.graphics.Color.WHITE
                    paint.textSize = dotR * 1.05f
                    canvas.nativeCanvas.drawText(
                        "${i + 1}", cx, cy - (paint.descent() + paint.ascent()) / 2f, paint
                    )
                }
                i == nextDot -> {
                    // Punt següent: pulsant taronja
                    val r = dotR * pulse
                    drawCircle(Color(0xFFFF8F00).copy(alpha = 0.22f), r * 2.2f, Offset(cx, cy))
                    drawCircle(Color(0xFFFF6F00), r, Offset(cx, cy))
                    drawCircle(Color.White, r * 0.42f, Offset(cx, cy))
                    paint.color    = android.graphics.Color.WHITE
                    paint.textSize = r * 1.05f
                    canvas.nativeCanvas.drawText(
                        "${i + 1}", cx, cy - (paint.descent() + paint.ascent()) / 2f, paint
                    )
                }
                else -> {
                    // Punt futur: gris petit
                    drawCircle(Color(0xFFBDBDBD), dotR * 0.72f, Offset(cx, cy))
                    drawCircle(Color.White, dotR * 0.38f, Offset(cx, cy))
                    paint.color    = android.graphics.Color.argb(180, 70, 70, 70)
                    paint.textSize = dotR * 0.85f
                    canvas.nativeCanvas.drawText(
                        "${i + 1}", cx, cy - (paint.descent() + paint.ascent()) / 2f, paint
                    )
                }
            }
        }
    }
}

@Composable
private fun CDVictoryScreen(onGameComplete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎉 Has unit tots els punts!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text("⭐  🐬", fontSize = 52.sp)
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Ets un artista! Has dibuixat una estrella i un dofí connectant els punts!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onGameComplete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Acabar")
            }
        }
    }
}
