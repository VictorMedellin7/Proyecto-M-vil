package com.example.project.db

class WordRepository {
    private val words = listOf(
        "KOTLIN",
        "ANDROID",
        "COMPOSE",
        "PROGRAMMING",
        "DEVELOPER",
        "FUNCTION",
        "VARIABLE",
        "COMPILER",
        "DATABASE",
        "DEBUGGING",
        "ITERATION",
        "SOFTWARE",
        "HARDWARE",
        "LIBRARY",
        "ALGORITHM",
        "COMPONENT",
        "INTERFACE",
        "NETWORK",
        "SECURITY",
        "ENCRYPTION"
    )

    fun getRandomWord(): String {
        return words.random()
    }
}

