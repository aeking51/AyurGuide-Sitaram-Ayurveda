package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AyurvedaMedicine
import com.example.data.model.DoshaType
import com.example.data.model.HealthGoal
import com.example.ui.components.EditProductPhotoDialog
import com.example.ui.components.ProductPhotoView
import com.example.ui.theme.NaturalBackground
import com.example.ui.theme.NaturalCardBorder
import com.example.ui.theme.NaturalCardSurface
import com.example.ui.theme.NaturalEarthGold
import com.example.ui.theme.NaturalKaphaGold
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
import com.example.ui.theme.NaturalVataViolet

/**
 * Dedicated full-window / full-screen view for displaying detailed Ayurveda medicine information.
 * Replaces popup modals to provide a spacious, distraction-free clinical reference experience
 * with back navigation, comprehensive botanical details, dosage instructions, and routine actions.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AyurvedaMedicineDetailScreen(
    medicine: AyurvedaMedicine,
    onNavigateBack: () -> Unit,
    onUpdatePhoto: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Intercept Android hardware/gesture back press to return cleanly to previous window
    BackHandler(onBack = onNavigateBack)

    var showEditPhotoDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .testTag("ayurveda_medicine_detail_window"),
        containerColor = NaturalBackground,
        topBar = {
            MedicineDetailTopBar(
                medicine = medicine,
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            MedicineDetailBottomBar(
                medicine = medicine,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("medicine_detail_scroll_container"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Hero Formulation Header Card with Product Photo
            item {
                HeroFormulationCard(
                    medicine = medicine,
                    canEditPhoto = onUpdatePhoto != null,
                    onEditPhotoClick = { showEditPhotoDialog = true }
                )
            }

            // 2. Classical Handbook Record (Therapeutic Index specifications)
            item {
                SitaramHandbookMonographCard(medicine = medicine)
            }

            // 3. Health Goals & Therapeutic Indications
            item {
                TherapeuticIndicationsSection(medicine = medicine)
            }

            // 4. Classical Dravyaguna (Energetics) Matrix
            item {
                DravyagunaSection(medicine = medicine)
            }

            // 5. Dosage & Timing (Anupana Vehicle)
            item {
                DosageAndTimingSection(medicine = medicine)
            }

            // 6. Itemized Ingredients & Botanical Species Registry
            item {
                IngredientsRegistrySection(medicine = medicine)
            }

            // 7. Diet & Lifestyle Pairing (Pathya & Apathya)
            item {
                DietaryGuidanceSection(medicine = medicine)
            }

            // Bottom breathing spacer
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Modal dialog to update or customize product photo
    if (showEditPhotoDialog && onUpdatePhoto != null) {
        EditProductPhotoDialog(
            medicine = medicine,
            onDismiss = { showEditPhotoDialog = false },
            onSavePhotoUrl = { newUrl ->
                onUpdatePhoto(newUrl)
                showEditPhotoDialog = false
            }
        )
    }
}

/**
 * Top App Bar with back navigation, breadcrumb title, and category badge
 */
@Composable
private fun MedicineDetailTopBar(
    medicine: AyurvedaMedicine,
    onNavigateBack: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = NaturalBackground,
        border = BorderStroke(0.dp, Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NaturalCardSurface)
                        .border(1.dp, NaturalCardBorder, CircleShape)
                        .testTag("detail_window_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Catalogue",
                        tint = NaturalTextHeading,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "CLASSICAL MONOGRAPH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NaturalOliveMuted
                    )
                    Text(
                        text = medicine.name,
                        fontFamily = FontFamily.Serif,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextHeading,
                        maxLines = 1
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(NaturalSageContainer)
                    .border(1.dp, NaturalSageBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = medicine.category.displayName,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalMossDark
                )
            }
        }
    }
}

