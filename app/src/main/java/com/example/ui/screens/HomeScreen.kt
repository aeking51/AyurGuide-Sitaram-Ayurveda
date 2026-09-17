package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AyurvedaMedicine
import com.example.data.model.DoshaType
import com.example.data.model.FormulationCategory
import com.example.data.model.UserRole
import com.example.ui.AyurvedaUiState
import com.example.ui.components.GuestLimitedAccessBanner
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
import com.example.ui.theme.NaturalVataViolet

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    uiState: AyurvedaUiState,
    onSelectMedicine: (AyurvedaMedicine) -> Unit,
    onCategorySelected: (FormulationCategory) -> Unit = {},
    onNavigateToLibrary: () -> Unit = {},
    // Optional legacy callbacks maintained for API compatibility
    onLogVitalityDose: () -> Unit = {},
    onToggleHabit: (String) -> Unit = {},
    onToggleDose: (String) -> Unit = {},
    onAddHydration: (Float) -> Unit = {},
    onNavigateToInsights: () -> Unit = {},
    onPromptSignIn: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val featuredMed = uiState.dailyVitalityMedicine

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Guest Access Limited Mode Notice
        if (uiState.currentUser.role == UserRole.GUEST) {
            item {
                GuestLimitedAccessBanner(
                    onSignInClick = onPromptSignIn,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // 1. Classical Welcome & Heritage Header
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, NaturalCardBorder, RoundedCornerShape(20.dp)),
                color = NaturalCardSurface
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "SITARAM AYURVEDA PHARMACOPEIA",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.3.sp,
                                color = NaturalEarthGold
                            )
                            Text(
                                text = "Therapeutic Index",
                                fontFamily = FontFamily.Serif,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextHeading
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NaturalSageContainer)
                                .border(1.dp, NaturalSageBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "AFI Formulations",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalMossDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Authentic classical medicine handbook based on Charaka Samhita, Ashtanga Hridaya, and Sahasrayogam standards.",
                        fontSize = 12.sp,
                        color = NaturalOliveMuted,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Stats Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NaturalBackground)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${uiState.allMedicines.size}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalMossPrimary
                            )
                            Text(
                                text = "Formulations",
                                fontSize = 9.5.sp,
                                color = NaturalOliveMuted
                            )
                        }

                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(NaturalCardBorder))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${FormulationCategory.values().size - 1}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalEarthGold
                            )
                            Text(
                                text = "Categories",
                                fontSize = 9.5.sp,
                                color = NaturalOliveMuted
                            )
                        }

                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(NaturalCardBorder))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val inStockCount = uiState.allMedicines.count { it.stockUnits > 0 }
                            Text(
                                text = "$inStockCount",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalPittaGreen
                            )
                            Text(
                                text = "In Dispensary",
                                fontSize = 9.5.sp,
                                color = NaturalOliveMuted
                            )
                        }
                    }
                }
            }
        }

        // 2. Featured Classical Formulation Spotlight (Hero Card)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(24.dp))
                    .clickable { onSelectMedicine(featuredMed) }
                    .testTag("featured_spotlight_card"),
                color = NaturalParchmentContainer,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "✦ FORMULATION SPOTLIGHT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.2.sp,
                                color = NaturalTerracotta
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = featuredMed.sanskritName,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalEarthGold
                            )
                            Text(
                                text = featuredMed.name,
                                fontFamily = FontFamily.Serif,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextHeading
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NaturalCardSurface)
                                .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = featuredMed.category.displayName,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalMossDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = featuredMed.shortDescription,
                        fontSize = 12.5.sp,
                        color = NaturalTextPrimary.copy(alpha = 0.9f),
                        lineHeight = 17.5.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dravyaguna & Classical Ref preview
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NaturalCardSurface.copy(alpha = 0.7f))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dosha: ${featuredMed.doshaImpact.ifBlank { "Tridoshic" }}",
                            fontSize = 10.sp,
                            fontStyle = FontStyle.Italic,
                            color = NaturalOliveMuted
                        )

                        Text(
                            text = "Virya: ${featuredMed.dravyaguna.virya}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalMossDark
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bottom Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Packing: ${featuredMed.effectivePacking}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NaturalOliveMuted
                        )

                        Button(
                            onClick = { onSelectMedicine(featuredMed) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NaturalMossPrimary),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("hero_view_monograph_btn")
                        ) {
                            Text(
                                text = "View Monograph",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 3. Classical Categories Showcase (From Handbook Structure)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXPLORE BY CLASSICAL CATEGORY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NaturalOliveMuted
                    )

                    TextButton(
                        onClick = onNavigateToLibrary,
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Full Catalogue",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalMossPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NaturalMossPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Horizontal Carousel of Major Classical Categories
                val majorCategories: List<Pair<FormulationCategory, String>> = listOf(
                    FormulationCategory.ARISHTA to "🍷 Fermented Tonics",
                    FormulationCategory.KWATHA to "🍵 Classical Decoctions",
                    FormulationCategory.TAILA to "🌿 Medicated Tailams",
                    FormulationCategory.GHRITA to "🧈 Medicated Ghee",
                    FormulationCategory.CHURNA to "🌾 Herbal Choornams",
                    FormulationCategory.VATI to "💊 Classical Pills",
                    FormulationCategory.RASAYANA to "🍯 Confections"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(majorCategories) { item ->
                        val cat = item.first
                        val desc = item.second
                        val count = uiState.allMedicines.count { it.category == cat }
                        Surface(
                            modifier = Modifier
                                .width(150.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, NaturalCardBorder, RoundedCornerShape(16.dp))
                                .clickable {
                                    onCategorySelected(cat)
                                    onNavigateToLibrary()
                                },
                            color = NaturalCardSurface
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = cat.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextHeading,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = desc,
                                    fontSize = 10.sp,
                                    color = NaturalOliveMuted,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "$count Items",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalMossDark
                                    )
                                    Text(
                                        text = "Explore →",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = NaturalMossPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Curated Classical Remedies
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FEATURED PHARMACOPEIA REMEDIES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NaturalOliveMuted
                    )

                    Text(
                        text = "${uiState.allMedicines.size} Formulations",
                        fontSize = 10.5.sp,
                        color = NaturalOliveMuted
                    )
                }

                // Grid/List of Curated Formulations
                uiState.allMedicines.take(6).forEach { med ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, NaturalCardBorder, RoundedCornerShape(18.dp))
                            .clickable { onSelectMedicine(med) }
                            .testTag("home_medicine_card_${med.id}"),
                        color = NaturalCardSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = med.sanskritName,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalEarthGold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = med.name,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextHeading
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = med.primaryBenefit,
                                    fontSize = 11.sp,
                                    color = NaturalOliveMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(horizontalAlignment = Alignment.End) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(NaturalSageContainer)
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = med.category.displayName,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalMossDark
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "View Monograph",
                                    tint = NaturalMossPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Standards & Dispensary Quality Footer Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, NaturalCardBorder, RoundedCornerShape(20.dp)),
                color = NaturalCardSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(NaturalSageContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = NaturalMossPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ayurvedic Formulary of India (AFI)",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextHeading
                        )
                        Text(
                            text = "All medicines prepared strictly as per classical texts with verified botanical parts and authentic Kerala processing.",
                            fontSize = 10.5.sp,
                            color = NaturalOliveMuted,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}
