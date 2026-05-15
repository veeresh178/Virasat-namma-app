package com.virasatnamma.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.virasatnamma.ui.theme.VirasatColors
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Procedural Heritage Background with Mandala patterns and Stone Texture
 */
@Composable
fun HeritageBackground(
    modifier: Modifier = Modifier,
    patternColor: Color = VirasatColors.AntiqueGold.copy(alpha = 0.05f)
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        
        // 1. Draw Subtle Stone Texture (Noise)
        val random = Random(42) // Consistent seed
        for (i in 0..1000) {
            val x = random.nextFloat() * canvasWidth
            val y = random.nextFloat() * canvasHeight
            val size = random.nextFloat() * 2f
            drawCircle(
                color = VirasatColors.Sandstone.copy(alpha = 0.1f),
                radius = size,
                center = Offset(x, y)
            )
        }

        // 2. Draw Mandala Patterns
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        
        drawMandala(centerX, 150f, patternColor)
        drawMandala(centerX, canvasHeight - 150f, patternColor)
        drawMandala(-50f, centerY, patternColor)
        drawMandala(canvasWidth + 50f, centerY, patternColor)
        
        // Decorative corner accents
        drawMandala(0f, 0f, patternColor)
        drawMandala(canvasWidth, 0f, patternColor)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMandala(
    x: Float,
    y: Float,
    color: Color
) {
    val radius = 180f
    val center = Offset(x, y)
    
    // Outer circle
    drawCircle(
        color = color,
        radius = radius,
        center = center,
        style = Stroke(width = 1f)
    )
    
    // Inner petals/geometric patterns
    val petals = 12
    for (i in 0 until petals) {
        val angle = (i * (360 / petals)).toDouble()
        val rad = Math.toRadians(angle)
        val endX = x + radius * cos(rad).toFloat()
        val endY = y + radius * sin(rad).toFloat()
        
        drawLine(
            color = color,
            start = center,
            end = Offset(endX, endY),
            strokeWidth = 0.5f
        )
        
        drawCircle(
            color = color,
            radius = radius / 4,
            center = Offset(endX, endY),
            style = Stroke(width = 0.5f)
        )
    }
}
