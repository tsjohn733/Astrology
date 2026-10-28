package com.example.astro.calc

import com.example.astro.model.*

object VaisheshikamsaEngine {

    fun calculateVaisheshikamsa(chartData: ChartData): List<VaisheshikamsaReport> = calculateTiers(chartData)
    fun calculateTiers(chartData: ChartData): List<VaisheshikamsaReport> {
        val vargas16 = chartData.vargaCharts
        val dashavargaTypes = VargaType.entries.filter { it.inDashavarga }

        return chartData.planets.map { p ->
            val planet = p.planet

            var count16 = 0
            var count10 = 0

            for ((vargaType, vargaChart) in vargas16) {
                val placement = vargaChart.placements.firstOrNull { it.planet == planet }
                if (placement != null && isDignifiedSign(planet, placement.sign)) {
                    count16++
                    if (vargaType in dashavargaTypes) {
                        count10++
                    }
                }
            }

            val shodashTier = getShodashvargaTierName(count16)
            val dashaTier = getDashavargaTierName(count10)

            val (meaning, powers, advice) = getVaisheshikamsaExplanation(planet, count16, shodashTier, count10, dashaTier)

            VaisheshikamsaReport(
                planet = planet,
                shodashvargaCount = count16,
                shodashvargaTier = shodashTier,
                dashavargaCount = count10,
                dashavargaTier = dashaTier,
                nameMeaning = meaning,
                bestowedPowers = powers,
                optimizationAdvice = advice
            )
        }
    }

