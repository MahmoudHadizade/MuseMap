package com.mahmoud.musemap.data.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class StorageService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun saveVideoToInternalStorage(uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val fileName = "VIDEO_${System.currentTimeMillis()}.mp4"

                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

                val file = File(context.filesDir, fileName)
                val outputStream = FileOutputStream(file)

                inputStream?.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}