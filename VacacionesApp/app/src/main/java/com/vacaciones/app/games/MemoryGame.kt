package com.vacaciones.app.games

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class MemoryCard(
    val id: Int,
    val emoji: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

@Composable
fun MemoryGame(onGameComplete: (Int) -> Unit) {
    val animals = listOf("🐶", "🐱", "🐭", "🐹", "🐰", "🦊")

    var cards by remember {
        mutableStateOf(
            (animals + animals)
                .mapIndexed { index, emoji -> MemoryCard(index, emoji) }
                .shuffled()
        )
    }

    var firstCardIndex by remember { mutableStateOf<Int?>(null) }
    var secondCardIndex by remember { mutableStateOf<Int?>(null) }
    var matches by remember { mutableStateOf(0) }
    var moves by remember { mutableStateOf(0) }
    var gameFinished by remember { mutableStateOf(false) }
    var canClick by remember { mutableStateOf(true) }

    LaunchedEffect(secondCardIndex) {
        if (secondCardIndex != null && firstCardIndex != null) {
            canClick = false
            delay(1000)

            val first = firstCardIndex!!
            val second = secondCardIndex!!

            if (cards[first].emoji == cards[second].emoji) {
                cards = cards.mapIndexed { index, card ->
                    if (index == first || index == second) {
                        card.copy(isMatched = true)
                    } else card
                }
                matches++

                if (matches == animals.size) {
                    gameFinished = true
                    val score = maxOf(0, 100 - (moves * 5))
                    onGameComplete(score)
                }
            } else {
                cards = cards.mapIndexed { index, card ->
                    if (index == first || index == second) {
                        card.copy(isFlipped = false)
                    } else card
                }
            }

            firstCardIndex = null
            secondCardIndex = null
            canClick = true
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!gameFinished) {
                Text(
                    text = "🎴 Memory d'Animals",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Moviments: $moves",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Parelles: $matches/${animals.size}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.height(400.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(cards.size) { index ->
                        val card = cards[index]
                        MemoryCardView(
                            card = card,
                            onClick = {
                                if (!canClick || card.isMatched || card.isFlipped) return@MemoryCardView

                                cards = cards.mapIndexed { i, c ->
                                    if (i == index) c.copy(isFlipped = true) else c
                                }

                                when {
                                    firstCardIndex == null -> {
                                        firstCardIndex = index
                                    }
                                    secondCardIndex == null && index != firstCardIndex -> {
                                        secondCardIndex = index
                                        moves++
                                    }
                                }
                            }
                        )
                    }
                }
            } else {
                Text(
                    text = "🎉 Completat!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "En $moves moviments",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (moves <= 12) "🏆 Increïble!"
                           else if (moves <= 16) "🌟 Molt bé!"
                           else "👍 Ben fet!",
                    fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
fun MemoryCardView(card: MemoryCard, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clickable(
                enabled = !card.isMatched && !card.isFlipped,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = when {
                card.isMatched -> MaterialTheme.colorScheme.tertiary
                card.isFlipped -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.primary
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (card.isFlipped || card.isMatched) 0.dp else 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (card.isFlipped || card.isMatched) card.emoji else "❓",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
