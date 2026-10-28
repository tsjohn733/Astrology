package com.example.astro.data

import com.example.astro.model.*

object PlanetaryInsightProvider {

    fun getInsightForPlanet(position: PlanetPosition): PlanetaryInsight {
        val p = position.planet
        val h = position.house
        val s = position.sign
        val d = position.dignity

        val rootWhy = when (p) {
            Planet.SUN -> "The Sun represents the Atma (core soul identity, willpower, and personal authority). Located in House $h (${s.englishName}), your sense of self and vitality is deeply directed toward ${getHouseSignification(h)}. Because its dignity is ${d.label}, your internal confidence operates ${getDignityOperation(d)}."
            Planet.MOON -> "The Moon rules the Manas (subconscious mind, emotional reactivity, and psychological security). In House $h (${s.englishName}), your emotional equilibrium is tied to ${getHouseSignification(h)}. Its ${d.label} status reveals how smoothly your mind processes stressors and adapts to fluctuating life tides."
            Planet.MARS -> "Mars governs Agni (kinetic energy, ambition, protective boundary setting, and executive action). Seated in House $h, your fighting spirit and assertive drive express directly in ${getHouseSignification(h)}. In ${d.label} dignity, your courage manifests as ${if (d.level >= 0) "focused constructive leadership" else "impatience or friction when stalled"}."
            Planet.MERCURY -> "Mercury directs Buddhi (discernment, analytical synthesis, linguistic adaptability, and trade). Placed in House $h (${s.englishName}), your mental processing is fascinated by ${getHouseSignification(h)}. Its ${d.label} dignity defines your verbal clarity and tactical problem solving."
            Planet.JUPITER -> "Jupiter is the Guru (dharma, ethical expansion, philosophical wisdom, and optimism). In House $h (${s.englishName}), life invites you to cultivate abundance and guidance in ${getHouseSignification(h)}. With ${d.label} strength, your moral compass provides steady clarity."
            Planet.VENUS -> "Venus governs Shukra (aesthetic harmony, relationship diplomacy, refinement, and value creation). Situated in House $h, your pursuit of beauty and collaborative connection centers on ${getHouseSignification(h)}. Its ${d.label} state guides how you negotiate romantic and creative bonds."
            Planet.SATURN -> "Saturn represents Shani (karmic discipline, reality testing, time, resilience, and maturity). Anchored in House $h (${s.englishName}), Saturn demands meticulous patience, accountability, and endurance in ${getHouseSignification(h)}. Initial delays here transform into unshakable lifelong mastery."
            Planet.RAHU -> "Rahu signifies psychological hunger, innovative disruption, out-of-the-box thinking, and future karma. In House $h (${s.englishName}), Rahu creates intense fascination and unconventional ambition regarding ${getHouseSignification(h)}, urging you to break conventional molds."
            Planet.KETU -> "Ketu embodies spiritual detachment, past-life expertise, intuitive synthesis, and inner mastery. Positioned in House $h (${s.englishName}), Ketu instills natural ease or dispassion regarding ${getHouseSignification(h)}, steering you toward internal meaning rather than external validation."
            Planet.ASCENDANT -> "The Ascendant marks your primary life filter, constitution, and physical incarnation. In ${s.englishName}, your worldview and primary operating rhythm are anchored in ${s.element} elemental energy and ${s.quality} momentum."
        }

        val psychologicalTendency = when (p) {
            Planet.SUN -> "Natural urge to lead and maintain sovereign autonomy. You feel depleted when forced into subservient or micro-managed roles without creative freedom."
            Planet.MOON -> "High receptivity to environmental tone and emotional atmosphere. You absorb the unspoken feelings of people in your vicinity."
            Planet.MARS -> "Direct, solution-first cognitive tempo. You value speed and decisive closure over prolonged deliberation, feeling irritable when progress is blocked."
            Planet.MERCURY -> "Quick-witted, agile neural pathways that thrive on cross-pollinating concepts, data analysis, and articulate communication."
            Planet.JUPITER -> "Big-picture idealism and benevolent inclination to teach, counsel, and synthesize higher principles into everyday practice."
            Planet.VENUS -> "Diplomatic instinct that seeks peaceful resolutions, sensory pleasure, artistic refinement, and pleasant social cohesion."
            Planet.SATURN -> "Pragmatic, cautious, and duty-driven mindset with high standards of personal accountability and zero tolerance for superficial promises."
            Planet.RAHU -> "Visionary restlessness that anticipates emergent trends, technological innovation, and unconventional pathways to recognition."
            Planet.KETU -> "Quiet perceptive depth that sees through social pretenses. You instinctively grasp root causes without requiring excessive explanation."
            Planet.ASCENDANT -> "Primary personality signature and natural physical disposition that guides your first reactions to life's invitations."
        }

        val benefits = when (p) {
            Planet.SUN -> listOf(
                "Charismatic presence and innate ability to inspire collective confidence",
                "Strong constitutional vitality and resilience against discouragement",
                "Clarity of personal mission and natural respect from authority figures"
            )
            Planet.MOON -> listOf(
                "Deep emotional empathy and intuitive emotional intelligence",
                "Capacity to nurture communities, teams, and collaborative environments",
                "Rich imaginative creativity and strong perceptual recall"
            )
            Planet.MARS -> listOf(
                "Fearless initiative to step into difficult or pioneer situations",
                "Physical stamina, athletic endurance, and protective instincts",
                "Exceptional capacity to mobilize resources and execute under pressure"
            )
            Planet.MERCURY -> listOf(
                "Articulate persuasion, negotiation finesse, and commercial acumen",
                "Rapid cognitive processing and aptitude for technical or linguistic skills",
                "Humorous, youthful adaptability that navigates social complexities"
            )
            Planet.JUPITER -> listOf(
                "Innate philosophical wisdom and gift for advising and mentoring others",
                "Generous benevolence that naturally attracts trustworthy allies",
                "Resilient optimism that transmutes setbacks into character development"
            )
            Planet.VENUS -> listOf(
                "Refined aesthetic eye for design, proportion, elegance, and art",
                "High relationship literacy, romantic charm, and peacemaking grace",
                "Talent for wealth generation through creative and harmonious enterprises"
            )
            Planet.SATURN -> listOf(
                "Formidable grit, patience, and work ethic that outlasts any competitor",
                "Strategic long-term vision that builds durable foundations",
                "Reliability, integrity, and profound respect earned through time"
            )
            Planet.RAHU -> listOf(
                "Courage to pioneer unconventional solutions and disrupt stagnant systems",
                "Sharp strategic instincts for viral growth and modern technologies",
                "Ability to navigate unfamiliar cultures and cross-border domains"
            )
            Planet.KETU -> listOf(
                "Spiritual clarity, meditative depth, and detachment from ego traps",
                "Lightning intuition and uncanny ability to pinpoint hidden flaws",
                "Freedom from superficial materialism and profound inner self-reliance"
            )
            Planet.ASCENDANT -> listOf(
                "Distinctive personal aura that commands authentic respect",
                "Coherent life alignment between inner values and outer actions"
            )
        }

        val precautions = when (p) {
            Planet.SUN -> listOf(
                "Watch for stubborn pride or taking healthy feedback as personal disrespect",
                "Avoid domineering conversations or needing constant validation of authority",
                "Prevent burnout caused by refusing to delegate responsibility"
            )
            Planet.MOON -> listOf(
                "Beware of mood-dependent decision making and emotional exhaustion",
                "Notice over-attachment to past nostalgia or perceived slights",
                "Avoid seeking external validation to stabilize internal self-worth"
            )
            Planet.MARS -> listOf(
                "Resist abrupt, abrasive reactions during miscommunications",
                "Beware of driving or physical exertion when emotionally agitated",
                "Avoid unnecessary confrontations that burn bridges for minor victories"
            )
            Planet.MERCURY -> listOf(
                "Guard against overthinking, cognitive analysis paralysis, and scattered focus",
                "Avoid nervous restlessness and compulsive multitasking",
                "Ensure promises made in casual conversation are rigorously tracked"
            )
            Planet.JUPITER -> listOf(
                "Watch for self-righteous dogmatism or unsolicited lecturing",
                "Avoid over-promising or underestimating project expenses due to excessive optimism",
                "Check dietary moderation; prevent excess sweet or heavy indulgences"
            )
            Planet.VENUS -> listOf(
                "Guard against conflict avoidance that allows resentments to simmer",
                "Avoid compulsive retail spending to comfort temporary emotional dips",
                "Ensure partner selection values character over mere superficial charm"
            )
            Planet.SATURN -> listOf(
                "Resist chronic cynicism, perfectionist paralysis, and self-criticism",
                "Avoid excessive isolation or believing you must carry the world alone",
                "Do not neglect joy, recreation, and spontaneity in the name of duty"
            )
            Planet.RAHU -> listOf(
                "Beware of get-rich-quick shortcuts or unvetted speculative ventures",
                "Watch for obsessive mental fixation on status symbols and accolades",
                "Ensure ethical transparency in all agreements and digital engagements"
            )
            Planet.KETU -> listOf(
                "Avoid nihilistic apathy or sudden abandonment of valuable responsibilities",
                "Guard against chronic cynicism toward worldly structures and commitments",
                "Maintain clear communication rather than withdrawing into silence"
            )
            Planet.ASCENDANT -> listOf(
                "Avoid taking on identities that belong to others",
                "Stay attentive to physical posture and energetic boundaries"
            )
        }

        val lifestyleChanges = when (p) {
            Planet.SUN -> listOf(
                "Circadian Sunlight Alignment: Spend 10-15 minutes in direct morning sunlight within 30 minutes of waking to anchor cortisol and vitality.",
                "Sovereign Posture Practice: Practice upright spine posture and chest opening during meetings to project quiet, grounded authority.",
                "Generous Leadership Habit: Intentionally acknowledge and publicly highlight one colleague or family member's contribution every day."
            )
            Planet.MOON -> listOf(
                "Evening Mental Unwinding: Disconnect from bright screens 1 hour before sleep; sip warm soothing herbal tea (chamomile or cardamom milk).",
                "Water & Fluid Ritual: Drink clean water from copper or ceramic vessels throughout the day to support cellular fluidity.",
                "Emotional Brain Dump: Keep an evening journal to write down floating thoughts and release emotional clutter before bed."
            )
            Planet.MARS -> listOf(
                "High-Intensity Morning Output: Channel fiery energy into vigorous strength training, martial arts, or brisk morning workouts.",
                "Tactical Cooling Breath: Practice Sheetali / Sheetkari or slow coherent exhales when adrenaline spikes in traffic or debate.",
                "Project Batching: Break large goals into short sprints with clear finish lines to satisfy Mars' thirst for tangible conquest."
            )
            Planet.MERCURY -> listOf(
                "Daily Single-Tasking Window: Reserve a 90-minute distraction-free focus block with all notifications silenced.",
                "Verbal Precision Discipline: Replace filler words and nervous speech with deliberate, articulate pauses.",
                "Nature Grounding: Walk barefoot on grass or spend 20 minutes in green spaces weekly to calm analytical nervous firing."
            )
            Planet.JUPITER -> listOf(
                "Philosophical Reading Cadence: Read 15 minutes of inspiring, philosophical, or timeless wisdom literature every morning.",
                "Mentorship & Service: Offer voluntary coaching, mentorship, or constructive advice to someone entering your field.",
                "Digestive Rhythm & Fasting: Practice light eating on Thursdays or maintain a consistent 12-14 hour overnight fasting window."
            )
            Planet.VENUS -> listOf(
                "Workspace Aesthetics: Curate clean, beautiful lines in your living and working space with fresh flowers or natural scents.",
                "Mindful Sensory Enjoyment: Savor meals slowly without screen distractions, appreciating texture, aroma, and presentation.",
                "Relational Transparency: Schedule weekly intentional check-ins with your partner or closest friend to nurture open intimacy."
            )
            Planet.SATURN -> listOf(
                "Rigorous Daily Routine: Wake and sleep at identical times every single day to align with Saturn's rhythmic law of nature.",
                "Warm Oil Abhyanga: Perform warm sesame oil self-massage on feet and scalp before bedtime to ground Vata and soothe joints.",
                "Service to the Underprivileged: Commit regular time or anonymous assistance to elderly care, sanitation workers, or stray animals."
            )
            Planet.RAHU -> listOf(
                "Digital Detox Sabbath: Take a complete digital screen detox for half a day every weekend to reset dopamine receptors.",
                "Pranayama Anchoring: Practice Nadi Shodhana (alternate nostril breathing) for 10 minutes daily to quiet erratic mental cravings.",
                "Clarity & Due Diligence: Enforce a 48-hour cooling period before committing to major financial or contractual opportunities."
            )
            Planet.KETU -> listOf(
                "Daily Silent Meditation: Practice 15-20 minutes of silent breath observation or Vipassana early in the morning.",
                "Decluttering Practice: Systematically donate clothes and belongings you haven't used in 6 months to honor detachment.",
                "Grounding Nourishment: Include warm, cooked, root vegetables and earthy spices (cumin, ginger) to keep your energy embodied."
            )
            Planet.ASCENDANT -> listOf(
                "Morning Intention Setting: Spend the first 5 minutes upon waking stating your core life intention for the day.",
                "Hydration and Breath: Start each morning with two full glasses of room temperature water and 10 deep belly breaths."
            )
        }

        return PlanetaryInsight(
            planet = p,
            rootWhy = rootWhy,
            psychologicalTendency = psychologicalTendency,
            giftsAndBenefits = benefits,
            precautionsAndBlindSpots = precautions,
            practicalLifestyleHabits = lifestyleChanges
        )
    }

