package com.example.data.repository

import com.example.data.model.AppUser
import com.example.data.model.AuditLogEntry
import com.example.data.model.AyurvedaIngredient
import com.example.data.model.AyurvedaMedicine
import com.example.data.model.DailyDoseLog
import com.example.data.model.DailyHabit
import com.example.data.model.DosageInfo
import com.example.data.model.DoshaType
import com.example.data.model.DravyagunaProfile
import com.example.data.model.FormulationCategory
import com.example.data.model.PrakritiQuestion
import com.example.data.model.UserRole
import com.example.data.model.UserStatus

object AyurvedaRepository {

    val allMedicines: List<AyurvedaMedicine> = listOf(
        AyurvedaMedicine(
            id = "ashwagandha_root",
            name = "Ashwagandha Root",
            sanskritName = "अश्वगंधा (Withania somnifera)",
            category = FormulationCategory.CHURNA,
            tagPill = "ADAPTOGEN",
            shortDescription = "Supports stress reduction and cognitive focus. Part of your Morning Ritual.",
            primaryBenefit = "Somatic vitality, adrenal support, and deep restful sleep",
            doshaImpact = "Vata & Kapha Pacifying (Vata-Kapha Shamaka)",
            targetDoshas = listOf(DoshaType.VATA, DoshaType.KAPHA),
            constituents = listOf("Flavonoids", "Alkaloids", "Withanolides", "Sitoindosides"),
            ingredients = listOf(
                AyurvedaIngredient(
                    name = "Ashwagandha Root",
                    sanskritName = "अश्वगंधा",
                    botanicalName = "Withania somnifera",
                    partUsed = "Sun-dried Rhizome & Root",
                    classicalRole = "Balya (Strength giver), Rasayana (Rejuvenator), Medhya (Nervine tonic)"
                ),
                AyurvedaIngredient(
                    name = "Black Pepper (Bioenhancer)",
                    sanskritName = "मरिच",
                    botanicalName = "Piper nigrum",
                    partUsed = "Dried Fruit",
                    classicalRole = "Deepana & Sroto-shodhana (Improves cellular assimilation)"
                )
            ),
            dravyaguna = DravyagunaProfile(
                rasa = listOf("Tikta (Bitter)", "Kashaya (Astringent)", "Madhura (Sweet)"),
                virya = "Ushna (Heating / Energizing)",
                vipaka = "Madhura (Nourishing post-digestive)",
                guna = listOf("Laghu (Light)", "Snigdha (Unctuous)")
            ),
            dosage = DosageInfo(
                summary = "500mg • After Breakfast",
                standardDose = "500mg - 1000mg (1/2 to 1 teaspoon powder)",
                frequency = "Twice Daily",
                timing = "Morning after meal & 30 mins before sleep",
                anupana = "Warm Cow's Milk, Ghee, or warm water with honey",
                caution = "Use with care in high Pitta conditions with burning sensations."
            ),
            indications = listOf("Chronic mental fatigue", "Anxiety & restlessness", "Muscle debility", "Insomnia"),
            contraindications = listOf("Acute high fever (Ama condition)", "Severe thyrotoxicosis without supervision"),
            pathyaWholesome = listOf("Warm whole milk", "Soaked almonds", "Ghee", "Warm stewed apples"),
            apathyaAvoid = listOf("Excessive caffeine", "Cold raw dry salads", "Late night eating"),
            isDailyVitality = true
        ),
        AyurvedaMedicine(
            id = "triphala_churna",
            name = "Triphala Churna",
            sanskritName = "त्रिफला चूर्ण (Three Sacred Fruits)",
            category = FormulationCategory.CHURNA,
            tagPill = "DIGESTIVE DETOX",
            shortDescription = "Classic three-fruit formulation for gentle colon cleanse and systemic detox.",
            primaryBenefit = "Gentle bowel motility, antioxidant protection, ocular health",
            doshaImpact = "Tridoshic Harmony (Balances Vata, Pitta, Kapha)",
            targetDoshas = listOf(DoshaType.TRIDOSHIC, DoshaType.PITTA, DoshaType.KAPHA),
            constituents = listOf("Tannins", "Gallic Acid", "Vitamin C", "Chebulagic Acid"),
            ingredients = listOf(
                AyurvedaIngredient(
                    name = "Amalaki (Indian Gooseberry)",
                    sanskritName = "आमलकी",
                    botanicalName = "Emblica officinalis",
                    partUsed = "Dried Pericarp",
                    classicalRole = "Rich natural Vitamin C, cooling Pitta regulator, Rasayana"
                ),
                AyurvedaIngredient(
                    name = "Bibhitaki (Belliric Myrobalan)",
                    sanskritName = "बिभीतकी",
                    botanicalName = "Terminalia bellirica",
                    partUsed = "Fruit Peel",
                    classicalRole = "Pacifies Kapha, supports lungs and mucosal health"
                ),
                AyurvedaIngredient(
                    name = "Haritaki (Chebulic Myrobalan)",
                    sanskritName = "हरीतकी",
                    botanicalName = "Terminalia chebula",
                    partUsed = "Dried Fruit",
                    classicalRole = "King of Medicines; scrapes Ama (toxins) and pacifies Vata"
                )
            ),
            dravyaguna = DravyagunaProfile(
                rasa = listOf("Pancha-Rasa (Contains 5 tastes except salty Lavana)"),
                virya = "Sheeta & Anushna (Balanced temperature)",
                vipaka = "Madhura (Sweet post-digestive)",
                guna = listOf("Laghu (Light)", "Ruksha (Dry)")
            ),
            dosage = DosageInfo(
                summary = "3g - 5g • At Bedtime",
                standardDose = "1/2 to 1 teaspoon (3g - 5g)",
                frequency = "Once daily before sleep",
                timing = "30-45 minutes after dinner before bed",
                anupana = "Warm water or equal parts ghee and raw honey",
                caution = "Avoid during acute diarrhea or early pregnancy."
            ),
            indications = listOf("Sluggish bowel habits", "Toxin accumulation (Ama)", "Eye fatigue", "Weak metabolism"),
            contraindications = listOf("Dysentery", "Acute dehydration"),
            pathyaWholesome = listOf("Warm water throughout the day", "Steamed vegetables", "Moong dal soup"),
            apathyaAvoid = listOf("Fried heavy snacks", "Cold curd", "Processed flour")
        ),
        AyurvedaMedicine(
            id = "brahmi_vati",
            name = "Brahmi Vati",
            sanskritName = "ब्राह्मी वटी (Classical Cognitive Tablet)",
            category = FormulationCategory.VATI,
            tagPill = "NOOTROPIC",
            shortDescription = "Enhances memory retention, concentration, and soothes emotional agitation.",
            primaryBenefit = "Brain fog clearance, memory enhancement, nervous equilibrium",
            doshaImpact = "Pacifies Sadhaka Pitta & Prana Vata",
            targetDoshas = listOf(DoshaType.VATA, DoshaType.PITTA),
            constituents = listOf("Bacosides A & B", "Alkaloids", "Sterols"),
            ingredients = listOf(
                AyurvedaIngredient(
                    name = "Brahmi Herb",
                    sanskritName = "ब्राह्मी",
                    botanicalName = "Bacopa monnieri",
                    partUsed = "Whole Aerial Plant",
                    classicalRole = "Medhya Rasayana (Potent neuroprotective adaptogen)"
                ),
                AyurvedaIngredient(
                    name = "Shankhpushpi",
                    sanskritName = "शंखपुष्पी",
                    botanicalName = "Convolvulus pluricaulis",
                    partUsed = "Whole Herb",
                    classicalRole = "Calms nervous excitement and promotes cerebral microcirculation"
                ),
                AyurvedaIngredient(
                    name = "Vacha (Sweet Flag)",
                    sanskritName = "वचा",
                    botanicalName = "Acorus calamus",
                    partUsed = "Purified Rhizome",
                    classicalRole = "Speech clarity, cognitive sharpness, clears srotas"
                )
            ),
            dravyaguna = DravyagunaProfile(
                rasa = listOf("Tikta (Bitter)", "Kashaya (Astringent)"),
                virya = "Sheeta (Cooling to mind & blood)",
                vipaka = "Madhura",
                guna = listOf("Laghu (Light)", "Sara (Promotes gentle movement)")
            ),
            dosage = DosageInfo(
                summary = "1-2 Tablets • After Meals",
                standardDose = "1 to 2 tablets (250mg - 500mg)",
                frequency = "Twice daily",
                timing = "After breakfast and lunch",
                anupana = "Warm water, warm milk, or Brahmi Ghrita",
                caution = "Take after food if prone to mild gastric sensitivity."
            ),
            indications = listOf("Examination stress", "Poor memory recall", "Mental fatigue", "Restless thoughts"),
            contraindications = listOf("Hypersensitivity to herbal bitters"),
            pathyaWholesome = listOf("A2 Cow Ghee", "Walnuts", "Pomegranate", "Fresh coconut water"),
            apathyaAvoid = listOf("Excessive green chilies", "Fermented alcohol", "Excessive screen time before bed")
        ),
        AyurvedaMedicine(
            id = "chyawanprash_awaleha",
            name = "Chyawanprash Awaleha",
            sanskritName = "च्यवनप्राश अवलेह (The Grand Vitality Jam)",
            category = FormulationCategory.RASAYANA,
            tagPill = "IMMUNITY BOOSTER",
            shortDescription = "Classical multi-herb botanical jam prepared in fresh amla, ghee, and honey.",
            primaryBenefit = "Deep immune barrier (Ojas), respiratory defense, tissue anti-aging",
            doshaImpact = "Tridoshic Balancer (Nourishes all 7 Dhatus)",
            targetDoshas = listOf(DoshaType.TRIDOSHIC, DoshaType.VATA, DoshaType.KAPHA),
            constituents = listOf("Bioflavonoids", "Ascorbic Acid", "Essential Fatty Acids", "Polyphenols"),
            ingredients = listOf(
                AyurvedaIngredient(
                    name = "Fresh Amla (Gooseberry)",
                    sanskritName = "आमलकी",
                    botanicalName = "Phyllanthus emblica",
                    partUsed = "Pulp of Fresh Wild Berries",
                    classicalRole = "Dominant ingredient (60%+); supreme Rasayana and cellular protector"
                ),
                AyurvedaIngredient(
                    name = "Dashamula Complex",
                    sanskritName = "दशमूल",
                    botanicalName = "Ten Sacred Roots",
                    partUsed = "Decoction of 10 Classical Roots",
                    classicalRole = "Strengthens respiratory system and deep organ vitality"
                ),
                AyurvedaIngredient(
                    name = "Pippali (Long Pepper)",
                    sanskritName = "पिप्पली",
                    botanicalName = "Piper longum",
                    partUsed = "Dried Spikes",
                    classicalRole = "Pranavaha Srotas rejuvenator; clears bronchial phlegm"
                ),
                AyurvedaIngredient(
                    name = "Pure Cow's Ghee & Sesame Oil",
                    sanskritName = "घृत एवं तैल",
                    botanicalName = "A2 Ghee & Sesamum indicum",
                    partUsed = "Cold-pressed / Cultured base",
                    classicalRole = "Lipid carrier driving herbs deep into cellular membranes"
                )
            ),
            dravyaguna = DravyagunaProfile(
                rasa = listOf("Madhura (Sweet)", "Amla (Sour)", "Tikta (Bitter)", "Katu (Pungent)"),
                virya = "Sheeta-Ushna Samashitoshna (Balanced)",
                vipaka = "Madhura",
                guna = listOf("Guru (Nourishing / Heavy)", "Snigdha (Unctuous)")
            ),
            dosage = DosageInfo(
                summary = "10g - 15g • Early Morning",
                standardDose = "1 tablespoon (12g - 15g)",
                frequency = "Once or twice daily",
                timing = "Morning on empty stomach or before breakfast",
                anupana = "Warm cow's milk or warm water",
                caution = "Diabetic individuals should consult practitioner due to jaggery/honey content."
            ),
            indications = listOf("Frequent seasonal colds", "General debility", "Weak lungs", "Post-illness fatigue"),
            contraindications = listOf("Acute uncontrolled hyperglycemia"),
            pathyaWholesome = listOf("Warm nourishing grains", "Warm milk with a pinch of turmeric", "Dates"),
            apathyaAvoid = listOf("Refrigerated drinks", "Ice creams", "Stale leftover food")
        ),
        AyurvedaMedicine(
            id = "kumkumadi_tailam",
            name = "Kumkumadi Tailam",
            sanskritName = "कुमकुमादि तैलम् (Miraculous Saffron Oil)",
            category = FormulationCategory.TAILA,
            tagPill = "SKIN RADIANCE",
            shortDescription = "Artisanal saffron-infused elixir crafted for blemishes and golden skin luster.",
            primaryBenefit = "Clears pigmentation, softens texture, imparts natural radiance",
            doshaImpact = "Pacifies Pitta and Rakta (Blood tissue)",
            targetDoshas = listOf(DoshaType.PITTA),
            constituents = listOf("Crocin", "Safranal", "Glycyrrhizin", "Santalols"),
            ingredients = listOf(
                AyurvedaIngredient(
                    name = "Kashmiri Saffron",
                    sanskritName = "कुंकुम",
                    botanicalName = "Crocus sativus",
                    partUsed = "Crimson Stigmas",
                    classicalRole = "Varnya (Complexion enhancer) and blood purifier"
                ),
                AyurvedaIngredient(
                    name = "Rakta Chandana (Red Sandalwood)",
                    sanskritName = "रक्त चन्दन",
                    botanicalName = "Pterocarpus santalinus",
                    partUsed = "Heartwood",
                    classicalRole = "Deeply cooling, eliminates sun pigmentation and redness"
                ),
                AyurvedaIngredient(
                    name = "Manjistha (Indian Madder)",
                    sanskritName = "मञ्जिष्ठा",
                    botanicalName = "Rubia cordifolia",
                    partUsed = "Stems & Roots",
                    classicalRole = "Premier lymph and micro-capillary purifier"
                ),
                AyurvedaIngredient(
                    name = "Pure Sesame Oil base",
                    sanskritName = "तिल तैल",
                    botanicalName = "Sesamum indicum",
                    partUsed = "Cold-pressed seed oil",
                    classicalRole = "Penetrates all 7 layers of the epidermis (*Twak*)"
                )
            ),
            dravyaguna = DravyagunaProfile(
                rasa = listOf("Madhura", "Tikta"),
                virya = "Anushna (Mild soothing warmth)",
                vipaka = "Madhura",
                guna = listOf("Snigdha (Hydrating)", "Sukshma (Deeply penetrative)")
            ),
            dosage = DosageInfo(
                summary = "3-4 Drops • Night Ritual",
                standardDose = "3 to 5 drops",
                frequency = "Once daily before sleep",
                timing = "Night after washing face with pure rosewater",
                anupana = "Topical application gently massaged upwards",
                caution = "For external facial use only."
            ),
            indications = listOf("Uneven skin tone", "Under-eye dark circles", "Blemish scars", "Dry patches"),
            contraindications = listOf("Active cystic pus-filled acne flare-up"),
            pathyaWholesome = listOf("Amla juice", "Watermelon", "Cilantro tea", "Hydrating water"),
            apathyaAvoid = listOf("Excessive direct sun without shade", "Excessive deep-fried salty snacks")
        ),
        AyurvedaMedicine(
            id = "amritarishta",
            name = "Amritarishta",
            sanskritName = "अमृतातर्ष (Fermented Giloy Nectar)",
            category = FormulationCategory.ARISHTA,
            tagPill = "LIVER & FEVER DETOX",
            shortDescription = "Naturally fermented herbal tonic targeting deep metabolic endotoxins.",
            primaryBenefit = "Clears stubborn metabolic toxins, strengthens spleen and liver function",
            doshaImpact = "Pacifies Pitta and Kapha",
            targetDoshas = listOf(DoshaType.PITTA, DoshaType.KAPHA),
            constituents = listOf("Tinosporaside", "Cordifolioside", "Self-generated herbal bio-alcohols"),
            ingredients = listOf(
                AyurvedaIngredient(
                    name = "Guduchi (Giloy / Amrita)",
                    sanskritName = "गुडूची",
                    botanicalName = "Tinospora cordifolia",
                    partUsed = "Fresh Stems",
                    classicalRole = "Jwarahara (Fever pacifier) and deep immunomodulator"
                ),
                AyurvedaIngredient(
                    name = "Dashamula",
                    sanskritName = "दशमूल",
                    botanicalName = "Ten Root Decoction",
                    partUsed = "Roots",
                    classicalRole = "Relieves inflammatory body aches"
                ),
                AyurvedaIngredient(
                    name = "Dhataki Flowers",
                    sanskritName = "धातकी",
                    botanicalName = "Woodfordia fruticosa",
                    partUsed = "Dried Blossoms",
                    classicalRole = "Natural fermentation initiator producing fine biological delivery"
                )
            ),
            dravyaguna = DravyagunaProfile(
                rasa = listOf("Tikta (Intensely bitter)", "Kashaya (Astringent)"),
                virya = "Ushna (Penetrative)",
                vipaka = "Madhura",
                guna = listOf("Laghu (Rapidly absorbed)", "Tikshna")
            ),
            dosage = DosageInfo(
                summary = "15ml - 20ml • After Lunch & Dinner",
                standardDose = "15ml to 25ml",
                frequency = "Twice daily",
                timing = "Immediately after lunch and dinner",
                anupana = "Diluted with equal quantity (1:1) of warm water",
                caution = "Do not take undiluted on completely empty stomach."
            ),
            indications = listOf("Post-viral lethargy", "Low digestive fire", "Chronic recurrent low-grade fever"),
            contraindications = listOf("Acute bleeding ulcers", "Children under 5 without medical supervision"),
            pathyaWholesome = listOf("Light khichdi", "Boiled vegetable broth", "Pomegranate"),
            apathyaAvoid = listOf("Heavy red meat", "Excessive sour pickles", "Stale curd")
        ),
        AyurvedaMedicine(
            id = "dashamula_kwatha",
            name = "Dashamula Kwatha",
            sanskritName = "दशमूल क्वाथ (Ten Roots Decoction)",
            category = FormulationCategory.KWATHA,
            tagPill = "VATA HARMONY",
            shortDescription = "Sacred decoction of ten forest roots that grounds aggravated Vata dosha.",
            primaryBenefit = "Joint comfort, nerve relaxation, easing menstrual cramps & stiffness",
            doshaImpact = "Supreme Vata Shamaka (Grounds erratic nervous energy)",
            targetDoshas = listOf(DoshaType.VATA),
            constituents = listOf("Flavonoid Glycosides", "Sitosterols", "Lupenone"),
            ingredients = listOf(
                AyurvedaIngredient(
                    name = "Bilva (Bael root)",
                    sanskritName = "बिल्व",
                    botanicalName = "Aegle marmelos",
                    partUsed = "Root Bark",
                    classicalRole = "Pacifies Vata-Kapha and supports intestinal gut lining"
                ),
                AyurvedaIngredient(
                    name = "Agnimantha",
                    sanskritName = "अग्निमन्थ",
                    botanicalName = "Premna integrifolia",
                    partUsed = "Root",
                    classicalRole = "Anti-inflammatory and relieves nerve tension"
                ),
                AyurvedaIngredient(
                    name = "Gokshura (Small Caltrops)",
                    sanskritName = "गोक्षुर",
                    botanicalName = "Tribulus terrestris",
                    partUsed = "Roots & Fruit",
                    classicalRole = "Urinary soothing and lower back support"
                )
            ),
            dravyaguna = DravyagunaProfile(
                rasa = listOf("Kashaya (Astringent)", "Tikta (Bitter)"),
                virya = "Ushna (Warm & soothing)",
                vipaka = "Katu",
                guna = listOf("Guru", "Ruksha")
            ),
            dosage = DosageInfo(
                summary = "40ml - 50ml • Twice Daily",
                standardDose = "40ml to 60ml freshly steeped",
                frequency = "Twice daily",
                timing = "30 minutes before meals",
                anupana = "Warm water or with a pinch of fresh ginger paste",
                caution = "Take warm; do not drink cold decoctions."
            ),
            indications = listOf("Lower back ache", "Sciatica stiffness", "Severe postpartum recovery", "Dry cough"),
            contraindications = listOf("Dehydration with severe burning sensation"),
            pathyaWholesome = listOf("Warm cooked oatmeal", "Ghee", "Warm sesame oil massages"),
            apathyaAvoid = listOf("Cold windy exposure", "Carbonated chilled water", "Dry dry cereals")
        ),
        AyurvedaMedicine(
            id = "gokshuradi_guggulu",
            name = "Gokshuradi Guggulu",
            sanskritName = "गोक्षुरादि गुग्गुलु (Renal & Fluid Balance Tablet)",
            category = FormulationCategory.VATI,
            tagPill = "URINARY & DETOX",
            shortDescription = "Synergistic resin tablet for kidney micro-circulation and fluid balance.",
            primaryBenefit = "Uric acid equilibrium, urinary comfort, joint stiffness relief",
            doshaImpact = "Pacifies Vata, Pitta, and Kapha in urinary channels",
            targetDoshas = listOf(DoshaType.PITTA, DoshaType.VATA),
            constituents = listOf("Guggulsterones", "Saponins", "Resins"),
            ingredients = listOf(
                AyurvedaIngredient(
                    name = "Gokshura",
                    sanskritName = "गोक्षुर",
                    botanicalName = "Tribulus terrestris",
                    partUsed = "Fruit & Root",
                    classicalRole = "Mutrala (Diuretic without potassium loss) and kidney tonic"
                ),
                AyurvedaIngredient(
                    name = "Shuddha Guggulu (Purified Resin)",
                    sanskritName = "शुद्ध गुग्गुलु",
                    botanicalName = "Commiphora mukul",
                    partUsed = "Purified Exudate",
                    classicalRole = "Scrapes metabolic crystallization from micro-channels"
                ),
                AyurvedaIngredient(
                    name = "Musta (Nut Grass)",
                    sanskritName = "मुस्ता",
                    botanicalName = "Cyperus rotundus",
                    partUsed = "Rhizome",
                    classicalRole = "Digestive fire promoter, relieves burning micturition"
                )
            ),
            dravyaguna = DravyagunaProfile(
                rasa = listOf("Madhura", "Tikta", "Katu"),
                virya = "Sheeta-Ushna (Neutralizing)",
                vipaka = "Madhura",
                guna = listOf("Laghu", "Ruksha")
            ),
            dosage = DosageInfo(
                summary = "2 Tablets • Twice Daily",
                standardDose = "1 to 2 tablets (500mg each)",
                frequency = "Twice daily",
                timing = "After breakfast and after dinner",
                anupana = "Warm water or Punarnavadi Kwatha",
                caution = "Stay well hydrated throughout the day while taking."
            ),
            indications = listOf("Elevated uric acid", "Joint aches with swelling", "Urinary tract irritation"),
            contraindications = listOf("Kidney failure requiring dialysis without nephrologist approval"),
            pathyaWholesome = listOf("Barley water", "Cucumber", "Coriander seed infusion", "Fresh coconut water"),
            apathyaAvoid = listOf("High purine red meat", "Refined white sugar", "Alcohol", "Sour vinegar")
        )
    )

