package com.example.astro.calc

import com.example.astro.model.*
import java.text.SimpleDateFormat
import java.util.*

object TransitEngine {

    data class TransitPosition(
        val planet: Planet,
        val sign: ZodiacSign,
        val degree: Double,
        val isRetrograde: Boolean
    )

    fun getCurrentTransits(): List<TransitPosition> {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val jd = EphemerisCalculator.calculateJulianDay(year, month, day, hour, minute, 0.0)
        val chart = EphemerisCalculator.calculateChart(year, month, day, hour, minute, 0.0, 0.0, 0.0)

        return chart.planets.map {
            TransitPosition(it.planet, it.sign, it.degreeInSign, it.isRetrograde)
        }
    }

    fun generateDailyPrediction(
        chart: ChartData
    ): DailyPrediction {
        val transits = getCurrentTransits()
        val moonSign = chart.getPlanet(Planet.MOON)?.sign ?: chart.ascendantSign
        val ascSign = chart.ascendantSign

        val saturnTransit = transits.firstOrNull { it.planet == Planet.SATURN }
        val jupiterTransit = transits.firstOrNull { it.planet == Planet.JUPITER }
        val moonTransit = transits.firstOrNull { it.planet == Planet.MOON }
        val venusTransit = transits.firstOrNull { it.planet == Planet.VENUS }
        val marsTransit = transits.firstOrNull { it.planet == Planet.MARS }

        // House from Moon (Chandra Lagna)
        val jupiterHouseFromMoon = if (jupiterTransit != null) {
            ((jupiterTransit.sign.number - moonSign.number + 12) % 12) + 1
        } else 1

        val saturnHouseFromMoon = if (saturnTransit != null) {
            ((saturnTransit.sign.number - moonSign.number + 12) % 12) + 1
        } else 1

        val moonHouseFromMoon = if (moonTransit != null) {
            ((moonTransit.sign.number - moonSign.number + 12) % 12) + 1
        } else 1

        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        val dateText = dateFormat.format(Date())

        val loveText = buildString {
            when {
                jupiterHouseFromMoon in listOf(5, 7, 9, 11) -> {
                    append("Auspicious planetary harmony graces your relationships today. ")
                    append("With transiting Jupiter activating an expansive trine/angle (${jupiterHouseFromMoon}th from Moon in ${jupiterTransit?.sign?.englishName}), empathy flows effortlessly. Great day for heartfelt vulnerability.")
                }
                venusTransit?.isRetrograde == true -> {
                    append("Venus prompts introspection in emotional dynamics. Re-examine unspoken expectations rather than making reactive commitments.")
                }
                else -> {
                    append("The Moon transiting through your ${moonHouseFromMoon}th house indicates an introspective mood. Focus on mutual listening, open dialogue, and quality shared stillness.")
                }
            }
        }

        val careerText = buildString {
            when {
                saturnHouseFromMoon in listOf(3, 6, 10, 11) -> {
                    append("Strong structural discipline supports your ambitions. ")
                    append("Saturn's Upachaya transit (${saturnHouseFromMoon}th from natal Moon in ${saturnTransit?.sign?.englishName}) rewards grit, methodical project execution, and strategic persistence.")
                }
                saturnHouseFromMoon in listOf(1, 2, 12) -> {
                    append("Saturn asks for patient endurance and meticulous budget/time management. Prioritize completing pending obligations over launching unvetted ventures.")
                }
                else -> {
                    append("Mars in ${marsTransit?.sign?.englishName} sharpens your decisive edge. Direct this momentum into solving complex bottlenecks rather than friction with colleagues.")
                }
            }
        }

        val healthText = buildString {
            when {
                moonHouseFromMoon in listOf(6, 8, 12) -> {
                    append("The lunar transit touches a sensitive house (${moonHouseFromMoon}th). Prioritize hydration, restorative sleep, and calming pranayama. Avoid digestive overloads.")
                }
                marsTransit?.isRetrograde == true -> {
                    append("Watch for nervous restlessness or inflammatory spikes. Balance physical exertion with somatic grounding and gentle stretching.")
                }
                else -> {
                    append("Vitality is balanced. Maintain steady circadian meal timings and take short walking breaks between screen sessions to clear your mental bandwidth.")
                }
            }
        }

        val summary = "Transiting Saturn is stationed in ${saturnTransit?.sign?.englishName} (${saturnHouseFromMoon}th from natal Moon), and Jupiter illuminates ${jupiterTransit?.sign?.englishName} (${jupiterHouseFromMoon}th from Moon). Today's celestial transits emphasize sustained diligence and emotional clarity."

        val tip = when {
            saturnHouseFromMoon in listOf(10, 11) -> "Align your high-leverage tasks before noon; planetary discipline is at peak support."
            jupiterHouseFromMoon in listOf(5, 9) -> "Follow intuitive insights and engage in mentorship or philosophical study today."
            moonHouseFromMoon in listOf(4, 7) -> "Make time for genuine conversation with family or partner; shared meals heal tension."
            else -> "Take 10 conscious, deep breaths before responding in tense meetings to harness your planetary center."
        }

        return DailyPrediction(
            dateText = dateText,
            loveInsight = loveText,
            careerInsight = careerText,
            healthInsight = healthText,
            transitsSummary = summary,
            tipOfTheDay = tip
        )
    }
}
