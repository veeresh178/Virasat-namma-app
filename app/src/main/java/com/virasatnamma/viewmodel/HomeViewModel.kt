package com.virasatnamma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virasatnamma.data.local.HeritageLocation
import com.virasatnamma.data.local.LocationCardState
import com.virasatnamma.data.repository.HeritageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * ViewModel for Home Screen - Heritage Site Discovery
 * Manages nearby sites, location filtering, and user interactions
 */
class HomeViewModel(
    private val repository: HeritageRepository
) : ViewModel() {
    
    // Mock user location (Hampi, Karnataka)
    private val userLatitude = 15.3352
    private val userLongitude = 76.4745
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    private val _nearestSites = MutableStateFlow<List<LocationCardState>>(emptyList())
    val nearestSites: StateFlow<List<LocationCardState>> = _nearestSites.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()
    
    init {
        loadHeritagesSites()
    }
    
    private fun loadHeritagesSites() {
        viewModelScope.launch {
            try {
                // Initialize with sample data if empty
                repository.initializeSampleData()
                
                // Collect all sites and calculate distances
                repository.getAllSites().collect { sites ->
                    val visitedIds = repository.getVisitedSiteIds()
                    val cardStates = sites.map { site ->
                        LocationCardState(
                            location = site.copy(isVisited = site.id in visitedIds),
                            distance = calculateDistance(
                                userLatitude, userLongitude,
                                site.latitude, site.longitude
                            )
                        )
                    }.sortedBy { it.distance }
                    
                    _nearestSites.value = cardStates
                    _uiState.value = HomeUiState.Success(cardStates)
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun filterByCategory(category: String) {
        viewModelScope.launch {
            _selectedCategory.value = category
            if (category == "All") {
                loadHeritagesSites()
            } else {
                try {
                    repository.getSitesByCategory(category).collect { sites ->
                        val visitedIds = repository.getVisitedSiteIds()
                        val cardStates = sites.map { site ->
                            LocationCardState(
                                location = site.copy(isVisited = site.id in visitedIds),
                                distance = calculateDistance(
                                    userLatitude, userLongitude,
                                    site.latitude, site.longitude
                                )
                            )
                        }
                        _nearestSites.value = cardStates
                        _uiState.value = HomeUiState.Success(cardStates)
                    }
                } catch (e: Exception) {
                    _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
    
    /**
     * Calculate distance between two coordinates using Haversine formula
     * Returns distance in kilometers
     */
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371.0
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return earthRadiusKm * c
    }
    
    fun refresh() {
        loadHeritagesSites()
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val sites: List<LocationCardState>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
