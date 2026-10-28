package com.example.astro.calc

import com.example.astro.model.*

object YogaEngine {

    fun detectYogas(chartData: ChartData): List<VedicYoga> {
        val detected = mutableListOf<VedicYoga>()
        val ascSign = chartData.ascendantSign
        val planets = chartData.planets
        val moon = chartData.getPlanet(Planet.MOON)
        val sun = chartData.getPlanet(Planet.SUN)
        val mars = chartData.getPlanet(Planet.MARS)
        val mercury = chartData.getPlanet(Planet.MERCURY)
        val jupiter = chartData.getPlanet(Planet.JUPITER)
        val venus = chartData.getPlanet(Planet.VENUS)
        val saturn = chartData.getPlanet(Planet.SATURN)
        val rahu = chartData.getPlanet(Planet.RAHU)
        val ketu = chartData.getPlanet(Planet.KETU)

        // 1. Pancha Mahapurusha Yogas (Kendra houses 1, 4, 7, 10 + Own or Exalted Sign)
        val kendraHouses = listOf(1, 4, 7, 10)

        // Ruchaka Yoga (Mars)
        if (mars != null && mars.house in kendraHouses && (mars.sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO, ZodiacSign.CAPRICORN))) {
            detected.add(
                VedicYoga(
                    id = "ruchaka_yoga",
                    name = "Ruchaka Yoga (Pancha Mahapurusha)",
                    sanskritName = "रुचक योग",
                    category = "Pancha Mahapurusha Yoga",
                    triggeringPlanets = listOf(Planet.MARS),
                    description = "Formed when Mars occupies a Kendra house (1, 4, 7, 10) in its own sign (Aries/Scorpio) or exaltation sign (Capricorn).",
                    benefits = listOf(
                        "Commanding executive leadership, military discipline, and courage under fire",
                        "Athletic physical stamina, high energy, and capacity to triumph over competitors",
                        "Acquisition of land, real estate, and mechanical/engineering prowess"
                    ),
                    disadvantages = listOf(
                        "Potential for excessive authoritarianism or impatience with slower collaborators",
                        "Need to consciously manage sudden temper spikes"
                    )
                )
            )
        }

