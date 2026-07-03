package com.example

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.*
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PolishPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// --- Types & States ---
enum class Screen {
    SPLASH,
    HOME,
    DOSE_CALC,
    PERFUSION_CALC,
    ECG_GUIDE,
    MEDICAL_SCORES,
    EMERGENCY_DRUGS,
    DRUG_INTERACT,
    NURSING_PROTOCOLS,
    QUICK_NOTES,
    TIMERS,
    CONVERTER,
    SETTINGS,
    AI_ASSISTANT,
    DRUG_SAFETY,
    VOICE_CHARTING,
    SHIFT_ASSISTANT,
    EMERGENCY_MODE,
    NURSING_SIMULATOR,
    WOUND_TRACKER,
    MED_SCANNER,
    GAMIFICATION
}

data class ActiveTimer(
    val id: String,
    val label: String,
    val totalSeconds: Int,
    val secondsRemaining: Int,
    val isPaused: Boolean = false
)

data class AlarmAlert(
    val id: String,
    val label: String,
    val timestamp: Long = System.currentTimeMillis()
)

// --- ViewModel Factory ---
class MainViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// --- Main ViewModel ---
class MainViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(AppLanguage.FR)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Notes state
    private val _notesList = MutableStateFlow<List<Note>>(emptyList())
    val notesList: StateFlow<List<Note>> = _notesList.asStateFlow()

    private val _noteSearchQuery = MutableStateFlow("")
    val noteSearchQuery: StateFlow<String> = _noteSearchQuery.asStateFlow()

    private val _editingNote = MutableStateFlow<Note?>(null)
    val editingNote: StateFlow<Note?> = _editingNote.asStateFlow()

    // Timers State
    private val _activeTimers = MutableStateFlow<List<ActiveTimer>>(emptyList())
    val activeTimers: StateFlow<List<ActiveTimer>> = _activeTimers.asStateFlow()

    private val _activeAlarms = MutableStateFlow<List<AlarmAlert>>(emptyList())
    val activeAlarms: StateFlow<List<AlarmAlert>> = _activeAlarms.asStateFlow()

    // --- Gamification System State ---
    private val _xp = MutableStateFlow(320) // Start with some base experience
    val xp: StateFlow<Int> = _xp.asStateFlow()

    private val _level = MutableStateFlow(3) // Start at Level 3
    val level: StateFlow<Int> = _level.asStateFlow()

    private val _badges = MutableStateFlow(listOf("Nouveau Né", "Héros Clinique", "Calculateur Rapide"))
    val badges: StateFlow<List<String>> = _badges.asStateFlow()

    private val _streak = MutableStateFlow(5) // 5-day streak
    val streak: StateFlow<Int> = _streak.asStateFlow()

    fun addXp(amount: Int) {
        val newXp = _xp.value + amount
        _xp.value = newXp
        // Every 200 XP is a level
        val newLevel = (newXp / 200) + 1
        if (newLevel > _level.value) {
            _level.value = newLevel
            // Add a badge on level up
            val badgeName = when (newLevel) {
                4 -> "Gourou des Doses"
                5 -> "Major de Promo"
                else -> "Expert Pocket Lvl $newLevel"
            }
            if (!_badges.value.contains(badgeName)) {
                _badges.value = _badges.value + badgeName
            }
        }
    }

    // --- Shift Assistant State ---
    private val _patients = MutableStateFlow<List<Patient>>(
        listOf(
            Patient(
                id = "p1",
                name = "Jean Dupont",
                room = "302-A",
                age = "68",
                diagnosis = "Insuffisance Cardiaque Congestive (ICC)",
                vitals = listOf(
                    VitalSign(bp = "138/84", pulse = "88", temp = "36.8", spo2 = "94", resp = "18")
                ),
                medications = listOf(
                    ScheduledMed(id = "m1", time = "08:00", name = "Furosémide", dosage = "40 mg IV"),
                    ScheduledMed(id = "m2", time = "12:00", name = "Ramipril", dosage = "5 mg PO", isGiven = true)
                ),
                wounds = listOf(
                    WoundEntry(id = "w1", location = "Sacrum", stage = "Stade II", dimensions = "2x3 cm", progress = 40, notes = "Bords réguliers, lit fibrineux à 20%", imagePresetIndex = 1)
                )
            ),
            Patient(
                id = "p2",
                name = "Fatima Alami",
                room = "304-B",
                age = "54",
                diagnosis = "Pneumopathie bactérienne communautaire",
                vitals = listOf(
                    VitalSign(bp = "115/72", pulse = "94", temp = "38.2", spo2 = "92", resp = "22")
                ),
                medications = listOf(
                    ScheduledMed(id = "m3", time = "08:00", name = "Ceftriaxone", dosage = "1 g IV"),
                    ScheduledMed(id = "m4", time = "14:00", name = "Paracétamol", dosage = "1 g IV")
                )
            )
        )
    )
    val patients: StateFlow<List<Patient>> = _patients.asStateFlow()

    private val _shiftTasks = MutableStateFlow<List<ShiftTask>>(
        listOf(
            ShiftTask(id = "t1", label = "Transmission de garde du matin", isCompleted = true),
            ShiftTask(id = "t2", label = "Tournée des constantes - 10h", isCompleted = false),
            ShiftTask(id = "t3", label = "Vérification du chariot d'urgence", isCompleted = false),
            ShiftTask(id = "t4", label = "Planification des pansements complexes", isCompleted = false)
        )
    )
    val shiftTasks: StateFlow<List<ShiftTask>> = _shiftTasks.asStateFlow()

    fun addPatient(name: String, room: String, age: String, diagnosis: String) {
        if (name.isBlank() || room.isBlank()) return
        val newPatient = Patient(
            name = name,
            room = room,
            age = age.ifBlank { "N/A" },
            diagnosis = diagnosis.ifBlank { "N/A" }
        )
        _patients.value = _patients.value + newPatient
        addXp(30) // Reward XP for adding patient
    }

    fun addPatientVitals(patientId: String, bp: String, pulse: String, temp: String, spo2: String, resp: String) {
        _patients.value = _patients.value.map { p ->
            if (p.id == patientId) {
                val newVitals = p.vitals + VitalSign(bp = bp, pulse = pulse, temp = temp, spo2 = spo2, resp = resp)
                p.copy(vitals = newVitals)
            } else p
        }
        addXp(15) // Reward XP for logging vitals
    }

    fun toggleMedGiven(patientId: String, medId: String) {
        _patients.value = _patients.value.map { p ->
            if (p.id == patientId) {
                val newMeds = p.medications.map { m ->
                    if (m.id == medId) {
                        val newStatus = !m.isGiven
                        if (newStatus) addXp(10) // Reward XP on medication given
                        m.copy(isGiven = newStatus)
                    } else m
                }
                p.copy(medications = newMeds)
            } else p
        }
    }

    fun addPatientMed(patientId: String, name: String, dosage: String, time: String) {
        if (name.isBlank() || dosage.isBlank()) return
        _patients.value = _patients.value.map { p ->
            if (p.id == patientId) {
                val newMeds = p.medications + ScheduledMed(name = name, dosage = dosage, time = time.ifBlank { "08:00" })
                p.copy(medications = newMeds)
            } else p
        }
        addXp(20)
    }

    fun addPatientWound(patientId: String, location: String, stage: String, dimensions: String, progress: Int, notes: String, presetIndex: Int) {
        _patients.value = _patients.value.map { p ->
            if (p.id == patientId) {
                val newWounds = p.wounds + WoundEntry(
                    location = location.ifBlank { "Sacrum" },
                    stage = stage,
                    dimensions = dimensions.ifBlank { "N/A" },
                    progress = progress,
                    notes = notes,
                    imagePresetIndex = presetIndex
                )
                p.copy(wounds = newWounds)
            } else p
        }
        addXp(40) // Wound assessment is complex, high XP!
    }

    fun addShiftTask(label: String) {
        if (label.isBlank()) return
        val newTask = ShiftTask(label = label)
        _shiftTasks.value = _shiftTasks.value + newTask
        addXp(5)
    }

    fun toggleShiftTask(taskId: String) {
        _shiftTasks.value = _shiftTasks.value.map { t ->
            if (t.id == taskId) {
                val newStatus = !t.isCompleted
                if (newStatus) addXp(15)
                t.copy(isCompleted = newStatus)
            } else t
        }
    }

    private var ringtone: Ringtone? = null
    private var contextRef: Context? = null

    init {
        // Observe search query and notes
        viewModelScope.launch {
            _noteSearchQuery.collectLatest { query ->
                if (query.isBlank()) {
                    repository.allNotes.collect { _notesList.value = it }
                } else {
                    repository.searchNotes(query).collect { _notesList.value = it }
                }
            }
        }

        // Ticking Coroutine for Timers
        viewModelScope.launch {
            while (true) {
                delay(1000)
                tickTimers()
            }
        }
    }

    fun registerContext(context: Context) {
        contextRef = context.applicationContext
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    // --- Notes CRUD ---
    fun saveNote(title: String, content: String) {
        viewModelScope.launch {
            val noteToSave = _editingNote.value?.copy(
                title = title.ifBlank { "Sans titre" },
                content = content,
                timestamp = System.currentTimeMillis()
            ) ?: Note(title = title.ifBlank { "Sans titre" }, content = content)
            
            if (noteToSave.id == 0) {
                repository.insert(noteToSave)
            } else {
                repository.update(noteToSave)
            }
            _editingNote.value = null
        }
    }

    fun startEditingNote(note: Note?) {
        _editingNote.value = note
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
            if (_editingNote.value?.id == note.id) {
                _editingNote.value = null
            }
        }
    }

    fun setNoteSearchQuery(query: String) {
        _noteSearchQuery.value = query
    }

    // --- Timers ---
    fun startTimer(label: String, minutes: Int) {
        if (minutes <= 0) return
        val totalSec = minutes * 60
        val newTimer = ActiveTimer(
            id = System.currentTimeMillis().toString(),
            label = label.ifBlank { "Rappel de perfusion" },
            totalSeconds = totalSec,
            secondsRemaining = totalSec
        )
        _activeTimers.value = _activeTimers.value + newTimer
    }

    fun togglePauseTimer(timerId: String) {
        _activeTimers.value = _activeTimers.value.map {
            if (it.id == timerId) it.copy(isPaused = !it.isPaused) else it
        }
    }

    fun cancelTimer(timerId: String) {
        _activeTimers.value = _activeTimers.value.filter { it.id != timerId }
    }

    private fun tickTimers() {
        val current = _activeTimers.value
        if (current.isEmpty()) return

        val triggered = mutableListOf<ActiveTimer>()
        val updated = current.map { timer ->
            if (!timer.isPaused) {
                val rem = timer.secondsRemaining - 1
                if (rem <= 0) {
                    triggered.add(timer)
                    null
                } else {
                    timer.copy(secondsRemaining = rem)
                }
            } else {
                timer
            }
        }.filterNotNull()

        _activeTimers.value = updated

        triggered.forEach {
            triggerAlarm(it)
        }
    }

    private fun triggerAlarm(timer: ActiveTimer) {
        val alarm = AlarmAlert(id = timer.id, label = timer.label)
        _activeAlarms.value = _activeAlarms.value + alarm

        contextRef?.let { context ->
            playAlarmSound(context)
            vibrateDevice(context)
            showNotification(context, "Nurse Pocket: ${timer.label}", "Le minuteur est terminé!")
        }
    }

    fun dismissAlarm(alarmId: String) {
        _activeAlarms.value = _activeAlarms.value.filter { it.id != alarmId }
        if (_activeAlarms.value.isEmpty()) {
            stopAlarmSound()
        }
    }

    private fun playAlarmSound(context: Context) {
        try {
            if (ringtone?.isPlaying == true) return
            val alertUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(context, alertUri)
            ringtone?.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopAlarmSound() {
        try {
            ringtone?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun vibrateDevice(context: Context) {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 500, 200, 500, 200, 500), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(1500)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNotification(context: Context, title: String, content: String) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "nurse_pocket_timers"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Nurse Pocket Alarms",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Alarms for medications and infusions"
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 500, 1000))

            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

// --- Android Activity ---
class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var repository: NoteRepository
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "nurse_pocket_db"
        ).fallbackToDestructiveMigration().build()

        repository = NoteRepository(database.noteDao())

        // Init ViewModel
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        viewModel.registerContext(this)

        enableEdgeToEdge()

        setContent {
            val isDark by viewModel.isDarkMode.collectAsState()
            MyApplicationTheme(darkTheme = isDark) {
                NursePocketApp(viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopAlarmSound()
    }
}

// --- Main App Root ---
@Composable
fun NursePocketApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val activeAlarms by viewModel.activeAlarms.collectAsState()

    val layoutDirection = if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (currentScreen == Screen.SPLASH) {
                SplashScreen(viewModel)
            } else {
                Scaffold(
                    topBar = {
                        @OptIn(ExperimentalMaterial3Api::class)
                        TopAppBar(
                            title = {
                                Column {
                                    Text(
                                        Translation.get("app_title", currentLanguage),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        Translation.get("app_subtitle", currentLanguage),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            navigationIcon = {
                                if (currentScreen != Screen.HOME) {
                                    IconButton(
                                        onClick = { viewModel.setScreen(Screen.HOME) },
                                        modifier = Modifier.testTag("back_button")
                                    ) {
                                        Icon(
                                            imageVector = if (currentLanguage.isRtl) Icons.Default.ArrowForward else Icons.Default.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            },
                            actions = {
                                IconButton(onClick = { viewModel.toggleTheme() }) {
                                    Icon(
                                        imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Theme"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                            )
                        )
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            Screen.HOME -> HomeScreen(viewModel, currentLanguage)
                            Screen.DOSE_CALC -> DoseCalcScreen(viewModel, currentLanguage)
                            Screen.PERFUSION_CALC -> PerfusionCalcScreen(viewModel, currentLanguage)
                            Screen.ECG_GUIDE -> EcgGuideScreen(currentLanguage)
                            Screen.MEDICAL_SCORES -> ScoresScreen(currentLanguage)
                            Screen.EMERGENCY_DRUGS -> DrugsScreen(currentLanguage)
                            Screen.DRUG_INTERACT -> DrugInteractScreen(currentLanguage)
                            Screen.NURSING_PROTOCOLS -> ProtocolsScreen(currentLanguage)
                            Screen.QUICK_NOTES -> NotesScreen(viewModel, currentLanguage)
                            Screen.TIMERS -> TimersScreen(viewModel, currentLanguage)
                            Screen.CONVERTER -> ConverterScreen(currentLanguage)
                            Screen.SETTINGS -> SettingsScreen(viewModel, currentLanguage, isDark)
                            
                            Screen.AI_ASSISTANT -> AiAssistantScreen(viewModel, currentLanguage)
                            Screen.DRUG_SAFETY -> DrugSafetyScreen(viewModel, currentLanguage)
                            Screen.VOICE_CHARTING -> VoiceChartingScreen(viewModel, currentLanguage)
                            Screen.SHIFT_ASSISTANT -> ShiftAssistantScreen(viewModel, currentLanguage)
                            Screen.EMERGENCY_MODE -> EmergencyScreen(currentLanguage)
                            Screen.NURSING_SIMULATOR -> SimulatorScreen(viewModel, currentLanguage)
                            Screen.WOUND_TRACKER -> WoundTrackerScreen(viewModel, currentLanguage)
                            Screen.MED_SCANNER -> MedScannerScreen(viewModel, currentLanguage)
                            Screen.GAMIFICATION -> GamificationScreen(viewModel, currentLanguage)
                            
                            else -> {}
                        }
                    }
                }
            }

            // Alarms Heads-up overlay (Active Alarm Dialog)
            if (activeAlarms.isNotEmpty()) {
                val alarm = activeAlarms.first()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .border(3.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Alarm",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(64.dp)
                            )
                            
                            Text(
                                text = if (currentLanguage.isRtl) "🚨 تنبيه ممرض !" else "🚨 ALERTE INFIRMIÈRE !",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )

                            Text(
                                text = alarm.label,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )

                            Button(
                                onClick = { viewModel.dismissAlarm(alarm.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("dismiss_alarm_button")
                            ) {
                                Text(
                                    text = if (currentLanguage.isRtl) "إيقاف التنبيه" else "ARRÊTER L'ALERTE",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Logo Drawing Component ---
@Composable
fun NursePocketLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(140.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF80DEEA), Color(0xFF0077C2))
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Red / Blue Cross in background
        Box(
            modifier = Modifier
                .size(70.dp, 22.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
        )
        Box(
            modifier = Modifier
                .size(22.dp, 70.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
        )
        // Icon over cross
        Icon(
            imageVector = Icons.Default.MedicalServices,
            contentDescription = "Nurse Logo",
            tint = Color(0xFF005691),
            modifier = Modifier.size(36.dp)
        )
        // Stethoscope visual wrap (Circle)
        Box(
            modifier = Modifier
                .size(110.dp)
                .border(3.dp, Color.White.copy(alpha = 0.7f), CircleShape)
        )
    }
}

// --- Splash Screen ---
@Composable
fun SplashScreen(viewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        delay(2000)
        viewModel.setScreen(Screen.HOME)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A192F), Color(0xFF172A45))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            NursePocketLogo()

            Text(
                text = "Nurse Pocket",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Text(
                text = "Assistant infirmier intelligent",
                color = Color(0xFF80DEEA),
                fontSize = 14.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(30.dp))
            CircularProgressIndicator(color = Color(0xFF80DEEA))
        }
    }
}

// --- Home Screen Menu ---
@Composable
fun HomeScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val items = listOf(
        Screen.DOSE_CALC to Pair(Translation.get("card_doses", lang), Icons.Default.Calculate),
        Screen.PERFUSION_CALC to Pair(Translation.get("card_perfusion", lang), Icons.Default.WaterDrop),
        Screen.ECG_GUIDE to Pair(Translation.get("card_ecg", lang), Icons.Default.Timeline),
        Screen.MEDICAL_SCORES to Pair(Translation.get("card_scores", lang), Icons.Default.Assignment),
        Screen.EMERGENCY_DRUGS to Pair(Translation.get("card_drugs", lang), Icons.Default.Medication),
        Screen.DRUG_INTERACT to Pair(Translation.get("card_interact", lang), Icons.Default.Shield),
        Screen.NURSING_PROTOCOLS to Pair(Translation.get("card_protocols", lang), Icons.Default.HealthAndSafety),
        Screen.QUICK_NOTES to Pair(Translation.get("card_notes", lang), Icons.Default.EditNote),
        Screen.TIMERS to Pair(Translation.get("card_timers", lang), Icons.Default.Alarm),
        Screen.CONVERTER to Pair(Translation.get("card_converter", lang), Icons.Default.Transform),
        
        Screen.AI_ASSISTANT to Pair(Translation.get("card_ai_assistant", lang), Icons.Default.AutoAwesome),
        Screen.DRUG_SAFETY to Pair(Translation.get("card_drug_safety", lang), Icons.Default.GppGood),
        Screen.VOICE_CHARTING to Pair(Translation.get("card_voice_charting", lang), Icons.Default.Mic),
        Screen.SHIFT_ASSISTANT to Pair(Translation.get("card_shift_assistant", lang), Icons.Default.AssignmentInd),
        Screen.EMERGENCY_MODE to Pair(Translation.get("card_emergency_mode", lang), Icons.Default.Campaign),
        Screen.NURSING_SIMULATOR to Pair(Translation.get("card_nursing_simulator", lang), Icons.Default.School),
        Screen.WOUND_TRACKER to Pair(Translation.get("card_wound_tracker", lang), Icons.Default.History),
        Screen.MED_SCANNER to Pair(Translation.get("card_med_scanner", lang), Icons.Default.QrCodeScanner),
        Screen.GAMIFICATION to Pair(Translation.get("card_gamification", lang), Icons.Default.EmojiEvents),
        
        Screen.SETTINGS to Pair(Translation.get("card_settings", lang), Icons.Default.Settings)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (lang == AppLanguage.AR) "الأدوات الطبية التمريضية" else "Outils Cliniques",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { (screen, info) ->
                val (title, icon) = info
                val isDarkTheme = !MaterialTheme.colorScheme.primary.equals(PolishPrimary)
                val (containerColor, iconColor) = when (screen) {
                    Screen.DOSE_CALC -> if (isDarkTheme) Pair(Color(0xFF00325B), Color(0xFFD3E4FF)) else Pair(Color(0xFFD3E4FF), Color(0xFF001D36))
                    Screen.PERFUSION_CALC -> if (isDarkTheme) Pair(Color(0xFF3B0082), Color(0xFFEADDFF)) else Pair(Color(0xFFEADDFF), Color(0xFF21005D))
                    Screen.ECG_GUIDE -> if (isDarkTheme) Pair(Color(0xFF680003), Color(0xFFF2B8B5)) else Pair(Color(0xFFF2B8B5), Color(0xFF410E0B))
                    Screen.MEDICAL_SCORES -> if (isDarkTheme) Pair(Color(0xFF00391C), Color(0xFFC1EAD1)) else Pair(Color(0xFFC1EAD1), Color(0xFF00210E))
                    Screen.EMERGENCY_DRUGS -> if (isDarkTheme) Pair(Color(0xFF5B1A00), Color(0xFFFFDBCB)) else Pair(Color(0xFFFFDBCB), Color(0xFF341100))
                    Screen.DRUG_INTERACT -> if (isDarkTheme) Pair(Color(0xFF002A3A), Color(0xFFC2E8FF)) else Pair(Color(0xFFC2E8FF), Color(0xFF001E2F))
                    Screen.NURSING_PROTOCOLS -> if (isDarkTheme) Pair(Color(0xFF2F3033), Color(0xFFE2E2E6)) else Pair(Color(0xFFE2E2E6), Color(0xFF1A1C1E))
                    Screen.QUICK_NOTES -> if (isDarkTheme) Pair(Color(0xFF4D3D00), Color(0xFFF4D03F)) else Pair(Color(0x33F4D03F), Color(0xFF7D6608))
                    Screen.TIMERS -> if (isDarkTheme) Pair(Color(0xFF680003), Color(0xFFFFDAD6)) else Pair(Color(0xFFFFDAD6), Color(0xFF410002))
                    Screen.CONVERTER -> if (isDarkTheme) Pair(Color(0xFF003732), Color(0xFFE0F2F1)) else Pair(Color(0xFFE0F2F1), Color(0xFF004D40))
                    Screen.SETTINGS -> if (isDarkTheme) Pair(Color(0xFF252A2D), Color(0xFFECEFF1)) else Pair(Color(0xFFECEFF1), Color(0xFF37474F))
                    
                    Screen.AI_ASSISTANT -> if (isDarkTheme) Pair(Color(0xFF311B92), Color(0xFFEDE7F6)) else Pair(Color(0xFFEDE7F6), Color(0xFF311B92))
                    Screen.DRUG_SAFETY -> if (isDarkTheme) Pair(Color(0xFF1B5E20), Color(0xFFE8F5E9)) else Pair(Color(0xFFE8F5E9), Color(0xFF1B5E20))
                    Screen.VOICE_CHARTING -> if (isDarkTheme) Pair(Color(0xFF0D47A1), Color(0xFFE3F2FD)) else Pair(Color(0xFFE3F2FD), Color(0xFF0D47A1))
                    Screen.SHIFT_ASSISTANT -> if (isDarkTheme) Pair(Color(0xFF004D40), Color(0xFFE0F2F1)) else Pair(Color(0xFFE0F2F1), Color(0xFF004D40))
                    Screen.EMERGENCY_MODE -> if (isDarkTheme) Pair(Color(0xFFB71C1C), Color(0xFFFFEBEE)) else Pair(Color(0xFFFFEBEE), Color(0xFFB71C1C))
                    Screen.NURSING_SIMULATOR -> if (isDarkTheme) Pair(Color(0xFFE65100), Color(0xFFFFF3E0)) else Pair(Color(0xFFFFF3E0), Color(0xFFE65100))
                    Screen.WOUND_TRACKER -> if (isDarkTheme) Pair(Color(0xFF4A148C), Color(0xFFF3E5F5)) else Pair(Color(0xFFF3E5F5), Color(0xFF4A148C))
                    Screen.MED_SCANNER -> if (isDarkTheme) Pair(Color(0xFF006064), Color(0xFFE0F7FA)) else Pair(Color(0xFFE0F7FA), Color(0xFF006064))
                    Screen.GAMIFICATION -> if (isDarkTheme) Pair(Color(0xFFF57F17), Color(0xFFFFFDE7)) else Pair(Color(0xFFFFFDE7), Color(0xFFF57F17))
                    
                    else -> Pair(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), MaterialTheme.colorScheme.primary)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp)
                        .clickable { viewModel.setScreen(screen) }
                        .testTag("menu_card_${screen.name.lowercase()}"),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    shape = RoundedCornerShape(24.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(containerColor, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = iconColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            fontSize = 12.5.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

// --- Quick Notes CRUD Screen ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val notes by viewModel.notesList.collectAsState()
    val searchQuery by viewModel.noteSearchQuery.collectAsState()
    val editingNote by viewModel.editingNote.collectAsState()

    var noteTitleInput by remember { mutableStateOf("") }
    var noteContentInput by remember { mutableStateOf("") }

    LaunchedEffect(editingNote) {
        noteTitleInput = editingNote?.title ?: ""
        noteContentInput = editingNote?.content ?: ""
    }

    if (editingNote != null) {
        // Edit/Create mode
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.startEditingNote(null) }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                Spacer(modifier = Modifier.width(8.dp))
                Text(Translation.get("back", lang), fontWeight = FontWeight.Bold)
            }

            Text(
                text = if (editingNote?.id == 0) Translation.get("add_note", lang) else Translation.get("edit_note", lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = noteTitleInput,
                onValueChange = { noteTitleInput = it },
                label = { Text(Translation.get("note_title", lang)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("note_title_input"),
                singleLine = true
            )

            OutlinedTextField(
                value = noteContentInput,
                onValueChange = { noteContentInput = it },
                label = { Text(Translation.get("note_content", lang)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("note_content_input")
            )

            Button(
                onClick = { viewModel.saveNote(noteTitleInput, noteContentInput) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_note_button")
            ) {
                Text(Translation.get("save", lang), fontWeight = FontWeight.Bold)
            }
        }
    } else {
        // Notes list view
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Translation.get("card_notes", lang),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                IconButton(
                    onClick = { viewModel.startEditingNote(Note(id = 0, title = "", content = "")) },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        .testTag("add_note_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setNoteSearchQuery(it) },
                placeholder = { Text(Translation.get("search", lang)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("note_search_input"),
                singleLine = true
            )

            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = Translation.get("empty_notes", lang),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notes) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.startEditingNote(note) }
                                .testTag("note_card_${note.id}"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = note.title,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.deleteNote(note) },
                                        modifier = Modifier.testTag("delete_note_button_${note.id}")
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                                Text(
                                    text = note.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Timers & Alarms Screen ---
@Composable
fun TimersScreen(viewModel: MainViewModel, lang: AppLanguage) {
    val activeTimers by viewModel.activeTimers.collectAsState()
    var labelInput by remember { mutableStateOf("") }
    var minutesInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Translation.get("card_timers", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Set new timer card
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = CardDefaults.outlinedCardBorder(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(Translation.get("timer_set", lang), fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = labelInput,
                    onValueChange = { labelInput = it },
                    label = { Text(Translation.get("timer_label", lang)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("timer_label_input"),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = minutesInput,
                        onValueChange = { minutesInput = it },
                        label = { Text(Translation.get("timer_duration", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("timer_minutes_input"),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            val mins = minutesInput.toIntOrNull() ?: 0
                            if (mins > 0) {
                                viewModel.startTimer(labelInput, mins)
                                labelInput = ""
                                minutesInput = ""
                            }
                        },
                        modifier = Modifier
                            .height(56.dp)
                            .testTag("start_timer_button")
                    ) {
                        Text(Translation.get("timer_start_btn", lang), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active timers list
        Text(Translation.get("timer_active", lang), fontWeight = FontWeight.Bold)

        if (activeTimers.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = if (lang == AppLanguage.AR) "لا توجد مؤقتات نشطة حالياً" else "Aucun minuteur actif",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(activeTimers) { timer ->
                    val min = timer.secondsRemaining / 60
                    val sec = timer.secondsRemaining % 60
                    val timeStr = String.format("%02d:%02d", min, sec)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = CardDefaults.outlinedCardBorder(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (timer.isPaused) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(timer.label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                Text("${timer.secondsRemaining} ${Translation.get("timer_sec_left", lang)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                            }

                            Text(
                                text = timeStr,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            // Pause / Play
                            IconButton(onClick = { viewModel.togglePauseTimer(timer.id) }) {
                                Icon(
                                    imageVector = if (timer.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                    contentDescription = "Pause/Play",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Cancel
                            IconButton(onClick = { viewModel.cancelTimer(timer.id) }) {
                                Icon(Icons.Default.Cancel, contentDescription = "Cancel", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
