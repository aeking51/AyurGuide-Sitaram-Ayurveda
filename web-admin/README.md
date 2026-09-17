# AyurGuide Clinical Admin Web Portal

This document outlines the blueprint and instructions for creating the dedicated **AyurGuide Web Administration Portal** in **Google AI Studio**.

---

## 1. How to Create the Web App in Google AI Studio

1. Open [Google AI Studio Build](https://ai.studio/build).
2. Click **Create New App** (or "+ New Application").
3. When prompted for the platform/template, select **Web** (React / Vite + Tailwind CSS or Next.js).
4. Name the application: **AyurGuide Admin Portal**.
5. Paste the prompt below into the AI Studio prompt box.

---

## 2. Copy-and-Paste Prompt for the Web App

Copy and paste this exact prompt into your new AI Studio Web project:

```text
Build a clinical desktop Web Administration Portal for AyurGuide (an Ayurvedic medicine management and clinical care platform) using React, Tailwind CSS, Lucide icons, and the Firebase Web SDK.

The web portal must connect to Cloud Firestore to manage the exact same backend collections as the AyurGuide Android mobile app:

1. Brand & Aesthetic Design:
   - Botanical, warm clinical aesthetic: Parchment light canvas (#FAF7F2), deep moss primary (#2D4A3E), sage accents (#E3EADF), terracotta alert tones (#C86446), and warm amber gold (#9A7B38).
   - High information-density desktop layout optimized for 1280px+ wide screens with clean data tables, quick stat cards, and modal sheets.

2. Core Navigation & Modules:
   - Module A: Formulations & Inventory (Collection: 'medicines')
     * Searchable, filterable data table displaying Sanskrit name, commercial name, category (Churna, Vati, Arishta, Taila, Ghrita, Rasayana, Kwatha), target doshas (Vata, Pitta, Kapha, Tridoshic), health goals (Digestion, Immunity, Stress Relief, Cognition, Skin Radiance, Joints & Mobility, Detox), stock units, batch number, and low stock warnings.
     * Quick in-line stock adjuster (+/- buttons or direct numeric input with real-time Firestore update).
     * Multi-step or tabbed "Add / Edit Formulation" modal:
       - General: Name, Sanskrit name, formulation category, tag pill, health goals (multi-select), primary benefit, short description.
       - Classical Energetics (Dravyaguna): Rasa (tastes), Virya (potency), Vipaka (post-digestive effect), Guna (qualities).
       - Ingredients: Dynamic list with herb name, Sanskrit botanical name, part used, and classical therapeutic role.
       - Dosage & Clinical Rules: Recommended dose, timing, frequency, traditional Anupana vehicle, contraindications, and pathya/wholesome foods.
       - Inventory & Batch: Stock count, batch number, low stock threshold.

   - Module B: User & Practitioner Directory (Collections: 'users' & 'user_roles')
     * Table of all registered users with avatar initials, email, current role (ADMIN, PRACTITIONER, PATIENT / WELLNESS SEEKER), verification status (VERIFIED, PENDING_VERIFICATION, SUSPENDED), and creation timestamp.
     * Role elevation dropdown to instantly switch roles with audit logging.
     * Status toggle to approve or suspend accounts.

   - Module C: Clinical Audit Trail (Collection: 'audit_logs')
     * Real-time chronological audit table recording: timestamp, user email, action type (STOCK_UPDATE, ROLE_CHANGE, MEDICINE_CREATE, DOSHA_ASSESSMENT), and details.

3. Firebase Configuration:
   - Use standard Firebase config (apiKey, authDomain, projectId, storageBucket, messagingSenderId, appId) configured via environment variables or a settings modal where administrators can enter their Firebase credentials.
   - Use real-time Firestore listeners (onSnapshot) so any update from the mobile app is reflected instantly on the web portal without refreshing.
```

---

## 3. Cloud Firestore Data Schema Compatibility

Your web application will read and write to these exact Firestore collections:

### Collection: `medicines`
Document ID: e.g. `triphala_churna` or auto-generated UUID
```json
{
  "id": "triphala_churna",
  "name": "Triphala Churna",
  "sanskritName": "त्रिफला चूर्ण (Three Sacred Fruits)",
  "category": "CHURNA",
  "tagPill": "DIGESTIVE DETOX",
  "shortDescription": "Classic three-fruit formulation for gentle colon cleanse and systemic detox.",
  "primaryBenefit": "Gentle bowel motility, antioxidant protection, ocular health",
  "doshaImpact": "Tridoshic Harmony (Balances Vata, Pitta, Kapha)",
  "targetDoshas": ["TRIDOSHIC", "PITTA", "KAPHA"],
  "healthGoals": ["DIGESTION", "DETOX"],
  "constituents": ["Tannins", "Gallic Acid", "Vitamin C"],
  "ingredients": [
    {
      "name": "Amalaki",
      "sanskritName": "आमलकी",
      "botanicalName": "Emblica officinalis",
      "partUsed": "Dried Pericarp",
      "classicalRole": "Rich natural Vitamin C, cooling Pitta regulator"
    }
  ],
  "dravyaguna": {
    "rasa": ["Pancha-Rasa (Except salty)"],
    "virya": "Sheeta & Anushna",
    "vipaka": "Madhura",
    "guna": ["Laghu", "Ruksha"]
  },
  "dosage": {
    "summary": "3g - 5g • At Bedtime",
    "standardDose": "1/2 to 1 teaspoon (3g - 5g)",
    "frequency": "Once daily before sleep",
    "timing": "30-45 minutes after dinner before bed",
    "anupana": "Warm water or honey",
    "caution": "Avoid during acute diarrhea."
  },
  "indications": ["Sluggish bowel habits", "Toxin accumulation (Ama)"],
  "contraindications": ["Dysentery", "Acute dehydration"],
  "pathyaWholesome": ["Warm water", "Steamed vegetables"],
  "apathyaAvoid": ["Fried heavy foods", "Cold curd"],
  "stockUnits": 45,
  "batchNumber": "AYUR-2026-B12",
  "isLowStock": false
}
```

### Collection: `user_roles`
Document ID: `userId` (matches Firebase Auth UID)
```json
{
  "userId": "usr_991823",
  "email": "practitioner@ayurveda.org",
  "role": "PRACTITIONER",
  "roleTitle": "Practitioner",
  "badgeLabel": "PRACTITIONER",
  "roleDescription": "Clinical consultations, formulation prescribing, patient health reviews",
  "updatedBy": "admin@ayurveda.org",
  "updatedAt": 1726286400000
}
```

### Collection: `audit_logs`
Document ID: Auto-generated
```json
{
  "timestamp": 1726286400000,
  "userEmail": "admin@ayurveda.org",
  "actionType": "STOCK_UPDATE",
  "entityId": "triphala_churna",
  "details": "Restocked Triphala Churna (+50 units). Current inventory: 95 units."
}
```
