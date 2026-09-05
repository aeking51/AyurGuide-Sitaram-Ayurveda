package com.example.data.model

enum class FormulationCategory(val displayName: String, val sanskritTerm: String) {
    ALL("All", "Sarva"),
    CHURNA("Churna", "चूर्ण (Herbal Powder)"),
    VATI("Vati", "वटी (Ayurvedic Tablet)"),
    ARISHTA("Arishta / Asava", "अरिष्ट (Fermented Elixir)"),
    TAILA("Taila", "तैल (Medicated Oil)"),
    GHRITA("Ghrita", "घृत (Medicated Ghee)"),
    RASAYANA("Rasayana", "रसायन (Rejuvenating Jam)"),
    KWATHA("Kwatha", "क्वाथ (Decoction)")
}

enum class DoshaType(val displayName: String, val symbol: String, val element: String) {
    VATA("Vata", "💨", "Ether & Air"),
    PITTA("Pitta", "🔥", "Fire & Water"),
    KAPHA("Kapha", "💧", "Earth & Water"),
    TRIDOSHIC("Tridoshic", "⚖️", "Balances All Doshas")
}

data class AyurvedaIngredient(
    val name: String,
    val sanskritName: String,
    val botanicalName: String,
    val partUsed: String,
    val classicalRole: String
)

data class DravyagunaProfile(
    val rasa: List<String>,      // Tastes: Madhura, Tikta, Kashaya, etc.
    val virya: String,           // Ushna (Heating) or Sheeta (Cooling)
    val vipaka: String,          // Post-digestive: Madhura, Katu, Amla
    val guna: List<String>       // Qualities: Laghu, Snigdha, Guru, etc.
)

data class DosageInfo(
    val summary: String,         // e.g. "500mg • After Breakfast"
    val standardDose: String,    // e.g. "500mg - 1000mg"
    val frequency: String,       // e.g. "Twice daily"
    val timing: String,          // e.g. "30 mins after meals"
    val anupana: String,         // Traditional vehicle (e.g. "Warm Milk or Cow's Ghee with Honey")
    val caution: String = ""
)

data class AyurvedaMedicine(
    val id: String,
    val name: String,
    val sanskritName: String,
    val category: FormulationCategory,
    val tagPill: String,         // e.g. "ADAPTOGEN", "DETOX", "NOOTROPIC"
    val shortDescription: String,
    val primaryBenefit: String,
    val doshaImpact: String,     // e.g. "Vata-Kapha Shamaka"
    val targetDoshas: List<DoshaType>,
    val constituents: List<String>, // e.g. "Flavonoids", "Alkaloids"
    val ingredients: List<AyurvedaIngredient>,
    val dravyaguna: DravyagunaProfile,
    val dosage: DosageInfo,
    val indications: List<String>,
    val contraindications: List<String>,
    val pathyaWholesome: List<String>, // Recommended foods
    val apathyaAvoid: List<String>,    // Foods to avoid
    val isDailyVitality: Boolean = false,
    val stockUnits: Int = 45,
    val batchNumber: String = "AYUR-2026-B12",
    val isLowStock: Boolean = false
)

enum class UserRole(
    val displayName: String,
    val badgeLabel: String,
    val description: String,
    val iconEmoji: String
) {
    ADMIN("Chief Administrator", "ADMIN", "Full operational & clinical control, formulation inventory, user directory, system audits", "⚡"),
    PRACTITIONER("Ayurvedic Vaidya", "PRACTITIONER", "Clinical consultations, formulation prescribing, patient health reviews", "⚕️"),
    PATIENT("Wellness Seeker", "PATIENT", "Personal daily routines, dosha harmony, hydration, classical herb library", "🌿")
}

enum class UserStatus(val label: String) {
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    PENDING("Pending Verification")
}

data class AppUser(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val prakriti: DoshaType = DoshaType.PITTA,
    val status: UserStatus = UserStatus.ACTIVE,
    val designation: String = "",
    val phone: String = "+91 98450 12345",
    val registeredDate: String = "Jan 12, 2026",
    val lastActive: String = "Just now",
    val adherencePercent: Int = 85,
    val assignedPractitioner: String? = null,
    val clinicalNotes: String = "",
    val password: String = "ayur123"
)

data class AuditLogEntry(
    val id: String,
    val timestamp: String,
    val actorName: String,
    val actionType: String,
    val targetItem: String,
    val details: String,
    val isWarning: Boolean = false
)

data class DailyHabit(
    val id: String,
    val title: String,
    val scheduledTime: String,
    val iconEmoji: String,
    val description: String,
    val isCompleted: Boolean = false
)

data class DailyDoseLog(
    val id: String,
    val medicineId: String,
    val medicineName: String,
    val doseLabel: String,
    val timing: String,
    val iconEmoji: String = "🍵",
    val isLogged: Boolean = false,
    val loggedAtTime: String? = null
)

data class PrakritiQuestion(
    val id: Int,
    val trait: String,
    val optionVata: String,
    val optionPitta: String,
    val optionKapha: String
)

data class PrakritiScore(
    val vataScore: Int = 0,
    val pittaScore: Int = 0,
    val kaphaScore: Int = 0,
    val dominantDosha: DoshaType = DoshaType.PITTA
)

