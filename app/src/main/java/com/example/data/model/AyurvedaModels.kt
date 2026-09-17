package com.example.data.model

enum class FormulationCategory(val displayName: String, val sanskritTerm: String) {
    ALL("All", "Sarva"),
    CHURNA("Churna", "चूर्ण (Herbal Powder)"),
    VATI("Vati", "वटी (Ayurvedic Tablet)"),
    ARISHTA("Arishta / Asava", "अरिष्ट (Fermented Elixir)"),
    ASAVA("Asava", "आसव (Fermented Infusion)"),
    ARKAM("Arkam", "अर्क (Distilled Extract)"),
    BHASMA_KSHARA("Bhasmam / Ksharams", "भस्म / क्षार (Purified Calx)"),
    GULIKA("Gulika", "गुळिका (Ayurvedic Pills)"),
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

/**
 * Health Goal classifications representing Ayurvedic therapeutic intentions.
 * Allows intuitive categorization and searching for specific health outcomes.
 */
enum class HealthGoal(
    val id: String,
    val displayName: String,
    val iconEmoji: String,
    val sanskritTerm: String,
    val shortSummary: String,
    val description: String
) {
    DIGESTION(
        id = "digestion",
        displayName = "Digestion",
        iconEmoji = "🌱",
        sanskritTerm = "Agni & Deepana",
        shortSummary = "Gut health & bowel regularity",
        description = "Formulations kindling metabolic fire (Agni), gut cleanse, and nutrient assimilation"
    ),
    IMMUNITY(
        id = "immunity",
        displayName = "Immunity",
        iconEmoji = "🛡️",
        sanskritTerm = "Ojas & Rasayana",
        shortSummary = "Deep vitality & defense",
        description = "Botanicals nourishing Ojas, respiratory resilience, and tissue longevity"
    ),
    STRESS_RELIEF(
        id = "stress_relief",
        displayName = "Stress Relief",
        iconEmoji = "🧘",
        sanskritTerm = "Manas Shanti",
        shortSummary = "Nervine calm & restorative sleep",
        description = "Adaptogens balancing Prana Vata, relieving mental fatigue and easing tension"
    ),
    COGNITION(
        id = "cognition",
        displayName = "Memory & Focus",
        iconEmoji = "🧠",
        sanskritTerm = "Medhya Rasayana",
        shortSummary = "Mental clarity & intellect",
        description = "Neuro-supportive herbs enhancing memory recall, concentration, and focus"
    ),
    SKIN_HEALTH(
        id = "skin_health",
        displayName = "Skin Radiance",
        iconEmoji = "✨",
        sanskritTerm = "Varnya & Twachya",
        shortSummary = "Complexion & blemish care",
        description = "Skin-clarifying herbs purifying Rakta (blood) and bestowing natural luster"
    ),
    JOINT_MOBILITY(
        id = "joint_mobility",
        displayName = "Joints & Mobility",
        iconEmoji = "🦴",
        sanskritTerm = "Sandhi Shoola",
        shortSummary = "Musculoskeletal comfort",
        description = "Herbs soothing aggravated Vata, easing stiffness, and promoting joint fluidity"
    ),
    DETOX(
        id = "detox",
        displayName = "Detox & Cleanse",
        iconEmoji = "💧",
        sanskritTerm = "Ama Pachana",
        shortSummary = "Metabolic toxin scraping",
        description = "Scraping deep Ama (endotoxins), cleansing micro-channels, and fluid equilibrium"
    )
}

data class AyurvedaIngredient(
    val name: String,
    val sanskritName: String = "",
    val botanicalName: String = "",
    val partUsed: String = "",
    val classicalRole: String = ""
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
    val ingredients: List<AyurvedaIngredient> = emptyList(),
    val dosageInstructions: String = "",
    val benefits: List<String> = emptyList(),
    val sanskritName: String = "",
    val category: FormulationCategory = FormulationCategory.CHURNA,
    val tagPill: String = "HERBAL",         // e.g. "ADAPTOGEN", "DETOX", "NOOTROPIC"
    val healthGoals: List<HealthGoal> = emptyList(),
    val shortDescription: String = "",
    val primaryBenefit: String = "",
    val doshaImpact: String = "",     // e.g. "Vata-Kapha Shamaka"
    val targetDoshas: List<DoshaType> = listOf(DoshaType.TRIDOSHIC),
    val constituents: List<String> = emptyList(), // e.g. "Flavonoids", "Alkaloids"
    val dravyaguna: DravyagunaProfile = DravyagunaProfile(listOf("Madhura"), "Sheeta", "Madhura", listOf("Laghu")),
    val dosage: DosageInfo = DosageInfo("500mg daily", "500mg", "Twice daily", "After meals", "Warm water"),
    val indications: List<String> = emptyList(),
    val contraindications: List<String> = emptyList(),
    val pathyaWholesome: List<String> = emptyList(), // Recommended foods
    val apathyaAvoid: List<String> = emptyList(),    // Foods to avoid
    val isDailyVitality: Boolean = false,
    val stockUnits: Int = 45,
    val batchNumber: String = "AYUR-2026-B12",
    val isLowStock: Boolean = false,
    // Fields directly mapped from Sitaram Ayurveda Therapeutic Index Handbook:
    val slNo: Int = 0,                                // Serial number from the Handbook
    val classicalReference: String = "",             // Textual authority, e.g. "Ashtamgahrudayam", "Bhaisajya Ratnavali", "Sahasrayogam"
    val packing: String = "",                         // e.g. "450 ml", "50 g", "100 Nos.", "60 Nos."
    val mainIngredientsText: String = "",             // e.g. "Abhaya, Dhatri, Kapitha, Vishala"
    val usageInstructionsText: String = "",           // e.g. "5-25 ml Twice daily", "Internal"
    val photoUrl: String = ""                         // Product packaging photo URL or asset reference
) {
    // Computed property ensuring health goals list is always available even if constructed without explicit goals
    val effectiveHealthGoals: List<HealthGoal>
        get() {
            if (healthGoals.isNotEmpty()) return healthGoals
            val text = "$name $sanskritName $tagPill $shortDescription $primaryBenefit ${indications.joinToString(" ")}".lowercase()
            val inferred = mutableListOf<HealthGoal>()
            if (text.contains("digest") || text.contains("bowel") || text.contains("colon") || text.contains("gut") || text.contains("metabol") || text.contains("triphala") || text.contains("amritarishta") || text.contains("agni")) {
                inferred.add(HealthGoal.DIGESTION)
            }
            if (text.contains("immun") || text.contains("ojas") || text.contains("rasayana") || text.contains("defense") || text.contains("chyawanprash") || text.contains("guduchi") || text.contains("amrita")) {
                inferred.add(HealthGoal.IMMUNITY)
            }
            if (text.contains("stress") || text.contains("sleep") || text.contains("calm") || text.contains("anxiety") || text.contains("nerv") || text.contains("insomnia") || text.contains("ashwagandha")) {
                inferred.add(HealthGoal.STRESS_RELIEF)
            }
            if (text.contains("memory") || text.contains("brain") || text.contains("focus") || text.contains("cognit") || text.contains("nootropic") || text.contains("brahmi") || text.contains("medhya")) {
                inferred.add(HealthGoal.COGNITION)
            }
            if (text.contains("skin") || text.contains("complexion") || text.contains("radiance") || text.contains("blemish") || text.contains("kumkumadi") || text.contains("saffron")) {
                inferred.add(HealthGoal.SKIN_HEALTH)
            }
            if (text.contains("joint") || text.contains("mobility") || text.contains("back") || text.contains("sciatica") || text.contains("dashamula") || text.contains("guggulu") || text.contains("stiff")) {
                inferred.add(HealthGoal.JOINT_MOBILITY)
            }
            if (text.contains("detox") || text.contains("cleanse") || text.contains("toxin") || text.contains("ama") || text.contains("renal") || text.contains("uric") || text.contains("fluid balance")) {
                inferred.add(HealthGoal.DETOX)
            }
            return if (inferred.isNotEmpty()) inferred.distinct() else listOf(HealthGoal.IMMUNITY)
        }

    // Computed property ensuring benefits list is always available even if constructed with primaryBenefit/indications
    val effectiveBenefits: List<String>
        get() = if (benefits.isNotEmpty()) {
            benefits
        } else if (primaryBenefit.isNotBlank()) {
            listOf(primaryBenefit) + indications
        } else {
            indications
        }

    // Computed property ensuring dosage instructions string is formatted and available
    val effectiveDosageInstructions: String
        get() = if (dosageInstructions.isNotBlank()) {
            dosageInstructions
        } else if (dosage.summary.isNotBlank()) {
            "${dosage.summary} • ${dosage.standardDose} (${dosage.frequency}, ${dosage.timing}. Anupana: ${dosage.anupana})"
        } else {
            "${dosage.standardDose} • ${dosage.frequency}"
        }

    val ingredientNames: List<String>
        get() = ingredients.map { it.name }

    // Formatted packing size from the Handbook (e.g. "450 ml", "50 g", "100 Nos.")
    val effectivePacking: String
        get() = packing.ifBlank {
            when (category) {
                FormulationCategory.ARISHTA, FormulationCategory.ASAVA, FormulationCategory.ARKAM -> "450 ml"
                FormulationCategory.CHURNA, FormulationCategory.BHASMA_KSHARA -> "50 g"
                FormulationCategory.GULIKA, FormulationCategory.VATI -> "100 Nos."
                FormulationCategory.TAILA -> "200 ml"
                FormulationCategory.GHRITA -> "150 g"
                FormulationCategory.RASAYANA -> "500 g"
                FormulationCategory.KWATHA -> "200 ml"
                else -> "Standard Unit"
            }
        }

    // Textual reference authority (e.g. "Ashtamgahrudayam", "Bhaisajya Ratnavali")
    val effectiveReference: String
        get() = classicalReference.ifBlank { "AFI Pharmacopoeia" }

    // Direct usage instructions from the Handbook
    val effectiveUsage: String
        get() = if (usageInstructionsText.isNotBlank()) usageInstructionsText else effectiveDosageInstructions

    // Direct main ingredients summary
    val effectiveMainIngredientsText: String
        get() = if (mainIngredientsText.isNotBlank()) {
            mainIngredientsText
        } else if (ingredients.isNotEmpty()) {
            ingredients.joinToString(", ") { it.name }
        } else {
            name
        }
}

enum class UserRole(
    val displayName: String,
    val badgeLabel: String,
    val description: String,
    val iconEmoji: String
) {
    ADMIN("Administrator", "ADMIN", "Full operational & clinical control, formulation inventory, user directory, system audits", "⚡"),
    PRACTITIONER("Practitioner", "PRACTITIONER", "Clinical consultations, formulation prescribing, patient health reviews", "⚕️"),
    PATIENT("Wellness Seeker", "WELLNESS SEEKER", "Personal daily routines, dosha harmony, hydration, classical herb library", "🌿"),
    GUEST("Guest Explorer", "GUEST", "Limited read-only access to classical formulations, botanical index & dosha guides", "🍃")
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

