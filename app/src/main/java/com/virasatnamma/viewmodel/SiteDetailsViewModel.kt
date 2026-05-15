package com.virasatnamma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virasatnamma.data.local.HeritageLocation
import com.virasatnamma.data.repository.HeritageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SiteDetailsViewModel(
    private val repository: HeritageRepository
) : ViewModel() {
    
    private val _siteDetails = MutableStateFlow<HeritageLocation?>(null)
    val siteDetails: StateFlow<HeritageLocation?> = _siteDetails.asStateFlow()
    
    private val _isVisited = MutableStateFlow(false)
    val isVisited: StateFlow<Boolean> = _isVisited.asStateFlow()
    
    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()
    
    private val _language = MutableStateFlow("EN")
    val language: StateFlow<String> = _language.asStateFlow()
    
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()
    
    fun loadSiteDetails(siteId: String) {
        viewModelScope.launch {
            try {
                val site = repository.getSiteById(siteId)
                if (site != null) {
                    _siteDetails.value = site
                    _isVisited.value = repository.isVisited(siteId)
                    _uiState.value = DetailsUiState.Success
                } else {
                    _uiState.value = DetailsUiState.Error("Site not found")
                }
            } catch (e: Exception) {
                _uiState.value = DetailsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun toggleLanguage() {
        _language.value = if (_language.value == "EN") "KN" else "EN"
        // Stop audio when switching language to prevent mixed speech
        _isAudioPlaying.value = false
    }
    
    fun toggleAudioPlayback() {
        _isAudioPlaying.value = !_isAudioPlaying.value
    }
    
    fun performCheckIn() {
        viewModelScope.launch {
            try {
                val site = _siteDetails.value ?: return@launch
                repository.addCheckIn(site.id, site.name, site.imageUrl)
                _isVisited.value = true
                _uiState.value = DetailsUiState.CheckInSuccess
            } catch (e: Exception) {
                _uiState.value = DetailsUiState.Error("Check-in failed: ${e.message}")
            }
        }
    }
}

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    object Success : DetailsUiState()
    object CheckInSuccess : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}
