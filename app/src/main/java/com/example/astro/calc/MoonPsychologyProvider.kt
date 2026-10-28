package com.example.astro.calc

import com.example.astro.model.MoonProfile
import com.example.astro.model.PlanetPosition
import com.example.astro.model.ZodiacSign

object MoonPsychologyProvider {

    fun generateMoonProfile(chart: com.example.astro.model.ChartData): MoonProfile? {
        val moonPos = chart.getPlanet(com.example.astro.model.Planet.MOON) ?: return null
        return generateProfile(moonPos)
    }

    fun generateProfile(moonPos: PlanetPosition): MoonProfile {
        val sign = moonPos.sign

        val (nature, signature, needs, stress, strengths, blindSpots) = when (sign) {
            ZodiacSign.ARIES -> SixTuple(
                "Dynamic, passionate, impatient, and instinctive",
                "Pioneering, courageous, and action-oriented mental tempo. Your mind reacts instantly to stimuli with raw emotional honesty.",
                "Freedom to initiate without micro-management, physical outlets for nervous energy, and direct, unfiltered communication.",
                "Impatience, irritability, and headache or tension spikes when forced to wait or submit to bureaucracy.",
                listOf("Fearless emotional resilience", "Innate pioneering initiative", "Rapid recovery from disappointment"),
                listOf("Impulsive reactivity without pause", "Combative stance when feeling vulnerable", "Boredom with routine follow-through")
            )
            ZodiacSign.TAURUS -> SixTuple(
                "Grounded, affectionate, steady, and sensory-anchored",
                "Serene, sensual, and extraordinarily patient emotional processing. Moon is exalted here, granting deep emotional stability.",
                "Predictable physical routines, financial solvency, tactile comfort, and dependable loyal companions.",
                "Stubborn resistance to change, comfort eating, and silent emotional entrenchment when routines are threatened.",
                listOf("Remarkable emotional composure under pressure", "Deep sensory and aesthetic refinement", "Unshakable loyalty to loved ones"),
                listOf("Reluctance to leave toxic comfort zones", "Possessiveness or material attachment", "Passive-aggressive withdrawal")
            )
            ZodiacSign.GEMINI -> SixTuple(
                "Inquisitive, communicative, versatile, and cerebral",
                "Restless, intellectual, and humorous mind. You process feelings through conversation, reading, and conceptual analysis.",
                "Mental stimulation, intellectual freedom, diverse social circles, and variety in daily engagements.",
                "Nervous anxiety, scattered focus, cognitive overwhelm, and insomnia from excessive mental buzzing.",
                listOf("Brilliant conversational charm", "Agile emotional compartmentalization", "Quick wit and adaptable humor"),
                listOf("Intellectualizing emotions instead of feeling them", "Fickle attention span", "Restlessness in long-term silence")
            )
            ZodiacSign.CANCER -> SixTuple(
                "Intuitive, deeply empathic, protective, and maternal",
                "Oceanic, profoundly sensitive, and protective emotional constitution. Moon rules Cancer in sovereign comfort.",
                "Domestic sanctuary, unconditional emotional reciprocation, honoring ancestral memories, and safe vulnerability.",
                "Moody withdrawal into your protective shell, emotional defensiveness, and brooding over perceived past slights.",
                listOf("Incredible instinctual gut radar", "Profound empathy and nurturing capacity", "Tenacious loyalty and protective care"),
                listOf("Smothering or overprotective tendencies", "Hyper-sensitivity to tone and atmosphere", "Emotional hoarding of grievances")
            )
            ZodiacSign.LEO -> SixTuple(
                "Noble, warm-hearted, expressive, and magnanimous",
                "Dignified, generous, and expressive mind. You experience emotions theatrically and desire sincere mutual admiration.",
                "Authentic appreciation, validation of creative gifts, dignified respect, and loyalty from peer circles.",
                "Wounded pride, dramatic hurt, emotional exhaustion from projecting perpetual strength to the public.",
                listOf("Warm, infectious optimism", "Natural inspirational leadership", "Generous protection of those you love"),
                listOf("Taking casual feedback as personal insult", "Need for constant applause or validation", "Difficulty admitting private weakness")
            )
            ZodiacSign.VIRGO -> SixTuple(
                "Analytical, modest, detail-attuned, and service-oriented",
                "Methodical, discerning, and self-improving mind. You show love through practical acts of care and problem-solving.",
                "Orderly living environment, tangible usefulness, mental clarity, and clear logical expectations.",
                "Hypochondria, relentless self-criticism, and compulsive perfectionist anxiety when chaos occurs.",
                listOf("Superb analytical emotional discernment", "Dedicated practical caregiving", "Meticulous organization and work ethic"),
                listOf("Chronic inner critic and over-scrutiny", "Worrying about uncontrollable futures", "Struggling to relax without guilt")
            )
            ZodiacSign.LIBRA -> SixTuple(
                "Diplomatic, gracious, harmonious, and partner-centric",
                "Aesthetic, balanced, and peace-seeking mind. You experience internal peace when surrounding social dynamics are harmonious.",
                "Collaborative partnership, polite social graces, beautiful aesthetic surroundings, and fairness in disputes.",
                "Analysis paralysis, conflict avoidance that builds hidden resentment, and losing identity inside relationships.",
                listOf("Peerless diplomatic mediator skills", "Graceful aesthetic and artistic discernment", "Gentle, magnetic social tact"),
                listOf("Compulsive people-pleasing", "Indecisiveness when forced to pick sides", "Suppressing authentic rage for fake harmony")
            )
            ZodiacSign.SCORPIO -> SixTuple(
                "Intense, investigative, transformative, and guarded",
                "Deep, penetrating, and fiercely perceptive mind. Moon is debilitated here, requiring profound inner spiritual alchemy.",
                "Absolute emotional authenticity, ironclad privacy, psychological depth, and unwavering trust.",
                "Obsessive brooding, paranoia, suspicion, and destructive emotional volcanism when betrayed.",
                listOf("Unyielding psychological courage", "Ability to regenerate completely from ashes", "Uncanny detection of hidden lies"),
                listOf("Holding lifelong grudges", "Controlling behavior born of vulnerability fears", "Difficulty forgiving honest mistakes")
            )
            ZodiacSign.SAGITTARIUS -> SixTuple(
                "Optimistic, philosophical, adventurous, and candid",
                "Expansive, truthful, and philosophical mind. You process emotional challenges through higher spiritual meaning and humor.",
                "Intellectual and physical freedom, travel, philosophical discussions, and room to seek higher truth.",
                "Restless escapism, blunt tactlessness, and dismissing heavy emotional nuance with simplistic optimism.",
                listOf("Inspiring, uplifting generosity", "Broad philosophical perspective", "Infectious joyful resilience"),
                listOf("Running away from tedious emotional processing", "Preaching unsolicited moral sermons", "Fear of restrictive commitment")
            )
            ZodiacSign.CAPRICORN -> SixTuple(
                "Pragmatic, reserved, dutiful, and structurally mature",
                "Disciplined, stoic, and realistic mind. You process emotions through duty, tangible accomplishments, and self-reliance.",
                "Structured long-term security, professional respect, financial resilience, and emotional reliability.",
                "Melancholy, chronic emotional austerity, feeling the weight of the entire world upon your shoulders alone.",
                listOf("Unmatched emotional grit and endurance", "Wise, mature, calm crisis management", "Integrity and steadfast dependability"),
                listOf("Emotional coldness or harsh self-denial", "Believing vulnerability equals weakness", "Pessimistic default expectations")
            )
            ZodiacSign.AQUARIUS -> SixTuple(
                "Humanitarian, unconventional, intellectual, and visionary",
                "Objective, independent, and socially conscious mind. You observe emotions from a panoramic, detached vantage point.",
                "Individual autonomy, intellectual freedom, community belonging, and unconventional pursuits.",
                "Emotional alienation, feeling like an misunderstood outsider, and detached aloofness when intimacy deepens.",
                listOf("Radical open-mindedness and egalitarian vision", "Calm, objective crisis navigation", "Original, progressive thinking"),
                listOf("Stubborn emotional detachment", "Dismissing irrational feelings as illogical", "Difficulty expressing visceral tenderness")
            )
            ZodiacSign.PISCES -> SixTuple(
                "Mystical, compassionate, boundless, and poetic",
                "Boundless, dream-rich, and spiritually porous mind. You soak up collective cosmic currents and subtle vibrations.",
                "Quiet retreat time, artistic expression, spiritual grounding, and gentle, loving environments.",
                "Emotional overwhelm, psychic sponging of others' negativity, escapism, and victim-martyr complexes.",
                listOf("Transcendent spiritual compassion", "Rich artistic, musical, and poetic imagination", "Profound healing and psychic sensitivity"),
                listOf("Poor boundary setting with toxic people", "Compulsive escapism through sleep or fantasy", "Difficulty confronting hard worldly facts")
            )
        }

        return MoonProfile(
            sign = sign,
            degreeInSign = moonPos.degreeInSign,
            nakshatra = moonPos.nakshatra,
            pada = moonPos.pada,
            emotionalNature = nature,
            psychologicalSignature = signature,
            instinctualSecurityNeeds = needs,
            stressResponse = stress,
            mentalStrengths = strengths,
            emotionalBlindSpots = blindSpots
        )
    }

    private data class SixTuple(
        val nature: String,
        val signature: String,
        val needs: String,
        val stress: String,
        val strengths: List<String>,
        val blindSpots: List<String>
    )
}