    val defaultDailyDoses: List<DailyDoseLog> = listOf(
        DailyDoseLog(
            id = "dose_ashwagandha",
            medicineId = "ashwagandha_root",
            medicineName = "Ashwagandha Root",
            doseLabel = "500mg",
            timing = "After Breakfast",
            iconEmoji = "🍵",
            isLogged = true,
            loggedAtTime = "8:15 AM"
        ),
        DailyDoseLog(
            id = "dose_triphala",
            medicineId = "triphala_churna",
            medicineName = "Triphala Churna",
            doseLabel = "3g",
            timing = "Before Bedtime",
            iconEmoji = "🌿",
            isLogged = false,
            loggedAtTime = null
        ),
        DailyDoseLog(
            id = "dose_brahmi",
            medicineId = "brahmi_vati",
            medicineName = "Brahmi Vati",
            doseLabel = "1 Tablet",
            timing = "After Lunch",
            iconEmoji = "✨",
            isLogged = false,
            loggedAtTime = null
        )
    )

    val defaultHabits: List<DailyHabit> = listOf(
        DailyHabit(
            id = "habit_pranayama",
            title = "Afternoon Pranayama",
            scheduledTime = "4:30 PM",
            iconEmoji = "🧘",
            description = "10 minutes of Nadi Shodhana (Alternate Nostril Breathing) for calming nervous tension.",
            isCompleted = false
        ),
        DailyHabit(
            id = "habit_gandusha",
            title = "Morning Oil Pulling (Gandusha)",
            scheduledTime = "7:00 AM",
            iconEmoji = "🪥",
            description = "Swish 1 tbsp warm sesame oil for 5-10 minutes to strengthen gums and draw oral toxins.",
            isCompleted = true
        ),
        DailyHabit(
            id = "habit_golden_milk",
            title = "Evening Golden Milk (Haldi Doodh)",
            scheduledTime = "9:30 PM",
            iconEmoji = "🥛",
            description = "Warm milk with turmeric, crushed black pepper, and nutmeg for deep restorative sleep.",
            isCompleted = false
        )
    )

