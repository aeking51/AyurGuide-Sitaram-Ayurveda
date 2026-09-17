package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppUser
import com.example.data.model.AyurvedaMedicine
import com.example.data.model.UserRole
import com.example.ui.AppTab
import com.example.ui.theme.AyurTheme
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
    searchHistory: List<String> = emptyList(),
    onSearchSubmitted: (String) -> Unit = {},
    onRemoveSearchHistoryItem: (String) -> Unit = {},
    onClearSearchHistory: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val theme = AyurTheme.colors

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
            Column(modifier = Modifier.weight(1f, fill = false)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "NAMASTE, ${userName.uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.4.sp,
                        color = theme.mutedText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (userRole) {
                                    UserRole.ADMIN -> theme.terracotta.copy(alpha = 0.15f)
                                    UserRole.PRACTITIONER -> theme.sageContainer
                                    UserRole.PATIENT -> theme.parchmentContainer
                                    UserRole.GUEST -> theme.sageContainer.copy(alpha = 0.7f)
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
                                UserRole.ADMIN -> theme.terracotta
                                UserRole.PRACTITIONER -> theme.primaryBrandDark
                                UserRole.PATIENT -> theme.earthGold
                                UserRole.GUEST -> theme.primaryBrand
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
                    color = theme.headingText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Avatar circle matching Natural Tones design
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(theme.sageContainer)
                    .border(2.dp, theme.cardBorder, CircleShape)
                    .clickable(onClick = onProfileClick)
                    .testTag("top_header_avatar"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = theme.primaryBrand
                )
            }
        }

        // Search Bar matching Design HTML
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(theme.cardBg)
                .border(1.dp, theme.cardBorder, RoundedCornerShape(16.dp))
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
                    tint = theme.mutedText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search herbs, remedies, doshas...",
                            fontSize = 13.sp,
                            color = theme.mutedText.copy(alpha = 0.7f)
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChanged,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearchSubmitted(searchQuery) }),
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            color = theme.primaryText,
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
                            tint = theme.mutedText,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Recent Search History Chips
        if (searchHistory.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.mutedText
                )
                searchHistory.forEach { historyItem ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = theme.cardBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, theme.cardBorder),
                        modifier = Modifier
                            .clickable { onSearchSubmitted(historyItem) }
                            .testTag("recent_search_chip_$historyItem")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = theme.primaryBrand,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = historyItem,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = theme.primaryText
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove $historyItem",
                                tint = theme.mutedText.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(10.dp)
                                    .clickable { onRemoveSearchHistoryItem(historyItem) }
                            )
                        }
                    }
                }

                Text(
                    text = "Clear",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NaturalTerracotta,
                    modifier = Modifier
                        .clickable { onClearSearchHistory() }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
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
    val theme = AyurTheme.colors
    val showAdminTab = currentUserRole == UserRole.ADMIN || currentUserRole == UserRole.PRACTITIONER

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = theme.cardBg,
        shadowElevation = 4.dp
    ) {
        Column {
            HorizontalDivider(
                thickness = 1.dp,
                color = theme.cardBorder
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    emoji = "🏠",
                    label = "Home",
                    isSelected = currentTab == AppTab.HOME,
                    onClick = { onTabSelected(AppTab.HOME) },
                    testTag = "nav_home",
                    modifier = Modifier.weight(1f)
                )
                NavItem(
                    emoji = "🌿",
                    label = "Catalogue",
                    isSelected = currentTab == AppTab.LIBRARY,
                    onClick = { onTabSelected(AppTab.LIBRARY) },
                    testTag = "nav_library",
                    modifier = Modifier.weight(1f)
                )
                NavItem(
                    emoji = "👤",
                    label = "Profile",
                    isSelected = currentTab == AppTab.PROFILE,
                    onClick = { onTabSelected(AppTab.PROFILE) },
                    testTag = "nav_profile",
                    modifier = Modifier.weight(1f)
                )
                if (showAdminTab) {
                    NavItem(
                        emoji = if (currentUserRole == UserRole.ADMIN) "⚡" else "⚕️",
                        label = if (currentUserRole == UserRole.ADMIN) "Admin" else "Clinic",
                        isSelected = currentTab == AppTab.ADMIN,
                        onClick = { onTabSelected(AppTab.ADMIN) },
                        testTag = "nav_admin",
                        modifier = Modifier.weight(1f)
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
    testTag: String,
    modifier: Modifier = Modifier
) {
    val theme = AyurTheme.colors

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Text(
            text = emoji,
            fontSize = 20.sp,
            color = if (isSelected) theme.primaryBrand else theme.mutedText.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (isSelected) theme.primaryBrand else theme.mutedText.copy(alpha = 0.6f)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
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

                // Action buttons: Close & Add to Routine
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(0.35f)
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NaturalCardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NaturalOliveMuted)
                    ) {
                        Text(
                            text = "Close",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            onAddToRoutine(medicine)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(0.65f)
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
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ADD TO ROUTINE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Guest Limited Access Notice Banner
 * Informs visitors of restricted/read-only mode and provides a direct call-to-action
 * to sign in or register for full features (clinical console, consultations, personalized regimens).
 */
@Composable
fun GuestLimitedAccessBanner(
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = AyurTheme.colors

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, theme.sageBorder, RoundedCornerShape(16.dp))
            .testTag("guest_limited_access_banner"),
        color = theme.sageContainer.copy(alpha = if (theme.isDark) 0.35f else 0.65f),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(theme.primaryBrand.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🍃", fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "GUEST ACCESS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.1.sp,
                                color = theme.primaryBrandDark
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(theme.primaryBrand)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "LIMITED",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Text(
                            text = "Classical Formulation Explorer",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.headingText
                        )
                    }
                }

                Button(
                    onClick = onSignInClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = theme.primaryBrand,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("guest_banner_signin_button")
                ) {
                    Text(
                        text = "Sign In / Join",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You are browsing the herb library in read-only guest mode. Sign in to save personal dosha regimens, consult Ayurvedic practitioners, or access the clinical console.",
                fontSize = 10.5.sp,
                color = theme.primaryText.copy(alpha = 0.85f),
                lineHeight = 15.sp
            )
        }
    }
}