/**
 * Hero Card with Sanskrit title, category, dosha equilibrium, health goals, and description
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeroFormulationCard(
    medicine: AyurvedaMedicine,
    canEditPhoto: Boolean = false,
    onEditPhotoClick: () -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("detail_hero_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = NaturalCardSurface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            // Prominent Product Photo with Packing & Reference Badges
            ProductPhotoView(
                medicine = medicine,
                aspectRatio = 1.8f,
                showPackingBadge = true,
                showReferenceBadge = true,
                showCategoryBadge = true,
                showEditAction = canEditPhoto,
                onEditPhotoClick = onEditPhotoClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category, Tag, and Stock Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NaturalParchmentContainer,
                        border = BorderStroke(1.dp, NaturalParchmentBorder)
                    ) {
                        Text(
                            text = medicine.category.displayName.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTerracotta,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NaturalSageContainer,
                        border = BorderStroke(1.dp, NaturalSageBorder)
                    ) {
                        Text(
                            text = medicine.tagPill,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalMossDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sanskrit Transliteration
            if (medicine.sanskritName.isNotBlank()) {
                Text(
                    text = medicine.sanskritName,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    color = NaturalEarthGold,
                    letterSpacing = 0.5.sp
                )
            }

            // Primary Medicine Name
            Text(
                text = medicine.name,
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextHeading,
                lineHeight = 30.sp
            )

            // Short overview description
            if (medicine.shortDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = medicine.shortDescription,
                    fontSize = 13.sp,
                    color = NaturalTextPrimary,
                    lineHeight = 19.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dosha Harmony Tag
            DoshaHarmonyBadge(medicine = medicine)

            // Health Goals Chips
            if (medicine.healthGoals.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    medicine.healthGoals.forEach { goal ->
                        HealthGoalChip(goal = goal)
                    }
                }
            }
        }
    }
}

/**
 * Dosha Harmony badge showing targeted doshas
 */
@Composable
private fun DoshaHarmonyBadge(medicine: AyurvedaMedicine) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = NaturalBackground,
        border = BorderStroke(1.dp, NaturalCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = NaturalMossPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "DOSHIC AFFINITY",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalOliveMuted,
                    letterSpacing = 0.8.sp
                )
            }

            Text(
                text = if (medicine.doshaImpact.isNotBlank()) medicine.doshaImpact else "Tridosha Shamak",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTerracotta
            )
        }
    }
}

/**
 * Health Goal visual chip
 */
@Composable
private fun HealthGoalChip(goal: HealthGoal) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = NaturalSageContainer.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, NaturalSageBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = goal.iconEmoji, fontSize = 12.sp)
            Text(
                text = goal.displayName,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = NaturalMossDark
            )
        }
    }
}

/**
 * Classical Formulation Handbook Record (Therapeutic Index reference monograph)
 */
@Composable
private fun SitaramHandbookMonographCard(medicine: AyurvedaMedicine) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("detail_handbook_monograph_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = NaturalCardSurface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NaturalCardBorder, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = NaturalEarthGold,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "CLASSICAL HANDBOOK RECORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalEarthGold,
                    letterSpacing = 1.2.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NaturalBackground)
                    .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HandbookDetailRow(label = "PRODUCT NAME", value = medicine.name)
                HorizontalDivider(color = NaturalCardBorder.copy(alpha = 0.5f))
                HandbookDetailRow(label = "CLASSICAL REFERENCE", value = medicine.effectiveReference)
                HorizontalDivider(color = NaturalCardBorder.copy(alpha = 0.5f))
                HandbookDetailRow(label = "PACKING", value = medicine.effectivePacking)
                HorizontalDivider(color = NaturalCardBorder.copy(alpha = 0.5f))
                HandbookDetailRow(label = "MAIN INGREDIENTS", value = medicine.effectiveMainIngredientsText)
                HorizontalDivider(color = NaturalCardBorder.copy(alpha = 0.5f))
                HandbookDetailRow(label = "USAGE / POSOLOGY", value = medicine.effectiveUsage)
                HorizontalDivider(color = NaturalCardBorder.copy(alpha = 0.5f))
                HandbookDetailRow(label = "INDICATIONS", value = medicine.indications.joinToString(", "))
            }
        }
    }
}

@Composable
private fun HandbookDetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Bold,
            color = NaturalMossDark,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value.ifBlank { "—" },
            fontSize = 13.sp,
            color = NaturalTextHeading,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Key therapeutic benefits & clinical indications
 */
