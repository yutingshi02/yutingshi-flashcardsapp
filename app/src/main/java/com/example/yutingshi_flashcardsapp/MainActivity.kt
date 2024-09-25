package com.example.yutingshi_flashcardsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class Flashcard(val question: String, val answer: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashcardQuizApp()
        }
    }
}

@Composable
fun FlashcardQuizApp() {
    // List of flashcards
    val flashcards = listOf(
        Flashcard("How old am I?", "22"),
        Flashcard("What is my zodiac sign?", "Virgo"),
        Flashcard("Where am I from?", "California"),
        Flashcard("Where was I born?", "Shanghai"),
        Flashcard("What type of dog do I have?", "Pomeranian"),
        Flashcard("Where do I go to school?", "Boston University")
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var quizComplete by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Check if quiz is complete
    if (currentQuestionIndex >= flashcards.size) {
        quizComplete = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE4E1)) // Pink background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!quizComplete) {
            // Display question card
            Card(
                modifier = Modifier.padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = flashcards[currentQuestionIndex].question,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input field for the answer
            BasicTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Submit button
            Button(onClick = {
                val isCorrect = userInput.equals(flashcards[currentQuestionIndex].answer, ignoreCase = true)
                val snackbarMessage = if (isCorrect) {
                    "Correct!"
                } else {
                    "Incorrect. The correct answer is: ${flashcards[currentQuestionIndex].answer}"
                }

                // Show Snackbar with feedback
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(snackbarMessage)
                }

                userInput = ""
                currentQuestionIndex++
            }) {
                Text("Submit Answer")
            }

            // Snackbar for feedback
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // Display completion message
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    Button(onClick = {
                        currentQuestionIndex = 0
                        userInput = ""
                        quizComplete = false
                    }) {
                        Text("Restart Quiz")
                    }
                }
            ) {
                Text("Quiz complete! You answered all questions.")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFlashcardQuizApp() {
    FlashcardQuizApp()
}
