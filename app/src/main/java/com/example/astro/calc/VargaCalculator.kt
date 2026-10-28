package com.example.astro.calc

import com.example.astro.model.*

object VargaCalculator {

    fun calculateAllVargas(
        planets: List<PlanetPosition>,
        ascDegree: Double,
        ascSign: ZodiacSign
    ): Map<VargaType, VargaChart> {
        val result = mutableMapOf<VargaType, VargaChart>()
        for (varga in VargaType.entries) {
            val ascVargaSign = calculateSignForVarga(varga, ascSign, ascDegree % 30.0, ascDegree)
            val placements = planets.map { p ->
                val vargaSign = calculateSignForVarga(varga, p.sign, p.degreeInSign, p.longitude)
                VargaPlacement(p.planet, vargaSign)
            }
            result[varga] = VargaChart(varga, ascVargaSign, placements)
        }
        return result
    }

    fun calculateSignForVarga(
        varga: VargaType,
        sign: ZodiacSign,
        degreeInSign: Double,
        fullLongitude: Double
    ): ZodiacSign {
        val deg = degreeInSign.coerceIn(0.0, 29.999999)
        val signNum = sign.number
        val isOdd = signNum % 2 != 0

        return when (varga) {
            VargaType.D1 -> sign

            VargaType.D2 -> { // Hora (15° divisions)
                if (isOdd) {
                    if (deg < 15.0) ZodiacSign.LEO else ZodiacSign.CANCER
                } else {
                    if (deg < 15.0) ZodiacSign.CANCER else ZodiacSign.LEO
                }
            }

            VargaType.D3 -> { // Drekkana (10° divisions)
                val part = (deg / 10.0).toInt().coerceIn(0, 2)
                when (part) {
                    0 -> sign
                    1 -> ZodiacSign.fromNumber(signNum + 4)
                    else -> ZodiacSign.fromNumber(signNum + 8)
                }
            }

            VargaType.D4 -> { // Chaturthamsha (7.5° divisions)
                val part = (deg / 7.5).toInt().coerceIn(0, 3)
                ZodiacSign.fromNumber(signNum + part * 3)
            }

            VargaType.D7 -> { // Saptamsha (4.2857° divisions)
                val part = (deg / (30.0 / 7.0)).toInt().coerceIn(0, 6)
                if (isOdd) {
                    ZodiacSign.fromNumber(signNum + part)
                } else {
                    ZodiacSign.fromNumber(signNum + 6 + part)
                }
            }

            VargaType.D9 -> { // Navamsha (3°20' divisions)
                // Total navamsha number from 0 Aries
                val totalNavamshas = (fullLongitude / (360.0 / 108.0)).toInt() % 12
                ZodiacSign.fromNumber(totalNavamshas + 1)
            }

            VargaType.D10 -> { // Dashamsha (3° divisions)
                val part = (deg / 3.0).toInt().coerceIn(0, 9)
                if (isOdd) {
                    ZodiacSign.fromNumber(signNum + part)
                } else {
                    ZodiacSign.fromNumber(signNum + 8 + part)
                }
            }

            VargaType.D12 -> { // Dwadashamsha (2.5° divisions)
                val part = (deg / 2.5).toInt().coerceIn(0, 11)
                ZodiacSign.fromNumber(signNum + part)
            }

            VargaType.D16 -> { // Shodashamsha (1.875° divisions)
                val part = (deg / (30.0 / 16.0)).toInt().coerceIn(0, 15)
                val startSign = when (sign.quality) {
                    "Movable" -> 1 // Aries
                    "Fixed" -> 5 // Leo
                    else -> 9 // Sagittarius
                }
                ZodiacSign.fromNumber(startSign + part)
            }

            VargaType.D20 -> { // Vimshamsha (1.5° divisions)
                val part = (deg / 1.5).toInt().coerceIn(0, 19)
                val startSign = when (sign.quality) {
                    "Movable" -> 1 // Aries
                    "Fixed" -> 9 // Sagittarius
                    else -> 5 // Leo
                }
                ZodiacSign.fromNumber(startSign + part)
            }

            VargaType.D24 -> { // Chaturvimshamsha (1.25° divisions)
                val part = (deg / 1.25).toInt().coerceIn(0, 23)
                val startSign = if (isOdd) 5 else 4 // Leo or Cancer
                ZodiacSign.fromNumber(startSign + part)
            }

            VargaType.D27 -> { // Saptavimshamsha (1.1111° divisions)
                val part = (deg / (30.0 / 27.0)).toInt().coerceIn(0, 26)
                val startSign = when (sign.element) {
                    "Fire" -> 1 // Aries
                    "Earth" -> 4 // Cancer
                    "Air" -> 7 // Libra
                    else -> 10 // Capricorn
                }
                ZodiacSign.fromNumber(startSign + part)
            }

            VargaType.D30 -> { // Trimshamsha (karmic evils/flaws)
                if (isOdd) {
                    when {
                        deg < 5.0 -> ZodiacSign.ARIES // Mars
                        deg < 10.0 -> ZodiacSign.AQUARIUS // Saturn
                        deg < 18.0 -> ZodiacSign.SAGITTARIUS // Jupiter
                        deg < 25.0 -> ZodiacSign.GEMINI // Mercury
                        else -> ZodiacSign.LIBRA // Venus
                    }
                } else {
                    when {
                        deg < 5.0 -> ZodiacSign.TAURUS // Venus
                        deg < 12.0 -> ZodiacSign.VIRGO // Mercury
                        deg < 20.0 -> ZodiacSign.PISCES // Jupiter
                        deg < 25.0 -> ZodiacSign.CAPRICORN // Saturn
                        else -> ZodiacSign.SCORPIO // Mars
                    }
                }
            }

            VargaType.D40 -> { // Khavedamsha (0.75° divisions)
                val part = (deg / 0.75).toInt().coerceIn(0, 39)
                val startSign = if (isOdd) 1 else 7 // Aries or Libra
                ZodiacSign.fromNumber(startSign + part)
            }

            VargaType.D45 -> { // Akshavedamsha (0.6666° divisions)
                val part = (deg / (30.0 / 45.0)).toInt().coerceIn(0, 44)
                val startSign = when (sign.quality) {
                    "Movable" -> 1 // Aries
                    "Fixed" -> 5 // Leo
                    else -> 9 // Sagittarius
                }
                ZodiacSign.fromNumber(startSign + part)
            }

            VargaType.D60 -> { // Shashtiamsha (0.5° divisions)
                val part = (deg / 0.5).toInt().coerceIn(0, 59)
                ZodiacSign.fromNumber(signNum + part)
            }
        }
    }
}
