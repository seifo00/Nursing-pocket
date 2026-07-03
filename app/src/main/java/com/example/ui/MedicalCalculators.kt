package com.example.ui

import kotlin.math.roundToInt

// --- Dose Calculator ---
data class DoseCalculationResult(
    val totalDoseMg: Double,
    val volumeToAdministerMl: Double
)

object MedicalCalculators {

    fun calculateDose(
        weightKg: Double,
        prescribedDoseMgKg: Double,
        availableMg: Double,
        availableMl: Double
    ): DoseCalculationResult {
        if (weightKg <= 0 || prescribedDoseMgKg <= 0 || availableMg <= 0 || availableMl <= 0) {
            return DoseCalculationResult(0.0, 0.0)
        }
        val totalDose = weightKg * prescribedDoseMgKg
        val volume = (totalDose * availableMl) / availableMg
        return DoseCalculationResult(
            totalDoseMg = (totalDose * 100.0).roundToInt() / 100.0,
            volumeToAdministerMl = (volume * 100.0).roundToInt() / 100.0
        )
    }

    // --- Perfusion Calculator ---
    data class PerfusionResult(
        val mlPerHour: Double,
        val dropsPerMinute: Double
    )

    fun calculatePerfusion(
        volumeMl: Double,
        durationMinutes: Double,
        dropFactor: Double
    ): PerfusionResult {
        if (volumeMl <= 0 || durationMinutes <= 0 || dropFactor <= 0) {
            return PerfusionResult(0.0, 0.0)
        }
        val mlPerHour = (volumeMl / (durationMinutes / 60.0))
        val dropsPerMinute = (volumeMl * dropFactor) / durationMinutes
        return PerfusionResult(
            mlPerHour = (mlPerHour * 10.0).roundToInt() / 10.0,
            dropsPerMinute = (dropsPerMinute * 10.0).roundToInt() / 10.0
        )
    }

    // --- BMI Calculator ---
    data class BmiResult(
        val score: Double,
        val interpretation: Map<AppLanguage, String>
    )

    fun calculateBmi(weightKg: Double, heightCm: Double): BmiResult {
        if (weightKg <= 0 || heightCm <= 0) {
            return BmiResult(0.0, emptyMap())
        }
        val heightM = heightCm / 100.0
        val bmi = weightKg / (heightM * heightM)
        val roundedBmi = (bmi * 10.0).roundToInt() / 10.0

        val interpretation = when {
            roundedBmi < 18.5 -> mapOf(
                AppLanguage.FR to "Insuffisance pondérale (Maigreur)",
                AppLanguage.AR to "نقص الوزن (نحافة شديدة)",
                AppLanguage.EN to "Underweight"
            )
            roundedBmi < 25.0 -> mapOf(
                AppLanguage.FR to "Poids normal",
                AppLanguage.AR to "وزن طبيعي وصحي",
                AppLanguage.EN to "Normal weight"
            )
            roundedBmi < 30.0 -> mapOf(
                AppLanguage.FR to "Surpoids",
                AppLanguage.AR to "زيادة في الوزن",
                AppLanguage.EN to "Overweight"
            )
            else -> mapOf(
                AppLanguage.FR to "Obésité",
                AppLanguage.AR to "سمنة مفرطة",
                AppLanguage.EN to "Obesity"
            )
        }

        return BmiResult(roundedBmi, interpretation)
    }

    // --- Glasgow Coma Scale (GCS) ---
    data class GcsResult(
        val score: Int,
        val interpretation: Map<AppLanguage, String>
    )

    fun calculateGcs(eyes: Int, verbal: Int, motor: Int): GcsResult {
        val total = eyes + verbal + motor
        val interpretation = when {
            total >= 13 -> mapOf(
                AppLanguage.FR to "Traumatisme crânien léger / Conscience normale",
                AppLanguage.AR to "إصابة خفيفة / وعي شبه طبيعي",
                AppLanguage.EN to "Mild / Minor brain injury"
            )
            total >= 9 -> mapOf(
                AppLanguage.FR to "Traumatisme crânien modéré (Somnolence)",
                AppLanguage.AR to "إصابة متوسطة (تخليط ذهني وخمول)",
                AppLanguage.EN to "Moderate brain injury"
            )
            else -> mapOf(
                AppLanguage.FR to "Coma / Traumatisme crânien grave (Intubation recommandée)",
                AppLanguage.AR to "غيبوبة / إصابة شديدة (يوصى بتأمين مجرى الهواء)",
                AppLanguage.EN to "Severe brain injury / Coma (Intubation likely required)"
            )
        }
        return GcsResult(total, interpretation)
    }

    // --- APGAR Score ---
    data class ApgarResult(
        val score: Int,
        val interpretation: Map<AppLanguage, String>
    )

