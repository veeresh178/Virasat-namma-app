package com.virasatnamma.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.virasatnamma.ui.components.PrimaryButton
import com.virasatnamma.ui.theme.VirasatColors
import com.virasatnamma.viewmodel.ScannerViewModel
import com.virasatnamma.viewmodel.ScannerUiState

/**
 * Premium Heritage QR Scanner Screen
 * Designed to look like a mystical scanner from an ancient era
 */
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel,
    onSiteFound: (String) -> Unit,
    onScanReset: () -> Unit
) {
    val scanState = viewModel.scanState.collectAsState().value
    val scannedSiteId = viewModel.scannedSiteId.collectAsState().value
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VirasatColors.SoftCream)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                AsyncImage(
                    model = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Stone_Chariot_of_Hampi.jpg/1200px-Stone_Chariot_of_Hampi.jpg",
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
                Text(
                    text = "Mystic Gateway",
                    style = MaterialTheme.typography.displaySmall,
                    color = VirasatColors.AntiqueGold,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
            }
            
            // SCANNER AREA
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                when (scanState) {
                    is ScannerUiState.Idle -> {
                        ScannerIdleContent(onScanSimulate = { viewModel.processScanResult("SITE:site_001") })
                    }
                    
                    is ScannerUiState.ScanSuccessful -> {
                        ScannerSuccessContent(
                            siteName = scanState.siteName,
                            onViewDetails = { scannedSiteId?.let { onSiteFound(it) } },
                            onReset = {
                                viewModel.resetScan()
                                onScanReset()
                            }
                        )
                    }
                    
                    is ScannerUiState.Error -> {
                        ScannerErrorContent(
                            message = scanState.message,
                            onRetry = { viewModel.resetScan() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScannerIdleContent(onScanSimulate: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .border(
                    width = 4.dp,
                    brush = Brush.sweepGradient(
                        listOf(VirasatColors.AntiqueGold, VirasatColors.DeepSaffron, VirasatColors.AntiqueGold)
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = null,
                tint = VirasatColors.DeepSaffron,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Text(
            text = "SCAN SACRED CODE",
            style = MaterialTheme.typography.titleLarge,
            color = VirasatColors.TempleBrown,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Point your device at the QR stone placed at the heritage site entrance to unlock its history.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = VirasatColors.StoneGray,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Spacer(Modifier.height(16.dp))
        
        PrimaryButton(
            text = "INITIATE SCAN",
            onClick = onScanSimulate
        )
    }
}

@Composable
fun ScannerSuccessContent(siteName: String, onViewDetails: () -> Unit, onReset: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Surface(
            color = Color.Green.copy(alpha = 0.1f),
            shape = CircleShape,
            modifier = Modifier.size(120.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color.Green.copy(alpha = 0.5f))
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = null,
                tint = Color.Green,
                modifier = Modifier.padding(32.dp)
            )
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "PATH UNLOCKED",
                style = MaterialTheme.typography.titleLarge,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = siteName,
                style = MaterialTheme.typography.displaySmall,
                color = VirasatColors.TempleBrown,
                textAlign = TextAlign.Center
            )
        }
        
        PrimaryButton(
            text = "REVEAL KNOWLEDGE",
            onClick = onViewDetails
        )
        
        TextButton(onClick = onReset) {
            Text("SCAN ANOTHER STONE", color = VirasatColors.DeepSaffron, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun ScannerErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = VirasatColors.Error,
            modifier = Modifier.size(80.dp)
        )
        
        Text(
            text = "GATEWAY BLOCKED",
            style = MaterialTheme.typography.titleLarge,
            color = VirasatColors.Error,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = VirasatColors.StoneGray
        )
        
        PrimaryButton(
            text = "TRY AGAIN",
            onClick = onRetry
        )
    }
}
