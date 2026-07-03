package com.example.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.Screen
import kotlin.math.roundToInt

// --- Dose Calculator Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseCalcScreen(viewModel: MainViewModel, lang: AppLanguage) {
    var weightInput by remember { mutableStateOf("") }
    var ageInput by remember { mutableStateOf("") }
    var prescribedInput by remember { mutableStateOf("") }
    var availableMgInput by remember { mutableStateOf("") }
    var availableMlInput by remember { mutableStateOf("") }

    val weight = weightInput.toDoubleOrNull() ?: 0.0
    val prescribed = prescribedInput.toDoubleOrNull() ?: 0.0
    val avMg = availableMgInput.toDoubleOrNull() ?: 0.0
    val avMl = availableMlInput.toDoubleOrNull() ?: 1.0

    val result = MedicalCalculators.calculateDose(weight, prescribed, avMg, avMl)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Title
        Text(
            text = Translation.get("card_doses", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Inputs Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text(Translation.get("dose_poids", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = ageInput,
                    onValueChange = { ageInput = it },
                    label = { Text(Translation.get("dose_age", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = prescribedInput,
                    onValueChange = { prescribedInput = it },
                    label = { Text(Translation.get("dose_prescrite", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = availableMgInput,
                        onValueChange = { availableMgInput = it },
                        label = { Text(Translation.get("dose_concent_mg", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = availableMlInput,
                        onValueChange = { availableMlInput = it },
                        label = { Text(Translation.get("dose_concent_ml", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        }

        // Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = Translation.get("dose_result_title", lang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(Translation.get("dose_total", lang), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("${result.totalDoseMg} mg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(Translation.get("dose_volume", lang), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("${result.volumeToAdministerMl} ml", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Example Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    Translation.get("dose_example_title", lang),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    Translation.get("dose_example_desc", lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// --- Perfusion Calculator Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfusionCalcScreen(viewModel: MainViewModel, lang: AppLanguage) {
    var volumeInput by remember { mutableStateOf("") }
    var durationInput by remember { mutableStateOf("") }
    var durationUnitIsMinutes by remember { mutableStateOf(false) } // Default Hours (false)
    var selectedFactor by remember { mutableStateOf(20.0) }
    var customFactorInput by remember { mutableStateOf("") }

    val volume = volumeInput.toDoubleOrNull() ?: 0.0
    val durationRaw = durationInput.toDoubleOrNull() ?: 0.0
    val durationMinutes = if (durationUnitIsMinutes) durationRaw else durationRaw * 60.0
    
    val currentFactor = if (selectedFactor == -1.0) {
        customFactorInput.toDoubleOrNull() ?: 20.0
    } else {
        selectedFactor
    }

    val result = MedicalCalculators.calculatePerfusion(volume, durationMinutes, currentFactor)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Translation.get("card_perfusion", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = volumeInput,
                    onValueChange = { volumeInput = it },
                    label = { Text(Translation.get("perf_volume", lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = durationInput,
                        onValueChange = { durationInput = it },
                        label = { Text(Translation.get("perf_duree", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1.2f),
                        singleLine = true
                    )

                    // Hours / Minutes Toggle Button
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                            .clickable { durationUnitIsMinutes = !durationUnitIsMinutes },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (durationUnitIsMinutes) Translation.get("perf_minutes", lang) else Translation.get("perf_hours", lang),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Toggle Unit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                // Drop factor choices
                Text(Translation.get("perf_factor", lang), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { selectedFactor = 20.0 }
                    ) {
                        RadioButton(selected = (selectedFactor == 20.0), onClick = { selectedFactor = 20.0 })
                        Text(Translation.get("perf_factor_standard", lang), style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { selectedFactor = 60.0 }
                    ) {
                        RadioButton(selected = (selectedFactor == 60.0), onClick = { selectedFactor = 60.0 })
                        Text(Translation.get("perf_factor_micro", lang), style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { selectedFactor = 15.0 }
                    ) {
                        RadioButton(selected = (selectedFactor == 15.0), onClick = { selectedFactor = 15.0 })
                        Text(Translation.get("perf_factor_sang", lang), style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { selectedFactor = -1.0 }
                    ) {
                        RadioButton(selected = (selectedFactor == -1.0), onClick = { selectedFactor = -1.0 })
                        Text(if (lang == AppLanguage.AR) "عامل مخصص" else "Custom factor", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                if (selectedFactor == -1.0) {
                    OutlinedTextField(
                        value = customFactorInput,
                        onValueChange = { customFactorInput = it },
                        label = { Text("gtt/ml") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }

        // Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = Translation.get("dose_result_title", lang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(Translation.get("perf_result_flow", lang), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("${result.mlPerHour} ml/h", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(Translation.get("perf_result_drops", lang), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("${result.dropsPerMinute} gtts/min", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

// --- ECG Quick Guide Screen ---
@Composable
fun EcgGuideScreen(lang: AppLanguage) {
    var selectedEcgId by remember { mutableStateOf("brady") }
    val ecgItem = MedicalData.ecgList.find { it.id == selectedEcgId } ?: MedicalData.ecgList.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Translation.get("card_ecg", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Horizontal scroll selectors for rhythms
        ScrollableTabRow(
            selectedTabIndex = MedicalData.ecgList.indexOfFirst { it.id == selectedEcgId },
            edgePadding = 0.dp
        ) {
            MedicalData.ecgList.forEach { item ->
                Tab(
                    selected = (selectedEcgId == item.id),
                    onClick = { selectedEcgId = item.id },
                    text = { Text(item.title[lang] ?: "", maxLines = 1, fontSize = 12.sp) }
                )
            }
        }

        // Active ECG view
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Waveform drawer
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column {
                        EcgWaveform(rhythmId = selectedEcgId)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = ecgItem.title[lang] ?: "",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            item {
                // Description Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = "Desc", tint = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(Translation.get("ecg_desc", lang), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        }
                        Text(ecgItem.desc[lang] ?: "", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            item {
                // Signs Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, contentDescription = "Signs", tint = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(Translation.get("ecg_signs", lang), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        }
                        Text(ecgItem.signs[lang] ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            item {
                // Nursing Action Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Healing, contentDescription = "Nursing", tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(Translation.get("ecg_nursing", lang), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        }
                        Text(ecgItem.nursingCare[lang] ?: "", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

// --- Medical Scores Screen ---
@Composable
fun ScoresScreen(lang: AppLanguage) {
    var activeScoreTab by remember { mutableStateOf("GCS") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Translation.get("card_scores", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Select score type
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val tabs = listOf("GCS", "APGAR", "BMI", "NEWS", "PAIN")
            tabs.forEach { tab ->
                Button(
                    onClick = { activeScoreTab = tab },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeScoreTab == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.weight(1f).height(40.dp),
                    contentPadding = PaddingValues(2.dp)
                ) {
                    Text(tab, fontSize = 10.sp, color = if (activeScoreTab == tab) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Display active score view
        when (activeScoreTab) {
            "GCS" -> GcsScoreView(lang)
            "APGAR" -> ApgarScoreView(lang)
            "BMI" -> BmiScoreView(lang)
            "NEWS" -> NewsScoreView(lang)
            "PAIN" -> PainScoreView(lang)
        }
    }
}

@Composable
fun GcsScoreView(lang: AppLanguage) {
    var eyes by remember { mutableStateOf(4) }
    var verbal by remember { mutableStateOf(5) }
    var motor by remember { mutableStateOf(6) }

    val result = MedicalCalculators.calculateGcs(eyes, verbal, motor)

    Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Glasgow Coma Scale", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

        Text(if (lang == AppLanguage.AR) "فتح العينين" else "Ouverture des yeux (E)", fontWeight = FontWeight.Medium)
        Column {
            val opts = listOf(
                4 to if (lang == AppLanguage.AR) "تلقائي (4)" else "Spontanée (4)",
                3 to if (lang == AppLanguage.AR) "عند طلب لفظي (3)" else "À la demande verbale (3)",
                2 to if (lang == AppLanguage.AR) "للألم (2)" else "À la douleur (2)",
                1 to if (lang == AppLanguage.AR) "لا يوجد (1)" else "Nulle (1)"
            )
            opts.forEach { (v, l) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { eyes = v }) {
                    RadioButton(selected = (eyes == v), onClick = { eyes = v })
                    Text(l, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Text(if (lang == AppLanguage.AR) "الاستجابة اللفظية" else "Réponse verbale (V)", fontWeight = FontWeight.Medium)
        Column {
            val opts = listOf(
                5 to if (lang == AppLanguage.AR) "متوجه ومنظم (5)" else "Orientée (5)",
                4 to if (lang == AppLanguage.AR) "مشوش (4)" else "Confuse (4)",
                3 to if (lang == AppLanguage.AR) "كلمات غير مناسبة (3)" else "Inappropriée (3)",
                2 to if (lang == AppLanguage.AR) "أصوات غير مفهومة (2)" else "Incompréhensible (2)",
                1 to if (lang == AppLanguage.AR) "لا يوجد (1)" else "Nulle (1)"
            )
            opts.forEach { (v, l) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { verbal = v }) {
                    RadioButton(selected = (verbal == v), onClick = { verbal = v })
                    Text(l, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Text(if (lang == AppLanguage.AR) "الاستجابة الحركية" else "Réponse motrice (M)", fontWeight = FontWeight.Medium)
        Column {
            val opts = listOf(
                6 to if (lang == AppLanguage.AR) "يطيع الأوامر (6)" else "Obéit aux ordres (6)",
                5 to if (lang == AppLanguage.AR) "يحدد موضع الألم (5)" else "Localise la douleur (5)",
                4 to if (lang == AppLanguage.AR) "انسحاب للألم (4)" else "Évitement à la douleur (4)",
                3 to if (lang == AppLanguage.AR) "انثناء شاذ (قشرة المخ) (3)" else "Flexion anormale (décortication) (3)",
                2 to if (lang == AppLanguage.AR) "امتداد شاذ (دماغ متوسط) (2)" else "Extension anormale (décérébration) (2)",
                1 to if (lang == AppLanguage.AR) "لا يوجد (1)" else "Nulle (1)"
            )
            opts.forEach { (v, l) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { motor = v }) {
                    RadioButton(selected = (motor == v), onClick = { motor = v })
                    Text(l, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        ScoreOutcomeCard(result.score, 15, result.interpretation[lang] ?: "", lang)
    }
}

@Composable
fun ApgarScoreView(lang: AppLanguage) {
    var app by remember { mutableStateOf(2) }
    var pulse by remember { mutableStateOf(2) }
    var grim by remember { mutableStateOf(2) }
    var act by remember { mutableStateOf(2) }
    var resp by remember { mutableStateOf(2) }

    val result = MedicalCalculators.calculateApgar(app, pulse, grim, act, resp)

    Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("APGAR Score (Nouveau-né / Newborn)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

        ApgarRow("Appearance (Teint / Color)", app, listOf(
            0 to (if (lang == AppLanguage.AR) "أزرق / شاحب (0)" else "Bleu ou pâle (0)"),
            1 to (if (lang == AppLanguage.AR) "جسم وردي وأطراف زرقاء (1)" else "Corps rose, extrémités bleues (1)"),
            2 to (if (lang == AppLanguage.AR) "وردي بالكامل (2)" else "Entièrement rose (2)")
        )) { app = it }

        ApgarRow("Pulse (Pouls / Heart Rate)", pulse, listOf(
            0 to (if (lang == AppLanguage.AR) "لا يوجد نبض (0)" else "Absent (0)"),
            1 to (if (lang == AppLanguage.AR) "أقل من 100 ن/د (1)" else "< 100 bpm (1)"),
            2 to (if (lang == AppLanguage.AR) "أكثر من 100 ن/د (2)" else ">= 100 bpm (2)")
        )) { pulse = it }

        ApgarRow("Grimace (Réactivité / Reflex)", grim, listOf(
            0 to (if (lang == AppLanguage.AR) "لا توجد استجابة (0)" else "Nulle (0)"),
            1 to (if (lang == AppLanguage.AR) "تكشيرة خفيفة (1)" else "Grimace / Légère flexion (1)"),
            2 to (if (lang == AppLanguage.AR) "سعال، عطاس، بكاء (2)" else "Cri, toux ou éternuement (2)")
        )) { grim = it }

        ApgarRow("Activity (Tonus / Muscle Tone)", act, listOf(
            0 to (if (lang == AppLanguage.AR) "مرتخي بالكامل (0)" else "Flasque (0)"),
            1 to (if (lang == AppLanguage.AR) "انثناء طفيف للأطراف (1)" else "Légère flexion des membres (1)"),
            2 to (if (lang == AppLanguage.AR) "حركة نشطة (2)" else "Mouvements actifs (2)")
        )) { act = it }

        ApgarRow("Respiration", resp, listOf(
            0 to (if (lang == AppLanguage.AR) "لا يوجد تنفس (0)" else "Absent (0)"),
            1 to (if (lang == AppLanguage.AR) "تنفس ضعيف أو بكاء خفيف (1)" else "Lent, irrégulier (1)"),
            2 to (if (lang == AppLanguage.AR) "بكاء قوي وتنفس طبيعي (2)" else "Cri vigoureux (2)")
        )) { resp = it }

        ScoreOutcomeCard(result.score, 10, result.interpretation[lang] ?: "", lang)
    }
}

@Composable
fun ApgarRow(label: String, selectedValue: Int, options: List<Pair<Int, String>>, onSelected: (Int) -> Unit) {
    Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        options.forEach { (value, txt) ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelected(value) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedValue == value) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Box(modifier = Modifier.padding(8.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(txt, fontSize = 11.sp, textAlign = TextAlign.Center, fontWeight = if (selectedValue == value) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }
    }
}

@Composable
fun BmiScoreView(lang: AppLanguage) {
    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }

    val weight = weightInput.toDoubleOrNull() ?: 0.0
    val height = heightInput.toDoubleOrNull() ?: 0.0

    val result = MedicalCalculators.calculateBmi(weight, height)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Indice de Masse Corporelle (IMC / BMI)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            label = { Text(if (lang == AppLanguage.AR) "الوزن (كغ)" else "Poids (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = heightInput,
            onValueChange = { heightInput = it },
            label = { Text(if (lang == AppLanguage.AR) "الطول (سم)" else "Taille (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        if (result.score > 0) {
            ScoreOutcomeCard(result.score.toInt(), 40, result.interpretation[lang] ?: "", lang, showFraction = false, rawScore = result.score.toString())
        }
    }
}

@Composable
fun NewsScoreView(lang: AppLanguage) {
    var respScore by remember { mutableStateOf(0) }
    var spo2Score by remember { mutableStateOf(0) }
    var tempScore by remember { mutableStateOf(0) }
    var sbpScore by remember { mutableStateOf(0) }
    var hrScore by remember { mutableStateOf(0) }
    var alertScore by remember { mutableStateOf(0) }
    var onOxygen by remember { mutableStateOf(false) }

    val result = MedicalCalculators.calculateNews(respScore, spo2Score, onOxygen, tempScore, sbpScore, hrScore, alertScore)

    Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("NEWS Score (National Early Warning Score)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

        // Respiration Rate
        NewsFieldRow("Fréquence respiratoire / Breath Rate (cycles/min)", respScore, listOf(
            3 to "< 8",
            1 to "9-11",
            0 to "12-20",
            1 to "21-24",
            3 to ">= 25"
        )) { respScore = it }

        // SpO2
        NewsFieldRow("SpO2 (%)", spo2Score, listOf(
            3 to "< 91",
            2 to "92-93",
            1 to "94-95",
            0 to ">= 96"
        )) { spo2Score = it }

        // Oxygen supplementation
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onOxygen = !onOxygen }.padding(vertical = 4.dp)) {
            Checkbox(checked = onOxygen, onCheckedChange = { onOxygen = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (lang == AppLanguage.AR) "يتلقى أكسجين إضافي (+2)" else "Sous oxygénothérapie (+2)", fontWeight = FontWeight.Medium)
        }

        // Temperature
        NewsFieldRow("Température (°C)", tempScore, listOf(
            3 to "< 35.0",
            1 to "35.1-36.0",
            0 to "36.1-38.0",
            1 to "38.1-39.0",
            2 to ">= 39.1"
        )) { tempScore = it }

        // Systolic BP
        NewsFieldRow("Tension artérielle systolique (mmHg)", sbpScore, listOf(
            3 to "< 90",
            2 to "91-100",
            1 to "101-110",
            0 to "111-219",
            3 to ">= 220"
        )) { sbpScore = it }

        // Heart Rate
        NewsFieldRow("Fréquence cardiaque / Pulse (bpm)", hrScore, listOf(
            3 to "< 40",
            1 to "41-50",
            0 to "51-90",
            1 to "91-110",
            2 to "111-130",
            3 to ">= 131"
        )) { hrScore = it }

        // Alertness
        NewsFieldRow("État de conscience / Alertness", alertScore, listOf(
            0 to (if (lang == AppLanguage.AR) "واعي (A)" else "A (Alerte)"),
            3 to (if (lang == AppLanguage.AR) "استجابة صوت/ألم/غائب (V/P/U)" else "V / P / U")
        )) { alertScore = it }

        ScoreOutcomeCard(result.score, 20, "${result.riskLevel[lang]}\n${result.action[lang]}", lang, showFraction = false)
    }
}

@Composable
fun NewsFieldRow(label: String, selectedScore: Int, choices: List<Pair<Int, String>>, onSelected: (Int) -> Unit) {
    Text(label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        choices.forEach { (score, text) ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelected(score) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedScore == score) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Box(modifier = Modifier.padding(6.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text, fontSize = 10.sp, fontWeight = if (selectedScore == score) FontWeight.Bold else FontWeight.Normal, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun PainScoreView(lang: AppLanguage) {
    var painValue by remember { mutableStateOf(0f) }

    val painTexts = mapOf(
        0 to mapOf(AppLanguage.FR to "Pas de douleur", AppLanguage.AR to "لا يوجد ألم", AppLanguage.EN to "No pain"),
        2 to mapOf(AppLanguage.FR to "Douleur légère", AppLanguage.AR to "ألم خفيف", AppLanguage.EN to "Mild pain"),
        4 to mapOf(AppLanguage.FR to "Douleur modérée", AppLanguage.AR to "ألم متوسط", AppLanguage.EN to "Moderate pain"),
        6 to mapOf(AppLanguage.FR to "Douleur sévère", AppLanguage.AR to "ألم شديد", AppLanguage.EN to "Severe pain"),
        8 to mapOf(AppLanguage.FR to "Douleur très sévère", AppLanguage.AR to "ألم شديد جداً", AppLanguage.EN to "Very severe"),
        10 to mapOf(AppLanguage.FR to "Douleur maximale", AppLanguage.AR to "أقصى ألم لا يطاق", AppLanguage.EN to "Worst imaginable pain")
    )

    val smileys = listOf("😊", "🙂", "😐", "🙁", "😢", "😭")

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(if (lang == AppLanguage.AR) "مقياس الألم البصري (Pain Scale)" else "Échelle de la Douleur", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Large smiley
                val smileyIndex = (painValue / 2f).toInt().coerceIn(0, 5)
                Text(smileys[smileyIndex], fontSize = 64.sp)

                Text(
                    text = "${painValue.toInt()} / 10",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (painValue > 6) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )

                val activeVal = (painValue.toInt() / 2) * 2
                Text(
                    text = painTexts[activeVal]?.get(lang) ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (painValue > 6) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                )

                Slider(
                    value = painValue,
                    onValueChange = { painValue = it.roundToInt().toFloat() },
                    valueRange = 0f..10f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ScoreOutcomeCard(score: Int, max: Int, description: String, lang: AppLanguage, showFraction: Boolean = true, rawScore: String = "") {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(Translation.get("score_result", lang), style = MaterialTheme.typography.titleSmall)
            
            Text(
                text = if (rawScore.isNotEmpty()) rawScore else if (showFraction) "$score / $max" else "$score",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(Translation.get("score_interpret", lang), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            Text(
                description,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// --- Emergency Drugs Screen ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrugsScreen(lang: AppLanguage) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedDrugId by remember { mutableStateOf<String?>(null) }

    val filteredDrugs = MedicalData.drugsList.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        (it.clazz[lang]?.contains(searchQuery, ignoreCase = true) == true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Translation.get("card_drugs", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(Translation.get("search", lang)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredDrugs) { drug ->
                val isExpanded = expandedDrugId == drug.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedDrugId = if (isExpanded) null else drug.id },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                    ),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(drug.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text(drug.clazz[lang] ?: "", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                            }
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expand"
                            )
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))

                            DrugDetailRow(Translation.get("drug_indications", lang), drug.indications[lang] ?: "")
                            DrugDetailRow(Translation.get("drug_poso_adult", lang), drug.posoAdult[lang] ?: "")
                            DrugDetailRow(Translation.get("drug_poso_pedia", lang), drug.posoPedia[lang] ?: "")
                            DrugDetailRow(Translation.get("drug_side_effects", lang), drug.sideEffects[lang] ?: "")
                            DrugDetailRow(Translation.get("drug_contra", lang), drug.contra[lang] ?: "")
                            DrugDetailRow(Translation.get("drug_admin_mode", lang), drug.adminMode[lang] ?: "")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrugDetailRow(label: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        Text(content, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}

// --- Nursing Protocols Screen ---
@Composable
fun ProtocolsScreen(lang: AppLanguage) {
    var selectedProtocolId by remember { mutableStateOf<String?>(null) }

    if (selectedProtocolId == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = Translation.get("card_protocols", lang),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(MedicalData.protocolsList) { protocol ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedProtocolId = protocol.id },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val vectorIcon = when (protocol.icon) {
                                "healing" -> Icons.Default.Healing
                                "colorize" -> Icons.Default.Colorize
                                "add" -> Icons.Default.AddCircle
                                "bloodtype" -> Icons.Default.WaterDrop
                                "star" -> Icons.Default.Star
                                "create" -> Icons.Default.Build
                                "refresh" -> Icons.Default.Refresh
                                else -> Icons.Default.Favorite
                            }
                            Icon(vectorIcon, contentDescription = "Icon", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                            
                            Text(
                                text = protocol.title[lang] ?: "",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.Default.ArrowForward, contentDescription = "Open")
                        }
                    }
                }
            }
        }
    } else {
        val protocol = MedicalData.protocolsList.find { it.id == selectedProtocolId }!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedProtocolId = null }
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                Spacer(modifier = Modifier.width(8.dp))
                Text(Translation.get("back", lang), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                protocol.title[lang] ?: "",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val steps = protocol.steps[lang] ?: emptyList()
                items(steps.mapIndexed { idx, step -> idx + 1 to step }) { (index, step) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$index", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Text(step, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// --- Medical Converter Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(lang: AppLanguage) {
    var activeMode by remember { mutableStateOf("kg_to_lbs") }
    var inputVal by remember { mutableStateOf("") }

    val rawNum = inputVal.toDoubleOrNull() ?: 0.0
    val result = MedicalCalculators.convert(rawNum, activeMode)

    val labelLeft = when (activeMode) {
        "kg_to_lbs" -> "kg"
        "lbs_to_kg" -> "lbs"
        "ml_to_l" -> "ml"
        "l_to_ml" -> "L"
        "c_to_f" -> "°C"
        "f_to_c" -> "°F"
        "mmol_to_mg" -> "mmol/L (Glucose)"
        "mg_to_mmol" -> "mg/dL (Glucose)"
        else -> ""
    }

    val labelRight = when (activeMode) {
        "kg_to_lbs" -> "lbs"
        "lbs_to_kg" -> "kg"
        "ml_to_l" -> "L"
        "l_to_ml" -> "ml"
        "c_to_f" -> "°F"
        "f_to_c" -> "°C"
        "mmol_to_mg" -> "mg/dL"
        "mg_to_mmol" -> "mmol/L"
        else -> ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Translation.get("card_converter", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Dropdown or selector choices
        val modes = listOf(
            "kg_to_lbs" to "kg ↔ lbs",
            "lbs_to_kg" to "lbs ↔ kg",
            "ml_to_l" to "ml ↔ L",
            "l_to_ml" to "L ↔ ml",
            "c_to_f" to "°C ↔ °F",
            "f_to_c" to "°F ↔ °C",
            "mmol_to_mg" to "mmol/L ↔ mg/dL",
            "mg_to_mmol" to "mg/dL ↔ mmol/L"
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                modes.forEach { (modeCode, modeLabel) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeMode = modeCode },
                        colors = CardDefaults.cardColors(
                            containerColor = if (activeMode == modeCode) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Box(modifier = Modifier.padding(12.dp).fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                            Text(modeLabel, fontWeight = if (activeMode == modeCode) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        }

        // Converter action
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(Translation.get("conv_from", lang), fontWeight = FontWeight.Bold)
                
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = inputVal,
                        onValueChange = { inputVal = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        suffix = { Text(labelLeft) }
                    )
                }

                Divider()

                Text(Translation.get("conv_result", lang), fontWeight = FontWeight.Bold)
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(4.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$result", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(labelRight, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }
    }
}

// --- Settings Screen ---
@Composable
fun SettingsScreen(viewModel: MainViewModel, lang: AppLanguage, isDark: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Translation.get("card_settings", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            border = CardDefaults.outlinedCardBorder(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Language settings
                Text(Translation.get("settings_lang", lang), fontWeight = FontWeight.Bold)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppLanguage.values().forEach { langOption ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setLanguage(langOption) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (langOption == lang) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Box(modifier = Modifier.padding(12.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text(langOption.displayName, fontWeight = if (langOption == lang) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Dark theme settings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(Translation.get("settings_theme", lang), fontWeight = FontWeight.Bold)
                    Switch(checked = isDark, onCheckedChange = { viewModel.toggleTheme() })
                }
            }
        }

        // About block
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = CardDefaults.outlinedCardBorder(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(Translation.get("settings_about", lang), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                Text(Translation.get("settings_about_desc", lang), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// --- Drug Interaction Checker Screen ---
data class CheckerDrug(
    val id: String,
    val name: String,
    val brand: String,
    val clazz: Map<AppLanguage, String>
)

data class InteractionRule(
    val drug1: String,
    val drug2: String,
    val severity: String, // "CRITICAL", "HIGH", "MONITOR"
    val mechanism: Map<AppLanguage, String>,
    val advice: Map<AppLanguage, String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrugInteractScreen(lang: AppLanguage) {
    // 1. Clinical Database
    val checkerDrugs = remember {
        listOf(
            CheckerDrug(
                id = "adrenaline",
                name = "Adrénaline",
                brand = "Épinéphrine",
                clazz = mapOf(
                    AppLanguage.FR to "Sympathomimétique, catécholamine",
                    AppLanguage.AR to "محاكي الودي، رافع ضغط",
                    AppLanguage.EN to "Sympathomimetic, catecholamine"
                )
            ),
            CheckerDrug(
                id = "amiodarone",
                name = "Amiodarone",
                brand = "Cordarone",
                clazz = mapOf(
                    AppLanguage.FR to "Antiarythmique de classe III",
                    AppLanguage.AR to "مضاد لاضطرابات نظم القلب فئة 3",
                    AppLanguage.EN to "Class III antiarrhythmic"
                )
            ),
            CheckerDrug(
                id = "atropine",
                name = "Atropine",
                brand = "Vagolytique",
                clazz = mapOf(
                    AppLanguage.FR to "Anticholinergique, vagolytique",
                    AppLanguage.AR to "مضاد الكولين، رافع النبض",
                    AppLanguage.EN to "Anticholinergic, vagolytic"
                )
            ),
            CheckerDrug(
                id = "diazepam",
                name = "Diazépam",
                brand = "Valium",
                clazz = mapOf(
                    AppLanguage.FR to "Benzodiazépine, anticonvulsant",
                    AppLanguage.AR to "بنزوديازيبين، مضاد تشنج",
                    AppLanguage.EN to "Benzodiazepine, anticonvulsant"
                )
            ),
            CheckerDrug(
                id = "morphine",
                name = "Morphine",
                brand = "Opiacé",
                clazz = mapOf(
                    AppLanguage.FR to "Antalgique majeur, opiacé",
                    AppLanguage.AR to "مسكن أفيوني قوي",
                    AppLanguage.EN to "Opioid analgesic"
                )
            ),
            CheckerDrug(
                id = "gentamicine",
                name = "Gentamicine",
                brand = "Aminoside",
                clazz = mapOf(
                    AppLanguage.FR to "Antibiotique aminoside",
                    AppLanguage.AR to "مضاد حيوي أمينوغليكوزيد",
                    AppLanguage.EN to "Aminoglycoside antibiotic"
                )
            ),
            CheckerDrug(
                id = "ceftriaxone",
                name = "Ceftriaxone",
                brand = "Rocéphine",
                clazz = mapOf(
                    AppLanguage.FR to "Antibiotique, Céphalosporine 3G",
                    AppLanguage.AR to "مضاد حيوي سيفالوسبورين جيل 3",
                    AppLanguage.EN to "Third-generation cephalosporin"
                )
            ),
            CheckerDrug(
                id = "aspirine",
                name = "Aspirine",
                brand = "Aspegic / Kardegic",
                clazz = mapOf(
                    AppLanguage.FR to "Antiagrégant plaquettaire, AINS",
                    AppLanguage.AR to "مضاد تجمع الصفائح، مسكن",
                    AppLanguage.EN to "Antiplatelet, NSAID"
                )
            ),
            CheckerDrug(
                id = "heparine",
                name = "Héparine",
                brand = "Calciparine / HNF",
                clazz = mapOf(
                    AppLanguage.FR to "Anticoagulant majeur",
                    AppLanguage.AR to "مضاد تخثر قوي",
                    AppLanguage.EN to "Anticoagulant"
                )
            ),
            CheckerDrug(
                id = "furosemide",
                name = "Furosémide",
                brand = "Lasix",
                clazz = mapOf(
                    AppLanguage.FR to "Diurétique de l'anse",
                    AppLanguage.AR to "مدر بول قوي",
                    AppLanguage.EN to "Loop diuretic"
                )
            ),
            CheckerDrug(
                id = "potassium",
                name = "Potassium (KCl)",
                brand = "Électrolyte",
                clazz = mapOf(
                    AppLanguage.FR to "Supplément électrolytique",
                    AppLanguage.AR to "مكمل بوتاسيوم وريدي/فموي",
                    AppLanguage.EN to "Electrolyte supplement"
                )
            ),
            CheckerDrug(
                id = "spironolactone",
                name = "Spironolactone",
                brand = "Aldactone",
                clazz = mapOf(
                    AppLanguage.FR to "Diurétique d'épargne potassique",
                    AppLanguage.AR to "مدر بول حافظ للبوتاسيوم",
                    AppLanguage.EN to "Potassium-sparing diuretic"
                )
            ),
            CheckerDrug(
                id = "ringer_lactate",
                name = "Ringer Lactate",
                brand = "RL (Contient du Calcium)",
                clazz = mapOf(
                    AppLanguage.FR to "Soluté de remplissage",
                    AppLanguage.AR to "محلول رينجر لاكتات المغذي",
                    AppLanguage.EN to "Intravenous fluid replacement"
                )
            )
        )
    }

    val interactionRules = remember {
        listOf(
            InteractionRule(
                drug1 = "diazepam",
                drug2 = "morphine",
                severity = "CRITICAL",
                mechanism = mapOf(
                    AppLanguage.FR to "Risque majeur de dépression respiratoire sévère, somnolence profonde, coma et décès par effet synergique dépresseur du SNC.",
                    AppLanguage.AR to "خطر شديد لتثبيط التنفس، الغيبوبة، والوفاة بسبب التأثير التآزري المثبط للجهاز العصبي المركزي بين البنزوديازيبين والأفيون.",
                    AppLanguage.EN to "Major risk of profound respiratory depression, severe sedation, coma, and death due to additive synergistic CNS depressant effects."
                ),
                advice = mapOf(
                    AppLanguage.FR to "Éviter l'association sauf si indispensable. Monitorer étroitement la fréquence respiratoire, l'état de conscience et la saturation (SpO2). Avoir l'antidote Naloxone et Flumazenil à portée immédiate.",
                    AppLanguage.AR to "تجنب الجمع بينهما إلا للضرورة القصوى. راقب بدقة معدل التنفس، درجة الوعي، ونسبة تشبع الأكسجين (SpO2). جهز مضادات الترياق (Naloxone و Flumazenil).",
                    AppLanguage.EN to "Avoid co-administration unless absolutely necessary. Closely monitor respiratory rate, level of consciousness, and oxygen saturation (SpO2). Keep Naloxone and Flumazenil rescue antidotes nearby."
                )
            ),
            InteractionRule(
                drug1 = "ceftriaxone",
                drug2 = "ringer_lactate",
                severity = "CRITICAL",
                mechanism = mapOf(
                    AppLanguage.FR to "Risque de précipitation de sel de ceftriaxone-calcium dans les poumons et les reins, pouvant être mortel, en particulier chez les nouveau-nés.",
                    AppLanguage.AR to "خطر تشكل ترسبات ملح السيفترياكسون والكالسيوم في الرئتين والكليتين، مما قد يهدد الحياة خاصة لدى حديثي الولادة والأطفال الصغار.",
                    AppLanguage.EN to "Risk of lethal ceftriaxone-calcium salt precipitation in lungs and kidneys, strictly contraindicated in neonates."
                ),
                advice = mapOf(
                    AppLanguage.FR to "Ne pas administrer simultanément par la même ligne de perfusion. Rincer abondamment la ligne entre les deux perfusions ou utiliser des voies différentes.",
                    AppLanguage.AR to "لا تعطِهما معًا في نفس خط التسريب الوريدي. اغسل الخط تمامًا بمحلول ملحي بين التسريبين أو استخدم خطين وريديين منفصلين.",
                    AppLanguage.EN to "Do not administer simultaneously through the same IV line. Thoroughly flush the line with normal saline between infusions, or use separate IV sites."
                )
            ),
            InteractionRule(
                drug1 = "potassium",
                drug2 = "spironolactone",
                severity = "CRITICAL",
                mechanism = mapOf(
                    AppLanguage.FR to "Risque d'hyperkaliémie sévère potentiellement mortelle par diminution de l'excrétion rénale du potassium associée à un apport exogène.",
                    AppLanguage.AR to "خطر حدوث ارتفاع شديد ومميت للبوتاسيوم في الدم بسبب انخفاض إفرازه الكلوي بفعل الاسبيرونولاكتون مع أخذ البوتاسيوم.",
                    AppLanguage.EN to "Risk of severe, life-threatening hyperkalemia due to additive potassium-retaining and supplementing effects."
                ),
                advice = mapOf(
                    AppLanguage.FR to "Éviter l'association systématique. Contrôler régulièrement la kaliémie et la fonction rénale. Arrêter la supplémentation si la kaliémie dépasse 5.0 mmol/L et surveiller l'ECG (ondes T pointues).",
                    AppLanguage.AR to "تجنب الجمع التلقائي بينهما. افحص بانتظام مستوى البوتاسيوم ووظائف الكلى. أوقف المكملات فورًا إذا تجاوز البوتاسيوم 5.0 ملمول/لتر وراقب تخطيط القلب.",
                    AppLanguage.EN to "Avoid routine co-administration. Frequently monitor serum potassium levels and renal function. Stop potassium supplements if potassium exceeds 5.0 mmol/L; monitor ECG for peaked T waves."
                )
            ),
            InteractionRule(
                drug1 = "adrenaline",
                drug2 = "amiodarone",
                severity = "HIGH",
                mechanism = mapOf(
                    AppLanguage.FR to "Majoration du risque d'arythmies ventriculaires sévères (torsades de pointes, tachycardie ventriculaire) par prolongation de l'intervalle QT.",
                    AppLanguage.AR to "زيادة خطر حدوث اضطرابات نظم بطينية خطيرة (تسارع بطيني) بسبب تآزر إطالة فاصل QT.",
                    AppLanguage.EN to "Increased risk of severe ventricular arrhythmias (torsades de pointes, ventricular tachycardia) due to cumulative prolonged QTc interval."
                ),
                advice = mapOf(
                    AppLanguage.FR to "Surveillance ECG continue obligatoire. Monitorer les électrolytes sanguins (kaliémie, magnésémie) pour réduire le risque d'arythmie.",
                    AppLanguage.AR to "المراقبة المستمرة لتخطيط القلب (ECG) إجبارية. راقب نسبة البوتاسيوم والمغنيسيوم في الدم لتقليل مخاطر اضطرابات النظم.",
                    AppLanguage.EN to "Continuous ECG monitoring is mandatory. Check blood electrolytes (potassium, magnesium) to mitigate arrhythmia triggers."
                )
            ),
            InteractionRule(
                drug1 = "aspirine",
                drug2 = "heparine",
                severity = "HIGH",
                mechanism = mapOf(
                    AppLanguage.FR to "Synergie pharmacodynamique augmentant considérablement le risque d'hémorragie majeure (gastro-intestinale, intracrânienne) par inhibition plaquettaire combinée à l'anticoagulation.",
                    AppLanguage.AR to "تأثير تآزري يزيد بشكل كبير من مخاطر حدوث نزيف حاد (هضمي أو دماغي) نتيجة الجمع بين تثبيط الصفائح ومضاد التخثر.",
                    AppLanguage.EN to "Pharmacodynamic synergy significantly increasing the risk of major bleeding (gastrointestinal, intracranial) by combining antiplatelet action with anticoagulation."
                ),
                advice = mapOf(
                    AppLanguage.FR to "Surveiller les signes cliniques de saignement (gingivorragies, hématurie, selles noires, ecchymoses). Contrôler régulièrement l'hémogramme (Hb, Plaquettes) et le TCA.",
                    AppLanguage.AR to "راقب علامات النزيف السريري (نزيف اللثة، بول دموي، براز أسود، كدمات). افحص بانتظام صورة الدم الكاملة ووقت التخثر (aPTT).",
                    AppLanguage.EN to "Monitor closely for clinical signs of bleeding (gum bleeding, hematuria, black/tarry stools, easy bruising). Regularly check hemoglobin, platelets, and aPTT."
                )
            ),
            InteractionRule(
                drug1 = "gentamicine",
                drug2 = "furosemide",
                severity = "HIGH",
                mechanism = mapOf(
                    AppLanguage.FR to "Augmentation synergique du risque d'ototoxicité (atteinte de l'audition et de l'équilibre) et de néphrotoxicité (insuffisance rénale aiguë).",
                    AppLanguage.AR to "زيادة تآزرية في مخاطر السمية السمعية (ضرر للسمع والاتزان) والسمية الكلوية (الفشل الكلوي الحاد) بفعل مدر البول الحلقي والامينوسيد.",
                    AppLanguage.EN to "Synergistic increase in risk of ototoxicity (vestibular and auditory damage) and nephrotoxicity (acute kidney injury)."
                ),
                advice = mapOf(
                    AppLanguage.FR to "Éviter l'association prolongée. Monitorer étroitement la diurèse, la créatininémie et l'audition du patient. Assurer une hydratation adéquate.",
                    AppLanguage.AR to "تجنب الجمع المطول بينهما. راقب بدقة كمية البول اليومية، الكرياتينين، والقدرة السمعية للمريض. حافظ على رطوبة وإماهة جيدة بالسوائل.",
                    AppLanguage.EN to "Avoid prolonged co-administration. Closely monitor daily urine output, serum creatinine, and hearing/balance function. Ensure patient remains well hydrated."
                )
            ),
            InteractionRule(
                drug1 = "gentamicine",
                drug2 = "ceftriaxone",
                severity = "MONITOR",
                mechanism = mapOf(
                    AppLanguage.FR to "Potentialisation possible de la toxicité rénale, bien que cette association d'antibiotiques soit couramment utilisée pour traiter les infections sévères.",
                    AppLanguage.AR to "احتمال زيادة خفيفة في السمية الكلوية، بالرغم من أن هذا الجمع المضاد للميكروبات شائع سريريًا لعلاج الالتهابات الشديدة.",
                    AppLanguage.EN to "Potential minor increase in renal toxicity, although this combination is frequently used in clinical practice for clinical synergy."
                ),
                advice = mapOf(
                    AppLanguage.FR to "Surveiller périodiquement la fonction rénale (créatininémie, clairance de la créatinine) et s'assurer d'une bonne hydratation du patient.",
                    AppLanguage.AR to "راقب وظائف الكلى دوريًا (الكرياتينين) وتأكد من حصول المريض على كميات كافية من السوائل الموصوفة.",
                    AppLanguage.EN to "Periodically monitor renal function (serum creatinine, GFR) and ensure optimal hydration status."
                )
            )
        )
    }

    // State Variables
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    var searchQuery by remember { mutableStateOf("") }

    // 2. Compute Interactions
    val activeInteractions = remember(selectedIds) {
        val list = mutableListOf<InteractionRule>()
        val selectedList = selectedIds.toList()
        for (i in 0 until selectedList.size) {
            for (j in i + 1 until selectedList.size) {
                val d1 = selectedList[i]
                val d2 = selectedList[j]
                val match = interactionRules.find {
                    (it.drug1 == d1 && it.drug2 == d2) || (it.drug1 == d2 && it.drug2 == d1)
                }
                if (match != null) {
                    list.add(match)
                }
            }
        }
        list
    }

    // Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Title
        Text(
            text = Translation.get("interact_title", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Selected Drugs horizontal list / chips
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = Translation.get("interact_selected", lang) + " (${selectedIds.size})",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (selectedIds.isEmpty()) {
                    Text(
                        text = if (lang == AppLanguage.AR) "لم يتم تحديد أي دواء" else "Aucun médicament sélectionné.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selectedIds.forEach { id ->
                            val drug = checkerDrugs.find { it.id == id }
                            if (drug != null) {
                                InputChip(
                                    selected = true,
                                    onClick = { selectedIds = selectedIds - id },
                                    label = { Text(drug.name, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    colors = InputChipDefaults.inputChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Interaction Results
        Text(
            text = if (lang == AppLanguage.FR) "Résultats d'analyse" else if (lang == AppLanguage.AR) "نتائج تحليل التفاعلات" else "Analysis Results",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxWidth()
        ) {
            if (selectedIds.size < 2) {
                // Empty / Welcome Onboarding State
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Shield",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = Translation.get("interact_empty_title", lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Translation.get("interact_empty_desc", lang),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )
                    }
                }
            } else if (activeInteractions.isEmpty()) {
                // Success State - No interactions
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = if (lang == AppLanguage.FR) "Sécurité Validée" else if (lang == AppLanguage.AR) "أمان تام" else "Safety Checked",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = Translation.get("interact_no_interaction", lang),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF2E7D32),
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                // List of interactions found
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(activeInteractions) { rule ->
                        val d1Name = checkerDrugs.find { it.id == rule.drug1 }?.name ?: rule.drug1
                        val d2Name = checkerDrugs.find { it.id == rule.drug2 }?.name ?: rule.drug2
                        
                        val (severityColor, severityText) = when (rule.severity) {
                            "CRITICAL" -> Pair(Color(0xFFBA1A1A), Translation.get("severity_critical", lang))
                            "HIGH" -> Pair(Color(0xFFE65100), Translation.get("severity_high", lang))
                            else -> Pair(Color(0xFFD4AF37), Translation.get("severity_monitor", lang))
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                // Left colored indicator
                                Box(
                                    modifier = Modifier
                                        .width(6.dp)
                                        .fillMaxHeight()
                                        .align(Alignment.CenterVertically)
                                        .background(severityColor)
                                        .size(height = 140.dp, width = 6.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "$d1Name + $d2Name",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = severityColor.copy(alpha = 0.12f),
                                            contentColor = severityColor
                                        ) {
                                            Text(
                                                text = severityText,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                                    // Mechanism
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = Translation.get("interact_mechanism", lang),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = severityColor
                                        )
                                        Text(
                                            text = rule.mechanism[lang] ?: rule.mechanism[AppLanguage.EN] ?: "",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 18.sp
                                        )
                                    }

                                    // Advice
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = Translation.get("interact_advice", lang),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = rule.advice[lang] ?: rule.advice[AppLanguage.EN] ?: "",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Checklist of available medications
        Text(
            text = Translation.get("interact_select_prompt", lang),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Live Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(Translation.get("interact_search_hint", lang)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // Drug Checklist View
        val filteredDrugs = remember(searchQuery) {
            if (searchQuery.isBlank()) {
                checkerDrugs
            } else {
                checkerDrugs.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.brand.contains(searchQuery, ignoreCase = true)
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(filteredDrugs) { drug ->
                val isSelected = selectedIds.contains(drug.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedIds = if (isSelected) {
                                selectedIds - drug.id
                            } else {
                                selectedIds + drug.id
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                    ),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = drug.name,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${drug.brand} • ${drug.clazz[lang] ?: drug.clazz[AppLanguage.EN]}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { checked ->
                                selectedIds = if (checked) {
                                    selectedIds + drug.id
                                } else {
                                    selectedIds - drug.id
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

