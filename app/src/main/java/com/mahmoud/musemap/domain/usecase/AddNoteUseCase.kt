package com.mahmoud.musemap.domain.usecase

import com.mahmoud.musemap.domain.model.Note
import com.mahmoud.musemap.domain.repository.NoteRepository
import javax.inject.Inject // <--- Add this import


class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw Exception("The title of the note cannot be empty!")
        }
        repository.insertNote(note)
    }
}