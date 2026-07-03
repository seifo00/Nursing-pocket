package com.example.ui

enum class AppLanguage(val code: String, val displayName: String, val isRtl: Boolean) {
    FR("fr", "Français", false),
    AR("ar", "العربية", true),
    EN("en", "English", false)
}

object Translation {
    fun get(key: String, lang: AppLanguage): String {
        return dict[key]?.get(lang) ?: key
    }

    private val dict = mapOf(
        "app_title" to mapOf(
            AppLanguage.FR to "Nurse Pocket",
            AppLanguage.AR to "محفظة الممرض",
            AppLanguage.EN to "Nurse Pocket"
        ),
        "app_subtitle" to mapOf(
            AppLanguage.FR to "Assistant infirmier de poche",
            AppLanguage.AR to "مساعد التمريض في جيبك",
            AppLanguage.EN to "Pocket Nursing Assistant"
        ),
        "back" to mapOf(
            AppLanguage.FR to "Retour",
            AppLanguage.AR to "رجوع",
            AppLanguage.EN to "Back"
        ),
        "save" to mapOf(
            AppLanguage.FR to "Enregistrer",
            AppLanguage.AR to "حفظ",
            AppLanguage.EN to "Save"
        ),
        "cancel" to mapOf(
            AppLanguage.FR to "Annuler",
            AppLanguage.AR to "إلغاء",
            AppLanguage.EN to "Cancel"
        ),
        "delete" to mapOf(
            AppLanguage.FR to "Supprimer",
            AppLanguage.AR to "حذف",
            AppLanguage.EN to "Delete"
        ),
        "edit" to mapOf(
            AppLanguage.FR to "Modifier",
            AppLanguage.AR to "تعديل",
            AppLanguage.EN to "Edit"
        ),
        "search" to mapOf(
            AppLanguage.FR to "Rechercher...",
            AppLanguage.AR to "بحث...",
            AppLanguage.EN to "Search..."
        ),
        "empty_notes" to mapOf(
            AppLanguage.FR to "Aucune note trouvée. Appuyez sur + pour en ajouter.",
            AppLanguage.AR to "لا توجد ملاحظات. اضغط على + لإضافة واحدة.",
            AppLanguage.EN to "No notes found. Tap + to add one."
        ),
        "note_title" to mapOf(
            AppLanguage.FR to "Titre",
            AppLanguage.AR to "العنوان",
            AppLanguage.EN to "Title"
        ),
        "note_content" to mapOf(
            AppLanguage.FR to "Contenu de la note...",
            AppLanguage.AR to "محتوى الملاحظة...",
            AppLanguage.EN to "Note content..."
        ),
        "add_note" to mapOf(
            AppLanguage.FR to "Ajouter une note",
            AppLanguage.AR to "إضافة ملاحظة",
            AppLanguage.EN to "Add Note"
        ),
        "edit_note" to mapOf(
            AppLanguage.FR to "Modifier la note",
            AppLanguage.AR to "تعديل الملاحظة",
            AppLanguage.EN to "Edit Note"
        ),
        
        // Main Cards
        "card_doses" to mapOf(
            AppLanguage.FR to "Calculateur de doses",
            AppLanguage.AR to "حساب جرعات الأدوية",
            AppLanguage.EN to "Dose Calculator"
        ),
        "card_perfusion" to mapOf(
            AppLanguage.FR to "Calculateur de perfusion",
            AppLanguage.AR to "سرعة التسريب الوريدي",
            AppLanguage.EN to "Infusion Calculator"
        ),
        "card_ecg" to mapOf(
            AppLanguage.FR to "ECG Quick Guide",
            AppLanguage.AR to "دليل تخطيط القلب السريع",
            AppLanguage.EN to "ECG Quick Guide"
        ),
        "card_scores" to mapOf(
            AppLanguage.FR to "Scores médicaux",
            AppLanguage.AR to "المؤشرات والسكور الطبي",
            AppLanguage.EN to "Medical Scores"
        ),
        "card_drugs" to mapOf(
            AppLanguage.FR to "Médicaments d'urgence",
            AppLanguage.AR to "أدوية الطوارئ",
            AppLanguage.EN to "Emergency Drugs"
        ),
        "card_protocols" to mapOf(
            AppLanguage.FR to "Protocoles infirmiers",
            AppLanguage.AR to "البروتوكولات التمريضية",
            AppLanguage.EN to "Nursing Protocols"
        ),
        "card_notes" to mapOf(
            AppLanguage.FR to "Notes rapides",
            AppLanguage.AR to "ملاحظات سريعة",
            AppLanguage.EN to "Quick Notes"
        ),
        "card_timers" to mapOf(
            AppLanguage.FR to "Timers & Alarmes",
            AppLanguage.AR to "المؤقتات والتنبيهات",
            AppLanguage.EN to "Timers & Alarms"
        ),
        "card_converter" to mapOf(
            AppLanguage.FR to "Convertisseur médical",
            AppLanguage.AR to "المحول الطبي",
            AppLanguage.EN to "Medical Converter"
        ),
        "card_settings" to mapOf(
            AppLanguage.FR to "Paramètres",
            AppLanguage.AR to "الإعدادات",
            AppLanguage.EN to "Settings"
        ),
        "card_ai_assistant" to mapOf(
            AppLanguage.FR to "Assistant Infirmier IA",
            AppLanguage.AR to "المساعد الذكي للتمريض",
            AppLanguage.EN to "AI Nurse Assistant"
        ),
        "card_drug_safety" to mapOf(
            AppLanguage.FR to "Sécurité Posologique",
            AppLanguage.AR to "الأمان الدوائي الذكي",
            AppLanguage.EN to "Smart Drug Safety"
        ),
        "card_voice_charting" to mapOf(
            AppLanguage.FR to "Dictée Vocale Clinique",
            AppLanguage.AR to "التدوين الصوتي السريري",
            AppLanguage.EN to "Voice Charting"
        ),
        "card_shift_assistant" to mapOf(
            AppLanguage.FR to "Gestion de Garde",
            AppLanguage.AR to "مساعد الوردية والمهام",
            AppLanguage.EN to "Shift Assistant"
        ),
        "card_emergency_mode" to mapOf(
            AppLanguage.FR to "MODE URGENCE VITALE",
            AppLanguage.AR to "طوارئ الإنعاش السريع",
            AppLanguage.EN to "EMERGENCY PROTOCOLS"
        ),
        "card_nursing_simulator" to mapOf(
            AppLanguage.FR to "Simulateur Clinique",
            AppLanguage.AR to "المحاكي السريري",
            AppLanguage.EN to "Clinical Simulator"
        ),
        "card_wound_tracker" to mapOf(
            AppLanguage.FR to "Suivi des Pansements",
            AppLanguage.AR to "متابعة التئام الجروح",
            AppLanguage.EN to "Wound Tracker"
        ),
        "card_med_scanner" to mapOf(
            AppLanguage.FR to "Scanner Médicament",
            AppLanguage.AR to "ماسح باركود الأدوية",
            AppLanguage.EN to "Medication Scanner"
        ),
        "card_gamification" to mapOf(
            AppLanguage.FR to "Progression & Badges",
            AppLanguage.AR to "الرتب والأوسمة التمريضية",
            AppLanguage.EN to "Rewards & Achievements"
        ),

        // Dose Calc
        "dose_poids" to mapOf(
            AppLanguage.FR to "Poids du patient (kg)",
            AppLanguage.AR to "وزن المريض (كغ)",
            AppLanguage.EN to "Patient Weight (kg)"
        ),
        "dose_age" to mapOf(
            AppLanguage.FR to "Âge du patient (Optionnel)",
            AppLanguage.AR to "العمر (اختياري)",
            AppLanguage.EN to "Patient Age (Optional)"
        ),
        "dose_prescrite" to mapOf(
            AppLanguage.FR to "Dose prescrite (mg/kg)",
            AppLanguage.AR to "الجرعة الموصوفة (ملغ/كغ)",
            AppLanguage.EN to "Prescribed Dose (mg/kg)"
        ),
        "dose_concent_mg" to mapOf(
            AppLanguage.FR to "Concentration dispo (mg)",
            AppLanguage.AR to "التركيز المتوفر (ملغ)",
            AppLanguage.EN to "Available Drug (mg)"
        ),
        "dose_concent_ml" to mapOf(
            AppLanguage.FR to "Dans volume (ml)",
            AppLanguage.AR to "في حجم (مل)",
            AppLanguage.EN to "In Volume (ml)"
        ),
        "dose_result_title" to mapOf(
            AppLanguage.FR to "Résultats du calcul",
            AppLanguage.AR to "نتائج الحساب",
            AppLanguage.EN to "Calculation Results"
        ),
        "dose_total" to mapOf(
            AppLanguage.FR to "Dose totale requise :",
            AppLanguage.AR to "الجرعة الإجمالية المطلوبة:",
            AppLanguage.EN to "Total Dose Required:"
        ),
        "dose_volume" to mapOf(
            AppLanguage.FR to "Volume à administrer :",
            AppLanguage.AR to "الحجم المراد إعطاؤه (مل):",
            AppLanguage.EN to "Volume to Administer:"
        ),
        "dose_example_title" to mapOf(
            AppLanguage.FR to "Exemple d'utilisation",
            AppLanguage.AR to "مثال توضيحي",
            AppLanguage.EN to "Example of Usage"
        ),
        "dose_example_desc" to mapOf(
            AppLanguage.FR to "Patient de 70 kg, prescription de 5 mg/kg, produit dispo en 500 mg / 10 ml.\n-> Dose totale = 350 mg\n-> Volume = 7 ml",
            AppLanguage.AR to "مريض وزنه 70 كغ، الجرعة 5 ملغ/كغ، الدواء المتوفر 500 ملغ في 10 مل.\n<- الجرعة الإجمالية = 350 ملغ\n<- الحجم المطلوب = 7 مل",
            AppLanguage.EN to "Patient 70 kg, prescription 5 mg/kg, available 500 mg / 10 ml.\n-> Total Dose = 350 mg\n-> Volume = 7 ml"
        ),

        // Perfusion Calc
        "perf_volume" to mapOf(
            AppLanguage.FR to "Volume total (ml)",
            AppLanguage.AR to "الحجم الإجمالي (مل)",
            AppLanguage.EN to "Total Volume (ml)"
        ),
        "perf_duree" to mapOf(
            AppLanguage.FR to "Durée",
            AppLanguage.AR to "المدة",
            AppLanguage.EN to "Duration"
        ),
        "perf_duree_unit" to mapOf(
            AppLanguage.FR to "Unité de durée",
            AppLanguage.AR to "وحدة الوقت",
            AppLanguage.EN to "Duration Unit"
        ),
        "perf_hours" to mapOf(
            AppLanguage.FR to "Heures",
            AppLanguage.AR to "ساعات",
            AppLanguage.EN to "Hours"
        ),
        "perf_minutes" to mapOf(
            AppLanguage.FR to "Minutes",
            AppLanguage.AR to "دقائق",
            AppLanguage.EN to "Minutes"
        ),
        "perf_factor" to mapOf(
            AppLanguage.FR to "Facteur de goutte (gtt/ml)",
            AppLanguage.AR to "عامل التقطير (قطرة/مل)",
            AppLanguage.EN to "Drop Factor (gtt/ml)"
        ),
        "perf_factor_standard" to mapOf(
            AppLanguage.FR to "Standard (20 gtt/ml - Standard)",
            AppLanguage.AR to "معياري (20 قطرة/مل - جهاز عادي)",
            AppLanguage.EN to "Standard (20 gtt/ml - Normal IV)"
        ),
        "perf_factor_micro" to mapOf(
            AppLanguage.FR to "Microgoutte (60 gtt/ml - Pédiatrique)",
            AppLanguage.AR to "دقيق (60 قطرة/مل - أطفال)",
            AppLanguage.EN to "Microdrip (60 gtt/ml - Pediatric)"
        ),
        "perf_factor_sang" to mapOf(
            AppLanguage.FR to "Sang / Transfusion (15 gtt/ml)",
            AppLanguage.AR to "دم / نقل دم (15 قطرة/مل)",
            AppLanguage.EN to "Blood / Transfusion (15 gtt/ml)"
        ),
        "perf_result_flow" to mapOf(
            AppLanguage.FR to "Débit en ml/h :",
            AppLanguage.AR to "معدل التدفق (مل/ساعة):",
            AppLanguage.EN to "Flow Rate (ml/h):"
        ),
        "perf_result_drops" to mapOf(
            AppLanguage.FR to "Vitesse en gouttes/min :",
            AppLanguage.AR to "سرعة التسريب (قطرة/دقيقة):",
            AppLanguage.EN to "Infusion Rate (drops/min):"
        ),

        // ECG Guide
        "ecg_desc" to mapOf(
            AppLanguage.FR to "Description",
            AppLanguage.AR to "الوصف",
            AppLanguage.EN to "Description"
        ),
        "ecg_signs" to mapOf(
            AppLanguage.FR to "Signes caractéristiques",
            AppLanguage.AR to "العلامات المميزة",
            AppLanguage.EN to "Key Signs"
        ),
        "ecg_nursing" to mapOf(
            AppLanguage.FR to "Action infirmière primaire",
            AppLanguage.AR to "الإجراء التمريضي الأولي",
            AppLanguage.EN to "Primary Nursing Action"
        ),

        // Medical Scores
        "score_select" to mapOf(
            AppLanguage.FR to "Sélectionner un score",
            AppLanguage.AR to "اختر المؤشر الطبي",
            AppLanguage.EN to "Select a Score"
        ),
        "score_result" to mapOf(
            AppLanguage.FR to "Score obtenu :",
            AppLanguage.AR to "النتيجة المحصلة:",
            AppLanguage.EN to "Calculated Score:"
        ),
        "score_interpret" to mapOf(
            AppLanguage.FR to "Interprétation :",
            AppLanguage.AR to "التفسير الطبي:",
            AppLanguage.EN to "Interpretation:"
        ),

        // Emergency Drugs
        "drug_class" to mapOf(
            AppLanguage.FR to "Classe thérapeutique",
            AppLanguage.AR to "الفئة العلاجية",
            AppLanguage.EN to "Therapeutic Class"
        ),
        "drug_indications" to mapOf(
            AppLanguage.FR to "Indications",
            AppLanguage.AR to "دواعي الاستعمال",
            AppLanguage.EN to "Indications"
        ),
        "drug_poso_adult" to mapOf(
            AppLanguage.FR to "Posologie Adulte",
            AppLanguage.AR to "الجرعة للبالغين",
            AppLanguage.EN to "Adult Dosage"
        ),
        "drug_poso_pedia" to mapOf(
            AppLanguage.FR to "Posologie Pédiatrique",
            AppLanguage.AR to "الجرعة للأطفال",
            AppLanguage.EN to "Pediatric Dosage"
        ),
        "drug_side_effects" to mapOf(
            AppLanguage.FR to "Effets secondaires",
            AppLanguage.AR to "الأعراض الجانبية",
            AppLanguage.EN to "Side Effects"
        ),
        "drug_contra" to mapOf(
            AppLanguage.FR to "Contre-indications",
            AppLanguage.AR to "موانع الاستعمال",
            AppLanguage.EN to "Contraindications"
        ),
        "drug_admin_mode" to mapOf(
            AppLanguage.FR to "Mode d'administration",
            AppLanguage.AR to "طريقة الإعطاء",
            AppLanguage.EN to "Route of Administration"
        ),

        // Protocols
        "protocol_steps" to mapOf(
            AppLanguage.FR to "Étapes du protocole",
            AppLanguage.AR to "خطوات البروتوكول",
            AppLanguage.EN to "Protocol Steps"
        ),

        // Timers
        "timer_set" to mapOf(
            AppLanguage.FR to "Lancer un minuteur",
            AppLanguage.AR to "تشغيل مؤقت جديد",
            AppLanguage.EN to "Start a Timer"
        ),
        "timer_label" to mapOf(
            AppLanguage.FR to "Nom du rappel (ex: perfusion, insuline...)",
            AppLanguage.AR to "اسم التذكير (مثال: سيروم، دواء، ضغط...)",
            AppLanguage.EN to "Reminder label (e.g. infusion, insulin...)"
        ),
        "timer_duration" to mapOf(
            AppLanguage.FR to "Durée (minutes)",
            AppLanguage.AR to "المدة (بالدقائق)",
            AppLanguage.EN to "Duration (minutes)"
        ),
        "timer_active" to mapOf(
            AppLanguage.FR to "Minuteurs actifs",
            AppLanguage.AR to "المؤقتات النشطة",
            AppLanguage.EN to "Active Timers"
        ),
        "timer_start_btn" to mapOf(
            AppLanguage.FR to "Démarrer",
            AppLanguage.AR to "تشغيل",
            AppLanguage.EN to "Start"
        ),
        "timer_sec_left" to mapOf(
            AppLanguage.FR to "restantes",
            AppLanguage.AR to "متبقية",
            AppLanguage.EN to "left"
        ),

        // Converter
        "conv_from" to mapOf(
            AppLanguage.FR to "Valeur à convertir",
            AppLanguage.AR to "القيمة المراد تحويلها",
            AppLanguage.EN to "Value to convert"
        ),
        "conv_result" to mapOf(
            AppLanguage.FR to "Résultat :",
            AppLanguage.AR to "النتيجة:",
            AppLanguage.EN to "Result:"
        ),

        // Settings
        "settings_lang" to mapOf(
            AppLanguage.FR to "Langue de l'application",
            AppLanguage.AR to "لغة التطبيق",
            AppLanguage.EN to "App Language"
        ),
        "settings_theme" to mapOf(
            AppLanguage.FR to "Mode sombre",
            AppLanguage.AR to "الوضع الليلي (الداكن)",
            AppLanguage.EN to "Dark Mode"
        ),
        "settings_about" to mapOf(
            AppLanguage.FR to "À propos",
            AppLanguage.AR to "حول التطبيق",
            AppLanguage.EN to "About"
        ),
        "settings_about_desc" to mapOf(
            AppLanguage.FR to "Nurse Pocket v1.0\nUn outil professionnel de poche conçu pour accompagner les infirmiers et étudiants en soins infirmiers dans leur pratique quotidienne.\nFonctionne 100% hors-ligne.",
            AppLanguage.AR to "محفظة الممرض Nurse Pocket إصدار 1.0\nأداة مهنية جيبية مصممة لمرافقة الممرضين وطلبة التمريض في ممارساتهم اليومية بالمستشفيات.\nيعمل 100% بدون إنترنت.",
            AppLanguage.EN to "Nurse Pocket v1.0\nA professional pocket suite designed to support nurses and nursing students in their daily clinical practice.\nWorks 100% offline."
        ),

        // Drug Interactions
        "card_interact" to mapOf(
            AppLanguage.FR to "Interactions Médicaments",
            AppLanguage.AR to "تفاعلات الأدوية",
            AppLanguage.EN to "Drug Interactions"
        ),
        "interact_title" to mapOf(
            AppLanguage.FR to "Interactions",
            AppLanguage.AR to "تفاعلات الأدوية",
            AppLanguage.EN to "Drug Interactions"
        ),
        "interact_select_prompt" to mapOf(
            AppLanguage.FR to "Sélectionnez des médicaments :",
            AppLanguage.AR to "اختر الأدوية المراد فحصها:",
            AppLanguage.EN to "Select medications to check:"
        ),
        "interact_selected" to mapOf(
            AppLanguage.FR to "Médicaments sélectionnés",
            AppLanguage.AR to "الأدوية المحددة للفحص",
            AppLanguage.EN to "Selected Medications"
        ),
        "interact_search_hint" to mapOf(
            AppLanguage.FR to "Rechercher un médicament...",
            AppLanguage.AR to "بحث عن دواء...",
            AppLanguage.EN to "Search medication..."
        ),
        "interact_empty_title" to mapOf(
            AppLanguage.FR to "Aucun médicament sélectionné",
            AppLanguage.AR to "لم يتم تحديد أي دواء",
            AppLanguage.EN to "No medications selected"
        ),
        "interact_empty_desc" to mapOf(
            AppLanguage.FR to "Sélectionnez au moins deux médicaments ci-dessous pour identifier instantanément les interactions indésirables, les mécanismes cliniques et les conseils infirmiers de surveillance.",
            AppLanguage.AR to "اختر دواءين على الأقل من القائمة أدناه لفحص التفاعلات الدوائية العكسية والآليات السريرية ونصائح المراقبة التمريضية في جيبك.",
            AppLanguage.EN to "Select at least two medications below to instantly check for adverse drug-drug interactions, clinical mechanisms, and nursing monitoring advice."
        ),
        "interact_no_interaction" to mapOf(
            AppLanguage.FR to "Aucune interaction majeure détectée entre ces médicaments dans notre base clinique.",
            AppLanguage.AR to "لم يتم العثور على أي تفاعلات دوائية خطيرة بين الأدوية المحددة في قاعدتنا السريرية.",
            AppLanguage.EN to "No major drug-drug interactions detected between these medications in our clinical database."
        ),
        "interact_severity" to mapOf(
            AppLanguage.FR to "Gravité",
            AppLanguage.AR to "الخطورة",
            AppLanguage.EN to "Severity"
        ),
        "interact_mechanism" to mapOf(
            AppLanguage.FR to "Mécanisme",
            AppLanguage.AR to "آلية التأثير",
            AppLanguage.EN to "Mechanism"
        ),
        "interact_advice" to mapOf(
            AppLanguage.FR to "Surveillance Infirmière",
            AppLanguage.AR to "المراقبة التمريضية",
            AppLanguage.EN to "Nursing Monitoring"
        ),
        "severity_critical" to mapOf(
            AppLanguage.FR to "CRITIQUE",
            AppLanguage.AR to "خطير جداً",
            AppLanguage.EN to "CRITICAL"
        ),
        "severity_high" to mapOf(
            AppLanguage.FR to "RISQUE ÉLEVÉ",
            AppLanguage.AR to "خطورة عالية",
            AppLanguage.EN to "HIGH RISK"
        ),
        "severity_monitor" to mapOf(
            AppLanguage.FR to "À SURVEILLER",
            AppLanguage.AR to "مراقبة سريرية",
            AppLanguage.EN to "MONITOR"
        )
    )
}
