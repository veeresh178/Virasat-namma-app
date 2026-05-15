package com.virasatnamma.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virasatnamma.ui.theme.VirasatColors

/**
 * Premium Heritage Action Button with Gold Gradient and Temple Borders
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (enabled) Brush.horizontalGradient(
                        listOf(VirasatColors.DeepBrass, VirasatColors.DarkMaroon)
                    ) else Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        listOf(VirasatColors.AntiqueGold, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }
    }
}

/**
 * Audio Playback Button with Ambient Glow and Traditional Border
 */
@Composable
fun AudioPlayButton(
    isPlaying: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(onClick = onToggle),
        color = VirasatColors.TempleBrown,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp, 
            VirasatColors.AntiqueGold.copy(alpha = if (isPlaying) glowAlpha else 0.4f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isPlaying) Brush.radialGradient(
                        colors = listOf(VirasatColors.LampGold.copy(alpha = 0.2f), Color.Transparent),
                        radius = 400f
                    ) else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = VirasatColors.LampGold,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = if (isPlaying) "PAUSE GUIDED TOUR" else "START GUIDED TOUR",
                    style = MaterialTheme.typography.titleMedium,
                    color = VirasatColors.AntiqueGold,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Language Toggle Chip - Heritage Style
 */
@Composable
fun LanguageToggleChip(
    currentLanguage: String,
    onLanguageChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onLanguageChange,
        modifier = modifier.height(44.dp),
        color = VirasatColors.Parchment,
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VirasatColors.AntiqueGold.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "EN",
                color = if (currentLanguage == "EN") VirasatColors.SaffronOrange else VirasatColors.StoneGray,
                fontWeight = if (currentLanguage == "EN") FontWeight.Bold else FontWeight.Normal
            )
            Box(Modifier.width(1.dp).height(16.dp).background(VirasatColors.AntiqueGold.copy(alpha = 0.3f)))
            Text(
                text = "KN",
                color = if (currentLanguage == "KN") VirasatColors.SaffronOrange else VirasatColors.StoneGray,
                fontWeight = if (currentLanguage == "KN") FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

/**
 * Check-in Badge Button - Royal Seal Style
 */
@Composable
fun CheckInBadge(
    isVisited: Boolean,
    onCheckIn: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    PrimaryButton(
        text = if (isVisited) "✓ VISITED" else "MARK AS VISITED",
        onClick = { onCheckIn?.invoke() },
        modifier = modifier,
        enabled = !isVisited
    )
}
