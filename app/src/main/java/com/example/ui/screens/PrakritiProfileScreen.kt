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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.UserRole
import com.example.data.repository.AyurvedaRepository
import com.example.ui.AyurvedaUiState
import com.example.ui.theme.NaturalBackground
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalCardSurface
import com.example.ui.theme.NaturalEarthGold
import com.example.ui.theme.NaturalMossDark
import com.example.ui.theme.NaturalMossPrimary
import com.example.ui.theme.NaturalOliveMuted
import com.example.ui.theme.NaturalParchmentBorder
import com.example.ui.theme.NaturalParchmentContainer
import com.example.ui.theme.NaturalPittaGreen
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageContainer
import com.example.ui.theme.NaturalTerracotta
import com.example.ui.theme.NaturalTextHeading
import com.example.ui.theme.NaturalTextPrimary

@Composable
fun PrakritiProfileScreen(
    uiState: AyurvedaUiState,
    onAnswerQuestion: (Int, DoshaType) -> Unit,
    onSwitchUserClicked: () -> Unit = {},
    onNavigateToAdmin: () -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dominant = uiState.prakritiScore.dominantDosha
    val user = uiState.currentUser
    val canAccessAdmin = user.role == UserRole.ADMIN || user.role == UserRole.PRACTITIONER

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // User Profile & Role Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, NaturalCardBorder, RoundedCornerShape(22.dp)),
                color = NaturalCardSurface,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(NaturalSageContainer)
                                    .border(1.dp, NaturalSageBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.role.iconEmoji,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = user.name,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextHeading
                                )
                                Text(
                                    text = user.designation.ifEmpty { user.email },
                                    fontSize = 11.sp,
                                    color = NaturalOliveMuted
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onSwitchUserClicked,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = NaturalMossPrimary
                            )
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Switch Role", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NaturalBackground)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Active Role: ${user.role.displayName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NaturalTextHeading
                        )

                        if (canAccessAdmin) {
                            Text(
                                text = "Go to Admin Dashboard →",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalMossPrimary,
                                modifier = Modifier.clickable(onClick = onNavigateToAdmin)
                            )
                        }
                    }
                }
            }
        }

        // Top Header
        item {
            Column {
                Text(
                    text = "AYURVEDIC CONSTITUTION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp,
                    color = NaturalOliveMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Prakriti & Dosha Profile",
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextHeading
                )
            }
        }

        // Dominant Constitution Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(24.dp)),
                color = NaturalParchmentContainer,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "DOMINANT PRAKRITI",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalEarthGold,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${dominant.displayName} Dominant",
                                fontFamily = FontFamily.Serif,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTerracotta
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = dominant.symbol, fontSize = 22.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val desc = when (dominant) {
                        DoshaType.PITTA -> "Governed by Fire & Water elements. Radiant metabolic heat, sharp intellect, and purposeful drive. Needs cooling, grounding foods."
                        DoshaType.VATA -> "Governed by Space & Air elements. Creative, quick, and enthusiastic. Needs warming, oily, calming nourishing foods."
                        DoshaType.KAPHA -> "Governed by Earth & Water elements. Steady, compassionate, and enduring stamina. Needs light, stimulating, warming foods."
                        DoshaType.TRIDOSHIC -> "Rare tridoshic equilibrium where Vata, Pitta, and Kapha maintain organic harmony."
                    }

                    Text(
                        text = desc,
                        fontSize = 11.sp,
                        color = Color(0xFF7A6A53),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Score breakdown pills
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DoshaScorePill(
                            label = "Pitta: ${uiState.prakritiScore.pittaScore}",
                            isDominant = dominant == DoshaType.PITTA
                        )
                        DoshaScorePill(
                            label = "Vata: ${uiState.prakritiScore.vataScore}",
                            isDominant = dominant == DoshaType.VATA
                        )
                        DoshaScorePill(
                            label = "Kapha: ${uiState.prakritiScore.kaphaScore}",
                            isDominant = dominant == DoshaType.KAPHA
                        )
                    }
                }
            }
        }

        // Prakriti Questionnaire Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "DISCOVER YOUR CONSTITUTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = NaturalOliveMuted
                )

                AyurvedaRepository.prakritiQuestions.forEach { q ->
                    val selectedChoice = uiState.prakritiAnswers[q.id]
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, NaturalCardBorder, RoundedCornerShape(20.dp)),
                        color = NaturalCardSurface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Q${q.id}. ${q.trait}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextHeading
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Vata Option
                            PrakritiOptionItem(
                                label = "Vata",
                                text = q.optionVata,
                                isSelected = selectedChoice == DoshaType.VATA,
                                onSelect = { onAnswerQuestion(q.id, DoshaType.VATA) }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Pitta Option
                            PrakritiOptionItem(
                                label = "Pitta",
                                text = q.optionPitta,
                                isSelected = selectedChoice == DoshaType.PITTA,
                                onSelect = { onAnswerQuestion(q.id, DoshaType.PITTA) }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Kapha Option
                            PrakritiOptionItem(
                                label = "Kapha",
                                text = q.optionKapha,
                                isSelected = selectedChoice == DoshaType.KAPHA,
                                onSelect = { onAnswerQuestion(q.id, DoshaType.KAPHA) }
                            )
                        }
                    }
                }
            }
        }

        // Diet Recommendations for Dominant Dosha
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, NaturalSageBorder, RoundedCornerShape(20.dp)),
                color = NaturalSageContainer.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "DIETARY GUIDELINES FOR ${dominant.displayName.uppercase()}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalMossPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Wholesome Tastes (Rasa): Sweet, Bitter, Astringent foods soothe Pitta.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NaturalTextHeading
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Favor cooling foods like ghee, cucumbers, melons, cilantro, and mint. Avoid excessively spicy, fermented, or deeply salted preparations.",
                        fontSize = 11.sp,
                        color = NaturalTextPrimary.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Account & Security Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, NaturalCardBorder, RoundedCornerShape(20.dp)),
                color = NaturalCardSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ACCOUNT & SESSION SECURITY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NaturalOliveMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = user.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextHeading
                            )
                            Text(
                                text = "Signed in as ${user.email}",
                                fontSize = 11.sp,
                                color = NaturalOliveMuted
                            )
                        }

                        OutlinedButton(
                            onClick = onLogout,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = NaturalTerracotta
                            ),
                            modifier = Modifier.testTag("profile_logout_button")
                        ) {
                            Text("Sign Out", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun DoshaScorePill(label: String, isDominant: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isDominant) NaturalMossPrimary else Color.White.copy(alpha = 0.7f))
            .border(1.dp, if (isDominant) NaturalMossPrimary else NaturalCardBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDominant) Color.White else NaturalOliveMuted
        )
    }
}

@Composable
private fun PrakritiOptionItem(
    label: String,
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) NaturalSageContainer else NaturalBackground)
            .border(1.dp, if (isSelected) NaturalSageBorder else NaturalCardBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onSelect)
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) NaturalMossPrimary else Color.Transparent)
                    .border(1.5.dp, if (isSelected) NaturalMossPrimary else NaturalOliveMuted, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) NaturalMossDark else NaturalOliveMuted
                )
                Text(
                    text = text,
                    fontSize = 10.sp,
                    color = NaturalTextPrimary,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
