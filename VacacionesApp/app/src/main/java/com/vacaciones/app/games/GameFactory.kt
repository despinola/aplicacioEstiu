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
            1 -> "Adivina la bandera"
            2 -> "Sumas y restas"
            3 -> "Capitales de Europa"
            4 -> "Juego de la serpiente"
            5 -> "Memory de animales"
            6 -> "Multiplicaciones"
            7 -> "Banderas de América"
            8 -> "Sopa de letras"
            9 -> "Ahorcado"
            10 -> "Capitales del mundo"
            11 -> "Unir puntos"
            12 -> "Puzzle"
            13 -> "Trivia de animales"
            14 -> "Encuentra diferencias"
            15 -> "Laberinto"
            16 -> "Adivina el color"
            17 -> "Secuencias"
            18 -> "Capitales de Asia"
            19 -> "Simon dice"
            20 -> "Palabras encadenadas"
            21 -> "Tangram"
            22 -> "Quiz de deportes"
            23 -> "Ordena números"
            24 -> "Banderas de África"
            25 -> "Tres en raya"
            26 -> "Adivinanzas"
            27 -> "Capitales de América"
            28 -> "Empareja imágenes"
            29 -> "Quiz Disney"
            30 -> "Laberinto difícil"
            31 -> "¡Sorpresa final!"
            else -> "Juego del día"
        }
    }
}
