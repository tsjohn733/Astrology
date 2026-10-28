package com.example.astro.model

data class MoonProfile(
    val sign: ZodiacSign,
    val degreeInSign: Double,
    val nakshatra: Nakshatra,
    val pada: Int,
    val emotionalNature: String,
    val psychologicalSignature: String,
    val instinctualSecurityNeeds: String,
    val stressResponse: String,
    val mentalStrengths: List<String>,
    val emotionalBlindSpots: List<String>
)

data class VedicYoga(
    val id: String,
    val name: String,
    val sanskritName: String,
    val category: String, // Pancha Mahapurusha, Raja Yoga, Dhana Yoga, Vipareeta, Dosha
    val triggeringPlanets: List<Planet>,
    val description: String,
    val benefits: List<String>,
    val disadvantages: List<String>,
    val isActive: Boolean = true
)

data class DashaPeriod(
    val planet: Planet,
    val level: String, // "Mahadasha" or "Antardasha"
    val startDateFormatted: String,
    val endDateFormatted: String,
    val startMillis: Long,
    val endMillis: Long,
    val isActive: Boolean,
    val generalTheme: String,
    val potentialGains: List<String>,
    val riskOfLosses: List<String>,
    val whatToDo: List<String>,
    val whatToAvoid: List<String>
)

data class VimshottariDashaSystem(
    val startingLord: Planet,
    val balanceYearsAtBirth: Double,
    val mahadashas: List<DashaPeriod>,
    val activeMahadasha: DashaPeriod?,
    val activeAntardashas: List<DashaPeriod>,
    val currentActiveAntardasha: DashaPeriod?
)

data class PlanetaryAspectInfo(
    val planet: Planet,
    val aspectedHouses: List<Int>,
    val aspectedPlanets: List<Planet>,
    val aspectSummary: String,
    val astrologicalResult: String
)

data class VaisheshikamsaReport(
    val planet: Planet,
    val shodashvargaCount: Int,
    val shodashvargaTier: String?,
    val dashavargaCount: Int,
    val dashavargaTier: String?,
    val nameMeaning: String,
    val bestowedPowers: String,
    val optimizationAdvice: String
)

data class LalKitabRemedy(
    val planet: Planet,
    val house: Int,
    val title: String,
    val remedyAction: String,
    val purpose: String,
    val precautions: String
)