        // Bhadra Yoga (Mercury)
        if (mercury != null && mercury.house in kendraHouses && (mercury.sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO))) {
            detected.add(
                VedicYoga(
                    id = "bhadra_yoga",
                    name = "Bhadra Yoga (Pancha Mahapurusha)",
                    sanskritName = "भद्र योग",
                    category = "Pancha Mahapurusha Yoga",
                    triggeringPlanets = listOf(Planet.MERCURY),
                    description = "Formed when Mercury occupies a Kendra house in Gemini or Virgo.",
                    benefits = listOf(
                        "Exceptional intellectual acumen, photographic recall, and mathematical/linguistic brilliance",
                        "Formidable commercial finesse, negotiation skills, and literary talent",
                        "Youthful demeanor, sharp wit, and eloquent diplomacy"
                    ),
                    disadvantages = listOf(
                        "Overthinking and nervous restlessness",
                        "Tendency to analyze emotions mechanically instead of feeling them"
                    )
                )
            )
        }

        // Hamsa Yoga (Jupiter)
        if (jupiter != null && jupiter.house in kendraHouses && (jupiter.sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES, ZodiacSign.CANCER))) {
            detected.add(
                VedicYoga(
                    id = "hamsa_yoga",
                    name = "Hamsa Yoga (Pancha Mahapurusha)",
                    sanskritName = "हंस योग",
                    category = "Pancha Mahapurusha Yoga",
                    triggeringPlanets = listOf(Planet.JUPITER),
                    description = "Formed when Jupiter occupies a Kendra house in Sagittarius, Pisces, or Cancer.",
                    benefits = listOf(
                        "Revered wisdom, ethical nobility, and natural mentorship authority",
                        "Spiritual depth, scholarly reputation, and divine protective grace",
                        "Generous benevolence that naturally attracts trustworthy patrons"
                    ),
                    disadvantages = listOf(
                        "Risk of dogmatic idealism or self-righteous lecturing if unchecked",
                        "Underestimating worldly deceit due to excessive benevolence"
                    )
                )
            )
        }

        // Malavya Yoga (Venus)
        if (venus != null && venus.house in kendraHouses && (venus.sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA, ZodiacSign.PISCES))) {
            detected.add(
                VedicYoga(
                    id = "malavya_yoga",
                    name = "Malavya Yoga (Pancha Mahapurusha)",
                    sanskritName = "मालव्य योग",
                    category = "Pancha Mahapurusha Yoga",
                    triggeringPlanets = listOf(Planet.VENUS),
                    description = "Formed when Venus occupies a Kendra house in Taurus, Libra, or Pisces.",
                    benefits = listOf(
                        "Supreme artistic refinement, aesthetic vision, and magnetic personal charm",
                        "Sensory luxury, conjugal happiness, and effortless prosperity",
                        "Exceptional diplomatic and collaborative negotiation grace"
                    ),
                    disadvantages = listOf(
                        "Vulnerability to hedonistic comfort zones and procrastination",
                        "Conflict avoidance that delays necessary confrontations"
                    )
                )
            )
        }

        // Sasa Yoga (Saturn)
        if (saturn != null && saturn.house in kendraHouses && (saturn.sign in listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS, ZodiacSign.LIBRA))) {
            detected.add(
                VedicYoga(
                    id = "sasa_yoga",
                    name = "Sasa Yoga (Pancha Mahapurusha)",
                    sanskritName = "शश योग",
                    category = "Pancha Mahapurusha Yoga",
                    triggeringPlanets = listOf(Planet.SATURN),
                    description = "Formed when Saturn occupies a Kendra house in Capricorn, Aquarius, or Libra.",
                    benefits = listOf(
                        "Formidable perseverance, stoic resilience, and strategic long-term vision",
                        "Ability to govern large masses, public organizations, and complex infrastructure",
                        "Deep humility, hard-won authority, and lifelong mastery"
                    ),
                    disadvantages = listOf(
                        "Tendency toward emotional austerity, melancholy, and excessive self-reliance",
                        "Delayed recognition that requires enduring initial testing"
                    )
                )
            )
        }

        // 2. Gajakesari Yoga (Jupiter in Kendra from Moon)
        if (moon != null && jupiter != null) {
            val houseDiffFromMoon = ((jupiter.sign.number - moon.sign.number + 12) % 12) + 1
            if (houseDiffFromMoon in listOf(1, 4, 7, 10)) {
                detected.add(
                    VedicYoga(
                        id = "gajakesari_yoga",
                        name = "Gajakesari Yoga",
                        sanskritName = "गजकेसरी योग",
                        category = "Auspicious Raja Yoga",
                        triggeringPlanets = listOf(Planet.JUPITER, Planet.MOON),
                        description = "Formed when Jupiter is situated in an angular house (1st, 4th, 7th, or 10th) from the natal Moon.",
                        benefits = listOf(
                            "Royal dignity, intellectual eloquence, and enduring public reputation",
                            "Overcoming adversaries through noble character, wisdom, and strategic patience",
                            "Prosperity, scholarly achievements, and societal benevolence"
                        ),
                        disadvantages = listOf(
                            "Over-magnifying expectations of others' integrity",
                            "Vulnerability to flattery from dishonest acquaintances"
                        )
                    )
                )
            }
        }

        // 3. Budhaditya Yoga (Sun + Mercury in same sign)
        if (sun != null && mercury != null && sun.sign == mercury.sign) {
            val isCombust = mercury.isCombust
            detected.add(
                VedicYoga(
                    id = "budhaditya_yoga",
                    name = "Budhaditya Yoga",
                    sanskritName = "बुधादित्य योग",
                    category = "Auspicious Raja Yoga",
                    triggeringPlanets = listOf(Planet.SUN, Planet.MERCURY),
                    description = "Formed when the Sun and Mercury conjoin in the same sign in House ${sun.house} (${sun.sign.englishName}).",
                    benefits = listOf(
                        "Brilliant analytical intellect, executive persuasion, and articulate administrative eloquence",
                        "Aptitude for governance, commerce, leadership, and technical innovation",
                        "Respected reputation in professional and academic arenas"
                    ),
                    disadvantages = if (isCombust) listOf(
                        "Mercury is combust: may experience mental exhaustion or nervous irritability when overworked"
                    ) else listOf(
                        "Sharp tongue during debates that may inadvertently bruise egos"
                    )
                )
            )
        }

        // 4. Chandra-Mangala Yoga (Moon + Mars in same sign or 7th aspect)
        if (moon != null && mars != null) {
            val sameSign = moon.sign == mars.sign
            val mutualAspect = ((mars.sign.number - moon.sign.number + 12) % 12) + 1 == 7
            if (sameSign || mutualAspect) {
                detected.add(
                    VedicYoga(
                        id = "chandra_mangala_yoga",
                        name = "Chandra-Mangala Yoga",
                        sanskritName = "चन्द्र-मंगल योग",
                        category = "Dhana (Wealth) Yoga",
                        triggeringPlanets = listOf(Planet.MOON, Planet.MARS),
                        description = "Formed by conjunction or mutual aspect between Moon and Mars, combining emotional intuition with kinetic ambition.",
                        benefits = listOf(
                            "Extraordinary entrepreneurial drive and wealth-accumulation prowess",
                            "Dynamic initiative to build independent assets and commercial ventures",
                            "Unshakable tenacity to bounce back from financial setbacks"
                        ),
                        disadvantages = listOf(
                            "Emotional volatility or restlessness in domestic matters",
                            "Tendency to measure personal worth purely through material acquisition"
                        )
                    )
                )
            }
        }

        // 5. Amala Yoga (Benefic in 10th house from Lagna or Moon)
        val benefics = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
        val beneficsIn10th = planets.filter { it.house == 10 && it.planet in benefics }
        if (beneficsIn10th.isNotEmpty()) {
            detected.add(
                VedicYoga(
                    id = "amala_yoga",
                    name = "Amala Yoga",
                    sanskritName = "अमल योग",
                    category = "Auspicious Raja Yoga",
                    triggeringPlanets = beneficsIn10th.map { it.planet },
                    description = "Formed when a natural benefic occupies the 10th house of career from Lagna.",
                    benefits = listOf(
                        "Spotless professional reputation and lasting career fame",
                        "High ethical integrity recognized by superiors and society",
                        "Philanthropic inclinations and harmonious professional influence"
                    ),
                    disadvantages = listOf(
                        "Struggling in toxic cut-throat corporate environments that lack integrity"
                    )
                )
            )
        }

        // 6. Vipareeta Raja Yogas (6, 8, 12 lords in 6, 8, 12 houses)
        val dusthanaHouses = listOf(6, 8, 12)
        val lord6 = ZodiacSign.fromNumber(((ascSign.number + 5 - 1) % 12) + 1).ruler
        val lord8 = ZodiacSign.fromNumber(((ascSign.number + 7 - 1) % 12) + 1).ruler
        val lord12 = ZodiacSign.fromNumber(((ascSign.number + 11 - 1) % 12) + 1).ruler

        val pLord6 = chartData.getPlanet(lord6)
        val pLord8 = chartData.getPlanet(lord8)
        val pLord12 = chartData.getPlanet(lord12)

        if (pLord6 != null && pLord6.house in dusthanaHouses) {
            detected.add(
                VedicYoga(
                    id = "harsha_yoga",
                    name = "Harsha Vipareeta Raja Yoga",
                    sanskritName = "हर्ष विपरीत राजयोग",
                    category = "Vipareeta Raja Yoga",
                    triggeringPlanets = listOf(lord6),
                    description = "Formed when the 6th lord is placed in a Dusthana house (6th, 8th, or 12th).",
                    benefits = listOf(
                        "Triumphing over acute crises, legal disputes, and competitor challenges",
                        "Immunity to adversary attacks; sudden breakthroughs arising from opponents' errors",
                        "Cheerfulness (Harsha) and bodily vigor"
                    ),
                    disadvantages = listOf(
                        "Success typically arrives only after passing through intense competitive stress"
                    )
                )
            )
        }

        if (pLord8 != null && pLord8.house in dusthanaHouses) {
            detected.add(
                VedicYoga(
                    id = "sarala_yoga",
                    name = "Sarala Vipareeta Raja Yoga",
                    sanskritName = "सरल विपरीत राजयोग",
                    category = "Vipareeta Raja Yoga",
                    triggeringPlanets = listOf(lord8),
                    description = "Formed when the 8th lord is placed in a Dusthana house.",
                    benefits = listOf(
                        "Longevity, fearless psychological depth, and unexpected windfalls",
                        "Transmuting profound catastrophic setbacks into major career rebirths",
                        "Scholarship in esoteric or investigative sciences"
                    ),
                    disadvantages = listOf(
                        "Requires navigating initial psychological crises before unlocking power"
                    )
                )
            )
        }

        if (pLord12 != null && pLord12.house in dusthanaHouses) {
            detected.add(
                VedicYoga(
                    id = "vimala_yoga",
                    name = "Vimala Vipareeta Raja Yoga",
                    sanskritName = "विमल विपरीत राजयोग",
                    category = "Vipareeta Raja Yoga",
                    triggeringPlanets = listOf(lord12),
                    description = "Formed when the 12th lord is placed in a Dusthana house.",
                    benefits = listOf(
                        "Purity of conduct (Vimala), spiritual wisdom, and prudent financial expenditure",
                        "Success in foreign lands and non-attachment to petty material grievances",
                        "Effortless avoidance of heavy debts and losses"
                    ),
                    disadvantages = listOf(
                        "Natural detachment may be misinterpreted by others as indifference"
                    )
                )
            )
        }

        // 7. Manglik / Kuja Dosha (Mars in 1, 4, 7, 8, 12)
        if (mars != null && mars.house in listOf(1, 4, 7, 8, 12)) {
            detected.add(
                VedicYoga(
                    id = "manglik_dosha",
                    name = "Manglik (Kuja) Dosha",
                    sanskritName = "मांगलिक दोष",
                    category = "Relational Challenge (Dosha)",
                    triggeringPlanets = listOf(Planet.MARS),
                    description = "Mars is positioned in House ${mars.house}, directing aggressive kinetic energy toward interpersonal bonds.",
                    benefits = listOf(
                        "Fierce loyalty, dynamic passion, and proactive protection of loved ones",
                        "High ambition to provide material stability for family"
                    ),
                    disadvantages = listOf(
                        "High friction or impatience in domestic negotiations",
                        "Need to cultivate cooling habits, active listening, and avoiding hasty ultimatums"
                    )
                )
            )
        }

        // 8. Guru Chandal Yoga (Jupiter + Rahu)
        if (jupiter != null && rahu != null && jupiter.sign == rahu.sign) {
            detected.add(
                VedicYoga(
                    id = "guru_chandal_yoga",
                    name = "Guru Chandal Yoga",
                    sanskritName = "गुरु चांडाल योग",
                    category = "Philosophical Tension (Dosha)",
                    triggeringPlanets = listOf(Planet.JUPITER, Planet.RAHU),
                    description = "Formed by the conjunction of Jupiter and Rahu in ${jupiter.sign.englishName}.",
                    benefits = listOf(
                        "Radical questioning of outdated orthodoxies and societal dogmas",
                        "Aptitude for cutting-edge technological innovation and revolutionary ideas"
                    ),
                    disadvantages = listOf(
                        "Skepticism toward legitimate spiritual teachers or traditional mentors",
                        "Risk of falling prey to fraudulent pseudo-intellectual shortcuts"
                    )
                )
            )
        }

        // 9. Kemadruma Yoga (No planets in 2nd and 12th from Moon, excluding Sun/Rahu/Ketu)
        if (moon != null) {
            val house2Sign = ZodiacSign.fromNumber(((moon.sign.number + 1 - 1) % 12) + 1)
            val house12Sign = ZodiacSign.fromNumber(((moon.sign.number - 1 - 1 + 12) % 12) + 1)
            val nonLuminaryPlanets = planets.filter { !it.planet.isLuminary && !it.planet.isNode }
            val hasIn2nd = nonLuminaryPlanets.any { it.sign == house2Sign }
            val hasIn12th = nonLuminaryPlanets.any { it.sign == house12Sign }

            if (!hasIn2nd && !hasIn12th) {
                detected.add(
                    VedicYoga(
                        id = "kemadruma_yoga",
                        name = "Kemadruma Yoga (Isolated Moon)",
                        sanskritName = "केमद्रुम योग",
                        category = "Emotional Challenge (Dosha)",
                        triggeringPlanets = listOf(Planet.MOON),
                        description = "Moon has no planets seated in the 2nd and 12th houses from it, creating an isolated psychological sensation.",
                        benefits = listOf(
                            "Profound self-reliance and internal spiritual independence",
                            "Ability to function exceptionally well in solitude and deep research"
                        ),
                        disadvantages = listOf(
                            "Recurring sensations of emotional loneliness or lack of external support",
                            "Overcoming by establishing consistent daily community interactions and service"
                        )
                    )
                )
            }
        }

        return detected
    }
}
