package com.vacaciones.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vacaciones.app.data.User
import com.vacaciones.app.ui.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointsScreen(
    viewModel: AppViewModel,
    onBackClick: () -> Unit
) {
    val allUsersPoints by viewModel.allUsersPoints.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val totalPoints by viewModel.currentUserTotalPoints.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏆 Classificació", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Tornar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Medalla del primer lloc si n'hi ha dades
            if (allUsersPoints.isNotEmpty()) {
                val (leader, leadPts) = allUsersPoints.first()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🥇", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        UserAvatar(
                            name = leader.name,
                            fallbackEmoji = leader.avatar,
                            size = 56.dp,
                            fallbackFontSize = 40.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = leader.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$leadPts punts",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF8F00)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Llista completa
            allUsersPoints.forEachIndexed { index, (user, pts) ->
                val isCurrentUser = user.id == currentUser?.id
                val medal = when (index) {
                    0 -> "🥇"
                    1 -> "🥈"
                    2 -> "🥉"
                    else -> "  ${index + 1}."
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrentUser)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = medal,
                            fontSize = 24.sp,
                            modifier = Modifier.width(48.dp),
                            textAlign = TextAlign.Center
                        )
                        UserAvatar(
                            name = user.name,
                            fallbackEmoji = user.avatar,
                            size = 40.dp,
                            fallbackFontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$pts ⭐",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (pts >= 0) Color(0xFF2E7D32) else Color(0xFFB71C1C)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Taula de punts de referència
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📋 Taula de punts",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    PointsReferenceRow("📖 Llegir", "+100 ⭐", Color(0xFF1565C0))
                    PointsReferenceRow("🏊 Anar a la piscina", "+30 ⭐", Color(0xFF1565C0))
                    PointsReferenceRow("🍽️ Parar la taula", "+20 ⭐", Color(0xFF1565C0))
                    PointsReferenceRow("🧼 Rentar plats", "+35 ⭐", Color(0xFF1565C0))
                    PointsReferenceRow("🥐 Preparar l'esmorzar", "+35 ⭐", Color(0xFF1565C0))
                    PointsReferenceRow("🪥 Rentar les dents", "+15 ⭐", Color(0xFF1565C0))
                    PointsReferenceRow("🌅 Veure la posta de sol", "+50 ⭐", Color(0xFF1565C0))
                    PointsReferenceRow("🌠 Veure estels fugaços", "+200 ⭐", Color(0xFF1565C0))
                    PointsReferenceRow("🌑 Eclipse (12 ag.)", "+400 ⭐", Color(0xFF1565C0))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    PointsReferenceRow("📺 TV 30 min", "+5 ⭐", Color(0xFF558B2F))
                    PointsReferenceRow("📺 TV +1 hora", "-5 ⭐", Color(0xFFE65100))
                    PointsReferenceRow("📺 TV +2 hores", "-15 ⭐", Color(0xFFB71C1C))
                    PointsReferenceRow("📱 Tablet/tel. 20 min", "0 ⭐", Color(0xFF757575))
                    PointsReferenceRow("📱 Tablet/tel. +1 hora", "-30 ⭐", Color(0xFFB71C1C))
                }
            }
        }
    }
}

@Composable
private fun PointsReferenceRow(task: String, points: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = task,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = points,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
