package com.example.astro.model

enum class ZodiacSign(
    val number: Int,
    val sanskritName: String,
    val englishName: String,
    val element: String,
    val quality: String,
    val ruler: Planet
) {
    ARIES(1, "Mesha", "Aries", "Fire", "Movable", Planet.MARS),
    TAURUS(2, "Vrishabha", "Taurus", "Earth", "Fixed", Planet.VENUS),
    GEMINI(3, "Mithuna", "Gemini", "Air", "Dual", Planet.MERCURY),
    CANCER(4, "Karka", "Cancer", "Water", "Movable", Planet.MOON),
    LEO(5, "Simha", "Leo", "Fire", "Fixed", Planet.SUN),
    VIRGO(6, "Kanya", "Virgo", "Earth", "Dual", Planet.MERCURY),
    LIBRA(7, "Tula", "Libra", "Air", "Movable", Planet.VENUS),
    SCORPIO(8, "Vrishchika", "Scorpio", "Water", "Fixed", Planet.MARS),
    SAGITTARIUS(9, "Dhanu", "Sagittarius", "Fire", "Dual", Planet.JUPITER),
    CAPRICORN(10, "Makara", "Capricorn", "Earth", "Movable", Planet.SATURN),
    AQUARIUS(11, "Kumbha", "Aquarius", "Air", "Fixed", Planet.SATURN),
    PISCES(12, "Meena", "Pisces", "Water", "Dual", Planet.JUPITER);

    companion object {
        fun fromNumber(num: Int): ZodiacSign {
            val normalized = ((num - 1) % 12 + 12) % 12 + 1
            return entries.first { it.number == normalized }
        }

        fun fromLongitude(deg: Double): Pair<ZodiacSign, Double> {
            val normalized = ((deg % 360.0) + 360.0) % 360.0
            val signIndex = (normalized / 30.0).toInt() + 1
            val degreeInSign = normalized % 30.0
            return Pair(fromNumber(signIndex), degreeInSign)
        }
    }
}

enum class Planet(
    val sanskritName: String,
    val englishName: String,
    val symbol: String,
    val nature: String
) {
    SUN("Surya", "Sun", "Su", "Soul, Vitality, Father, Authority"),
    MOON("Chandra", "Moon", "Mo", "Mind, Emotions, Intuition, Mother"),
    MARS("Mangal", "Mars", "Ma", "Energy, Courage, Action, Drive"),
    MERCURY("Budha", "Mercury", "Me", "Intellect, Speech, Commerce, Logic"),
    JUPITER("Guru", "Jupiter", "Ju", "Wisdom, Dharma, Expansion, Prosperity"),
    VENUS("Shukra", "Venus", "Ve", "Love, Aesthetics, Harmony, Pleasure"),
    SATURN("Shani", "Saturn", "Sa", "Discipline, Karma, Longevity, Humility"),
    RAHU("Rahu", "North Node", "Ra", "Desire, Innovation, Foreign, Material Drive"),
    KETU("Ketu", "South Node", "Ke", "Liberation, Detachment, Spirituality, Insight"),
    ASCENDANT("Lagna", "Ascendant", "Asc", "Self, Physical Body, Life Path");

    val isLuminary: Boolean get() = this == SUN || this == MOON
    val isNode: Boolean get() = this == RAHU || this == KETU
}

enum class Dignity(val label: String, val level: Int) {
    EXALTED("Exalted (Uchcha)", 5),
    MOOLATRIKONA("Moolatrikona", 4),
    OWN_SIGN("Own Sign (Swakshetra)", 3),
    GREAT_FRIEND("Great Friend (Adhi Mitra)", 2),
    FRIEND("Friendly (Mitra)", 1),
    NEUTRAL("Neutral (Sama)", 0),
    ENEMY("Enemy (Shatru)", -1),
    GREAT_ENEMY("Great Enemy (Adhi Shatru)", -2),
    DEBILITATED("Debilitated (Neecha)", -3)
}

data class Nakshatra(
    val number: Int,
    val name: String,
    val lord: Planet,
    val deity: String
)

