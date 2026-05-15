package com.virasatnamma.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.virasatnamma.ui.components.HeritageBackground
import com.virasatnamma.ui.theme.VirasatColors
import com.virasatnamma.viewmodel.PassportViewModel
import com.virasatnamma.viewmodel.PassportUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Premium Digital Passport Screen
 */
@Composable
fun PassportScreen(
    viewModel: PassportViewModel,
    onSiteClick: (String, String) -> Unit
) {
    val passportData = viewModel.passportData.collectAsState().value
    val visits = viewModel.visits.collectAsState().value
    val uiState = viewModel.uiState.collectAsState().value
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VirasatColors.SoftCream)
    ) {
        // Subtle Mandala Background Pattern
        HeritageBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            // ROYAL HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                AsyncImage(
                    model = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3b/Vijayanagara_ruins_Hampi.jpg/1200px-Vijayanagara_ruins_Hampi.jpg",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent, VirasatColors.SoftCream),
                                startY = 0f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HistoryEdu,
                            contentDescription = null,
                            tint = VirasatColors.AntiqueGold,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Heritage Passport",
                            style = MaterialTheme.typography.displaySmall,
                            color = VirasatColors.AntiqueGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Your journey through Karnataka's soul",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(start = 48.dp)
                    )
                }
            }
            
            // CONTENT
            when (uiState) {
                is PassportUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = VirasatColors.DeepSaffron)
                    }
                }
                
                is PassportUiState.Success -> {
                    if (passportData != null) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            item {
                                StatsScrollCard(passportData.visitedSites, passportData.totalSites, passportData.visitPercentage)
                            }
                            
                            if (visits.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "📜 PILGRIMAGE LOG",
                                        style = MaterialTheme.typography.titleMedium,
                                        letterSpacing = 2.sp,
                                        color = VirasatColors.DarkMaroon,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                
                                items(visits) { visit ->
                                    PassportVisitCard(visit) { onSiteClick(visit.siteId, visit.siteName) }
                                }
                            } else {
                                item {
                                    EmptyPassportState()
                                }
                            }
                            
                            item { Spacer(Modifier.height(100.dp)) }
                        }
                    }
                }
                
                is PassportUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${uiState.message}", color = VirasatColors.Error)
                    }
                }
            }
        }
    }
}

@Composable
fun StatsScrollCard(visited: Int, total: Int, percentage: Float) {
    Surface(
        color = VirasatColors.Parchment,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, VirasatColors.AntiqueGold.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("SITES VISITED", visited.toString(), VirasatColors.DeepSaffron)
                Box(Modifier.width(1.dp).height(40.dp).background(VirasatColors.AntiqueGold.copy(alpha = 0.3f)))
                StatItem("TOTAL SITES", total.toString(), VirasatColors.TempleBrown)
            }
            
            Spacer(Modifier.height(20.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Heritage Completion", style = MaterialTheme.typography.labelMedium)
                    Text("%.0f%%".format(percentage), color = VirasatColors.DeepSaffron, fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = percentage / 100f,
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                    color = VirasatColors.DeepSaffron,
                    trackColor = VirasatColors.AntiqueGold.copy(alpha = 0.1f)
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.displaySmall, color = color, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = VirasatColors.StoneGray)
    }
}

@Composable
fun PassportVisitCard(visit: com.virasatnamma.data.local.VisitRecord, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = VirasatColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = visit.siteImageUrl,
                contentDescription = null,
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(text = visit.siteName, style = MaterialTheme.typography.titleMedium, color = VirasatColors.TempleBrown)
                Text(
                    text = "Visited: ${formatDate(visit.timestamp)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = VirasatColors.StoneGray
                )
            }
            
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = VirasatColors.AntiqueGold,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun EmptyPassportState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(Icons.Default.EmojiEvents, null, Modifier.size(80.dp), tint = VirasatColors.AntiqueGold.copy(alpha = 0.3f))
        Text(
            "The scroll is empty. Begin your pilgrimage to fill it with glory.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = VirasatColors.StoneGray
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(date)
}
