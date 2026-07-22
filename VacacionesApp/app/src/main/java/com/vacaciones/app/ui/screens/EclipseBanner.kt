package com.vacaciones.app.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private val eclipseFacts = listOf(
    "🌑 Quan la Lluna es posa exactament davant del Sol, el tapa completament. Això és un eclipse solar total!",
    "👁️ Durant la totalitat, el cel s'enfosqueix i podeu veure les estrelles de dia. Els ocells s'emmudeixin pensant que és de nit.",
    "🌟 Apareix la corona solar: una llum suau i daurada al voltant del Sol que normalment no es pot veure mai.",
    "🌡️ La temperatura baixa uns 10 graus de cop! Tota la natura queda desconcertada.",
    "💍 Just abans i just després de la totalitat s'hi veu l'«anell de diamant»: un punt brillant amb un cercle de llum al voltant.",
    "⏱️ La totalitat dura pocs minuts. Avui serà especial i molt rar: ¡no l'oblideu mai!",
    "🕶️ IMPORTANT: Només podeu mirar el Sol directament durant la totalitat completa. Fora d'aquest moment, cal portar ulleres especials d'eclipse."
)

@Composable
fun EclipseBanner() {
    // Animation cycles through: partial ingress → totality → partial egress
    // 0.0 – 0.35 : ingress (moon comes in from right)
    // 0.35 – 0.65 : totality (moon fully covers sun, corona glows)
    // 0.65 – 1.0  : egress (moon exits to left)
    val infiniteTransition = rememberInfiniteTransition(label = "eclipse")
    val eclipsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "eclipsePhase"
    )

    // Corona glow pulses during totality
    val coronaGlow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "coronaGlow"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0D2B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌑 Eclipse Solar Total",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD740),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Avui, 12 d'agost del 2026",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFFFB300),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Eclipse animation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF050514), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawEclipseScene(eclipsePhase, coronaGlow)
                }

                // Phase label overlay
                val phaseLabel = when {
                    eclipsePhase < 0.35f -> "Eclipse parcial ☀️"
                    eclipsePhase < 0.65f -> "✨ TOTALITAT ✨"
                    else -> "Eclipse parcial ☀️"
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                        .background(Color(0x99000000), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = phaseLabel,
                        color = if (eclipsePhase in 0.35f..0.65f) Color(0xFFFFD740) else Color(0xFFFFFFFF),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (eclipsePhase in 0.35f..0.65f) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Què és un eclipse solar?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD740)
            )

            Spacer(modifier = Modifier.height(8.dp))

            eclipseFacts.forEach { fact ->
                val isWarning = fact.startsWith("🕶️")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            if (isWarning) Color(0xFFB71C1C).copy(alpha = 0.25f)
                            else Color.White.copy(alpha = 0.08f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = fact,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp,
                        color = if (isWarning) Color(0xFFFF8A80) else Color(0xFFE0E0E0)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

private fun DrawScope.drawEclipseScene(phase: Float, coronaGlow: Float) {
    val w = size.width
    val h = size.height
    val cx = w / 2f
    val cy = h / 2f

    // 0..1 normalized totality progress (peaks at 0.5)
    val totalityStrength = when {
        phase < 0.35f -> 0f
        phase < 0.65f -> {
            val t = (phase - 0.35f) / 0.30f
            // sine curve: 0→1→0 across totality
            sin(t * PI.toFloat()).coerceIn(0f, 1f)
        }
        else -> 0f
    }

    // Sky color interpolates: day blue → deep space black
    val skyBlue  = Color(0xFF0D1B45)
    val skyBlack = Color(0xFF010108)
    val skyColor = lerp(skyBlue, skyBlack, totalityStrength)
    drawRect(skyColor)

    // Stars — only visible during/near totality
    if (totalityStrength > 0.1f) {
        val starAlpha = (totalityStrength * 0.9f).coerceIn(0f, 1f)
        val stars = listOf(
            Offset(w*0.08f, h*0.12f), Offset(w*0.18f, h*0.35f),
            Offset(w*0.25f, h*0.10f), Offset(w*0.78f, h*0.15f),
            Offset(w*0.88f, h*0.32f), Offset(w*0.92f, h*0.10f),
            Offset(w*0.65f, h*0.08f), Offset(w*0.42f, h*0.05f),
            Offset(w*0.12f, h*0.60f), Offset(w*0.85f, h*0.65f),
            Offset(w*0.05f, h*0.45f), Offset(w*0.95f, h*0.50f)
        )
        stars.forEach { s ->
            drawCircle(Color.White.copy(alpha = starAlpha), 2.2f, s)
        }
    }

    val sunR = w * 0.13f

    // Sun glow (visible when not in full totality)
    if (totalityStrength < 0.95f) {
        val glowAlpha = ((1f - totalityStrength) * 0.45f).coerceIn(0f, 1f)
        drawCircle(
            Brush.radialGradient(
                listOf(Color(0x00FFD740), Color(0x00FFA000)),
                center = Offset(cx, cy),
                radius = sunR * 2.8f
            ),
            radius = sunR * 2.8f,
            center = Offset(cx, cy)
        )
        drawCircle(
            Brush.radialGradient(
                listOf(
                    Color(0xFFFFEB3B).copy(alpha = (glowAlpha * 2.5f).coerceIn(0f, 1f)),
                    Color(0xFFFF8F00).copy(alpha = (glowAlpha * 0.8f).coerceIn(0f, 1f)),
                    Color(0x00FF8F00)
                ),
                center = Offset(cx, cy),
                radius = sunR * 2.5f
            ),
            radius = sunR * 2.5f,
            center = Offset(cx, cy)
        )
    }

    // Sun rays (only when not in totality)
    if (totalityStrength < 0.8f) {
        val rayAlpha = (1f - totalityStrength * 1.25f).coerceIn(0f, 1f)
        val rayCount = 12
        for (i in 0 until rayCount) {
            val angle = i * (2 * PI / rayCount).toFloat()
            val innerR = sunR * 1.25f
            val outerR = sunR * (1.6f + if (i % 2 == 0) 0.3f else 0f)
            drawLine(
                Color(0xFFFFD740).copy(alpha = rayAlpha * 0.7f),
                Offset(cx + cos(angle) * innerR, cy + sin(angle) * innerR),
                Offset(cx + cos(angle) * outerR, cy + sin(angle) * outerR),
                strokeWidth = if (i % 2 == 0) 3f else 1.8f,
                cap = StrokeCap.Round
            )
        }
    }

    // Corona during totality
    if (totalityStrength > 0.05f) {
        val coroStrength = totalityStrength * coronaGlow
        // Multi-layer corona
        for (layer in 1..5) {
            val layerR = sunR * (1.15f + layer * 0.28f)
            val layerAlpha = coroStrength * (0.55f / layer)
            drawCircle(
                Color(0xFFFFF8E1).copy(alpha = layerAlpha),
                radius = layerR,
                center = Offset(cx, cy),
                style = Stroke(width = sunR * 0.18f)
            )
        }
        // Corona streamers (long rays)
        val streamerAngles = listOf(15f, 55f, 95f, 130f, 175f, 210f, 255f, 300f, 340f)
        streamerAngles.forEach { deg ->
            val rad = (deg * PI / 180f).toFloat()
            val len = sunR * (1.8f + (sin(deg * 0.15f) * 0.6f).toFloat().coerceIn(0f, 1f))
            drawLine(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFFFF8E1).copy(alpha = coroStrength * 0.7f),
                        Color(0x00FFF8E1)
                    ),
                    start = Offset(cx + cos(rad) * sunR, cy + sin(rad) * sunR),
                    end = Offset(cx + cos(rad) * (sunR + len), cy + sin(rad) * (sunR + len))
                ),
                start = Offset(cx + cos(rad) * sunR, cy + sin(rad) * sunR),
                end = Offset(cx + cos(rad) * (sunR + len), cy + sin(rad) * (sunR + len)),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }
    }

    // Sun disk
    val sunVisible = totalityStrength < 0.99f
    if (sunVisible) {
        val sunAlpha = (1f - (totalityStrength - 0.95f).coerceAtLeast(0f) * 20f).coerceIn(0f, 1f)
        drawCircle(
            Brush.radialGradient(
                listOf(Color(0xFFFFFFCC).copy(alpha = sunAlpha), Color(0xFFFFB300).copy(alpha = sunAlpha)),
                center = Offset(cx, cy),
                radius = sunR
            ),
            radius = sunR,
            center = Offset(cx, cy)
        )
    }

    // Moon position
    // Ingress: moon comes from right (cx + 2.5*sunR → cx)
    // Totality: moon centered on sun (cx)
    // Egress: moon exits to left (cx → cx - 2.5*sunR)
    val moonCx = when {
        phase < 0.35f -> {
            val t = phase / 0.35f
            cx + sunR * 2.5f * (1f - t)
        }
        phase < 0.65f -> cx
        else -> {
            val t = (phase - 0.65f) / 0.35f
            cx - sunR * 2.5f * t
        }
    }
    val moonCy = cy

    // Diamond ring effect (just before/after totality)
    val ringStrength = when {
        phase in 0.28f..0.38f || phase in 0.62f..0.72f -> {
            val d = minOf(
                (phase - 0.28f) / 0.07f,
                (0.38f - phase) / 0.07f + if (phase > 0.35f) 1f else 0f,
                (phase - 0.62f) / 0.07f,
                (0.72f - phase) / 0.07f
            ).coerceIn(0f, 1f)
            d
        }
        else -> 0f
    }

    if (ringStrength > 0.05f) {
        // Diamond "bead" of light
        val bead = findDiamondRingBead(moonCx, moonCy, cx, cy, sunR)
        drawCircle(
            Color(0xFFFFFFFF).copy(alpha = ringStrength * 0.95f),
            radius = 6f,
            center = bead
        )
        // Glow around bead
        drawCircle(
            Color(0xFFFFD740).copy(alpha = ringStrength * 0.5f),
            radius = 14f,
            center = bead
        )
    }

    // Moon disk (draw last, on top)
    drawCircle(Color(0xFF111118), radius = sunR * 1.02f, center = Offset(moonCx, moonCy))
    // Moon limb (slight rim)
    drawCircle(
        Color(0xFF2A2A35),
        radius = sunR * 1.02f,
        center = Offset(moonCx, moonCy),
        style = Stroke(width = 1.5f)
    )

    // Horizon line (ground)
    drawRect(
        Color(0xFF0A0A1A),
        topLeft = Offset(0f, h * 0.82f),
        size = Size(w, h * 0.18f)
    )
    // Silhouette of landscape
    val horizon = Path().apply {
        moveTo(0f, h * 0.82f)
        lineTo(w * 0.08f, h * 0.75f)
        lineTo(w * 0.15f, h * 0.79f)
        lineTo(w * 0.22f, h * 0.72f)
        lineTo(w * 0.28f, h * 0.78f)
        // flat middle (sea/beach)
        lineTo(w * 0.65f, h * 0.80f)
        lineTo(w * 0.72f, h * 0.74f)
        lineTo(w * 0.80f, h * 0.77f)
        lineTo(w * 0.88f, h * 0.73f)
        lineTo(w, h * 0.80f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(horizon, Color(0xFF0A0A1A))
}

private fun findDiamondRingBead(
    moonCx: Float, moonCy: Float,
    sunCx: Float, sunCy: Float,
    sunR: Float
): Offset {
    // The bead appears at the point on the sun's limb closest to the moon's edge
    val dx = moonCx - sunCx
    val dy = moonCy - sunCy
    val dist = sqrt(dx * dx + dy * dy).coerceAtLeast(0.01f)
    val nx = dx / dist
    val ny = dy / dist
    // Opposite side of moon center from displacement
    return Offset(sunCx - nx * sunR * 0.9f, sunCy - ny * sunR * 0.9f)
}

private fun lerp(a: Color, b: Color, t: Float): Color {
    val tc = t.coerceIn(0f, 1f)
    return Color(
        red   = a.red   + (b.red   - a.red)   * tc,
        green = a.green + (b.green - a.green) * tc,
        blue  = a.blue  + (b.blue  - a.blue)  * tc,
        alpha = 1f
    )
}
