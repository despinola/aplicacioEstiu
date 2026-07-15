package com.vacaciones.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.drawscope.Fill

private val funFacts = listOf(
    "🏔️ Grenoble està envoltada de tres muntanyes! És com viure dins d'un bol gegant de muntanyes.",
    "🚡 Té un telecabina molt famós que va fins dalt d'una fortalesa. S'anomenen les 'Bulles' (bombolles) perquè les cabines són rodones i transparents!",
    "⛷️ Grenoble va acollir els Jocs Olímpics d'Hivern el 1968. Molts esquiadors d'arreu del món van venir a competir!",
    "🏰 Al cim de la muntanya hi ha la Bastilla, una fortalesa antiga des d'on es veu tota la ciutat i les Alps nevades.",
    "🌊 Dos rius es troben a Grenoble: l'Isère i el Drac. Imagina dos rius fent-se una abraçada!",
    "🧪 Grenoble és una de les ciutats més intel·ligents de França, amb molts científics i inventors treballant-hi."
)

@Composable
fun TravelDayBanner() {
    val infiniteTransition = rememberInfiniteTransition(label = "plane")
    val planeProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "plane"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F4FD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🚗 Avui comencem el viatge!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Barcelona → Grenoble",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Map
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFFB3E5FC), RoundedCornerShape(12.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawMap(planeProgress)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sabies que...?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            funFacts.forEach { fact ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color(0xFFFFFFFF).copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = fact,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

private fun DrawScope.drawMap(planeProgress: Float) {
    val w = size.width
    val h = size.height

    // --- Background sea ---
    drawRect(Color(0xFF81D4FA))

    // --- Iberian Peninsula (simplified) ---
    val iberia = Path().apply {
        moveTo(w * 0.02f, h * 0.55f)
        cubicTo(w * 0.00f, h * 0.30f, w * 0.08f, h * 0.08f, w * 0.20f, h * 0.06f)
        cubicTo(w * 0.30f, h * 0.04f, w * 0.42f, h * 0.10f, w * 0.44f, h * 0.20f)
        cubicTo(w * 0.46f, h * 0.30f, w * 0.40f, h * 0.42f, w * 0.38f, h * 0.52f)
        cubicTo(w * 0.30f, h * 0.65f, w * 0.15f, h * 0.68f, w * 0.02f, h * 0.55f)
        close()
    }
    drawPath(iberia, Color(0xFF8BC34A))
    drawPath(iberia, Color(0xFF558B2F), style = Stroke(1.5f))

    // --- South France (simplified) ---
    val france = Path().apply {
        moveTo(w * 0.44f, h * 0.20f)
        cubicTo(w * 0.50f, h * 0.10f, w * 0.65f, h * 0.04f, w * 0.80f, h * 0.08f)
        cubicTo(w * 0.92f, h * 0.12f, w * 0.98f, h * 0.22f, w * 0.98f, h * 0.40f)
        cubicTo(w * 0.90f, h * 0.50f, w * 0.75f, h * 0.55f, w * 0.60f, h * 0.52f)
        cubicTo(w * 0.50f, h * 0.50f, w * 0.44f, h * 0.40f, w * 0.44f, h * 0.20f)
        close()
    }
    drawPath(france, Color(0xFFA5D6A7))
    drawPath(france, Color(0xFF388E3C), style = Stroke(1.5f))

    // --- Pyrenees (mountain symbols) ---
    val pyrX = w * 0.44f
    val pyrY = h * 0.26f
    for (i in 0..3) {
        val mx = pyrX + i * w * 0.035f
        drawMountain(mx, pyrY, w * 0.016f, Color(0xFF795548))
    }

    // --- Alps (mountain symbols) ---
    val alpsStartX = w * 0.72f
    val alpsY = h * 0.20f
    for (i in 0..2) {
        val mx = alpsStartX + i * w * 0.042f
        drawMountain(mx, alpsY, w * 0.020f, Color(0xFF9E9E9E))
    }

    // --- Mediterranean coast label ---
    // (just visual color gradient already gives the feel)

    // --- Barcelona dot ---
    val bcnX = w * 0.34f
    val bcnY = h * 0.32f
    drawCircle(Color(0xFFE53935), 10f, Offset(bcnX, bcnY))
    drawCircle(Color.White, 5f, Offset(bcnX, bcnY))

    // --- Grenoble dot ---
    val grnX = w * 0.76f
    val grnY = h * 0.28f
    drawCircle(Color(0xFF1565C0), 10f, Offset(grnX, grnY))
    drawCircle(Color.White, 5f, Offset(grnX, grnY))

    // --- Route dashed line ---
    val routePath = Path().apply {
        moveTo(bcnX, bcnY)
        cubicTo(
            bcnX + (grnX - bcnX) * 0.3f, bcnY - h * 0.12f,
            bcnX + (grnX - bcnX) * 0.7f, grnY - h * 0.10f,
            grnX, grnY
        )
    }
    drawPath(
        routePath,
        Color(0xFFFF6F00),
        style = Stroke(
            width = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 8f), 0f),
            cap = StrokeCap.Round
        )
    )

    // --- Animated car/plane along the route ---
    val t = planeProgress.coerceIn(0f, 1f)
    val carPos = evalCubicBezier(
        Offset(bcnX, bcnY),
        Offset(bcnX + (grnX - bcnX) * 0.3f, bcnY - h * 0.12f),
        Offset(bcnX + (grnX - bcnX) * 0.7f, grnY - h * 0.10f),
        Offset(grnX, grnY),
        t
    )
    drawCircle(Color(0xFFFF6F00), 9f, carPos)
    drawCircle(Color.White, 5f, carPos)

    // --- City name labels (drawn as colored rectangles with implied text) ---
    // Barcelona label background
    drawRoundRect(
        Color(0xCCFFFFFF),
        topLeft = Offset(bcnX - 38f, bcnY + 14f),
        size = androidx.compose.ui.geometry.Size(76f, 20f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
    )
    // Grenoble label background
    drawRoundRect(
        Color(0xCCFFFFFF),
        topLeft = Offset(grnX - 42f, grnY + 14f),
        size = androidx.compose.ui.geometry.Size(84f, 20f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
    )
}

private fun DrawScope.drawMountain(cx: Float, baseY: Float, halfBase: Float, color: Color) {
    val path = Path().apply {
        moveTo(cx - halfBase, baseY)
        lineTo(cx, baseY - halfBase * 1.6f)
        lineTo(cx + halfBase, baseY)
        close()
    }
    drawPath(path, color)
    // Snow cap
    val snowPath = Path().apply {
        moveTo(cx, baseY - halfBase * 1.6f)
        lineTo(cx - halfBase * 0.35f, baseY - halfBase * 1.0f)
        lineTo(cx + halfBase * 0.35f, baseY - halfBase * 1.0f)
        close()
    }
    drawPath(snowPath, Color.White.copy(alpha = 0.85f))
}

private fun evalCubicBezier(p0: Offset, p1: Offset, p2: Offset, p3: Offset, t: Float): Offset {
    val mt = 1f - t
    return Offset(
        mt * mt * mt * p0.x + 3 * mt * mt * t * p1.x + 3 * mt * t * t * p2.x + t * t * t * p3.x,
        mt * mt * mt * p0.y + 3 * mt * mt * t * p1.y + 3 * mt * t * t * p2.y + t * t * t * p3.y
    )
}

// ── Day 2: Grenoble city day ─────────────────────────────────────────────────

private val grenobleFacts = listOf(
    "🚡 Pugeu a les 'Bulles'! Són cabines de telefèric rodones i transparents que pugen fins a la Bastilla. Us sentireu com si anéssiu dins d'una bombolla de sabó!",
    "🏰 A dalt de tot trobareu la Bastilla, una fortalesa antiga amb vistes increïbles a tota la ciutat i les muntanyes nevades.",
    "🌊 Passegeu per la vora de l'Isère, el riu que creua la ciutat. Podeu veure els ponts de colors i els patinadors!",
    "🎨 El centre vell de Grenoble té carrers estrets plens de cases de colors. Busqueu les façanes pintades amb murals gegants!",
    "🍫 Grenoble és famosa per les nous! Moltes llaminadures i pastissos de la zona porten nous. Tasteu-ne alguna!"
)

@Composable
fun GrenobleDay2Banner() {
    val infiniteTransition = rememberInfiniteTransition(label = "cable")
    val cableProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cable"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏙️ Dia a Grenoble",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Explorem la ciutat! 🚡🏰🌊",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF388E3C),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Grenoble illustration with cable car
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFB3E5FC), RoundedCornerShape(12.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawGrenobleScene(cableProgress)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Què farem avui?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )

            Spacer(modifier = Modifier.height(8.dp))

            grenobleFacts.forEach { fact ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(fact, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

private fun DrawScope.drawGrenobleScene(cableT: Float) {
    val w = size.width
    val h = size.height

    // Sky
    drawRect(Color(0xFF87CEEB))

    // Sun
    drawCircle(Color(0xFFFFD740), w * 0.06f, Offset(w * 0.88f, h * 0.14f))

    // Mountain right (Bastilla)
    val mountain = Path().apply {
        moveTo(w * 0.55f, h)
        lineTo(w * 0.72f, h * 0.22f)
        lineTo(w * 0.88f, h)
        close()
    }
    drawPath(mountain, Color(0xFF78909C))
    val snow = Path().apply {
        moveTo(w * 0.72f, h * 0.22f)
        lineTo(w * 0.65f, h * 0.48f)
        lineTo(w * 0.79f, h * 0.48f)
        close()
    }
    drawPath(snow, Color.White)

    // Bastilla fortress on top
    drawRect(Color(0xFF5D4037), topLeft = Offset(w * 0.69f, h * 0.20f), size = androidx.compose.ui.geometry.Size(w * 0.06f, h * 0.10f))
    drawRect(Color(0xFF4E342E), topLeft = Offset(w * 0.685f, h * 0.16f), size = androidx.compose.ui.geometry.Size(w * 0.016f, h * 0.06f))
    drawRect(Color(0xFF4E342E), topLeft = Offset(w * 0.705f, h * 0.16f), size = androidx.compose.ui.geometry.Size(w * 0.016f, h * 0.06f))
    drawRect(Color(0xFF4E342E), topLeft = Offset(w * 0.725f, h * 0.16f), size = androidx.compose.ui.geometry.Size(w * 0.016f, h * 0.06f))

    // Ground / city
    drawRect(Color(0xFF8BC34A), topLeft = Offset(0f, h * 0.72f), size = androidx.compose.ui.geometry.Size(w, h * 0.28f))

    // Buildings
    listOf(
        Triple(0.04f, 0.38f, 0.10f),
        Triple(0.15f, 0.28f, 0.09f),
        Triple(0.25f, 0.42f, 0.11f),
        Triple(0.37f, 0.32f, 0.08f)
    ).forEach { (x, topFrac, width) ->
        val bh = h * (0.72f - topFrac)
        drawRect(
            Color(0xFFECEFF1),
            topLeft = Offset(w * x, h * topFrac),
            size = androidx.compose.ui.geometry.Size(w * width, bh)
        )
        drawRect(
            Color(0xFFB0BEC5),
            topLeft = Offset(w * x, h * topFrac),
            size = androidx.compose.ui.geometry.Size(w * width, bh),
            style = Stroke(1f)
        )
        // windows
        for (row in 0..1) {
            for (col in 0..1) {
                drawRect(
                    Color(0xFFFFD740).copy(alpha = 0.8f),
                    topLeft = Offset(w * x + w * width * 0.15f + col * w * width * 0.4f, h * topFrac + h * 0.06f + row * h * 0.09f),
                    size = androidx.compose.ui.geometry.Size(w * width * 0.22f, h * 0.06f)
                )
            }
        }
    }

    // River at bottom
    drawRect(Color(0xFF29B6F6), topLeft = Offset(0f, h * 0.80f), size = androidx.compose.ui.geometry.Size(w * 0.55f, h * 0.08f))

    // Cable car wire
    val wireStart = Offset(w * 0.10f, h * 0.65f)
    val wireEnd = Offset(w * 0.72f, h * 0.26f)
    drawLine(Color(0xFF424242), wireStart, wireEnd, 2f)

    // Animated cable car cabin (bubble)
    val cabinPos = Offset(
        wireStart.x + (wireEnd.x - wireStart.x) * cableT,
        wireStart.y + (wireEnd.y - wireStart.y) * cableT
    )
    drawCircle(Color(0xDDFFFFFF), w * 0.038f, cabinPos)
    drawCircle(Color(0xFF0288D1), w * 0.038f, cabinPos, style = Stroke(2.5f))
    // silhouettes inside
    drawCircle(Color(0xFF5D4037), w * 0.010f, Offset(cabinPos.x - w * 0.013f, cabinPos.y + w * 0.005f))
    drawCircle(Color(0xFF5D4037), w * 0.010f, Offset(cabinPos.x + w * 0.013f, cabinPos.y + w * 0.005f))
}

// ── Day 3: Mont Blanc + Lago Maggiore ────────────────────────────────────────

private val montBlancFacts = listOf(
    "🏔️ El Mont Blanc és la muntanya més alta de tota Europa occidental! Fa 4.808 metres. Si poseu 16.000 nens un sobre l'altre arribaríeu al cim!",
    "🚡 El telefèric de l'Aiguille du Midi és un dels més alts del món. En pocs minuts pujeu des dels 1.035m fins als 3.842m. Feu-vos una foto amb les Alps als peus!",
    "❄️ Allà dalt sempre hi ha neu, fins i tot a l'estiu! La temperatura pot ser de -10°C quan a baix fa 25°C.",
    "🏕️ Per la tarda, el Lago Maggiore us espera! És un llac enorme entre França, Suïssa i Itàlia, ple d'illes amb jardins de flors increïbles.",
    "🌊 Al Lago Maggiore hi ha les Illes Borromees, tres illetes amb palaus i jardins fantàstics. Semblen sortides d'un conte!"
)

@Composable
fun MontBlancLagoDay3Banner() {
    val infiniteTransition = rememberInfiniteTransition(label = "cable3")
    val cableProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cable3"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏔️ Mont Blanc → 🏕️ Lago Maggiore",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0),
                textAlign = TextAlign.Center
            )
            Text(
                text = "La muntanya més alta d'Europa i a dormir al llac!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mont Blanc scene
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFB3E5FC), RoundedCornerShape(12.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawMontBlancScene(cableProgress)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lago Maggiore scene
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFF80DEEA), RoundedCornerShape(12.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLagoScene()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ho sabíeu?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            montBlancFacts.forEach { fact ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(fact, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

private fun DrawScope.drawMontBlancScene(cableT: Float) {
    val w = size.width
    val h = size.height

    // Sky gradient effect (just light blue)
    drawRect(Color(0xFFB3E5FC))
    // Higher sky darker blue at top
    drawRect(Color(0xFF4FC3F7).copy(alpha = 0.5f), topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(w, h * 0.4f))

    // Clouds
    listOf(Offset(w * 0.12f, h * 0.12f), Offset(w * 0.55f, h * 0.08f), Offset(w * 0.80f, h * 0.16f)).forEach { c ->
        drawCircle(Color.White.copy(alpha = 0.9f), w * 0.045f, c)
        drawCircle(Color.White.copy(alpha = 0.9f), w * 0.035f, Offset(c.x + w * 0.04f, c.y + h * 0.01f))
        drawCircle(Color.White.copy(alpha = 0.9f), w * 0.035f, Offset(c.x - w * 0.04f, c.y + h * 0.01f))
    }

    // Background mountains
    val bgMountain = Path().apply {
        moveTo(0f, h * 0.75f)
        lineTo(w * 0.15f, h * 0.40f)
        lineTo(w * 0.30f, h * 0.60f)
        lineTo(w * 0.45f, h * 0.35f)
        lineTo(w * 0.60f, h * 0.55f)
        lineTo(w * 0.75f, h * 0.30f)
        lineTo(w * 0.90f, h * 0.50f)
        lineTo(w, h * 0.42f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(bgMountain, Color(0xFF90A4AE))

    // Mont Blanc main peak
    val montBlanc = Path().apply {
        moveTo(w * 0.25f, h)
        lineTo(w * 0.50f, h * 0.06f)
        lineTo(w * 0.75f, h)
        close()
    }
    drawPath(montBlanc, Color(0xFF607D8B))

    // Snow on Mont Blanc (large)
    val snowMB = Path().apply {
        moveTo(w * 0.50f, h * 0.06f)
        lineTo(w * 0.36f, h * 0.36f)
        cubicTo(w * 0.42f, h * 0.30f, w * 0.58f, h * 0.30f, w * 0.64f, h * 0.36f)
        close()
    }
    drawPath(snowMB, Color.White)
    // Snow texture lines
    for (i in 1..3) {
        drawLine(
            Color(0xFFE0E0E0),
            Offset(w * 0.50f - w * 0.04f * i, h * (0.14f + i * 0.05f)),
            Offset(w * 0.50f + w * 0.04f * i, h * (0.14f + i * 0.05f)),
            1.5f
        )
    }

    // Ground / valley
    drawRect(Color(0xFF66BB6A), topLeft = Offset(0f, h * 0.82f), size = androidx.compose.ui.geometry.Size(w, h * 0.18f))

    // Cable car wire from valley to near summit
    val wireBot = Offset(w * 0.15f, h * 0.78f)
    val wireTop = Offset(w * 0.50f, h * 0.32f)
    drawLine(Color(0xFF424242), wireBot, wireTop, 2f)

    // Animated cabin
    val cabinPos = Offset(
        wireBot.x + (wireTop.x - wireBot.x) * cableT,
        wireBot.y + (wireTop.y - wireBot.y) * cableT
    )
    drawRect(
        Color(0xDDFFFFFF),
        topLeft = Offset(cabinPos.x - w * 0.032f, cabinPos.y - h * 0.05f),
        size = androidx.compose.ui.geometry.Size(w * 0.064f, h * 0.08f)
    )
    drawRect(
        Color(0xFF0288D1),
        topLeft = Offset(cabinPos.x - w * 0.032f, cabinPos.y - h * 0.05f),
        size = androidx.compose.ui.geometry.Size(w * 0.064f, h * 0.08f),
        style = Stroke(2f)
    )
    // Cabin rope up
    drawLine(Color(0xFF424242), Offset(cabinPos.x, cabinPos.y - h * 0.05f), Offset(cabinPos.x, cabinPos.y - h * 0.09f), 1.5f)

    // "4808m" marker at top
    drawCircle(Color(0xFFFFD740), w * 0.025f, Offset(w * 0.50f, h * 0.06f))
}

private fun DrawScope.drawLagoScene() {
    val w = size.width
    val h = size.height

    // Sky
    drawRect(Color(0xFFE3F2FD), size = androidx.compose.ui.geometry.Size(w, h * 0.35f))

    // Water
    drawRect(Color(0xFF29B6F6), topLeft = Offset(0f, h * 0.35f), size = androidx.compose.ui.geometry.Size(w, h * 0.65f))

    // Water shimmer
    for (i in 0..5) {
        drawLine(
            Color.White.copy(alpha = 0.3f),
            Offset(w * 0.05f + i * w * 0.15f, h * 0.55f),
            Offset(w * 0.12f + i * w * 0.15f, h * 0.55f),
            2f
        )
        drawLine(
            Color.White.copy(alpha = 0.3f),
            Offset(w * 0.02f + i * w * 0.15f, h * 0.68f),
            Offset(w * 0.10f + i * w * 0.15f, h * 0.68f),
            2f
        )
    }

    // Far shore hills
    val hills = Path().apply {
        moveTo(0f, h * 0.35f)
        cubicTo(w * 0.15f, h * 0.10f, w * 0.35f, h * 0.05f, w * 0.50f, h * 0.18f)
        cubicTo(w * 0.65f, h * 0.05f, w * 0.85f, h * 0.08f, w, h * 0.20f)
        lineTo(w, h * 0.35f)
        close()
    }
    drawPath(hills, Color(0xFF81C784))

    // Island
    drawCircle(Color(0xFF66BB6A), w * 0.06f, Offset(w * 0.38f, h * 0.42f))
    // Palazzo on island
    drawRect(Color(0xFFFFF9C4), topLeft = Offset(w * 0.345f, h * 0.28f), size = androidx.compose.ui.geometry.Size(w * 0.07f, h * 0.15f))
    drawRect(Color(0xFFE57373), topLeft = Offset(w * 0.345f, h * 0.24f), size = androidx.compose.ui.geometry.Size(w * 0.07f, h * 0.06f))

    // Camping tent on right shore
    val tentX = w * 0.78f
    val tentY = h * 0.38f
    val tent = Path().apply {
        moveTo(tentX - w * 0.07f, tentY + h * 0.12f)
        lineTo(tentX, tentY)
        lineTo(tentX + w * 0.07f, tentY + h * 0.12f)
        close()
    }
    drawPath(tent, Color(0xFFFF8F00))
    drawPath(tent, Color(0xFFE65100), style = Stroke(1.5f))
    // Tent door
    drawRect(Color(0xFF5D4037), topLeft = Offset(tentX - w * 0.018f, tentY + h * 0.06f), size = androidx.compose.ui.geometry.Size(w * 0.036f, h * 0.06f))

    // Stars in sky
    listOf(Offset(w*0.08f, h*0.08f), Offset(w*0.50f, h*0.12f), Offset(w*0.85f, h*0.06f), Offset(w*0.68f, h*0.18f)).forEach { s ->
        drawCircle(Color(0xFFFFD740), 3f, s)
    }

    // Moon
    drawCircle(Color(0xFFFFF9C4), w * 0.04f, Offset(w * 0.90f, h * 0.10f))
    drawCircle(Color(0xFFE3F2FD), w * 0.030f, Offset(w * 0.905f, h * 0.085f))
}
