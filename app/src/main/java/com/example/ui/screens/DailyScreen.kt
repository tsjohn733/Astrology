package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.astro.calc.TransitEngine
import com.example.astro.calc.TransitTimelineEngine
import com.example.astro.model.ChartData
import com.example.astro.model.DailyPrediction
import com.example.astro.model.TransitTimelineItem
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.LocalizationManager

import com.example.astro.model.VimshottariDashaSystem

@Composable
fun DailyScreen(
    chartData: ChartData?,
    dailyPrediction: DailyPrediction?,
    currentLanguage: AppLanguage,
    onOpenReminderDialog: () -> Unit,
    dashaSystem: VimshottariDashaSystem? = null,
    modifier: Modifier = Modifier
) {
    if (dailyPrediction == null || chartData == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = StarlightGold)
        }
        return
    }

    val currentTransits = TransitEngine.getCurrentTransits()
    val timelineItems = remember(chartData) {
        TransitTimelineEngine.generateTimeline(chartData)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Date Header & Reminder Action Bar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("gochar_transits", currentLanguage),
                            style = MaterialTheme.typography.labelMedium,
                            color = SacredCyan,
                            fontWeight = FontWeight.Bold
                        )

                        FilledTonalButton(
                            onClick = onOpenReminderDialog,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color(0x33F5D061),
                                contentColor = StarlightGold
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("set_daily_reminder_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Reminder",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dailyPrediction.dateText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StarlightGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = dailyPrediction.transitsSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextPrimary,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Active Dasha vs Today's Date Comparison Banner
        if (dashaSystem?.activeMahadasha != null && dashaSystem.currentActiveAntardasha != null) {
            val activeMaha = dashaSystem.activeMahadasha
            val activeAntar = dashaSystem.currentActiveAntardasha

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StarlightGold)),
                    modifier = Modifier.fillMaxWidth().testTag("daily_active_dasha_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timeline,
                                    contentDescription = null,
                                    tint = StarlightGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Active Dasha vs Today's Date",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = StarlightGold
                                )
                            }
                            Surface(
                                color = Color(0x3310B981),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "DATE MATCHED",
                                    color = ExaltedGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${activeMaha.planet.englishName} Mahadasha (${activeMaha.startDateFormatted} to ${activeMaha.endDateFormatted})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CosmicTextPrimary
                        )
                        Text(
                            text = "Active Sub-period: ${activeAntar.planet.englishName} Antardasha (${activeAntar.startDateFormatted} to ${activeAntar.endDateFormatted})",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = SacredCyan
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "How Today's Transits Interact with Your Dasha: In Vedic astrology, Gochar transits only trigger events if the active Dasha lords permit. With ${activeMaha.planet.englishName} directing your long-term chapter and ${activeAntar.planet.englishName} executing day-to-day karmas, today's planetary movements filter directly through their dignity.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = Color(0x2210B981),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "What to Do Today:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ExaltedGreen
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = activeAntar.whatToDo.firstOrNull() ?: "Maintain moral discipline",
                                        fontSize = 11.sp,
                                        color = CosmicTextPrimary
                                    )
                                }
                            }

                            Surface(
                                color = Color(0x22EF4444),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "What to Avoid Today:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DebilitatedRed
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = activeAntar.whatToAvoid.firstOrNull() ?: "Impulsive arguments",
                                        fontSize = 11.sp,
                                        color = Color(0xFFFCA5A5)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tip of the Day Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF261E0A)),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StarlightGold)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x33F5D061)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Tip of the Day",
                            tint = StarlightGold
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = LocalizationManager.getString("tip_of_the_day", currentLanguage),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dailyPrediction.tipOfTheDay,
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Section: Upcoming Ingress & Transit Timeline
        item {
            Column {
                Text(
                    text = LocalizationManager.getString("transit_timeline_title", currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = StarlightGold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = LocalizationManager.getString("transit_timeline_subtitle", currentLanguage),
                    style = MaterialTheme.typography.bodySmall,
                    color = CosmicTextTertiary
                )
            }
        }

        // Timeline items
        items(timelineItems) { timelineItem ->
            TransitTimelineCard(item = timelineItem)
        }

        // Section: Predictions for Love, Career, Health
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Personalized Transit Predictions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = StarlightGold
            )
        }

        item {
            PredictionCard(
                title = LocalizationManager.getString("love_title", currentLanguage),
                icon = Icons.Default.Favorite,
                iconTint = Color(0xFFF43F5E),
                content = dailyPrediction.loveInsight
            )
        }

        item {
            PredictionCard(
                title = LocalizationManager.getString("career_title", currentLanguage),
                icon = Icons.Default.Work,
                iconTint = SacredCyan,
                content = dailyPrediction.careerInsight
            )
        }

        item {
            PredictionCard(
                title = LocalizationManager.getString("health_title", currentLanguage),
                icon = Icons.Default.FitnessCenter,
                iconTint = ExaltedGreen,
                content = dailyPrediction.healthInsight
            )
        }

        // Current Sky Coordinates Snapshot
        item {
            Text(
                text = "Current Real-Time Sky Positions",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = CosmicTextSecondary
            )
        }

        items(currentTransits) { pos ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = pos.planet.symbol,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = pos.planet.englishName,
                            fontWeight = FontWeight.Medium,
                            color = CosmicTextPrimary
                        )
                    }
                    Text(
                        text = "${pos.sign.englishName} (${String.format("%.1f", pos.degree)}°)${if (pos.isRetrograde) " (R)" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun TransitTimelineCard(
    item: TransitTimelineItem,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isExpanded) StarlightGold else Color(0x33F5D061)
            )
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .testTag("transit_timeline_card_${item.planet.name.lowercase()}")
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
                        Text(
                            text = item.planet.symbol,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = item.planet.englishName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CosmicTextPrimary
                        )
                        Text(
                            text = "In ${item.currentSign.englishName} (${String.format("%.1f", item.currentDegree)}°) • Stay: ${item.totalStayInSign}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSecondary
                        )
                    }
                }

                Surface(
                    color = Color(0x3338BDF8),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${item.daysToNextIngress}d left",
                        color = SacredCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Degree progress bar in current sign
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.currentSign.englishName} (${String.format("%.1f", item.currentDegree)}°)",
                        fontSize = 10.sp,
                        color = CosmicTextTertiary
                    )
                    Text(
                        text = "Next: ${item.nextSign.englishName} (~${item.approximateIngressDate})",
                        fontSize = 10.sp,
                        color = StarlightGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { item.progressPercent },
                    color = StarlightGold,
                    trackColor = CosmicSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Next Ingress summary banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Shift into House ${item.upcomingHouseActivated} (${item.nextSign.englishName})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SacredCyan
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isExpanded) "Hide Details" else "View Chart Impact",
                        fontSize = 11.sp,
                        color = StarlightGold,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = StarlightGold,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Expandable Chart Impact Breakdown
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    HorizontalDivider(color = Color(0x33334155))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = item.chartImpactTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = StarlightGold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.chartImpactDescription,
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextPrimary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Suggested Preparation Habits:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SacredCyan
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    item.preparationHabits.forEach { habit ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text(text = "✦ ", color = StarlightGold, fontSize = 11.sp)
                            Text(
                                text = habit,
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

@Composable
fun PredictionCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPrimary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                color = CosmicTextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