    val prakritiQuestions: List<PrakritiQuestion> = listOf(
        PrakritiQuestion(
            id = 1,
            trait = "Body Frame & Physical Build",
            optionVata = "Slender, light, prominent joints, difficulty gaining weight",
            optionPitta = "Medium frame, moderate muscular tone, steady weight",
            optionKapha = "Broad frame, sturdy build, tendency to gain weight easily"
        ),
        PrakritiQuestion(
            id = 2,
            trait = "Digestive Fire (Agni) & Appetite",
            optionVata = "Irregular: sometimes voracious, sometimes forget to eat; prone to gas",
            optionPitta = "Strong & fiery: irritable if meals are delayed, fast digestion",
            optionKapha = "Slow & steady: can comfortably skip meals, slow metabolism"
        ),
        PrakritiQuestion(
            id = 3,
            trait = "Mental Temperament & Reaction to Stress",
            optionVata = "Quick, imaginative, prone to worry, anxiety, and scattered thoughts",
            optionPitta = "Sharp, focused, ambitious, prone to impatience, anger, and perfectionism",
            optionKapha = "Calm, affectionate, steady, resistant to sudden change, unhurried"
        ),
        PrakritiQuestion(
            id = 4,
            trait = "Sleep Quality & Patterns",
            optionVata = "Light, restless, prone to waking up between 2 AM and 4 AM",
            optionPitta = "Moderate (6-7 hrs), vivid dreams, wakes up alert and ready",
            optionKapha = "Deep, heavy (8+ hrs), difficult to wake up in early morning"
        )
    )

