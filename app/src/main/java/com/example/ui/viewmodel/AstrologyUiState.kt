package com.example.ui.viewmodel

import com.example.astro.model.*
import com.example.data.local.BirthProfileEntity
import com.example.ui.components.ChartStyle
import com.example.util.AppLanguage

enum class AppTab(val titleKey: String) {
    CHART("tab_chart"),
    VARGAS("tab_vargas"),
    YOGAS_DASHA("tab_yogas_dasha"),
    DAILY("tab_daily"),
    CONSULT("tab_consult")
}

data class AstrologyUiState(
    val currentProfileName: String = "Self",
    val birthYear: Int = 1995,
    val birthMonth: Int = 10,
    val birthDay: Int = 24,
    val birthHour: Int = 14,
    val birthMinute: Int = 30,
    val cityName: String = "New Delhi",
    val country: String = "India",
    val latitude: Double = 28.6139,
    val longitude: Double = 77.2090,
    val timezoneOffsetHours: Double = 5.5,

    val chartData: ChartData? = null,
    val chartStyle: ChartStyle = ChartStyle.NORTH_INDIAN,
    val selectedHouse: Int? = 1,

    val activeTab: AppTab = AppTab.CHART,
    val vargaGroup: VargaGroup = VargaGroup.SHODASHA_VARGA,
    val selectedVarga: VargaType = VargaType.D1,

    val selectedPlanet: Planet = Planet.SUN,
    val currentPlanetInsight: PlanetaryInsight? = null,

    // Advanced Vedic features
    val moonProfile: MoonProfile? = null,
    val detectedYogas: List<VedicYoga> = emptyList(),
    val dashaSystem: VimshottariDashaSystem? = null,
    val aspects: List<PlanetaryAspectInfo> = emptyList(),
    val vaisheshikamsaReports: List<VaisheshikamsaReport> = emptyList(),
    val lalKitabRemedies: List<LalKitabRemedy> = emptyList(),
    val selectedRemedyPlanet: Planet = Planet.SUN,
    val selectedDashaTab: Int = 0,

    val dailyPrediction: DailyPrediction? = null,

    // AI Astrologer consultation
    val consultQuestion: String = "",
    val consultAnswer: String = "",
    val isConsultLoading: Boolean = false,
    val consultError: String? = null,

    val savedProfiles: List<BirthProfileEntity> = emptyList(),
    val isProfileEditorOpen: Boolean = false,

    // Daily Reminder Settings
    val isReminderEnabled: Boolean = true,
    val reminderHour24: Int = 8,
    val reminderMinute: Int = 0,
    val isReminderDialogOpen: Boolean = false,

    // Multi-Language Localization
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isLanguageDialogOpen: Boolean = false
)
