package com.example.astro.calc

import com.example.astro.model.*
import java.text.SimpleDateFormat
import java.util.*

object VimshottariDashaEngine {

    val DASHA_ORDER = listOf(
        Planet.KETU to 7.0,
        Planet.VENUS to 20.0,
        Planet.SUN to 6.0,
        Planet.MOON to 10.0,
        Planet.MARS to 7.0,
        Planet.RAHU to 18.0,
        Planet.JUPITER to 16.0,
        Planet.SATURN to 19.0,
        Planet.MERCURY to 17.0
    )

    fun calculateDashaSystem(
        chartData: ChartData,
        birthYear: Int,
        birthMonth: Int,
        birthDay: Int,
        birthHour: Int = 12,
        birthMinute: Int = 0
    ): VimshottariDashaSystem {
        val moonPos = chartData.getPlanet(Planet.MOON)
        val moonLong = moonPos?.longitude ?: 0.0

        val nakshatraSpan = 360.0 / 27.0 // 13.3333333°
        val nakshatraIndex = (moonLong / nakshatraSpan).toInt().coerceIn(0, 26)
        val degInNakshatra = moonLong % nakshatraSpan
        val fractionTraversed = degInNakshatra / nakshatraSpan
        val fractionRemaining = (1.0 - fractionTraversed).coerceIn(0.01, 0.99)

        val startingLord = EphemerisCalculator.NAKSHATRAS[nakshatraIndex].lord
        val startingIndex = DASHA_ORDER.indexOfFirst { it.first == startingLord }.let { if (it == -1) 0 else it }

        val (firstLord, totalYears) = DASHA_ORDER[startingIndex]
        val balanceYearsAtBirth = totalYears * fractionRemaining

        val birthCal = Calendar.getInstance().apply {
            set(birthYear, birthMonth - 1, birthDay, birthHour, birthMinute, 0)
        }

        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val nowMillis = System.currentTimeMillis()

        val mahadashas = mutableListOf<DashaPeriod>()
        var currentStartCal = birthCal.clone() as Calendar

        // Calculate all 9 Mahadashas
        for (i in 0 until 9) {
            val idx = (startingIndex + i) % 9
            val (lord, lordTotalYears) = DASHA_ORDER[idx]
            val yearsForThisPeriod = if (i == 0) balanceYearsAtBirth else lordTotalYears

            val endCal = currentStartCal.clone() as Calendar
            val days = (yearsForThisPeriod * 365.25).toInt()
            endCal.add(Calendar.DAY_OF_YEAR, days)

            val startMillis = currentStartCal.timeInMillis
            val endMillis = endCal.timeInMillis
            val isActive = nowMillis in startMillis..endMillis

            val theme = getDashaTheme(lord, "Mahadasha")
            val (gains, losses, doList, avoidList) = getDashaGuidance(lord)

            mahadashas.add(
                DashaPeriod(
                    planet = lord,
                    level = "Mahadasha",
                    startDateFormatted = dateFormat.format(currentStartCal.time),
                    endDateFormatted = dateFormat.format(endCal.time),
                    startMillis = startMillis,
                    endMillis = endMillis,
                    isActive = isActive,
                    generalTheme = theme,
                    potentialGains = gains,
                    riskOfLosses = losses,
                    whatToDo = doList,
                    whatToAvoid = avoidList
                )
            )

            currentStartCal = endCal
        }

        val activeMahadasha = mahadashas.firstOrNull { it.isActive } ?: mahadashas.firstOrNull()

        // Calculate Antardashas for the active Mahadasha
        val activeAntardashas = mutableListOf<DashaPeriod>()
        var currentActiveAntardasha: DashaPeriod? = null

        if (activeMahadasha != null) {
            val mLord = activeMahadasha.planet
            val mIndex = DASHA_ORDER.indexOfFirst { it.first == mLord }.let { if (it == -1) 0 else it }
            val mTotalYears = DASHA_ORDER[mIndex].second

            val antardashaStartCal = Calendar.getInstance().apply {
                timeInMillis = activeMahadasha.startMillis
            }

            for (j in 0 until 9) {
                val aIdx = (mIndex + j) % 9
                val (aLord, aTotalYears) = DASHA_ORDER[aIdx]
                val aYears = (mTotalYears * aTotalYears) / 120.0
                val aDays = (aYears * 365.25).toInt()

                val aEndCal = antardashaStartCal.clone() as Calendar
                aEndCal.add(Calendar.DAY_OF_YEAR, aDays)

                val aStartMillis = antardashaStartCal.timeInMillis
                val aEndMillis = aEndCal.timeInMillis
                val isAntarActive = nowMillis in aStartMillis..aEndMillis

                val aTheme = "${mLord.englishName} Mahadasha • ${aLord.englishName} Antardasha"
                val (aGains, aLosses, aDoList, aAvoidList) = getDashaGuidance(aLord)

                val aPeriod = DashaPeriod(
                    planet = aLord,
                    level = "Antardasha",
                    startDateFormatted = dateFormat.format(antardashaStartCal.time),
                    endDateFormatted = dateFormat.format(aEndCal.time),
                    startMillis = aStartMillis,
                    endMillis = aEndMillis,
                    isActive = isAntarActive,
                    generalTheme = aTheme,
                    potentialGains = aGains,
                    riskOfLosses = aLosses,
                    whatToDo = aDoList,
                    whatToAvoid = aAvoidList
                )

                activeAntardashas.add(aPeriod)
                if (isAntarActive) {
                    currentActiveAntardasha = aPeriod
                }

                antardashaStartCal.timeInMillis = aEndMillis
            }
        }

        return VimshottariDashaSystem(
            startingLord = startingLord,
            balanceYearsAtBirth = balanceYearsAtBirth,
            mahadashas = mahadashas,
            activeMahadasha = activeMahadasha,
            activeAntardashas = activeAntardashas,
            currentActiveAntardasha = currentActiveAntardasha ?: activeAntardashas.firstOrNull()
        )
    }

