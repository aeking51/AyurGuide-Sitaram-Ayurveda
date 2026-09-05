package com.example.ui.screens

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DoshaType
import com.example.ui.AyurvedaUiState
import com.example.ui.theme.NaturalBackground
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalCardSurface
import com.example.ui.theme.NaturalEarthGold
import com.example.ui.theme.NaturalHydrationBlue
import com.example.ui.theme.NaturalKaphaGold
import com.example.ui.theme.NaturalMossPrimary
import com.example.ui.theme.NaturalOliveMuted
import com.example.ui.theme.NaturalParchmentBorder
import com.example.ui.theme.NaturalParchmentContainer
import com.example.ui.theme.NaturalPittaGreen
import com.example.ui.theme.NaturalProgressTrack
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageContainer
import com.example.ui.theme.NaturalTerracotta
import com.example.ui.theme.NaturalTextHeading
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalVataViolet

@Composable
fun HealthInsightsScreen(
    uiState: AyurvedaUiState,
    onUpdateDosha: (DoshaType, Int) -> Unit,
    onAddHydration: (Float) -> Unit,
    onToggleHabit: (String) -> Unit,
    onToggleDose: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Section Title
        item {
            Column {
                Text(
                    text = "AYURVEDIC HEALTH TRACKER",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp,
                    color = NaturalOliveMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Dosha Harmony & Vitality",
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextHeading
                )
            }
        }

        // 1. Dosha Balance Triple Meters
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalCardBorder, RoundedCornerShape(24.dp)),
                color = NaturalCardSurface,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CURRENT DOSHA METERS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalOliveMuted,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Tap to Adjust",
                            fontSize = 10.sp,
                            color = NaturalEarthGold,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Pitta Meter
                    DoshaMeterRow(
                        doshaName = "Pitta (Fire/Water)",
                        symbol = "🔥",
                        percent = uiState.pittaPercent,
                        accentColor = NaturalPittaGreen,
                        onValueChange = { onUpdateDosha(DoshaType.PITTA, it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Vata Meter
                    DoshaMeterRow(
                        doshaName = "Vata (Ether/Air)",
                        symbol = "💨",
                        percent = uiState.vataPercent,
                        accentColor = NaturalVataViolet,
                        onValueChange = { onUpdateDosha(DoshaType.VATA, it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Kapha Meter
                    DoshaMeterRow(
                        doshaName = "Kapha (Earth/Water)",
                        symbol = "💧",
                        percent = uiState.kaphaPercent,
                        accentColor = NaturalKaphaGold,
                        onValueChange = { onUpdateDosha(DoshaType.KAPHA, it) }
                    )
                }
            }
        }

        // 2. Hydration & Herbal Detox Counter
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalCardBorder, RoundedCornerShape(24.dp)),
                color = NaturalCardSurface,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "DETOX & HYDRATION LEVEL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalOliveMuted,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Warm Water & Herbal Teas",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextHeading
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NaturalParchmentContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${Math.round((uiState.hydrationLiters / uiState.hydrationTargetLiters) * 100)}% Goal",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTerracotta
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress Bar
                    val progress = (uiState.hydrationLiters / uiState.hydrationTargetLiters).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(NaturalProgressTrack)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(NaturalHydrationBlue)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${uiState.hydrationLiters}L of ${uiState.hydrationTargetLiters}L Target",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextHeading
                        )

                        // Quick buttons: -250ml, +250ml, +500ml
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(NaturalBackground)
                                    .border(1.dp, NaturalCardBorder, CircleShape)
                                    .clickable { onAddHydration(-0.25f) }
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Remove 250ml",
                                    tint = NaturalOliveMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Button(
                                onClick = { onAddHydration(0.25f) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NaturalSageContainer,
                                    contentColor = NaturalMossPrimary
                                ),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("+250ml", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onAddHydration(0.5f) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NaturalMossPrimary,
                                    contentColor = Color.White
                                ),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("+500ml", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 3. Dinacharya (Daily Ayurvedic Rituals)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "DAILY DINACHARYA RITUALS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = NaturalOliveMuted
                )

                uiState.dailyHabits.forEach { habit ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, if (habit.isCompleted) NaturalSageBorder else NaturalCardBorder, RoundedCornerShape(20.dp))
                            .clickable { onToggleHabit(habit.id) },
                        color = if (habit.isCompleted) NaturalSageContainer.copy(alpha = 0.4f) else NaturalCardSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(NaturalSageContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = habit.iconEmoji, fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = habit.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalTextHeading
                                    )
                                    Text(
                                        text = "${habit.scheduledTime} • ${habit.description}",
                                        fontSize = 10.sp,
                                        color = NaturalOliveMuted,
                                        maxLines = 2
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (habit.isCompleted) NaturalMossPrimary else NaturalBackground)
                                    .border(1.dp, if (habit.isCompleted) NaturalMossPrimary else NaturalCardBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (habit.isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Daily Dosage Adherence
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(20.dp)),
                color = NaturalParchmentContainer.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TODAY'S MEDICINE LOG",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalEarthGold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val loggedCount = uiState.dailyDoses.count { it.isLogged }
                    Text(
                        text = "$loggedCount of ${uiState.dailyDoses.size} scheduled doses taken today",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTerracotta
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Regularity with designated Anupana ensures maximum bioavailability according to Charaka Samhita.",
                        fontSize = 11.sp,
                        color = NaturalTextPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun DoshaMeterRow(
    doshaName: String,
    symbol: String,
    percent: Int,
    accentColor: Color,
    onValueChange: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = symbol, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = doshaName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NaturalTextHeading
                )
            }
            Text(
                text = "$percent%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }

        Slider(
            value = percent.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 20f..100f,
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = NaturalProgressTrack
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        )
    }
}