data class PlanetPosition(
    val planet: Planet,
    val longitude: Double,
    val sign: ZodiacSign,
    val degreeInSign: Double,
    val house: Int, // 1 to 12
    val nakshatra: Nakshatra,
    val pada: Int, // 1 to 4
    val isRetrograde: Boolean,
    val isCombust: Boolean,
    val dignity: Dignity,
    val vargottama: Boolean = false
)

enum class VargaGroup {
    SAPTA_VARGA,
    DASHA_VARGA,
    SHODASHA_VARGA
}

enum class VargaType(
    val division: Int,
    val title: String,
    val sanskritName: String,
    val purpose: String,
    val inSaptavarga: Boolean,
    val inDashavarga: Boolean
) {
    D1(1, "Rashi", "Rashi", "Physical body, overall life foundation & general fortunes", true, true),
    D2(2, "Hora", "Hora", "Wealth, liquid assets, speech, financial vitality", true, true),
    D3(3, "Drekkana", "Drekkana", "Siblings, vitality, courage, initiative & drive", true, true),
    D4(4, "Chaturthamsha", "Turyamsha", "Fixed property, land, inner happiness, luck", false, true),
    D7(7, "Saptamsha", "Saptamsha", "Children, progeny, legacy, partnerships & fruits of labor", true, true),
    D9(9, "Navamsha", "Dharmamsha", "Soul purpose, marriage, spouse, spiritual destiny (Crucial)", true, true),
    D10(10, "Dashamsha", "Karmamsha", "Career, profession, social authority, reputation, power", false, true),
    D12(12, "Dwadashamsha", "Suryamsha", "Parents, ancestry, inherited traits & past lineage", true, true),
    D16(16, "Shodashamsha", "Kalamsha", "Vehicles, comforts, pleasures & luxurious conveyances", false, true),
    D20(20, "Vimshamsha", "Upasanamsha", "Spiritual inclination, devotion, meditation & sadhana", false, false),
    D24(24, "Chaturvimshamsha", "Siddhamsha", "Higher education, learning, skills, mental accomplishments", false, false),
    D27(27, "Saptavimshamsha", "Bhamsha", "Physical strengths, endurance, innate vulnerabilities", false, false),
    D30(30, "Trimshamsha", "Trimshamsha", "Adversities, karmic challenges, health risks & misfortunes", true, true),
    D40(40, "Khavedamsha", "Chatvarimshamsha", "Auspicious & inauspicious auspicious karmic occurrences", false, false),
    D45(45, "Akshavedamsha", "Akshavedamsha", "Character integrity, moral purity, all-round auspiciousness", false, false),
    D60(60, "Shashtiamsha", "Shashtiamsha", "Deep past-life karma, seed causes of destiny (Most refined)", false, false);
}

data class VargaPlacement(
    val planet: Planet,
    val sign: ZodiacSign,
    val degree: Double = 0.0
)

data class VargaChart(
    val type: VargaType,
    val ascendantSign: ZodiacSign,
    val placements: List<VargaPlacement>
) {
    fun getPlanetsInHouse(house: Int): List<Planet> {
        val targetSign = ZodiacSign.fromNumber(ascendantSign.number + (house - 1))
        return placements.filter { it.sign == targetSign }.map { it.planet }
    }
}

data class ChartData(
    val ascendantDegree: Double,
    val ascendantSign: ZodiacSign,
    val ayanamsha: Double,
    val planets: List<PlanetPosition>,
    val vargaCharts: Map<VargaType, VargaChart>
) {
    fun getPlanet(planet: Planet): PlanetPosition? = planets.firstOrNull { it.planet == planet }
    fun getPlanetsInHouse(house: Int): List<PlanetPosition> = planets.filter { it.house == house }
}

data class PlanetaryInsight(
    val planet: Planet,
    val rootWhy: String,
    val psychologicalTendency: String,
    val giftsAndBenefits: List<String>,
    val precautionsAndBlindSpots: List<String>,
    val practicalLifestyleHabits: List<String>
)

data class DailyPrediction(
    val dateText: String,
    val loveInsight: String,
    val careerInsight: String,
    val healthInsight: String,
    val transitsSummary: String,
    val tipOfTheDay: String
)

data class CityLocation(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double
)