    fun calculateApgar(appearance: Int, pulse: Int, grimace: Int, activity: Int, respiration: Int): ApgarResult {
        val total = appearance + pulse + grimace + activity + respiration
        val interpretation = when {
            total >= 7 -> mapOf(
                AppLanguage.FR to "Excellente santé, nouveau-né vigoureux",
                AppLanguage.AR to "صحة ممتازة ونشاط طبيعي للمولود",
                AppLanguage.EN to "Excellent health, normal newborn transition"
            )
            total >= 4 -> mapOf(
                AppLanguage.FR to "Dépression modérée, nécessite une surveillance et stimulation",
                AppLanguage.AR to "حالة متوسطة، بحاجة لمراقبة وتحفيز بالأكسجين",
                AppLanguage.EN to "Moderately depressed, requires monitoring and stimulation"
            )
            else -> mapOf(
                AppLanguage.FR to "Urgence vitale critique, réanimation néonatale immédiate requise",
                AppLanguage.AR to "حالة حرجة جداً، يجب البدء فوراً في إنعاش حديثي الولادة",
                AppLanguage.EN to "Critical emergency, immediate neonatal resuscitation required"
            )
        }
        return ApgarResult(total, interpretation)
    }

    // --- NEWS Score (National Early Warning Score) ---
    data class NewsResult(
        val score: Int,
        val riskLevel: Map<AppLanguage, String>,
        val action: Map<AppLanguage, String>
    )

    fun calculateNews(
        respRateScore: Int,
        spo2Score: Int,
        oxSupp: Boolean,
        tempScore: Int,
        sbpScore: Int,
        hrScore: Int,
        alertScore: Int
    ): NewsResult {
        var total = respRateScore + spo2Score + tempScore + sbpScore + hrScore + alertScore
        if (oxSupp) total += 2

        val riskLevel: Map<AppLanguage, String>
        val action: Map<AppLanguage, String>

        when {
            total == 0 -> {
                riskLevel = mapOf(
                    AppLanguage.FR to "Risque faible (Score 0)",
                    AppLanguage.AR to "خطورة منخفضة جداً (0)",
                    AppLanguage.EN to "Low risk (Score 0)"
                )
                action = mapOf(
                    AppLanguage.FR to "Surveillance clinique standard.",
                    AppLanguage.AR to "المراقبة الروتينية العادية.",
                    AppLanguage.EN to "Routine ward monitoring."
                )
            }
            total in 1..4 -> {
                riskLevel = mapOf(
                    AppLanguage.FR to "Risque faible (Score 1-4)",
                    AppLanguage.AR to "خطورة منخفضة (1-4)",
                    AppLanguage.EN to "Low risk (Score 1-4)"
                )
                action = mapOf(
                    AppLanguage.FR to "Informer l'infirmier responsable. Fréquence des mesures accrue.",
                    AppLanguage.AR to "إبلاغ الممرض المسؤول بزيادة وتيرة القياسات.",
                    AppLanguage.EN to "Inform registered nurse. Increase monitoring frequency."
                )
            }
            total in 5..6 -> {
                riskLevel = mapOf(
                    AppLanguage.FR to "Risque moyen (Score 5-6 ou score de 3 sur un seul paramètre)",
                    AppLanguage.AR to "خطورة متوسطة (5-6 أو قيمة 3 في قياس واحد)",
                    AppLanguage.EN to "Medium risk (Score 5-6)"
                )
                action = mapOf(
                    AppLanguage.FR to "Évaluation urgente par l'équipe médicale.",
                    AppLanguage.AR to "تقييم عاجل ومراجعة من قبل الفريق الطبي.",
                    AppLanguage.EN to "Urgent assessment by clinical/medical team."
                )
            }
            else -> {
                riskLevel = mapOf(
                    AppLanguage.FR to "RISQUE ÉLEVÉ (Score >= 7)",
                    AppLanguage.AR to "خطورة عالية جداً (>= 7)",
                    AppLanguage.EN to "HIGH RISK (Score >= 7)"
                )
                action = mapOf(
                    AppLanguage.FR to "Alerte d'urgence immédiate ! Équipe de réanimation.",
                    AppLanguage.AR to "استدعاء فريق الطوارئ والإنعاش الطبي فوراً!",
                    AppLanguage.EN to "Immediate emergency response! Code Blue trigger."
                )
            }
        }

        return NewsResult(total, riskLevel, action)
    }

    // --- Medical Converter ---
    fun convert(value: Double, mode: String): Double {
        val raw = when (mode) {
            "kg_to_lbs" -> value * 2.20462
            "lbs_to_kg" -> value / 2.20462
            "ml_to_l" -> value / 1000.0
            "l_to_ml" -> value * 1000.0
            "c_to_f" -> value * 1.8 + 32.0
            "f_to_c" -> (value - 32.0) / 1.8
            "mmol_to_mg" -> value * 18.0182 // standard for blood glucose
            "mg_to_mmol" -> value / 18.0182
            else -> value
        }
        return (raw * 1000.0).roundToInt() / 1000.0
    }
}
