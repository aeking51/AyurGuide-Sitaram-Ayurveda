package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.AyurvedaMedicine
import com.example.ui.AyurvedaUiState
import com.example.ui.theme.NaturalBackground
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalCardSurface
import com.example.ui.theme.NaturalEarthGold
import com.example.ui.theme.NaturalHydrationBlue
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

@Composable
fun HomeScreen(
    uiState: AyurvedaUiState,
    onLogVitalityDose: () -> Unit,
    onSelectMedicine: (AyurvedaMedicine) -> Unit,
    onToggleHabit: (String) -> Unit,
    onToggleDose: (String) -> Unit,
    onAddHydration: (Float) -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToInsights: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Daily Vitality Section (Exact replica of Design HTML layout & styling)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(24.dp))
                    .clickable { onSelectMedicine(uiState.dailyVitalityMedicine) }
                    .testTag("vitality_card"),
                color = NaturalParchmentContainer,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Header row: Category & Pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "DAILY VITALITY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalEarthGold,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = uiState.dailyVitalityMedicine.name,
                                fontFamily = FontFamily.Serif,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTerracotta
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.5f))
                                .border(1.dp, NaturalTerracotta.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = uiState.dailyVitalityMedicine.tagPill,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTerracotta,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = uiState.dailyVitalityMedicine.shortDescription,
                        fontSize = 11.sp,
                        color = Color(0xFF7A6A53),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Constituents tags matching Natural Tones design
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        uiState.dailyVitalityMedicine.constituents.take(2).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NaturalSageContainer)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NaturalMossPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bottom row: Dosage and Log button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = NaturalEarthGold.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(0.dp)
                            )
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🍵", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = uiState.dailyVitalityMedicine.dosage.summary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NaturalTerracotta
                            )
                        }

                        Button(
                            onClick = onLogVitalityDose,
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("log_dose_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.isDailyVitalityLogged) NaturalPittaGreen else NaturalMossPrimary,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (uiState.isDailyVitalityLogged) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = if (uiState.isDailyVitalityLogged) "LOGGED" else "LOG DOSE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Health Tracking Section (Matching Design HTML 2-col + span-2 structure)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HEALTH TRACKING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NaturalOliveMuted,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                    Text(
                        text = "VIEW INSIGHTS ➜",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalMossPrimary,
                        modifier = Modifier
                            .clickable(onClick = onNavigateToInsights)
                            .padding(4.dp)
                    )
                }

                // 2 Column Grid: Dosha and Detox (Hydration)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left: Dosha Balance Card
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, NaturalCardBorder, RoundedCornerShape(20.dp))
                            .clickable(onClick = onNavigateToInsights),
                        color = NaturalCardSurface,
                        shadowElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🌿", fontSize = 18.sp)
                                Text(
                                    text = "DOSHA",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalOliveMuted
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Pitta Balance",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextHeading
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Progress bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(NaturalProgressTrack)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(uiState.pittaPercent / 100f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(NaturalPittaGreen)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${uiState.pittaPercent}% Optimal",
                                fontSize = 10.sp,
                                color = NaturalOliveMuted
                            )
                        }
                    }

                    // Right: Detox / Hydration Card with quick incrementer
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, NaturalCardBorder, RoundedCornerShape(20.dp)),
                        color = NaturalCardSurface,
                        shadowElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "💧", fontSize = 18.sp)
                                Text(
                                    text = "DETOX",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalOliveMuted
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Hydration Level",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextHeading
                                )
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(NaturalSageContainer)
                                        .clickable { onAddHydration(0.25f) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add 250ml",
                                        tint = NaturalMossPrimary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Progress bar
                            val progress = (uiState.hydrationLiters / uiState.hydrationTargetLiters).coerceIn(0f, 1f)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(NaturalProgressTrack)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progress)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(NaturalHydrationBlue)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${uiState.hydrationLiters}L of ${uiState.hydrationTargetLiters}L",
                                fontSize = 10.sp,
                                color = NaturalOliveMuted
                            )
                        }
                    }
                }

                // Span-2 Card (Ritual: Afternoon Pranayama matching HTML design)
                val primaryHabit = uiState.dailyHabits.firstOrNull()
                if (primaryHabit != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, NaturalSageBorder, RoundedCornerShape(20.dp))
                            .clickable { onToggleHabit(primaryHabit.id) }
                            .testTag("pranayama_card"),
                        color = NaturalSageContainer,
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color.White.copy(alpha = 0.45f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = primaryHabit.iconEmoji, fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = primaryHabit.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalTextHeading
                                    )
                                    Text(
                                        text = "Scheduled for ${primaryHabit.scheduledTime}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = NaturalMossPrimary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (primaryHabit.isCompleted) NaturalMossPrimary else Color.White.copy(alpha = 0.6f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (primaryHabit.isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Text(
                                        text = "➜",
                                        fontSize = 12.sp,
                                        color = NaturalMossPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Today's Medicine Routine Checklist
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TODAY'S MEDICINE SCHEDULE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NaturalOliveMuted
                    )
                    Text(
                        text = "EXPLORE HERBS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalMossPrimary,
                        modifier = Modifier
                            .clickable(onClick = onNavigateToLibrary)
                            .padding(4.dp)
                    )
                }

                uiState.dailyDoses.forEach { dose ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, NaturalCardBorder, RoundedCornerShape(16.dp))
                            .clickable { onToggleDose(dose.id) },
                        color = NaturalCardSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = dose.iconEmoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = dose.medicineName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalTextHeading
                                    )
                                    Text(
                                        text = "${dose.doseLabel} • ${dose.timing}",
                                        fontSize = 10.sp,
                                        color = NaturalOliveMuted
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (dose.isLogged) NaturalMossPrimary else NaturalBackground)
                                    .border(1.dp, if (dose.isLogged) NaturalMossPrimary else NaturalCardBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (dose.isLogged) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Logged",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Ayurvedic Wisdom / Ritucharya
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(20.dp)),
                color = NaturalParchmentContainer.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "📜", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "RITUCHARYA (SEASONAL HARMONY)",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalEarthGold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Sip warm water with a dash of dry ginger to gently rekindle digestive fire (Agni) without aggravating Pitta.",
                            fontSize = 11.sp,
                            color = NaturalTerracotta,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
