package com.virasatnamma.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.virasatnamma.ui.theme.VirasatColors

/**
 * Premium Heritage Image Component
 * Supports Ken Burns effect (slow zoom), parallax (external control), and automatic fallbacks.
 */
@Composable
fun HeritageImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    parallaxFactor: Float = 0f,
    showKenBurns: Boolean = true,
    overlayGradient: Boolean = true,
    clipShape: RoundedCornerShape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
) {
    // Ken Burns Animation
    val infiniteTransition = rememberInfiniteTransition(label = "kenBurns")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (showKenBurns) 1.15f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Fallback Image List (Mock heritage images to avoid blank spaces)
    val fallbacks = listOf(
        "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Stone_Chariot_of_Hampi.jpg/1200px-Stone_Chariot_of_Hampi.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Hampi_peaceful_landscape.jpg/1200px-Hampi_peaceful_landscape.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3b/Vijayanagara_ruins_Hampi.jpg/1200px-Vijayanagara_ruins_Hampi.jpg"
    )
    
    val displayModel = if (model == null || (model is String && model.isEmpty())) {
        fallbacks.random()
    } else {
        model
    }

    Box(modifier = modifier.clip(clipShape)) {
        AsyncImage(
            model = displayModel,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationY = parallaxFactor * 100f
                },
            contentScale = ContentScale.Crop
        )

        if (overlayGradient) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            ),
                            startY = 200f
                        )
                    )
            )
        }
    }
}
