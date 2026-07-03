package com.example.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class EcgItem(
    val id: String,
    val title: Map<AppLanguage, String>,
    val desc: Map<AppLanguage, String>,
    val signs: Map<AppLanguage, String>,
    val nursingCare: Map<AppLanguage, String>
)

data class EmergencyDrug(
    val id: String,
    val name: String,
    val clazz: Map<AppLanguage, String>,
    val indications: Map<AppLanguage, String>,
    val posoAdult: Map<AppLanguage, String>,
    val posoPedia: Map<AppLanguage, String>,
    val sideEffects: Map<AppLanguage, String>,
    val contra: Map<AppLanguage, String>,
    val adminMode: Map<AppLanguage, String>
)

data class NursingProtocol(
    val id: String,
    val title: Map<AppLanguage, String>,
    val icon: String, // name of icon to display
    val steps: Map<AppLanguage, List<String>>
)

object MedicalData {

    val ecgList = listOf(
        EcgItem(
            id = "brady",
            title = mapOf(
                AppLanguage.FR to "Bradycardie sinusale",
                AppLanguage.AR to "تباطؤ ضربات القلب الجيبي",
                AppLanguage.EN to "Sinus Bradycardia"
            ),
            desc = mapOf(
                AppLanguage.FR to "Rythme régulier mais lent, d'origine sinusale, avec une fréquence cardiaque inférieure à 60 battements par minute (bpm).",
                AppLanguage.AR to "نبض منتظم ولكنه بطيء، ينشأ من العقدة الجيبية، حيث ينخفض معدل ضربات القلب عن 60 نبضة في الدقيقة.",
                AppLanguage.EN to "Regular but slow rhythm originating from the sinus node, with a heart rate below 60 beats per minute (bpm)."
            ),
            signs = mapOf(
                AppLanguage.FR to "FC < 60 bpm, Ondes P présentes avant chaque QRS, Intervalle PR normal (0.12 - 0.20s), Complexes QRS fins.",
                AppLanguage.AR to "معدل ضربات القلب < 60 نبضة/دقيقة، موجات P تسبق كل مركب QRS، فاصل PR طبيعي، مركبات QRS ضيقة.",
                AppLanguage.EN to "HR < 60 bpm, P waves present before every QRS, normal PR interval (0.12 - 0.20s), narrow QRS complexes."
            ),
            nursingCare = mapOf(
                AppLanguage.FR to "1. Évaluer la tolérance (tension artérielle, état de conscience, dyspnée).\n2. Installer le patient en position de sécurité.\n3. Préparer l'Atropine et l'accès veineux si le patient est instable.\n4. Aviser le médecin en urgence.",
                AppLanguage.AR to "1. تقييم العلامات الحيوية (ضغط الدم، درجة الوعي، ضيق التنفس).\n2. وضع المريض في وضعية مريحة وآمنة.\n3. تحضير الأتروبين وتأمين خط وريدي إذا كانت الحالة غير مستقرة.\n4. إبلاغ الطبيب فورًا.",
                AppLanguage.EN to "1. Assess patient tolerance (blood pressure, mental status, dyspnea).\n2. Position patient comfortably and safely.\n3. Prepare Atropine and secure IV access if patient is unstable.\n4. Notify doctor immediately."
            )
        ),
        EcgItem(
            id = "tachy",
            title = mapOf(
                AppLanguage.FR to "Tachycardie sinusale",
                AppLanguage.AR to "تسارع ضربات القلب الجيبي",
                AppLanguage.EN to "Sinus Tachycardia"
            ),
            desc = mapOf(
                AppLanguage.FR to "Accélération du rythme sinusal régulier avec une fréquence cardiaque supérieure à 100 bpm, souvent liée à la fièvre, l'effort, la douleur ou le stress.",
                AppLanguage.AR to "تسارع في النبض الجيبي المنتظم ليزيد عن 100 نبضة في الدقيقة، وغالبًا ما يكون بسبب الحمى، الألم، المجهود البدني، أو التوتر.",
                AppLanguage.EN to "Acceleration of the regular sinus rhythm with a heart rate above 100 bpm, often associated with fever, pain, exertion, or stress."
            ),
            signs = mapOf(
                AppLanguage.FR to "FC > 100 bpm (généralement 100-160), Rythme régulier, Ondes P présentes et normales, Complexes QRS fins.",
                AppLanguage.AR to "معدل ضربات القلب > 100 نبضة/دقيقة (عادة 100-160)، نبض منتظم، موجات P موجودة وطبيعية، مركبات QRS ضيقة.",
                AppLanguage.EN to "HR > 100 bpm (typically 100-160), regular rhythm, P waves present and normal, narrow QRS complexes."
            ),
            nursingCare = mapOf(
                AppLanguage.FR to "1. Identifier et traiter la cause sous-jacente (calmer la douleur, traiter la fièvre, hydratation).\n2. Surveiller les signes vitaux.\n3. Rassurer le patient.\n4. Réaliser un ECG à 12 dérivations.",
                AppLanguage.AR to "1. تحديد ومعالجة السبب الكامن (تسكين الألم، خفض الحرارة، الإماهة بالسوائل).\n2. مراقبة العلامات الحيوية.\n3. طمأنة وتهدئة المريض.\n4. إجراء تخطيط قلب كامل (12 اتجاهًا).",
                AppLanguage.EN to "1. Identify and treat the underlying cause (relieve pain, manage fever, hydration).\n2. Monitor vital signs.\n3. Reassure and calm the patient.\n4. Perform a complete 12-lead ECG."
            )
        ),
        EcgItem(
            id = "fa",
            title = mapOf(
                AppLanguage.FR to "Fibrillation auriculaire (FA)",
                AppLanguage.AR to "الرجفان الأذيني",
                AppLanguage.EN to "Atrial Fibrillation (AFib)"
            ),
            desc = mapOf(
                AppLanguage.FR to "La tachyarythmie supraventriculaire la plus fréquente, caractérisée par une activation auriculaire anarchique et désordonnée sans ondes P.",
                AppLanguage.AR to "اضطراب نظم القلب فوق البطيني الأكثر شيوعًا، يتميز بنشاط أذيني فوضوي وغير منظم مع غياب تام لموجات P المنتظمة.",
                AppLanguage.EN to "The most common supraventricular tachyarrhythmia, characterized by chaotic and disorganized atrial activation without distinct P waves."
            ),
            signs = mapOf(
                AppLanguage.FR to "Rythme complètement irrégulier (anarchie), Absence d'ondes P (remplacées par une ligne trémulante), Complexes QRS fins.",
                AppLanguage.AR to "نبض غير منتظم تمامًا، غياب موجات P (تستبدل بموجات تذبذبية صغيرة وفوضوية)، مركبات QRS ضيقة.",
                AppLanguage.EN to "Completely irregular rhythm (irregularly irregular), absence of P waves (replaced by baseline fibrillatory waves), narrow QRS."
            ),
            nursingCare = mapOf(
                AppLanguage.FR to "1. Évaluer la stabilité hémodynamique (tension, dyspnée, douleur thoracique).\n2. Préparer les traitements anticoagulants ou ralentisseurs (Bêtabloquants, Amiodarone) selon prescription.\n3. Enregistrer un ECG 12 pistes.",
                AppLanguage.AR to "1. تقييم الاستقرار الديناميكي الدموي (الضغط، ضيق التنفس، ألم الصدر).\n2. تحضير مضادات التخثر أو مخفضات ضربات القلب (حاصرات بيتا، أميودارون) حسب وصفة الطبيب.\n3. تسجيل تخطيط قلب 12 اتجاهًا.",
                AppLanguage.EN to "1. Assess hemodynamic stability (blood pressure, dyspnea, chest pain).\n2. Prepare anticoagulants or rate-limiting medications (Beta-blockers, Amiodarone) as prescribed.\n3. Record a 12-lead ECG."
            )
        ),
        EcgItem(
            id = "flutter",
            title = mapOf(
                AppLanguage.FR to "Flutter auriculaire",
                AppLanguage.AR to "الرفرفة الأذينية",
                AppLanguage.EN to "Atrial Flutter"
            ),
            desc = mapOf(
                AppLanguage.FR to "Trouble du rythme auriculaire caractérisé par une activité électrique rapide et régulière des oreillettes en aspect de 'dents de scie'.",
                AppLanguage.AR to "اضطراب نبض أذيني يتميز بنشاط كهربائي سريع ومنتظم للأذينين يظهر في التخطيط على شكل 'أسنان منشار'.",
                AppLanguage.EN to "Atrial rhythm disorder characterized by rapid and regular electrical atrial activity creating a 'sawtooth' baseline pattern."
            ),
            signs = mapOf(
                AppLanguage.FR to "Ondes de flutter régulières en 'dents de scie' (ondes F), Fréquence auriculaire d'environ 250-350 bpm, Conduction vers le QRS souvent de ratio 2:1 ou 3:1.",
                AppLanguage.AR to "موجات رفرفة منتظمة كـ 'أسنان المنشار' (موجات F)، معدل الأذينين حوالي 250-350 ن/د، ونسبة التوصيل للبطينين 2:1 أو 3:1.",
                AppLanguage.EN to "Regular 'sawtooth' flutter waves (F waves), atrial rate approx 250-350 bpm, conduction ratio to QRS is often 2:1 or 3:1."
            ),
            nursingCare = mapOf(
                AppLanguage.FR to "1. Évaluer le statut clinique du patient.\n2. Administrer le traitement prescrit (ralentisseurs de nœud AV).\n3. Préparer pour une éventuelle cardioversion électrique s'il y a instabilité.",
                AppLanguage.AR to "1. تقييم الحالة السريرية العامة للمريض.\n2. إعطاء الأدوية الموصوفة للتحكم في التوصيل الأذيني البطيني.\n3. التحضير لتقويم نظم القلب الكهربائي في حال عدم الاستقرار.",
                AppLanguage.EN to "1. Assess clinical status of the patient.\n2. Administer prescribed AV-nodal blocking agents.\n3. Prepare for electrical cardioversion if hemodynamically unstable."
            )
        ),
        EcgItem(
            id = "tv",
            title = mapOf(
                AppLanguage.FR to "Tachycardie ventriculaire (TV)",
                AppLanguage.AR to "التسارع البطيني",
                AppLanguage.EN to "Ventricular Tachycardia (VT)"
            ),
            desc = mapOf(
                AppLanguage.FR to "Urgence vitale. Succession rapide de complexes QRS larges d'origine ventriculaire, menant rapidement à un arrêt cardio-respiratoire.",
                AppLanguage.AR to "حالة طارئة مهددة للحياة. تتابع سريع لمركبات QRS عريضة تنشأ من البطين، وتؤدي سريعًا إلى توقف القلب والتنفس.",
                AppLanguage.EN to "Life-threatening emergency. A rapid succession of wide QRS complexes originating from the ventricles, leading quickly to cardiac arrest."
            ),
            signs = mapOf(
                AppLanguage.FR to "FC > 120 bpm (souvent 150-250), Complexes QRS très larges (> 0.12s) et réguliers, Absence d'ondes P visibles.",
                AppLanguage.AR to "معدل النبض > 120 ن/د (عادة 150-250)، مركبات QRS عريضة جدًا ونظامية، لا توجد موجات P مرئية.",
                AppLanguage.EN to "HR > 120 bpm (typically 150-250), very wide QRS complexes (> 0.12s) and regular, no visible P waves."
            ),
            nursingCare = mapOf(
                AppLanguage.FR to "1. VÉRIFIER LE POULS IMMÉDIATEMENT.\n2. Si pouls présent : Oxygène, accès veineux, alerter le médecin, préparer l'Amiodarone ou cardioversion.\n3. Si SANS pouls : Débuter immédiatement la RCP et appeler à l'aide (Choc électrique requis).",
                AppLanguage.AR to "1. فحص النبض فورًا (في غضون ثوانٍ).\n2. إذا وجد نبض: إعطاء أكسجين، تأمين وريد، استدعاء طبيب الطوارئ، تحضير أميودارون.\n3. إذا كان بدون نبض: بدء الإنعاش القلبي الرئوي (RCP) واستدعاء فريق الطوارئ فورًا واستخدام جهاز الصدمات.",
                AppLanguage.EN to "1. CHECK THE PULSE IMMEDIATELY.\n2. If pulse is present: Administer oxygen, IV access, notify doctor, prepare Amiodarone/cardioversion.\n3. If PULSELESS: Start CPR immediately, call for code team, and prepare defibrillator (shockable)."
            )
        ),
        EcgItem(
            id = "fv",
            title = mapOf(
                AppLanguage.FR to "Fibrillation ventriculaire (FV)",
                AppLanguage.AR to "الرجفان البطيني",
                AppLanguage.EN to "Ventricular Fibrillation (VF)"
            ),
            desc = mapOf(
                AppLanguage.FR to "Arrêt circulatoire immédiat. Activité électrique ventriculaire anarchique, sans aucune contraction efficace du muscle cardiaque.",
                AppLanguage.AR to "توقف دوران الدم فورًا. نشاط كهربائي فوضوي وعشوائي في البطين، دون أي انقباض فعال لعضلة القلب (موت سريري).",
                AppLanguage.EN to "Immediate circulatory arrest. Chaotic, disorganized ventricular electrical activity with no effective mechanical contraction."
            ),
            signs = mapOf(
                AppLanguage.FR to "Tracé totalement chaotique, absence de complexes QRS identifiables, oscillations de taille et de forme variables.",
                AppLanguage.AR to "مخطط فوضوي وعشوائي تمامًا، غياب كامل لمركبات QRS، اهتزازات وتذبذبات سريعة ومختلفة الشكل والحجم.",
                AppLanguage.EN to "Totally chaotic baseline, absence of identifiable QRS complexes, rapid and erratic waves of varying size and shape."
            ),
            nursingCare = mapOf(
                AppLanguage.FR to "1. URGENCE VITALE (Patient inconscient sans pouls/respiration).\n2. Lancer l'alerte Arrêt Cardiaque.\n3. Commencer immédiatement la RCP (MCE : 30 compressions / 2 insufflations).\n4. Défibrillation précoce maximale dès que disponible (Choc requis).",
                AppLanguage.AR to "1. حالة توقف قلب مؤكدة (المريض فاقد للوعي وبدون نبض أو تنفس).\n2. إعلان حالة توقف القلب واستدعاء المساعدة.\n3. البدء فورًا في تدليك الصدر والتنفس الاصطناعي (RCP: 30 ضغطة / 2 تنفس).\n4. استخدام جهاز الصدمات الكهربائية فورًا (صدمة كهربائية مستعجلة).",
                AppLanguage.EN to "1. EXTREME EMERGENCY (Unconscious, pulseless, apneic).\n2. Call cardiac arrest code immediately.\n3. Start high-quality CPR (30 compressions : 2 breaths).\n4. Apply defibrillator pads and shock immediately (highly shockable rhythm)."
            )
        ),
        EcgItem(
            id = "asystolie",
            title = mapOf(
                AppLanguage.FR to "Asystolie",
                AppLanguage.AR to "توقف نشاط القلب (الخط المسطح)",
                AppLanguage.EN to "Asystole"
            ),
            desc = mapOf(
                AppLanguage.FR to "Arrêt cardiaque caractérisé par l'absence totale d'activité électrique et mécanique ventriculaire (ligne plate).",
                AppLanguage.AR to "توقف تام للقلب، يتميز بغياب كامل للنشاط الكهربائي والميكانيكي للبطينين (يظهر كخط مستقيم تقريبًا).",
                AppLanguage.EN to "Cardiac arrest characterized by the total absence of ventricular electrical and mechanical activity (flatline)."
            ),
            signs = mapOf(
                AppLanguage.FR to "Ligne presque plate, absence totale d'ondes ou de complexes QRS. (Toujours vérifier le branchement des électrodes !)",
                AppLanguage.AR to "خط مسطح بالكامل، غياب أي نبضات أو موجات كهربائية. (تأكد دائمًا من توصيل أقطاب الجهاز بالجلد!).",
                AppLanguage.EN to "Flatline or near-flatline, complete absence of QRS complexes. (Always verify lead connections!)."
            ),
            nursingCare = mapOf(
                AppLanguage.FR to "1. Alerter l'équipe de réanimation.\n2. Débuter la RCP immédiate.\n3. Administrer l'Adrénaline 1mg IV directe toutes les 3-5 min selon protocole.\n4. Rythme NON CHOQUABLE. Poursuivre le massage sans relâche.",
                AppLanguage.AR to "1. استدعاء فريق الطوارئ والإنعاش.\n2. البدء الفوري في الإنعاش القلبي الرئوي (MCE).\n3. إعطاء الأدرينالين 1 ملغ وريدي مباشر كل 3-5 دقائق حسب البروتوكول.\n4. هذا الإيقاع غير قابل للصدمة الكهربائية (Non-Shockable)، استمر بالتدليك والإنعاش.",
                AppLanguage.EN to "1. Alert code blue team.\n2. Start immediate CPR.\n3. Administer Epinephrine (Adrénaline) 1mg IV push every 3-5 minutes as prescribed.\n4. Rhythm is NON-SHOCKABLE. Continue CPR continuously."
            )
        ),
        EcgItem(
            id = "bav",
            title = mapOf(
                AppLanguage.FR to "Bloc Auriculo-Ventriculaire (BAV)",
                AppLanguage.AR to "حصار أذيني بطيني",
                AppLanguage.EN to "Atrioventricular Block (AV Block)"
            ),
            desc = mapOf(
                AppLanguage.FR to "Retard ou interruption de la conduction de l'influx électrique entre les oreillettes et les ventricules.",
                AppLanguage.AR to "تأخر أو انقطاع في توصيل النبضة الكهربائية من الأذينين إلى البطينين بسبب خلل في العقدة الأذينية البطينية.",
                AppLanguage.EN to "Delay or complete interruption of electrical conduction between the atria and the ventricles."
            ),
            signs = mapOf(
                AppLanguage.FR to "BAV1 : PR allongé (>0.20s). BAV2 : Ondes P bloquées périodiquement. BAV3 : Dissociation complète entre P et QRS, rythme d'échappement lent.",
                AppLanguage.AR to "درجة 1: تطاول فاصل PR. درجة 2: سقوط موجة QRS دوريًا. درجة 3: انفصال تام بين موجات P ومركبات QRS مع نبض بطيني بطيء جدًا.",
                AppLanguage.EN to "1st Deg: Prolonged PR (>0.20s). 2nd Deg: Periodically dropped QRS. 3rd Deg: Complete dissociation between P and QRS with slow escape rate."
            ),
            nursingCare = mapOf(
                AppLanguage.FR to "1. Évaluer la tolérance clinique du patient (vertiges, hypotension).\n2. Préparer l'Atropine pour les BAV aigus symptomatiques.\n3. Garder le matériel de stimulation cardiaque externe (stimulateur temporaire) à proximité.",
                AppLanguage.AR to "1. تقييم تحمل المريض للنبض البطيء (دوخة، هبوط ضغط، غياب وعي).\n2. تحضير الأتروبين لحالات الحصار الحاد المصحوب بأعراض.\n3. تجهيز جهاز تنظيم ضربات القلب الخارجي المؤقت (Pacing) ليكون قريبًا.",
                AppLanguage.EN to "1. Assess clinical tolerance (dizziness, syncope, hypotension).\n2. Prepare Atropine for acute symptomatic blocks.\n3. Keep transcutaneous pacemaker nearby for severe cases (3rd degree)."
            )
        )
    )

