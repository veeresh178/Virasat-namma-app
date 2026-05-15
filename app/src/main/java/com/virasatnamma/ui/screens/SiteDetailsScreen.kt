package com.virasatnamma.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.virasatnamma.ui.components.AudioPlayButton
import com.virasatnamma.ui.components.CheckInBadge
import com.virasatnamma.ui.components.HeritageImage
import com.virasatnamma.ui.components.LanguageToggleChip
import com.virasatnamma.ui.theme.VirasatColors
import com.virasatnamma.viewmodel.SiteDetailsViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteDetailsScreen(
    siteId: String,
    viewModel: SiteDetailsViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Initialize TTS
    DisposableEffect(context) {
        var ttsInstance: TextToSpeech? = null
        ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance?.setLanguage(Locale("en", "IN"))
                isTtsReady = true
            }
        }
        tts = ttsInstance
        onDispose {
            ttsInstance?.stop()
            ttsInstance?.shutdown()
        }
    }

    LaunchedEffect(siteId) {
        viewModel.loadSiteDetails(siteId)
    }
    
    val siteDetails = viewModel.siteDetails.collectAsState().value
    val isVisited = viewModel.isVisited.collectAsState().value
    val isAudioPlaying = viewModel.isAudioPlaying.collectAsState().value
    val language = viewModel.language.collectAsState().value

    // Lottie Animation for the name section
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url("https://assets10.lottiefiles.com/packages/lf20_m6cuL6.json")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(50))
                    ) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = VirasatColors.SoftCream
    ) { padding ->
        val topPadding = padding.calculateTopPadding()
        if (siteDetails != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Main Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    // HERO IMAGE SECTION with Parallax and Ken Burns (HeritageImage handles zoom)
                    HeritageImage(
                        model = siteDetails.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp),
                        parallaxFactor = scrollState.value * 0.001f,
                        showKenBurns = true,
                        overlayGradient = true,
                        clipShape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )

                    // CONTENT SECTION
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(VirasatColors.SoftCream)
                            .padding(horizontal = 20.dp)
                            .offset(y = (-40).dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Title Section with Animation
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier.size(80.dp)
                            )
                            
                            Text(
                                text = siteDetails.name,
                                style = MaterialTheme.typography.displaySmall,
                                textAlign = TextAlign.Center,
                                color = VirasatColors.TempleBrown
                            )
                            
                            Spacer(Modifier.height(8.dp))
                            
                            // Decorative divider
                            DecorativeDivider()
                            
                            Spacer(Modifier.height(8.dp))

                            Surface(
                                color = VirasatColors.SaffronOrange.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, VirasatColors.SaffronOrange.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "${siteDetails.category} • EST. ${siteDetails.yearEstablished}",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                    color = VirasatColors.SaffronOrange,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Language Toggle
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            LanguageToggleChip(
                                currentLanguage = language,
                                onLanguageChange = { viewModel.toggleLanguage() }
                            )
                        }

                        // Parchment Style Info Card
                        ParchmentCard {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, null, tint = VirasatColors.DarkMaroon, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "HISTORY & LEGEND",
                                        style = MaterialTheme.typography.titleMedium,
                                        letterSpacing = 2.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Text(
                                    text = if (language == "EN") siteDetails.descriptionEn else siteDetails.descriptionKn,
                                    style = MaterialTheme.typography.bodyLarge,
                                    lineHeight = 26.sp
                                )
                            }
                        }

                        // Hidden Facts with Expandable Animation
                        if (siteDetails.hiddenFacts.isNotEmpty()) {
                            Text(
                                text = "✨ SACRED SECRETS",
                                style = MaterialTheme.typography.titleMedium,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            
                            siteDetails.hiddenFacts.forEach { fact ->
                                StoneFactCard(fact)
                            }
                        }

                        // Audio Guide with Glowing Effect
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AudioPlayButton(
                                isPlaying = isAudioPlaying,
                                onToggle = { viewModel.toggleAudioPlayback() }
                            )
                        }

                        // Check-in Badge
                        CheckInBadge(
                            isVisited = isVisited,
                            onCheckIn = { viewModel.performCheckIn() }
                        )

                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DecorativeDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(0.6f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(Modifier.height(1.dp).weight(1f).background(VirasatColors.AntiqueGold))
        Text(" ⚜ ", color = VirasatColors.AntiqueGold, fontSize = 18.sp)
        Box(Modifier.height(1.dp).weight(1f).background(VirasatColors.AntiqueGold))
    }
}

@Composable
fun ParchmentCard(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = VirasatColors.Parchment,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, VirasatColors.AntiqueGold.copy(alpha = 0.3f))
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

@Composable
fun StoneFactCard(fact: String) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = VirasatColors.Sandstone.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, VirasatColors.Sandstone.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = fact,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) 100 else 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
