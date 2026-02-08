package com.mahmoud.musemap.presentation.add_note

import android.content.Context
import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoud.musemap.data.repository.AudioPlayer
import com.mahmoud.musemap.data.repository.AudioRecorder
import com.mahmoud.musemap.data.repository.LocationService
import com.mahmoud.musemap.data.repository.StorageService
import com.mahmoud.musemap.domain.model.Note
import com.mahmoud.musemap.domain.usecase.AddNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val repository: com.mahmoud.musemap.domain.repository.NoteRepository,
    private val storageService: StorageService,
    private val savedStateHandle: SavedStateHandle,
    private val locationService: LocationService,
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _noteTitle = mutableStateOf("")
    val noteTitle: State<String> = _noteTitle

    private val _noteContent = mutableStateOf("")
    val noteContent: State<String> = _noteContent

    private val _videoUri = mutableStateOf<android.net.Uri?>(null)
    val videoUri: State<android.net.Uri?> = _videoUri

    private val _isRecording = mutableStateOf(false)
    val isRecording: State<Boolean> = _isRecording

    private val _audioPath = mutableStateOf<String?>(null)
    val audioPath: State<String?> = _audioPath

    private val _currentLocation = mutableStateOf<Location?>(null)
    val currentLocation: State<Location?> = _currentLocation

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var audioFile: File? = null

    init {
        val noteId = savedStateHandle.get<Long>("noteId") ?: -1L
        if (noteId != -1L) {
            viewModelScope.launch {
                repository.getNoteById(noteId)?.let { note ->
                    _noteTitle.value = note.title
                    _noteContent.value = note.content
                    _audioPath.value = note.audioPath

                    if (note.latitude != null && note.longitude != null) {
                        val loc = Location("dummy provider")
                        loc.latitude = note.latitude
                        loc.longitude = note.longitude
                        _currentLocation.value = loc
                    }

                    note.videoPath?.let { path ->
                        _videoUri.value = android.net.Uri.fromFile(File(path))
                    }
                }
            }
        }
    }


    fun onTitleChange(text: String) {
        _noteTitle.value = text
    }

    fun onContentChange(text: String) {
        _noteContent.value = text
    }

    fun onVideoSelected(uri: android.net.Uri) {
        _videoUri.value = uri
    }

    fun fetchLocation() {
        viewModelScope.launch {
            val location = locationService.getCurrentLocation()
            _currentLocation.value = location
        }
    }

    fun toggleRecording(context: Context) {
        if (_isRecording.value) {
            audioRecorder.stop()
            _isRecording.value = false
            _audioPath.value = audioFile?.absolutePath
        } else {
            val file = File(context.cacheDir, "audio_${System.currentTimeMillis()}.mp3")
            audioRecorder.start(file)
            audioFile = file
            _isRecording.value = true
        }
    }

    fun playAudio() {
        _audioPath.value?.let { path ->
            audioPlayer.playFile(File(path))
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            try {
                var finalPath = _videoUri.value?.path
                if (_videoUri.value.toString().startsWith("content://")) {
                    finalPath =
                        _videoUri.value?.let { storageService.saveVideoToInternalStorage(it) }
                }

                val lat = _currentLocation.value?.latitude
                val lng = _currentLocation.value?.longitude

                val currentId = savedStateHandle.get<Long>("noteId") ?: -1L
                val idToSave = if (currentId == -1L) 0 else currentId

                addNoteUseCase(
                    Note(
                        id = idToSave,
                        title = noteTitle.value,
                        content = noteContent.value,
                        timestamp = System.currentTimeMillis(),
                        audioPath = _audioPath.value,
                        videoPath = finalPath,
                        latitude = lat,
                        longitude = lng
                    )
                )
                _eventFlow.emit(UiEvent.SaveNote)
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar(e.message ?: "Error"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}