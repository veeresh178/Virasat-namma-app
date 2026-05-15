package com.virasatnamma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virasatnamma.data.repository.HeritageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for QR Scanner Screen
 * Handles QR code scanning and navigation to site details
 */
class ScannerViewModel(
    private val repository: HeritageRepository
) : ViewModel() {
    
    private val _scanResult = MutableStateFlow<String>("")
    val scanResult: StateFlow<String> = _scanResult.asStateFlow()
    
    private val _scannedSiteId = MutableStateFlow<String?>(null)
    val scannedSiteId: StateFlow<String?> = _scannedSiteId.asStateFlow()
    
    private val _scanState = MutableStateFlow<ScannerUiState>(ScannerUiState.Idle)
    val scanState: StateFlow<ScannerUiState> = _scanState.asStateFlow()
    
    fun processScanResult(rawResult: String) {
        viewModelScope.launch {
            try {
                // Extract siteId from QR code (format: "SITE:site_001")
                val siteId = if (rawResult.startsWith("SITE:")) {
                    rawResult.removePrefix("SITE:")
                } else {
                    rawResult
                }
                
                // Verify site exists
                val site = repository.getSiteById(siteId)
                if (site != null) {
                    _scannedSiteId.value = siteId
                    _scanResult.value = rawResult
                    _scanState.value = ScannerUiState.ScanSuccessful(site.name)
                } else {
                    _scanState.value = ScannerUiState.Error("Site not found: $siteId")
                }
            } catch (e: Exception) {
                _scanState.value = ScannerUiState.Error("Scan error: ${e.message}")
            }
        }
    }
    
    fun resetScan() {
        _scanResult.value = ""
        _scannedSiteId.value = null
        _scanState.value = ScannerUiState.Idle
    }
}

sealed class ScannerUiState {
    object Idle : ScannerUiState()
    data class ScanSuccessful(val siteName: String) : ScannerUiState()
    data class Error(val message: String) : ScannerUiState()
}
