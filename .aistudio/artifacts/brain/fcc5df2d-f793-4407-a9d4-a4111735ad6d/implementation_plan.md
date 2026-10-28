# Comprehensive Vedic Astrology Core: Moon Profile, Visual Vargas, Yogas, Vimshottari Dasha, Lal Kitab Remedies & Vaisheshikamsa Tiers

Expanding **JyotishVeda** with authentic classical Vedic algorithms: Moon Sign (Chandra Rashi) psychological profiling, interactive visual Kundali diagrams for all 16 divisional charts, automated Yoga detection (benefits & disadvantages), 120-year Vimshottari Dasha/Antardasha timeline compared with current date, authentic Lal Kitab remedies, full planetary aspect (Drishti) calculations, and classical Vaisheshikamsa dignity tiers (Kalpavriksha, Parijata, etc.) across Shodashvarga and Dashavarga.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural and user experience choices have been confirmed:
> - **Vimshottari Dasha Depth**: Complete calculation of Mahadasha and Antardasha cycles with the currently active period highlighted against the current calendar date, providing specific benefits/losses, what to do, and what to avoid.
> - **Visual Divisional Charts**: The interactive `KundaliCanvas` will render the authentic Kundali diagram (North or South Indian) for whichever divisional chart is selected (D1 to D60), showing its specific Lagna and planetary house placements.
> - **Lal Kitab Remedies**: Dedicated section grouped by planet and highlighting the active Mahadasha & Antardasha planets with authentic practical remedies and precautions.
> - **Classical Vaisheshikamsa Tiers**: Detection and explanation of planet repetitions in own/exalted signs across Shodashvarga (Kalpavriksha, Chandanavana, Kamadhenu, etc.) and Dashavarga (Parijata, Uttama, Gopura, Simhasana, etc.) explaining their names, meanings, and optimization tactics.
> - **Total Yogas & Planetary Aspects (Drishti)**: Comprehensive detection of Pancha Mahapurusha, Raja, Dhana, Vipareeta, and Dosha yogas, accompanied by mutual and special planetary aspect breakdowns.

---

## 1. Overview & Core Concept

1. **Moon Sign (Chandra Rashi) & Psychological Profile**:
   - Computes the sidereal Moon sign, nakshatra, and pada.
   - Generates an in-depth emotional and subconscious psychological profile (Manas): instinctual security triggers, emotional processing tempo, interpersonal empathy, and Vedic mental health guidance.
2. **Visual Kundali for All Divisional Charts (D1 - D60)**:
   - Upgrades `VargaScreen` to include a full-featured `KundaliCanvas` renderer.
   - Switching between D1 Rashi, D9 Navamsha, D10 Dashamsha, D7 Saptamsha, D2 Hora, D3 Drekkana, D30 Trimshamsha, etc., immediately draws the actual North or South Indian chart for that varga with its own calculated Lagna and planetary house positions.
3. **Classical Vedic Yogas Engine**:
   - Detects all major combinations formed by planet placements, house lordships, and angles:
     - *Pancha Mahapurusha*: Ruchaka, Bhadra, Hamsa, Malavya, Sasa.
     - *Auspicious Raja & Dhana Yogas*: Gajakesari, Budhaditya, Chandra-Mangala, Lakshmi, Amala, Parivartana, Kahala.
     - *Vipareeta Raja Yogas*: Harsha, Sarala, Vimala.
     - *Challenging Yogas / Doshas*: Kemadruma, Guru Chandal, Angarak, Manglik / Kuja Dosha.
   - For every yoga: reveals whether it is active, its specific benefits, potential pitfalls, and cancellation factors (Bhanga).
4. **Vimshottari Dasha & Antardasha Timeline**:
   - Calculates the exact balance of dasha at birth based on the Moon's traversed longitude within its birth nakshatra.
   - Generates the chronological sequence of all 9 Mahadashas (120-year cycle) and nested Antardashas.
   - Highlights the currently active Mahadasha and Antardasha for today's date.
   - Details active planet behavioral themes, favorable opportunities, loss risks, actionable "Do's", and critical "Don'ts".
   - Integrates directly with the Daily Transits screen to compare active Dasha with current transits.
5. **Lal Kitab Remedies System**:
   - Specific, grounded Lal Kitab remedies corresponding to planetary placements and active dasha rulers (e.g. copper coin offering, serving black dogs, silver square token, brass ornaments, green mung charity, coconut immersion).
6. **Planetary Aspects (Drishti) Engine**:
   - Computes 7th house aspects for all grahas, plus special aspects:
     - Mars (4th, 7th, 8th)
     - Jupiter (5th, 7th, 9th)
     - Saturn (3rd, 7th, 10th)
     - Rahu / Ketu (5th, 7th, 9th)
   - Highlights which houses and planets receive aspects, and explains the resulting karmic friction or blessing.
