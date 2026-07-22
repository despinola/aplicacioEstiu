package com.vacaciones.app.games

import androidx.compose.runtime.Composable

object GameFactory {
    @Composable
    fun getGameForDay(day: Int, onGameComplete: (Int) -> Unit) {
        when (day) {
            1 -> FlagsGame(onGameComplete = onGameComplete)
            2 -> MathGame(onGameComplete = onGameComplete)
            3 -> CapitalsGame(region = "Europa", onGameComplete = onGameComplete)
            4 -> SnakeGame(onGameComplete = onGameComplete)
            5 -> MemoryGame(onGameComplete = onGameComplete)
            6 -> MathGame(operation = "multiply", onGameComplete = onGameComplete)
            7 -> FlagsGame(region = "America", onGameComplete = onGameComplete)
            8 -> WordSearchGame(onGameComplete = onGameComplete)
            9 -> HangmanGame(onGameComplete = onGameComplete)
            10 -> CapitalsGame(region = "Mundo", onGameComplete = onGameComplete)
            11 -> ConnectDotsGame(onGameComplete = onGameComplete)
            12 -> PuzzleGame(onGameComplete = onGameComplete)
            13 -> AnimalTriviaGame(onGameComplete = onGameComplete)
            14 -> FindDifferencesGame(onGameComplete = onGameComplete)
            15 -> MazeGame(onGameComplete = onGameComplete)
            16 -> ColorGuessGame(onGameComplete = onGameComplete)
            17 -> SequenceGame(onGameComplete = onGameComplete)
            18 -> CapitalsGame(region = "Asia", onGameComplete = onGameComplete)
            19 -> SimonSaysGame(onGameComplete = onGameComplete)
            20 -> WordChainGame(onGameComplete = onGameComplete)
            21 -> TangramGame(onGameComplete = onGameComplete)
            22 -> SportsQuizGame(onGameComplete = onGameComplete)
            23 -> SortNumbersGame(onGameComplete = onGameComplete)
            24 -> FlagsGame(region = "Africa", onGameComplete = onGameComplete)
            25 -> TicTacToeGame(onGameComplete = onGameComplete)
            26 -> RiddlesGame(onGameComplete = onGameComplete)
            27 -> CapitalsGame(region = "America", onGameComplete = onGameComplete)
            28 -> MatchImagesGame(onGameComplete = onGameComplete)
            29 -> DisneyQuizGame(onGameComplete = onGameComplete)
            30 -> MazeGame(level = 2, onGameComplete = onGameComplete)
            31 -> SurpriseGameFinale(onGameComplete = onGameComplete)
            else -> FlagsGame(onGameComplete = onGameComplete)
        }
    }

    fun getGameName(day: Int): String {
        return when (day) {
            1 -> "Endevina la bandera"
            2 -> "Sumes i restes"
            3 -> "Capitals d'Europa"
            4 -> "Joc de la serp"
            5 -> "Memory d'animals"
            6 -> "Multiplicacions"
            7 -> "Banderes d'Amèrica"
            8 -> "Sopa de lletres"
            9 -> "El penjat"
            10 -> "Capitals del món"
            11 -> "Unir punts"
            12 -> "Puzzle"
            13 -> "Trivia d'animals"
            14 -> "Troba diferències"
            15 -> "Laberint"
            16 -> "Endevina el color"
            17 -> "Seqüències"
            18 -> "Capitals d'Àsia"
            19 -> "Simon diu"
            20 -> "Paraules encadenades"
            21 -> "Tangram"
            22 -> "Quiz d'esports"
            23 -> "Ordena números"
            24 -> "Banderes d'Àfrica"
            25 -> "Tres en ratlla"
            26 -> "Endevinalles"
            27 -> "Capitals d'Amèrica"
            28 -> "Aparella imatges"
            29 -> "Quiz Disney"
            30 -> "Laberint difícil"
            31 -> "Sorpresa final!"
            else -> "Joc del dia"
        }
    }
}
