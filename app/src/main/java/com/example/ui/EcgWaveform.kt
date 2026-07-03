package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun EcgWaveform(rhythmId: String, modifier: Modifier = Modifier) {
    val isDark = MaterialTheme.colorScheme.background.value.toString().contains("Dark") // basic check
    
    // Medical grid color
    val gridColor = if (isDark) Color(0xFF1B3A4B) else Color(0xFFFFEBEE)
    val waveColor = if (isDark) Color(0xFF00E676) else Color(0xFFD32F2F) // Green on dark, Red on light
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(if (isDark) Color(0xFF071426) else Color(0xFFFFF8F8))
    ) {
        val width = size.width
        val height = size.height
        val midY = height / 2f
        
        // Draw standard ECG Millimeter Grid background
        val step = 15f
        var xGrid = 0f
        while (xGrid < width) {
            val strokeWidth = if ((xGrid / step).toInt() % 5 == 0) 1.5f else 0.5f
            drawLine(
                color = gridColor,
                start = Offset(xGrid, 0f),
                end = Offset(xGrid, height),
                strokeWidth = strokeWidth
            )
            xGrid += step
        }
        
        var yGrid = 0f
        while (yGrid < height) {
            val strokeWidth = if ((yGrid / step).toInt() % 5 == 0) 1.5f else 0.5f
            drawLine(
                color = gridColor,
                start = Offset(0f, yGrid),
                end = Offset(width, yGrid),
                strokeWidth = strokeWidth
            )
            yGrid += step
        }

        // Generate and draw the characteristic path for the chosen rhythm
        val path = Path()
        path.moveTo(0f, midY)
        
        when (rhythmId) {
            "brady" -> {
                for (x in 0..width.toInt()) {
                    val cycleX = x % 240
                    val y = when {
                        cycleX in 30..45 -> midY - 10f * kotlin.math.sin((cycleX - 30) * Math.PI / 15).toFloat()
                        cycleX in 60..63 -> midY + 4f
                        cycleX in 63..67 -> midY - (height * 0.4f)
                        cycleX in 67..71 -> midY + (height * 0.12f)
                        cycleX in 71..74 -> midY
                        cycleX in 100..120 -> midY - 14f * kotlin.math.sin((cycleX - 100) * Math.PI / 20).toFloat()
                        else -> midY
                    }
                    if (x == 0) path.moveTo(x.toFloat(), y) else path.lineTo(x.toFloat(), y)
                }
            }
            "tachy" -> {
                for (x in 0..width.toInt()) {
                    val cycleX = x % 70
                    val y = when {
                        cycleX in 10..20 -> midY - 8f * kotlin.math.sin((cycleX - 10) * Math.PI / 10).toFloat()
                        cycleX in 22..24 -> midY + 4f
                        cycleX in 24..27 -> midY - (height * 0.38f)
                        cycleX in 27..30 -> midY + (height * 0.1f)
                        cycleX in 30..32 -> midY
                        cycleX in 40..52 -> midY - 12f * kotlin.math.sin((cycleX - 40) * Math.PI / 12).toFloat()
                        else -> midY
                    }
                    if (x == 0) path.moveTo(x.toFloat(), y) else path.lineTo(x.toFloat(), y)
                }
            }
            "fa" -> {
                val dists = listOf(40, 110, 150, 230, 290, 370, 430, 510, 580)
                for (x in 0..width.toInt()) {
                    val baseTremble = 6f * kotlin.math.sin(x * 0.6).toFloat() * kotlin.math.cos(x * 0.15).toFloat()
                    var spikeOffset = 0f
                    for (spike in dists) {
                        val dx = x - spike
                        if (dx in -5..5) {
                            spikeOffset = when {
                                dx in -5..-3 -> 5f * (dx + 5) / 2f
                                dx in -3..0 -> -height * 0.4f * (dx + 3) / 3f
                                dx in 0..3 -> -height * 0.4f + height * 0.48f * dx / 3f
                                else -> height * 0.08f - height * 0.08f * (dx - 3) / 2f
                            }
                            break
                        }
                    }
                    val y = midY + baseTremble + spikeOffset
                    if (x == 0) path.moveTo(x.toFloat(), y) else path.lineTo(x.toFloat(), y)
                }
            }
            "flutter" -> {
                val dists = listOf(100, 220, 340, 460, 580)
                for (x in 0..width.toInt()) {
                    val sawtooth = 12f * (x % 30) / 30f - 6f
                    var spikeOffset = 0f
                    for (spike in dists) {
                        val dx = x - spike
                        if (dx in -5..5) {
                            spikeOffset = when {
                                dx < 0 -> -height * 0.4f * (dx + 5) / 5f
                                else -> -height * 0.4f + height * 0.4f * dx / 5f
                            }
                        }
                    }
                    val y = midY + sawtooth + spikeOffset
                    if (x == 0) path.moveTo(x.toFloat(), y) else path.lineTo(x.toFloat(), y)
                }
            }
            "tv" -> {
                for (x in 0..width.toInt()) {
                    val y = midY - (height * 0.38f) * kotlin.math.sin(x * 0.15).toFloat()
                    if (x == 0) path.moveTo(x.toFloat(), y) else path.lineTo(x.toFloat(), y)
                }
            }
            "fv" -> {
                for (x in 0..width.toInt()) {
                    val y = midY - (height * 0.28f) * (kotlin.math.sin(x * 0.12).toFloat() * kotlin.math.cos(x * 0.04).toFloat() + 0.6f * kotlin.math.sin(x * 0.28).toFloat())
                    if (x == 0) path.moveTo(x.toFloat(), y) else path.lineTo(x.toFloat(), y)
                }
            }
            "asystolie" -> {
                for (x in 0..width.toInt()) {
                    val noise = 1.5f * kotlin.math.sin(x * 0.08).toFloat() * kotlin.math.cos(x * 0.25).toFloat()
                    val y = midY + noise
                    if (x == 0) path.moveTo(x.toFloat(), y) else path.lineTo(x.toFloat(), y)
                }
            }
            "bav" -> {
                for (x in 0..width.toInt()) {
                    val cycleX = x % 260
                    val y = when {
                        cycleX in 20..35 -> midY - 9f * kotlin.math.sin((cycleX - 20) * Math.PI / 15).toFloat()
                        cycleX in 120..123 -> midY + 4f
                        cycleX in 123..127 -> midY - (height * 0.4f)
                        cycleX in 127..131 -> midY + (height * 0.1f)
                        cycleX in 131..134 -> midY
                        cycleX in 160..180 -> midY - 14f * kotlin.math.sin((cycleX - 160) * Math.PI / 20).toFloat()
                        else -> midY
                    }
                    if (x == 0) path.moveTo(x.toFloat(), y) else path.lineTo(x.toFloat(), y)
                }
            }
            else -> { // Normal Sinus Rhythm
                for (x in 0..width.toInt()) {
                    val cycleX = x % 120
                    val y = when {
                        cycleX in 15..30 -> midY - 8f * kotlin.math.sin((cycleX - 15) * Math.PI / 15).toFloat()
                        cycleX in 40..42 -> midY + 4f
                        cycleX in 42..45 -> midY - (height * 0.42f)
                        cycleX in 45..48 -> midY + (height * 0.12f)
                        cycleX in 48..50 -> midY
                        cycleX in 70..88 -> midY - 12f * kotlin.math.sin((cycleX - 70) * Math.PI / 18).toFloat()
                        else -> midY
                    }
                    if (x == 0) path.moveTo(x.toFloat(), y) else path.lineTo(x.toFloat(), y)
                }
            }
        }
        
        drawPath(
            path = path,
            color = waveColor,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}
