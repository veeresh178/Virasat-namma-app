package com.virasatnamma.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.virasatnamma.ui.components.HeritageBackground
import com.virasatnamma.ui.components.HeritageLocationCard
import com.virasatnamma.ui.components.ShimmerLocationCard
import com.virasatnamma.ui.theme.VirasatColors
import com.virasatnamma.viewmodel.HomeUiState
import com.virasatnamma.viewmodel.HomeViewModel

/**
 * Premium Heritage Home Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSiteClick: (String) -> Unit,
    onScannerClick: () -> Unit,
    onPassportClick: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    val nearestSites = viewModel.nearestSites.collectAsState().value
    val selectedCategory = viewModel.selectedCategory.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VirasatColors.SoftCream)
    ) {
        // Subtle Mandala Background Pattern
        HeritageBackground()

        // Main Scrollable Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp) // Space for floating nav
        ) {
            // Animated Header Section
            item {
                HeritageHeader(selectedCategory, onCategorySelected = { viewModel.filterByCategory(it) })
            }

            // Site List
            when (uiState) {
                is HomeUiState.Loading -> {
                    items(3) {
                        ShimmerLocationCard()
                    }
                }
                is HomeUiState.Success -> {
                    items(nearestSites) { site ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInVertically { it / 2 }
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                HeritageLocationCard(
                                    locationState = site,
                                    onCardClick = { onSiteClick(site.location.id) }
                                )
                            }
                        }
                    }
                }
                is HomeUiState.Error -> {
                    item {
                        Text(
                            text = uiState.message,
                            modifier = Modifier.padding(32.dp),
                            color = VirasatColors.Error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        // Floating Search Bar at Top
        FloatingSearchBar(modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun HeritageHeader(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
    ) {
        // Banner Image
        AsyncImage(
            model = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Hampi_peaceful_landscape.jpg/1200px-Hampi_peaceful_landscape.jpg",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Darkened Overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, VirasatColors.SoftCream),
                        startY = 0f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Welcome to",
                style = MaterialTheme.typography.titleMedium,
                color = VirasatColors.SoftCream.copy(alpha = 0.8f)
            )
            Text(
                text = "Virasat-Namma",
                style = MaterialTheme.typography.displayMedium,
                color = VirasatColors.AntiqueGold,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Animated Category Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Temple", "Monument", "Palace", "Historical Market").forEach { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        modifier = Modifier.clickable { onCategorySelected(category) },
                        color = if (isSelected) VirasatColors.DeepSaffron else VirasatColors.Parchment.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = if (isSelected) 8.dp else 2.dp,
                        border = if (isSelected) null else BorderStroke(1.dp, VirasatColors.AntiqueGold.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = if (isSelected) Color.White else VirasatColors.TempleBrown,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingSearchBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(56.dp),
        color = VirasatColors.White.copy(alpha = 0.9f),
        shape = CircleShape,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = VirasatColors.TempleBrown)
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Search heritage, temples...",
                style = MaterialTheme.typography.bodyMedium,
                color = VirasatColors.StoneGray,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {}) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = VirasatColors.DeepSaffron)
            }
        }
    }
}