    private fun getDashaTheme(planet: Planet, level: String): String = when (planet) {
        Planet.SUN -> "Soul realization, authority elevation, government interfaces, and establishing independent sovereignty."
        Planet.MOON -> "Emotional fluctuations, home and maternal themes, public popularity, relational sensitivity, and mental peace."
        Planet.MARS -> "Dynamic kinetic drive, decisive action, property acquisitions, courage, competition, and leadership initiatives."
        Planet.MERCURY -> "Commercial trade, intellectual writing, linguistic eloquence, rapid business transactions, and educational milestones."
        Planet.JUPITER -> "Spiritual expansion, guru blessing, financial prosperity, ethical mentoring, family births, and higher philosophical wisdom."
        Planet.VENUS -> "Sensory refinement, luxury, marital harmony, artistic creativity, vehicle acquisition, and diplomatic negotiations."
        Planet.SATURN -> "Karmic discipline, restructuring foundations, perseverance under delayed rewards, organizational endurance, and profound maturity."
        Planet.RAHU -> "Ambitious material hunger, foreign travel, unconventional disruption, technological leaps, and breaking traditional molds."
        Planet.KETU -> "Spiritual detachment, intuitive breakthroughs, philosophical re-evaluation, liberation from past ties, and inward meditation."
        Planet.ASCENDANT -> "Primary life orientation."
    }

