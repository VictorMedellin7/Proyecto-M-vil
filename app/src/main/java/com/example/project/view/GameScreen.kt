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

@Composable
fun GameScreen() {
    var word by remember { mutableStateOf("KOTLIN") }
    var guessedLetters by remember { mutableStateOf("") }
    var incorrectGuesses by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ahorcado",
            style = MaterialTheme.typography.headlineMedium, // Utiliza headlineMedium para Material 3
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        HangmanDrawing(incorrectGuesses)

        Spacer(modifier = Modifier.height(32.dp))

        WordDisplay(word, guessedLetters)

        Spacer(modifier = Modifier.height(32.dp))

        LetterInput { letter ->
            if (word.contains(letter, ignoreCase = true)) {
                guessedLetters += letter.uppercase()
            } else {
                incorrectGuesses++
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Intentos incorrectos: $incorrectGuesses",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.error
        )
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
        }
        if (incorrectGuesses > 1) {
            drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 60f), end = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 150f), strokeWidth = 4f)
        }
        if (incorrectGuesses > 2) {
            drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 80f), end = Offset(200f + beamLength - 30f, baseY - poleHeight + nooseHeight + 110f), strokeWidth = 4f)
        }
        if (incorrectGuesses > 3) {
            drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 80f), end = Offset(200f + beamLength + 30f, baseY - poleHeight + nooseHeight + 110f), strokeWidth = 4f)
        }
        if (incorrectGuesses > 4) {
            drawLine(Color.Black, start = Offset(200f + beamLength, baseY - poleHeight + nooseHeight + 150f), end = Offset(200f + beamLength - 30f, baseY - poleHeight + nooseHeight + 210f), strokeWidth = 4f)
        }
        if (incorrectGuesses > 5) {
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