7. **Varga Dignity Tiers (Vaisheshikamsa)**:
   - Evaluates each planet's repetition in own, exalted, or moolatrikona sign across the 16 divisional charts and 10 divisional charts.
   - Shodashvarga tiers: Bhedaka (2), Kusuma (3), Nagapushpa (4), Kanduka (5), Kerala (6), **Kalpavriksha (7)**, Chandanavana (8), Parijata (9), Kamadhenu (10), Vipula (11), Makuta (12), Amaraloka (13), Vaishnavamsha (14), Saiva (15), Shridhama (16).
   - Dashavarga tiers: Parijata (2), Uttama (3), Gopura (4), Simhasana (5), Paravata (6), Devaloka (7), Iravata (8), Vaishnavamsha (9), Shridhama (10).
   - Explains the esoteric symbolism of each tier and practical advice on harnessing that planet's peak strength.

---

## 2. Technical Architecture & Modifications

```
┌─────────────────────────────────────────────────────────────────┐
│                        Astrology UI Layer                       │
│  ┌───────────────────────┐ ┌──────────────────┐ ┌─────────────┐ │
│  │ Moon Profile & Yogas  │ │ Visual Varga     │ │ Dasha & Lal │ │
│  │ Cards (Pancha Maha...)│ │ Kundali Canvas   │ │ Kitab Tab   │ │
│  └───────────▲───────────┘ └────────▲─────────┘ └──────▲──────┘ │
└──────────────┼──────────────────────┼──────────────────┼────────┘
               │                      │                  │
               ▼                      ▼                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Vedic Calculation Core                     │
│  ┌─────────────────────────┐  ┌───────────────────────────────┐ │
│  │ YogaEngine.kt           │  │ VimshottariDashaEngine.kt     │ │
│  │ - 20+ Classical Yogas   │  │ - 120-yr cycle & Antardashas  │ │
│  │ - Benefits & Disadv.    │  │ - Date-matched active period  │ │
│  └─────────────────────────┘  └───────────────────────────────┘ │
│  ┌─────────────────────────┐  ┌───────────────────────────────┐ │
│  │ AspectEngine.kt         │  │ LalKitabRemediesProvider.kt   │ │
│  │ - Drishti calculations  │  │ - Planet & Dasha based cures  │ │
│  │ - Mutual & House aspects│  │ - Taboos & precautions        │ │
│  └─────────────────────────┘  └───────────────────────────────┘ │
│  ┌─────────────────────────┐  ┌───────────────────────────────┐ │
│  │ VaisheshikamsaEngine.kt │  │ MoonPsychologyProvider.kt     │ │
│  │ - Shodashvarga tiers    │  │ - Chandra Rashi profiling     │ │
│  │ - Dashavarga tiers      │  │ - Emotional & mental signature│ │
│  └─────────────────────────┘  └───────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Components to Create / Update
1. **`com.example.astro.calc.YogaEngine`**: Identifies all classical Vedic Yogas (Pancha Mahapurusha, Gajakesari, Budhaditya, Chandra-Mangala, Vipareeta, Doshas) with detailed benefits and pitfalls.
2. **`com.example.astro.calc.VimshottariDashaEngine`**: Computes full 120-year Mahadasha and Antardasha dates from birth Moon longitude, identifies current active dasha for today's date, and provides guidance (gains, losses, do's, don'ts).
3. **`com.example.astro.calc.AspectEngine`**: Calculates planetary Drishti on houses and other planets.
4. **`com.example.astro.calc.VaisheshikamsaEngine`**: Calculates repetition tiers across Shodashvarga and Dashavarga (Kalpavriksha, Parijata, etc.) with explanations and optimization instructions.
5. **`com.example.astro.data.LalKitabRemediesProvider`**: Curates practical Lal Kitab remedies for each planet and active dasha.
6. **`com.example.astro.data.MoonPsychologyProvider`**: Formulates emotional and psychological profiles for all 12 Chandra Rashis.
7. **`VargaScreen.kt` Update**: Integrates `KundaliCanvas` so every divisional chart (D1 to D60) renders as an interactive visual Kundali diagram.
8. **New Tab / Screen (`YogasDashaScreen.kt` or sub-views)**: Displays Yogas, Vimshottari Dasha timeline, Lal Kitab remedies, and Vaisheshikamsa tiers.
9. **`DailyScreen.kt` Update**: Explicitly links the active Dasha period with the daily transit forecast.

---

## 3. Verification Plan

1. **Vimshottari Dasha Math Verification**:
   - Verify birth dasha balance calculation from Moon nakshatra position.
   - Verify Mahadasha order: Ketu, Venus, Sun, Moon, Mars, Rahu, Jupiter, Saturn, Mercury totaling 120 years.
   - Verify active Mahadasha and Antardasha accurately corresponds to current date (October 2026).
2. **Varga Canvas & Tiers Verification**:
   - Verify selecting D9, D10, D7 etc. updates the Kundali diagram with that varga's Lagna and planets.
   - Verify Vaisheshikamsa tier counts (e.g. Kalpavriksha for 7 repetitions) match Parashara rules.
3. **Yoga & Aspect Calculations**:
   - Test Gajakesari, Budhaditya, and Pancha Mahapurusha detection against sample charts.
   - Test Mars (4, 7, 8), Jupiter (5, 7, 9), and Saturn (3, 7, 10) aspects.
4. **Build & Automated Tests**:
   - Run unit tests in `ExampleUnitTest` covering Dasha calculations, Yoga detection, and Vaisheshikamsa tiers.
   - Run `compile_applet` for error-free build.