    private fun getDashaGuidance(planet: Planet): FourTuple = when (planet) {
        Planet.SUN -> FourTuple(
            listOf("Public recognition and honor", "Clarity of personal life purpose", "Strengthened physical vitality"),
            listOf("Friction with authority or father figures", "Ego-driven isolation", "High blood pressure / eye fatigue"),
            listOf("Spend 15 minutes in morning sunlight", "Step into leadership responsibilities", "Offer water to Sun (Surya Arghya)"),
            listOf("Do not domineer colleagues", "Avoid stubborn false pride", "Never break promises made to elders")
        )
        Planet.MOON -> FourTuple(
            listOf("Deep emotional empathy and peace", "Successful public-facing endeavors", "Comfortable domestic life"),
            listOf("Mood swings and depressive dips", "Over-attachment to nostalgic memories", "Water-retention or lymphatic sluggishness"),
            listOf("Maintain regular hydration and sleep cycles", "Spend time near water bodies", "Practice evening journaling"),
            listOf("Do not make permanent decisions during emotional lows", "Avoid cold damp environments", "Do not harbor resentment")
        )
        Planet.MARS -> FourTuple(
            listOf("Triumph over competitors", "Successful real estate investments", "High physical stamina and boldness"),
            listOf("Sudden impulsive disputes and lawsuits", "Accidental cuts, burns, or inflammatory spikes", "Friction with siblings"),
            listOf("Channel fiery energy into vigorous strength training", "Practice cooling breathwork (Sheetali)", "Donate blood voluntarily"),
            listOf("Do not drive when enraged", "Avoid hasty ultimatums in partnerships", "Never leave sharp tools disorganized")
        )
        Planet.MERCURY -> FourTuple(
            listOf("Lucrative commercial trade and contracts", "High academic and linguistic achievements", "Sharpened wit and analytical skill"),
            listOf("Mental anxiety and insomnia from overthinking", "Scattered multitasking without finish", "Nervous exhaustion"),
            listOf("Single-task in 90-minute deep work blocks", "Practice green dietary habits (leafy greens)", "Honor commercial agreements"),
            listOf("Do not gossip or engage in double-talk", "Avoid compulsive phone screen scrolling", "Do not overcommit verbally")
        )
        Planet.JUPITER -> FourTuple(
            listOf("Substantial wealth and treasury expansion", "Children, progeny, and legacy blessings", "Honorable reputation and wisdom"),
            listOf("Over-optimistic financial over-extension", "Liver or metabolic sluggishness from rich diets", "Self-righteous preaching"),
            listOf("Read philosophical or sacred literature daily", "Offer mentorship to novices", "Maintain fasting on Thursdays"),
            listOf("Do not indulge in excessive sweets or heavy oils", "Avoid disrespecting teachers or mentors", "Never take unethical shortcuts")
        )
        Planet.VENUS -> FourTuple(
            listOf("Aesthetic luxuries, fine vehicles, and home comforts", "Deep romance and marital bonding", "Creative design mastery"),
            listOf("Extravagant overspending on superficial trinkets", "Compromising personal values for temporary peace", "Hormonal imbalances"),
            listOf("Curate clean, harmonious living spaces", "Wear clean, elegant attire", "Practice transparent communication with partner"),
            listOf("Do not use retail therapy to numb emotional voids", "Avoid conflict avoidance that lets resentment fester", "Never betray mutual trust")
        )
        Planet.SATURN -> FourTuple(
            listOf("Indestructible long-term foundations", "Massive respect earned through diligence", "Spiritual endurance and stoic peace"),
            listOf("Chronic delays, heavy labor, and feeling unappreciated", "Joint, bone, or Vata ailments", "Cynicism and isolation"),
            listOf("Wake and sleep at identical times daily", "Serve the underprivileged and stray animals", "Perform warm sesame oil massage on feet"),
            listOf("Do not procrastinate on legal or civic duties", "Avoid self-pity and blaming fate", "Never mistreat laborers or subordinates")
        )
        Planet.RAHU -> FourTuple(
            listOf("Sudden windfall gains and foreign horizons", "Pioneering technological breakthroughs", "High ambition and status leap"),
            listOf("Obsessive illusions and speculative crashes", "Sudden scandals, toxic deception, or anxiety", "Addiction or escapism"),
            listOf("Practice 10 minutes of alternate nostril pranayama", "Enforce 48-hour cooling periods before investments", "Wash feet with cold water"),
            listOf("Do not gamble or chase get-rich-quick schemes", "Avoid synthetic drugs or excessive intoxicants", "Do not sign unread contracts")
        )
        Planet.KETU -> FourTuple(
            listOf("Profound spiritual awakenings and inner peace", "Lightning intuition and esoteric discernment", "Freedom from material cravings"),
            listOf("Apathy, disconnection from worldly duties, and sudden losses", "Diagnostic confusion in health matters", "Chronic isolation"),
            listOf("Practice silent breath observation (Vipassana)", "Feed stray dogs with bread or biscuits", "Declutter unused belongings systematically"),
            listOf("Do not abruptly abandon family responsibilities", "Avoid cynical nihlism", "Do not neglect bodily nutrition")
        )
        Planet.ASCENDANT -> FourTuple(emptyList(), emptyList(), emptyList(), emptyList())
    }

    private data class FourTuple(
        val gains: List<String>,
        val losses: List<String>,
        val doList: List<String>,
        val avoidList: List<String>
    )
}
