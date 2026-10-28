package com.example.astro.calc

import com.example.astro.model.*
import kotlin.math.*

object EphemerisCalculator {

    val NAKSHATRAS = listOf(
        Nakshatra(1, "Ashwini", Planet.KETU, "Ashvins"),
        Nakshatra(2, "Bharani", Planet.VENUS, "Yama"),
        Nakshatra(3, "Krittika", Planet.SUN, "Agni"),
        Nakshatra(4, "Rohini", Planet.MOON, "Brahma"),
        Nakshatra(5, "Mrigashira", Planet.MARS, "Soma"),
        Nakshatra(6, "Ardra", Planet.RAHU, "Rudra"),
        Nakshatra(7, "Punarvasu", Planet.JUPITER, "Aditi"),
        Nakshatra(8, "Pushya", Planet.SATURN, "Brihaspati"),
        Nakshatra(9, "Ashlesha", Planet.MERCURY, "Sarpa"),
        Nakshatra(10, "Magha", Planet.KETU, "Pitris"),
        Nakshatra(11, "Purva Phalguni", Planet.VENUS, "Bhaga"),
        Nakshatra(12, "Uttara Phalguni", Planet.SUN, "Aryaman"),
        Nakshatra(13, "Hasta", Planet.MOON, "Savitr"),
        Nakshatra(14, "Chitra", Planet.MARS, "Tvashtr"),
        Nakshatra(15, "Swati", Planet.RAHU, "Vayu"),
        Nakshatra(16, "Vishakha", Planet.JUPITER, "Indragni"),
        Nakshatra(17, "Anuradha", Planet.SATURN, "Mitra"),
        Nakshatra(18, "Jyeshtha", Planet.MERCURY, "Indra"),
        Nakshatra(19, "Mula", Planet.KETU, "Nirriti"),
        Nakshatra(20, "Purva Ashadha", Planet.VENUS, "Apah"),
        Nakshatra(21, "Uttara Ashadha", Planet.SUN, "Vishvadevas"),
        Nakshatra(22, "Shravana", Planet.MOON, "Vishnu"),
        Nakshatra(23, "Dhanishta", Planet.MARS, "Vasus"),
        Nakshatra(24, "Shatabhisha", Planet.RAHU, "Varuna"),
        Nakshatra(25, "Purva Bhadrapada", Planet.JUPITER, "Aja Ekapada"),
        Nakshatra(26, "Uttara Bhadrapada", Planet.SATURN, "Ahir Budhnya"),
        Nakshatra(27, "Revati", Planet.MERCURY, "Pushan")
    )

