package com.example.catanclock.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun FlipClockNumber(
    number: Int,
    textColor: Color,
    modifier: Modifier = Modifier,
    cardBgColor: Color = Color(0xFF1E1E1E),
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
        val factor = count * 0.68f + (count - 1) * 0.08f
        val calculatedHeight = (this.maxWidth / factor).coerceAtMost(220.dp)
        val cardWidth = calculatedHeight * 0.68f
        val spacing = calculatedHeight * 0.08f

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            digits.forEachIndexed { idx, char ->
                if (idx > 0) Spacer(modifier = Modifier.width(spacing))
                AnimatedFlipCardDigit(
                    digit = char.toString(),
                    textColor = textColor,
                    cardBgColor = cardBgColor,
                    cardHeight = calculatedHeight,
                    modifier = Modifier
                        .width(cardWidth)
                        .height(calculatedHeight)
                )
            }
        }
    }
}

@Composable
fun AnimatedFlipCardDigit(
    digit: String,
    textColor: Color,
    cardBgColor: Color,
    cardHeight: Dp,
    modifier: Modifier = Modifier
) {
    var oldDigit by remember { mutableStateOf(digit) }
    var currentDigit by remember { mutableStateOf(digit) }
    val flipProgress = remember { Animatable(0f) }

    LaunchedEffect(digit) {
        if (digit != currentDigit) {
            oldDigit = currentDigit
            currentDigit = digit
            flipProgress.snapTo(0f)
            flipProgress.animateTo(
                targetValue = 180f,
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            )
        }
    }

    val rotation = flipProgress.value
    val displayDigit = if (rotation < 90f) oldDigit else currentDigit
    val rotationXAngle = if (rotation < 90f) -rotation else (180f - rotation)

    Surface(
        modifier = modifier
            .graphicsLayer {
                rotationX = rotationXAngle
                cameraDistance = 16f * density
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            }
            .clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, Color(0xFF383838), RoundedCornerShape(12.dp)),
        color = cardBgColor,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // High-Contrast Centered Digit Text
            Text(
                text = displayDigit,
                color = textColor,
                fontSize = (cardHeight.value * 0.65f).sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )

            // Horizontal Split Crease Line
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(
                    thickness = 2.5.dp,
                    color = Color.Black.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            // Hinge Notches
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF101010))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF101010))
                )
            }
        }
    }
}
