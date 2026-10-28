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
import androidx.compose.material.icons.filled.*
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
import com.example.astro.data.LalKitabRemediesProvider
import com.example.astro.model.*
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.LocalizationManager

@Composable
fun YogasDashaScreen(
    yogas: List<VedicYoga>,
    dashaSystem: VimshottariDashaSystem?,
    remedies: List<LalKitabRemedy>,
    selectedRemedyPlanet: Planet,
    chartData: ChartData?,
    selectedPlanet: Planet,
    currentInsight: PlanetaryInsight?,
    selectedSubTab: Int,
    currentLanguage: AppLanguage,
    onSelectSubTab: (Int) -> Unit,
    onSelectRemedyPlanet: (Planet) -> Unit,
    onSelectPlanet: (Planet) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedMahadashaPlanet by remember { mutableStateOf<Planet?>(dashaSystem?.activeMahadasha?.planet) }

    Column(modifier = modifier.fillMaxSize()) {
        // Top Sub-Tabs Bar
        ScrollableTabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = CelestialNavy,
            contentColor = StarlightGold,
            edgePadding = 16.dp,
            divider = {},
            modifier = Modifier.fillMaxWidth().testTag("yogas_dasha_tab_row")
        ) {
            val tabs = listOf(
                "Yogas & Doshas (${yogas.size})",
                "Vimshottari Dasha",
                "Lal Kitab Remedies",
                "Planetary Intel"
            )
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSubTab == index,
                    onClick = { onSelectSubTab(index) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    },
                    selectedContentColor = StarlightGold,
                    unselectedContentColor = CosmicTextSecondary
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedSubTab) {
                // 0: VEDIC YOGAS & DOSHAS
                0 -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = StarlightGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Vedic Planetary Yogas (${yogas.size} Found)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = StarlightGold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Yogas are sacred planetary combinations defined by Maharishi Parashara that shape your destiny, wealth, moral character, and challenges. Below are all active yogas identified in your chart:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextSecondary
                                )
                            }
                        }
                    }

                    if (yogas.isEmpty()) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "No major classical yogas detected in primary positions.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = CosmicTextSecondary
                                    )
                                }
                            }
                        }
                    } else {
                        items(yogas) { yoga ->
                            val isDosha = yoga.category.contains("Dosha", ignoreCase = true)
                            val badgeColor = if (isDosha) DebilitatedRed else StarlightGold

                            Card(
                                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                                shape = RoundedCornerShape(14.dp),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(badgeColor.copy(alpha = 0.5f))),
                                modifier = Modifier.fillMaxWidth().testTag("yoga_card_${yoga.id}")
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = yoga.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDosha) Color(0xFFFCA5A5) else StarlightGold
                                            )
                                            Text(
                                                text = "Sanskrit: ${yoga.sanskritName}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = SacredCyan
                                            )
                                        }

                                        Surface(
                                            color = badgeColor.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = yoga.category,
                                                color = badgeColor,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Triggering Planets
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Triggered by: ",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CosmicTextTertiary
                                        )
                                        yoga.triggeringPlanets.forEach { p ->
                                            Surface(
                                                color = Color(0x22F5D061),
                                                shape = RoundedCornerShape(4.dp),
                                                modifier = Modifier.padding(end = 4.dp)
                                            ) {
                                                Text(
                                                    text = "${p.symbol} ${p.englishName}",
                                                    color = StarlightGold,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = yoga.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextPrimary
                                    )

                                    if (yoga.benefits.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "Benefits & Auspicious Results:",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ExaltedGreen
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        yoga.benefits.forEach { b ->
                                            Text(
                                                text = "• $b",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = CosmicTextSecondary,
                                                modifier = Modifier.padding(bottom = 2.dp)
                                            )
                                        }
                                    }

                                    if (yoga.disadvantages.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = if (isDosha) "Afflictions & Vulnerabilities:" else "Nuances & Disadvantages:",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = DebilitatedRed
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        yoga.disadvantages.forEach { d ->
                                            Text(
                                                text = "• $d",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFFFCA5A5),
                                                modifier = Modifier.padding(bottom = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 1: VIMSHOTTARI DASHA SYSTEM
                1 -> {
                    if (dashaSystem != null) {
                        // Current Active Dasha Highlight Card
                        item {
                            val activeMaha = dashaSystem.activeMahadasha
                            val activeAntar = dashaSystem.currentActiveAntardasha

                            Card(
                                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                                shape = RoundedCornerShape(16.dp),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StarlightGold)),
                                modifier = Modifier.fillMaxWidth().testTag("active_dasha_card")
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Timeline,
                                                    contentDescription = null,
                                                    tint = StarlightGold,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Current Active Vimshottari Period",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = StarlightGold
                                                )
                                            }
                                            Text(
                                                text = "${activeMaha?.planet?.englishName ?: ""} Mahadasha / ${activeAntar?.planet?.englishName ?: ""} Antardasha",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = CosmicTextPrimary
                                            )
                                        }

                                        Surface(
                                            color = Color(0x3310B981),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "NOW ACTIVE",
                                                color = ExaltedGreen,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Duration: ${activeAntar?.startDateFormatted ?: ""} to ${activeAntar?.endDateFormatted ?: ""}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SacredCyan
                                    )

                                    if (activeAntar != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = activeAntar.generalTheme,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = CosmicTextPrimary
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "Potential Gains in this Period:",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ExaltedGreen
                                        )
                                        activeAntar.potentialGains.forEach { g ->
                                            Text(text = "• $g", style = MaterialTheme.typography.bodySmall, color = CosmicTextSecondary)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Risk of Losses / Pitfalls:",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = DebilitatedRed
                                        )
                                        activeAntar.riskOfLosses.forEach { l ->
                                            Text(text = "• $l", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFCA5A5))
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "What You Need to Do (Auspicious Actions):",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = StarlightGold
                                        )
                                        activeAntar.whatToDo.forEach { td ->
                                            Text(text = "✓ $td", style = MaterialTheme.typography.bodySmall, color = CosmicTextPrimary)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "What to Strictly Avoid:",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFB923C)
                                        )
                                        activeAntar.whatToAvoid.forEach { ta ->
                                            Text(text = "✕ $ta", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFED7AA))
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "120-Year Vimshottari Mahadashas (Tap to expand Antardashas)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = StarlightGold
                            )
                        }

                        // Mahadasha list
                        items(dashaSystem.mahadashas) { md ->
                            val isExpanded = expandedMahadashaPlanet == md.planet

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (md.isActive) CosmicSurfaceVariant else CosmicSurface
                                ),
                                shape = RoundedCornerShape(12.dp),
                                border = if (md.isActive) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StarlightGold)) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        expandedMahadashaPlanet = if (isExpanded) null else md.planet
                                    }
                                    .testTag("mahadasha_card_${md.planet.name.lowercase()}")
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = md.planet.symbol,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = StarlightGold
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "${md.planet.englishName} Mahadasha",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = CosmicTextPrimary
                                                )
                                                Text(
                                                    text = "${md.startDateFormatted} — ${md.endDateFormatted}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = SacredCyan
                                                )
                                            }
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (md.isActive) {
                                                Surface(
                                                    color = Color(0x3310B981),
                                                    shape = RoundedCornerShape(6.dp)
                                                ) {
                                                    Text(
                                                        text = "Active",
                                                        color = ExaltedGreen,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                            }
                                            Icon(
                                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                                contentDescription = null,
                                                tint = CosmicTextSecondary
                                            )
                                        }
                                    }

                                    if (isExpanded) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        HorizontalDivider(color = Color(0x22FFFFFF))
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "Sub-Periods (Antardashas):",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = StarlightGold
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))

                                        // If this is the active mahadasha, show actual active antardashas
                                        val antardashasList = if (md.isActive) dashaSystem.activeAntardashas else emptyList()

                                        if (antardashasList.isNotEmpty()) {
                                            antardashasList.forEach { ad ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = "${ad.planet.symbol} ${ad.planet.englishName}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            fontWeight = if (ad.isActive) FontWeight.Bold else FontWeight.Normal,
                                                            color = if (ad.isActive) StarlightGold else CosmicTextPrimary
                                                        )
                                                        if (ad.isActive) {
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Text(
                                                                text = "(Current)",
                                                                color = ExaltedGreen,
                                                                fontSize = 9.sp,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                        }
                                                    }
                                                    Text(
                                                        text = "${ad.startDateFormatted} to ${ad.endDateFormatted}",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = if (ad.isActive) SacredCyan else CosmicTextTertiary
                                                    )
                                                }
                                            }
                                        } else {
                                            Text(
                                                text = "General Theme: ${md.generalTheme}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = CosmicTextSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2: LAL KITAB REMEDIES
                2 -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.HealthAndSafety,
                                        contentDescription = null,
                                        tint = StarlightGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Lal Kitab Astrological Remedies",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = StarlightGold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Authentic classical Lal Kitab Upays (remedies). Lal Kitab uses simple, compassionate karmic remedies (daan, seva, and behavioral alignment) to ward off malefic planetary energies.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextSecondary
                                )
                            }
                        }
                    }

                    // Active Dasha Remedies Header if available
                    if (dashaSystem?.activeMahadasha != null) {
                        item {
                            val activeLord = dashaSystem.activeMahadasha.planet
                            val activeAntarLord = dashaSystem.currentActiveAntardasha?.planet
                            val dashaRemedies = LalKitabRemediesProvider.getRemediesForActiveDasha(activeLord, activeAntarLord)

                            Card(
                                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                                shape = RoundedCornerShape(14.dp),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ExaltedGreen)),
                                modifier = Modifier.fillMaxWidth().testTag("active_dasha_remedies_card")
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Priority Remedies for Active Dasha (${activeLord.englishName})",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ExaltedGreen
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Since you are currently running ${activeLord.englishName} Mahadasha, following these specific remedies balances your aura immediately:",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    dashaRemedies.take(2).forEach { rem ->
                                        Text(
                                            text = "• ${rem.title}: ${rem.remedyAction}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CosmicTextPrimary,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Planet selector row
                    item {
                        Text(
                            text = "Browse Remedies by Planet (Navagraha)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(Planet.entries) { planet ->
                                val isSelected = planet == selectedRemedyPlanet
                                Surface(
                                    color = if (isSelected) StarlightGold else CosmicSurface,
                                    contentColor = if (isSelected) Color(0xFF1E1500) else CosmicTextPrimary,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .clickable { onSelectRemedyPlanet(planet) }
                                        .testTag("remedy_planet_chip_${planet.name.lowercase()}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = planet.symbol,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = planet.englishName,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Selected Planet remedies
                    items(remedies) { remedy ->
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
                                    Text(
                                        text = remedy.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = StarlightGold
                                    )
                                    Surface(
                                        color = Color(0x3338BDF8),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "House ${remedy.house}",
                                            color = SacredCyan,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = Color(0x1AF5D061),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "Remedy Action (Upay):",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = StarlightGold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = remedy.remedyAction,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CosmicTextPrimary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Astrological Purpose: ${remedy.purpose}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextSecondary
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Strict Precautions: ${remedy.precautions}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFFCA5A5)
                                )
                            }
                        }
                    }
                }

                // 3: PLANETARY INTEL (Deep behavioral insight)
                3 -> {
                    item {
                        Text(
                            text = "Select Planet to Inspect Behavioral Intel",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(Planet.entries) { planet ->
                                val isSelected = planet == selectedPlanet
                                Surface(
                                    color = if (isSelected) StarlightGold else CosmicSurface,
                                    contentColor = if (isSelected) Color(0xFF1E1500) else CosmicTextPrimary,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .clickable { onSelectPlanet(planet) }
                                        .testTag("intel_planet_chip_${planet.name.lowercase()}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = planet.symbol,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = planet.englishName,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (currentInsight != null) {
                        val pos = chartData?.getPlanet(selectedPlanet)
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "${selectedPlanet.englishName} in House ${pos?.house ?: 1} (${pos?.sign?.englishName ?: ""})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = StarlightGold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = currentInsight.rootWhy,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = CosmicTextPrimary
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Innate Powers & Benefits:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = ExaltedGreen
                                    )
                                    currentInsight.giftsAndBenefits.forEach { b ->
                                        Text(text = "• $b", style = MaterialTheme.typography.bodySmall, color = CosmicTextSecondary)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Precautions & Blind Spots:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = DebilitatedRed
                                    )
                                    currentInsight.precautionsAndBlindSpots.forEach { p ->
                                        Text(text = "• $p", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFCA5A5))
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Practical Lifestyle Changes:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SacredCyan
                                    )
                                    currentInsight.practicalLifestyleHabits.forEach { l ->
                                        Text(text = "• $l", style = MaterialTheme.typography.bodySmall, color = CosmicTextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