    val defaultUsers: List<AppUser> = listOf(
        AppUser(
            id = "user_admin_vasant",
            name = "Dr. Vasant Sharma",
            email = "admin.vasant@ayurguide.org",
            role = UserRole.ADMIN,
            prakriti = DoshaType.TRIDOSHIC,
            status = UserStatus.ACTIVE,
            designation = "Chief Vaidya & Clinical Director",
            phone = "+91 98450 11001",
            registeredDate = "Oct 15, 2024",
            lastActive = "Active now",
            adherencePercent = 98,
            clinicalNotes = "Oversees Ayurvedic Pharmacopoeia compliance and formulation batches."
        ),
        AppUser(
            id = "user_practitioner_meera",
            name = "Dr. Meera Nambiar",
            email = "dr.meera@ayurguide.org",
            role = UserRole.PRACTITIONER,
            prakriti = DoshaType.PITTA,
            status = UserStatus.ACTIVE,
            designation = "Senior Ayurvedic Physician",
            phone = "+91 98450 22002",
            registeredDate = "Nov 02, 2024",
            lastActive = "12 mins ago",
            adherencePercent = 94,
            clinicalNotes = "Specialist in Dravyaguna (Herbal pharmacology) & Kayachikitsa."
        ),
        AppUser(
            id = "user_practitioner_kabir",
            name = "Dr. Kabir Deshmukh",
            email = "dr.kabir@ayurguide.org",
            role = UserRole.PRACTITIONER,
            prakriti = DoshaType.VATA,
            status = UserStatus.ACTIVE,
            designation = "Consultant Vaidya",
            phone = "+91 98450 33003",
            registeredDate = "Jan 05, 2025",
            lastActive = "1 hour ago",
            adherencePercent = 91,
            clinicalNotes = "Focuses on Dinacharya routines and nervous balance."
        ),
        AppUser(
            id = "user_patient_arjun",
            name = "Arjun Mehta",
            email = "arjun.m@example.com",
            role = UserRole.PATIENT,
            prakriti = DoshaType.PITTA,
            status = UserStatus.ACTIVE,
            designation = "Wellness Seeker",
            phone = "+91 98450 44004",
            registeredDate = "Jan 12, 2026",
            lastActive = "Just now",
            adherencePercent = 88,
            assignedPractitioner = "Dr. Meera Nambiar",
            clinicalNotes = "Monitoring Pitta digestive acid sensitivity; taking Triphala & Brahmi."
        ),
        AppUser(
            id = "user_patient_priya",
            name = "Priya Sundaram",
            email = "priya.s@example.com",
            role = UserRole.PATIENT,
            prakriti = DoshaType.VATA,
            status = UserStatus.ACTIVE,
            designation = "Wellness Seeker",
            phone = "+91 98450 55005",
            registeredDate = "Feb 01, 2026",
            lastActive = "3 hours ago",
            adherencePercent = 78,
            assignedPractitioner = "Dr. Kabir Deshmukh",
            clinicalNotes = "Vata insomnia management with Ashwagandha and evening Golden Milk."
        ),
        AppUser(
            id = "user_patient_devika",
            name = "Devika Roy",
            email = "devika.r@example.com",
            role = UserRole.PATIENT,
            prakriti = DoshaType.KAPHA,
            status = UserStatus.ACTIVE,
            designation = "Wellness Seeker",
            phone = "+91 98450 66006",
            registeredDate = "Feb 14, 2026",
            lastActive = "Yesterday",
            adherencePercent = 82,
            assignedPractitioner = "Dr. Meera Nambiar",
            clinicalNotes = "Kapha metabolic rekindling with Dashamoola & ginger decoctions."
        ),
        AppUser(
            id = "user_patient_rahul",
            name = "Rahul Verma",
            email = "rahul.v@example.com",
            role = UserRole.PATIENT,
            prakriti = DoshaType.PITTA,
            status = UserStatus.SUSPENDED,
            designation = "Account Suspended",
            phone = "+91 98450 77007",
            registeredDate = "Dec 18, 2025",
            lastActive = "5 days ago",
            adherencePercent = 45,
            clinicalNotes = "Requires consultation validation before resuming herb regimen."
        )
    )

