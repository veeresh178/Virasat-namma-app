package com.virasatnamma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virasatnamma.data.local.DigitalPassport
import com.virasatnamma.data.local.VisitRecord
import com.virasatnamma.data.repository.HeritageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Digital Passport Screen
 * Displays visited sites and check-in history
 */
class PassportViewModel(
    private val repository: HeritageRepository
) : ViewModel() {
    
    private val _passportData = MutableStateFlow<DigitalPassport?>(null)
    val passportData: StateFlow<DigitalPassport?> = _passportData.asStateFlow()
    
    private val _visits = MutableStateFlow<List<VisitRecord>>(emptyList())
    val visits: StateFlow<List<VisitRecord>> = _visits.asStateFlow()
    
    private val _uiState = MutableStateFlow<PassportUiState>(PassportUiState.Loading)
    val uiState: StateFlow<PassportUiState> = _uiState.asStateFlow()
    
    init {
        loadPassportData()
    }
    
    private fun loadPassportData() {
        viewModelScope.launch {
            try {
                repository.getAllCheckIns().collect { visitRecords ->
                    val sortedVisits = visitRecords.sortedByDescending { it.timestamp }
                    _visits.value = sortedVisits

                    val totalSites = repository.getSiteCount()
                    val visitedSiteCount = sortedVisits.map { it.siteId }.distinct().count()

                    _passportData.value = DigitalPassport(
                        totalSites = totalSites,
                        visitedSites = visitedSiteCount,
                        visitPercentage = if (totalSites > 0) (visitedSiteCount * 100f) / totalSites else 0f,
                        visits = sortedVisits
                    )
                    _uiState.value = PassportUiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = PassportUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun refresh() {
        loadPassportData()
    }
}

sealed class PassportUiState {
    object Loading : PassportUiState()
    object Success : PassportUiState()
    data class Error(val message: String) : PassportUiState()
}
