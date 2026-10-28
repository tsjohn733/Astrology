package com.example.ai

import android.util.Log
import com.example.astro.model.ChartData
import com.example.astro.model.DailyPrediction
import com.example.astro.model.Planet
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout

private const val TAG = "AstrologyAiService"

class AstrologyAiService {

    private val model by lazy {
        Firebase.ai.generativeModel("gemini-3.8-flash")
    }

    fun answerAstrologyQuestionStream(
        question: String,
        chart: ChartData,
        profileName: String,
        transitsSummary: String = ""
    ): Flow<String> = flow {
        val chartContext = buildString {
            append("User Profile: $profileName\n")
            append("Ascendant (Lagna): ${chart.ascendantSign.englishName} (${chart.ascendantSign.sanskritName}) at ${String.format("%.2f", chart.ascendantDegree % 30.0)}°\n")
            append("Lahiri Ayanamsha: ${String.format("%.2f", chart.ayanamsha)}°\n")
            append("Planetary Placements (D1 Rashi):\n")
            chart.planets.forEach { p ->
                append(" - ${p.planet.englishName} (${p.planet.sanskritName}): ${p.sign.englishName} in House ${p.house}, ")
                append("${String.format("%.2f", p.degreeInSign)}°, Nakshatra: ${p.nakshatra.name} Pada ${p.pada}, ")
                append("Dignity: ${p.dignity.label}")
                if (p.isRetrograde) append(", Retrograde (Vakri)")
                if (p.isCombust) append(", Combust (Asta)")
                if (p.vargottama) append(", Vargottama (in D1 & D9)")
                append("\n")
            }

            // Key divisional charts summary
            val d9 = chart.vargaCharts[com.example.astro.model.VargaType.D9]
            if (d9 != null) {
                append("Navamsha (D9 - Dharmamsha/Marriage/Soul):\n")
                append(" - D9 Lagna: ${d9.ascendantSign.englishName}\n")
                d9.placements.forEach { pl ->
                    append(" - ${pl.planet.englishName}: ${pl.sign.englishName}; ")
                }
                append("\n")
            }

            val d10 = chart.vargaCharts[com.example.astro.model.VargaType.D10]
            if (d10 != null) {
                append("Dashamsha (D10 - Career/Authority):\n")
                append(" - D10 Lagna: ${d10.ascendantSign.englishName}\n")
            }

            if (transitsSummary.isNotBlank()) {
                append("Current Transits Context: $transitsSummary\n")
            }
        }

        val prompt = """
            You are a revered, deeply knowledgeable, and empathetic Vedic Astrologer (Jyotishi) and holistic life mentor.
            The user is seeking personalized astrological guidance and clarity based on their authentic Vedic birth chart.
            
            CHART DATA:
            $chartContext
            
            USER QUESTION:
            "$question"
            
            INSTRUCTIONS:
            1. Address the question with warmth, precision, and authentic Vedic principles (Parashara / Jaimini philosophy).
            2. Explain WHY and WHICH specific planets, houses, aspects (Drishti), or divisional charts (such as Navamsha D9 or Dashamsha D10) govern this issue.
            3. Demystify the root cause clearly: if a planet is afflicted, debilitated, retrograde, or combust, explain what internal psychological or behavioral tension it reflects.
            4. Offer grounded, practical lifestyle remedies and behavioral adjustments (such as routine, mindset, diet, communication style, or mindfulness practices) that align with the cosmic energies.
            5. Maintain a constructive, empowering tone that encourages conscious agency and personal growth, rather than fatalism.
            6. Format cleanly with clear headings, bullet points, and elegant prose.
        """.trimIndent()

        try {
            withTimeout(60_000L) {
                model.generateContentStream(prompt).collect { chunk ->
                    val text = chunk.text.orEmpty()
                    if (text.isNotEmpty()) {
                        emit(text)
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Astrology AI consultation timed out", e)
            emit("\n\n*(The consultation request timed out after 60 seconds. Please check network connection and try again.)*")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to generate astrological guidance", e)
            emit("\n\n*(Astrological AI Guidance service encountered an issue: ${e.localizedMessage ?: "Unknown error"}. Please ensure internet connectivity.)*")
        }
    }.flowOn(Dispatchers.IO)
}
