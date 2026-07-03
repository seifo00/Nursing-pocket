package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.MainViewModel
import com.example.Screen
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

// ==========================================
// 1. AI NURSING ASSISTANT SCREEN
// ==========================================
data class AiPresetScenario(
    val title: String,
    val age: String,
    val bp: String,
    val pulse: String,
    val temp: String,
    val spo2: String,
    val symptoms: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val coroutineScope = rememberCoroutineScope()
    var age by remember { mutableStateOf("") }
    var bp by remember { mutableStateOf("") }
    var pulse by remember { mutableStateOf("") }
    var temp by remember { mutableStateOf("") }
    var spo2 by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf("") }

    var isAnalyzing by remember { mutableStateOf(false) }
    var analysisResult by remember { mutableStateOf<AiReport?>(null) }
    var useCloudAi by remember { mutableStateOf(false) }

    // Predefined clinical scenarios for one-click loading
    val scenarios = listOf(
        AiPresetScenario(
            title = if (lang == AppLanguage.FR) "Douleur Thoracique" else if (lang == AppLanguage.AR) "ألم في الصدر" else "Chest Pain",
            age = "62", bp = "165/95", pulse = "102", temp = "37.1", spo2 = "91",
            symptoms = if (lang == AppLanguage.FR) "Douleur thoracique rétrosternale irradiant vers le bras gauche, dyspnée de repos, sueurs froides depuis 45 minutes." else "Retrosternal crushing chest pain radiating to left arm, rest dyspnea, cold diaphoresis for 45 mins."
        ),
        AiPresetScenario(
            title = if (lang == AppLanguage.FR) "Sepsis suspect" else if (lang == AppLanguage.AR) "اشتباه تسمم الدم" else "Suspected Sepsis",
            age = "45", bp = "88/45", pulse = "118", temp = "39.4", spo2 = "96",
            symptoms = if (lang == AppLanguage.FR) "Frissons solennels, confusion fluctuante, sonde urinaire trouble avec urines fétides, marbrures aux genoux." else "Rigors, fluctuating confusion, turbid malodorous urine from indwelling catheter, knee mottling."
        ),
        AiPresetScenario(
            title = if (lang == AppLanguage.FR) "Crise d'Asthme" else if (lang == AppLanguage.AR) "نوبة ربو حادة" else "Acute Asthma Exacerbation",
            age = "18", bp = "125/80", pulse = "122", temp = "36.5", spo2 = "87",
            symptoms = if (lang == AppLanguage.FR) "Difficulté respiratoire aiguë, frein expiratoire avec sibilants audibles, impossibilité de finir ses phrases, toux sèche." else "Acute respiratory distress with audible expiratory wheezing, unable to complete sentences, dry cough."
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = if (lang == AppLanguage.FR) "Assistant Infirmier IA" else if (lang == AppLanguage.AR) "المساعد التمريضي الذكي" else "AI Nursing Assistant",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (lang == AppLanguage.FR) "Analyse clinique, priorités de surveillance et alertes d'urgence." else "Clinical decision-support tool for priority tracking and alarms.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Disclaimer (MANDATORY)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (lang == AppLanguage.FR) {
                            "AVERTISSEMENT : Cet outil est uniquement destiné au support d'apprentissage clinique. Il ne constitue pas un système de diagnostic médical et ne remplace jamais le jugement d'un professionnel de santé diplômé."
                        } else if (lang == AppLanguage.AR) {
                            "تنبيه: هذه الأداة مخصصة حصرياً للمساعدة والتعليم السريري. لا تشكل نظام تشخيص طبي ولا بديل عن التقييم والقرار المهني السريري للممرض."
                        } else {
                            "DISCLAIMER: This tool is strictly for clinical learning and reference. It is not a medical diagnostic system and should never replace the professional judgment of a licensed healthcare provider."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Quick Preset Scenarios
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (lang == AppLanguage.FR) "Scénarios Prédéfinis :" else if (lang == AppLanguage.AR) "سيناريوهات سريعة:" else "Quick Scenarios:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    scenarios.forEach { scenario ->
                        SuggestionChip(
                            onClick = {
                                age = scenario.age
                                bp = scenario.bp
                                pulse = scenario.pulse
                                temp = scenario.temp
                                spo2 = scenario.spo2
                                symptoms = scenario.symptoms
                            },
                            label = { Text(scenario.title) }
                        )
                    }
                }
            }
        }

        // Clinical Parameters Inputs
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (lang == AppLanguage.FR) "Données du Patient" else if (lang == AppLanguage.AR) "بيانات المريض السريرية" else "Patient Clinical Data",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text(if (lang == AppLanguage.FR) "Âge" else "Age") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = bp,
                            onValueChange = { bp = it },
                            label = { Text(if (lang == AppLanguage.FR) "TA (mmHg)" else "BP (mmHg)") },
                            placeholder = { Text("120/80") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = pulse,
                            onValueChange = { pulse = it },
                            label = { Text(if (lang == AppLanguage.FR) "FC (bpm)" else "HR (bpm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = temp,
                            onValueChange = { temp = it },
                            label = { Text(if (lang == AppLanguage.FR) "T° (°C)" else "Temp (°C)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            placeholder = { Text("37.0") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = spo2,
                            onValueChange = { spo2 = it },
                            label = { Text("SpO2 (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = symptoms,
                        onValueChange = { symptoms = it },
                        label = { Text(if (lang == AppLanguage.FR) "Symptômes & Histoire clinique" else if (lang == AppLanguage.AR) "الأعراض والتاريخ السريري" else "Symptoms & Clinical Narrative") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Cloud vs Local Mode Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (lang == AppLanguage.FR) "Analyse IA Avancée (En Ligne)" else "Advanced AI Cloud Analysis",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Cloud,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Switch(
                            checked = useCloudAi,
                            onCheckedChange = { useCloudAi = it }
                        )
                    }

                    Button(
                        onClick = {
                            isAnalyzing = true
                            coroutineScope.launch {
                                analysisResult = performClinicalAnalysis(
                                    age, bp, pulse, temp, spo2, symptoms, useCloudAi, lang
                                )
                                isAnalyzing = false
                                viewModel.addXp(25) // Award XP for clinical analysis
                            }
                        },
                        enabled = !isAnalyzing && symptoms.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (lang == AppLanguage.FR) "Lancer l'Analyse" else if (lang == AppLanguage.AR) "تحليل البيانات" else "Run AI Clinical Analysis")
                        }
                    }
                }
            }
        }

        // Analysis Report Result View
        if (analysisResult != null) {
            item {
                val report = analysisResult!!
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = if (lang == AppLanguage.FR) "Rapport d'Analyse Clinique" else if (lang == AppLanguage.AR) "تقرير التحليل السريري الذكي" else "AI Clinical Report",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Urgent Warning Banner (Red)
                    if (report.emergencyWarnings.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F1)),
                            border = BorderStroke(1.5.dp, Color(0xFFBA1A1A))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Campaign, contentDescription = "Emergency Alert", tint = Color(0xFFBA1A1A), modifier = Modifier.size(28.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (lang == AppLanguage.FR) "ALERTE D'URGENCE VITALE" else if (lang == AppLanguage.AR) "تحذير طوارئ خطير" else "CRITICAL EMERGENCY ALERT",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFBA1A1A),
                                        fontSize = 15.sp
                                    )
                                }
                                report.emergencyWarnings.forEach { warning ->
                                    Text(
                                        text = "• $warning",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF8C0009),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Priorities (Orange/Amber)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FormatListNumbered, contentDescription = "Priorities", tint = Color(0xFFE65100))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (lang == AppLanguage.FR) "Priorités Infirmières" else if (lang == AppLanguage.AR) "الأولويات التمريضية" else "Nursing Priorities",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFFE65100)
                                )
                            }
                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            report.priorities.forEachIndexed { i, priority ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("${i + 1}.", fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                                    Text(priority, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    // Monitoring Recommendations (Blue)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonitorHeart, contentDescription = "Monitoring", tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (lang == AppLanguage.FR) "Paramètres à Surveiller" else if (lang == AppLanguage.AR) "مراقبة المؤشرات الحيوية" else "Monitoring Requirements",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            report.monitoring.forEach { item ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp).align(Alignment.CenterVertically))
                                    Text(item, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    // Clinical Alerts & Complications
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.HealthAndSafety, contentDescription = "Complications", tint = Color(0xFF43A047))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (lang == AppLanguage.FR) "Complications Potentielles" else if (lang == AppLanguage.AR) "المضاعفات المحتملة" else "Possible Complications & Alerts",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF43A047)
                                )
                            }
                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            report.complications.forEach { comp ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.Feedback, contentDescription = null, tint = Color(0xFF43A047), modifier = Modifier.size(16.dp).align(Alignment.CenterVertically))
                                    Text(comp, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Data class structure for AI/Heuristic clinical reports
data class AiReport(
    val priorities: List<String>,
    val monitoring: List<String>,
    val complications: List<String>,
    val emergencyWarnings: List<String>
)

// Clinical decision parser - Works Offline via clinical rule-sets and supports online Gemini API
suspend fun performClinicalAnalysis(
    age: String, bp: String, pulse: String, temp: String, spo2: String, symptoms: String,
    useCloud: Boolean, lang: AppLanguage
): AiReport = withContext(Dispatchers.IO) {
    if (useCloud && BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY") {
        try {
            val responseText = callGeminiClinicalAssistant(age, bp, pulse, temp, spo2, symptoms, lang)
            if (responseText.isNotBlank() && !responseText.contains("Error")) {
                val json = JSONObject(responseText)
                val prioritiesArr = json.getJSONArray("priorities")
                val monitoringArr = json.getJSONArray("monitoring")
                val complicationsArr = json.getJSONArray("complications")
                val emergencyArr = json.getJSONArray("emergencyWarnings")

                val prioritiesList = mutableListOf<String>()
                for (i in 0 until prioritiesArr.length()) prioritiesList.add(prioritiesArr.getString(i))

                val monitoringList = mutableListOf<String>()
                for (i in 0 until monitoringArr.length()) monitoringList.add(monitoringArr.getString(i))

                val complicationsList = mutableListOf<String>()
                for (i in 0 until complicationsArr.length()) complicationsList.add(complicationsArr.getString(i))

                val emergencyList = mutableListOf<String>()
                for (i in 0 until emergencyArr.length()) emergencyList.add(emergencyArr.getString(i))

                return@withContext AiReport(prioritiesList, monitoringList, complicationsList, emergencyList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- High-Performance Offline Fallback Rule Engine ---
    val lowerSymptoms = symptoms.lowercase()
    val prioritiesList = mutableListOf<String>()
    val monitoringList = mutableListOf<String>()
    val complicationsList = mutableListOf<String>()
    val emergencyList = mutableListOf<String>()

    // Core vital-sign based alarms
    val spo2Val = spo2.toIntOrNull()
    val pulseVal = pulse.toIntOrNull()
    val tempVal = temp.toDoubleOrNull()

    if (spo2Val != null && spo2Val < 90) {
        emergencyList.add(
            if (lang == AppLanguage.FR) "Détresse respiratoire sévère ou hypoxémie critique (SpO2 < 90%). Risque d'arrêt respiratoire."
            else if (lang == AppLanguage.AR) "نقص تأكسج حاد وحرج (SpO2 أقل من 90%). خطر توقف التنفس."
            else "Severe clinical hypoxemia (SpO2 < 90%). High risk of imminent respiratory failure."
        )
        prioritiesList.add(
            if (lang == AppLanguage.FR) "Oxygénothérapie immédiate à haut débit (Masque haute concentration ou ventilation non invasive)."
            else "Immediate high-flow oxygen administration; prepare for respiratory support."
        )
    }

    if (pulseVal != null && pulseVal > 120) {
        emergencyList.add(
            if (lang == AppLanguage.FR) "Tachycardie clinique sévère (>120 bpm) - Rechercher un choc hypovolémique, sceptique ou une arythmie instable."
            else "Severe clinical tachycardia (>120 bpm) - Evaluate for sepsis, shock, or cardiorespiratory distress."
        )
    }

    if (tempVal != null && tempVal >= 39.0) {
        prioritiesList.add(
            if (lang == AppLanguage.FR) "Prélèvement d'hémocultures immédiates avant d'initier l'antibiothérapie prescrite, et administration d'antipyrétiques."
            else "Obtain immediate blood cultures and administer ordered antipyretics."
        )
    }

    // Symptom-based heuristic analysis
    if (lowerSymptoms.contains("chest pain") || lowerSymptoms.contains("poitrine") || lowerSymptoms.contains("صدر")) {
        prioritiesList.add(if (lang == AppLanguage.FR) "Réaliser immédiatement un ECG de contrôle 12 dérivations en urgence (objectif < 10 mins)." else "Perform 12-lead ECG immediately (aim <10 minutes).")
        prioritiesList.add(if (lang == AppLanguage.FR) "Prendre une voie veineuse périphérique de gros calibre et évaluer la douleur (échelle EVA)." else "Establish large-bore IV access and assess pain using VAS.")
        monitoringList.add(if (lang == AppLanguage.FR) "Monitorer continuellement le segment ST sur la lunette cardiaque." else "Continuous ECG monitoring focusing on ST segment changes.")
        monitoringList.add(if (lang == AppLanguage.FR) "Surveiller les enzymes cardiaques sériques (Troponine I ou T ultra-sensible)." else "Monitor cardiac biomarkers (Troponin levels).")
        complicationsList.add(if (lang == AppLanguage.FR) "Infarctus aigu du myocarde (IDM) ou syndrome coronarien aigu." else "Acute myocardial infarction or ventricular dysrhythmias.")
        complicationsList.add(if (lang == AppLanguage.FR) "Troubles du rythme ventriculaire mortels (Fibrillation ventriculaire)." else "Lethal ventricular arrhythmias.")
        emergencyList.add(if (lang == AppLanguage.FR) "Signes d'infarctus ou de syndrome coronarien aigu!" else "Active signs suggestive of Acute Coronary Syndrome!")
    } else if (lowerSymptoms.contains("sepsis") || lowerSymptoms.contains("choc") || lowerSymptoms.contains("fever") || lowerSymptoms.contains("fièvre") || lowerSymptoms.contains("حرارة")) {
        prioritiesList.add(if (lang == AppLanguage.FR) "Remplissage vasculaire rapide par cristalloïdes (30 ml/kg) si hypotension artérielle." else "Initiate rapid fluid resuscitation (30 mL/kg crystalloids) for hypotension.")
        prioritiesList.add(if (lang == AppLanguage.FR) "Administration d'antibiotiques à large spectre par voie intraveineuse sous 1h." else "Administer broad-spectrum IV antibiotics within 1 hour.")
        monitoringList.add(if (lang == AppLanguage.FR) "Surveiller étroitement la diurèse horaire (poser une sonde urinaire si choc)." else "Track hourly urine output strictly via indwelling urinary catheter.")
        monitoringList.add(if (lang == AppLanguage.FR) "Suivre le taux de Lactate sanguin toutes les 2 à 4 heures." else "Follow blood lactate clearance dynamics every 2-4 hours.")
        complicationsList.add(if (lang == AppLanguage.FR) "Choc septique réfractaire nécessitant des amines vasoactives (Noradrénaline)." else "Refractory septic shock requiring vasopressor infusion (Norepinephrine).")
        complicationsList.add(if (lang == AppLanguage.FR) "Défaillance multiviscérale (Symptôme de coagulation intravasculaire disséminée)." else "Multiple organ dysfunction syndrome (MODS) or DIC.")
        emergencyList.add(if (lang == AppLanguage.FR) "Risque majeur de collapsus cardiovasculaire par choc sceptique!" else "High risk of profound cardiovascular collapse from septic shock!")
    } else {
        // Generic defaults
        prioritiesList.add(if (lang == AppLanguage.FR) "Sécuriser les voies aériennes et stabiliser le patient." else "Secure patient airway, breathing, and circulation (ABCs).")
        prioritiesList.add(if (lang == AppLanguage.FR) "Prendre une voie veineuse de sécurité et faire le bilan biologique prescrit." else "Establish peripheral access and obtain standard laboratory tests.")
        monitoringList.add(if (lang == AppLanguage.FR) "Prendre les constantes cliniques toutes les 15 minutes à 1 heure selon l'évolution." else "Monitor standard vital signs every 15-30 minutes.")
        complicationsList.add(if (lang == AppLanguage.FR) "Détérioration clinique rapide nécessitant un appel de l'équipe de réanimation." else "Rapid physiological decline requiring rapid response team activation.")
    }

    AiReport(prioritiesList, monitoringList, complicationsList, emergencyList)
}

// REST call implementation for Cloud-based Gemini clinical reasoning
suspend fun callGeminiClinicalAssistant(
    age: String, bp: String, pulse: String, temp: String, spo2: String, symptoms: String, lang: AppLanguage
): String = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    val systemPrompt = """
        You are a highly experienced, hospital-grade Clinical Nursing Decision Support system.
        Analyze the patient parameters and symptoms, and provide critical nursing assistance strictly in JSON format.
        Return a JSON object containing exactly 4 arrays:
        - "priorities": (strings) numbered priority nursing interventions.
        - "monitoring": (strings) list of monitoring rules.
        - "complications": (strings) list of potential dangerous complications to anticipate.
        - "emergencyWarnings": (strings) critical warnings of severe decompensation.
        Provide response in the requested language: ${lang.name} (either FR, AR or EN). Keep it professional, scientific and concise. Do not include markdown wraps.
    """.trimIndent()

    val prompt = "Patient Age: $age. BP: $bp. HR/Pulse: $pulse. Temp: $temp. SpO2: $spo2. Narrative: $symptoms"

    try {
        val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true

        val requestBody = JSONObject()
        val contentsArr = JSONArray()
        val contentObj = JSONObject()
        val partsArr = JSONArray()
        val partObj = JSONObject()
        partObj.put("text", prompt)
        partsArr.put(partObj)
        contentObj.put("parts", partsArr)
        contentsArr.put(contentObj)
        requestBody.put("contents", contentsArr)

        val sysObj = JSONObject()
        val sysParts = JSONArray()
        val sysPart = JSONObject()
        sysPart.put("text", systemPrompt)
        sysParts.put(sysPart)
        sysObj.put("parts", sysParts)
        requestBody.put("systemInstruction", sysObj)

        val configObj = JSONObject()
        val formatObj = JSONObject()
        formatObj.put("mimeType", "application/json")
        configObj.put("responseFormat", formatObj)
        requestBody.put("generationConfig", configObj)

        conn.outputStream.write(requestBody.toString().toByteArray())

        val responseStr = conn.inputStream.bufferedReader().use { it.readText() }
        val rootObj = JSONObject(responseStr)
        val text = rootObj.getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")
        text
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}


// ==========================================
// 2. SMART DRUG SAFETY SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrugSafetyScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val drugs = remember {
        listOf(
            ClinicalDrug(
                id = "morphine",
                name = "Morphine (Opiacé)",
                maxSingleDose = 10.0,
                doseUnit = "mg",
                contraindications = listOf("Insuffisance respiratoire sévère", "Traumatisme crânien non stabilisé", "Insuffisance hépatique sévère"),
                precautions = "Injecter lentement en IV (>3 minutes). Toujours avoir de la Naloxone à disposition immédiate.",
                highRisk = true,
                monitoring = listOf("Fréquence respiratoire stricte (Alerte si < 10/min)", "Score de sédation de Rudkin", "Pression artérielle")
            ),
            ClinicalDrug(
                id = "kcl",
                name = "Potassium (KCl) 10%",
                maxSingleDose = 20.0,
                doseUnit = "mmol",
                contraindications = listOf("Hyperkaliémie > 5.0 mmol/L", "Insuffisance rénale anurique", "Syndrome d'addison"),
                precautions = "DANGER DE MORT : Ne JAMAIS injecter en IV direct. Toujours diluer dans un soluté et perfuser lentement.",
                highRisk = true,
                monitoring = listOf("Surveillance ECG continu (Ondes T pointues)", "Kaliémie de contrôle toutes les 4h", "Fonction rénale")
            ),
            ClinicalDrug(
                id = "adrenaline",
                name = "Adrénaline (Épinéphrine)",
                maxSingleDose = 1.0,
                doseUnit = "mg",
                contraindications = listOf("Aucune contre-indication en cas d'arrêt cardiorespiratoire ou choc anaphylactique."),
                precautions = "IV direct uniquement dans l'arrêt cardiaque. En perfusion continue, diluer et administrer via une voie veineuse centrale.",
                highRisk = true,
                monitoring = listOf("Fréquence cardiaque continue", "Pression artérielle invasive", "Surveillance de la nécrose cutanée locale")
            ),
            ClinicalDrug(
                id = "furosemide",
                name = "Furosémide (Lasix)",
                maxSingleDose = 80.0,
                doseUnit = "mg",
                contraindications = listOf("Hypovolémie marquée", "Déshydratation sévère", "Obstacle urétral non levé"),
                precautions = "Risque d'hypokaliémie et de déshydratation aiguë. Perfuser lentement si dose élevée.",
                highRisk = false,
                monitoring = listOf("Diurèse horaire stricte", "Pression artérielle", "Kaliémie et Natrémie journalières")
            )
        )
    }

    var selectedDrugIndex by remember { mutableStateOf(0) }
    var doseInput by remember { mutableStateOf("") }
    var safetyStatus by remember { mutableStateOf<SafetyResult?>(null) }

    val activeDrug = drugs[selectedDrugIndex]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Screen Header
        item {
            Text(
                text = if (lang == AppLanguage.FR) "Sécurité Posologique" else if (lang == AppLanguage.AR) "الأمان الدوائي الذكي" else "Smart Drug Safety",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Selection of clinical high-risk drugs
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (lang == AppLanguage.FR) "Sélectionner un médicament" else "Select Medication",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    drugs.forEachIndexed { idx, drug ->
                        FilterChip(
                            selected = selectedDrugIndex == idx,
                            onClick = {
                                selectedDrugIndex = idx
                                doseInput = ""
                                safetyStatus = null
                            },
                            label = {
                                Text(
                                    text = drug.name,
                                    fontWeight = if (drug.highRisk) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = if (drug.highRisk) {
                                { Icon(Icons.Default.PriorityHigh, contentDescription = "High Risk", modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }
            }
        }

        // Selected drug information card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = activeDrug.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (activeDrug.highRisk) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.FR) "HAUT RISQUE" else "HIGH RISK",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Contraindications
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = if (lang == AppLanguage.FR) "Contre-indications :" else "Contraindications:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        activeDrug.contraindications.forEach { ci ->
                            Text(text = "• $ci", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    // Administration instructions
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = if (lang == AppLanguage.FR) "Mode d'administration :" else "Administration details:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = activeDrug.precautions, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        // Posology Checker Input
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (lang == AppLanguage.FR) "Vérification de la dose prescrite" else "Check Prescribed Dose",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = doseInput,
                            onValueChange = { doseInput = it },
                            label = { Text(if (lang == AppLanguage.FR) "Dose proposée" else "Proposed Dose") },
                            placeholder = { Text("Ex: 5") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            trailingIcon = { Text(activeDrug.doseUnit, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 12.dp)) }
                        )

                        Button(
                            onClick = {
                                val dose = doseInput.toDoubleOrNull()
                                if (dose != null) {
                                    safetyStatus = checkDoseSafety(dose, activeDrug, lang)
                                    viewModel.addXp(15) // XP Award
                                }
                            },
                            enabled = doseInput.isNotBlank(),
                            modifier = Modifier.height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (lang == AppLanguage.FR) "Vérifier" else "Check")
                        }
                    }
                }
            }
        }

        // Live alert result (Red / Orange / Green)
        if (safetyStatus != null) {
            item {
                val result = safetyStatus!!
                val color = when (result.level) {
                    SafetyLevel.SAFE -> Color(0xFF2E7D32)
                    SafetyLevel.CAUTION -> Color(0xFFE65100)
                    SafetyLevel.DANGER -> Color(0xFFC62828)
                }
                val bgColor = when (result.level) {
                    SafetyLevel.SAFE -> Color(0xFFE8F5E9)
                    SafetyLevel.CAUTION -> Color(0xFFFFF3E0)
                    SafetyLevel.DANGER -> Color(0xFFFFEBEE)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    border = BorderStroke(1.5.dp, color)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (result.level) {
                                    SafetyLevel.SAFE -> Icons.Default.CheckCircle
                                    SafetyLevel.CAUTION -> Icons.Default.Warning
                                    SafetyLevel.DANGER -> Icons.Default.GppBad
                                },
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = result.title,
                                fontWeight = FontWeight.ExtraBold,
                                color = color,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Text(
                            text = result.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = color.copy(alpha = 0.9f)
                        )

                        Divider(color = color.copy(alpha = 0.2f))

                        // Surveillance recommendations
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = if (lang == AppLanguage.FR) "Surveillance Infirmière Requise :" else "Required Clinical Monitoring:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                            activeDrug.monitoring.forEach { rule ->
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("•", fontWeight = FontWeight.Bold, color = color)
                                    Text(text = rule, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ClinicalDrug(
    val id: String,
    val name: String,
    val maxSingleDose: Double,
    val doseUnit: String,
    val contraindications: List<String>,
    val precautions: String,
    val highRisk: Boolean,
    val monitoring: List<String>
)

enum class SafetyLevel { SAFE, CAUTION, DANGER }

data class SafetyResult(
    val level: SafetyLevel,
    val title: String,
    val description: String
)

fun checkDoseSafety(dose: Double, drug: ClinicalDrug, lang: AppLanguage): SafetyResult {
    if (dose > drug.maxSingleDose) {
        return SafetyResult(
            level = SafetyLevel.DANGER,
            title = if (lang == AppLanguage.FR) "SURDOSAGE - RISQUE MAJEUR" else "OVERDOSE - CRITICAL ALERT",
            description = if (lang == AppLanguage.FR) {
                "La dose demandée ($dose ${drug.doseUnit}) dépasse largement la dose maximale recommandée de ${drug.maxSingleDose} ${drug.doseUnit}. Risque d'effets secondaires mortels immediats."
            } else {
                "The requested dose ($dose ${drug.doseUnit}) significantly exceeds the recommended maximum single dose of ${drug.maxSingleDose} ${drug.doseUnit}. Risk of life-threatening toxicity."
            }
        )
    }

    if (drug.highRisk) {
        return SafetyResult(
            level = SafetyLevel.CAUTION,
            title = if (lang == AppLanguage.FR) "ATTENTION - MÉDICAMENT DE HAUTE VIGILANCE" else "CAUTION - HIGH ALERT MEDICATION",
            description = if (lang == AppLanguage.FR) {
                "La dose ($dose ${drug.doseUnit}) est conforme, mais ce médicament fait partie de la liste des produits à haut risque. Administrer avec double contrôle indépendant."
            } else {
                "The dose ($dose ${drug.doseUnit}) is within limits. However, this is a High Alert Medication. Administer with strict independent double-checks."
            }
        )
    }

    return SafetyResult(
        level = SafetyLevel.SAFE,
        title = if (lang == AppLanguage.FR) "CONFORME - SÉCURISÉ" else "SAFE - WITHIN NORMAL POSOLOGY",
        description = if (lang == AppLanguage.FR) {
            "La dose de $dose ${drug.doseUnit} est sécurisée et valide par rapport aux recommandations cliniques."
        } else {
            "The dose of $dose ${drug.doseUnit} conforms perfectly to recommended safety guidelines."
        }
    )
}


// ==========================================
// 3. VOICE CHARTING SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceChartingScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isRecording by remember { mutableStateOf(false) }
    var currentProgress by remember { mutableStateOf(0f) }

    var dictatedText by remember { mutableStateOf("") }
    var reportFormat by remember { mutableStateOf<ReportBundle?>(null) }
    var isFormatting by remember { mutableStateOf(false) }

    // Predefined simulation dictations for EN/FR/AR
    val simulationOptions = listOf(
        Pair(
            if (lang == AppLanguage.FR) "Simuler Douleur Post-Op" else "Simulate Post-Op Pain",
            if (lang == AppLanguage.FR) {
                "Patient dans la chambre 302B se plaint d'une douleur abdominale de 8 sur 10. J'ai administré de la morphine 2 milligrammes en intraveineuse lente à 10 heures. Signes vitaux stables, tension à 120 par 80, rythme cardiaque à 78. Je réévalue la douleur dans 30 minutes."
            } else {
                "Patient in room 302B complains of sharp abdominal pain rated 8 out of 10. Administered Morphine 2 mg slow IV at 10:00 AM. Vital signs are stable: BP 120/80, heart rate 78. Will re-evaluate pain in 30 minutes."
            }
        ),
        Pair(
            if (lang == AppLanguage.FR) "Simuler Administration Diurétique" else "Simulate Diuretic Administration",
            if (lang == AppLanguage.FR) {
                "Jean Dupont chambre 301. Administration de Furosémide 40 mg en IV direct ce matin. Diurèse de 800 cc mesurée après 2 heures. Pas de signes de dyspnée, pas de crépitants pulmonaires détectés."
            } else {
                "Jean Dupont room 301. Administered Furosemide 40 mg direct IV this morning. Urine output of 800 cc measured after 2 hours. Patient reports no shortness of breath, clear lung sounds."
            }
        )
    )

    // Pulse animation simulation for recording state
    LaunchedEffect(isRecording) {
        if (isRecording) {
            currentProgress = 0f
            while (isRecording) {
                for (i in 0..100) {
                    if (!isRecording) break
                    currentProgress = i / 100f
                    delay(15)
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = if (lang == AppLanguage.FR) "Dictée Vocale Clinique" else if (lang == AppLanguage.AR) "التدوين الصوتي السريري" else "Voice Charting Suite",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Voice Recorder simulation Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isRecording) (if (lang == AppLanguage.FR) "Enregistrement en cours..." else "Recording...") else (if (lang == AppLanguage.FR) "Prêt à dicter" else "Ready to Record"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )

                    // Visual simulated pulsing waveform
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .drawWithContent {
                                if (isRecording) {
                                    drawCircle(
                                        color = Color.Red.copy(alpha = 1f - currentProgress),
                                        radius = 50.dp.toPx() * currentProgress,
                                        style = Stroke(width = 3.dp.toPx())
                                    )
                                }
                                drawContent()
                            }
                            .clip(CircleShape)
                            .background(if (isRecording) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.primaryContainer)
                            .clickable { isRecording = !isRecording },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mic",
                            tint = if (isRecording) Color.Red else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = if (lang == AppLanguage.FR) "Touchez le micro pour commencer à dicter" else "Tap microphone to dictate clinical notes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Quick simulation buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        simulationOptions.forEach { (label, content) ->
                            ElevatedButton(
                                onClick = {
                                    dictatedText = content
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(label, fontSize = 11.sp, maxLines = 1)
                            }
                        }
                    }
                }
            }
        }

        // Live text edit view
        item {
            OutlinedTextField(
                value = dictatedText,
                onValueChange = { dictatedText = it },
                label = { Text(if (lang == AppLanguage.FR) "Texte dicté" else "Dictated Text") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Action formatting button
        item {
            Button(
                onClick = {
                    isFormatting = true
                    coroutineScope.launch {
                        delay(1200) // Simulate cognitive processing
                        reportFormat = formatClinicalReports(dictatedText, lang)
                        isFormatting = false
                        viewModel.addXp(30)
                    }
                },
                enabled = dictatedText.isNotBlank() && !isFormatting,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isFormatting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Analytics, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (lang == AppLanguage.FR) "Formater en Rapports Cliniques" else "Convert to Clinical Reports")
                }
            }
        }

        // Multi-format Reports Output
        if (reportFormat != null) {
            item {
                val bundle = reportFormat!!
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // SOAP Format
                    ReportViewer(
                        title = "1. SOAP Note (Note clinique standardisée)",
                        content = bundle.soap,
                        context = context
                    )

                    // Standard hand-over (SBAR)
                    ReportViewer(
                        title = if (lang == AppLanguage.FR) "2. Rapport SBAR (Transmission de garde)" else "2. SBAR Handover Report",
                        content = bundle.sbar,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
fun ReportViewer(title: String, content: String, context: Context) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                IconButton(onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Clinical Report", content))
                }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                }
            }
            Divider()
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

data class ReportBundle(val soap: String, val sbar: String)

fun formatClinicalReports(raw: String, lang: AppLanguage): ReportBundle {
    val isFr = lang == AppLanguage.FR
    val soapText = if (isFr) {
        """
        S (Subjectif) : Patient conscient, exprime : "${raw.substringBefore(".")}"
        O (Objectif) : Constantes cliniques relevées. TA normale, pouls régulier, saturation stable.
        A (Analyse) : Épisode douloureux aigu post-opératoire géré par antalgie opiacée de palier III.
        P (Plan d'action) : Réévaluation clinique de la douleur à 30 minutes. Monitorage de la vigilance et de la fréquence respiratoire.
        """.trimIndent()
    } else {
        """
        S (Subjective) : Patient states: "${raw.substringBefore(".")}"
        O (Objective) : Vital signs checked and recorded. Stable hemodynamic parameters.
        A (Assessment) : Acute post-operative pain controlled with strong opioid analgesia.
        P (Plan) : Re-evaluate VAS score in 30 minutes. Strict monitoring of sedation and respiratory rate.
        """.trimIndent()
    }

    val sbarText = if (isFr) {
        """
        S (Situation) : Patient chambre 302B, se plaint d'une douleur aiguë évaluée à 8/10.
        B (Background) : Contexte post-opératoire récent.
        A (Assessment) : Injection de Morphine 2 mg IV faite à 10:00. Constantes stables.
        R (Recommendation) : Surveiller la fréquence respiratoire stricte et s'assurer du repos clinique du patient.
        """.trimIndent()
    } else {
        """
        S (Situation) : Patient in room 302B reporting severe breakthrough pain (VAS 8/10).
        B (Background) : Recent surgical/post-operative recovery.
        A (Assessment) : Administered IV Morphine 2 mg slow push at 10:00 AM. Hemodynamics stable.
        R (Recommendation) : Monitor respiratory status closely, evaluate sedation score, and reassess pain relief.
        """.trimIndent()
    }

    return ReportBundle(soapText, sbarText)
}


// ==========================================
// 4. SHIFT ASSISTANT SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftAssistantScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val patients by viewModel.patients.collectAsState()
    val tasks by viewModel.shiftTasks.collectAsState()

    var showAddPatientDialog by remember { mutableStateOf(false) }
    var newPatName by remember { mutableStateOf("") }
    var newPatRoom by remember { mutableStateOf("") }
    var newPatAge by remember { mutableStateOf("") }
    var newPatDiag by remember { mutableStateOf("") }

    var selectedPatientForVitals by remember { mutableStateOf<Patient?>(null) }
    var bpVal by remember { mutableStateOf("") }
    var hrVal by remember { mutableStateOf("") }
    var tempVal by remember { mutableStateOf("") }
    var spo2Val by remember { mutableStateOf("") }
    var respVal by remember { mutableStateOf("") }

    var selectedPatientForMed by remember { mutableStateOf<Patient?>(null) }
    var medName by remember { mutableStateOf("") }
    var medDose by remember { mutableStateOf("") }
    var medTime by remember { mutableStateOf("") }

    var selectedPatientForWound by remember { mutableStateOf<Patient?>(null) }
    var woundLoc by remember { mutableStateOf("") }
    var woundStage by remember { mutableStateOf("Stade II") }
    var woundSize by remember { mutableStateOf("") }
    var woundProgress by remember { mutableStateOf(50) }
    var woundNotes by remember { mutableStateOf("") }

    var generalTaskText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Title & Action Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lang == AppLanguage.FR) "Gestion de Garde" else if (lang == AppLanguage.AR) "مساعد الوردية والمهام" else "Smart Shift Assistant",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(onClick = { showAddPatientDialog = true }) {
                    Icon(Icons.Default.PersonAdd, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (lang == AppLanguage.FR) "Patient" else "+ Patient")
                }
            }
        }

        // Active Patients List
        item {
            Text(text = if (lang == AppLanguage.FR) "Patients Actifs" else "Active Patients", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        items(patients) { pat ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Header Room & Name
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = pat.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(text = "Chambre ${pat.room} • ${pat.age} ans", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = pat.diagnosis, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Bold)
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    // Vitals display
                    pat.vitals.lastOrNull()?.let { vit ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "TA: ${vit.bp}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = "FC: ${vit.pulse} bpm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = "T°: ${vit.temp} °C", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = "SpO2: ${vit.spo2}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (vit.spo2.toIntOrNull() ?: 100 < 94) Color.Red else Color.Unspecified)
                        }
                    } ?: Text(text = "Aucune constante enregistrée.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                    // Medications list
                    if (pat.medications.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = if (lang == AppLanguage.FR) "Médications du service :" else "Scheduled Medications:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            pat.medications.forEach { med ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = med.isGiven,
                                            onCheckedChange = { viewModel.toggleMedGiven(pat.id, med.id) }
                                        )
                                        Text(text = "${med.time} - ${med.name} (${med.dosage})", fontSize = 12.sp, style = if (med.isGiven) MaterialTheme.typography.bodyMedium.copy(color = Color.Gray) else MaterialTheme.typography.bodyMedium)
                                    }
                                    if (med.isGiven) {
                                        Icon(Icons.Default.Check, contentDescription = "Given", tint = Color.Green, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Interactive Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { selectedPatientForVitals = pat },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Vitals", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { selectedPatientForMed = pat },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.AddModerator, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Med", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { selectedPatientForWound = pat },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Healing, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pans.", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Shift Checklist Section
        item {
            Text(text = if (lang == AppLanguage.FR) "Liste des Tâches du Shift" else "Shift Tasks Checklist", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = generalTaskText,
                    onValueChange = { generalTaskText = it },
                    label = { Text(if (lang == AppLanguage.FR) "Nouvelle tâche générale" else "New General Task") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    viewModel.addShiftTask(generalTaskText)
                    generalTaskText = ""
                }, modifier = Modifier.height(56.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }

        items(tasks) { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleShiftTask(task.id) }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { viewModel.toggleShiftTask(task.id) }
                )
                Text(
                    text = task.label,
                    style = if (task.isCompleted) MaterialTheme.typography.bodyMedium.copy(color = Color.Gray) else MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

    // Modal Dialog: Add Patient
    if (showAddPatientDialog) {
        AlertDialog(
            onDismissRequest = { showAddPatientDialog = false },
            title = { Text(if (lang == AppLanguage.FR) "Ajouter un Patient" else "Add New Patient") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = newPatName, onValueChange = { newPatName = it }, label = { Text("Nom complet") })
                    OutlinedTextField(value = newPatRoom, onValueChange = { newPatRoom = it }, label = { Text("Chambre") })
                    OutlinedTextField(value = newPatAge, onValueChange = { newPatAge = it }, label = { Text("Âge") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = newPatDiag, onValueChange = { newPatDiag = it }, label = { Text("Diagnostic") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addPatient(newPatName, newPatRoom, newPatAge, newPatDiag)
                    showAddPatientDialog = false
                    newPatName = ""
                    newPatRoom = ""
                    newPatAge = ""
                    newPatDiag = ""
                }) {
                    Text("Valider")
                }
            }
        )
    }

    // Modal Dialog: Log Vitals
    if (selectedPatientForVitals != null) {
        val pat = selectedPatientForVitals!!
        AlertDialog(
            onDismissRequest = { selectedPatientForVitals = null },
            title = { Text("Prendre Constantes: ${pat.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = bpVal, onValueChange = { bpVal = it }, label = { Text("Tension (ex: 120/80)") })
                    OutlinedTextField(value = hrVal, onValueChange = { hrVal = it }, label = { Text("Fréquence Cardiaque (bpm)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = tempVal, onValueChange = { tempVal = it }, label = { Text("Température (°C)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                    OutlinedTextField(value = spo2Val, onValueChange = { spo2Val = it }, label = { Text("SpO2 (%)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = respVal, onValueChange = { respVal = it }, label = { Text("Fréquence respiratoire") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addPatientVitals(pat.id, bpVal, hrVal, tempVal, spo2Val, respVal)
                    selectedPatientForVitals = null
                    bpVal = ""
                    hrVal = ""
                    tempVal = ""
                    spo2Val = ""
                    respVal = ""
                }) {
                    Text("Enregistrer")
                }
            }
        )
    }

    // Modal Dialog: Add Medication
    if (selectedPatientForMed != null) {
        val pat = selectedPatientForMed!!
        AlertDialog(
            onDismissRequest = { selectedPatientForMed = null },
            title = { Text("Planifier un médicament: ${pat.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = medName, onValueChange = { medName = it }, label = { Text("Nom du médicament") })
                    OutlinedTextField(value = medDose, onValueChange = { medDose = it }, label = { Text("Dosage") })
                    OutlinedTextField(value = medTime, onValueChange = { medTime = it }, label = { Text("Heure d'administration (ex: 14:00)") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addPatientMed(pat.id, medName, medDose, medTime)
                    selectedPatientForMed = null
                    medName = ""
                    medDose = ""
                    medTime = ""
                }) {
                    Text("Planifier")
                }
            }
        )
    }

    // Modal Dialog: Assess Wound / Pansement
    if (selectedPatientForWound != null) {
        val pat = selectedPatientForWound!!
        AlertDialog(
            onDismissRequest = { selectedPatientForWound = null },
            title = { Text("Évaluation Pansement: ${pat.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = woundLoc, onValueChange = { woundLoc = it }, label = { Text("Localisation de la plaie (ex: Sacrum)") })
                    OutlinedTextField(value = woundSize, onValueChange = { woundSize = it }, label = { Text("Dimensions (ex: 3x4 cm)") })
                    OutlinedTextField(value = woundNotes, onValueChange = { woundNotes = it }, label = { Text("Observations du lit de la plaie") })
                    Text("Évolution vers la cicatrisation (0-100%): $woundProgress%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = woundProgress.toFloat(),
                        onValueChange = { woundProgress = it.toInt() },
                        valueRange = 0f..100f
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addPatientWound(pat.id, woundLoc, woundStage, woundSize, woundProgress, woundNotes, (0..2).random())
                    selectedPatientForWound = null
                    woundLoc = ""
                    woundSize = ""
                    woundNotes = ""
                    woundProgress = 50
                }) {
                    Text("Valider")
                }
            }
        )
    }
}


// ==========================================
// 5. EMERGENCY MODE SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(lang: AppLanguage) {
    val emergencyProtocols = remember {
        listOf(
            EmergencyProtocol(
                title = if (lang == AppLanguage.FR) "Arrêt Cardiorespiratoire" else "Cardiac Arrest",
                icon = Icons.Default.Favorite,
                steps = listOf(
                    "Reconnaître l'inconscience + absence de respiration : Appeler de l'aide et le chariot d'urgence.",
                    "Débuter immédiatement le massage cardiaque externe : 30 compressions pour 2 insufflations (fréquence 100-120/min).",
                    "Placer le défibrillateur (DAE / Manuel) dès que disponible. Analyser le rythme.",
                    "Si rythme choquable (FV/TV) : Délivrer le choc immédiatement puis reprendre le MCE.",
                    "Si rythme non choquable (Asystolie/AEP) : Poursuivre le MCE. Administrer Adrénaline 1mg IVD toutes les 3 à 5 minutes.",
                    "Gérer les voies aériennes et poser une intubation au besoin."
                ),
                doses = listOf("Adrénaline : 1 mg IVD direct toutes les 3-5 min (sans dilution).", "Amiodarone : 300 mg IVD après le 3ème choc.")
            ),
            EmergencyProtocol(
                title = if (lang == AppLanguage.FR) "Anaphylaxie Sévère" else "Anaphylaxis",
                icon = Icons.Default.Warning,
                steps = listOf(
                    "Arrêter immédiatement tout traitement suspect en cours (perfusion, etc.).",
                    "Injecter l'Adrénaline par voie Intramusculaire (IM) immédiatement au tiers moyen externe de la cuisse.",
                    "Libérer les voies aériennes et placer le patient en position allongée jambes surélevées (sauf si détresse respiratoire).",
                    "Administrer de l'Oxygène à haut débit (15 L/min au masque à haute concentration).",
                    "Poser 2 voies veineuses de gros calibre et débuter un remplissage rapide par NaCl 0.9%."
                ),
                doses = listOf("Adrénaline IM : 0.5 mg chez l'adulte (dilution 1/1000). Peut être répété après 5-15 min.", "Méthylprednisolone : 120 mg IVD.")
            ),
            EmergencyProtocol(
                title = if (lang == AppLanguage.FR) "Hypoglycémie Sévère (Coma)" else "Severe Hypoglycemia",
                icon = Icons.Default.Bolt,
                steps = listOf(
                    "Vérifier le dextro clinique en urgence.",
                    "Si patient inconscient : Poser une voie veineuse périphérique d'urgence.",
                    "Injecter du G30% (Glucose 30%) en intraveineux direct lent.",
                    "Si pas de voie veineuse disponible : Injecter du Glucagon 1mg en IM ou SC.",
                    "Re-contrôler la glycémie capillaire 15 minutes après l'injection."
                ),
                doses = listOf("Glucose G30% : 50 à 100 mL en IVD direct lent.", "Glucagon IM/SC : 1 mg (si pas d'accès IV).")
            )
        )
    }

    var selectedProtocol by remember { mutableStateOf<EmergencyProtocol?>(null) }
    var activeTimerSeconds by remember { mutableStateOf(0) }
    var timerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            while (timerRunning) {
                delay(1000)
                activeTimerSeconds++
            }
        }
    }

    if (selectedProtocol == null) {
        // Main emergency button grid
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (lang == AppLanguage.FR) "MODE URGENCE VITALE" else "CRITICAL EMERGENCY MODE",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFBA1A1A)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(emergencyProtocols) { proto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedProtocol = proto
                                activeTimerSeconds = 0
                                timerRunning = true
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F1)),
                        border = BorderStroke(2.dp, Color(0xFFBA1A1A))
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = proto.icon,
                                contentDescription = null,
                                tint = Color(0xFFBA1A1A),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = proto.title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFBA1A1A)
                            )
                        }
                    }
                }
            }
        }
    } else {
        // Active protocols step-by-step assistant
        val proto = selectedProtocol!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    selectedProtocol = null
                    timerRunning = false
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(text = proto.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFFBA1A1A))
            }

            // Running Intervention Stopwatch
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("INTERVENTION TIMER", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        val mins = activeTimerSeconds / 60
                        val secs = activeTimerSeconds % 60
                        Text(
                            text = String.format("%02d:%02d", mins, secs),
                            color = Color.Red,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                    IconButton(onClick = { timerRunning = !timerRunning }) {
                        Icon(
                            imageVector = if (timerRunning) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = "Control",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            // Step guide
            Text("NURSING ACTIONS PRIORITIES (OFFLINE AVAILABLE)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            proto.steps.forEachIndexed { idx, step ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFFBA1A1A).copy(alpha = 0.3f))
                ) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFFBA1A1A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${idx + 1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Text(text = step, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // High-risk Emergency Dosages Cheat sheet
            Text("EMERGENCY DOSAGES REFERENCE", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFBA1A1A))
            proto.doses.forEach { dose ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F1))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Medication, contentDescription = null, tint = Color(0xFFBA1A1A))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = dose, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF8C0009))
                    }
                }
            }
        }
    }
}

data class EmergencyProtocol(
    val title: String,
    val icon: ImageVector,
    val steps: List<String>,
    val doses: List<String>
)


// ==========================================
// 6. NURSING SIMULATOR SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatorScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val simScenarios = remember {
        listOf(
            SimScenario(
                title = if (lang == AppLanguage.FR) "Arrêt Cardiorespiratoire (Réanimation)" else "Cardiac Arrest Simulation",
                intro = if (lang == AppLanguage.FR) {
                    "Vous entrez dans la chambre d'un patient de 65 ans et constatez qu'il ne répond pas et ne respire plus."
                } else {
                    "You enter the room of a 65-year-old patient and find him unresponsive and not breathing."
                },
                steps = listOf(
                    SimStep(
                        question = if (lang == AppLanguage.FR) "Quelle est votre première action immédiate ?" else "What is your immediate first action?",
                        options = listOf(
                            SimOption("Lancer l'alarme d'arrêt cardiaque et appeler à l'aide", true, 50, "Correct. La sécurité clinique impose d'obtenir du soutien immédiatement."),
                            SimOption("Vérifier le dossier clinique pour voir les antécédents", false, 0, "Incorrect. Perte de temps critique face à un arrêt circulatoire potentiel.")
                        )
                    ),
                    SimStep(
                        question = if (lang == AppLanguage.FR) "L'aide arrive. Que débutez-vous immédiatement ?" else "Help is on the way. What do you start immediately?",
                        options = listOf(
                            SimOption("Massage cardiaque externe (30 compressions pour 2 insufflations)", true, 50, "Correct. Les compressions cardiaques sauvent le cerveau et le myocarde."),
                            SimOption("Injecter de l'Atropine intraveineuse", false, 0, "Incorrect. Atropine n'est pas indiquée dans le protocole standard d'ACR initial.")
                        )
                    )
                )
            ),
            SimScenario(
                title = if (lang == AppLanguage.FR) "Choc Septique" else "Septic Shock Simulation",
                intro = "Patient avec fièvre élevée, somnolence, et tension artérielle effondrée à 85/40 mmHg.",
                steps = listOf(
                    SimStep(
                        question = if (lang == AppLanguage.FR) "Quelle priorité d'accès vasculaire devez-vous sécuriser ?" else "Which vascular access priority is critical?",
                        options = listOf(
                            SimOption("Poser rapidement 2 voies veineuses périphériques de gros calibre (14-16G)", true, 50, "Correct. Essentiel pour perfuser rapidement de grands volumes de cristalloïdes."),
                            SimOption("Demander la pose d'une voie veineuse centrale d'urgence", false, 0, "Incorrect. La pose d'une VVC prend trop de temps au stade initial d'urgence.")
                        )
                    )
                )
            )
        )
    }

    var selectedScenarioIdx by remember { mutableStateOf<Int?>(null) }
    var currentStepIdx by remember { mutableStateOf(0) }
    var currentScore by remember { mutableStateOf(0) }
    var stepFeedback by remember { mutableStateOf<String?>(null) }
    var scenarioFinished by remember { mutableStateOf(false) }

    if (selectedScenarioIdx == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (lang == AppLanguage.FR) "Simulateur Clinique" else "Clinical Nursing Simulator",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            simScenarios.forEachIndexed { idx, sim ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedScenarioIdx = idx
                            currentStepIdx = 0
                            currentScore = 0
                            stepFeedback = null
                            scenarioFinished = false
                        },
                    border = CardDefaults.outlinedCardBorder(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = sim.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                        Text(text = sim.intro, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 2)
                    }
                }
            }
        }
    } else {
        val scenario = simScenarios[selectedScenarioIdx!!]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { selectedScenarioIdx = null }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(text = scenario.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }

            if (!scenarioFinished) {
                val step = scenario.steps[currentStepIdx]
                Text(
                    text = "Question ${currentStepIdx + 1}/${scenario.steps.size}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(text = step.question, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)

                step.options.forEach { opt ->
                    Button(
                        onClick = {
                            if (opt.isCorrect) {
                                currentScore += opt.xpReward
                            }
                            stepFeedback = opt.rationale
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = opt.text, textAlign = TextAlign.Center)
                    }
                }

                if (stepFeedback != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("RATIONALE CLINIQUE :", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                            Text(text = stepFeedback!!, style = MaterialTheme.typography.bodyMedium)
                            Button(onClick = {
                                stepFeedback = null
                                if (currentStepIdx + 1 < scenario.steps.size) {
                                    currentStepIdx++
                                } else {
                                    scenarioFinished = true
                                    viewModel.addXp(currentScore) // Sync to VM
                                }
                            }) {
                                Text("Suivant")
                            }
                        }
                    }
                }
            } else {
                // Success / End Simulation Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = BorderStroke(1.5.dp, Color(0xFF2E7D32))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "Success", tint = Color(0xFFD4AF37), modifier = Modifier.size(64.dp))
                        Text("SIMULATION TERMINÉE !", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = Color(0xFF1B5E20))
                        Text("Vous avez obtenu $currentScore XP !", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = Color(0xFF2E7D32))
                        Button(onClick = { selectedScenarioIdx = null }) {
                            Text("Retourner à la liste")
                        }
                    }
                }
            }
        }
    }
}

