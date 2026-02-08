package com.mahmoud.musemap.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val timestamp: Long,
    val audioPath: String? = null,
    val videoPath: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)