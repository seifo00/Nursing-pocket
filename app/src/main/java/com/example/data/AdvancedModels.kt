package com.example.data

import com.example.ui.AppLanguage

data class Patient(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val room: String,
    val age: String,
    val diagnosis: String,
    val vitals: List<VitalSign> = emptyList(),
    val medications: List<ScheduledMed> = emptyList(),
    val wounds: List<WoundEntry> = emptyList()
)

data class VitalSign(
    val timestamp: Long = System.currentTimeMillis(),
    val bp: String,
    val pulse: String,
    val temp: String,
    val spo2: String,
    val resp: String
)

data class ScheduledMed(
    val id: String = System.currentTimeMillis().toString(),
    val time: String,
    val name: String,
    val dosage: String,
    val isGiven: Boolean = false
)

data class WoundEntry(
    val id: String = System.currentTimeMillis().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val location: String, // e.g., Sacrum, Heel
    val stage: String,    // e.g., Stage I, II, III, IV
    val dimensions: String, // e.g., 2x3 cm
    val progress: Int,    // 0-100%
    val notes: String,
    val imagePresetIndex: Int = 0 // Represents mock visual timeline image index
)

data class ShiftTask(
    val id: String = System.currentTimeMillis().toString(),
    val label: String,
    val isCompleted: Boolean = false
)
