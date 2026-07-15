package com.vacaciones.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

private data class Particle(
    val x: Float,
    val y: Float,
    val vy: Float,
    val vx: Float,
    val color: Color,
    val size: Float,
    val rotation: Float,
    val rotSpeed: Float,
    val isRect: Boolean
)

private val CONFETTI_COLORS = listOf(
    Color(0xFFFF4081), Color(0xFFFFD740), Color(0xFF40C4FF),
    Color(0xFF69F0AE), Color(0xFFFF6E40), Color(0xFFE040FB),
    Color(0xFFFFEA00), Color(0xFF00E5FF), Color(0xFFFF80AB)
)

private fun newParticle(startY: Float = Random.nextFloat()) = Particle(
    x = Random.nextFloat(),
    y = startY,
    vy = 0.0018f + Random.nextFloat() * 0.003f,
    vx = (Random.nextFloat() - 0.5f) * 0.0012f,
    color = CONFETTI_COLORS.random(),
    size = 5f + Random.nextFloat() * 9f,
    rotation = Random.nextFloat() * 360f,
    rotSpeed = (Random.nextFloat() - 0.5f) * 6f,
    isRect = Random.nextBoolean()
)

data class BirthdayConfig(
    val name: String,
    val subtitle: String,
    val message: String,
    val emoji: String = "🎂",
    val titleColor: Color,
    val subtitleColor: Color,
    val messageColor: Color,
    val bgColor: Color,
    val customTitle: String? = null
)

val BIRTHDAY_RITA = BirthdayConfig(
    name = "Rita",
    subtitle = "Avui fas 8 anys! 🎉",
    message = "Ets la nostra princesa i la llum de casa.\nQue tots els teus somnis es facin realitat! ✨",
    titleColor = Color(0xFFE91E63),
    subtitleColor = Color(0xFF7B1FA2),
    messageColor = Color(0xFF4A148C),
    bgColor = Color(0xFFFFF8E1)
)

val BIRTHDAY_BLAI = BirthdayConfig(
    name = "Blai",
    subtitle = "Avui fas 10 anys! 🎉",
    message = "Ets un crack i l'orgull de la família.\nQue aquest any nou t'arribi ple d'aventures! 🚀",
    titleColor = Color(0xFF1565C0),
    subtitleColor = Color(0xFF00796B),
    messageColor = Color(0xFF1A237E),
    bgColor = Color(0xFFE3F2FD)
)

val ANIVERSARI_PARELLA = BirthdayConfig(
    name = "David i Miriam",
    customTitle = "Feliç aniversari! 💑",
    subtitle = "22 anys junts! ❤️",
    message = "Des del 9 d'agost del 2004, cada dia és\nuna aventura compartida. Us estimem molt!",
    emoji = "💍",
    titleColor = Color(0xFFB71C1C),
    subtitleColor = Color(0xFFE53935),
    messageColor = Color(0xFF4E342E),
    bgColor = Color(0xFFFFEBEE)
)

val SANT_MIRIAM = BirthdayConfig(
    name = "Miriam",
    subtitle = "Avui és el teu sant! 🌸",
    message = "Ets la millor mare del món.\nQue aquest dia t'ompli d'amor i alegria! 💛",
    emoji = "🌸",
    titleColor = Color(0xFF6A1B9A),
    subtitleColor = Color(0xFFC2185B),
    messageColor = Color(0xFF4A148C),
    bgColor = Color(0xFFF3E5F5)
)

@Composable
fun BirthdayBanner(config: BirthdayConfig) {
    var particles by remember { mutableStateOf(List(70) { newParticle() }) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(16L)
            particles = particles.map { p ->
                val ny = p.y + p.vy
                if (ny > 1.05f) newParticle(startY = -0.05f)
                else p.copy(
                    y = ny,
                    x = (p.x + p.vx).coerceIn(0f, 1f),
                    rotation = (p.rotation + p.rotSpeed) % 360f
                )
            }
        }
    }

    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.90f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = config.bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                particles.forEach { p ->
                    val px = p.x * size.width
                    val py = p.y * size.height
                    val s = p.size * density
                    withTransform({
                        translate(px, py)
                        rotate(p.rotation, pivot = Offset.Zero)
                    }) {
                        if (p.isRect) {
                            drawRect(
                                color = p.color.copy(alpha = 0.85f),
                                topLeft = Offset(-s / 2f, -s / 4f),
                                size = Size(s, s / 2f)
                            )
                        } else {
                            drawCircle(
                                color = p.color.copy(alpha = 0.85f),
                                radius = s / 2.5f,
                                center = Offset.Zero
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = config.emoji,
                    fontSize = 56.sp,
                    modifier = Modifier.scale(pulse)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = config.customTitle ?: "Moltes felicitats, ${config.name}!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = config.titleColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = config.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = config.subtitleColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = config.message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = config.messageColor,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
