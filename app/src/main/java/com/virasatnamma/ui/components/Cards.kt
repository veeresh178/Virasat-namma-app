package com.virasatnamma.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.virasatnamma.data.local.LocationCardState
import com.virasatnamma.ui.theme.VirasatColors

/**
 * Premium Heritage Location Card
 * Designed to look like a carved stone tablet with layered depth.
 */
@Composable
fun HeritageLocationCard(
    locationState: LocationCardState,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url("https://assets10.lottiefiles.com/packages/lf20_m6cuL6.json")
    )

    // Subtle floating animation
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val dy by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dy"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { translationY = dy }
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = VirasatColors.Parchment
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(VirasatColors.AntiqueGold.copy(alpha = 0.5f), Color.Transparent)
                ),
                shape = RoundedCornerShape(20.dp)
            )
        ) {
            // Using HeritageImage for animated content and fallbacks
            HeritageImage(
                model = locationState.location.imageUrl,
                contentDescription = locationState.location.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                clipShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                showKenBurns = true
            )
            
            // Text Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (locationState.location.category == "Temple") {
                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(
                            text = locationState.location.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = VirasatColors.TempleBrown,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    if (locationState.location.isVisited) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Visited",
                            tint = VirasatColors.AntiqueGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Text(
                    text = locationState.location.shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = VirasatColors.StoneGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = VirasatColors.AntiqueGold.copy(alpha = 0.2f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Distance",
                            tint = VirasatColors.DeepSaffron,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "%.1f KM".format(locationState.distance),
                            style = MaterialTheme.typography.labelLarge,
                            color = VirasatColors.DeepSaffron,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    
                    Text(
                        text = locationState.location.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = VirasatColors.TempleBrown,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

/**
 * Small Heritage Preview Card
 */
@Composable
fun HeritagePreviewCard(
    name: String,
    description: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = VirasatColors.Parchment),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            HeritageImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                clipShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                overlayGradient = false
            )
            
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    color = VirasatColors.TempleBrown,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = VirasatColors.StoneGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
