package com.aman.todoapp.model

import java.util.Date

data class Task(
    val id: Int?,
    val title: String,
    val description: String,
    val date: Date
)