@Composable
private fun TherapeuticIndicationsSection(medicine: AyurvedaMedicine) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = NaturalCardSurface,
        border = BorderStroke(1.dp, NaturalCardBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = NaturalMossPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "THERAPEUTIC ACTIONS & INDICATIONS",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalMossPrimary,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Highlighted Benefit
            if (medicine.primaryBenefit.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NaturalParchmentContainer.copy(alpha = 0.65f),
                    border = BorderStroke(1.dp, NaturalParchmentBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "✦", fontSize = 14.sp, color = NaturalEarthGold, fontWeight = FontWeight.Bold)
                        Column {
                            Text(
                                text = "PRIMARY ACTION",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTerracotta,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = medicine.primaryBenefit,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NaturalTextHeading,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Itemized Benefits
            val benefitsList = medicine.effectiveBenefits
            if (benefitsList.isNotEmpty()) {
                benefitsList.forEach { benefit ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "•",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalEarthGold
                        )
                        Text(
                            text = benefit,
                            fontSize = 12.5.sp,
                            color = NaturalTextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                Text(
                    text = "Formulated to harmonize vitiated doshas, stimulate internal metabolic vitality (Agni), and promote deep tissue rejuvenation (Rasayana).",
                    fontSize = 12.5.sp,
                    color = NaturalOliveMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

/**
 * Dravyaguna (Classical Pharmacology & Energetics) Matrix
 */
@Composable
private fun DravyagunaSection(medicine: AyurvedaMedicine) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = NaturalCardSurface,
        border = BorderStroke(1.dp, NaturalCardBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = NaturalOliveMuted,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "DRAVYAGUNA (CLASSICAL ENERGETICS)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalOliveMuted,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4-Quadrant Energetics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Virya (Potency)
                EnergeticCard(
                    title = "VIRYA (POTENCY)",
                    value = medicine.dravyaguna.virya,
                    subtext = if (medicine.dravyaguna.virya.contains("Ushna", ignoreCase = true)) "Heating" else "Cooling",
                    modifier = Modifier.weight(1f)
                )

                // Vipaka (Post-Digestive Effect)
                EnergeticCard(
                    title = "VIPAKA",
                    value = medicine.dravyaguna.vipaka,
                    subtext = "Post-digestive transformation",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Rasa (Tastes)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = NaturalBackground,
                border = BorderStroke(1.dp, NaturalCardBorder)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "RASA (TASTE PROFILE)",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalOliveMuted,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = medicine.dravyaguna.rasa.joinToString(" • "),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NaturalTextHeading
                    )
                }
            }

            if (medicine.dravyaguna.guna.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = NaturalBackground,
                    border = BorderStroke(1.dp, NaturalCardBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "GUNA (PHYSICAL QUALITIES)",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalOliveMuted,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = medicine.dravyaguna.guna.joinToString(" • "),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NaturalTextHeading
                        )
                    }
                }
            }
        }
    }
}

/**
 * Metric card for Virya and Vipaka
 */
@Composable
private fun EnergeticCard(
    title: String,
    value: String,
    subtext: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = NaturalBackground,
        border = BorderStroke(1.dp, NaturalCardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalOliveMuted,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextHeading
            )
            Text(
                text = subtext,
                fontSize = 10.sp,
                color = NaturalOliveMuted
            )
        }
    }
}

/**
 * Dosage instructions, recommended Anupana vehicle, and cautions
 */
@Composable
private fun DosageAndTimingSection(medicine: AyurvedaMedicine) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = NaturalCardSurface,
        border = BorderStroke(1.dp, NaturalCardBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = NaturalEarthGold,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "DOSAGE & ADMINISTRATION (ANUPANA)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalEarthGold,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Standard Dose and Frequency
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = NaturalParchmentContainer.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, NaturalParchmentBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "STANDARD DOSE",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTerracotta,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = medicine.dosage.standardDose,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextHeading
                        )
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = NaturalSageContainer.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, NaturalSageBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "FREQUENCY & TIMING",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalMossDark,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = medicine.dosage.frequency,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextHeading
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Timing: ${medicine.dosage.timing}",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = NaturalTextPrimary,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Anupana (Carrier Vehicle) Callout Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = NaturalBackground,
                border = BorderStroke(1.dp, NaturalCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "🥛", fontSize = 20.sp)
                    Column {
                        Text(
                            text = "RECOMMENDED ANUPANA (VEHICLE)",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalOliveMuted,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = medicine.dosage.anupana,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NaturalTextHeading
                        )
                    }
                }
            }

            // Caution if present
            if (medicine.dosage.caution.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF8E1),
                    border = BorderStroke(1.dp, Color(0xFFFFE082))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFF57F17),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Caution: ${medicine.dosage.caution}",
                            fontSize = 11.5.sp,
                            color = Color(0xFF5D4037)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Itemized botanical ingredients list with Latin names and classical roles
 */
