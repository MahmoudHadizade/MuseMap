package com.mahmoud.musemap.domain.repository

import com.mahmoud.musemap.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Long): Note?
}