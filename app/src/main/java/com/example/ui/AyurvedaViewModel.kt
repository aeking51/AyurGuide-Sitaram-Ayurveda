package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppUser
import com.example.data.model.AuditLogEntry
import com.example.data.model.AyurvedaMedicine
import com.example.data.model.DailyDoseLog
import com.example.data.model.DailyHabit
import com.example.data.model.DoshaType
import com.example.data.model.FormulationCategory
import com.example.data.model.PrakritiScore
import com.example.data.model.UserRole
import com.example.data.model.UserStatus
import com.example.data.repository.AyurvedaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab {
    HOME,
    LIBRARY,
    INSIGHTS,
    PROFILE,
    ADMIN
}

enum class AuthMode {
    LOGIN,
    SIGNUP,
    FORGOT_PASSWORD,
    OTP_RESET
}

data class AyurvedaUiState(
    val currentTab: AppTab = AppTab.HOME,
    val searchQuery: String = "",
    val selectedCategory: FormulationCategory = FormulationCategory.ALL,
    val selectedDosha: DoshaType? = null,
    val allMedicines: List<AyurvedaMedicine> = AyurvedaRepository.allMedicines,
    val isCatalogueLoading: Boolean = false,
    val selectedMedicine: AyurvedaMedicine? = null,
    val dailyVitalityMedicine: AyurvedaMedicine = AyurvedaRepository.allMedicines.first(),
    val isDailyVitalityLogged: Boolean = false,
    val dailyDoses: List<DailyDoseLog> = AyurvedaRepository.defaultDailyDoses,
    val dailyHabits: List<DailyHabit> = AyurvedaRepository.defaultHabits,
    val pittaPercent: Int = 72,
    val vataPercent: Int = 65,
    val kaphaPercent: Int = 78,
    val hydrationLiters: Float = 1.2f,
    val hydrationTargetLiters: Float = 2.5f,
    val prakritiAnswers: Map<Int, DoshaType> = mapOf(1 to DoshaType.PITTA, 2 to DoshaType.PITTA, 3 to DoshaType.VATA, 4 to DoshaType.PITTA),
    val prakritiScore: PrakritiScore = PrakritiScore(vataScore = 1, pittaScore = 3, kaphaScore = 0, dominantDosha = DoshaType.PITTA),
    val snackbarMessage: String? = null,
    // Role-based User & Admin State
    val currentUser: AppUser = AyurvedaRepository.defaultUsers.first(), // Starts as Admin Dr. Vasant Sharma
    val allUsers: List<AppUser> = AyurvedaRepository.defaultUsers,
    val userRoleFilter: UserRole? = null,
    val auditLogs: List<AuditLogEntry> = AyurvedaRepository.defaultAuditLogs,
    val isAddMedicineDialogOpen: Boolean = false,
    val isAddUserDialogOpen: Boolean = false,
    val isSwitchUserDialogOpen: Boolean = false,
    val selectedUserForDetail: AppUser? = null,
    // Authentication State
    val isAuthenticated: Boolean = true,
    val authMode: AuthMode = AuthMode.LOGIN,
    val authErrorMessage: String? = null,
    val authSuccessMessage: String? = null,
    val pendingResetEmail: String = "",
    val generatedOtpCode: String? = null
)

class AyurvedaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AyurvedaUiState())
    val uiState: StateFlow<AyurvedaUiState> = _uiState.asStateFlow()
    private var catalogueFetchJob: Job? = null

    init {
        fetchCatalogueFromDatabase(debounce = 350L)
    }

    fun setTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
        if (tab == AppTab.LIBRARY) {
            fetchCatalogueFromDatabase(debounce = 300L)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.isNotEmpty() && query.length % 2 == 1) {
            fetchCatalogueFromDatabase(debounce = 250L)
        }
    }

    fun onCategorySelected(category: FormulationCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        fetchCatalogueFromDatabase(debounce = 300L)
    }

    fun onDoshaSelected(dosha: DoshaType?) {
        _uiState.update {
            it.copy(selectedDosha = if (it.selectedDosha == dosha) null else dosha)
        }
        fetchCatalogueFromDatabase(debounce = 300L)
    }

    fun refreshCatalogue() {
        fetchCatalogueFromDatabase(debounce = 450L)
    }

    fun fetchCatalogueFromDatabase(debounce: Long = 300L) {
        catalogueFetchJob?.cancel()
        catalogueFetchJob = viewModelScope.launch {
            _uiState.update { it.copy(isCatalogueLoading = true) }
            delay(debounce)
            _uiState.update { it.copy(isCatalogueLoading = false) }
        }
    }

    fun selectMedicine(medicine: AyurvedaMedicine?) {
        _uiState.update { it.copy(selectedMedicine = medicine) }
    }

    // Role-Based User Management
    fun switchUser(user: AppUser) {
        _uiState.update { state ->
            val nextTab = if (user.role == UserRole.PATIENT && state.currentTab == AppTab.ADMIN) {
                AppTab.HOME
            } else {
                state.currentTab
            }
            val newLog = AuditLogEntry(
                id = "log_${System.currentTimeMillis()}",
                timestamp = "Just now",
                actorName = "${user.name} (${user.role.badgeLabel})",
                actionType = "USER_SWITCH",
                targetItem = user.name,
                details = "Switched active session to ${user.name} (${user.role.displayName})."
            )
            state.copy(
                currentUser = user,
                currentTab = nextTab,
                isSwitchUserDialogOpen = false,
                auditLogs = listOf(newLog) + state.auditLogs,
                snackbarMessage = "Logged in as ${user.name} (${user.role.displayName})"
            )
        }
    }

    fun updateUserRole(userId: String, newRole: UserRole) {
        _uiState.update { state ->
            val updatedUsers = state.allUsers.map { user ->
                if (user.id == userId) user.copy(role = newRole) else user
            }
            val targetUser = state.allUsers.find { it.id == userId }
            val updatedCurrent = if (state.currentUser.id == userId) state.currentUser.copy(role = newRole) else state.currentUser
            val newLog = AuditLogEntry(
                id = "log_${System.currentTimeMillis()}",
                timestamp = "Just now",
                actorName = "${state.currentUser.name} (${state.currentUser.role.badgeLabel})",
                actionType = "ROLE_CHANGE",
                targetItem = targetUser?.name ?: userId,
                details = "User role changed to ${newRole.badgeLabel} by ${state.currentUser.name}."
            )
            state.copy(
                allUsers = updatedUsers,
                currentUser = updatedCurrent,
                selectedUserForDetail = state.selectedUserForDetail?.let { if (it.id == userId) it.copy(role = newRole) else it },
                auditLogs = listOf(newLog) + state.auditLogs,
                snackbarMessage = "Updated role for ${targetUser?.name ?: "user"} to ${newRole.displayName}"
            )
        }
    }

    fun updateUserStatus(userId: String, newStatus: UserStatus) {
        _uiState.update { state ->
            val updatedUsers = state.allUsers.map { user ->
                if (user.id == userId) user.copy(status = newStatus) else user
            }
            val targetUser = state.allUsers.find { it.id == userId }
            val newLog = AuditLogEntry(
                id = "log_${System.currentTimeMillis()}",
                timestamp = "Just now",
                actorName = "${state.currentUser.name} (${state.currentUser.role.badgeLabel})",
                actionType = "STATUS_UPDATE",
                targetItem = targetUser?.name ?: userId,
                details = "Account status set to ${newStatus.label}.",
                isWarning = newStatus == UserStatus.SUSPENDED
            )
            state.copy(
                allUsers = updatedUsers,
                selectedUserForDetail = state.selectedUserForDetail?.let { if (it.id == userId) it.copy(status = newStatus) else it },
                auditLogs = listOf(newLog) + state.auditLogs,
                snackbarMessage = "Status for ${targetUser?.name} changed to ${newStatus.label}"
            )
        }
    }

    fun addNewUser(user: AppUser) {
        _uiState.update { state ->
            val newLog = AuditLogEntry(
                id = "log_${System.currentTimeMillis()}",
                timestamp = "Just now",
                actorName = "${state.currentUser.name} (${state.currentUser.role.badgeLabel})",
                actionType = "USER_REGISTERED",
                targetItem = user.name,
                details = "Registered new user with role ${user.role.badgeLabel} and Prakriti ${user.prakriti.displayName}."
            )
            state.copy(
                allUsers = listOf(user) + state.allUsers,
                isAddUserDialogOpen = false,
                auditLogs = listOf(newLog) + state.auditLogs,
                snackbarMessage = "Registered new user ${user.name}"
            )
        }
    }

    fun setUserRoleFilter(role: UserRole?) {
        _uiState.update { it.copy(userRoleFilter = role) }
    }

    fun selectUserForDetail(user: AppUser?) {
        _uiState.update { it.copy(selectedUserForDetail = user) }
    }

    fun setAddMedicineDialogOpen(open: Boolean) {
        _uiState.update { it.copy(isAddMedicineDialogOpen = open) }
    }

    fun setAddUserDialogOpen(open: Boolean) {
        _uiState.update { it.copy(isAddUserDialogOpen = open) }
    }

    fun setSwitchUserDialogOpen(open: Boolean) {
        _uiState.update { it.copy(isSwitchUserDialogOpen = open) }
    }

    // Inventory & Medicine Management
    fun addNewMedicine(medicine: AyurvedaMedicine) {
        _uiState.update { state ->
            val newLog = AuditLogEntry(
                id = "log_${System.currentTimeMillis()}",
                timestamp = "Just now",
                actorName = "${state.currentUser.name} (${state.currentUser.role.badgeLabel})",
                actionType = "FORMULATION_ADDED",
                targetItem = medicine.name,
                details = "Added new classical formulation (${medicine.category.displayName}) to master catalogue. Stock: ${medicine.stockUnits} units."
            )
            state.copy(
                allMedicines = listOf(medicine) + state.allMedicines,
                isAddMedicineDialogOpen = false,
                auditLogs = listOf(newLog) + state.auditLogs,
                snackbarMessage = "Added ${medicine.name} to catalogue"
            )
        }
    }

    fun updateMedicineStock(medicineId: String, newStock: Int) {
        _uiState.update { state ->
            val clamped = newStock.coerceAtLeast(0)
            val updatedMedicines = state.allMedicines.map { med ->
                if (med.id == medicineId) {
                    med.copy(stockUnits = clamped, isLowStock = clamped < 15)
                } else med
            }
            val target = state.allMedicines.find { it.id == medicineId }
            val newLog = AuditLogEntry(
                id = "log_${System.currentTimeMillis()}",
                timestamp = "Just now",
                actorName = "${state.currentUser.name} (${state.currentUser.role.badgeLabel})",
                actionType = "STOCK_UPDATE",
                targetItem = target?.name ?: medicineId,
                details = "Adjusted inventory stock to $clamped units."
            )
            state.copy(
                allMedicines = updatedMedicines,
                auditLogs = listOf(newLog) + state.auditLogs,
                snackbarMessage = "Updated stock for ${target?.name ?: "medicine"}: $clamped units"
            )
        }
    }

    fun toggleMedicineVitality(medicineId: String) {
        _uiState.update { state ->
            val target = state.allMedicines.find { it.id == medicineId }
            if (target != null) {
                val updated = state.allMedicines.map {
                    it.copy(isDailyVitality = it.id == medicineId)
                }
                state.copy(
                    allMedicines = updated,
                    dailyVitalityMedicine = target,
                    snackbarMessage = "${target.name} set as featured Daily Vitality herb"
                )
            } else state
        }
    }

    fun deleteMedicine(medicineId: String) {
        _uiState.update { state ->
            val target = state.allMedicines.find { it.id == medicineId }
            val newLog = AuditLogEntry(
                id = "log_${System.currentTimeMillis()}",
                timestamp = "Just now",
                actorName = "${state.currentUser.name} (${state.currentUser.role.badgeLabel})",
                actionType = "FORMULATION_ARCHIVED",
                targetItem = target?.name ?: medicineId,
                details = "Formulation archived from active dispensary.",
                isWarning = true
            )
            state.copy(
                allMedicines = state.allMedicines.filter { it.id != medicineId },
                selectedMedicine = if (state.selectedMedicine?.id == medicineId) null else state.selectedMedicine,
                auditLogs = listOf(newLog) + state.auditLogs,
                snackbarMessage = "Removed ${target?.name ?: "medicine"} from catalogue"
            )
        }
    }

    fun prescribeMedicineToPatient(medicine: AyurvedaMedicine, patientId: String) {
        _uiState.update { state ->
            val patient = state.allUsers.find { it.id == patientId }
            val newLog = AuditLogEntry(
                id = "log_${System.currentTimeMillis()}",
                timestamp = "Just now",
                actorName = "${state.currentUser.name} (${state.currentUser.role.badgeLabel})",
                actionType = "PRESCRIPTION_ISSUED",
                targetItem = medicine.name,
                details = "Prescribed ${medicine.dosage.summary} to ${patient?.name ?: "patient"}."
            )
            state.copy(
                auditLogs = listOf(newLog) + state.auditLogs,
                snackbarMessage = "Prescribed ${medicine.name} to ${patient?.name ?: "patient"}"
            )
        }
    }

    // Daily Health Routines
    fun logDailyVitalityDose() {
        _uiState.update { state ->
            val newLogged = !state.isDailyVitalityLogged
            val msg = if (newLogged) "Logged: 500mg ${state.dailyVitalityMedicine.name} taken" else "${state.dailyVitalityMedicine.name} dose unlogged"
            val updatedDoses = state.dailyDoses.map { dose ->
                if (dose.medicineId == state.dailyVitalityMedicine.id) {
                    dose.copy(isLogged = newLogged, loggedAtTime = if (newLogged) "Just now" else null)
                } else dose
            }
            state.copy(
                isDailyVitalityLogged = newLogged,
                dailyDoses = updatedDoses,
                snackbarMessage = msg
            )
        }
    }

    fun toggleDose(doseId: String) {
        _uiState.update { state ->
            val updated = state.dailyDoses.map { dose ->
                if (dose.id == doseId) {
                    val nextState = !dose.isLogged
                    dose.copy(isLogged = nextState, loggedAtTime = if (nextState) "Just now" else null)
                } else dose
            }
            val vitalityLogged = updated.find { it.medicineId == state.dailyVitalityMedicine.id }?.isLogged ?: state.isDailyVitalityLogged
            state.copy(dailyDoses = updated, isDailyVitalityLogged = vitalityLogged)
        }
    }

    fun toggleHabit(habitId: String) {
        _uiState.update { state ->
            val updated = state.dailyHabits.map { habit ->
                if (habit.id == habitId) {
                    habit.copy(isCompleted = !habit.isCompleted)
                } else habit
            }
            val habit = state.dailyHabits.find { it.id == habitId }
            val msg = if (habit?.isCompleted == false) "Completed ${habit.title}!" else null
            state.copy(dailyHabits = updated, snackbarMessage = msg)
        }
    }

    fun addHydration(amountLiters: Float) {
        _uiState.update { state ->
            val newAmount = (state.hydrationLiters + amountLiters).coerceIn(0f, 5f)
            val rounded = (Math.round(newAmount * 10f) / 10f)
            state.copy(hydrationLiters = rounded)
        }
    }

    fun updateDoshaLevel(dosha: DoshaType, percent: Int) {
        _uiState.update { state ->
            when (dosha) {
                DoshaType.PITTA -> state.copy(pittaPercent = percent)
                DoshaType.VATA -> state.copy(vataPercent = percent)
                DoshaType.KAPHA -> state.copy(kaphaPercent = percent)
                DoshaType.TRIDOSHIC -> state
            }
        }
    }

    fun addMedicineToDailyRoutine(medicine: AyurvedaMedicine) {
        _uiState.update { state ->
            val exists = state.dailyDoses.any { it.medicineId == medicine.id }
            if (exists) {
                state.copy(snackbarMessage = "${medicine.name} is already in your Daily Routine")
            } else {
                val newDose = DailyDoseLog(
                    id = "custom_${System.currentTimeMillis()}",
                    medicineId = medicine.id,
                    medicineName = medicine.name,
                    doseLabel = medicine.dosage.summary.split("•").firstOrNull()?.trim() ?: "1 dose",
                    timing = medicine.dosage.summary.split("•").getOrNull(1)?.trim() ?: medicine.dosage.timing,
                    iconEmoji = "🌿",
                    isLogged = false
                )
                state.copy(
                    dailyDoses = state.dailyDoses + newDose,
                    snackbarMessage = "Added ${medicine.name} to Daily Routine"
                )
            }
        }
    }

    fun answerPrakriti(questionId: Int, doshaChoice: DoshaType) {
        _uiState.update { state ->
            val newAnswers = state.prakritiAnswers + (questionId to doshaChoice)
            val vataCount = newAnswers.values.count { it == DoshaType.VATA }
            val pittaCount = newAnswers.values.count { it == DoshaType.PITTA }
            val kaphaCount = newAnswers.values.count { it == DoshaType.KAPHA }

            val dominant = when {
                pittaCount >= vataCount && pittaCount >= kaphaCount -> DoshaType.PITTA
                vataCount >= pittaCount && vataCount >= kaphaCount -> DoshaType.VATA
                else -> DoshaType.KAPHA
            }
            state.copy(
                prakritiAnswers = newAnswers,
                prakritiScore = PrakritiScore(vataCount, pittaCount, kaphaCount, dominant)
            )
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    // -------------------------------------------------------------
    // Authentication Operations
    // -------------------------------------------------------------

    fun setAuthMode(mode: AuthMode) {
        _uiState.update { it.copy(authMode = mode, authErrorMessage = null, authSuccessMessage = null) }
    }

    fun login(email: String, pass: String): Boolean {
        val trimmedEmail = email.trim()
        val user = _uiState.value.allUsers.firstOrNull { it.email.equals(trimmedEmail, ignoreCase = true) }
        if (user == null) {
            _uiState.update { it.copy(authErrorMessage = "No account found with this email. Please check or sign up.") }
            return false
        }
        if (user.status == UserStatus.SUSPENDED) {
            _uiState.update { it.copy(authErrorMessage = "This account is suspended. Contact clinical administrator.") }
            return false
        }
        if (pass.isNotEmpty() && user.password != pass) {
            _uiState.update { it.copy(authErrorMessage = "Incorrect password. Tap 'Forgot Password?' to reset.") }
            return false
        }

        val loginLog = AuditLogEntry(
            id = "audit_${System.currentTimeMillis()}",
            timestamp = "Just now",
            actorName = user.name,
            actionType = "LOGIN_SUCCESS",
            targetItem = user.role.displayName,
            details = "Logged in successfully to AyurGuide portal."
        )

        _uiState.update { state ->
            state.copy(
                currentUser = user,
                isAuthenticated = true,
                authErrorMessage = null,
                authSuccessMessage = "Welcome back, ${user.name}!",
                auditLogs = listOf(loginLog) + state.auditLogs
            )
        }
        return true
    }

    fun quickLoginAs(user: AppUser) {
        switchUser(user)
        _uiState.update {
            it.copy(
                isAuthenticated = true,
                authErrorMessage = null,
                authSuccessMessage = "Logged in as ${user.name} (${user.role.displayName})"
            )
        }
    }

    fun signup(
        name: String,
        email: String,
        pass: String,
        role: UserRole,
        prakriti: DoshaType,
        designation: String = ""
    ): Boolean {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(authErrorMessage = "Please fill in all required fields.") }
            return false
        }
        val trimmedEmail = email.trim()
        if (_uiState.value.allUsers.any { it.email.equals(trimmedEmail, ignoreCase = true) }) {
            _uiState.update { it.copy(authErrorMessage = "An account with this email already exists.") }
            return false
        }

        val newUser = AppUser(
            id = "user_${System.currentTimeMillis()}",
            name = name.trim(),
            email = trimmedEmail,
            role = role,
            prakriti = prakriti,
            status = UserStatus.ACTIVE,
            designation = designation.ifBlank { role.displayName },
            registeredDate = "Today",
            lastActive = "Just now",
            password = pass
        )

        val regLog = AuditLogEntry(
            id = "audit_${System.currentTimeMillis()}",
            timestamp = "Just now",
            actorName = newUser.name,
            actionType = "USER_REGISTERED",
            targetItem = "${role.badgeLabel} ACCOUNT",
            details = "New ${role.displayName} registered with ${prakriti.displayName} constitution."
        )

        _uiState.update { state ->
            state.copy(
                allUsers = state.allUsers + newUser,
                currentUser = newUser,
                isAuthenticated = true,
                authErrorMessage = null,
                authSuccessMessage = "Account created! Welcome, ${newUser.name}.",
                auditLogs = listOf(regLog) + state.auditLogs
            )
        }
        return true
    }

    fun requestPasswordReset(email: String): Boolean {
        val trimmed = email.trim()
        if (trimmed.isBlank()) {
            _uiState.update { it.copy(authErrorMessage = "Please enter your registered email address.") }
            return false
        }
        val user = _uiState.value.allUsers.firstOrNull { it.email.equals(trimmed, ignoreCase = true) }
        if (user == null) {
            _uiState.update { it.copy(authErrorMessage = "No account found registered with $trimmed.") }
            return false
        }

        val otp = (100000..999999).random().toString()
        _uiState.update {
            it.copy(
                pendingResetEmail = trimmed,
                generatedOtpCode = otp,
                authMode = AuthMode.OTP_RESET,
                authErrorMessage = null,
                authSuccessMessage = "Verification OTP code sent to $trimmed: $otp"
            )
        }
        return true
    }

    fun completePasswordReset(email: String, otpInput: String, newPassword: String): Boolean {
        if (otpInput.trim() != _uiState.value.generatedOtpCode) {
            _uiState.update { it.copy(authErrorMessage = "Invalid OTP code. Please enter the 6-digit verification code.") }
            return false
        }
        if (newPassword.length < 4) {
            _uiState.update { it.copy(authErrorMessage = "Password must be at least 4 characters.") }
            return false
        }

        val resetLog = AuditLogEntry(
            id = "audit_${System.currentTimeMillis()}",
            timestamp = "Just now",
            actorName = email,
            actionType = "PASSWORD_RESET",
            targetItem = "CREDENTIALS",
            details = "Password reset verified via OTP."
        )

        _uiState.update { state ->
            val updatedUsers = state.allUsers.map { u ->
                if (u.email.equals(email, ignoreCase = true)) u.copy(password = newPassword) else u
            }
            val updatedCurrent = if (state.currentUser.email.equals(email, ignoreCase = true)) {
                state.currentUser.copy(password = newPassword)
            } else state.currentUser

            state.copy(
                allUsers = updatedUsers,
                currentUser = updatedCurrent,
                authMode = AuthMode.LOGIN,
                generatedOtpCode = null,
                pendingResetEmail = "",
                authErrorMessage = null,
                authSuccessMessage = "Password reset successfully! Please sign in with your new password.",
                auditLogs = listOf(resetLog) + state.auditLogs
            )
        }
        return true
    }

    fun logout() {
        val logoutLog = AuditLogEntry(
            id = "audit_${System.currentTimeMillis()}",
            timestamp = "Just now",
            actorName = _uiState.value.currentUser.name,
            actionType = "LOGOUT",
            targetItem = "SESSION",
            details = "User signed out."
        )
        _uiState.update { state ->
            state.copy(
                isAuthenticated = false,
                authMode = AuthMode.LOGIN,
                authErrorMessage = null,
                authSuccessMessage = null,
                currentTab = AppTab.HOME,
                auditLogs = listOf(logoutLog) + state.auditLogs
            )
        }
    }

    fun clearAuthMessages() {
        _uiState.update { it.copy(authErrorMessage = null, authSuccessMessage = null) }
    }
}