    val defaultAuditLogs: List<AuditLogEntry> = listOf(
        AuditLogEntry(
            id = "log_1",
            timestamp = "10:45 AM Today",
            actorName = "Dr. Vasant Sharma (Admin)",
            actionType = "FORMULATION_VERIFY",
            targetItem = "Ashwagandha Churna",
            details = "Batch #AYUR-2026-B12 passed heavy metals & microbial purity assays under API guidelines.",
            isWarning = false
        ),
        AuditLogEntry(
            id = "log_2",
            timestamp = "09:30 AM Today",
            actorName = "Dr. Meera Nambiar (Vaidya)",
            actionType = "PRESCRIPTION_ISSUED",
            targetItem = "Triphala Churna (3g)",
            details = "Prescribed to patient Arjun Mehta for Pitta digestive regulation with warm water anupana.",
            isWarning = false
        ),
        AuditLogEntry(
            id = "log_3",
            timestamp = "Yesterday 04:15 PM",
            actorName = "Inventory System",
            actionType = "LOW_STOCK_ALERT",
            targetItem = "Kumkumadi Tailam",
            details = "Stock dropped to 8 units. Automated re-order threshold reached for raw Saffron (Kumkuma).",
            isWarning = true
        ),
        AuditLogEntry(
            id = "log_4",
            timestamp = "Yesterday 11:20 AM",
            actorName = "Dr. Vasant Sharma (Admin)",
            actionType = "ROLE_MODIFICATION",
            targetItem = "Dr. Kabir Deshmukh",
            details = "Privilege granted: Clinical Vaidya Practitioner credentials certified.",
            isWarning = false
        ),
        AuditLogEntry(
            id = "log_5",
            timestamp = "Mar 01, 2026",
            actorName = "Security Gateway",
            actionType = "ACCOUNT_SUSPENSION",
            targetItem = "Rahul Verma",
            details = "Temporarily suspended due to unconfirmed contraindication flags.",
            isWarning = true
        )
    )
}
