package com.example.catanclock.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catanclock.ui.theme.CatanPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun CatanDicePanel(
    lastRoll: Pair<Int, Int>?,
    lastEventRoll: Int?,
    isCitiesAndKnightsEnabled: Boolean,
    onRollDice: () -> Unit,
    onToggleCitiesAndKnights: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var displayRedVal by remember { mutableIntStateOf(lastRoll?.first ?: 3) }
    var displayYellowVal by remember { mutableIntStateOf(lastRoll?.second ?: 4) }
    var displayEventVal by remember { mutableIntStateOf(lastEventRoll ?: 1) }
    var isRolling by remember { mutableStateOf(false) }

    val rotationRed = remember { Animatable(0f) }
    val rotationYellow = remember { Animatable(0f) }
    val rotationEvent = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(1f) }

    val triggerRoll = {
        if (!isRolling) {
            isRolling = true
            onRollDice()
            coroutineScope.launch {
                val rollJob = launch {
                    // Flicker random values during the first 240ms ONLY
                    for (step in 1..4) {
                        displayRedVal = Random.nextInt(1, 7)
                        displayYellowVal = Random.nextInt(1, 7)
                        displayEventVal = Random.nextInt(1, 7)
                        delay(60)
                    }
                    // Lock onto final rolled results BEFORE 3D rotation lands!
                    displayRedVal = lastRoll?.first ?: Random.nextInt(1, 7)
                    displayYellowVal = lastRoll?.second ?: Random.nextInt(1, 7)
                    displayEventVal = lastEventRoll ?: Random.nextInt(1, 7)
                }

                launch {
                    rotationRed.animateTo(360f, tween(500, easing = LinearOutSlowInEasing))
                    rotationRed.snapTo(0f)
                }
                launch {
                    rotationYellow.animateTo(-360f, tween(500, easing = LinearOutSlowInEasing))
                    rotationYellow.snapTo(0f)
                }
                launch {
                    rotationEvent.animateTo(360f, tween(500, easing = LinearOutSlowInEasing))
                    rotationEvent.snapTo(0f)
                }
                launch {
                    scaleAnim.animateTo(1.18f, tween(250))
                    scaleAnim.animateTo(1f, tween(250))
                }

                rollJob.join()
                displayRedVal = lastRoll?.first ?: displayRedVal
                displayYellowVal = lastRoll?.second ?: displayYellowVal
                displayEventVal = lastEventRoll ?: displayEventVal
                isRolling = false
            }
        }
    }

    LaunchedEffect(lastRoll) {
        if (lastRoll != null) {
            displayRedVal = lastRoll.first
            displayYellowVal = lastRoll.second
        }
        if (lastEventRoll != null) {
            displayEventVal = lastEventRoll
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.5.dp, Color(0xFF383838), RoundedCornerShape(24.dp))
            .clickable { triggerRoll() },
        color = Color(0xFF1B1B1B),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Panel Header & C&K Toggle Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        tint = CatanPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Catan Dice",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(
                        selected = isCitiesAndKnightsEnabled,
                        onClick = { onToggleCitiesAndKnights(!isCitiesAndKnightsEnabled) },
                        label = { Text("🛡 C&K Die", fontSize = 11.sp) }
                    )

                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Panel",
                            tint = Color.Gray
                        )
                    }
                }
            }

            // Dice Display Row (Tapping anywhere rolls all dice!)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                // Red Die (Matching Red Border)
                CatanDie(
                    value = displayRedVal,
                    dieColor = Color(0xFFD32F2F),
                    dotColor = Color.White,
                    borderColor = Color(0xFFE53935),
                    rotationAngle = rotationRed.value,
                    scaleFactor = scaleAnim.value
                )

                // Yellow Die (Matching Yellow Border)
                CatanDie(
                    value = displayYellowVal,
                    dieColor = Color(0xFFFFD600),
                    dotColor = Color(0xFF121212),
                    borderColor = Color(0xFFFFEA00),
                    rotationAngle = rotationYellow.value,
                    scaleFactor = scaleAnim.value
                )

                // Cities & Knights Event Die (Matching Ivory Border)
                if (isCitiesAndKnightsEnabled) {
                    EventDie(
                        value = displayEventVal,
                        rotationAngle = rotationEvent.value,
                        scaleFactor = scaleAnim.value
                    )
                }
            }

            Text(
                text = "Tap dice to roll",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun EventDie(
    value: Int,
    rotationAngle: Float,
    scaleFactor: Float
) {
    val symbolColor = when (value) {
        1, 2, 3 -> Color(0xFF1B1B1B) // Black Barbarian
        4 -> Color(0xFF1E88E5)       // Blue Trade
        5 -> Color(0xFF4CAF50)       // Green Politics
        6 -> Color(0xFFFFD600)       // Yellow Science
        else -> Color(0xFF1B1B1B)
    }

    Surface(
        modifier = Modifier
            .size(76.dp)
            .graphicsLayer {
                rotationZ = rotationAngle
                scaleX = scaleFactor
                scaleY = scaleFactor
            }
            .clip(RoundedCornerShape(16.dp))
            .border(2.5.dp, Color(0xFFE0D8C8), RoundedCornerShape(16.dp)),
        color = Color(0xFFF5F0E6), // Ivory / Cream Event Die
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = symbolColor,
                modifier = Modifier.size(46.dp)
            )
        }
    }
}

@Composable
fun CatanDie(
    value: Int,
    dieColor: Color,
    dotColor: Color,
    borderColor: Color,
    rotationAngle: Float,
    scaleFactor: Float
) {
    Surface(
        modifier = Modifier
            .size(76.dp)
            .graphicsLayer {
                rotationZ = rotationAngle
                scaleX = scaleFactor
                scaleY = scaleFactor
            }
            .clip(RoundedCornerShape(16.dp))
            .border(2.5.dp, borderColor, RoundedCornerShape(16.dp)),
        color = dieColor,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(9.dp),
            contentAlignment = Alignment.Center
        ) {
            val showTL = value in listOf(2, 3, 4, 5, 6)
            val showTR = value in listOf(4, 5, 6)
            val showML = value == 6
            val showC  = value in listOf(1, 3, 5)
            val showMR = value == 6
            val showBL = value in listOf(4, 5, 6)
            val showBR = value in listOf(2, 3, 4, 5, 6)

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DieDot(visible = showTL, color = dotColor)
                    Spacer(modifier = Modifier.width(1.dp))
                    DieDot(visible = showTR, color = dotColor)
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DieDot(visible = showML, color = dotColor)
                    DieDot(visible = showC, color = dotColor)
                    DieDot(visible = showMR, color = dotColor)
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DieDot(visible = showBL, color = dotColor)
                    Spacer(modifier = Modifier.width(1.dp))
                    DieDot(visible = showBR, color = dotColor)
                }
            }
        }
    }
}

@Composable
fun DieDot(visible: Boolean, color: Color) {
    Box(
        modifier = Modifier
            .size(15.dp)
            .clip(CircleShape)
            .background(if (visible) color else Color.Transparent)
    )
}
