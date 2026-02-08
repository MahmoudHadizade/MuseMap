package com.mahmoud.musemap.domain.usecase

import com.mahmoud.musemap.domain.model.Note
import com.mahmoud.musemap.domain.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class AddNoteUseCaseTest {

    private val repository: NoteRepository = mockk()

    private lateinit var addNoteUseCase: AddNoteUseCase

    @Before
    fun setUp() {
        addNoteUseCase = AddNoteUseCase(repository)
    }

    @Test
    fun `invoke throws exception when title is empty`() = runBlocking {
        val note = Note(
            title = "",
            content = "Some content",
            timestamp = 123L
        )

        assertThrows(Exception::class.java) {
            runBlocking {
                addNoteUseCase(note)
            }
        }

        coVerify(exactly = 0) { repository.insertNote(any()) }
    }

    @Test
    fun `invoke calls repository when title is valid`() = runBlocking {
        val note = Note(
            title = "Valid Title",
            content = "Valid Content",
            timestamp = 123L
        )

        coEvery { repository.insertNote(any()) } returns Unit

        addNoteUseCase(note)

        coVerify(exactly = 1) { repository.insertNote(note) }
    }
}