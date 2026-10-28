package com.example.astro.calc

import com.example.astro.model.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object TransitTimelineEngine {

    fun generateTimeline(chartData: ChartData): List<TransitTimelineItem> {
        val currentTransits = TransitEngine.getCurrentTransits()
        val ascSign = chartData.ascendantSign
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

        return currentTransits.map { transit ->
            val p = transit.planet
            val sign = transit.sign
            val deg = transit.degree.coerceIn(0.0, 29.99)

            val (stayDurationStr, dailyMotion, isRetrogradeMotion) = when (p) {
                Planet.SATURN -> Triple("~2.5 years (30 months)", 30.0 / 912.0, false)
                Planet.JUPITER -> Triple("~1 year (12 months)", 30.0 / 365.0, false)
                Planet.RAHU, Planet.KETU -> Triple("~18 months (1.5 years)", 30.0 / 547.0, true)
                Planet.MARS -> Triple("~45 days", 30.0 / 45.0, false)
                Planet.SUN -> Triple("30 days (1 month)", 30.0 / 30.0, false)
                Planet.VENUS -> Triple("~23 days", 30.0 / 23.0, false)
                Planet.MERCURY -> Triple("~21 days", 30.0 / 21.0, false)
                Planet.MOON -> Triple("~2.25 days (54 hrs)", 30.0 / 2.25, false)
                Planet.ASCENDANT -> Triple("~2 hours", 30.0 / (2.0 / 24.0), false)
            }

            val remainingDeg = if (isRetrogradeMotion) deg else (30.0 - deg)
            val daysRemaining = (remainingDeg / dailyMotion).roundToInt().coerceAtLeast(1)

            val progress = if (isRetrogradeMotion) {
                ((30.0 - deg) / 30.0).toFloat().coerceIn(0f, 1f)
            } else {
                (deg / 30.0).toFloat().coerceIn(0f, 1f)
            }

            val nextSign = if (isRetrogradeMotion) {
                ZodiacSign.fromNumber(sign.number - 1)
            } else {
                ZodiacSign.fromNumber(sign.number + 1)
            }

            val targetCal = calendar.clone() as Calendar
            targetCal.add(Calendar.DAY_OF_YEAR, daysRemaining)
            val ingressDateStr = dateFormat.format(targetCal.time)

            val upcomingHouse = ((nextSign.number - ascSign.number + 12) % 12) + 1

            val (impactTitle, impactDesc, habits) = getUpcomingHouseImpact(p, upcomingHouse, nextSign)

            TransitTimelineItem(
                planet = p,
                currentSign = sign,
                currentDegree = deg,
                progressPercent = progress,
                totalStayInSign = stayDurationStr,
                nextSign = nextSign,
                daysToNextIngress = daysRemaining,
                approximateIngressDate = ingressDateStr,
                upcomingHouseActivated = upcomingHouse,
                chartImpactTitle = impactTitle,
                chartImpactDescription = impactDesc,
                preparationHabits = habits
            )
        }.sortedBy { it.daysToNextIngress }
    }

    private fun getUpcomingHouseImpact(
        planet: Planet,
        upcomingHouse: Int,
        nextSign: ZodiacSign
    ): Triple<String, String, List<String>> {
        val title = "${planet.englishName} enters House $upcomingHouse (${nextSign.englishName})"

        val desc = when (planet) {
            Planet.SATURN -> "When Saturn shifts into your ${upcomingHouse}th house, your reality testing and karmic accountability will pivot toward ${getHouseArea(upcomingHouse)}. This starts a disciplined 2.5-year restructuring cycle."
            Planet.JUPITER -> "Jupiter's entry into House $upcomingHouse brings benevolent expansion, optimistic opportunities, and mentorship toward ${getHouseArea(upcomingHouse)}. Favorable for initiating meaningful ventures."
            Planet.RAHU -> "Rahu's transition into House $upcomingHouse stimulates intense hunger, technological curiosity, and out-of-the-box unconventional momentum in ${getHouseArea(upcomingHouse)}."
            Planet.KETU -> "Ketu moving into House $upcomingHouse invites introspective detachment, spiritual liberation, and deeper intuitive refinement regarding ${getHouseArea(upcomingHouse)}."
            Planet.MARS -> "Mars entering House $upcomingHouse injects aggressive initiative, courage, and focused execution into ${getHouseArea(upcomingHouse)}. Watch for impulsiveness."
            Planet.SUN -> "The Sun entering House $upcomingHouse illuminates ${getHouseArea(upcomingHouse)}, boosting authority, sovereign confidence, and public visibility for the next month."
            Planet.VENUS -> "Venus moving into House $upcomingHouse harmonizes ${getHouseArea(upcomingHouse)}, encouraging social diplomacy, romantic connection, artistic refinement, and aesthetic pleasure."
            Planet.MERCURY -> "Mercury brings swift commercial negotiations, intellectual curiosity, and sharp communication to ${getHouseArea(upcomingHouse)}."
            Planet.MOON -> "The Moon brings a quick 2-day emotional focus toward ${getHouseArea(upcomingHouse)}, sensitizing your moods and responsiveness."
            Planet.ASCENDANT -> "Lagna transition."
        }

        val habits = when (upcomingHouse) {
            1 -> listOf("Prioritize physical vitality and personal clarity", "Update wardrobe and project quiet presence")
            2 -> listOf("Audit savings and family communication", "Practice mindful speech and dietary moderation")
            3 -> listOf("Batch written communications and research", "Initiate courage-testing physical exercise")
            4 -> listOf("Declutter home sanctuary and improve sleep hygiene", "Carve out quiet time for inner emotional grounding")
            5 -> listOf("Dedicate time for creative hobbies and study", "Maintain ethical discipline in speculative opportunities")
            6 -> listOf("Organize daily work checklists and routines", "Prioritize gut health and preventative wellness")
            7 -> listOf("Schedule regular deep listening sessions with partner", "Ensure contractual agreements are clear and mutual")
            8 -> listOf("Practice daily meditation and psychological introspection", "Review emergency funds and shared financial holdings")
            9 -> listOf("Engage with wisdom literature or philosophical mentors", "Plan purposeful journeys or higher learning")
            10 -> listOf("Align quarterly career goals and executive milestones", "Step up to leadership duties with humility")
            11 -> listOf("Strengthen network connections with supportive peers", "Scale collaborative projects for collective impact")
            12 -> listOf("Establish a restorative screen-free evening winding down", "Practice voluntary charity or anonymous service")
            else -> listOf("Stay observant and aligned with daily routines")
        }

        return Triple(title, desc, habits)
    }

    private fun getHouseArea(house: Int): String = when (house) {
        1 -> "your personal identity, physical constitution, and leadership presence"
        2 -> "your liquid wealth, family assets, speech, and value foundations"
        3 -> "your courage, communication crafts, initiative, and siblings"
        4 -> "your home foundation, mother, real estate, and inner contentment"
        5 -> "your creative intelligence, romance, children, and higher learning"
        6 -> "your daily routines, problem-solving, overcoming competitors, and immunity"
        7 -> "your marriage, business partnerships, and diplomatic public relations"
        8 -> "your psychological depth, transformation, longevity, and esoteric research"
        9 -> "your higher dharma, philosophical beliefs, guru guidance, and fortune"
        10 -> "your professional vocation, public status, and career authority"
        11 -> "your highest aspirations, financial gains, and collective community impact"
        12 -> "your subconscious mind, spiritual surrender (Moksha), sleep, and foreign horizons"
        else -> "this house's sphere"
    }
}
