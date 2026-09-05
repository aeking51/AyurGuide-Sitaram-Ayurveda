package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.model.AyurvedaMedicine
import com.example.data.model.DailyDoseLog
import com.example.data.model.DailyHabit
import com.example.data.model.DoshaType
import com.example.data.model.FormulationCategory
import com.example.data.model.PrakritiScore
import com.example.data.repository.AyurvedaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppTab {
    HOME,
    LIBRARY,
    INSIGHTS,
    PROFILE
}

data class AyurvedaUiState(
    val currentTab: AppTab = AppTab.HOME,
    val searchQuery: String = "",
    val selectedCategory: FormulationCategory = FormulationCategory.ALL,
    val selectedDosha: DoshaType? = null,
    val allMedicines: List<AyurvedaMedicine> = AyurvedaRepository.allMedicines,
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
    val snackbarMessage: String? = null
)

class AyurvedaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AyurvedaUiState())
    val uiState: StateFlow<AyurvedaUiState> = _uiState.asStateFlow()

    fun setTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelected(category: FormulationCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onDoshaSelected(dosha: DoshaType?) {
        _uiState.update {
            it.copy(selectedDosha = if (it.selectedDosha == dosha) null else dosha)
        }
    }

    fun selectMedicine(medicine: AyurvedaMedicine?) {
        _uiState.update { it.copy(selectedMedicine = medicine) }
    }

    fun logDailyVitalityDose() {
        _uiState.update { state ->
            val newLogged = !state.isDailyVitalityLogged
            val msg = if (newLogged) "Logged: 500mg Ashwagandha Root taken" else "Ashwagandha dose unlogged"
            // Also update in dailyDoses if exists
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
            // Sync vitality if same id
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
}
