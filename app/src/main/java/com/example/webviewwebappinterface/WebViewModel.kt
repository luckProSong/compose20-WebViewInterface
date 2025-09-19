package com.example.webviewwebappinterface

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WebViewModel: ViewModel() {
    data class WebUiState(
        val toastMessage: String? = null,
        val popupMessage: String? = null,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(WebUiState())
    val uiState: StateFlow<WebUiState> = _uiState

    fun onWebToast(message: String){
        _uiState.update { it.copy(toastMessage = message) }
    }

    fun onWebPopup(message: String){
        _uiState.update { it.copy(popupMessage = message) }
    }

    fun onError(message: String){
        _uiState.update { it.copy(errorMessage = message) }
    }

    fun clearError(){
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearToast(){
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun clearPopup(){
        _uiState.update { it.copy(popupMessage = null) }
    }
}