package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AyurvedaMedicine
import com.example.data.model.DoshaType
import com.example.data.model.FormulationCategory
import com.example.ui.AyurvedaUiState
import com.example.ui.components.CatalogueFilterBottomSheet
import com.example.ui.components.CatalogueSortBottomSheet
import com.example.ui.components.ProductPhotoView
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

enum class CatalogueSortOrder(val displayName: String, val subtitle: String = "") {
    NAME_ASC("Name (A to Z)", "Alphabetical order by trade name"),
    NAME_DESC("Name (Z to A)", "Reverse alphabetical order"),
    CATEGORY("Category", "Grouped by classical formulation class")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CatalogueScreen(
    uiState: AyurvedaUiState,
    onCategorySelected: (FormulationCategory) -> Unit,
    onDoshaSelected: (DoshaType?) -> Unit,
    onSelectMedicine: (AyurvedaMedicine) -> Unit,
    onRefresh: () -> Unit = {},
    onSearchSubmitted: (String) -> Unit = {},
    onRemoveSearchHistoryItem: (String) -> Unit = {},
    onClearSearchHistory: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var sortOrder by remember { mutableStateOf(CatalogueSortOrder.NAME_ASC) }
    var localSearchFilter by remember { mutableStateOf("") }
    var selectedHerbFilter by remember { mutableStateOf<String?>(null) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showSortSheet by remember { mutableStateOf(false) }

    val activeSearch = if (uiState.searchQuery.isNotEmpty()) uiState.searchQuery else localSearchFilter

    // Multi-criteria filter pipeline focusing purely on classical therapeutics
    val filteredMedicines = uiState.allMedicines.filter { med ->
        val matchesSearch = activeSearch.isEmpty() ||
                med.name.contains(activeSearch, ignoreCase = true) ||
                med.sanskritName.contains(activeSearch, ignoreCase = true) ||
                med.primaryBenefit.contains(activeSearch, ignoreCase = true) ||
                med.tagPill.contains(activeSearch, ignoreCase = true) ||
                med.indications.any { it.contains(activeSearch, ignoreCase = true) } ||
                med.ingredients.any { 
                    it.name.contains(activeSearch, ignoreCase = true) || 
                    it.botanicalName.contains(activeSearch, ignoreCase = true) 
                }

        val matchesCategory = uiState.selectedCategory == FormulationCategory.ALL ||
                med.category == uiState.selectedCategory

        val matchesDosha = uiState.selectedDosha == null ||
                med.targetDoshas.contains(uiState.selectedDosha) ||
                med.targetDoshas.contains(DoshaType.TRIDOSHIC)

        val matchesHerb = selectedHerbFilter == null || med.ingredients.any {
            it.name.contains(selectedHerbFilter!!, ignoreCase = true) ||
            it.botanicalName.contains(selectedHerbFilter!!, ignoreCase = true)
        }

        matchesSearch && matchesCategory && matchesDosha && matchesHerb
    }.let { list ->
        when (sortOrder) {
            CatalogueSortOrder.NAME_ASC -> list.sortedBy { it.name }
            CatalogueSortOrder.NAME_DESC -> list.sortedByDescending { it.name }
            CatalogueSortOrder.CATEGORY -> list.sortedBy { it.category.displayName }
        }
    }

    val shimmerBrush = rememberNaturalShimmerBrush()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBackground)
    ) {
        // 1. Refined Classical Catalogue Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NaturalCardSurface,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = "THERAPEUTIC INDEX HANDBOOK",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp,
                            color = NaturalEarthGold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Classical Medicine Catalogue",
                            fontFamily = FontFamily.Serif,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextHeading,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Sync / Refresh Button
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NaturalSageContainer)
                            .testTag("catalogue_refresh_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync from Database",
                            tint = NaturalMossDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fast In-Catalogue Search Field
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, NaturalCardBorder, RoundedCornerShape(12.dp)),
                    color = NaturalBackground
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = NaturalOliveMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (localSearchFilter.isEmpty() && uiState.searchQuery.isEmpty()) {
                                Text(
                                    text = "Search by medicine, Sanskrit, herb or indication...",
                                    fontSize = 12.sp,
                                    color = NaturalOliveMuted.copy(alpha = 0.75f)
                                )
                            }
                            BasicTextField(
                                value = localSearchFilter,
                                onValueChange = { localSearchFilter = it },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = {
                                    if (localSearchFilter.isNotBlank()) {
                                        onSearchSubmitted(localSearchFilter)
                                    }
                                }),
                                textStyle = TextStyle(
                                    fontSize = 12.sp,
                                    color = NaturalTextPrimary,
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("catalogue_local_search_input")
                            )
                        }
                        if (localSearchFilter.isNotEmpty()) {
                            IconButton(
                                onClick = { localSearchFilter = "" },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    tint = NaturalOliveMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                // Recent Search History Chips in Catalogue
                if (uiState.searchHistory.isNotEmpty()) {
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
                            color = NaturalOliveMuted
                        )
                        uiState.searchHistory.forEach { query ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NaturalBackground,
                                border = BorderStroke(1.dp, NaturalCardBorder),
                                modifier = Modifier
                                    .clickable {
                                        localSearchFilter = query
                                        onSearchSubmitted(query)
                                    }
                                    .testTag("catalogue_recent_$query")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = NaturalMossPrimary,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = query,
                                        fontSize = 10.sp,
                                        color = NaturalTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove $query",
                                        tint = NaturalOliveMuted.copy(alpha = 0.6f),
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clickable { onRemoveSearchHistoryItem(query) }
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Clear",
                            fontSize = 9.5.sp,
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

        // 2. Filter and Sort By Primary Buttons
        val activeFilterCount = (if (uiState.selectedCategory != FormulationCategory.ALL) 1 else 0) +
                (if (uiState.selectedDosha != null) 1 else 0) +
                (if (selectedHerbFilter != null) 1 else 0)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // FILTER BUTTON
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showFilterSheet = true }
                    .testTag("catalogue_filter_button"),
                color = if (activeFilterCount > 0) NaturalSageContainer else NaturalCardSurface,
                border = BorderStroke(
                    1.dp,
                    if (activeFilterCount > 0) NaturalMossPrimary else NaturalCardBorder
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = if (activeFilterCount > 0) NaturalMossPrimary else NaturalOliveMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Filter",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeFilterCount > 0) NaturalMossDark else NaturalTextPrimary
                    )
                    if (activeFilterCount > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(NaturalMossPrimary)
                                .size(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = activeFilterCount.toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // SORT BY BUTTON
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showSortSheet = true }
                    .testTag("catalogue_sort_button"),
                color = NaturalCardSurface,
                border = BorderStroke(1.dp, NaturalCardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f, fill = false),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⇅",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalMossPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "SORT BY",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalOliveMuted,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = sortOrder.displayName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NaturalTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Sort",
                        tint = NaturalOliveMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Active Filter Badges Bar
        if (activeFilterCount > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalOliveMuted
                )

                if (uiState.selectedCategory != FormulationCategory.ALL) {
                    ActiveFilterChip(
                        label = uiState.selectedCategory.displayName,
                        onDismiss = { onCategorySelected(FormulationCategory.ALL) }
                    )
                }

                if (uiState.selectedDosha != null) {
                    ActiveFilterChip(
                        label = "${uiState.selectedDosha!!.symbol} ${uiState.selectedDosha!!.displayName}",
                        onDismiss = { onDoshaSelected(null) }
                    )
                }

                if (selectedHerbFilter != null) {
                    ActiveFilterChip(
                        label = "🌱 $selectedHerbFilter",
                        onDismiss = { selectedHerbFilter = null }
                    )
                }

                Text(
                    text = "Reset all",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTerracotta,
                    modifier = Modifier
                        .clickable {
                            onCategorySelected(FormulationCategory.ALL)
                            onDoshaSelected(null)
                            selectedHerbFilter = null
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }

        // 3. Status Bar: Results count & Registry action
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (uiState.isCatalogueLoading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val infiniteTransition = rememberInfiniteTransition(label = "PulsingLeaf")
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 0.85f,
                        targetValue = 1.15f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "PulseScale"
                    )
                    Text(
                        text = "🌿",
                        fontSize = 12.sp,
                        modifier = Modifier.scale(pulseScale)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SYNCING SITARAM PHARMACOPEIA...",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp,
                        color = NaturalMossPrimary
                    )
                }

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(14.dp)
                        .testTag("catalogue_loading_spinner"),
                    color = NaturalMossPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filteredMedicines.size} FORMULATIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp,
                        color = NaturalOliveMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (selectedHerbFilter != null || localSearchFilter.isNotEmpty() || uiState.selectedCategory != FormulationCategory.ALL || uiState.selectedDosha != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• FILTERED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NaturalTerracotta,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // 5. Crossfade between Skeleton Loading and Filtered Formulations
        Crossfade(
            targetState = uiState.isCatalogueLoading,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            label = "CatalogueContentCrossfade",
            modifier = Modifier.fillMaxSize()
        ) { loading ->
            if (loading) {
                // Subtle Shimmer Skeleton Loading Cards
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .testTag("catalogue_skeleton_list"),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(3) { index ->
                        MedicineCatalogueSkeletonCard(
                            shimmerBrush = shimmerBrush,
                            modifier = Modifier.testTag("catalogue_skeleton_card_$index")
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            } else if (filteredMedicines.isEmpty()) {
                // Clean Empty State with 1-click reset
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .border(1.dp, NaturalCardBorder, RoundedCornerShape(22.dp)),
                        color = NaturalCardSurface
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🍃", fontSize = 34.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Formulations Found",
                                fontFamily = FontFamily.Serif,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextHeading
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "No classical medicine matches the active search term or category filters.",
                                fontSize = 11.5.sp,
                                color = NaturalOliveMuted,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            FilledTonalButton(
                                onClick = {
                                    onCategorySelected(FormulationCategory.ALL)
                                    onDoshaSelected(null)
                                    localSearchFilter = ""
                                    selectedHerbFilter = null
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = NaturalSageContainer,
                                    contentColor = NaturalMossDark
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Reset All Filters",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                // Formulations Monograph Catalogue
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("catalogue_medicine_list"),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredMedicines, key = { it.id }) { med ->
                        MedicineCatalogueCard(
                            medicine = med,
                            onClick = { onSelectMedicine(med) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    // Modal Bottom Sheets for Sort and Filter
    if (showSortSheet) {
        CatalogueSortBottomSheet(
            currentSortOrder = sortOrder,
            onSortOrderSelected = { sortOrder = it },
            onDismiss = { showSortSheet = false }
        )
    }

    if (showFilterSheet) {
        val categoryCounts = remember(uiState.allMedicines) {
            FormulationCategory.values().associateWith { cat ->
                if (cat == FormulationCategory.ALL) uiState.allMedicines.size
                else uiState.allMedicines.count { it.category == cat }
            }
        }

        CatalogueFilterBottomSheet(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = onCategorySelected,
            selectedDosha = uiState.selectedDosha,
            onDoshaSelected = onDoshaSelected,
            selectedHerbFilter = selectedHerbFilter,
            onHerbFilterSelected = { selectedHerbFilter = it },
            totalResultsCount = filteredMedicines.size,
            categoryCounts = categoryCounts,
            onResetAllFilters = {
                onCategorySelected(FormulationCategory.ALL)
                onDoshaSelected(null)
                selectedHerbFilter = null
            },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@Composable
private fun ActiveFilterChip(
    label: String,
    onDismiss: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = NaturalSageContainer,
        border = BorderStroke(1.dp, NaturalSageBorder),
        modifier = Modifier.clip(RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = NaturalMossDark
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove $label",
                tint = NaturalOliveMuted,
                modifier = Modifier
                    .size(11.dp)
                    .clickable(onClick = onDismiss)
            )
        }
    }
}

// -------------------------------------------------------------
// High-Polish Medicine Monograph Card
// -------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MedicineCatalogueCard(
    medicine: AyurvedaMedicine,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = androidx.compose.runtime.remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.018f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "catalogue_card_hover_scale"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .hoverable(interactionSource)
            .clip(RoundedCornerShape(18.dp))
            .border(
                1.dp,
                if (isHovered) NaturalEarthGold.copy(alpha = 0.6f) else NaturalCardBorder,
                RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .testTag("medicine_card_${medicine.id}"),
        color = NaturalCardSurface,
        shadowElevation = if (isHovered) 4.dp else 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // High-Resolution Product Photo with Packing & Category Badges
            ProductPhotoView(
                medicine = medicine,
                aspectRatio = 2.4f,
                showPackingBadge = true,
                showReferenceBadge = true,
                showCategoryBadge = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Row 1: Sanskrit Title, Category Badge & Tag Pill
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
                        letterSpacing = 0.5.sp
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

                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NaturalParchmentContainer)
                            .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = medicine.category.displayName,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTerracotta,
                            letterSpacing = 0.4.sp
                        )
                    }

                    if (medicine.tagPill.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = medicine.tagPill,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NaturalOliveMuted,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Handbook Specifications Box: Classical Reference & Usage
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NaturalParchmentContainer.copy(alpha = 0.6f))
                    .border(1.dp, NaturalParchmentBorder, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "REFERENCE: ${medicine.effectiveReference}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalMossDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "USAGE: ${medicine.effectiveUsage}",
                        fontSize = 8.5.sp,
                        color = NaturalOliveMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NaturalMossDark)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = medicine.effectivePacking,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalEarthGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Short Monograph Description
            Text(
                text = medicine.shortDescription.ifBlank { medicine.primaryBenefit },
                fontSize = 11.sp,
                color = NaturalTextPrimary.copy(alpha = 0.88f),
                lineHeight = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Main Ingredients Text from Handbook
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NaturalBackground)
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Main Ingredients: ",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalOliveMuted
                )
                Text(
                    text = medicine.effectiveMainIngredientsText,
                    fontSize = 9.5.sp,
                    color = NaturalTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Row: Monograph Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onClick,
                    shape = RoundedCornerShape(10.dp),
                    color = NaturalMossPrimary,
                    shadowElevation = 1.dp,
                    modifier = Modifier.testTag("btn_view_monograph_${medicine.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "View Monograph",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Shimmer Skeleton Loading Card
// -------------------------------------------------------------

@Composable
fun MedicineCatalogueSkeletonCard(
    shimmerBrush: Brush,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, NaturalCardBorder, RoundedCornerShape(22.dp)),
        color = NaturalCardSurface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .width(85.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(shimmerBrush)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(170.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(shimmerBrush)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(shimmerBrush)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .height(11.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(shimmerBrush)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.70f)
                    .height(11.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(shimmerBrush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(shimmerBrush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(shimmerBrush)
                )

                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(shimmerBrush)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// Natural Tones Shimmer Helper
// -------------------------------------------------------------

@Composable
fun rememberNaturalShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "NaturalShimmerTransition")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "NaturalShimmerOffset"
    )

    return Brush.linearGradient(
        colors = listOf(
            NaturalCardSurface,
            NaturalSageContainer.copy(alpha = 0.55f),
            NaturalParchmentContainer.copy(alpha = 0.70f),
            NaturalCardSurface
        ),
        start = Offset(translateAnimation - 400f, translateAnimation - 400f),
        end = Offset(translateAnimation, translateAnimation)
    )
}
