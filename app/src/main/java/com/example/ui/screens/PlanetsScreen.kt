package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
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
import com.example.ui.theme.*

@Composable
fun PlanetsScreen(
    chartData: ChartData?,
    selectedPlanet: Planet,
    currentInsight: PlanetaryInsight?,
    onSelectPlanet: (Planet) -> Unit,
    modifier: Modifier = Modifier
) {
    if (chartData == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = StarlightGold)
        }
        return
    }

    val currentPosition = chartData.getPlanet(selectedPlanet)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Horizontal Planet Selector
        item {
            Text(
                text = "Select Planet (Graha)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = StarlightGold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(chartData.planets) { p ->
                    val isSelected = p.planet == selectedPlanet
                    Surface(
                        color = if (isSelected) StarlightGold else CosmicSurface,
                        contentColor = if (isSelected) Color(0xFF1E1500) else CosmicTextPrimary,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clickable { onSelectPlanet(p.planet) }
                            .testTag("planet_tab_${p.planet.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = p.planet.symbol,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = p.planet.englishName,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        if (currentPosition != null && currentInsight != null) {
            // Planet Hero Header Card
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33F5D061)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = currentPosition.planet.symbol,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = StarlightGold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "${currentPosition.planet.englishName} (${currentPosition.planet.sanskritName})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicTextPrimary
                                    )
                                    Text(
                                        text = "House ${currentPosition.house} • ${currentPosition.sign.englishName} ${String.format("%.2f", currentPosition.degreeInSign)}°",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SacredCyan
                                    )
                                }
                            }

                            // Dignity Chip
                            val dignityColor = when {
                                currentPosition.dignity == Dignity.EXALTED || currentPosition.dignity == Dignity.MOOLATRIKONA -> ExaltedGreen
                                currentPosition.dignity == Dignity.OWN_SIGN -> StarlightGold
                                currentPosition.dignity == Dignity.DEBILITATED -> DebilitatedRed
                                else -> NeutralBlue
                            }
                            Surface(
                                color = dignityColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = currentPosition.dignity.label,
                                    color = dignityColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Nakshatra: ${currentPosition.nakshatra.name} (Pada ${currentPosition.pada}) • Deity: ${currentPosition.nakshatra.deity} • Ruler: ${currentPosition.nakshatra.lord.englishName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSecondary
                        )

                        if (currentPosition.isRetrograde || currentPosition.isCombust || currentPosition.vargottama) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (currentPosition.isRetrograde) {
                                    Surface(color = Color(0x33F87171), shape = RoundedCornerShape(6.dp)) {
                                        Text(
                                            text = "Vakri (Retrograde): Heightened introspective force",
                                            color = Color(0xFFF87171),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                                if (currentPosition.isCombust) {
                                    Surface(color = Color(0x33FB923C), shape = RoundedCornerShape(6.dp)) {
                                        Text(
                                            text = "Asta (Combust): Purified by Solar rays",
                                            color = Color(0xFFFB923C),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                                if (currentPosition.vargottama) {
                                    Surface(color = Color(0x3310B981), shape = RoundedCornerShape(6.dp)) {
                                        Text(
                                            text = "Vargottama (D1 & D9 aligned)",
                                            color = ExaltedGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 1: Why This Planet Behaves This Way
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                    shape = RoundedCornerShape(16.dp),
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
                                text = "Why This Planet Behaves This Way In Your Life",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = StarlightGold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = currentInsight.rootWhy,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CosmicTextPrimary,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Psychological Signature: ${currentInsight.psychologicalTendency}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Section 2: Benefits & Natural Gifts
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = ExaltedGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Benefits & Innate Powers",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ExaltedGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        currentInsight.giftsAndBenefits.forEach { benefit ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(text = "✦ ", color = StarlightGold, fontWeight = FontWeight.Bold)
                                Text(
                                    text = benefit,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Section 3: Precautions & Blind Spots
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFF87171),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Precautions & Behavioral Blind Spots",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF87171)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        currentInsight.precautionsAndBlindSpots.forEach { precaution ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(text = "⚠ ", color = Color(0xFFF87171), fontWeight = FontWeight.Bold)
                                Text(
                                    text = precaution,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Section 4: Practical Lifestyle Remedies & Habits
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF142032)),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SacredCyan)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Practical Lifestyle Changes & Habits",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SacredCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Daily behavioral adjustments to align with this planet's cosmic frequency:",
                            style = MaterialTheme.typography.labelSmall,
                            color = CosmicTextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        currentInsight.practicalLifestyleHabits.forEachIndexed { idx, habit ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Surface(
                                        color = Color(0x3338BDF8),
                                        shape = CircleShape,
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${idx + 1}",
                                                color = SacredCyan,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = habit,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextPrimary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
