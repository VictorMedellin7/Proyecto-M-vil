package com.example.project.logic

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class GameLogic(word: String) {
    var word: String by mutableStateOf(word)
        private set
    var guessedLetters: String by mutableStateOf("")
        private set
    var incorrectGuesses: Int by mutableStateOf(0)
        private set
    var gameOver: Boolean by mutableStateOf(false)
        private set
    var gameWon: Boolean by mutableStateOf(false)
        private set

    fun makeGuess(letter: String) {
        if (!guessedLetters.contains(letter, ignoreCase = true)) {
            if (word.contains(letter, ignoreCase = true)) {
                guessedLetters += letter.uppercase()
                if (word.all { it.uppercaseChar() in guessedLetters }) {
                    gameWon = true
                    gameOver = true
                }
            } else {
                incorrectGuesses++
                if (incorrectGuesses >= 3) {
                    gameOver = true
                }
            }
        }
    }
}

