package ru.sikuda.mobile.todo_compose.model

import java.io.Serializable

data class Note(
    val id: Long,
    val date: String,
    val content: String,
    val details: String,
    val filename: String
    ): Serializable