    val drugsList = listOf(
        EmergencyDrug(
            id = "adrenaline",
            name = "Adrénaline (Épinéphrine)",
            clazz = mapOf(
                AppLanguage.FR to "Sympathomimétique, catécholamine vasoconstrice",
                AppLanguage.AR to "محاكي الودي، رافع قوي لضغط الدم ومنشط للقلب",
                AppLanguage.EN to "Sympathomimetic, catecholamine, vasopressor"
            ),
            indications = mapOf(
                AppLanguage.FR to "Arrêt cardio-respiratoire (ACR), Choc anaphylactique sévère, Asthme aigu grave (en dernier recours).",
                AppLanguage.AR to "توقف القلب والتنفس، الصدمة التحسسية الشديدة (Anaphylaxie)، النوبات الربوية الحادة الشديدة.",
                AppLanguage.EN to "Cardiac arrest, severe anaphylactic shock, severe acute asthma (refractory)."
            ),
            posoAdult = mapOf(
                AppLanguage.FR to "ACR : 1 mg IV direct toutes les 3 à 5 minutes.\nChoc anaphylactique : 0.3 mg à 0.5 mg IM.",
                AppLanguage.AR to "توقف القلب: 1 ملغ وريدي مباشر كل 3 إلى 5 دقائق.\nالصدمة التحسسية: 0.3 ملغ إلى 0.5 ملغ حقن عضلي (IM).",
                AppLanguage.EN to "Cardiac Arrest: 1 mg IV push every 3-5 minutes.\nAnaphylaxis: 0.3 to 0.5 mg IM."
            ),
            posoPedia = mapOf(
                AppLanguage.FR to "ACR : 10 mcg/kg (0.01 mg/kg) IV direct.\nChoc anaphylactique : 0.01 mg/kg IM.",
                AppLanguage.AR to "توقف القلب: 10 ميكروغرام/كغ (0.01 ملغ/كغ) وريدي مباشر.\nالصدمة التحسسية: 0.01 ملغ/كغ حقن عضلي.",
                AppLanguage.EN to "Cardiac Arrest: 10 mcg/kg (0.01 mg/kg) IV push.\nAnaphylaxis: 0.01 mg/kg IM."
            ),
            sideEffects = mapOf(
                AppLanguage.FR to "Tachycardie, arythmies ventriculaires, hypertension artérielle sévère, tremblements, anxiété.",
                AppLanguage.AR to "تسارع نبض القلب، اضطرابات النظم البطينية، ارتفاع ضغط الدم الشديد، الرعشة، القلق.",
                AppLanguage.EN to "Tachycardia, ventricular arrhythmias, severe hypertension, tremors, anxiety."
            ),
            contra = mapOf(
                AppLanguage.FR to "Aucune contre-indication en situation d'urgence vitale (Arrêt cardiaque).",
                AppLanguage.AR to "لا توجد أي موانع استعمال في حالات الطوارئ القصوى وتوقف القلب.",
                AppLanguage.EN to "No contraindications in life-threatening emergency situations (Cardiac arrest)."
            ),
            adminMode = mapOf(
                AppLanguage.FR to "Intraveineuse directe (IVD) rapide, Intramusculaire (IM - cuisse), ou perfusion continue à la seringue électrique.",
                AppLanguage.AR to "حقن وريدي مباشر سريع (IVD)، حقن عضلي (IM في الفخذ)، أو تسريب مستمر عبر مضخة المحاقن الكهربائية.",
                AppLanguage.EN to "Rapid IV push (IVD), Intramuscular (IM - anterolateral thigh), or continuous IV infusion."
            )
        ),
        EmergencyDrug(
            id = "atropine",
            name = "Atropine",
            clazz = mapOf(
                AppLanguage.FR to "Anticholinergique, antispasmodique, vagolytique",
                AppLanguage.AR to "مضاد الكولين، مثبط للعصب الحائر، يرفع النبض",
                AppLanguage.EN to "Anticholinergic, parasympatholytic"
            ),
            indications = mapOf(
                AppLanguage.FR to "Bradycardie sinusale symptomatique, bloc auriculo-ventriculaire, antidote des intoxications aux organophosphorés.",
                AppLanguage.AR to "تباطؤ ضربات القلب المصحوب بأعراض، الحصار الأذيني البطيني، ترياق في التسمم بالمبيدات الحشرية.",
                AppLanguage.EN to "Symptomatic sinus bradycardia, AV block, antidote for organophosphate poisoning."
            ),
            posoAdult = mapOf(
                AppLanguage.FR to "Bradycardie : 0.5 mg à 1 mg IVD toutes les 3-5 min (Max 3 mg). Intoxication : 2 mg IV répétable.",
                AppLanguage.AR to "تباطؤ النبض: 0.5 ملغ إلى 1 ملغ وريدي مباشر كل 3-5 دقائق (الحد الأقصى 3 ملغ).\nالتسمم: 2 ملغ وريدي مكرر.",
                AppLanguage.EN to "Bradycardia: 0.5 to 1 mg IV push every 3-5 min (Max 3 mg). Poisoning: 2 mg IV repeated."
            ),
            posoPedia = mapOf(
                AppLanguage.FR to "0.02 mg/kg IVD (dose minimale 0.1 mg, max 0.5 mg par dose).",
                AppLanguage.AR to "0.02 ملغ/كغ حقن وريدي مباشر (أقل جرعة 0.1 ملغ، والحد الأقصى 0.5 ملغ للحقنة).",
                AppLanguage.EN to "0.02 mg/kg IV push (minimum dose 0.1 mg, max 0.5 mg per dose)."
            ),
            sideEffects = mapOf(
                AppLanguage.FR to "Sécheresse buccale, mydriase, constipation, rétention urinaire, confusion, tachycardie.",
                AppLanguage.AR to "جفاف الفم، اتساع حدقة العين، الإمساك، احتباس البول، التخليط الذهني، تسارع النبض.",
                AppLanguage.EN to "Dry mouth, mydriasis, constipation, urinary retention, confusion, tachycardia."
            ),
            contra = mapOf(
                AppLanguage.FR to "Glaucome à angle fermé, rétention d'urine liée à un adénome de la prostate (sauf urgence vitale).",
                AppLanguage.AR to "مياه زرقاء في العين (غلوكوما)، احتباس البول الناتج عن تضخم البروستات (إلا في حالة الخطر الحياتي).",
                AppLanguage.EN to "Angle-closure glaucoma, urinary retention due to prostatic hypertrophy (except in absolute emergency)."
            ),
            adminMode = mapOf(
                AppLanguage.FR to "Intraveineuse directe (IVD). S'assurer de rincer après l'injection.",
                AppLanguage.AR to "حقن وريدي مباشر (IVD). يجب غسل القسطرة بمحلول ملحي بعد الحقن.",
                AppLanguage.EN to "IV push (IVD). Ensure saline flush after injection."
            )
        ),
        EmergencyDrug(
            id = "amiodarone",
            name = "Amiodarone (Cordarone)",
            clazz = mapOf(
                AppLanguage.FR to "Antiarythmique de classe III",
                AppLanguage.AR to "مضاد لاضطرابات نظم القلب (الفئة الثالثة)",
                AppLanguage.EN to "Class III antiarrhythmic"
            ),
            indications = mapOf(
                AppLanguage.FR to "Fibrillation ventriculaire et Tachycardie ventriculaire sans pouls (après 3e choc), tachycardies supraventriculaires.",
                AppLanguage.AR to "الرجفان والتسارع البطيني بدون نبض (بعد الصدمة الثالثة)، تسارع ضربات القلب فوق البطيني.",
                AppLanguage.EN to "Pulseless VT/VF (after 3rd shock), supraventricular tachycardia."
            ),
            posoAdult = mapOf(
                AppLanguage.FR to "ACR : 300 mg IVD dilués dans 20 ml G5% (première dose), puis 150 mg si besoin.\nPerfusion stable : 150 mg à 300 mg sur 30 min.",
                AppLanguage.AR to "توقف القلب: 300 ملغ وريدي مباشر مخففة في محلول سكري 5% (جرعة أولى)، ثم 150 ملغ إذا لزم الأمر.\nالجرعة للمريض المستقر: 150 ملغ إلى 300 ملغ تسريب في 30 دقيقة.",
                AppLanguage.EN to "Cardiac arrest: 300 mg IV push diluted in 20ml D5% (1st dose), then 150 mg if needed.\nStable arrhythmias: 150-300 mg IV infusion over 30 min."
            ),
            posoPedia = mapOf(
                AppLanguage.FR to "ACR : 5 mg/kg IVD dilués dans du G5% uniquement.",
                AppLanguage.AR to "توقف القلب للأطفال: 5 ملغ/كغ وريدي مباشر مخففة في محلول سكري 5% حصريًا.",
                AppLanguage.EN to "Cardiac arrest: 5 mg/kg IV push diluted in D5% only."
            ),
            sideEffects = mapOf(
                AppLanguage.FR to "Hypotension artérielle, bradycardie, phlébite locale (si veine périphérique sans dilution), toxicité thyroïdienne.",
                AppLanguage.AR to "هبوط ضغط الدم، تباطؤ النبض، التهاب الوريد الموضعي (عند إعطائه بتركيز عالٍ)، اضطرابات الغدة الدرقية.",
                AppLanguage.EN to "Hypotension, bradycardia, local phlebitis (peripheral vein), thyroid toxicity."
            ),
            contra = mapOf(
                AppLanguage.FR to "Bradycardie sinusale sévère, bloc de conduction sans stimulateur, allergie à l'iode.",
                AppLanguage.AR to "تباطؤ القلب الجيبي الشديد، حصار القلب بدون منظم، الحساسية الشديدة لليود.",
                AppLanguage.EN to "Severe sinus bradycardia, heart block without a pacemaker, iodine allergy."
            ),
            adminMode = mapOf(
                AppLanguage.FR to "DILUTION EXCLUSIVE dans du glucose à 5% (G5%). Ne pas mélanger au sérum salé. Préférer une voie centrale.",
                AppLanguage.AR to "التخفيف الحصري في محلول غلوكوز 5% (G5%). لا تخلط مع المحلول الملحي (Salé). يفضل عبر قسطرة وريدية مركزية.",
                AppLanguage.EN to "DILUTE ONLY in Glucose 5% (D5W). Do not mix with normal saline. Central line preferred."
            )
        ),
        EmergencyDrug(
            id = "diazepam",
            name = "Diazépam (Valium)",
            clazz = mapOf(
                AppLanguage.FR to "Anxiolytique, anticonvulsant, benzodiazépine",
                AppLanguage.AR to "مضاد قلق، مضاد تشنج وصرع (بنزوديازيبين)",
                AppLanguage.EN to "Benzodiazepine, anticonvulsant, anxiolytic"
            ),
            indications = mapOf(
                AppLanguage.FR to "Crise convulsive convulsive convulsive, état de mal épileptique, agitation sévère, prémédication.",
                AppLanguage.AR to "نوبات التشنج والصرع، الحالة الصرعية المستمرة، الهياج الشديد، التهدئة قبل الجراحة.",
                AppLanguage.EN to "Acute seizures, status epilepticus, severe agitation, procedural sedation."
            ),
            posoAdult = mapOf(
                AppLanguage.FR to "Crise : 5 mg à 10 mg IV lente ou intrarectal.",
                AppLanguage.AR to "النوبة الحادة: 5 ملغ إلى 10 ملغ حقن وريدي بطيء أو عبر الشرج (IR).",
                AppLanguage.EN to "Seizures: 5 to 10 mg slow IV or intrarectal."
            ),
            posoPedia = mapOf(
                AppLanguage.FR to "0.2 mg à 0.5 mg/kg intrarectal (Valium rectal) ou 0.1 mg à 0.2 mg/kg IV lent.",
                AppLanguage.AR to "0.2 ملغ إلى 0.5 ملغ/كغ عبر الشرج أو 0.1 ملغ إلى 0.2 ملغ/كغ حقن وريدي بطيء جداً.",
                AppLanguage.EN to "0.2 to 0.5 mg/kg intrarectal or 0.1 to 0.2 mg/kg slow IV."
            ),
            sideEffects = mapOf(
                AppLanguage.FR to "Dépression respiratoire, somnolence, hypotonie musculaire, hypotension.",
                AppLanguage.AR to "ثبيط وهبوط التنفس، الخمول والنعاس، ارتخاء العضلات، هبوط ضغط الدم.",
                AppLanguage.EN to "Respiratory depression, somnolence, muscle weakness, hypotension."
            ),
            contra = mapOf(
                AppLanguage.FR to "Insuffisance respiratoire sévère, myasthénie, apnée du sommeil.",
                AppLanguage.AR to "الفشل التنفسي الشديد، الوهن العضلي الوبيل، انقطاع النفس الانسدادي أثناء النوم.",
                AppLanguage.EN to "Severe respiratory insufficiency, myasthenia gravis, sleep apnea."
            ),
            adminMode = mapOf(
                AppLanguage.FR to "Injection intraveineuse lente (pas plus de 5 mg/min chez l'adulte) ou voie intrarectale chez l'enfant.",
                AppLanguage.AR to "حقن وريدي بطيء (لا يتجاوز 5 ملغ/دقيقة للبالغ) أو حقن شرجي للأطفال.",
                AppLanguage.EN to "Slow IV injection (max 5 mg/min in adults) or intrarectal route for children."
            )
        ),
        EmergencyDrug(
            id = "morphine",
            name = "Morphine",
            clazz = mapOf(
                AppLanguage.FR to "Antalgique majeur, opiacé",
                AppLanguage.AR to "مسكن قوي جداً للألم، من مشتقات الأفيون",
                AppLanguage.EN to "Opioid analgesic"
            ),
            indications = mapOf(
                AppLanguage.FR to "Douleur aiguë sévère résistante (traumatismes, infarctus du myocarde, douleur post-opératoire), œdème aigu du poumon (OAP).",
                AppLanguage.AR to "الآلام الحادة الشديدة المستعصية (الحوادث والكسور، احتشاء عضلة القلب، آلام بعد الجراحة)، وذمة الرئة الحادة (OAP).",
                AppLanguage.EN to "Severe acute pain (trauma, myocardial infarction, post-op pain), acute pulmonary edema."
            ),
            posoAdult = mapOf(
                AppLanguage.FR to "Titration IV : 1 mg à 3 mg toutes les 5 à 10 min jusqu'à soulagement (dose totale moyenne 5-10 mg).",
                AppLanguage.AR to "معايرة وريدية: 1 ملغ إلى 3 ملغ كل 5 إلى 10 دقائق حتى يسكن الألم (الجرعة الإجمالية المعتادة 5-10 ملغ).",
                AppLanguage.EN to "IV titration: 1 to 3 mg every 5-10 min until pain relief (average total dose 5-10 mg)."
            ),
            posoPedia = mapOf(
                AppLanguage.FR to "0.05 mg à 0.1 mg/kg IV lent dilué.",
                AppLanguage.AR to "0.05 ملغ إلى 0.1 ملغ/كغ حقن وريدي بطيء مخفف جداً.",
                AppLanguage.EN to "0.05 to 0.1 mg/kg slow IV, diluted."
            ),
            sideEffects = mapOf(
                AppLanguage.FR to "Dépression respiratoire, nausées, vomissements, prurit, somnolence, bradycardie, hypotension.",
                AppLanguage.AR to "تثبيط التنفس، الغثيان والقيء، الحكة، الخمول، بطء النبض، هبوط الضغط.",
                AppLanguage.EN to "Respiratory depression, nausea, vomiting, pruritus, drowsiness, bradycardia."
            ),
            contra = mapOf(
                AppLanguage.FR to "Insuffisance respiratoire non assistée, traumatisme crânien grave non ventilé, insuffisance hépatique sévère.",
                AppLanguage.AR to "الفشل التنفسي غير الاصطناعي، إصابات الرأس الشديدة دون تهوية، القصور الكبدي الحاد.",
                AppLanguage.EN to "Unmanaged respiratory depression, severe head injury, acute liver failure."
            ),
            adminMode = mapOf(
                AppLanguage.FR to "Intraveineuse lente diluée (titration), sous-cutanée (SC) ou intramusculaire (IM). Préparer l'antidote (Naloxone).",
                AppLanguage.AR to "حقن وريدي بطيء مخفف (معايرة)، حقن تحت الجلد (SC) أو حقن عضلي (IM). جهز دائمًا الترياق (نالكسون).",
                AppLanguage.EN to "Slow diluted IV (titration), subcutaneous (SC) or intramuscular (IM). Keep Naloxone antidote ready."
            )
        ),
        EmergencyDrug(
            id = "salbutamol",
            name = "Salbutamol (Ventoline)",
            clazz = mapOf(
                AppLanguage.FR to "Bronchodilatateur, bêta-2 mimétique d'action rapide",
                AppLanguage.AR to "موسع للشعب الهوائية، محفز مستقبلات بيتا 2 سريع المفعول",
                AppLanguage.EN to "Bronchodilator, fast-acting beta-2 agonist"
            ),
            indications = mapOf(
                AppLanguage.FR to "Crise d'asthme aiguë, bronchospasme, BPCO exacerbée, hyperkaliémie menaçante (aide à faire entrer le potassium dans la cellule).",
                AppLanguage.AR to "نوبات الربو الحادة، ضيق تشنج الشعب الهوائية، نوبات انسداد الرئة المزمن، علاج طارئ لارتفاع بوتاسيوم الدم.",
                AppLanguage.EN to "Acute asthma attack, bronchospasm, COPD exacerbation, hyperkalemia (shifts potassium intracellularly)."
            ),
            posoAdult = mapOf(
                AppLanguage.FR to "Nébulisation : 5 mg dilués dans du sérum salé à inhaler sur 10-15 min. Spray : 2 à 4 bouffées.",
                AppLanguage.AR to "عبر الإرذاذ (Nébulisation): 5 ملغ مخففة في محلول ملحي وتستنشق خلال 10-15 دقيقة.\nالبخاخ: 2 إلى 4 بخات.",
                AppLanguage.EN to "Nebulization: 5 mg diluted in normal saline over 10-15 min. MDI: 2 to 4 puffs."
            ),
            posoPedia = mapOf(
                AppLanguage.FR to "Nébulisation : 1.25 mg à 2.5 mg dilués.",
                AppLanguage.AR to "عبر الإرذاذ للأطفال: 1.25 ملغ إلى 2.5 ملغ مخففة في محلول ملحي.",
                AppLanguage.EN to "Nebulization: 1.25 to 2.5 mg diluted in saline."
            ),
            sideEffects = mapOf(
                AppLanguage.FR to "Tachycardie, palpitations, tremblements fins des extrémités, hypokaliémie (si fortes doses).",
                AppLanguage.AR to "تسارع النبض، خفقان، رعشة خفيفة في الأطراف، انخفاض البوتاسيوم في الدم.",
                AppLanguage.EN to "Tachycardia, palpitations, fine muscle tremors, hypokalemia (high doses)."
            ),
            contra = mapOf(
                AppLanguage.FR to "Hypersensibilité connue au produit.",
                AppLanguage.AR to "الحساسية المفرطة المعروفة للمستحضر.",
                AppLanguage.EN to "Known hypersensitivity to the drug."
            ),
            adminMode = mapOf(
                AppLanguage.FR to "Inhalation par aérosol ou spray (chambre d'inhalation). Intraveineuse continue très rare en réanimation.",
                AppLanguage.AR to "الاستنشاق عبر جهاز الإرذاذ أو البخاخ الهوائي. أو الحقن الوريدي النادر في العناية المركزة.",
                AppLanguage.EN to "Inhalation via nebulizer or metered-dose inhaler (MDI). Continuous IV infusion is restricted to ICU."
            )
        ),
        EmergencyDrug(
            id = "gentamicine",
            name = "Gentamicine",
            clazz = mapOf(
                AppLanguage.FR to "Antibiotique de la classe des aminosides (aminoglycosides)",
                AppLanguage.AR to "مضاد حيوي من عائلة الأمينوغليكوزيدات (قاتل للبكتيريا)",
                AppLanguage.EN to "Aminoglycoside antibiotic"
            ),
            indications = mapOf(
                AppLanguage.FR to "Infections bactériennes graves (septicémies, endocardites, infections urinaires sévères, pyélonéphrite) souvent associé.",
                AppLanguage.AR to "الالتهابات البكتيرية الشديدة (تسمم الدم، التهاب شغاف القلب، التهابات المسالك البولية الحادة، التهاب الكلى) وغالبًا ما يشارك.",
                AppLanguage.EN to "Severe bacterial infections (sepsis, endocarditis, severe urinary tract infections, pyelonephritis), combined."
            ),
            posoAdult = mapOf(
                AppLanguage.FR to "3 à 8 mg/kg en dose unique journalière par perfusion intraveineuse lente.",
                AppLanguage.AR to "3 إلى 8 ملغ/كغ كجرعة يومية واحدة، عبر التسريب الوريدي البطيء.",
                AppLanguage.EN to "3 to 8 mg/kg as a single daily dose via slow IV infusion."
            ),
            posoPedia = mapOf(
                AppLanguage.FR to "3 à 7.5 mg/kg par jour, souvent fractionné ou en dose unique selon protocole pédiatrique.",
                AppLanguage.AR to "3 إلى 7.5 ملغ/كغ يوميًا، مقسمة أو كجرعة واحدة حسب بروتوكول الأطفال المعمول به.",
                AppLanguage.EN to "3 to 7.5 mg/kg per day, in divided doses or single dose depending on pediatric protocol."
            ),
            sideEffects = mapOf(
                AppLanguage.FR to "Néphrotoxicité (toxicité pour les reins), Ototoxicité (toxicité pour l'oreille interne, réversible ou non).",
                AppLanguage.AR to "سمية كلوية (تأثير ضار على الكلى)، سمية أذنية (ضرر للسمع والاتزان قد يكون غير راجع).",
                AppLanguage.EN to "Nephrotoxicity (kidney damage), ototoxicity (vestibular and auditory damage, can be permanent)."
            ),
            contra = mapOf(
                AppLanguage.FR to "Allergie aux aminosides, myasthénie.",
                AppLanguage.AR to "الحساسية المعروفة للأمينوغليكوزيدات، الوهن العضلي الوبيل.",
                AppLanguage.EN to "Hypersensitivity to aminoglycosides, myasthenia gravis."
            ),
            adminMode = mapOf(
                AppLanguage.FR to "Perfusion intraveineuse (IV) lente diluée dans du sérum salé ou glucosé sur 30 à 60 minutes. Jamais en injection IV directe.",
                AppLanguage.AR to "تسريب وريدي بطيء مخفف في محلول ملحي أو سكري على مدى 30 إلى 60 دقيقة. لا يعطى أبدًا حقنًا وريديًا مباشرًا.",
                AppLanguage.EN to "Slow IV infusion diluted in normal saline or D5W over 30-60 minutes. Never administered via rapid IV push."
            )
        ),
        EmergencyDrug(
            id = "ceftriaxone",
            name = "Ceftriaxone (Rocéphine)",
            clazz = mapOf(
                AppLanguage.FR to "Antibiotique, Céphalosporine de 3ème génération (C3G)",
                AppLanguage.AR to "مضاد حيوي من عائلة السيفالوسبورينات (الجيل الثالث)",
                AppLanguage.EN to "Third-generation cephalosporin antibiotic"
            ),
            indications = mapOf(
                AppLanguage.FR to "Méningites bactériennes, pneumonies graves, septicémies, infections urinaires compliquées, maladie de Lyme.",
                AppLanguage.AR to "التهاب السحايا البكتيري، الالتهاب الرئوي الحاد، تسمم الدم، التهابات مجاري البول المعقدة، داء لايم.",
                AppLanguage.EN to "Bacterial meningitis, severe pneumonia, sepsis, complicated UTIs, Lyme disease."
            ),
            posoAdult = mapOf(
                AppLanguage.FR to "1 g à 2 g par jour en une seule prise IV ou IM. (Jusqu'à 4 g dans les méningites).",
                AppLanguage.AR to "1 غرام إلى 2 غرام يوميًا كجرعة واحدة وريديًا أو عضليًا. (تصل إلى 4 غرام في التهاب السحايا).",
                AppLanguage.EN to "1 to 2 g daily as a single dose IV or IM. (Up to 4 g for meningitis)."
            ),
            posoPedia = mapOf(
                AppLanguage.FR to "50 mg à 100 mg/kg par jour en une prise unique (max 4 g).",
                AppLanguage.AR to "50 ملغ إلى 100 ملغ/كغ يوميًا كجرعة واحدة (الحد الأقصى 4 غرام).",
                AppLanguage.EN to "50 to 100 mg/kg daily in a single dose (max 4 g)."
            ),
            sideEffects = mapOf(
                AppLanguage.FR to "Troubles digestifs (diarrhées, nausées), réactions allergiques cutanées, anomalies sanguines temporaires.",
                AppLanguage.AR to "اضطرابات هضمية (إسهال، غثيان)، تفاعلات حساسية جلدية، تغيرات مؤقتة في مكونات الدم.",
                AppLanguage.EN to "Gastrointestinal disturbances (diarrhea, nausea), allergic skin reactions, transient blood count anomalies."
            ),
            contra = mapOf(
                AppLanguage.FR to "Allergie connue aux céphalosporines ou pénicillines (allergie croisée), nouveau-né avec hyperbilirubinémie.",
                AppLanguage.AR to "حساسية مفرطة للسيفالوسبورينات أو البنسلين، حديثي الولادة المصابين بارتفاع بيليروبين الدم.",
                AppLanguage.EN to "Known hypersensitivity to cephalosporins or penicillins (cross-allergy), neonates with hyperbilirubinemia."
            ),
            adminMode = mapOf(
                AppLanguage.FR to "Injection intraveineuse lente (sur 2 à 4 min), perfusion IV ou injection intramusculaire profonde (souvent diluée avec de la lidocaïne).",
                AppLanguage.AR to "حقن وريدي بطيء (خلال 2 إلى 4 دقائق)، تسريب وريدي، أو حقن عضلي عميق (يخفف غالبًا مع الليدوكائين لتخفيف الألم).",
                AppLanguage.EN to "Slow IV injection (over 2-4 min), IV infusion, or deep IM injection (often diluted with lidocaine to reduce pain)."
            )
        )
    )