    private fun getHouseSignification(house: Int): String = when (house) {
        1 -> "your personal identity, vitality, physical presence, and direct life trajectory"
        2 -> "your material security, accumulated wealth, dietary habits, and verbal expression"
        3 -> "your courage, initiative, siblings, communication crafts, and short travel"
        4 -> "your home foundation, mother, emotional tranquility, and real estate"
        5 -> "your creative intellect, romance, speculative discernment, and past-life merits (Purva Punya)"
        6 -> "your problem-solving vigor, daily routines, overcoming obstacles, and immune strength"
        7 -> "your partnerships, marriage, public contracts, and relational diplomacy"
        8 -> "your psychological depth, longevity, transformative breakthroughs, and esoteric research"
        9 -> "your higher dharma, philosophical convictions, guru guidance, and good fortune"
        10 -> "your professional career, executive authority, social status, and public reputation"
        11 -> "your highest ambitions, social networks, lucrative gains, and collective impact"
        12 -> "your subconscious mind, spiritual liberation (Moksha), sleep, and foreign horizons"
        else -> "this house's sphere"
    }

    private fun getDignityOperation(dignity: Dignity): String = when (dignity) {
        Dignity.EXALTED -> "at peak radiance, granting extraordinary confidence and high standards"
        Dignity.MOOLATRIKONA, Dignity.OWN_SIGN -> "with effortless sovereign mastery and authentic ease"
        Dignity.GREAT_FRIEND, Dignity.FRIEND -> "with supportive, cooperative, and constructive momentum"
        Dignity.NEUTRAL -> "in a balanced, adaptable, and pragmatic manner"
        Dignity.ENEMY, Dignity.GREAT_ENEMY -> "through friction, requiring conscious refinement and self-awareness"
        Dignity.DEBILITATED -> "under strain or humility, urging you to develop internal fortitude rather than relying on external crutches"
    }
}
