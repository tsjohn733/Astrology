package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun VargaScreen(
    chartData: ChartData?,
    chartStyle: ChartStyle,
    selectedGroup: VargaGroup,
    selectedVarga: VargaType,
    vaisheshikamsaReports: List<VaisheshikamsaReport>,
    onSelectGroup: (VargaGroup) -> Unit,
    onSelectVarga: (VargaType) -> Unit,
    onSelectPlanet: (Planet) -> Unit,
    onToggleChartStyle: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (chartData == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = StarlightGold)
        }
        return
    }

    var selectedVargaHouse by remember { mutableStateOf<Int?>(1) }

    val availableVargas = when (selectedGroup) {
        VargaGroup.SAPTA_VARGA -> VargaType.entries.filter { it.inSaptavarga }
        VargaGroup.DASHA_VARGA -> VargaType.entries.filter { it.inDashavarga }
        VargaGroup.SHODASHA_VARGA -> VargaType.entries
    }

    val currentVargaChart = chartData.vargaCharts[selectedVarga]

    // Construct derived ChartData for the interactive visual KundaliCanvas
    val vargaKundaliChartData = remember(chartData, selectedVarga, currentVargaChart) {
        if (currentVargaChart != null) {
            createChartDataForVarga(currentVargaChart, chartData)
        } else {
            chartData
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Group Filter Chips (Shodashvarga, Saptavarga, Dashavarga)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Varga Classification Scheme",
                        style = MaterialTheme.typography.labelMedium,
                        color = CosmicTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedGroup == VargaGroup.SHODASHA_VARGA,
                            onClick = { onSelectGroup(VargaGroup.SHODASHA_VARGA) },
                            label = { Text("Shodashvarga (16)", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StarlightGold,
                                selectedLabelColor = Color(0xFF1E1500)
                            ),
                            modifier = Modifier.testTag("group_shodashvarga")
                        )
                        FilterChip(
                            selected = selectedGroup == VargaGroup.DASHA_VARGA,
                            onClick = { onSelectGroup(VargaGroup.DASHA_VARGA) },
                            label = { Text("Dashavarga (10)", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StarlightGold,
                                selectedLabelColor = Color(0xFF1E1500)
                            ),
                            modifier = Modifier.testTag("group_dashavarga")
                        )
                        FilterChip(
                            selected = selectedGroup == VargaGroup.SAPTA_VARGA,
                            onClick = { onSelectGroup(VargaGroup.SAPTA_VARGA) },
                            label = { Text("Saptavarga (7)", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StarlightGold,
                                selectedLabelColor = Color(0xFF1E1500)
                            ),
                            modifier = Modifier.testTag("group_saptavarga")
                        )
                    }
                }
            }
        }

        // Horizontal Carousel of available Vargas
        item {
            Text(
                text = "Select Divisional Chart (D1 - D60)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = StarlightGold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(availableVargas) { varga ->
                    val isSelected = varga == selectedVarga
                    Surface(
                        color = if (isSelected) StarlightGold else CosmicSurface,
                        contentColor = if (isSelected) Color(0xFF1E1500) else CosmicTextPrimary,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clickable { onSelectVarga(varga) }
                            .testTag("varga_chip_${varga.name.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = varga.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = varga.title,
                                fontSize = 11.sp,
                                color = if (isSelected) Color(0xFF4A3800) else CosmicTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // VISUAL KUNDALI DIAGRAM FOR THE SELECTED VARGA
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0x44F5D061))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = StarlightGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${selectedVarga.name} Kundali Chart Diagram",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = StarlightGold
                                )
                            }
                            Text(
                                text = "Interactive diagram with calculated divisional lagna & houses",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSecondary
                            )
                        }

                        IconButton(
                            onClick = onToggleChartStyle,
                            modifier = Modifier.testTag("btn_toggle_varga_chart_style")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Toggle Style",
                                tint = SacredCyan
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    KundaliCanvas(
                        chartData = vargaKundaliChartData,
                        chartStyle = chartStyle,
                        selectedHouse = selectedVargaHouse,
                        onHouseSelected = { selectedVargaHouse = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Style: ${if (chartStyle == ChartStyle.NORTH_INDIAN) "North Indian (Diamond)" else "South Indian (Zodiac Box)"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextTertiary
                        )
                        if (currentVargaChart != null) {
                            Text(
                                text = "Lagna: ${currentVargaChart.ascendantSign.englishName}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = SacredCyan
                            )
                        }
                    }
                }
            }
        }

        // Selected Varga Overview & Purpose
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0x33F5D061))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${selectedVarga.name} • ${selectedVarga.title}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = StarlightGold
                            )
                            Text(
                                text = "Classical Sanskrit: ${selectedVarga.sanskritName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SacredCyan
                            )
                        }

                        if (currentVargaChart != null) {
                            Surface(
                                color = Color(0x3338BDF8),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Lagna: ${currentVargaChart.ascendantSign.englishName}",
                                    color = SacredCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = selectedVarga.purpose,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CosmicTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = getVargaDetailedGuide(selectedVarga),
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSecondary
                    )
                }
            }
        }

        // Placements in this Varga
        item {
            Text(
                text = "Planetary Placements in ${selectedVarga.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = StarlightGold
            )
        }

        if (currentVargaChart != null) {
            items(currentVargaChart.placements) { placement ->
                val d1Pos = chartData.getPlanet(placement.planet)
                val isVargottama = d1Pos != null && d1Pos.sign == placement.sign

                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectPlanet(placement.planet) }
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x22F5D061)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = placement.planet.symbol,
                                    fontWeight = FontWeight.Bold,
                                    color = StarlightGold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = placement.planet.englishName,
                                    fontWeight = FontWeight.Bold,
                                    color = CosmicTextPrimary
                                )
                                Text(
                                    text = "Seated in ${placement.sign.englishName} (${placement.sign.sanskritName})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextSecondary
                                )
                            }
                        }

                        if (isVargottama) {
                            Surface(
                                color = Color(0x3310B981),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Vargottama Strength",
                                    color = ExaltedGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "Element: ${placement.sign.element}",
                                color = CosmicTextTertiary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // VAISHESHIKAMSA DIGNITY TIERS SECTION
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StarlightGold)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = StarlightGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Vaisheshikamsa Dignity Tiers",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "According to Maharishi Parashara, when a planet repeats in its own or exalted sign across multiple divisional charts, it attains elevated celestial dignity (e.g. Kalpavriksha in Shodashvarga, Parijata in Dashavarga). Below is your chart's exact dignity analysis:",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSecondary
                    )
                }
            }
        }

        // Reports for each planet
        items(vaisheshikamsaReports) { report ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(14.dp),
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
                                text = report.planet.symbol,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = StarlightGold
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = report.planet.englishName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPrimary
                            )
                        }

                        // Badge for Tier
                        val badgeTier = report.shodashvargaTier ?: report.dashavargaTier ?: "Normal"
                        Surface(
                            color = if (report.shodashvargaTier != null || report.dashavargaTier != null) Color(0x33F5D061) else Color(0x22FFFFFF),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = badgeTier,
                                color = if (report.shodashvargaTier != null || report.dashavargaTier != null) StarlightGold else CosmicTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Shodashvarga (16): ${report.shodashvargaCount}/16 Vargas (${report.shodashvargaTier ?: "Standard"})",
                            style = MaterialTheme.typography.bodySmall,
                            color = SacredCyan
                        )
                        Text(
                            text = "Dashavarga (10): ${report.dashavargaCount}/10 Vargas (${report.dashavargaTier ?: "Standard"})",
                            style = MaterialTheme.typography.bodySmall,
                            color = SacredCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Meaning of Tier: ${report.nameMeaning}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = CosmicTextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Bestowed Powers: ${report.bestowedPowers}",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = Color(0x1AF5D061),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "How to Extract Maximum Benefits:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = StarlightGold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = report.optimizationAdvice,
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

private fun createChartDataForVarga(vargaChart: VargaChart, baseChart: ChartData): ChartData {
    val ascSign = vargaChart.ascendantSign
    val planets = vargaChart.placements.map { placement ->
        val orig = baseChart.getPlanet(placement.planet)
        val house = ((placement.sign.number - ascSign.number + 12) % 12) + 1
        val dummyNakshatra = orig?.nakshatra ?: Nakshatra(1, "Ashwini", Planet.KETU, "Ashwini Kumaras")
        PlanetPosition(
            planet = placement.planet,
            longitude = (placement.sign.number - 1) * 30.0 + 15.0,
            sign = placement.sign,
            degreeInSign = 15.0,
            house = house,
            nakshatra = dummyNakshatra,
            pada = orig?.pada ?: 1,
            isRetrograde = orig?.isRetrograde ?: false,
            isCombust = orig?.isCombust ?: false,
            dignity = orig?.dignity ?: Dignity.NEUTRAL,
            vargottama = orig?.sign == placement.sign
        )
    }
    return ChartData(
        ascendantDegree = (ascSign.number - 1) * 30.0 + 15.0,
        ascendantSign = ascSign,
        ayanamsha = baseChart.ayanamsha,
        planets = planets,
        vargaCharts = emptyMap()
    )
}

private fun getVargaDetailedGuide(varga: VargaType): String = when (varga) {
    VargaType.D1 -> "The foundational matrix. Reflects your external biological shell, general vitality, constitution, and macro life trajectories."
    VargaType.D2 -> "Wealth & Liquidity. Parashara divides each sign into solar and lunar halves, illustrating your capacity to convert effort into sustainable treasury and speech."
    VargaType.D3 -> "Energy, Courage & Siblings. Analyzes your initiative, competitive drive, physical stamina, and brotherly/sisterly bonds."
    VargaType.D4 -> "Fixed Assets & Emotional Haven. Examines your inner psychological peace, relationship with your mother, ancestral real estate, and fortune."
    VargaType.D7 -> "Fruits of Labor & Progeny. Reveals the manifestation of long-term projects, creative legacy, children, and mutual partnerships."
    VargaType.D9 -> "The Crucial Dharmamsha. Essential companion to D1. Shows the fruit of past-life merit, marital chemistry, and the true soul trajectory after age 30. Planets in identical signs in D1 and D9 become Vargottama (unshakable power)."
    VargaType.D10 -> "Career & Public Authority. Directs your professional achievements, social respect, executive leadership, and vocational destiny."
    VargaType.D12 -> "Ancestral Lineage. Traces the subconscious genetic inheritance received from parents, forebears, and past familial karma."
    VargaType.D16 -> "Luxury & Conveyances. Reflects comfort in vehicles, material pleasures, and psychological ease when navigating modern conveniences."
    VargaType.D20 -> "Spiritual Evolution. Maps your inner devotion, meditative aptitude, mantra capacity, and connection to higher consciousness."
    VargaType.D24 -> "Academic Mastery & Intellect. Guides technical training, research skills, higher education, and mental synthesis."
    VargaType.D27 -> "Physical Strengths & Flaws. Assesses psychological stamina, bodily endurance, and specific physical vulnerabilities."
    VargaType.D30 -> "Karmic Challenges & Misfortunes. Highlights psychological shadow tendencies, moral dilemmas, and chronic blind spots requiring conscious vigilance."
    VargaType.D40 -> "Auspicious Occurrences. Indicates hereditary fortune, divine protection, and auspicious timing across generations."
    VargaType.D45 -> "Moral Character & Integrity. Analyzes subconscious ethical rectitude and all-around purity of intent."
    VargaType.D60 -> "The Master Shashtiamsha. The ultimate sub-division (0°30' slices). Uncovers the exact karmic seeds and past-life actions that shaped your present incarnation."
}
