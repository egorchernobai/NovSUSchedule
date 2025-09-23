package ru.chezzz.novsuapp

data class Lesson(
    val dayOfWeek: String,
    val startTime: String,
    val type: String,
    val subject: String,
    val teacher: String?,
    val room: String?,
    val comment: String?,
    val endTime: String
)
