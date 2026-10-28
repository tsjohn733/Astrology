package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.astro.model.*
import com.example.ui.components.ChartStyle
import com.example.ui.components.KundaliCanvas
import com.example.ui.theme.*

@Composable
fun ChartScreen(
    chartData: ChartData?,
    chartStyle: ChartStyle,
    selectedHouse: Int?,
    moonProfile: MoonProfile?,
    aspects: List<PlanetaryAspectInfo>,
    onHouseSelected: (Int) -> Unit,
    onToggleChartStyle: () -> Unit,
    onSelectPlanet: (Planet) -> Unit,
    modifier: Modifier = Modifier
) {
    if (chartData == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = StarlightGold)
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Style Toggle & Summary Bar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Lagna: ${chartData.ascendantSign.englishName}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = StarlightGold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${chartData.ascendantSign.sanskritName})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CosmicTextSecondary
                            )
                        }
                        Text(
                            text = "Lahiri Ayanamsha: ${String.format("%.2f", chartData.ayanamsha)}°",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextTertiary
                        )
                    }

                    FilledTonalButton(
                        onClick = onToggleChartStyle,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0x33F5D061),
                            contentColor = StarlightGold
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("toggle_chart_style_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Toggle Chart Style",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (chartStyle == ChartStyle.NORTH_INDIAN) "North Indian" else "South Indian",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // MOON SIGN (CHANDRA RASHI) & EMOTIONAL/PSYCHOLOGICAL PROFILE CARD
        if (moonProfile != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StarlightGold)),
                    modifier = Modifier.fillMaxWidth().testTag("moon_sign_profile_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33F5D061)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = StarlightGold,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Chandra Rashi (Moon Sign)",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = CosmicTextSecondary
                                    )
                                    Text(
                                        text = "${moonProfile.sign.englishName} (${moonProfile.sign.sanskritName})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = StarlightGold
                                    )
                                }
                            }

                            Surface(
                                color = Color(0x3338BDF8),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${moonProfile.nakshatra.name} Pada ${moonProfile.pada}",
                                    color = SacredCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Psychological Nature & Mind",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = moonProfile.emotionalNature,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CosmicTextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Subconscious Mindset: ${moonProfile.psychologicalSignature}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Core Security Need: ${moonProfile.instinctualSecurityNeeds}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SacredCyan
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Under Stress: ${moonProfile.stressResponse}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFF87171)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Cognitive & Emotional Strengths:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            moonProfile.mentalStrengths.forEach { str ->
                                Surface(
                                    color = Color(0x2210B981),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "✓ $str",
                                        color = ExaltedGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Emotional Blind Spots to Watch:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            moonProfile.emotionalBlindSpots.forEach { spot ->
                                Surface(
                                    color = Color(0x22EF4444),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "! $spot",
                                        color = Color(0xFFFCA5A5),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Kundali Canvas
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                KundaliCanvas(
                    chartData = chartData,
                    chartStyle = chartStyle,
                    selectedHouse = selectedHouse,
                    onHouseSelected = onHouseSelected,
                    modifier = Modifier.testTag("kundali_chart_canvas")
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tap any house or sign to inspect seated planets, lords & significations",
                    style = MaterialTheme.typography.labelSmall,
                    color = CosmicTextTertiary
                )
            }
        }

        // Selected House Details Card
        if (selectedHouse != null) {
            item {
                HouseInspectorCard(
                    house = selectedHouse,
                    chartData = chartData,
                    onSelectPlanet = onSelectPlanet
                )
            }
        }

        // Planetary Positions Quick List
        item {
            Text(
                text = "Planetary Placements (Grahas)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = StarlightGold
            )
        }

        items(chartData.planets) { planetPos ->
            PlanetRowCard(
                position = planetPos,
                onClick = { onSelectPlanet(planetPos.planet) }
            )
        }

        // PLANETARY ASPECTS (DRISHTI) SECTION
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SacredCyan)),
                modifier = Modifier.fillMaxWidth().testTag("planetary_aspects_section")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = SacredCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Planetary Aspects (Drishti & Results)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Vedic astrology calculates Drishti (glance) where planets cast energetic influence on other houses and seated planets. Mars aspects 4th, 7th, 8th; Jupiter & Rahu/Ketu aspect 5th, 7th, 9th; Saturn aspects 3rd, 7th, 10th; all planets cast 7th aspect.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSecondary
                    )
                }
            }
        }

        items(aspects) { asp ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = asp.planet.symbol,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = StarlightGold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = asp.planet.englishName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPrimary
                            )
                        }

                        Surface(
                            color = Color(0x3338BDF8),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Aspects Houses: ${asp.aspectedHouses.joinToString(", ")}",
                                color = SacredCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = asp.aspectSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = Color(0x1538BDF8),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "Astrological Result:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = StarlightGold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = asp.astrologicalResult,
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HouseInspectorCard(
    house: Int,
    chartData: ChartData,
    onSelectPlanet: (Planet) -> Unit,
    modifier: Modifier = Modifier
) {
    val ascSignNum = chartData.ascendantSign.number
    val houseSignNum = ((ascSignNum + house - 2) % 12) + 1
    val sign = ZodiacSign.fromNumber(houseSignNum)
    val planetsInHouse = chartData.getPlanetsInHouse(house)

    val houseTitle = when (house) {
        1 -> "1st House (Tanu Bhava) • Self & Vitality"
        2 -> "2nd House (Dhana Bhava) • Wealth & Family"
        3 -> "3rd House (Sahaja Bhava) • Courage & Initiative"
        4 -> "4th House (Sukha Bhava) • Home, Mother & Peace"
        5 -> "5th House (Putra Bhava) • Intellect & Dharma"
        6 -> "6th House (Ari Bhava) • Health, Service & Victory"
        7 -> "7th House (Yuvati Bhava) • Partnerships & Marriage"
        8 -> "8th House (Randhra Bhava) • Transformation & Depth"
        9 -> "9th House (Dharma Bhava) • Fortune & Higher Truth"
        10 -> "10th House (Karma Bhava) • Career & Social Stature"
        11 -> "11th House (Labha Bhava) • Gains & Ambitions"
        12 -> "12th House (Vyaya Bhava) • Liberation & Subconscious"
        else -> "House $house"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0x33F5D061))),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = houseTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = StarlightGold
                )
                Surface(
                    color = Color(0x3338BDF8),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Sign: ${sign.englishName} (${sign.sanskritName})",
                        color = SacredCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "House Lord: ${sign.ruler.englishName} (${sign.ruler.sanskritName}) • Element: ${sign.element} (${sign.quality})",
                style = MaterialTheme.typography.bodySmall,
                color = CosmicTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))
            if (planetsInHouse.isEmpty()) {
                Text(
                    text = "No planets seated in this house. The house is governed by its lord ${sign.ruler.englishName}.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CosmicTextTertiary
                )
            } else {
                Text(
                    text = "Occupying Planets (Tap for deep behavior intel):",
                    style = MaterialTheme.typography.labelSmall,
                    color = CosmicTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    planetsInHouse.forEach { p ->
                        AssistChip(
                            onClick = { onSelectPlanet(p.planet) },
                            label = {
                                Text(
                                    text = "${p.planet.symbol} ${p.planet.englishName}${if (p.isRetrograde) " (R)" else ""}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = CosmicSurfaceVariant,
                                labelColor = CosmicTextPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlanetRowCard(
    position: PlanetPosition,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("planet_item_${position.planet.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x22F5D061)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = position.planet.symbol,
                        fontWeight = FontWeight.Bold,
                        color = StarlightGold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = position.planet.englishName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CosmicTextPrimary
                        )
                        if (position.isRetrograde) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Vakri (R)",
                                color = Color(0xFFF87171),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (position.isCombust) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Combust",
                                color = Color(0xFFFB923C),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (position.vargottama) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                color = Color(0x3310B981),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Vargottama",
                                    color = ExaltedGreen,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "House ${position.house} • ${position.sign.englishName} ${String.format("%.1f", position.degreeInSign)}°",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSecondary
                    )
                    Text(
                        text = "${position.nakshatra.name} Pada ${position.pada} (Lord: ${position.nakshatra.lord.englishName})",
                        style = MaterialTheme.typography.labelSmall,
                        color = CosmicTextTertiary
                    )
                }
            }

            // Dignity badge
            val badgeColor = when {
                position.dignity == Dignity.EXALTED || position.dignity == Dignity.MOOLATRIKONA -> ExaltedGreen
                position.dignity == Dignity.OWN_SIGN -> StarlightGold
                position.dignity == Dignity.DEBILITATED -> DebilitatedRed
                else -> NeutralBlue
            }

            Surface(
                color = badgeColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = position.dignity.label.substringBefore(" ("),
                    color = badgeColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