@Composable
private fun IngredientsRegistrySection(medicine: AyurvedaMedicine) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = NaturalCardSurface,
        border = BorderStroke(1.dp, NaturalCardBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = NaturalMossPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "BOTANICAL INGREDIENTS (${medicine.ingredients.size})",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalMossPrimary,
                        letterSpacing = 0.8.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = NaturalSageContainer
                ) {
                    Text(
                        text = "100% Herbal",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalMossDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (medicine.ingredients.isNotEmpty()) {
                medicine.ingredients.forEachIndexed { index, ing ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (ing.sanskritName.isNotBlank()) "${ing.name} (${ing.sanskritName})" else ing.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextHeading
                                )
                                if (ing.botanicalName.isNotBlank()) {
                                    Text(
                                        text = ing.botanicalName,
                                        fontSize = 11.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = NaturalEarthGold
                                    )
                                }
                            }

                            if (ing.partUsed.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = NaturalBackground,
                                    border = BorderStroke(1.dp, NaturalCardBorder)
                                ) {
                                    Text(
                                        text = ing.partUsed,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = NaturalOliveMuted,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        if (ing.classicalRole.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Classical Role: ${ing.classicalRole}",
                                fontSize = 11.sp,
                                color = NaturalOliveMuted
                            )
                        }

                        if (index < medicine.ingredients.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 8.dp),
                                thickness = 0.5.dp,
                                color = NaturalCardBorder
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Full classical botanical recipe prepared according to standard Ayurvedic Formulary of India (AFI) protocols.",
                    fontSize = 12.sp,
                    color = NaturalOliveMuted
                )
            }
        }
    }
}

/**
 * Dietary and lifestyle guidance (Pathya and Apathya)
 */
@Composable
private fun DietaryGuidanceSection(medicine: AyurvedaMedicine) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = NaturalCardSurface,
        border = BorderStroke(1.dp, NaturalCardBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "DIET & LIFESTYLE HARMONIZATION",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalOliveMuted,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Wholesome Foods (Pathya)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = NaturalSageContainer.copy(alpha = 0.45f),
                border = BorderStroke(1.dp, NaturalSageBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "🌿 WHOLESOME REGIMEN (PATHYA)",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalMossDark,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (medicine.pathyaWholesome.isNotEmpty()) {
                            medicine.pathyaWholesome.joinToString(" • ")
                        } else {
                            "Warm seasoned moong soup, steamed leafy vegetables, cumin water, adequate rest, and balanced circadian rhythm."
                        },
                        fontSize = 12.sp,
                        color = NaturalTextPrimary,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Foods to Avoid (Apathya)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFDECEA).copy(alpha = 0.6f),
                border = BorderStroke(1.dp, Color(0xFFF5C6CB))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "🚫 SUBSTANCES TO AVOID (APATHYA)",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (medicine.apathyaAvoid.isNotEmpty()) {
                            medicine.apathyaAvoid.joinToString(" • ")
                        } else {
                            "Excessive pungent, sour, deeply fried foods, refrigerated iced water, late night heavy meals, and emotional stress."
                        },
                        fontSize = 12.sp,
                        color = NaturalTextPrimary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

/**
 * Pinned Bottom Action Bar for catalogue navigation and reference
 */
@Composable
private fun MedicineDetailBottomBar(
    medicine: AyurvedaMedicine,
    onNavigateBack: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = NaturalCardSurface,
        border = BorderStroke(1.dp, NaturalCardBorder),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("detail_window_bottom_back_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NaturalMossPrimary)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Return to Medicine Catalogue",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