    fun calculateJulianDay(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        timezoneOffsetHours: Double
    ): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = 2 - a + (a / 4)
        val decimalDay = day + (hour + minute / 60.0 - timezoneOffsetHours) / 24.0
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + decimalDay + b - 1524.5
    }

    fun calculateLahiriAyanamsha(jd: Double): Double {
        val t = (jd - 2451545.0) / 36525.0
        // Lahiri Ayanamsha formula
        val ayanamsha = 23.856 + 1.39697 * t + 0.000308 * t * t
        return ayanamsha
    }

    fun calculateAscendant(
        jd: Double,
        latitude: Double,
        longitude: Double,
        ayanamsha: Double
    ): Pair<Double, ZodiacSign> {
        val t = (jd - 2451545.0) / 36525.0
        // Greenwich Mean Sidereal Time in degrees
        var gmst = 280.46061837 + 360.98564736629 * (jd - 2451545.0) + 0.000387933 * t * t
        gmst = normalizeDegrees(gmst)

        // Local Sidereal Time (RAMC)
        val lst = normalizeDegrees(gmst + longitude)
        val theta = Math.toRadians(lst)
        val phi = Math.toRadians(latitude)
        val eps = Math.toRadians(23.439291 - 0.0130042 * t)

        // Ascendant calculation
        val y = cos(theta)
        val x = -sin(theta) * cos(eps) - tan(phi) * sin(eps)
        var tropicalAsc = Math.toDegrees(atan2(y, x))
        tropicalAsc = normalizeDegrees(tropicalAsc)

        val siderealAsc = normalizeDegrees(tropicalAsc - ayanamsha)
        val sign = ZodiacSign.fromLongitude(siderealAsc).first
        return Pair(siderealAsc, sign)
    }

    fun calculateChart(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        latitude: Double,
        longitude: Double,
        timezoneOffsetHours: Double
    ): ChartData {
        val jd = calculateJulianDay(year, month, day, hour, minute, timezoneOffsetHours)
        val ayanamsha = calculateLahiriAyanamsha(jd)
        val (ascDeg, ascSign) = calculateAscendant(jd, latitude, longitude, ayanamsha)

        val t = (jd - 2451545.0) / 36525.0
        val d = jd - 2451545.0

        // Calculate tropical coordinates
        val sunTropical = calculateSunLongitude(d, t)
        val moonTropical = calculateMoonLongitude(d, t)
        val mercuryTropical = calculateMercuryLongitude(d, t)
        val venusTropical = calculateVenusLongitude(d, t)
        val marsTropical = calculateMarsLongitude(d, t)
        val jupiterTropical = calculateJupiterLongitude(d, t)
        val saturnTropical = calculateSaturnLongitude(d, t)
        val rahuTropical = calculateRahuLongitude(d, t)
        val ketuTropical = normalizeDegrees(rahuTropical + 180.0)

        // Calculate sidereal positions
        val rawPositions = listOf(
            Triple(Planet.SUN, normalizeDegrees(sunTropical - ayanamsha), false),
            Triple(Planet.MOON, normalizeDegrees(moonTropical - ayanamsha), false),
            Triple(Planet.MARS, normalizeDegrees(marsTropical - ayanamsha), isMarsRetrograde(marsTropical, sunTropical)),
            Triple(Planet.MERCURY, normalizeDegrees(mercuryTropical - ayanamsha), isMercuryRetrograde(mercuryTropical, sunTropical)),
            Triple(Planet.JUPITER, normalizeDegrees(jupiterTropical - ayanamsha), isJupiterRetrograde(jupiterTropical, sunTropical)),
            Triple(Planet.VENUS, normalizeDegrees(venusTropical - ayanamsha), isVenusRetrograde(venusTropical, sunTropical)),
            Triple(Planet.SATURN, normalizeDegrees(saturnTropical - ayanamsha), isSaturnRetrograde(saturnTropical, sunTropical)),
            Triple(Planet.RAHU, normalizeDegrees(rahuTropical - ayanamsha), true), // Always retrograde in Vedic
            Triple(Planet.KETU, normalizeDegrees(ketuTropical - ayanamsha), true)
        )

        val sunSidereal = normalizeDegrees(sunTropical - ayanamsha)

        val planetPositions = rawPositions.map { (planet, siderealDeg, isRetro) ->
            val (sign, degInSign) = ZodiacSign.fromLongitude(siderealDeg)
            // Vedic Whole Sign House: House 1 is the sign containing Ascendant
            val house = ((sign.number - ascSign.number + 12) % 12) + 1
            val (nakshatra, pada) = calculateNakshatra(siderealDeg)

            // Combustion check (orb distance to Sun)
            val isCombust = if (planet != Planet.SUN && !planet.isNode) {
                val dist = abs(angularDistance(siderealDeg, sunSidereal))
                val combustionOrb = when (planet) {
                    Planet.MOON -> 12.0
                    Planet.MARS -> 17.0
                    Planet.MERCURY -> 14.0
                    Planet.JUPITER -> 11.0
                    Planet.VENUS -> 10.0
                    Planet.SATURN -> 15.0
                    else -> 0.0
                }
                dist <= combustionOrb
            } else false

            // Dignity determination
            val dignity = evaluateDignity(planet, sign, degInSign)

            PlanetPosition(
                planet = planet,
                longitude = siderealDeg,
                sign = sign,
                degreeInSign = degInSign,
                house = house,
                nakshatra = nakshatra,
                pada = pada,
                isRetrograde = isRetro,
                isCombust = isCombust,
                dignity = dignity
            )
        }

        // Calculate all 16 Vargas
        val vargas = VargaCalculator.calculateAllVargas(planetPositions, ascDeg, ascSign)

        // Check Vargottama: Planet in same sign in D1 (Rashi) and D9 (Navamsha)
        val d9Chart = vargas[VargaType.D9]
        val updatedPlanets = planetPositions.map { p ->
            val d9Placement = d9Chart?.placements?.firstOrNull { it.planet == p.planet }
            val isVargottama = d9Placement?.sign == p.sign
            p.copy(vargottama = isVargottama)
        }

        return ChartData(
            ascendantDegree = ascDeg,
            ascendantSign = ascSign,
            ayanamsha = ayanamsha,
            planets = updatedPlanets,
            vargaCharts = vargas
        )
    }

    private fun evaluateDignity(planet: Planet, sign: ZodiacSign, degInSign: Double): Dignity {
        return when (planet) {
            Planet.SUN -> when {
                sign == ZodiacSign.ARIES && degInSign <= 10.0 -> Dignity.EXALTED
                sign == ZodiacSign.ARIES -> Dignity.EXALTED
                sign == ZodiacSign.LIBRA -> Dignity.DEBILITATED
                sign == ZodiacSign.LEO && degInSign <= 20.0 -> Dignity.MOOLATRIKONA
                sign == ZodiacSign.LEO -> Dignity.OWN_SIGN
                sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES, ZodiacSign.SCORPIO, ZodiacSign.CANCER) -> Dignity.FRIEND
                sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO) -> Dignity.NEUTRAL
                else -> Dignity.ENEMY
            }
            Planet.MOON -> when {
                sign == ZodiacSign.TAURUS && degInSign <= 3.0 -> Dignity.EXALTED
                sign == ZodiacSign.TAURUS -> Dignity.MOOLATRIKONA
                sign == ZodiacSign.SCORPIO -> Dignity.DEBILITATED
                sign == ZodiacSign.CANCER -> Dignity.OWN_SIGN
                sign in listOf(ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS, ZodiacSign.GEMINI, ZodiacSign.VIRGO) -> Dignity.FRIEND
                else -> Dignity.NEUTRAL
            }
            Planet.MARS -> when {
                sign == ZodiacSign.CAPRICORN -> Dignity.EXALTED
                sign == ZodiacSign.CANCER -> Dignity.DEBILITATED
                sign == ZodiacSign.ARIES && degInSign <= 12.0 -> Dignity.MOOLATRIKONA
                sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO) -> Dignity.OWN_SIGN
                sign in listOf(ZodiacSign.LEO, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES) -> Dignity.FRIEND
                sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS) -> Dignity.NEUTRAL
                else -> Dignity.ENEMY
            }
            Planet.MERCURY -> when {
                sign == ZodiacSign.VIRGO && degInSign <= 15.0 -> Dignity.EXALTED
                sign == ZodiacSign.VIRGO && degInSign <= 20.0 -> Dignity.MOOLATRIKONA
                sign in listOf(ZodiacSign.VIRGO, ZodiacSign.GEMINI) -> Dignity.OWN_SIGN
                sign == ZodiacSign.PISCES -> Dignity.DEBILITATED
                sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA, ZodiacSign.LEO) -> Dignity.FRIEND
                sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO, ZodiacSign.SAGITTARIUS, ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS) -> Dignity.NEUTRAL
                else -> Dignity.ENEMY
            }
            Planet.JUPITER -> when {
                sign == ZodiacSign.CANCER -> Dignity.EXALTED
                sign == ZodiacSign.CAPRICORN -> Dignity.DEBILITATED
                sign == ZodiacSign.SAGITTARIUS && degInSign <= 10.0 -> Dignity.MOOLATRIKONA
                sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES) -> Dignity.OWN_SIGN
                sign in listOf(ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SCORPIO) -> Dignity.FRIEND
                sign in listOf(ZodiacSign.AQUARIUS) -> Dignity.NEUTRAL
                else -> Dignity.ENEMY
            }
            Planet.VENUS -> when {
                sign == ZodiacSign.PISCES -> Dignity.EXALTED
                sign == ZodiacSign.VIRGO -> Dignity.DEBILITATED
                sign == ZodiacSign.LIBRA && degInSign <= 15.0 -> Dignity.MOOLATRIKONA
                sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA) -> Dignity.OWN_SIGN
                sign in listOf(ZodiacSign.GEMINI, ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS) -> Dignity.FRIEND
                sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO) -> Dignity.NEUTRAL
                else -> Dignity.ENEMY
            }
            Planet.SATURN -> when {
                sign == ZodiacSign.LIBRA -> Dignity.EXALTED
                sign == ZodiacSign.ARIES -> Dignity.DEBILITATED
                sign == ZodiacSign.AQUARIUS && degInSign <= 20.0 -> Dignity.MOOLATRIKONA
                sign in listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS) -> Dignity.OWN_SIGN
                sign in listOf(ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.VIRGO) -> Dignity.FRIEND
                sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES) -> Dignity.NEUTRAL
                else -> Dignity.ENEMY
            }
            Planet.RAHU -> when (sign) {
                ZodiacSign.TAURUS, ZodiacSign.GEMINI -> Dignity.EXALTED
                ZodiacSign.VIRGO -> Dignity.OWN_SIGN
                ZodiacSign.SCORPIO, ZodiacSign.SAGITTARIUS -> Dignity.DEBILITATED
                else -> Dignity.NEUTRAL
            }
            Planet.KETU -> when (sign) {
                ZodiacSign.SCORPIO, ZodiacSign.SAGITTARIUS -> Dignity.EXALTED
                ZodiacSign.PISCES -> Dignity.OWN_SIGN
                ZodiacSign.TAURUS, ZodiacSign.GEMINI -> Dignity.DEBILITATED
                else -> Dignity.NEUTRAL
            }
            Planet.ASCENDANT -> Dignity.NEUTRAL
        }
    }

    private fun calculateNakshatra(siderealDeg: Double): Pair<Nakshatra, Int> {
        val deg = normalizeDegrees(siderealDeg)
        val nakshatraSpan = 360.0 / 27.0 // 13.3333333 degrees = 13° 20'
        val index = (deg / nakshatraSpan).toInt().coerceIn(0, 26)
        val degInNakshatra = deg % nakshatraSpan
        val padaSpan = nakshatraSpan / 4.0 // 3° 20'
        val pada = ((degInNakshatra / padaSpan).toInt() + 1).coerceIn(1, 4)
        return Pair(NAKSHATRAS[index], pada)
    }

    // Planetary positions using standard analytical ephemeris
    private fun calculateSunLongitude(d: Double, t: Double): Double {
        val l0 = 280.46646 + 36000.76983 * t
        val m = Math.toRadians(357.52911 + 35999.05029 * t)
        val c = (1.914602 - 0.004817 * t) * sin(m) + (0.019993 - 0.000101 * t) * sin(2 * m)
        return normalizeDegrees(l0 + c)
    }

    private fun calculateMoonLongitude(d: Double, t: Double): Double {
        val l0 = 218.3164477 + 481267.88123421 * t
        val dE = Math.toRadians(297.8501921 + 445267.1114034 * t)
        val m = Math.toRadians(357.5291092 + 35999.0502909 * t)
        val mPrime = Math.toRadians(134.9633964 + 477198.8675055 * t)
        val f = Math.toRadians(93.2720950 + 483202.0175233 * t)

        val periodic = 6.288774 * sin(mPrime) +
                1.274027 * sin(2 * dE - mPrime) +
                0.658314 * sin(2 * dE) +
                0.213618 * sin(2 * mPrime) -
                0.185116 * sin(m) -
                0.114332 * sin(2 * f)
        return normalizeDegrees(l0 + periodic)
    }

    private fun calculateMercuryLongitude(d: Double, t: Double): Double {
        val n = normalizeDegrees(48.3313 + 3.24587E-5 * d)
        val i = Math.toRadians(7.0047 + 5.00E-8 * d)
        val w = normalizeDegrees(29.1241 + 1.01444E-5 * d)
        val a = 0.387098
        val e = 0.205635 + 5.59E-10 * d
        val m = normalizeDegrees(168.6562 + 4.0923344368 * d)
        return calculateHeliocentricToGeocentric(a, e, m, w, n, i, d, t)
    }

    private fun calculateVenusLongitude(d: Double, t: Double): Double {
        val n = normalizeDegrees(76.6799 + 2.46590E-5 * d)
        val i = Math.toRadians(3.3946 + 2.75E-8 * d)
        val w = normalizeDegrees(54.8910 + 1.38374E-5 * d)
        val a = 0.723330
        val e = 0.006773 - 1.302E-9 * d
        val m = normalizeDegrees(48.0052 + 1.6021302244 * d)
        return calculateHeliocentricToGeocentric(a, e, m, w, n, i, d, t)
    }

    private fun calculateMarsLongitude(d: Double, t: Double): Double {
        val n = normalizeDegrees(49.5574 + 2.11081E-5 * d)
        val i = Math.toRadians(1.8497 - 1.78E-8 * d)
        val w = normalizeDegrees(286.5016 + 2.92961E-5 * d)
        val a = 1.523688
        val e = 0.093405 + 2.516E-9 * d
        val m = normalizeDegrees(18.6021 + 0.5240207766 * d)
        return calculateHeliocentricToGeocentric(a, e, m, w, n, i, d, t)
    }

    private fun calculateJupiterLongitude(d: Double, t: Double): Double {
        val n = normalizeDegrees(100.4542 + 2.76854E-5 * d)
        val i = Math.toRadians(1.3030 - 1.557E-7 * d)
        val w = normalizeDegrees(273.8777 + 1.64505E-5 * d)
        val a = 5.20256
        val e = 0.048498 + 4.469E-9 * d
        val m = normalizeDegrees(19.8950 + 0.0830853001 * d)
        return calculateHeliocentricToGeocentric(a, e, m, w, n, i, d, t)
    }

    private fun calculateSaturnLongitude(d: Double, t: Double): Double {
        val n = normalizeDegrees(113.6634 + 2.38980E-5 * d)
        val i = Math.toRadians(2.4886 - 1.081E-7 * d)
        val w = normalizeDegrees(339.3939 + 2.97661E-5 * d)
        val a = 9.55475
        val e = 0.055546 - 9.499E-9 * d
        val m = normalizeDegrees(316.9670 + 0.0334442282 * d)
        return calculateHeliocentricToGeocentric(a, e, m, w, n, i, d, t)
    }

    private fun calculateRahuLongitude(d: Double, t: Double): Double {
        // Lunar Mean Ascending Node
        val omega = 125.04452 - 1934.136261 * t + 0.0020708 * t * t
        return normalizeDegrees(omega)
    }

    private fun calculateHeliocentricToGeocentric(
        a: Double,
        e: Double,
        mDeg: Double,
        wDeg: Double,
        nDeg: Double,
        iRad: Double,
        d: Double,
        t: Double
    ): Double {
        val mRad = Math.toRadians(mDeg)
        // Kepler equation solution E = M + e*sin(E)
        var eAnomaly = mRad + e * sin(mRad) * (1.0 + e * cos(mRad))
        for (step in 0..3) {
            eAnomaly -= (eAnomaly - e * sin(eAnomaly) - mRad) / (1.0 - e * cos(eAnomaly))
        }

        val xv = a * (cos(eAnomaly) - e)
        val yv = a * (sqrt(1.0 - e * e) * sin(eAnomaly))
        val v = atan2(yv, xv)
        val r = sqrt(xv * xv + yv * yv)

        val wRad = Math.toRadians(wDeg)
        val nRad = Math.toRadians(nDeg)

        // Heliocentric rectangular coordinates
        val xh = r * (cos(nRad) * cos(v + wRad) - sin(nRad) * sin(v + wRad) * cos(iRad))
        val yh = r * (sin(nRad) * cos(v + wRad) + cos(nRad) * sin(v + wRad) * cos(iRad))
        val zh = r * (sin(v + wRad) * sin(iRad))

        // Earth heliocentric coordinates
        val sunLonRad = Math.toRadians(calculateSunLongitude(d, t))
        val xs = cos(sunLonRad)
        val ys = sin(sunLonRad)

        // Geocentric coordinates (Earth to planet)
        val xg = xh + xs
        val yg = yh + ys

        return normalizeDegrees(Math.toDegrees(atan2(yg, xg)))
    }

    private fun isMercuryRetrograde(mercuryLon: Double, sunLon: Double): Boolean {
        val diff = angularDistance(mercuryLon, sunLon)
        return abs(diff) < 28.0 && diff < 0 // When approaching interior conjunction
    }

    private fun isVenusRetrograde(venusLon: Double, sunLon: Double): Boolean {
        val diff = angularDistance(venusLon, sunLon)
        return abs(diff) < 47.0 && diff < -30.0
    }

    private fun isMarsRetrograde(marsLon: Double, sunLon: Double): Boolean {
        val diff = abs(angularDistance(marsLon, sunLon))
        return diff > 135.0 // Outer planets retrograde when opposing the Sun
    }

    private fun isJupiterRetrograde(jupiterLon: Double, sunLon: Double): Boolean {
        val diff = abs(angularDistance(jupiterLon, sunLon))
        return diff > 120.0
    }

    private fun isSaturnRetrograde(saturnLon: Double, sunLon: Double): Boolean {
        val diff = abs(angularDistance(saturnLon, sunLon))
        return diff > 110.0
    }

    private fun angularDistance(a: Double, b: Double): Double {
        var diff = (a - b) % 360.0
        if (diff > 180.0) diff -= 360.0
        if (diff < -180.0) diff += 360.0
        return diff
    }

    fun normalizeDegrees(deg: Double): Double {
        var d = deg % 360.0
        if (d < 0.0) d += 360.0
        return d
    }
}
