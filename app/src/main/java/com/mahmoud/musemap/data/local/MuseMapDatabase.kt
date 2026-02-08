package com.mahmoud.musemap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mahmoud.musemap.data.local.dao.NoteDao
import com.mahmoud.musemap.data.local.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false

)
abstract class MuseMapDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}