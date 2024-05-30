package com.example.project.db

class WordRepository {
    private val words = listOf("KOTLIN", "ANDROID", "COMPOSE", "PROGRAMMING", "DEVELOPER")

    fun getRandomWord(): String {
        return words.random()
    }
}
