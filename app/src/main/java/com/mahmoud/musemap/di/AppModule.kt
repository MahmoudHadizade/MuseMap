package com.mahmoud.musemap.di

import android.app.Application
import androidx.room.Room
import com.mahmoud.musemap.data.local.MuseMapDatabase
import com.mahmoud.musemap.data.local.dao.NoteDao
import com.mahmoud.musemap.data.repository.NoteRepositoryImpl
import com.mahmoud.musemap.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): MuseMapDatabase {
        return Room.databaseBuilder(
            app,
            MuseMapDatabase::class.java,
            "musemap_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: MuseMapDatabase): NoteDao {
        return db.noteDao()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(dao)
    }
}