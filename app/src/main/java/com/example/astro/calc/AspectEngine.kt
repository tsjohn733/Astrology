package com.example.astro.calc

import com.example.astro.model.*

object AspectEngine {

    fun calculateAspects(chartData: ChartData): List<PlanetaryAspectInfo> {
        val planets = chartData.planets

        return planets.map { p ->
            val sourceHouse = p.house
            val aspectOffsets = when (p.planet) {
                Planet.MARS -> listOf(4, 7, 8)
                Planet.JUPITER -> listOf(5, 7, 9)
                Planet.SATURN -> listOf(3, 7, 10)
                Planet.RAHU, Planet.KETU -> listOf(5, 7, 9)
                else -> listOf(7) // Sun, Moon, Mercury, Venus aspect 7th
            }

            val targetHouses = aspectOffsets.map { offset ->
                ((sourceHouse + offset - 2) % 12) + 1
            }

            val aspectedGrahas = planets.filter { other ->
                other.planet != p.planet && other.house in targetHouses
            }.map { it.planet }

            val summary = "${p.planet.englishName} in H$sourceHouse casts aspects on Houses: ${targetHouses.joinToString(", ") { "H$it" }}" +
                    if (aspectedGrahas.isNotEmpty()) " (Aspecting: ${aspectedGrahas.joinToString(", ") { it.englishName }})" else ""

            val interpretation = getAspectInterpretation(p.planet, targetHouses, aspectedGrahas)

            PlanetaryAspectInfo(
                planet = p.planet,
                aspectedHouses = targetHouses,
                aspectedPlanets = aspectedGrahas,
                aspectSummary = summary,
                astrologicalResult = interpretation
            )
        }
    }

    private fun getAspectInterpretation(planet: Planet, houses: List<Int>, aspectedGrahas: List<Planet>): String = when (planet) {
        Planet.SUN -> "The Solar aspect infuses Houses ${houses.joinToString(", ") { "H$it" }} with authority, vitality, and illumination. However, because Sun is a mild krura (cruel) graha, its aspect can introduce pride or separation in sensitive relational houses."
        Planet.MOON -> "The Lunar aspect casts soothing emotional nourishment, public receptivity, and intuitive sensitivity upon Houses ${houses.joinToString(", ") { "H$it" }}."
        Planet.MARS -> "Mars casts its aggressive, kinetic Drishti upon Houses ${houses.joinToString(", ") { "H$it" }} (including 4th and 8th special aspects), injecting courage, competitive urgency, and engineering stamina, but warning of friction, hasty disputes, or inflammatory heat."
        Planet.MERCURY -> "Mercury's aspect stimulates commercial acumen, linguistic clarity, rational discernment, and intellectual curiosity across Houses ${houses.joinToString(", ") { "H$it" }}."
        Planet.JUPITER -> "Jupiter's divine Amrita (nectar) Drishti on Houses ${houses.joinToString(", ") { "H$it" }} (including 5th and 9th trinal aspects) bestows divine protection, moral elevation, expansion, and neutralization of existing afflictions."
        Planet.VENUS -> "Venus casts harmonious aesthetic grace, peacemaking diplomacy, luxury, and refined affection upon Houses ${houses.joinToString(", ") { "H$it" }}."
        Planet.SATURN -> "Saturn's heavy, disciplined Drishti on Houses ${houses.joinToString(", ") { "H$it" }} (including 3rd and 10th special aspects) brings reality testing, delayed manifestation, accountability, and demand for unyielding patience."
        Planet.RAHU -> "Rahu's smoky, obsessive aspect on Houses ${houses.joinToString(", ") { "H$it" }} creates unconventional curiosity, technological fascination, and intense craving, requiring conscious ethical grounding."
        Planet.KETU -> "Ketu's piercing spiritual aspect on Houses ${houses.joinToString(", ") { "H$it" }} prompts detachment, disillusionment with superficial facades, and deep inward intuitive quest."
        Planet.ASCENDANT -> "Lagna projection."
    }
}
