package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppUser
import com.example.data.model.AuditLogEntry
import com.example.data.model.AyurvedaIngredient
import com.example.data.model.AyurvedaMedicine
import com.example.data.model.DosageInfo
import com.example.data.model.DoshaType
import com.example.data.model.DravyagunaProfile
import com.example.data.model.FormulationCategory
import com.example.data.model.UserRole
import com.example.data.model.UserStatus
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

enum class AdminTab(val title: String, val icon: String) {
    OVERVIEW("Overview", "📊"),
    INVENTORY("Formulations", "🌿"),
    USERS("User Directory", "👥"),
    AUDIT("Safety & Audits", "🛡️")
}

@Composable
fun AdminDashboardScreen(
    uiState: AyurvedaUiState,
    onSwitchUser: (AppUser) -> Unit,
    onUpdateUserRole: (String, UserRole) -> Unit,
    onUpdateUserStatus: (String, UserStatus) -> Unit,
    onAddNewUser: (AppUser) -> Unit,
    onSetUserRoleFilter: (UserRole?) -> Unit,
    onSelectUserForDetail: (AppUser?) -> Unit,
    onOpenAddMedicineDialog: (Boolean) -> Unit,
    onOpenAddUserDialog: (Boolean) -> Unit,
    onOpenSwitchUserDialog: (Boolean) -> Unit,
    onAddNewMedicine: (AyurvedaMedicine) -> Unit,
    onUpdateStock: (String, Int) -> Unit,
    onToggleVitality: (String) -> Unit,
    onDeleteMedicine: (String) -> Unit,
    onSelectMedicine: (AyurvedaMedicine) -> Unit,
    onPrescribeToPatient: (AyurvedaMedicine, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentAdminTab by remember { mutableStateOf(AdminTab.OVERVIEW) }
    val currentUser = uiState.currentUser
    val isChiefAdmin = currentUser.role == UserRole.ADMIN

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Admin Banner & Active User Quick Switcher
        item {
            AdminHeaderCard(
                currentUser = currentUser,
                onSwitchProfileClicked = { onOpenSwitchUserDialog(true) }
            )
        }

        // 2. Sub-Tab Selector
        item {
            AdminSubTabRow(
                selectedTab = currentAdminTab,
                onTabSelected = { currentAdminTab = it }
            )
        }

        // 3. Tab Contents
        when (currentAdminTab) {
            AdminTab.OVERVIEW -> {
                item {
                    AdminKpiSection(
                        totalMedicines = uiState.allMedicines.size,
                        lowStockCount = uiState.allMedicines.count { it.stockUnits < 15 },
                        totalUsers = uiState.allUsers.size,
                        activePatients = uiState.allUsers.count { it.role == UserRole.PATIENT && it.status == UserStatus.ACTIVE },
                        auditLogCount = uiState.auditLogs.size
                    )
                }

                item {
                    DoshaCohortDistributionCard(users = uiState.allUsers)
                }

                item {
                    QuickActionAdminRow(
                        onAddFormulation = { onOpenAddMedicineDialog(true) },
                        onAddUser = { onOpenAddUserDialog(true) },
                        onViewLogs = { currentAdminTab = AdminTab.AUDIT }
                    )
                }

                item {
                    RecentAuditPreviewCard(
                        logs = uiState.auditLogs.take(3),
                        onViewAll = { currentAdminTab = AdminTab.AUDIT }
                    )
                }
            }

            AdminTab.INVENTORY -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CLASSICAL FORMULATION INVENTORY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.3.sp,
                                color = NaturalOliveMuted
                            )
                            Text(
                                text = "${uiState.allMedicines.size} Classical Remedies Managed",
                                fontSize = 12.sp,
                                color = NaturalTextPrimary.copy(alpha = 0.7f)
                            )
                        }

                        Button(
                            onClick = { onOpenAddMedicineDialog(true) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NaturalMossPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("admin_add_herb_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Add Herb", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                items(uiState.allMedicines, key = { it.id }) { medicine ->
                    AdminMedicineCard(
                        medicine = medicine,
                        isChiefAdmin = isChiefAdmin,
                        onUpdateStock = { delta -> onUpdateStock(medicine.id, medicine.stockUnits + delta) },
                        onToggleVitality = { onToggleVitality(medicine.id) },
                        onDelete = { onDeleteMedicine(medicine.id) },
                        onCardClick = { onSelectMedicine(medicine) }
                    )
                }
            }

            AdminTab.USERS -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ROLE-BASED USER DIRECTORY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.3.sp,
                                color = NaturalOliveMuted
                            )
                            Text(
                                text = "${uiState.allUsers.size} Registered Accounts",
                                fontSize = 12.sp,
                                color = NaturalTextPrimary.copy(alpha = 0.7f)
                            )
                        }

                        Button(
                            onClick = { onOpenAddUserDialog(true) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NaturalMossPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("admin_add_user_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ New User", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                item {
                    RoleFilterRow(
                        selectedRole = uiState.userRoleFilter,
                        onSelectRole = onSetUserRoleFilter,
                        users = uiState.allUsers
                    )
                }

                val filteredUsers = uiState.allUsers.filter {
                    uiState.userRoleFilter == null || it.role == uiState.userRoleFilter
                }

                items(filteredUsers, key = { it.id }) { user ->
                    AdminUserCard(
                        user = user,
                        isCurrentUser = user.id == currentUser.id,
                        onCardClick = { onSelectUserForDetail(user) },
                        onSwitchToUser = { onSwitchUser(user) },
                        onPromoteRole = { newRole -> onUpdateUserRole(user.id, newRole) },
                        onToggleStatus = { newStatus -> onUpdateUserStatus(user.id, newStatus) }
                    )
                }
            }

            AdminTab.AUDIT -> {
                item {
                    Column {
                        Text(
                            text = "PHARMACOPOEIA SAFETY & AUDIT LOGS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.3.sp,
                            color = NaturalOliveMuted
                        )
                        Text(
                            text = "Verifiable ledger of batch validations, roles & clinical alerts",
                            fontSize = 12.sp,
                            color = NaturalTextPrimary.copy(alpha = 0.7f)
                        )
                    }
                }

                items(uiState.auditLogs, key = { it.id }) { log ->
                    AuditLogRow(log = log)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialogs
    if (uiState.isSwitchUserDialogOpen) {
        SwitchUserDialog(
            currentUserId = currentUser.id,
            users = uiState.allUsers,
            onSelectUser = onSwitchUser,
            onDismiss = { onOpenSwitchUserDialog(false) }
        )
    }

    if (uiState.isAddMedicineDialogOpen) {
        AddMedicineDialog(
            onDismiss = { onOpenAddMedicineDialog(false) },
            onConfirmAdd = onAddNewMedicine
        )
    }

    if (uiState.isAddUserDialogOpen) {
        AddUserDialog(
            onDismiss = { onOpenAddUserDialog(false) },
            onConfirmAdd = onAddNewUser
        )
    }

    uiState.selectedUserForDetail?.let { user ->
        UserDetailRoleDialog(
            user = user,
            isChiefAdmin = isChiefAdmin,
            onDismiss = { onSelectUserForDetail(null) },
            onRoleChange = { newRole -> onUpdateUserRole(user.id, newRole) },
            onStatusChange = { newStatus -> onUpdateUserStatus(user.id, newStatus) },
            onSwitchToUser = {
                onSwitchUser(user)
                onSelectUserForDetail(null)
            }
        )
    }
}

// -------------------------------------------------------------
// UI Sub-components
// -------------------------------------------------------------

@Composable
private fun AdminHeaderCard(
    currentUser: AppUser,
    onSwitchProfileClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, NaturalCardBorder, RoundedCornerShape(24.dp)),
        color = NaturalCardSurface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                when (currentUser.role) {
                                    UserRole.ADMIN -> NaturalTerracotta.copy(alpha = 0.18f)
                                    UserRole.PRACTITIONER -> NaturalSageContainer
                                    UserRole.PATIENT -> NaturalParchmentContainer
                                }
                            )
                            .border(
                                1.5.dp,
                                when (currentUser.role) {
                                    UserRole.ADMIN -> NaturalTerracotta
                                    UserRole.PRACTITIONER -> NaturalMossPrimary
                                    UserRole.PATIENT -> NaturalOliveMuted
                                },
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.role.iconEmoji,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentUser.name,
                                fontFamily = FontFamily.Serif,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextHeading
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            RoleBadge(role = currentUser.role)
                        }

                        Text(
                            text = currentUser.designation.ifEmpty { currentUser.email },
                            fontSize = 11.sp,
                            color = NaturalOliveMuted
                        )
                    }
                }

                // Switch Role / User Button
                OutlinedButton(
                    onClick = onSwitchProfileClicked,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NaturalMossPrimary
                    ),
                    modifier = Modifier.testTag("admin_switch_user_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Switch user",
                        modifier = Modifier.size(14.dp),
                        tint = NaturalMossPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Switch",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NaturalBackground)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Access: ${currentUser.role.description}",
                        fontSize = 10.sp,
                        color = NaturalTextPrimary.copy(alpha = 0.8f),
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RoleBadge(role: UserRole) {
    val (bgColor, textColor, borderCol) = when (role) {
        UserRole.ADMIN -> Triple(
            NaturalTerracotta.copy(alpha = 0.15f),
            NaturalTerracotta,
            NaturalTerracotta.copy(alpha = 0.5f)
        )
        UserRole.PRACTITIONER -> Triple(
            NaturalSageContainer,
            NaturalMossDark,
            NaturalSageBorder
        )
        UserRole.PATIENT -> Triple(
            NaturalParchmentContainer,
            NaturalEarthGold,
            NaturalParchmentBorder
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderCol, RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "${role.iconEmoji} ${role.badgeLabel}",
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            letterSpacing = 0.4.sp
        )
    }
}

@Composable
fun StatusBadge(status: UserStatus) {
    val (bg, txt) = when (status) {
        UserStatus.ACTIVE -> Pair(NaturalSageContainer, NaturalMossDark)
        UserStatus.SUSPENDED -> Pair(NaturalTerracotta.copy(alpha = 0.15f), NaturalTerracotta)
        UserStatus.PENDING -> Pair(NaturalParchmentContainer, NaturalEarthGold)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = status.label.uppercase(),
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = txt,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun AdminSubTabRow(
    selectedTab: AdminTab,
    onTabSelected: (AdminTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AdminTab.values().forEach { tab ->
            val isSelected = tab == selectedTab
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        if (isSelected) NaturalMossPrimary else NaturalCardBorder,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onTabSelected(tab) }
                    .testTag("admin_tab_${tab.name.lowercase()}"),
                color = if (isSelected) NaturalMossPrimary else NaturalCardSurface
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = tab.icon, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tab.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else NaturalTextHeading
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminKpiSection(
    totalMedicines: Int,
    lowStockCount: Int,
    totalUsers: Int,
    activePatients: Int,
    auditLogCount: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Formulations KPI
            KpiCard(
                title = "FORMULATIONS",
                metric = "$totalMedicines",
                subtext = if (lowStockCount > 0) "$lowStockCount Low Stock" else "All In Stock",
                isWarning = lowStockCount > 0,
                icon = "📦",
                modifier = Modifier.weight(1f)
            )

            // Patient Roster KPI
            KpiCard(
                title = "ACTIVE PATIENTS",
                metric = "$activePatients",
                subtext = "of $totalUsers Total Users",
                isWarning = false,
                icon = "🌿",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Clinical Adherence KPI
            KpiCard(
                title = "ADHERENCE RATE",
                metric = "88%",
                subtext = "Classical Dinacharya",
                isWarning = false,
                icon = "🍵",
                modifier = Modifier.weight(1f)
            )

            // Safety Audit KPI
            KpiCard(
                title = "API COMPLIANCE",
                metric = "100%",
                subtext = "$auditLogCount Audit Entries",
                isWarning = false,
                icon = "🛡️",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    metric: String,
    subtext: String,
    isWarning: Boolean,
    icon: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(
                1.dp,
                if (isWarning) NaturalTerracotta.copy(alpha = 0.4f) else NaturalCardBorder,
                RoundedCornerShape(18.dp)
            ),
        color = NaturalCardSurface
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.1.sp,
                    color = NaturalOliveMuted
                )
                Text(text = icon, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = metric,
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalTextHeading
            )

            Text(
                text = subtext,
                fontSize = 10.sp,
                color = if (isWarning) NaturalTerracotta else NaturalMossDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DoshaCohortDistributionCard(users: List<AppUser>) {
    val patients = users.filter { it.role == UserRole.PATIENT }
    val total = if (patients.isNotEmpty()) patients.size else 1
    val pittaCount = patients.count { it.prakriti == DoshaType.PITTA }
    val vataCount = patients.count { it.prakriti == DoshaType.VATA }
    val kaphaCount = patients.count { it.prakriti == DoshaType.KAPHA }

    val pittaPct = (pittaCount * 100) / total
    val vataPct = (vataCount * 100) / total
    val kaphaPct = (kaphaCount * 100) / total

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, NaturalSageBorder, RoundedCornerShape(20.dp)),
        color = NaturalSageContainer.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CLINICAL DOSHA COHORT DEMOGRAPHICS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = NaturalMossPrimary
                )
                Text(
                    text = "${patients.size} Seekers",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalOliveMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pitta bar
            DoshaProgressBar(
                dosha = "Pitta (Digestive Fire & Agni)",
                count = pittaCount,
                percent = pittaPct,
                color = NaturalTerracotta,
                symbol = "🔥"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Vata bar
            DoshaProgressBar(
                dosha = "Vata (Nervous & Mobility)",
                count = vataCount,
                percent = vataPct,
                color = NaturalEarthGold,
                symbol = "💨"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Kapha bar
            DoshaProgressBar(
                dosha = "Kapha (Metabolism & Structure)",
                count = kaphaCount,
                percent = kaphaPct,
                color = NaturalMossPrimary,
                symbol = "💧"
            )
        }
    }
}

@Composable
private fun DoshaProgressBar(
    dosha: String,
    count: Int,
    percent: Int,
    color: Color,
    symbol: String
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$symbol $dosha",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = NaturalTextHeading
            )
            Text(
                text = "$count ($percent%)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = NaturalOliveMuted
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (percent / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color.White.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun QuickActionAdminRow(
    onAddFormulation: () -> Unit,
    onAddUser: () -> Unit,
    onViewLogs: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onAddFormulation,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = NaturalCardSurface,
                contentColor = NaturalMossPrimary
            )
        ) {
            Text("+ Herb", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = onAddUser,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = NaturalCardSurface,
                contentColor = NaturalMossPrimary
            )
        ) {
            Text("+ User", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = onViewLogs,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = NaturalCardSurface,
                contentColor = NaturalMossPrimary
            )
        ) {
            Text("🛡️ Audits", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun RecentAuditPreviewCard(
    logs: List<AuditLogEntry>,
    onViewAll: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, NaturalCardBorder, RoundedCornerShape(20.dp)),
        color = NaturalCardSurface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT REGULATORY & AUDIT EVENTS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = NaturalOliveMuted
                )

                Text(
                    text = "View All →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalMossPrimary,
                    modifier = Modifier.clickable(onClick = onViewAll)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            logs.forEach { log ->
                AuditLogRow(log = log, compact = true)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun AuditLogRow(log: AuditLogEntry, compact: Boolean = false) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (log.isWarning) NaturalTerracotta.copy(alpha = 0.3f) else NaturalCardBorder,
                RoundedCornerShape(14.dp)
            ),
        color = if (log.isWarning) NaturalTerracotta.copy(alpha = 0.05f) else NaturalBackground
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = if (log.isWarning) "⚠️" else "🛡️",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = log.targetItem,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextHeading
                    )
                    Text(
                        text = log.timestamp,
                        fontSize = 10.sp,
                        color = NaturalOliveMuted
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = log.details,
                    fontSize = 11.sp,
                    color = NaturalTextPrimary.copy(alpha = 0.85f),
                    lineHeight = 15.sp
                )

                if (!compact) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Actor: ${log.actorName} • Action: ${log.actionType}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = NaturalOliveMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminMedicineCard(
    medicine: AyurvedaMedicine,
    isChiefAdmin: Boolean,
    onUpdateStock: (Int) -> Unit,
    onToggleVitality: () -> Unit,
    onDelete: () -> Unit,
    onCardClick: () -> Unit
) {
    val isLowStock = medicine.stockUnits < 15

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                if (isLowStock) NaturalTerracotta.copy(alpha = 0.4f) else NaturalCardBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onCardClick),
        color = NaturalCardSurface
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medicine.sanskritName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalEarthGold
                    )
                    Text(
                        text = medicine.name,
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextHeading
                    )
                    Text(
                        text = "Batch: ${medicine.batchNumber} • ${medicine.category.displayName}",
                        fontSize = 10.sp,
                        color = NaturalOliveMuted
                    )
                }

                // Daily Vitality Featured Toggle
                IconButton(
                    onClick = onToggleVitality,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (medicine.isDailyVitality) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Toggle Vitality",
                        tint = if (medicine.isDailyVitality) NaturalEarthGold else NaturalOliveMuted
                    )
                }

                if (isChiefAdmin) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Archive formulation",
                            tint = NaturalOliveMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stock & Inventory Stepper
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NaturalBackground)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isLowStock) NaturalTerracotta.copy(alpha = 0.15f) else NaturalSageContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isLowStock) "⚠️ LOW: ${medicine.stockUnits} units" else "${medicine.stockUnits} units in stock",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLowStock) NaturalTerracotta else NaturalMossDark
                        )
                    }
                }

                // Stepper [-] [+]
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(NaturalCardSurface)
                            .border(1.dp, NaturalCardBorder, CircleShape)
                            .clickable { onUpdateStock(-5) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "-5", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NaturalTextHeading)
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(NaturalMossPrimary)
                            .clickable { onUpdateStock(+10) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "+10", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleFilterRow(
    selectedRole: UserRole?,
    onSelectRole: (UserRole?) -> Unit,
    users: List<AppUser>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FilterChip(
            selected = selectedRole == null,
            onClick = { onSelectRole(null) },
            label = { Text("All (${users.size})", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalMossPrimary,
                selectedLabelColor = Color.White
            )
        )

        UserRole.values().forEach { role ->
            val count = users.count { it.role == role }
            FilterChip(
                selected = selectedRole == role,
                onClick = { onSelectRole(if (selectedRole == role) null else role) },
                label = { Text("${role.iconEmoji} ${role.badgeLabel} ($count)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NaturalMossPrimary,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun AdminUserCard(
    user: AppUser,
    isCurrentUser: Boolean,
    onCardClick: () -> Unit,
    onSwitchToUser: () -> Unit,
    onPromoteRole: (UserRole) -> Unit,
    onToggleStatus: (UserStatus) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                if (isCurrentUser) NaturalMossPrimary else NaturalCardBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onCardClick)
            .testTag("user_card_${user.id}"),
        color = NaturalCardSurface
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(NaturalSageContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.name.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NaturalMossPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextHeading
                            )
                            if (isCurrentUser) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "(You)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalMossPrimary
                                )
                            }
                        }
                        Text(
                            text = user.email,
                            fontSize = 11.sp,
                            color = NaturalOliveMuted
                        )
                    }
                }

                RoleBadge(role = user.role)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Details line: Prakriti, Adherence, Status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NaturalBackground)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Constitution: ",
                        fontSize = 10.sp,
                        color = NaturalOliveMuted
                    )
                    Text(
                        text = "${user.prakriti.symbol} ${user.prakriti.displayName}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextHeading
                    )
                }

                StatusBadge(status = user.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Actions row: Quick Login As + Promote Role
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isCurrentUser) {
                    OutlinedButton(
                        onClick = onSwitchToUser,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = NaturalMossPrimary
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Login as ${user.name.split(" ").first()}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Button(
                    onClick = onCardClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NaturalSageContainer,
                        contentColor = NaturalMossDark
                    ),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Manage Role", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Interactive Dialogs
// -------------------------------------------------------------

@Composable
fun SwitchUserDialog(
    currentUserId: String,
    users: List<AppUser>,
    onSelectUser: (AppUser) -> Unit,
    onDismiss: () -> Unit,
    onLogout: (() -> Unit)? = null
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, NaturalCardBorder, RoundedCornerShape(24.dp)),
            color = NaturalCardSurface
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SWITCH USER PROFILE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = NaturalOliveMuted
                        )
                        Text(
                            text = "Test Role-Based Access Control",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextHeading
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalOliveMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                users.forEach { user ->
                    val isSelected = user.id == currentUserId
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                1.dp,
                                if (isSelected) NaturalMossPrimary else NaturalCardBorder,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { onSelectUser(user) },
                        color = if (isSelected) NaturalSageContainer.copy(alpha = 0.5f) else NaturalBackground
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = user.role.iconEmoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = user.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalTextHeading
                                    )
                                    Text(
                                        text = "${user.role.displayName} • ${user.prakriti.displayName}",
                                        fontSize = 10.sp,
                                        color = NaturalOliveMuted
                                    )
                                }
                            }

                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Current",
                                    tint = NaturalMossPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                if (onLogout != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = NaturalTerracotta
                        )
                    ) {
                        Text("Log Out of Sanctuary", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun UserDetailRoleDialog(
    user: AppUser,
    isChiefAdmin: Boolean,
    onDismiss: () -> Unit,
    onRoleChange: (UserRole) -> Unit,
    onStatusChange: (UserStatus) -> Unit,
    onSwitchToUser: () -> Unit
) {
    var selectedRole by remember { mutableStateOf(user.role) }
    var selectedStatus by remember { mutableStateOf(user.status) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, NaturalCardBorder, RoundedCornerShape(24.dp)),
            color = NaturalCardSurface
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "USER ACCESS & ROLE MANAGEMENT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp,
                            color = NaturalOliveMuted
                        )
                        Text(
                            text = user.name,
                            fontFamily = FontFamily.Serif,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextHeading
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalOliveMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ASSIGN ROLE:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalOliveMuted
                )

                Spacer(modifier = Modifier.height(6.dp))

                UserRole.values().forEach { role ->
                    val isChosen = selectedRole == role
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                if (isChosen) NaturalMossPrimary else NaturalCardBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                selectedRole = role
                                onRoleChange(role)
                            },
                        color = if (isChosen) NaturalSageContainer else NaturalBackground
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = role.iconEmoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = role.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextHeading
                                )
                                Text(
                                    text = role.description,
                                    fontSize = 9.sp,
                                    color = NaturalOliveMuted,
                                    lineHeight = 12.sp
                                )
                            }
                            if (isChosen) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = NaturalMossPrimary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ACCOUNT STATUS:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalOliveMuted
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UserStatus.values().forEach { status ->
                        val isChosen = selectedStatus == status
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    1.dp,
                                    if (isChosen) NaturalMossPrimary else NaturalCardBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedStatus = status
                                    onStatusChange(status)
                                },
                            color = if (isChosen) NaturalSageContainer else NaturalBackground
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = status.label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextHeading
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSwitchToUser,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NaturalMossPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Login As This User", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AddMedicineDialog(
    onDismiss: () -> Unit,
    onConfirmAdd: (AyurvedaMedicine) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var sanskritName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(FormulationCategory.CHURNA) }
    var primaryBenefit by remember { mutableStateOf("") }
    var standardDose by remember { mutableStateOf("500mg - 1g with warm water") }
    var stockUnits by remember { mutableIntStateOf(50) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, NaturalCardBorder, RoundedCornerShape(24.dp)),
            color = NaturalCardSurface
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NEW CLASSICAL REMEDY",
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextHeading
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalOliveMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                AdminInputField(label = "Medicine Name", value = name, onValueChange = { name = it }, placeholder = "e.g. Shatavari Churna")
                Spacer(modifier = Modifier.height(8.dp))
                AdminInputField(label = "Sanskrit Name & Botanical", value = sanskritName, onValueChange = { sanskritName = it }, placeholder = "e.g. शतावरी (Asparagus racemosus)")
                Spacer(modifier = Modifier.height(8.dp))
                AdminInputField(label = "Primary Therapeutic Benefit", value = primaryBenefit, onValueChange = { primaryBenefit = it }, placeholder = "e.g. Hormonal balance, nourishing Rasayana")
                Spacer(modifier = Modifier.height(8.dp))
                AdminInputField(label = "Standard Dosage & Anupana", value = standardDose, onValueChange = { standardDose = it }, placeholder = "e.g. 500mg twice daily with warm milk")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val newMed = AyurvedaMedicine(
                                id = "custom_${System.currentTimeMillis()}",
                                name = name.trim(),
                                sanskritName = sanskritName.ifBlank { name }.trim(),
                                category = selectedCategory,
                                tagPill = "HERBAL FORMULATION",
                                shortDescription = primaryBenefit.ifBlank { "Traditional Ayurvedic classical formulation." },
                                primaryBenefit = primaryBenefit.ifBlank { "Holistic vitality and dosha pacification." },
                                doshaImpact = "Tridoshic Balancer",
                                targetDoshas = listOf(DoshaType.TRIDOSHIC),
                                constituents = listOf("Phytosterols", "Alkaloids", "Saponins"),
                                ingredients = listOf(
                                    AyurvedaIngredient(
                                        name = name.trim(),
                                        sanskritName = sanskritName.ifBlank { name }.trim(),
                                        botanicalName = "Botanical Herb",
                                        partUsed = "Classical Herbal Compound",
                                        classicalRole = "Rasayana & Dravyaguna agent"
                                    )
                                ),
                                dravyaguna = DravyagunaProfile(
                                    rasa = listOf("Madhura (Sweet)", "Tikta (Bitter)"),
                                    virya = "Sheeta (Cooling)",
                                    vipaka = "Madhura (Nourishing)",
                                    guna = listOf("Guru (Heavy)", "Snigdha (Unctuous)")
                                ),
                                dosage = DosageInfo(
                                    summary = standardDose,
                                    standardDose = standardDose,
                                    frequency = "Twice Daily",
                                    timing = "After Meals",
                                    anupana = "Warm Water or Milk"
                                ),
                                indications = listOf("General debility", "Metabolic harmony"),
                                contraindications = listOf("Use under Vaidya supervision"),
                                pathyaWholesome = listOf("Light warm nourishing food"),
                                apathyaAvoid = listOf("Excessive cold, stale foods"),
                                stockUnits = stockUnits,
                                batchNumber = "AYUR-2026-B${(10..99).random()}"
                            )
                            onConfirmAdd(newMed)
                        }
                    },
                    enabled = name.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NaturalMossPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Add Formulation to Inventory", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AddUserDialog(
    onDismiss: () -> Unit,
    onConfirmAdd: (AppUser) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.PATIENT) }
    var selectedPrakriti by remember { mutableStateOf(DoshaType.PITTA) }
    var designation by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, NaturalCardBorder, RoundedCornerShape(24.dp)),
            color = NaturalCardSurface
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REGISTER NEW USER",
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextHeading
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalOliveMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                AdminInputField(label = "Full Name", value = name, onValueChange = { name = it }, placeholder = "e.g. Dr. Anand Joshi")
                Spacer(modifier = Modifier.height(8.dp))
                AdminInputField(label = "Email Address", value = email, onValueChange = { email = it }, placeholder = "e.g. anand.j@ayurguide.org")
                Spacer(modifier = Modifier.height(8.dp))
                AdminInputField(label = "Designation / Title", value = designation, onValueChange = { designation = it }, placeholder = "e.g. Ayurvedic Vaidya / Patient")

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "ROLE:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NaturalOliveMuted)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    UserRole.values().forEach { role ->
                        val isChosen = selectedRole == role
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, if (isChosen) NaturalMossPrimary else NaturalCardBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedRole = role },
                            color = if (isChosen) NaturalSageContainer else NaturalBackground
                        ) {
                            Box(modifier = Modifier.padding(vertical = 6.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${role.iconEmoji} ${role.badgeLabel}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextHeading
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank() && email.isNotBlank()) {
                            val newUser = AppUser(
                                id = "user_${System.currentTimeMillis()}",
                                name = name.trim(),
                                email = email.trim(),
                                role = selectedRole,
                                prakriti = selectedPrakriti,
                                status = UserStatus.ACTIVE,
                                designation = designation.ifBlank { selectedRole.displayName },
                                registeredDate = "Today",
                                lastActive = "Just now"
                            )
                            onConfirmAdd(newUser)
                        }
                    },
                    enabled = name.isNotBlank() && email.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NaturalMossPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Register & Grant Access", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AdminInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NaturalOliveMuted)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(NaturalBackground)
                .border(1.dp, NaturalCardBorder, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            if (value.isEmpty()) {
                Text(text = placeholder, fontSize = 11.sp, color = NaturalOliveMuted.copy(alpha = 0.6f))
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(fontSize = 12.sp, color = NaturalTextHeading, fontWeight = FontWeight.Normal),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