    private fun isDignifiedSign(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.LEO || sign == ZodiacSign.ARIES
            Planet.MOON -> sign == ZodiacSign.CANCER || sign == ZodiacSign.TAURUS
            Planet.MARS -> sign == ZodiacSign.ARIES || sign == ZodiacSign.SCORPIO || sign == ZodiacSign.CAPRICORN
            Planet.MERCURY -> sign == ZodiacSign.GEMINI || sign == ZodiacSign.VIRGO
            Planet.JUPITER -> sign == ZodiacSign.SAGITTARIUS || sign == ZodiacSign.PISCES || sign == ZodiacSign.CANCER
            Planet.VENUS -> sign == ZodiacSign.TAURUS || sign == ZodiacSign.LIBRA || sign == ZodiacSign.PISCES
            Planet.SATURN -> sign == ZodiacSign.CAPRICORN || sign == ZodiacSign.AQUARIUS || sign == ZodiacSign.LIBRA
            Planet.RAHU -> sign == ZodiacSign.TAURUS || sign == ZodiacSign.GEMINI || sign == ZodiacSign.VIRGO
            Planet.KETU -> sign == ZodiacSign.SCORPIO || sign == ZodiacSign.SAGITTARIUS || sign == ZodiacSign.PISCES
            Planet.ASCENDANT -> false
        }
    }

    fun getShodashvargaTierName(count: Int): String? = when (count) {
        2 -> "Bhedaka (भेदक)"
        3 -> "Kusuma (कुसुम - Blossom)"
        4 -> "Nagapushpa (नागपुष्प - Sacred Flower)"
        5 -> "Kanduka (कन्दुक)"
        6 -> "Kerala (केरल)"
        7 -> "Kalpavriksha (कल्पवृक्ष - Wish-Fulfilling Tree)"
        8 -> "Chandanavana (चन्दनवन - Fragrant Sandalwood Forest)"
        9 -> "Parijata (पारिजात - Celestial Celestial Blossom)"
        10 -> "Kamadhenu (कामधेनु - Divine Cow of Abundance)"
        11 -> "Vipula (विपुल - Infinite Radiance)"
        12 -> "Makuta (मुकुट - Royal Crown of Sovereign Kings)"
        13 -> "Amaraloka (अमरलोक - Immortal Realm of the Devas)"
        14 -> "Vaishnavamsha (वैष्णवांश - Abode of Lord Vishnu)"
        15 -> "Saiva (शैवांश - Auspicious Abode of Lord Shiva)"
        16 -> "Shridhama / Vaisheshikamsa (श्रीधाम - Supreme Transcendental Abode)"
        else -> if (count >= 1) "Swa/Uchcha in 1 Varga" else "Ordinary Placements"
    }

    fun getDashavargaTierName(count: Int): String? = when (count) {
        2 -> "Parijata (पारिजात)"
        3 -> "Uttama (उत्तम - Superior)"
        4 -> "Gopura (गोपुर - Temple Gateway/Sanctum)"
        5 -> "Simhasana (सिंहासन - Sovereign Throne)"
        6 -> "Paravata (पारावत - High Mountain Citadel)"
        7 -> "Devaloka (देवलोक - Divine Celestial Domain)"
        8 -> "Iravata (ऐरावत - Royal Royal Mount of Indra)"
        9 -> "Vaishnavamsha (वैष्णवांश - Vishnu's Radiance)"
        10 -> "Shridhama (श्रीधाम - Supreme Abode of Lakshmi)"
        else -> if (count >= 1) "1 Dignified Varga" else "Ordinary"
    }

    private fun getVaisheshikamsaExplanation(
        planet: Planet,
        count16: Int,
        tier16: String?,
        count10: Int,
        tier10: String?
    ): Triple<String, String, String> {
        val meaning = when {
            count16 >= 10 -> "Supreme Kamadhenu/Shridhama Tier: The planet repeats in exalted/own signs across 10+ divisional charts. In classical Parashara Jyotish, this grants miraculous sovereign powers that cannot be thwarted by ordinary malefic transits."
            count16 >= 7 -> "Kalpavriksha / Chandanavana Tier: Named after the sacred mythological Kalpavriksha (the celestial Wish-Fulfilling Tree of Indra's Swarga) and the fragrant Sandalwood forest. It means whatever this planet signifies will effortlessly manifest without exhausting strife."
            count16 >= 4 -> "Nagapushpa / Kerala Tier: A sacred floral blossom tier, representing blossoming intellectual and material fortune that emerges with maturity and consistency."
            count16 in 2..3 -> "Bhedaka / Kusuma Tier: The bud begins to open; the planet has double or triple root anchoring in the subconscious divisional layers."
            else -> "Foundational Stage: The planet's strength is primarily concentrated in specific divisional domains rather than uniform across all sub-harmonics."
        }

        val powers = when {
            count16 >= 7 -> "Bestows magnetic authority, divine luck, immunity from catastrophic failures in its karaka areas, and spontaneous breakthroughs during its active Mahadashas."
            count16 >= 4 -> "Provides steady financial security, professional resilience, respect among peers, and high creative output."
            count16 in 2..3 -> "Grants reliable talents and occasional lucky breaks when engaging in projects aligned with this planet's elemental nature."
            else -> "Yields pragmatic results when backed by conscious discipline and targeted daily remedies."
        }

        val advice = when (planet) {
            Planet.SUN -> "Extract maximum benefit through morning meditation, ethical leadership, and wearing ruby or pure copper."
            Planet.MOON -> "Extract maximum benefit by honoring maternal figures, staying close to natural water, and consuming silver-charged water."
            Planet.MARS -> "Channel this fiery potency into high-stakes engineering, athletics, land acquisition, and defending vulnerable dependents."
            Planet.MERCURY -> "Leverage this commercial brilliance in negotiations, writing, publishing, software development, and trade."
            Planet.JUPITER -> "Maximize benefits through advisory roles, teaching, voluntary dharma service, and cultivating guru blessings."
            Planet.VENUS -> "Activate creative design, diplomacy, partnerships, and high aesthetic ventures; maintain spotless marital ethics."
            Planet.SATURN -> "Harness this immense endurance by taking on monumental long-term infrastructure projects and serving the elderly."
            Planet.RAHU -> "Deploy innovative disruption, cutting-edge technology, and global cross-border ventures with strict ethical transparency."
            Planet.KETU -> "Channel this laser intuition into silent meditation, mathematical research, esoteric philosophy, and deep introspection."
            Planet.ASCENDANT -> "Maintain alignment between personal values and outward actions."
        }

        return Triple(meaning, powers, advice)
    }
}
