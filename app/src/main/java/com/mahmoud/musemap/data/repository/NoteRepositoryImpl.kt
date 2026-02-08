package com.mahmoud.musemap.data.repository

import com.mahmoud.musemap.data.local.dao.NoteDao
import com.mahmoud.musemap.data.local.entity.toDomain
import com.mahmoud.musemap.data.local.entity.toEntity
import com.mahmoud.musemap.domain.model.Note
import com.mahmoud.musemap.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override suspend fun insertNote(note: Note) {
        val entity = note.toEntity()
        dao.insertNote(entity)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toEntity())
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return dao.getNoteById(id)?.toDomain()
    }
}