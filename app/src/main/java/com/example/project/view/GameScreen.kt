package com.example.project.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
                Text(text = dialogTitle)
            },
            text = {
                Text(dialogMessage)
            },
            confirmButton = {
                TextButton(onClick = {
                    gameLogic = GameLogic(wordRepository.getRandomWord())
                    showDialog = false
                }) {
                    Text("Volver a jugar")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Intentos: ${3 - gameLogic.incorrectGuesses}",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ahorcado",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            HangmanDrawing(gameLogic.incorrectGuesses)

            Spacer(modifier = Modifier.height(32.dp))

            WordDisplay(gameLogic.word, gameLogic.guessedLetters)

            Spacer(modifier = Modifier.height(32.dp))

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
    Canvas(modifier = Modifier.size(400.dp)) {
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
                modifier = Modifier.padding(4.dp)
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
        textStyle = LocalTextStyle.current.copy(fontSize = 24.sp),
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)
    )
}



