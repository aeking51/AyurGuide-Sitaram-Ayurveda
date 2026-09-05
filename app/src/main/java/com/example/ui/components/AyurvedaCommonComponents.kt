package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppUser
import com.example.data.model.AyurvedaMedicine
import com.example.data.model.UserRole
import com.example.ui.AppTab
import com.example.ui.theme.NaturalBackground
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalCardSurface
import com.example.ui.theme.NaturalEarthGold
import com.example.ui.theme.NaturalMossDark
import com.example.ui.theme.NaturalMossPrimary
import com.example.ui.theme.NaturalOliveMuted
import com.example.ui.theme.NaturalParchmentBorder
import com.example.ui.theme.NaturalParchmentContainer
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageContainer
import com.example.ui.theme.NaturalTerracotta
import com.example.ui.theme.NaturalTextHeading
import com.example.ui.theme.NaturalTextPrimary

@Composable
fun AyurTopHeader(
    userName: String = "Arjun",
    userRole: UserRole = UserRole.PATIENT,
    onProfileClick: () -> Unit = {},
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "NAMASTE, ${userName.uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.4.sp,
                        color = NaturalOliveMuted
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (userRole) {
                                    UserRole.ADMIN -> NaturalTerracotta.copy(alpha = 0.15f)
                                    UserRole.PRACTITIONER -> NaturalSageContainer
                                    UserRole.PATIENT -> NaturalParchmentContainer
                                }
                            )
                            .clickable(onClick = onProfileClick)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${userRole.iconEmoji} ${userRole.badgeLabel}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = when (userRole) {
                                UserRole.ADMIN -> NaturalTerracotta
                                UserRole.PRACTITIONER -> NaturalMossDark
                                UserRole.PATIENT -> NaturalEarthGold
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "AyurGuide",
                    fontFamily = FontFamily.Serif,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NaturalTextHeading
                )
            }

            // Avatar circle matching Natural Tones design
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(NaturalSageContainer)
                    .border(2.dp, Color.White, CircleShape)
                    .clickable(onClick = onProfileClick)
                    .testTag("top_header_avatar"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = NaturalMossPrimary
                )
            }
        }

        // Search Bar matching Design HTML
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NaturalCardSurface)
                .border(1.dp, NaturalCardBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NaturalOliveMuted,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search herbs, remedies, doshas...",
                            fontSize = 13.sp,
                            color = NaturalOliveMuted.copy(alpha = 0.7f)
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChanged,
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            color = NaturalTextPrimary,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_input")
                    )
                }
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchQueryChanged("") },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = NaturalOliveMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AyurBottomNav(
    currentTab: AppTab,
    currentUserRole: UserRole = UserRole.PATIENT,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val showAdminTab = currentUserRole == UserRole.ADMIN || currentUserRole == UserRole.PRACTITIONER

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = NaturalCardSurface,
        shadowElevation = 4.dp
    ) {
        Column {
            HorizontalDivider(
                thickness = 1.dp,
                color = NaturalCardBorder
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    emoji = "🏠",
                    label = "Home",
                    isSelected = currentTab == AppTab.HOME,
                    onClick = { onTabSelected(AppTab.HOME) },
                    testTag = "nav_home"
                )
                NavItem(
                    emoji = "📖",
                    label = "Library",
                    isSelected = currentTab == AppTab.LIBRARY,
                    onClick = { onTabSelected(AppTab.LIBRARY) },
                    testTag = "nav_library"
                )
                NavItem(
                    emoji = "📉",
                    label = "Insights",
                    isSelected = currentTab == AppTab.INSIGHTS,
                    onClick = { onTabSelected(AppTab.INSIGHTS) },
                    testTag = "nav_insights"
                )
                NavItem(
                    emoji = "👤",
                    label = "Profile",
                    isSelected = currentTab == AppTab.PROFILE,
                    onClick = { onTabSelected(AppTab.PROFILE) },
                    testTag = "nav_profile"
                )
                if (showAdminTab) {
                    NavItem(
                        emoji = if (currentUserRole == UserRole.ADMIN) "⚡" else "⚕️",
                        label = if (currentUserRole == UserRole.ADMIN) "Admin" else "Clinic",
                        isSelected = currentTab == AppTab.ADMIN,
                        onClick = { onTabSelected(AppTab.ADMIN) },
                        testTag = "nav_admin"
                    )
                }
            }
        }
    }
}

