package com.example.catanclock.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun SevenSegmentNumber(
    number: Int,
    activeColor: Color,
    modifier: Modifier = Modifier,
    dimColor: Color = activeColor.copy(alpha = 0.025f),
    maxDigitCount: Int = 3
) {
    val totalSecs = number.coerceAtLeast(0)
    val digits = when {
        maxDigitCount >= 3 || totalSecs >= 100 -> String.format(Locale.getDefault(), "%03d", totalSecs)
        else -> String.format(Locale.getDefault(), "%02d", totalSecs)
    }

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        val count = digits.length
        val factor = count * 0.50f + (count - 1) * 0.08f
        val calculatedHeight = (this.maxWidth / factor).coerceAtMost(210.dp)
        val digitWidth = calculatedHeight * 0.50f
        val digitSpacing = calculatedHeight * 0.08f

        Row(verticalAlignment = Alignment.CenterVertically) {
            digits.forEachIndexed { idx, char ->
                if (idx > 0) Spacer(modifier = Modifier.width(digitSpacing))
                SevenSegmentDigit(
                    digit = char.digitToIntOrNull() ?: 0,
                    activeColor = activeColor,
                    dimColor = dimColor,
                    modifier = Modifier
                        .width(digitWidth)
                        .height(calculatedHeight)
                )
            }
        }
    }
}

@Composable
fun SevenSegmentDigit(
    digit: Int,
    activeColor: Color,
    dimColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val thickness = w * 0.15f
        val gap = thickness * 0.12f

        // Segment bitmask: A, B, C, D, E, F, G
        val segs = when (digit) {
            0 -> booleanArrayOf(true, true, true, true, true, true, false)
            1 -> booleanArrayOf(false, true, true, false, false, false, false)
            2 -> booleanArrayOf(true, true, false, true, true, false, true)
            3 -> booleanArrayOf(true, true, true, true, false, false, true)
            4 -> booleanArrayOf(false, true, true, false, false, true, true)
            5 -> booleanArrayOf(true, false, true, true, false, true, true)
            6 -> booleanArrayOf(true, false, true, true, true, true, true)
            7 -> booleanArrayOf(true, true, true, false, false, false, false)
            8 -> booleanArrayOf(true, true, true, true, true, true, true)
            9 -> booleanArrayOf(true, true, true, true, false, true, true)
            else -> booleanArrayOf(false, false, false, false, false, false, false)
        }

        val halfH = h / 2f

        // A (Top)
        drawPath(
            path = Path().apply {
                moveTo(thickness + gap, 0f)
                lineTo(w - thickness - gap, 0f)
                lineTo(w - thickness * 1.5f - gap, thickness)
                lineTo(thickness * 1.5f + gap, thickness)
                close()
            },
            color = if (segs[0]) activeColor else dimColor
        )

        // B (Top Right)
        drawPath(
            path = Path().apply {
                moveTo(w, thickness + gap)
                lineTo(w, halfH - gap / 2f)
                lineTo(w - thickness, halfH - thickness / 2f - gap)
                lineTo(w - thickness, thickness * 1.5f + gap)
                close()
            },
            color = if (segs[1]) activeColor else dimColor
        )

        // C (Bottom Right)
        drawPath(
            path = Path().apply {
                moveTo(w, halfH + gap / 2f)
                lineTo(w, h - thickness - gap)
                lineTo(w - thickness, h - thickness * 1.5f - gap)
                lineTo(w - thickness, halfH + thickness / 2f + gap)
                close()
            },
            color = if (segs[2]) activeColor else dimColor
        )

        // D (Bottom)
        drawPath(
            path = Path().apply {
                moveTo(thickness + gap, h)
                lineTo(w - thickness - gap, h)
                lineTo(w - thickness * 1.5f - gap, h - thickness)
                lineTo(thickness * 1.5f + gap, h - thickness)
                close()
            },
            color = if (segs[3]) activeColor else dimColor
        )

        // E (Bottom Left)
        drawPath(
            path = Path().apply {
                moveTo(0f, halfH + gap / 2f)
                lineTo(thickness, halfH + thickness / 2f + gap)
                lineTo(thickness, h - thickness * 1.5f - gap)
                lineTo(0f, h - thickness - gap)
                close()
            },
            color = if (segs[4]) activeColor else dimColor
        )

        // F (Top Left)
        drawPath(
            path = Path().apply {
                moveTo(0f, thickness + gap)
                lineTo(thickness, thickness * 1.5f + gap)
                lineTo(thickness, halfH - thickness / 2f - gap)
                lineTo(0f, halfH - gap / 2f)
                close()
            },
            color = if (segs[5]) activeColor else dimColor
        )

        // G (Middle)
        drawPath(
            path = Path().apply {
                moveTo(thickness * 1.2f + gap, halfH)
                lineTo(thickness * 1.7f + gap, halfH - thickness / 2f)
                lineTo(w - thickness * 1.7f - gap, halfH - thickness / 2f)
                lineTo(w - thickness * 1.2f - gap, halfH)
                lineTo(w - thickness * 1.7f - gap, halfH + thickness / 2f)
                lineTo(thickness * 1.7f + gap, halfH + thickness / 2f)
                close()
            },
            color = if (segs[6]) activeColor else dimColor
        )
    }
}
