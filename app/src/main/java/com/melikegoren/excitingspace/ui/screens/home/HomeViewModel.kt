package com.melikegoren.excitingspace.ui.screens.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import com.melikegoren.excitingspace.common.Result
import com.melikegoren.excitingspace.common.asResult
import com.melikegoren.excitingspace.data.repository.ApodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apodRepository: ApodRepository,
): ViewModel() {


    private var _uiState = MutableStateFlow<ApodUiState>(ApodUiState.Loading)
    val uiState: StateFlow<ApodUiState> = _uiState.asStateFlow()


    fun apodUiStatePhoto(apiKey: String){
        viewModelScope.launch {
            apodRepository.getApodWithPhoto(apiKey)
                .asResult()
                .collectLatest {
                    when(it){
                        is Result.Loading -> {
                            _uiState.value = ApodUiState.Loading
                            Log.d("apodLoading","loadingggapo")

                        }

                        is Result.Error -> {
                            _uiState.value = ApodUiState.Error(it.exception.message.toString())
                        }

                        is Result.Success -> {
                            _uiState.value = ApodUiState.Success(it.data.toHomeScreenUiModelPhoto() ?: HomeScreenUiModelPhoto("","","") )
                            //Log.d("apodTitle", it.data.explanation.toString())
                        }
                    }
                }
        }
    }

    fun apodUiStateVideo(apiKey: String){
        viewModelScope.launch {
            apodRepository.getApodWithVideo(apiKey)
                .asResult()
                .collectLatest {
                    when(it){
                        is Result.Loading -> {
                            _uiState.value = ApodUiState.Loading
                            Log.d("apodLoading","loadingggapo")

                        }

                        is Result.Error -> {
                            _uiState.value = ApodUiState.Error(it.exception.message.toString())
                        }

                        is Result.Success -> {
                            _uiState.value = ApodUiState.Success(it.data.toHomeScreenUiModelVideo())
                            Log.d("apodTitle", it.data.explanation.toString())
                        }
                    }
                }
        }
    }

    fun saveApodImageToGallery(painter: AsyncImagePainter, context: Context, url: String, title: String){
        viewModelScope.launch {
            apodRepository.saveImageToGallery(painter, context, url, title)
        }
    }








}

sealed interface ApodUiState {
    object Loading : ApodUiState
    data class Success(val data: HomeScreenUiModelPhoto) : ApodUiState
    data class Error (val message: String) : ApodUiState
}




