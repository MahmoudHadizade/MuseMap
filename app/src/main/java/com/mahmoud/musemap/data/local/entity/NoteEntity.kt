package com.mahmoud.musemap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mahmoud.musemap.domain.model.Note

@Entity(tableName = "note_table")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val audioPath: String?,
    val videoPath: String?,
    val latitude: Double?,
    val longitude: Double?
)

fun NoteEntity.toDomain(): Note {
    return Note(
        id = id ?: 0,
        title = title,
        content = content,
        timestamp = timestamp,
        audioPath = audioPath,
        videoPath = videoPath,
        latitude = latitude,
        longitude = longitude
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = if (id == 0L) null else id, // If ID is 0, make it null so Room generates a new one.
        title = title,
        content = content,
        timestamp = timestamp,
        audioPath = audioPath,
        videoPath = videoPath,
        latitude = latitude,
        longitude = longitude
    )
}