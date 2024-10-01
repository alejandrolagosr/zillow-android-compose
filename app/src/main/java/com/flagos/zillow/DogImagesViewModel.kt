package com.flagos.zillow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flagos.zillow.data.DogImagesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DogImagesViewModel(private val dogImagesRepository: DogImagesRepository): ViewModel() {

    private val _state = MutableStateFlow(DogImagesState())
    val state: StateFlow<DogImagesState> = _state.asStateFlow()

    fun processIntent(intent: DogImagesIntent) {
        when(intent) {
            is DogImagesIntent.GetImages -> loadImages()
        }
    }

    private fun loadImages() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val images = dogImagesRepository.getImages()
                _state.value = DogImagesState(images = images)
            } catch (exception: Exception) {
                _state.value = DogImagesState(error = exception.message)
            }
        }
    }

    sealed class DogImagesIntent {
        data object GetImages: DogImagesIntent()
    }

    data class DogImagesState(
        val isLoading: Boolean = false,
        val images: List<String> = emptyList(),
        val error: String? = null
    )
}