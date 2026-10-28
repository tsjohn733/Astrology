package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AstrologyAiService
import com.example.astro.calc.*
import com.example.astro.data.FamousCharts
import com.example.astro.data.LalKitabRemediesProvider
import com.example.astro.data.PlanetaryInsightProvider
import com.example.astro.data.SampleProfile
import com.example.astro.model.*
import com.example.data.local.AppDatabase
import com.example.data.local.BirthProfileEntity
import com.example.reminder.ReminderScheduler
import com.example.ui.components.ChartStyle
import com.example.util.AppLanguage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AstrologyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val profileDao = db.birthProfileDao()
    private val aiService = AstrologyAiService()
    private val prefs = application.getSharedPreferences("jyotish_settings", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(AstrologyUiState())
    val uiState: StateFlow<AstrologyUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        loadProfilesAndInitialChart()
    }

    private fun loadSettings() {
        val langCode = prefs.getString("app_language", AppLanguage.ENGLISH.code) ?: AppLanguage.ENGLISH.code
        val lang = AppLanguage.entries.firstOrNull { it.code == langCode } ?: AppLanguage.ENGLISH
        val reminderEnabled = prefs.getBoolean("reminder_enabled", true)
        val reminderH = prefs.getInt("reminder_hour", 8)
        val reminderM = prefs.getInt("reminder_minute", 0)

        _uiState.update {
            it.copy(
                currentLanguage = lang,
                isReminderEnabled = reminderEnabled,
                reminderHour24 = reminderH,
                reminderMinute = reminderM
            )
        }

        if (reminderEnabled) {
            ReminderScheduler.scheduleDailyReminder(getApplication(), reminderH, reminderM)
        }
    }

    private fun loadProfilesAndInitialChart() {
        viewModelScope.launch {
            profileDao.getAllProfiles().collect { list ->
                if (list.isEmpty()) {
                    // Seed default profile
                    val defaultProfile = BirthProfileEntity(
                        name = "Seeker (You)",
                        year = 1995,
                        month = 10,
                        day = 24,
                        hour = 14,
                        minute = 30,
                        cityName = "New Delhi",
                        country = "India",
                        latitude = 28.6139,
                        longitude = 77.2090,
                        timezoneOffsetHours = 5.5,
                        isPrimary = true,
                        notes = "Your personal birth chart"
                    )
                    profileDao.insertProfile(defaultProfile)
                } else {
                    _uiState.update { it.copy(savedProfiles = list) }
                    val active = list.firstOrNull { it.isPrimary } ?: list.first()
                    loadProfileData(active)
                }
            }
        }
    }

    private fun loadProfileData(profile: BirthProfileEntity) {
        val chart = EphemerisCalculator.calculateChart(
            year = profile.year,
            month = profile.month,
            day = profile.day,
            hour = profile.hour,
            minute = profile.minute,
            latitude = profile.latitude,
            longitude = profile.longitude,
            timezoneOffsetHours = profile.timezoneOffsetHours
        )

        val daily = TransitEngine.generateDailyPrediction(chart)
        val initialPlanet = chart.planets.firstOrNull()?.planet ?: Planet.SUN
        val planetPos = chart.getPlanet(initialPlanet)
        val initialInsight = planetPos?.let { PlanetaryInsightProvider.getInsightForPlanet(it) }

        // Advanced Vedic calculations
        val moonProf = MoonPsychologyProvider.generateMoonProfile(chart)
        val yogas = YogaEngine.detectYogas(chart)
        val dasha = VimshottariDashaEngine.calculateDashaSystem(
            chartData = chart,
            birthYear = profile.year,
            birthMonth = profile.month,
            birthDay = profile.day,
            birthHour = profile.hour,
            birthMinute = profile.minute
        )
        val aspects = AspectEngine.calculateAspects(chart)
        val vaisheshikamsa = VaisheshikamsaEngine.calculateVaisheshikamsa(chart)
        val remedies = LalKitabRemediesProvider.getRemediesForPlanet(initialPlanet)

        _uiState.update {
            it.copy(
                currentProfileName = profile.name,
                birthYear = profile.year,
                birthMonth = profile.month,
                birthDay = profile.day,
                birthHour = profile.hour,
                birthMinute = profile.minute,
                cityName = profile.cityName,
                country = profile.country,
                latitude = profile.latitude,
                longitude = profile.longitude,
                timezoneOffsetHours = profile.timezoneOffsetHours,
                chartData = chart,
                dailyPrediction = daily,
                selectedPlanet = initialPlanet,
                currentPlanetInsight = initialInsight,
                moonProfile = moonProf,
                detectedYogas = yogas,
                dashaSystem = dasha,
                aspects = aspects,
                vaisheshikamsaReports = vaisheshikamsa,
                lalKitabRemedies = remedies,
                selectedRemedyPlanet = initialPlanet
            )
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun toggleChartStyle() {
        _uiState.update {
            val newStyle = if (it.chartStyle == ChartStyle.NORTH_INDIAN) {
                ChartStyle.SOUTH_INDIAN
            } else {
                ChartStyle.NORTH_INDIAN
            }
            it.copy(chartStyle = newStyle)
        }
    }

    fun selectHouse(house: Int) {
        _uiState.update { it.copy(selectedHouse = house) }
    }

    fun selectVargaGroup(group: VargaGroup) {
        _uiState.update { it.copy(vargaGroup = group) }
    }

    fun selectVarga(varga: VargaType) {
        _uiState.update { it.copy(selectedVarga = varga) }
    }

    fun selectPlanet(planet: Planet) {
        val chart = _uiState.value.chartData ?: return
        val pos = chart.getPlanet(planet) ?: return
        val insight = PlanetaryInsightProvider.getInsightForPlanet(pos)
        _uiState.update {
            it.copy(
                selectedPlanet = planet,
                currentPlanetInsight = insight
            )
        }
    }

    fun selectRemedyPlanet(planet: Planet) {
        val rems = LalKitabRemediesProvider.getRemediesForPlanet(planet)
        _uiState.update {
            it.copy(
                selectedRemedyPlanet = planet,
                lalKitabRemedies = rems
            )
        }
    }

    fun selectDashaTab(index: Int) {
        _uiState.update { it.copy(selectedDashaTab = index) }
    }

    fun updateConsultQuestion(question: String) {
        _uiState.update { it.copy(consultQuestion = question) }
    }

    fun askAstrologer(customQuestion: String? = null) {
        val question = customQuestion ?: _uiState.value.consultQuestion.trim()
        if (question.isBlank()) return

        val chart = _uiState.value.chartData ?: return
        val profileName = _uiState.value.currentProfileName
        val transitsSummary = _uiState.value.dailyPrediction?.transitsSummary ?: ""

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isConsultLoading = true,
                    consultAnswer = "",
                    consultError = null,
                    consultQuestion = question
                )
            }

            try {
                aiService.answerAstrologyQuestionStream(
                    question = question,
                    chart = chart,
                    profileName = profileName,
                    transitsSummary = transitsSummary
                ).collect { chunk ->
                    _uiState.update {
                        it.copy(consultAnswer = it.consultAnswer + chunk)
                    }
                }
                _uiState.update { it.copy(isConsultLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isConsultLoading = false,
                        consultError = e.localizedMessage ?: "Consultation query failed."
                    )
                }
            }
        }
    }

    fun setProfileEditorOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isProfileEditorOpen = isOpen) }
    }

    fun setReminderDialogOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isReminderDialogOpen = isOpen) }
    }

    fun setLanguageDialogOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isLanguageDialogOpen = isOpen) }
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString("app_language", language.code).apply()
        _uiState.update { it.copy(currentLanguage = language) }
    }

    fun saveReminderSettings(enabled: Boolean, hour24: Int, minute: Int) {
        prefs.edit()
            .putBoolean("reminder_enabled", enabled)
            .putInt("reminder_hour", hour24)
            .putInt("reminder_minute", minute)
            .apply()

        _uiState.update {
            it.copy(
                isReminderEnabled = enabled,
                reminderHour24 = hour24,
                reminderMinute = minute
            )
        }

        if (enabled) {
            val tip = _uiState.value.dailyPrediction?.tipOfTheDay
                ?: "Check today's active planetary transits and customized chart forecasts."
            ReminderScheduler.scheduleDailyReminder(getApplication(), hour24, minute, tip)
        } else {
            ReminderScheduler.cancelDailyReminder(getApplication())
        }
    }

    fun triggerTestNotification() {
        val tip = _uiState.value.dailyPrediction?.tipOfTheDay
            ?: "Align your high-leverage tasks before noon; planetary discipline is at peak support."
        ReminderScheduler.triggerTestNotification(getApplication(), tip)
    }

    fun saveNewProfile(
        name: String,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        cityName: String,
        country: String,
        latitude: Double,
        longitude: Double,
        timezoneOffsetHours: Double,
        isPrimary: Boolean = true
    ) {
        viewModelScope.launch {
            if (isPrimary) {
                profileDao.clearPrimaryFlags()
            }
            val entity = BirthProfileEntity(
                name = name,
                year = year,
                month = month,
                day = day,
                hour = hour,
                minute = minute,
                cityName = cityName,
                country = country,
                latitude = latitude,
                longitude = longitude,
                timezoneOffsetHours = timezoneOffsetHours,
                isPrimary = isPrimary
            )
            val id = profileDao.insertProfile(entity)
            if (isPrimary) {
                profileDao.setPrimaryFlag(id)
            }
            loadProfileData(entity.copy(id = id))
            setProfileEditorOpen(false)
        }
    }

    fun switchProfile(profile: BirthProfileEntity) {
        viewModelScope.launch {
            profileDao.setAsPrimary(profile.id)
            loadProfileData(profile.copy(isPrimary = true))
        }
    }

    fun loadFamousProfile(sample: SampleProfile) {
        saveNewProfile(
            name = sample.name,
            year = sample.year,
            month = sample.month,
            day = sample.day,
            hour = sample.hour,
            minute = sample.minute,
            cityName = sample.cityName,
            country = sample.country,
            latitude = sample.latitude,
            longitude = sample.longitude,
            timezoneOffsetHours = sample.timezoneOffsetHours,
            isPrimary = true
        )
    }
}