@Composable
private fun NavItem(
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Text(
            text = emoji,
            fontSize = 20.sp,
            color = if (isSelected) NaturalMossPrimary else NaturalTextPrimary.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = if (isSelected) NaturalMossPrimary else NaturalTextPrimary.copy(alpha = 0.4f)
        )
    }
}

@Composable
fun AyurMedicineDetailDialog(
    medicine: AyurvedaMedicine,
    onDismiss: () -> Unit,
    onAddToRoutine: (AyurvedaMedicine) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .heightIn(max = 680.dp)
                .clip(RoundedCornerShape(28.dp))
                .border(1.dp, NaturalCardBorder, RoundedCornerShape(28.dp)),
            color = NaturalBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = medicine.sanskritName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalEarthGold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = medicine.name,
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextHeading
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(NaturalSageContainer.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = NaturalMossDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tags
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(NaturalParchmentContainer)
                            .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = medicine.tagPill,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTerracotta
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(NaturalSageContainer)
                            .border(1.dp, NaturalSageBorder, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = medicine.category.displayName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalMossPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable content details
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Classical Energetics (Dravyaguna)
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = NaturalCardSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NaturalCardBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "DRAVYAGUNA (ENERGETICS)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalOliveMuted,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Virya (Potency)", fontSize = 10.sp, color = NaturalOliveMuted)
                                        Text(medicine.dravyaguna.virya, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NaturalTextHeading)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Vipaka", fontSize = 10.sp, color = NaturalOliveMuted)
                                        Text(medicine.dravyaguna.vipaka, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NaturalTextHeading)
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Rasa (Tastes): ${medicine.dravyaguna.rasa.joinToString(", ")}", fontSize = 11.sp, color = NaturalTextPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Dosha Impact: ${medicine.doshaImpact}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = NaturalTerracotta)
                            }
                        }
                    }

                    // 2. Dosage & Timing Recommendations (Anupana)
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = NaturalParchmentContainer,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NaturalParchmentBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "DOSAGE & TIMING (ANUPANA)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalEarthGold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Standard Dose: ${medicine.dosage.standardDose}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTerracotta
                                )
                                Text(
                                    text = "Timing: ${medicine.dosage.timing} (${medicine.dosage.frequency})",
                                    fontSize = 11.sp,
                                    color = NaturalTextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(NaturalCardSurface.copy(alpha = 0.7f))
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Text("🥛 Recommended Anupana (Carrier Vehicle):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NaturalEarthGold)
                                        Text(medicine.dosage.anupana, fontSize = 11.sp, color = NaturalTextPrimary)
                                    }
                                }
                                if (medicine.dosage.caution.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("⚠️ Caution: ${medicine.dosage.caution}", fontSize = 10.sp, color = NaturalOliveMuted)
                                }
                            }
                        }
                    }

                    // 3. Itemized Ingredients List
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = NaturalCardSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NaturalCardBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "DETAILED INGREDIENTS LIST",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalOliveMuted,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                medicine.ingredients.forEachIndexed { index, ing ->
                                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "${ing.name} (${ing.sanskritName})",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = NaturalTextHeading
                                            )
                                            Text(
                                                text = ing.partUsed,
                                                fontSize = 10.sp,
                                                color = NaturalOliveMuted
                                            )
                                        }
                                        Text(
                                            text = "Botanical: ${ing.botanicalName}",
                                            fontSize = 10.sp,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                            color = NaturalEarthGold
                                        )
                                        Text(
                                            text = "Role: ${ing.classicalRole}",
                                            fontSize = 10.sp,
                                            color = NaturalTextPrimary
                                        )
                                        if (index < medicine.ingredients.size - 1) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 6.dp),
                                                thickness = 0.5.dp,
                                                color = NaturalCardBorder
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 4. Indications & Diet Guidance (Pathya)
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = NaturalCardSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NaturalCardBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "DIET & LIFESTYLE PAIRING",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalOliveMuted,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "🌿 Wholesome Foods (Pathya):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalMossPrimary
                                )
                                Text(
                                    text = medicine.pathyaWholesome.joinToString(" • "),
                                    fontSize = 11.sp,
                                    color = NaturalTextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "🚫 Foods to Avoid (Apathya):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTerracotta
                                )
                                Text(
                                    text = medicine.apathyaAvoid.joinToString(" • "),
                                    fontSize = 11.sp,
                                    color = NaturalTextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action button: Add to Routine
                Button(
                    onClick = {
                        onAddToRoutine(medicine)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("add_to_routine_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NaturalMossPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ADD TO DAILY ROUTINE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