    val protocolsList = listOf(
        NursingProtocol(
            id = "perfusion",
            title = mapOf(
                AppLanguage.FR to "Pose de perfusion IV",
                AppLanguage.AR to "تركيب الكانيولا والسيروم الوريدي",
                AppLanguage.EN to "IV Cannulation & Perfusion"
            ),
            icon = "healing",
            steps = mapOf(
                AppLanguage.FR to listOf(
                    "S'assurer de la prescription et expliquer le geste au patient.",
                    "Réaliser une hygiène des mains rigoureuse (friction hydro-alcoolique).",
                    "Préparer le matériel (cathéter de calibre approprié, tubulure purgée, garrot, compresses antiseptiques, pansement adhésif transparent).",
                    "Poser le garrot à 10-15 cm au-dessus du site choisi et repérer la veine par palpation.",
                    "Désinfecter largement la zone cutanée selon le protocole antiseptique (en partant du centre vers la périphérie).",
                    "Tendre la peau, introduire le cathéter biseau vers le haut avec un angle de 15 à 30°.",
                    "Observer le retour veineux dans la chambre de reflux.",
                    "Retirer doucement le mandrin tout en glissant le cathéter souple dans la veine.",
                    "Retirer le garrot, brancher la tubulure purgée sans contaminer les embouts, puis jeter l'aiguille dans le collecteur d'aiguilles souillées.",
                    "Fixer le cathéter solidement avec le pansement transparent et régler le débit de perfusion prescrit."
                ),
                AppLanguage.AR to listOf(
                    "التحقق من الوصفة الطبية وشرح الإجراء للمريض لطمأنته وكسب تعاونه.",
                    "تطهير وتعقيم اليدين جيداً بالجيل الكحولي.",
                    "تحضير الأدوات (كانيولا بالقياس المناسب، محلول جاهز ومفرغ من الهواء، تورنيكه، مسحات كحولية، لاصق شفاف تثبيتي).",
                    "ربط التورنيكه (Garrot) على بعد 10-15 سم فوق موقع الحقن وتحديد الوريد بالجس بالأصابع.",
                    "تعقيم موقع الحقن بحركة دائرية من الداخل إلى الخارج والسماح له بأن يجف تلقائياً.",
                    "شد الجلد أسفل الوريد، وإدخال الكانيولا بزاوية 15 إلى 30 درجة وفتحة الإبرة للأعلى.",
                    "ملاحظة تدفق الدم في الغرفة الخلفية للكانيولا للتأكد من دخول الوريد.",
                    "سحب الإبرة المعدنية تدريجيًا إلى الخلف مع دفع الأنبوب البلاستيكي المرن داخل الوريد بلطف.",
                    "فك التورنيكه فوراً، والضغط فوق الوريد لمنع تدفق الدم، وتوصيل جهاز المحلول المعقم.",
                    "التخلص من الإبرة في صندوق النفايات الحادة (Sharp Box)، وتثبيت الكانيولا باللاصق الشفاف وضبط سرعة المحلول."
                ),
                AppLanguage.EN to listOf(
                    "Verify the medical order and explain the procedure to the patient.",
                    "Perform thorough hand hygiene (alcohol hand rub).",
                    "Prepare equipment (appropriate size cannula, primed tubing, tourniquet, antiseptic wipes, transparent dressing).",
                    "Apply the tourniquet 10-15 cm above the selected site and locate the vein by palpation.",
                    "Cleanse the insertion site extensively with antiseptic using a circular motion (inside to outside).",
                    "Taut the skin, insert the cannula bevel up at a 15 to 30-degree angle.",
                    "Observe blood flashback in the flash chamber.",
                    "Gently withdraw the needle stylus while sliding the flexible catheter completely into the vein.",
                    "Release the tourniquet, connect the primed IV tubing aseptically, and discard the needle in the sharps container.",
                    "Secure the cannula firmly with a transparent dressing and set the prescribed flow rate."
                )
            )
        ),
        NursingProtocol(
            id = "injection_im",
            title = mapOf(
                AppLanguage.FR to "Injection Intramusculaire (IM)",
                AppLanguage.AR to "الحقن العضلي (IM)",
                AppLanguage.EN to "Intramuscular Injection (IM)"
            ),
            icon = "colorize",
            steps = mapOf(
                AppLanguage.FR to listOf(
                    "Vérifier les '5 bons' (patient, médicament, dose, voie, moment) et préparer le produit de manière aseptique.",
                    "Choisir le site d'injection (fesse quadrant supéro-externe, ou muscle deltoïde de l'épaule).",
                    "Nettoyer la peau avec un antiseptique.",
                    "Tendre la peau fermement entre le pouce et l'index (ou pincer chez les personnes très minces).",
                    "Introduire l'aiguille d'un geste rapide et franc, perpendiculairement à la peau (angle de 90°).",
                    "Aspirer légèrement pour vérifier l'absence de retour sanguin (si sang présent : retirer l'aiguille et recommencer).",
                    "Injecter le produit lentement et régulièrement.",
                    "Retirer l'aiguille rapidement et appliquer une compresse sèche sur le point de ponction sans frictionner.",
                    "Jeter l'aiguille immédiatement dans le container à aiguilles et réévaluer la douleur du patient."
                ),
                AppLanguage.AR to listOf(
                    "التحقق من القواعد الخمس الصحيحة وتجهيز الدواء في سرنجة معقمة.",
                    "تحديد مكان الحقن بدقة (الربع العلوي الخارجي لعضلة الأرداف، أو عضلة الدالية في الكتف).",
                    "تعقيم الجلد بمسحة كحولية وتركها لتجف.",
                    "شد الجلد حول موضع الحقن باستخدام أصابع اليد الأخرى لتسهيل الاختراق.",
                    "إدخال الإبرة بسرعة وثبات وبشكل عمودي تماماً بزاوية 90 درجة.",
                    "سحب المكبس قليلاً للخلف للتأكد من عدم دخول وعاء دموية (إذا ظهر دم، اسحب الإبرة وتخلص منها وابدأ من جديد).",
                    "حقن الدواء ببطء وهدوء لتقليل التوتر والألم في العضلة.",
                    "سحب السرنجة بسرعة وبنفس الزاوية، والضغط الخفيف بقطنة جافة معقمة على موقع الحقن دون تدليك شديد.",
                    "التخلص من السرنجة في صندوق النفايات الحادة ومراقبة استجابة المريض."
                ),
                AppLanguage.EN to listOf(
                    "Verify the '5 rights' of medication administration and draw the medication aseptically.",
                    "Select the injection site (dorsogluteal upper-outer quadrant, ventrogluteal, or deltoid muscle).",
                    "Cleanse the skin with an antiseptic wipe.",
                    "Taut the skin firmly between thumb and fingers.",
                    "Insert the needle quickly and firmly at a 90-degree angle, perpendicular to the skin.",
                    "Aspirate gently to ensure no blood return (if blood appears, withdraw the needle and prepare a new dose).",
                    "Inject the medication slowly and steadily to reduce discomfort.",
                    "Withdraw the needle quickly at the same angle, and apply gentle pressure with a dry swab.",
                    "Discard the needle in the sharps container immediately and assess the patient."
                )
            )
        ),
        NursingProtocol(
            id = "injection_sc",
            title = mapOf(
                AppLanguage.FR to "Injection Sous-Cutanée (SC)",
                AppLanguage.AR to "الحقن تحت الجلد (SC)",
                AppLanguage.EN to "Subcutaneous Injection (SC)"
            ),
            icon = "add",
            steps = mapOf(
                AppLanguage.FR to listOf(
                    "Préparer la seringue ou le stylo prérempli (ex: insuline, héparine type Lovenox).",
                    "Sélectionner la zone (abdomen à distance du nombril, face externe du bras ou de la cuisse).",
                    "Nettoyer la peau si nécessaire (non recommandé systématiquement pour l'insuline à domicile).",
                    "Former un pli cutané large entre le pouce et l'index pour soulever le tissu sous-cutané.",
                    "Introduire l'aiguille rapidement avec un angle de 45° ou 90° selon la longueur de l'aiguille et le tissu adipeux.",
                    "Injecter le produit lentement. S'il s'agit d'héparine préremplie, ne pas chasser la bulle d'air de la seringue avant.",
                    "Maintenir l'aiguille en place 5 à 10 secondes après la fin de l'injection (surtout pour l'insuline).",
                    "Relâcher le pli cutané, retirer l'aiguille et appliquer une légère pression sans masser pour éviter les ecchymoses."
                ),
                AppLanguage.AR to listOf(
                    "تحضير المحقن أو القلم مسبق التعبئة (مثل الأنسولين أو مضادات التخثر كاللوفينوكس).",
                    "اختيار موقع الحقن المناسب (البطن بعيداً عن السرة بمقدار إصبعين، أو الجزء الخارجي من الفخذ أو العضد).",
                    "تنظيف وتطهير الجلد عند الحاجة.",
                    "عمل ثنية جلدية (قرصة خفيفة) بين الإبهام والسبابة لرفع النسيج الدهني تحت الجلد عن العضلة.",
                    "إدخال الإبرة بسرعة بزاوية 45 أو 90 درجة (حسب طول الإبرة وسمك طبقة الدهون).",
                    "حقن الدواء ببطء. (في حقن لوفينوكس الجاهزة، لا تفرغ فقاعة الهواء الموجودة بداخلها قبل الحقن).",
                    "الانتظار لمدة 5 إلى 10 ثوانٍ قبل سحب الإبرة لضمان امتصاص الجرعة بالكامل وعدم تسربها.",
                    "ترك الثنية الجلدية، وسحب السرنجة بلطف، والضغط بقطنة خفيفة دون تدليك لمنع حدوث كدمات زرقاء."
                ),
                AppLanguage.EN to listOf(
                    "Prepare the syringe or prefilled pen (e.g., insulin, low-molecular-weight heparin like Lovenox).",
                    "Select the injection site (abdomen avoiding umbilical area, outer aspect of upper arm, or anterior thigh).",
                    "Cleanse the skin with antiseptic if needed.",
                    "Pinch a skin fold between thumb and forefinger to elevate subcutaneous tissue.",
                    "Insert the needle quickly at a 45-degree or 90-degree angle based on needle length and patient habitus.",
                    "Inject the medication slowly. (For prefilled heparin syringes, do not expel the air bubble before injecting).",
                    "Keep the needle in place for 5-10 seconds after injecting to prevent medication leakage.",
                    "Release the skin pinch, withdraw the needle, and apply light pressure without rubbing to prevent bruising."
                )
            )
        ),
        NursingProtocol(
            id = "prelevement",
            title = mapOf(
                AppLanguage.FR to "Prélèvement sanguin (Prise de sang)",
                AppLanguage.AR to "سحب عينات الدم الوريدي",
                AppLanguage.EN to "Blood Sample Collection"
            ),
            icon = "bloodtype",
            steps = mapOf(
                AppLanguage.FR to listOf(
                    "Vérifier les tubes requis sur le bon d'analyse et préparer les étiquettes.",
                    "Identifier le patient et lui demander s'il est à jeun si requis.",
                    "Appliquer le garrot et choisir une veine (pli du coude de préférence).",
                    "Désinfecter le site de ponction et laisser sécher.",
                    "Insérer l'aiguille montée sur le corps de pompe (Vacutainer) avec un angle de 15-30°.",
                    "Enfoncer les tubes de prélèvement un par un dans l'ordre strict des couleurs (Bleu -> Rouge -> Vert -> Violet -> Gris).",
                    "Homogénéiser chaque tube immédiatement par retournements lents (5 à 10 fois).",
                    "Retirer le garrot dès que le dernier tube commence à se remplir.",
                    "Retirer l'aiguille tout en appliquant une compresse, maintenir une pression ferme, et sécuriser l'aiguille.",
                    "Identifier immédiatement les tubes au lit du patient et les envoyer au laboratoire."
                ),
                AppLanguage.AR to listOf(
                    "التحقق من أنابيب التحليل المطلوبة في استمارة الفحص وتحضير الملصقات الاسمية.",
                    "التحقق من هوية المريض بدقة وسؤاله إذا كان صائمًا (عند الحاجة).",
                    "ربط التورنيكه على العضد واختيار وريد واضح للعين أو اللمس (يفضل طية الكوع).",
                    "تعقيم الجلد جيدًا بمسحة كحولية وتركها تجف تمامًا لمنع تكسر كرات الدم.",
                    "إدخال إبرة السحب (جهاز Vacutainer) في الوريد بزاوية 15 إلى 30 درجة.",
                    "إدخال أنابيب التحليل واحدة تلو الأخرى بالترتيب الصحيح للألوان (الأزرق -> الأحمر -> الأخضر -> البنفسجي -> الرمادي).",
                    "قلب كل أنبوب بلطف من 5 إلى 10 مرات فور امتلائه لخلط الدم مع المواد المضافة دون تكسيره.",
                    "فك التورنيكه بمجرد بدء امتلاء الأنبوب الأخير وقبل سحب الإبرة.",
                    "سحب الإبرة بلطف ووضع قطنة جافة مع الضغط المستمر لعدة دقائق، ووضع لاصق.",
                    "كتابة اسم المريض وبياناته على الأنابيب فوراً أمام المريض وإرسالها للمختبر."
                ),
                AppLanguage.EN to listOf(
                    "Check the required tubes on the laboratory request and prepare patient labels.",
                    "Verify the patient's identity and ask if fasting is required.",
                    "Apply the tourniquet and select a prominent vein (cubital fossa is preferred).",
                    "Disinfect the puncture site and allow it to air-dry.",
                    "Insert the needle mounted on the holder (Vacutainer system) at a 15-30 degree angle.",
                    "Push collection tubes into the holder one by one in the correct order of draw (Blue -> Red -> Green -> Purple -> Grey).",
                    "Invert each filled tube gently 5 to 10 times immediately to mix with additives.",
                    "Release the tourniquet as soon as the last tube begins to fill.",
                    "Withdraw the needle while applying pressure with a dry swab, and engage the needle safety shield.",
                    "Label the tubes immediately at the bedside and arrange transport to the laboratory."
                )
            )
        ),
        NursingProtocol(
            id = "pansement",
            title = mapOf(
                AppLanguage.FR to "Réalisation de pansement stérile",
                AppLanguage.AR to "تضميد الجروح بطريقة معقمة",
                AppLanguage.EN to "Sterile Wound Dressing"
            ),
            icon = "star",
            steps = mapOf(
                AppLanguage.FR to listOf(
                    "Installer le patient confortablement et lui expliquer le soin.",
                    "Se laver les mains et mettre des gants non stériles pour retirer l'ancien pansement.",
                    "Observer l'aspect de la plaie (cicatrisation, rougeurs, odeurs, écoulements) et jeter l'ancien pansement.",
                    "Retirer les gants sales et effectuer une nouvelle désinfection des mains.",
                    "Ouvrir le set de pansement stérile et organiser le champ stérile.",
                    "Mettre des gants stériles ou utiliser les pinces stériles du set.",
                    "Nettoyer la plaie à l'aide de compresses stériles imbibées de sérum physiologique, du propre vers le sale (de l'intérieur vers l'extérieur).",
                    "Sécher la plaie par tapotement avec des compresses stériles sèches.",
                    "Appliquer le nouveau pansement protecteur stérile (compresse, interface, hydrocellulaire) adapté.",
                    "Fixer le pansement et noter la date du soin sur l'adhésif."
                ),
                AppLanguage.AR to listOf(
                    "تهيئة المريض في وضعية مناسبة ومريحة وشرح خطوات الغيار له.",
                    "غسل اليدين وارتداء قفازات نظيفة (غير معقمة) لإزالة الضماد القديم بلطف.",
                    "تقييم وفحص الجرح (اللون، الرائحة، الإفرازات، وجود التهاب) ثم التخلص من الضماد القديم والقفازات المتسخة.",
                    "تطهير اليدين مجددًا بالجيل المعقم.",
                    "فتح عدة الغيار المعقمة (Set de pansement) وتجهيز السطح المعقم والضماد الجديد.",
                    "ارتداء قفازات معقمة أو استخدام الملاقط المعقمة الموجودة في العدة.",
                    "تنظيف الجرح باستخدام شاش معقم مبلل بمحلول ملحي معقم (Physiologique) من الداخل إلى الخارج (من الأنظف إلى الأكثر تلوثًا).",
                    "تجفيف الجرح بلطف بالتربيت عليه باستخدام شاش معقم جاف.",
                    "وضع الضماد الجديد المناسب لنوع الجرح (شاش معقم، ضماد رغوي، أو غير لاصق) لحمايته.",
                    "تثبيت الضماد باللاصق الطبي وكتابة تاريخ وتوقيت الغيار عليه لمتابعته سريعا."
                ),
                AppLanguage.EN to listOf(
                    "Position the patient comfortably and explain the procedure.",
                    "Wash hands and wear non-sterile gloves to remove the old dressing gently.",
                    "Inspect the wound (healing status, redness, odor, drainage) and discard the old dressing and gloves.",
                    "Perform hand hygiene again with alcohol rub.",
                    "Open the sterile dressing pack and organize the sterile field.",
                    "Put on sterile gloves or use the sterile forceps from the pack.",
                    "Cleanse the wound using sterile gauze soaked in normal saline, wiping from clean to dirty (inside to outside).",
                    "Pat dry gently using dry sterile gauze.",
                    "Apply the appropriate new sterile dressing (gauze, hydrocellular, or border plaster).",
                    "Secure the dressing and write the date and time of the procedure on the tape."
                )
            )
        ),
        NursingProtocol(
            id = "sondage",
            title = mapOf(
                AppLanguage.FR to "Sondage urinaire chez l'adulte",
                AppLanguage.AR to "تركيب القسطرة البولية معقمة",
                AppLanguage.EN to "Urinary Catheterization"
            ),
            icon = "create",
            steps = mapOf(
                AppLanguage.FR to listOf(
                    "Vérifier l'indication médicale stricte et s'assurer de l'absence d'antécédent de fausse route.",
                    "Expliquer le déroulement au patient pour favoriser la relaxation sphinctérienne.",
                    "Effectuer une toilette intime rigoureuse au savon doux, puis rincer et sécher.",
                    "Préparer un champ stérile avec la sonde de Foley, le sac collecteur, la seringue d'eau stérile, le gel lubrifiant stérile et les compresses antiseptiques.",
                    "Mettre des gants stériles et installer le champ fenêtré stérile.",
                    "Lubrifier l'extrémité de la sonde de Foley.",
                    "Désinfecter le méat urinaire de l'avant vers l'arrière.",
                    "Introduire doucement la sonde dans l'urètre jusqu'au retour des urines (pousser 5 cm de plus ensuite).",
                    "Gonfler le ballonnet de rétention avec 10 ml d'eau stérile (ne jamais utiliser de sérum salé).",
                    "Tirer légèrement sur la sonde jusqu'à sentir la résistance, connecter le sac collecteur en système clos, et fixer le sac sous le niveau de la vessie."
                ),
                AppLanguage.AR to listOf(
                    "التحقق من الأمر الطبي المكتوب والتأكد من عدم وجود موانع تركيب.",
                    "شرح الإجراء للمريض لمساعدته على الاسترخاء وتسهيل مرور القسطرة عبر الصمام البولي.",
                    "إجراء غسيل وتنظيف عميق لمنطقة الحوض بالماء والصابون ثم تجفيفها.",
                    "فتح عدة القسطرة المعقمة (قسطرة فولي Foley، كيس جمع البول، سرنجة ماء معقم لتثبيت البالون، هلام طبي معقم ومزلق، ومطهر).",
                    "ارتداء قفازات معقمة ووضع المفرش المعقم حول فتحة البول.",
                    "وضع الهلام الطبي المزلق (Gel lubrifiant) على طرف القسطرة لتسهيل دخولها دون ألم.",
                    "تطهير فتحة البول بالمسحات المطهرة باتجاه واحد من الأعلى للأسفل.",
                    "إدخال القسطرة بلطف شديد حتى يتدفق البول في الأنبوب (ثم دفعها 5 سم إضافية لضمان استقرارها في المثانة).",
                    "نفخ بالون التثبيت ببطء بحقن 10 مل من الماء المعقم (لا تستخدم أبدًا محلولًا ملحيًا لأنه قد يتبلور ويسد الصمام).",
                    "سحب القسطرة للخارج برفق حتى تثبت، وتوصيل كيس جمع البول بإحكام، وتعليق الكيس في مستوى أدنى من مستوى المثانة."
                ),
                AppLanguage.EN to listOf(
                    "Verify the medical order and rule out any history of urethral trauma.",
                    "Explain the procedure to the patient to encourage sphincter relaxation.",
                    "Perform perineal care with soap and water, then rinse and dry.",
                    "Prepare a sterile field with the Foley catheter, drainage bag, sterile water syringe, sterile lubricant, and antiseptic swabs.",
                    "Put on sterile gloves and apply the sterile fenestrated drape.",
                    "Apply sterile lubricant to the tip of the catheter.",
                    "Cleanse the urinary meatus with antiseptic using front-to-back strokes.",
                    "Gently insert the catheter into the urethra until urine flows, then advance it 5 cm further.",
                    "Inflate the retention balloon with 10 mL of sterile water (never use normal saline).",
                    "Gently pull back until resistance is felt, connect to the drainage bag in a closed system, and secure the bag below bladder level."
                )
            )
        ),
        NursingProtocol(
            id = "oxygene",
            title = mapOf(
                AppLanguage.FR to "Oxygénothérapie (Administration d'O2)",
                AppLanguage.AR to "العلاج بالأكسجين ومراقبته",
                AppLanguage.EN to "Oxygen Therapy Administration"
            ),
            icon = "refresh",
            steps = mapOf(
                AppLanguage.FR to listOf(
                    "Évaluer le patient (Fréquence respiratoire, dyspnée, coloration des téguments, saturation SpO2).",
                    "Vérifier la prescription d'oxygène (débit en L/min et cible de saturation SpO2).",
                    "Choisir l'interface appropriée (lunettes pour débits < 4 L/min, masque simple pour 4-8 L/min, masque à haute concentration pour débits > 9 L/min).",
                    "Raccorder le dispositif au débitmètre mural d'oxygène (utiliser un humidificateur si débit > 4 L/min).",
                    "Ouvrir le débitmètre au niveau prescrit et vérifier la présence de flux de gaz.",
                    "Placer le dispositif confortablement sur le patient (ajuster les sangles du masque ou insérer les ergots des lunettes dans les narines).",
                    "Expliquer au patient de respirer normalement et de ne pas fumer (danger d'incendie).",
                    "Surveiller régulièrement la saturation en oxygène (SpO2), la fréquence respiratoire et l'état de conscience.",
                    "Inspecter la peau derrière les oreilles et sous le nez pour prévenir les escarres dues aux fixations."
                ),
                AppLanguage.AR to listOf(
                    "تقييم حالة التنفس للمريض (معدل التنفس، وجود ضيق، لون الشفاه والأظافر، قياس نسبة تشبع الأكسجين SpO2).",
                    "التحقق من الوصفة الطبية (معدل التدفق باللتر/دقيقة والنسبة المستهدفة للأكسجين SpO2).",
                    "اختيار الجهاز المناسب (شعبة أنفية Lunettes للتدفق البسيط < 4 لتر/د، ماسك بسيط للتدفق المتوسط 4-8 لتر/د، أو ماسك عالي التركيز للتدفق العالي > 9 لتر/د).",
                    "توصيل الجهاز بمخرج الأكسجين في الجدار (استخدم جهاز الترطيب Humidificateur بالماء المعقم إذا كان التدفق أعلى من 4 لتر/دقيقة).",
                    "فتح مقياس التدفق (Débitmètre) عند الرقم المطلوب والتأكد من تدفق الغاز باليد.",
                    "وضع الجهاز وتعديله على وجه المريض بشكل مريح (إدخال شوكتي الأنبوب الأنفي في فتحتي الأنف وتثبيته خلف الأذنين).",
                    "تنبيه المريض ومرافقيه بالتنفس بشكل طبيعي وعدم استخدام أي مصدر للنار أو التدخين في الغرفة (خطر انفجار الأكسجين).",
                    "مراقبة نسبة الأكسجين SpO2 باستمرار، ومعدل التنفس، ودرجة وعي المريض.",
                    "فحص الجلد خلف الأذنين وتحت الأنف بانتظام للتأكد من عدم حدوث قرح ضغط ناتجة عن الأنابيب."
                ),
                AppLanguage.EN to listOf(
                    "Assess the patient's respiratory status (Respiratory rate, dyspnea, skin color, SpO2 saturation).",
                    "Verify the oxygen prescription (flow rate in L/min and target SpO2 range).",
                    "Choose the appropriate device (nasal cannula for flow < 4 L/min, simple face mask for 4-8 L/min, non-rebreather mask for flow > 9 L/min).",
                    "Connect the device to the oxygen flowmeter (add sterile humidifier if flow rate is > 4 L/min).",
                    "Turn on the flowmeter to the prescribed level and verify gas flow.",
                    "Place the device comfortably on the patient (insert cannula prongs in nares or adjust mask straps).",
                    "Instruct the patient to breathe normally and emphasize fire safety (no smoking/sparking).",
                    "Monitor SpO2 saturation, respiratory rate, and mental status continuously.",
                    "Inspect the skin behind the ears and nose regularly to prevent pressure sores from straps."
                )
            )
        ),
        NursingProtocol(
            id = "rcp",
            title = mapOf(
                AppLanguage.FR to "Réanimation Cardio-Pulmonaire (RCP)",
                AppLanguage.AR to "الإنعاش القلبي الرئوي الأساسي (RCP)",
                AppLanguage.EN to "Cardiopulmonary Resuscitation (CPR)"
            ),
            icon = "favorite",
            steps = mapOf(
                AppLanguage.FR to listOf(
                    "Sécuriser la zone (S'assurer que le sauveteur et la victime sont hors de danger).",
                    "Évaluer la conscience : Secouer doucement les épaules et poser une question simple. (Pas de réponse = Inconscient).",
                    "Évaluer la respiration : Libérer les voies aériennes en basculant la tête en arrière et regarder si la poitrine se soulève pendant 10 secondes maximum. (Pas de respiration = Arrêt respiratoire).",
                    "Appeler à l'aide immédiatement (Code Bleu, SAMU 15) et demander un défibrillateur (DSA).",
                    "Placer le patient sur un plan dur et se positionner à genoux à côté de lui.",
                    "Commencer le Massage Cardiaque Externe (MCE) : Placer le talon d'une main au centre de la poitrine (bas du sternum), l'autre main croisée au-dessus, bras tendus.",
                    "Comprimer le thorax régulièrement à une fréquence de 100 à 120 compressions par minute, sur une profondeur de 5 à 6 cm.",
                    "Pratiquer l'alternance : 30 compressions thoraciques suivies de 2 insufflations (bouche-à-bouche ou ballon auto-remplisseur Ambu) si formé, sinon masser en continu.",
                    "Allumer le défibrillateur (DSA) dès sa réception et suivre scrupuleusement ses instructions vocales.",
                    "Poursuivre la réanimation sans interruption jusqu'à l'arrivée des secours qualifiés ou la reprise d'une respiration normale."
                ),
                AppLanguage.AR to listOf(
                    "تأمين موقع الحادث للتأكد من سلامتك وسلامة الضحية قبل البدء.",
                    "تقييم درجة الوعي: هز كتفي المريض بلطف ومناداته بصوت عالٍ (إذا لم يستجب = فاقد للوعي).",
                    "تقييم التنفس: إرجاع الرأس للخلف ورفع الذقن لفتح مجرى الهواء، ومراقبة حركة الصدر وسماع التنفس لمدة 10 ثوانٍ كحد أقصى (إذا كان لا يتنفس = توقف تنفس وقرار إنعاش).",
                    "استدعاء المساعدة فوراً (الاتصال بالرقم الطبي الداخلي للمستشفى أو الإسعاف) وطلب إحضار جهاز الصدمات الكهربائية (AED).",
                    "وضع المريض على سطح صلب مستوٍ، والركوع بجانبه.",
                    "بدء تدليك الصدر (الضغطات الصدرية): وضع كعب إحدى اليدين في منتصف صدر المريض (النصف السفلي من عظمة القص) ووضع اليد الأخرى فوقها وشبك الأصابع مع الحفاظ على استقامة الذراعين.",
                    "الضغط على الصدر بمعدل 100 إلى 120 ضغطة في الدقيقة، وبعمق يتراوح بين 5 إلى 6 سم، والسماح للصدر بالارتداد بالكامل بعد كل ضغطة.",
                    "الحفاظ على نسبة التناوب: 30 ضغطة صدرية متبوعة بنفختين إنقاذيتين (عبر ماسك الأمبو Ambu) إذا كنت مدرباً، أو استمر بالتدليك المتواصل بدون انقطاع.",
                    "تشغيل جهاز الصدمات الكهربائية (AED) فور وصوله وتوصيل الأقطاب بجلد الصدر واتباع التعليمات الصوتية للجهاز بدقة.",
                    "الاستمرار في عملية الإنعاش دون توقف حتى وصول الفريق المختص أو ظهور علامات واضحة لاستعادة الوعي والتنفس الطبيعي."
                ),
                AppLanguage.EN to listOf(
                    "Ensure scene safety (Check that the rescuer and victim are in a safe environment).",
                    "Assess responsiveness: Tap the shoulders and shout. (No response = Unconscious).",
                    "Assess breathing: Open the airway by tilting the head and lifting the chin, then observe chest rise for no more than 10 seconds. (No normal breathing = Cardiac Arrest).",
                    "Shout for help immediately, call emergency services (Code Blue / EMS), and ask for an AED.",
                    "Place the victim on a firm, flat surface and kneel beside them.",
                    "Start chest compressions: Place the heel of one hand in the center of the chest (lower half of sternum), interlock your other hand on top, keeping arms straight.",
                    "Compress the chest at a rate of 100 to 120 compressions per minute, to a depth of 5 to 6 cm.",
                    "Maintain the compression-to-ventilation ratio: 30 compressions followed by 2 breaths (using pocket mask or bag-valve-mask), or perform continuous compressions.",
                    "Turn on the AED as soon as it arrives, apply pads to the bare chest, and follow the voice prompts exactly.",
                    "Continue CPR without interruption until advanced life support arrives or the victim shows signs of recovery (normal breathing)."
                )
            )
        )
    )
}
