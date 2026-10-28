package com.example

import com.example.astro.calc.EphemerisCalculator
import com.example.astro.calc.TransitEngine
import com.example.astro.calc.TransitTimelineEngine
import com.example.astro.calc.VargaCalculator
import com.example.astro.data.CityDatabase
import com.example.astro.data.PlanetaryInsightProvider
import com.example.astro.model.*
import com.example.ui.screens.convert12To24
import com.example.ui.screens.format24To12
import com.example.util.AppLanguage
import com.example.util.LocalizationManager
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testLahiriAyanamsha() {
        val jd = EphemerisCalculator.calculateJulianDay(2000, 1, 1, 12, 0, 0.0)
        val ayanamsha = EphemerisCalculator.calculateLahiriAyanamsha(jd)
        assertTrue("Ayanamsha should be around 23.85 degrees in 2000", ayanamsha in 23.5..24.2)
    }

    @Test
    fun testChartCalculation() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        assertNotNull(chart)
        assertEquals(9, chart.planets.size) // 7 planets + Rahu + Ketu
        assertTrue("Ascendant degree in 0..360", chart.ascendantDegree in 0.0..360.0)

        chart.planets.forEach { p ->
            assertTrue("House must be 1 to 12", p.house in 1..12)
            assertTrue("Pada must be 1 to 4", p.pada in 1..4)
            assertTrue("Degree in sign must be 0 to 30", p.degreeInSign in 0.0..30.0)
            assertTrue("Nakshatra number 1 to 27", p.nakshatra.number in 1..27)
        }
    }

    @Test
    fun testShodashvargaCalculations() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        // All 16 divisional charts must be present
        assertEquals(16, chart.vargaCharts.size)
        for (varga in VargaType.entries) {
            val vargaChart = chart.vargaCharts[varga]
            assertNotNull("Varga chart $varga must be computed", vargaChart)
            assertEquals("Varga $varga must place all 9 planets", 9, vargaChart?.placements?.size)
        }

        // Test Saptavarga filter
        val saptavarga = VargaType.entries.filter { it.inSaptavarga }
        assertEquals(7, saptavarga.size)

        // Test Dashavarga filter
        val dashavarga = VargaType.entries.filter { it.inDashavarga }
        assertEquals(10, dashavarga.size)
    }

    @Test
    fun testPlanetaryInsightsAndRemedies() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        chart.planets.forEach { planetPos ->
            val insight = PlanetaryInsightProvider.getInsightForPlanet(planetPos)
            assertTrue("Root why must be populated", insight.rootWhy.isNotBlank())
            assertTrue("Benefits must exist", insight.giftsAndBenefits.isNotEmpty())
            assertTrue("Precautions must exist", insight.precautionsAndBlindSpots.isNotEmpty())
            assertTrue("Practical lifestyle habits must exist", insight.practicalLifestyleHabits.isNotEmpty())
        }
    }

    @Test
    fun testDailyTransitPredictions() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        val prediction = TransitEngine.generateDailyPrediction(chart)
        assertTrue(prediction.loveInsight.isNotBlank())
        assertTrue(prediction.careerInsight.isNotBlank())
        assertTrue(prediction.healthInsight.isNotBlank())
        assertTrue(prediction.transitsSummary.isNotBlank())
        assertTrue(prediction.tipOfTheDay.isNotBlank())
    }

    @Test
    fun testCitySearchSuggestions() {
        val muResults = CityDatabase.searchCities("Mu")
        assertTrue("Results should not be empty", muResults.isNotEmpty())
        assertTrue("Should contain Mumbai", muResults.any { it.name.contains("Mumbai", ignoreCase = true) })
        assertTrue("Should contain Munich", muResults.any { it.name.contains("Munich", ignoreCase = true) })

        val banResults = CityDatabase.searchCities("Ban")
        assertTrue("Should contain Bengaluru/Bangalore", banResults.any { it.name.contains("Bengaluru", ignoreCase = true) })
        assertTrue("Should contain Bangkok", banResults.any { it.name.contains("Bangkok", ignoreCase = true) })

        val delResults = CityDatabase.searchCities("Del")
        assertTrue("Should contain New Delhi", delResults.any { it.name.contains("Delhi", ignoreCase = true) })
    }

    @Test
    fun testAmPmTimeConversion() {
        assertEquals(0, convert12To24(12, false))
        assertEquals(12, convert12To24(12, true))
        assertEquals(9, convert12To24(9, false))
        assertEquals(14, convert12To24(2, true))
        assertEquals(23, convert12To24(11, true))

        assertEquals("02:30 PM", format24To12(14, 30))
        assertEquals("12:00 AM", format24To12(0, 0))
        assertEquals("12:15 PM", format24To12(12, 15))
        assertEquals("09:45 AM", format24To12(9, 45))
    }

    @Test
    fun testTransitTimelineAndIngress() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        val timeline = TransitTimelineEngine.generateTimeline(chart)
        assertTrue("Timeline should contain transits", timeline.isNotEmpty())

        timeline.forEach { item ->
            assertTrue("Days to next ingress should be positive", item.daysToNextIngress > 0)
            assertTrue("Progress should be between 0 and 1", item.progressPercent in 0f..1f)
            assertTrue("Upcoming house must be 1 to 12", item.upcomingHouseActivated in 1..12)
            assertTrue("Chart impact title must be non-empty", item.chartImpactTitle.isNotBlank())
            assertTrue("Preparation habits must be populated", item.preparationHabits.isNotEmpty())
        }
    }

    @Test
    fun testLocalizationManager() {
        for (lang in AppLanguage.entries) {
            val appName = LocalizationManager.getString("app_name", lang)
            val tabChart = LocalizationManager.getString("tab_chart", lang)
            val gocharTitle = LocalizationManager.getString("transit_timeline_title", lang)

            assertTrue("App name in $lang should not be blank", appName.isNotBlank())
            assertTrue("Tab chart in $lang should not be blank", tabChart.isNotBlank())
            assertTrue("Gochar title in $lang should not be blank", gocharTitle.isNotBlank())
        }
    }

    @Test
    fun testMoonPsychologicalProfile() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        val moonProfile = com.example.astro.calc.MoonPsychologyProvider.generateMoonProfile(chart)
        assertNotNull(moonProfile)
        val profile = moonProfile!!
        assertTrue("Emotional nature should not be blank", profile.emotionalNature.isNotBlank())
        assertTrue("Security needs must be detailed", profile.instinctualSecurityNeeds.isNotBlank())
        assertTrue("Stress response must be non-empty", profile.stressResponse.isNotBlank())
        assertTrue("Mental strengths must be present", profile.mentalStrengths.isNotEmpty())
        assertTrue("Emotional blind spots must be present", profile.emotionalBlindSpots.isNotEmpty())
    }

    @Test
    fun testVedicYogaDetection() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        val yogas = com.example.astro.calc.YogaEngine.detectYogas(chart)
        assertNotNull(yogas)
        assertTrue("Yogas should be detected", yogas.isNotEmpty())
        yogas.forEach { y ->
            assertTrue("Name must be non-empty", y.name.isNotBlank())
            assertTrue("Category must be non-empty", y.category.isNotBlank())
            assertTrue("Description must be non-empty", y.description.isNotBlank())
        }
    }

    @Test
    fun testVimshottariDashaCalculations() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        val dasha = com.example.astro.calc.VimshottariDashaEngine.calculateDashaSystem(
            chartData = chart,
            birthYear = 1995,
            birthMonth = 10,
            birthDay = 24
        )
        assertNotNull(dasha)
        assertEquals("Must generate all 9 Mahadashas", 9, dasha.mahadashas.size)
        assertTrue("Active Mahadasha should be identified", dasha.activeMahadasha != null)
        assertTrue("Active Antardasha should be identified", dasha.currentActiveAntardasha != null)
        assertEquals("Active Mahadasha should have 9 Antardashas", 9, dasha.activeAntardashas.size)
    }

    @Test
    fun testPlanetaryAspects() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        val aspects = com.example.astro.calc.AspectEngine.calculateAspects(chart)
        assertEquals("Must calculate aspects for all 9 planets", 9, aspects.size)
        aspects.forEach { asp ->
            assertTrue("Must aspect at least 1 house", asp.aspectedHouses.isNotEmpty())
            assertTrue("Summary must be non-empty", asp.aspectSummary.isNotBlank())
            assertTrue("Astrological result must be non-empty", asp.astrologicalResult.isNotBlank())
        }
    }

    @Test
    fun testVaisheshikamsaDignityTiers() {
        val chart = EphemerisCalculator.calculateChart(
            year = 1995,
            month = 10,
            day = 24,
            hour = 14,
            minute = 30,
            latitude = 28.6139,
            longitude = 77.2090,
            timezoneOffsetHours = 5.5
        )

        val reports = com.example.astro.calc.VaisheshikamsaEngine.calculateVaisheshikamsa(chart)
        assertEquals("Must analyze all 9 planets", 9, reports.size)
        reports.forEach { rep ->
            assertTrue("Shodashvarga count in 0..16", rep.shodashvargaCount in 0..16)
            assertTrue("Dashavarga count in 0..10", rep.dashavargaCount in 0..10)
            assertTrue("Meaning must be non-empty", rep.nameMeaning.isNotBlank())
            assertTrue("Bestowed powers must be non-empty", rep.bestowedPowers.isNotBlank())
            assertTrue("Advice must be non-empty", rep.optimizationAdvice.isNotBlank())
        }
    }

    @Test
    fun testLalKitabRemedies() {
        Planet.entries.forEach { planet ->
            val remedies = com.example.astro.data.LalKitabRemediesProvider.getRemediesForPlanet(planet)
            assertTrue("Must have remedies for $planet", remedies.isNotEmpty())
            remedies.forEach { r ->
                assertTrue("Title must be present", r.title.isNotBlank())
                assertTrue("Remedy action must be present", r.remedyAction.isNotBlank())
                assertTrue("Purpose must be present", r.purpose.isNotBlank())
                assertTrue("Precautions must be present", r.precautions.isNotBlank())
            }
        }
    }
}
