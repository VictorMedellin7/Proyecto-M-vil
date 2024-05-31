package com.example.project.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.logic.GameLogic
import com.example.project.db.WordRepository

@Composable
fun GameScreen() {
    val wordRepository = WordRepository()
    var gameLogic by remember { mutableStateOf(GameLogic(wordRepository.getRandomWord())) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogTitle by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = dialogTitle, style = MaterialTheme.typography.headlineMedium, color = Color.Red)
            },
            text = {
                Text(dialogMessage, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            },
            confirmButton = {
                TextButton(onClick = {
                    gameLogic = GameLogic(wordRepository.getRandomWord())
                    showDialog = false
                }) {
                    Text("Volver a jugar", style = MaterialTheme.typography.labelLarge, color = Color.Black)
                }
            },
            modifier = Modifier.background(Color.White).border(2.dp, Color.Black)
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color(0xFFF0EAD6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ahorcado",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF6200EA)
            )

            HangmanDrawing(gameLogic.incorrectGuesses)

            Text(
                text = "Intentos: ${3 - gameLogic.incorrectGuesses}",
                fontSize = 20.sp,
                color = Color(0xFF6200EA)
            )

            WordDisplay(gameLogic.word, gameLogic.guessedLetters)

            GuessedLettersDisplay(gameLogic.guessedLettersList)

            if (gameLogic.gameOver) {
                dialogTitle = if (gameLogic.gameWon) "¡Felicidades!" else "¡Has perdido!"
                dialogMessage = if (gameLogic.gameWon) "¡Has ganado el juego!" else "La palabra era ${gameLogic.word}"
                showDialog = true
            } else {
                LetterInput { letter ->
                    gameLogic.makeGuess(letter)
                }
            }
        }
    }
}

@Composable
fun HangmanDrawing(incorrectGuesses: Int) {
    Canvas(modifier = Modifier.size(300.dp).border(2.dp, Color.Black)) {
        val baseY = 360f
        val poleHeight = 300f
        val beamLength = 200f
        val nooseHeight = 40f

        drawLine(Color.Black, start = Offset(75f, baseY), end = Offset(325f, baseY), strokeWidth = 10f)
        drawLine(Color.Black, start = Offset(200f, baseY), end = Offset(200f, baseY - poleHeight), strokeWidth = 10f)
        drawLine(Color.Black, start = Offset(200f, baseY - poleHeight), end = Offset(200f + beamLength, baseY - poleHeight), strokeWidth = 10f)
        drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight), end = Offset(200f + beamLength, baseY - poleHeight + nooseHeight), strokeWidth = 10f)

        if (incorrectGuesses > 0) {
            drawCircle(Color.Black, center = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 30f), radius = 30f, style = Stroke(width = 4f))
            drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 60f), end = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 150f), strokeWidth = 4f)
        }
        if (incorrectGuesses > 1) {
            drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 80f), end = Offset(200f + beamLength - 30f, baseY - poleHeight + nooseHeight + 110f), strokeWidth = 4f)
            drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 80f), end = Offset(200f + beamLength + 30f, baseY - poleHeight + nooseHeight + 110f), strokeWidth = 4f)
        }
        if (incorrectGuesses > 2) {
            drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 150f), end = Offset(200f + beamLength - 30f, baseY - poleHeight + nooseHeight + 210f), strokeWidth = 4f)
            drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 150f), end = Offset(200f + beamLength + 30f, baseY - poleHeight + nooseHeight + 210f), strokeWidth = 4f)
        }
    }
}

@Composable
fun WordDisplay(word: String, guessedLetters: String) {
    Row {
        word.forEach { letter ->
            val displayLetter = if (guessedLetters.contains(letter, ignoreCase = true)) {
                letter.toString()
            } else {
                "_"
            }
            Text(
                text = displayLetter,
                fontSize = 32.sp,
                modifier = Modifier.padding(4.dp),
                color = if (displayLetter == "_") Color.Gray else Color.Black,
                style = TextStyle(fontSize = 32.sp, color = Color.Black)
            )
        }
    }
}

@Composable
fun GuessedLettersDisplay(guessedLettersList: List<String>) {
    Row {
        Text(
            text = "Letras escritas: ",
            fontSize = 20.sp,
            color = Color(0xFF6200EA)
        )
        guessedLettersList.forEach { letter ->
            Text(
                text = letter,
                fontSize = 20.sp,
                color = Color(0xFF6200EA),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun LetterInput(onLetterEntered: (String) -> Unit) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    BasicTextField(
        value = textState,
        onValueChange = {
            if (it.text.length <= 1) {
                textState = it
                if (it.text.isNotEmpty()) {
                    onLetterEntered(it.text)
                    textState = TextFieldValue("")
                }
            }
        },
        textStyle = LocalTextStyle.current.copy(fontSize = 24.sp, color = Color.Black),
        modifier = Modifier
            .border(2.dp, Color.Black)
            .background(Color.White)
            .padding(8.dp)
            .width(50.dp)
    )
}

