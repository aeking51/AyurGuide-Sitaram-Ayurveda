package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Info
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
import com.example.data.model.DoshaType
import com.example.data.model.FormulationCategory
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
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageContainer
import com.example.ui.theme.NaturalTerracotta
import com.example.ui.theme.NaturalTextHeading
import com.example.ui.theme.NaturalTextPrimary

@Composable
fun CatalogueScreen(
    uiState: AyurvedaUiState,
    onCategorySelected: (FormulationCategory) -> Unit,
    onDoshaSelected: (DoshaType?) -> Unit,
    onSelectMedicine: (AyurvedaMedicine) -> Unit,
    onAddToRoutine: (AyurvedaMedicine) -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter medicines by search, category, and dosha
    val filteredMedicines = uiState.allMedicines.filter { med ->
        val matchesSearch = uiState.searchQuery.isEmpty() ||
                med.name.contains(uiState.searchQuery, ignoreCase = true) ||
                med.sanskritName.contains(uiState.searchQuery, ignoreCase = true) ||
                med.primaryBenefit.contains(uiState.searchQuery, ignoreCase = true) ||
                med.ingredients.any { it.name.contains(uiState.searchQuery, ignoreCase = true) || it.botanicalName.contains(uiState.searchQuery, ignoreCase = true) }

        val matchesCategory = uiState.selectedCategory == FormulationCategory.ALL ||
                med.category == uiState.selectedCategory

        val matchesDosha = uiState.selectedDosha == null ||
                med.targetDoshas.contains(uiState.selectedDosha) ||
                med.targetDoshas.contains(DoshaType.TRIDOSHIC)

        matchesSearch && matchesCategory && matchesDosha
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBackground)
    ) {
        // Formulation Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FormulationCategory.values().forEach { cat ->
                val isSelected = uiState.selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) NaturalMossPrimary else NaturalCardSurface)
                        .border(1.dp, if (isSelected) NaturalMossPrimary else NaturalCardBorder, RoundedCornerShape(16.dp))
                        .clickable { onCategorySelected(cat) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                        .testTag("filter_cat_${cat.name}")
                ) {
                    Text(
                        text = cat.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else NaturalTextPrimary
                    )
                }
            }
        }

        // Dosha Target Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val doshaFilters = listOf(null to "All Doshas") + DoshaType.values().map { it to "${it.symbol} ${it.displayName}" }
            doshaFilters.forEach { (dosha, label) ->
                val isSelected = uiState.selectedDosha == dosha
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) NaturalSageContainer else Color.Transparent)
                        .border(1.dp, if (isSelected) NaturalSageBorder else NaturalCardBorder.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .clickable { onDoshaSelected(dosha) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) NaturalMossDark else NaturalOliveMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Results count label
        Text(
            text = "${filteredMedicines.size} CLASSICAL FORMULATIONS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = NaturalOliveMuted,
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 4.dp)
        )

        // Medicine Catalogue List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredMedicines, key = { it.id }) { med ->
                MedicineCatalogueCard(
                    medicine = med,
                    onClick = { onSelectMedicine(med) },
                    onAddToRoutine = { onAddToRoutine(med) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun MedicineCatalogueCard(
    medicine: AyurvedaMedicine,
    onClick: () -> Unit,
    onAddToRoutine: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, NaturalCardBorder, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .testTag("medicine_card_${medicine.id}"),
        color = NaturalCardSurface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Sanskrit & Tag Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medicine.sanskritName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalEarthGold,
                        letterSpacing = 0.6.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = medicine.name,
                        fontFamily = FontFamily.Serif,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextHeading
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NaturalParchmentContainer)
                        .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = medicine.tagPill,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTerracotta,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = medicine.shortDescription,
                fontSize = 11.sp,
                color = NaturalTextPrimary.copy(alpha = 0.85f),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Key Ingredients preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NaturalBackground)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Key Herbs: ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalOliveMuted
                )
                Text(
                    text = medicine.ingredients.take(3).joinToString(", ") { it.name },
                    fontSize = 10.sp,
                    color = NaturalTextPrimary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom row: Dosage summary & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🥄", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = medicine.dosage.summary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NaturalTerracotta
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = onAddToRoutine,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(NaturalSageContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkAdd,
                            contentDescription = "Add to routine",
                            tint = NaturalMossPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onClick,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(NaturalParchmentContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "View Details",
                            tint = NaturalTerracotta,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
