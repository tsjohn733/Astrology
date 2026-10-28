package com.example.astro.data

import com.example.astro.model.LalKitabRemedy
import com.example.astro.model.Planet

object LalKitabRemediesProvider {

    private val remediesMap: Map<Planet, List<LalKitabRemedy>> = mapOf(
        Planet.SUN to listOf(
            LalKitabRemedy(
                planet = Planet.SUN,
                house = 1,
                title = "Surya Arghya & Copper Vessel Remedy",
                remedyAction = "Offer water mixed with red vermilion (kumkum) and jaggery to the rising Sun daily from a pure copper vessel.",
                purpose = "Enhances vital aura, confidence, public leadership, and rectifies solar afflictions.",
                precautions = "Avoid speaking untruths, disrespecting father, or accepting free items from strangers."
            ),
            LalKitabRemedy(
                planet = Planet.SUN,
                house = 10,
                title = "Golden Grain & Father's Blessing",
                remedyAction = "Touch the feet of your father and paternal elders before embarking on important career endeavors; feed whole wheat grain to cattle.",
                purpose = "Stabilizes authoritative career, administrative status, and government honors.",
                precautions = "Never install water hand-pumps or leaking taps inside the main entrance."
            ),
            LalKitabRemedy(
                planet = Planet.SUN,
                house = 5,
                title = "Sweet Bread & Purity Practice",
                remedyAction = "Bake sweet wheat roti and feed to red monkeys or cows on Sunday mornings.",
                purpose = "Blessings for progeny, creative vitality, intellect, and spiritual radiance.",
                precautions = "Refrain from boasting or taking false credit for others' labor."
            )
        ),
        Planet.MOON to listOf(
            LalKitabRemedy(
                planet = Planet.MOON,
                house = 4,
                title = "Matru Ashirwad & Pure Silver Water",
                remedyAction = "Take silver tumbler or coin blessed by mother and keep it permanently in your room; touch mother's feet daily.",
                purpose = "Stabilizes emotional volatility, calms the mind, and blesses peace of mind and domestic harmony.",
                precautions = "Never sell or mortgage family heirlooms made of silver; do not sleep with head towards the North."
            ),
            LalKitabRemedy(
                planet = Planet.MOON,
                house = 1,
                title = "Kheer & Milk Abhishekam",
                remedyAction = "Offer sweetened cow milk or unboiled milk to a Shiva Lingam on Monday mornings; consume food in silver utensils.",
                purpose = "Protects psychological health, removes anxiety, and cultivates intuitive insight.",
                precautions = "Never keep broken glassware, empty pots, or stagnant water on the terrace roof."
            ),
            LalKitabRemedy(
                planet = Planet.MOON,
                house = 7,
                title = "White Flower Offering",
                remedyAction = "Donate white rice, sugar, or white sweets to elderly women or pilgrims.",
                purpose = "Harmonizes marital relations, emotional empathy, and public popularity.",
                precautions = "Avoid having dinner late at night after midnight."
            )
        ),
        Planet.MARS to listOf(
            LalKitabRemedy(
                planet = Planet.MARS,
                house = 1,
                title = "Red Lentil (Masoor) & Blood Donation",
                remedyAction = "Donate red lentils (lal masoor dal) or sweet batashas at a Hanuman temple on Tuesday; donate blood if medically fit.",
                purpose = "Converts erratic aggression and impulsiveness into heroic discipline and physical endurance.",
                precautions = "Maintain cordial relations with younger brothers; avoid wearing torn red attire."
            ),
            LalKitabRemedy(
                planet = Planet.MARS,
                house = 8,
                title = "Baking Sweet Roti (Tandoori Roti)",
                remedyAction = "Prepare sweet wheat rotis made with jaggery and feed them to street dogs or cows on Tuesdays.",
                purpose = "Pacifies Manglik Dosha, averts sudden accidents, surgeries, and property disputes.",
                precautions = "Never consume alcohol, meat, or engage in violent quarrels inside the home."
            ),
            LalKitabRemedy(
                planet = Planet.MARS,
                house = 10,
                title = "Deer Skin & Earth Pot Honey",
                remedyAction = "Keep pure forest honey in an unbaked earthen pot (mitti ki handi) covered with a lid in the southern corner of the house.",
                purpose = "Bestows immense executive command, land holdings, and victory in legal competitions.",
                precautions = "Never deceive partners in construction, real estate, or mechanical work."
            )
        ),
        Planet.MERCURY to listOf(
            LalKitabRemedy(
                planet = Planet.MERCURY,
                house = 5,
                title = "Soaked Green Moong & Bird Feeding",
                remedyAction = "Soak whole green gram (sabut moong dal) overnight and feed it to wild green birds or pigeons every Wednesday at dawn.",
                purpose = "sharpens communication, analytical brilliance, mathematical prowess, and business negotiation.",
                precautions = "Avoid insulting daughter, maternal aunts (Mausi), or sisters; avoid keeping green plastic plants."
            ),
            LalKitabRemedy(
                planet = Planet.MERCURY,
                house = 1,
                title = "Silver Wire & Clean Speech",
                remedyAction = "Wear a clean silver ring on the little finger or keep a solid silver ball in your purse.",
                purpose = "Eliminates speech hesitation, enhances mercantile instincts, and brings lucrative commerce deals.",
                precautions = "Never keep defective electronic equipment, obsolete radios, or non-working clocks at home."
            ),
            LalKitabRemedy(
                planet = Planet.MERCURY,
                house = 7,
                title = "Go-Seva with Green Fodder",
                remedyAction = "Feed fresh green spinach or green fodder (hara chaara) to cows on Wednesday.",
                purpose = "Brings commercial growth, cordial partnership relations, and trade expansion.",
                precautions = "Do not participate in gambling, speculative betting, or fraudulent accounting."
            )
        ),
        Planet.JUPITER to listOf(
            LalKitabRemedy(
                planet = Planet.JUPITER,
                house = 9,
                title = "Kesar-Chandan Tilak & Guru Seva",
                remedyAction = "Apply pure saffron (kesar) or turmeric paste on your forehead and naval every morning; touch the feet of your teachers and gurus.",
                purpose = "Attracts divine grace, wealth, higher wisdom, spiritual authority, and moral stature.",
                precautions = "Never dishonor temples, sacred books, spiritual teachers, or cut a Peepal tree."
            ),
            LalKitabRemedy(
                planet = Planet.JUPITER,
                house = 1,
                title = "Chana Dal & Brass Vessel Donation",
                remedyAction = "Donate split yellow chickpeas (chana dal) and turmeric roots wrapped in yellow cloth to a priest or spiritual center on Thursdays.",
                purpose = "Enhances intellect, removes financial stagnation, and grants radiant judgment.",
                precautions = "Do not consume meat or liquor on Thursday; keep religious texts carefully wrapped."
            ),
            LalKitabRemedy(
                planet = Planet.JUPITER,
                house = 2,
                title = "Yellow Topaz / Saffron Milk",
                remedyAction = "Drink lukewarm milk infused with 2 strands of saffron before bedtime; keep a yellow handkerchief in your pocket.",
                purpose = "Protects family wealth accumulation, speech sweetness, and academic honors.",
                precautions = "Avoid taking gifts of religious idols without proper consecration."
            )
        ),
        Planet.VENUS to listOf(
            LalKitabRemedy(
                planet = Planet.VENUS,
                house = 7,
                title = "White Silk & Curd Offering",
                remedyAction = "Donate white clarified butter (desi ghee), curd, camphor, or white cow to a temple or Gaushala on Friday.",
                purpose = "Fosters luxurious comforts, marital intimacy, artistic talent, and vehicle prosperity.",
                precautions = "Never wear dirty, unwashed, or torn clothes; treat spouse and women with absolute reverence."
            ),
            LalKitabRemedy(
                planet = Planet.VENUS,
                house = 12,
                title = "Subtle Natural Fragrance",
                remedyAction = "Apply natural sandalwood or white rose attar (non-alcoholic fragrance) behind the ears; wash curd off hands after eating.",
                purpose = "Averts wasteful expenses, cultivates refined aesthetic elegance and restful deep sleep.",
                precautions = "Never stay in dark, damp, poorly ventilated bedrooms."
            ),
            LalKitabRemedy(
                planet = Planet.VENUS,
                house = 4,
                title = "Silver Coin in Milk",
                remedyAction = "Keep a pure square silver coin soaked in rain water or Ganga jal inside a silver vessel at your altar.",
                purpose = "Guarantees conveyance comforts, palatial residence bliss, and domestic peace.",
                precautions = "Avoid extramarital flirtation and frivolous luxury debts."
            )
        ),
        Planet.SATURN to listOf(
            LalKitabRemedy(
                planet = Planet.SATURN,
                house = 10,
                title = "Mustard Oil Chhaya Daan & Street Dog Seva",
                remedyAction = "Look at your reflection in a bowl of mustard oil and donate it along with black gram (sabut urad dal) on Saturday; feed roti spread with mustard oil to black dogs.",
                purpose = "Subdues Sade Sati afflictions, delays in profession, joint pains, and heavy burdens of karma.",
                precautions = "Strictly abstain from alcohol, gambling, meat, and harassing laborers or sanitation workers."
            ),
            LalKitabRemedy(
                planet = Planet.SATURN,
                house = 1,
                title = "Iron Vessel & Shoe Donation",
                remedyAction = "Donate black leather footwear or iron cooking pans (tawa) to impoverished daily-wage laborers.",
                purpose = "Transforms grueling hardship into unyielding stamina, resilience, and monumental lasting success.",
                precautions = "Never buy or bring new iron articles, mustard oil, or leather home on Saturdays."
            ),
            LalKitabRemedy(
                planet = Planet.SATURN,
                house = 7,
                title = "Burying Jaggery & Peepal Pradakshina",
                remedyAction = "Light a mustard oil lamp under a sacred Peepal tree after sunset on Saturdays and walk 7 rounds.",
                purpose = "Removes delays in marriage, pacifies chronic business hurdles, and blesses long life.",
                precautions = "Never construct or break walls in the western direction on Saturdays."
            )
        ),
        Planet.RAHU to listOf(
            LalKitabRemedy(
                planet = Planet.RAHU,
                house = 1,
                title = "Silver Elephant & Barley in Flowing River",
                remedyAction = "Keep a solid pure silver elephant figure in your study/bedroom; immerse unpeeled barley or coconuts in a flowing river on Saturday/Wednesday.",
                purpose = "Disperses mental illusions, paranoia, phantom phobias, addiction traps, and sudden catastrophic losses.",
                precautions = "Never clutter the space beneath your bed; avoid keeping broken umbrellas or electrical junk."
            ),
            LalKitabRemedy(
                planet = Planet.RAHU,
                house = 5,
                title = "Radish Donation & Silver Plate Meals",
                remedyAction = "Keep fresh white radishes with leaves by your pillow overnight and donate them to cows or temple kitchen in the morning.",
                purpose = "Removes childlessness curses, cleanses speculative losses, and stabilizes eccentric thoughts.",
                precautions = "Maintain harmonious, respectful rapport with in-laws (sasural)."
            ),
            LalKitabRemedy(
                planet = Planet.RAHU,
                house = 12,
                title = "Red Cloth Saunf & Kitchen Dining",
                remedyAction = "Keep fennel seeds (saunf) tied in a small red cloth under your pillow; eat meals sitting on the kitchen floor.",
                purpose = "Eliminates nightmares, insomnia, hospitalizations, and invisible enemies.",
                precautions = "Do not smoke, consume intoxicants, or install smoke mirrors."
            )
        ),
        Planet.KETU to listOf(
            LalKitabRemedy(
                planet = Planet.KETU,
                house = 9,
                title = "Dual-Color Blanket & Lord Ganesha Aradhana",
                remedyAction = "Donate a black-and-white checkered blanket to the poor; chant 'Om Gam Ganapataye Namaha' with a mala; feed multi-colored stray dogs.",
                purpose = "Averts sudden accidents, nervous system afflictions, urinary tract issues, and bestows moksha-level spiritual insight.",
                precautions = "Never mistreat dogs; never break spiritual vows or ridicule holy mendicants."
            ),
            LalKitabRemedy(
                planet = Planet.KETU,
                house = 12,
                title = "Golden Ear Piercing & Sesame Offering",
                remedyAction = "Wear pure gold earrings (or gold studs in earlobes); donate black and white sesame seeds (til).",
                purpose = "Activates transcendental higher intuition, protects lineage (santati), and removes aimless wandering.",
                precautions = "Avoid giving away footwear to relatives; keep sour pickles in glass containers."
            ),
            LalKitabRemedy(
                planet = Planet.KETU,
                house = 1,
                title = "Saffron in Water & Dog Care",
                remedyAction = "Drink a pinch of saffron water in the morning and offer sweet rotis to stray dogs.",
                purpose = "Stabilizes spinal health, dissolves irrational anxieties, and awakens inner guru guidance.",
                precautions = "Do not wear footwear inside the puja room or sacred study altar."
            )
        )
    )

    fun getRemediesForPlanet(planet: Planet): List<LalKitabRemedy> {
        return remediesMap[planet] ?: remediesMap[Planet.SUN] ?: emptyList()
    }

    fun getAllRemediesGroupedByPlanet(): Map<Planet, List<LalKitabRemedy>> {
        return remediesMap
    }

    fun getRemediesForActiveDasha(mahadashaLord: Planet, antardashaLord: Planet?): List<LalKitabRemedy> {
        val list = mutableListOf<LalKitabRemedy>()
        remediesMap[mahadashaLord]?.let { list.addAll(it) }
        if (antardashaLord != null && antardashaLord != mahadashaLord) {
            remediesMap[antardashaLord]?.let { list.addAll(it) }
        }
        return list
    }
}