data class SimScenario(val title: String, val intro: String, val steps: List<SimStep>)
data class SimStep(val question: String, val options: List<SimOption>)
data class SimOption(val text: String, val isCorrect: Boolean, val xpReward: Int, val rationale: String)


// ==========================================
// 7. WOUND PROGRESS TRACKER SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoundTrackerScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val patients by viewModel.patients.collectAsState()
    var selectedPatientIdx by remember { mutableStateOf(0) }

    val activePatient = if (patients.isNotEmpty()) patients[selectedPatientIdx] else null

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = if (lang == AppLanguage.FR) "Suivi des Pansements" else "Wound Progress Tracker",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (activePatient != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Patient en cours :", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        patients.forEachIndexed { idx, pat ->
                            FilterChip(
                                selected = selectedPatientIdx == idx,
                                onClick = { selectedPatientIdx = idx },
                                label = { Text(pat.name) }
                            )
                        }
                    }
                }
            }

            // Wound assessments Timeline list
            if (activePatient.wounds.isEmpty()) {
                item {
                    Text("Aucune évaluation de plaie disponible pour ce patient. Créez-en une dans le module 'Gestion de Garde'.")
                }
            } else {
                items(activePatient.wounds) { wound ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = CardDefaults.outlinedCardBorder(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Plaie : ${wound.location} (${wound.stage})",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(wound.timestamp)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            Divider()

                            // Healing progress bar with colored label
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Cicatrisation :", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("${wound.progress}%", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                            }
                            LinearProgressIndicator(
                                progress = wound.progress / 100f,
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF2E7D32)
                            )

                            // Render mock wound graphic representation using basic canvas drawing
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .background(Color(0xFFFFF3F3), RoundedCornerShape(12.dp))
                                    .border(BorderStroke(1.dp, Color(0xFFE57373)), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    // Let's render a procedural wound diagram showing progression
                                    val healFactor = wound.progress / 100f
                                    val sizeX = 140f * (1f - (healFactor * 0.5f))
                                    val sizeY = 80f * (1f - (healFactor * 0.5f))

                                    // Outer inflamed tissue
                                    drawOval(
                                        color = Color(0xFFFFCDD2).copy(alpha = 1f - healFactor),
                                        topLeft = Offset(center.x - sizeX, center.y - sizeY),
                                        size = androidx.compose.ui.geometry.Size(sizeX * 2, sizeY * 2)
                                    )

                                    // Fibrin / Necrotic center
                                    drawOval(
                                        color = if (healFactor > 0.8f) Color(0xFF81C784) else Color(0xFFEF5350),
                                        topLeft = Offset(center.x - sizeX * 0.6f, center.y - sizeY * 0.6f),
                                        size = androidx.compose.ui.geometry.Size(sizeX * 1.2f, sizeY * 1.2f)
                                    )
                                }
                                Text("Schéma cicatriciel procédural", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp))
                            }

                            Text(text = "Dimensions : ${wound.dimensions}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Observations : ${wound.notes}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}


// ==========================================
// 8. MEDICATION SCANNER SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedScannerScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val simulatedDatabase = remember {
        listOf(
            ScannedDrug("5412345678901", "Morphine 10mg/ml", "Seringue pré-remplie", "Antalgique Palier 3", "Injecter IV très lentement. Surveiller conscience et pupilles."),
            ScannedDrug("3400930000012", "Ceftriaxone 1g", "Flacon Poudre", "Antibiotique Céphalosporine", "Vérifier l'absence d'allergies aux Bêta-lactamines. Ne jamais mélanger avec du Ringer Lactate.")
        )
    }

    var manualBarcode by remember { mutableStateOf("") }
    var activeDrugResult by remember { mutableStateOf<ScannedDrug?>(null) }
    var isScanning by remember { mutableStateOf(false) }

    LaunchedEffect(isScanning) {
        if (isScanning) {
            delay(1500) // Scan delay simulation
            activeDrugResult = simulatedDatabase.random()
            isScanning = false
            viewModel.addXp(20)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (lang == AppLanguage.FR) "Scanner de Médicaments" else "Medication Scanner",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Viewfinder Scanner Card animation
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (isScanning) {
                    CircularProgressIndicator(color = Color.Red)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White, modifier = Modifier.size(72.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { isScanning = true }) {
                            Text("Simuler le Scan")
                        }
                    }
                }
            }
        }

        // Manual bar code text entry fallback
        OutlinedTextField(
            value = manualBarcode,
            onValueChange = { manualBarcode = it },
            label = { Text("Entrez le code-barres manuellement") },
            placeholder = { Text("Ex: 5412345678901") },
            trailingIcon = {
                IconButton(onClick = {
                    val drug = simulatedDatabase.find { it.barcode == manualBarcode }
                    if (drug != null) {
                        activeDrugResult = drug
                    }
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Result drug information sheet
        if (activeDrugResult != null) {
            val d = activeDrugResult!!
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = d.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Text(text = "Catégorie : ${d.category}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Text(text = "Forme : ${d.form}", style = MaterialTheme.typography.bodySmall)
                    Divider()
                    Text(text = "Remarques cliniques de surveillance :", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(text = d.notes, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

data class ScannedDrug(val barcode: String, val name: String, val form: String, val category: String, val notes: String)


// ==========================================
// 9. GAMIFICATION SYSTEM SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamificationScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val xp by viewModel.xp.collectAsState()
    val level by viewModel.level.collectAsState()
    val badges by viewModel.badges.collectAsState()
    val streak by viewModel.streak.collectAsState()

    val challenges = listOf(
        Pair("Garde de Nuit validée (+10 XP)", true),
        Pair("Analyser un ECG de Torsade de Pointes (+15 XP)", false),
        Pair("Faire un calcul de dose parfait (+20 XP)", true)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Level banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "NIVEAU CLINIQUE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Niveau $level", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = "Streak", tint = Color(0xFFFF6F00), modifier = Modifier.size(32.dp))
                            Text(text = "$streak Jours", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleLarge)
                        }
                    }

                    // Progress to next level
                    val progressRatio = (xp % 200) / 200f
                    LinearProgressIndicator(
                        progress = progressRatio,
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "${xp % 200} / 200 XP pour le prochain niveau", fontSize = 11.sp)
                }
            }
        }

        // Achievements Badges grid list
        item {
            Text(text = "Badges & Succès Débloqués", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                badges.forEach { b ->
                    Card(
                        modifier = Modifier.width(130.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFFFD54F), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFF57F17))
                            }
                            Text(text = b, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }

        // Daily Clinical Challenges Checklist
        item {
            Text(text = "Défis d'Apprentissage Journaliers", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        items(challenges) { ch ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = ch.first, style = MaterialTheme.typography.bodyMedium)
                    Checkbox(checked = ch.second, onCheckedChange = {})
                }
            }
        }
    }
}
