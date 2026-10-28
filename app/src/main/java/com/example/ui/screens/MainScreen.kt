package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.AstrologyViewModel
import com.example.util.LocalizationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AstrologyViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = LocalizationManager.getString("app_name", state.currentLanguage),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = LocalizationManager.getString("app_subtitle", state.currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextTertiary
                        )
                    }
                },
                actions = {
                    // Language Switcher button
                    IconButton(
                        onClick = { viewModel.setLanguageDialogOpen(true) },
                        modifier = Modifier.testTag("top_bar_language_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = StarlightGold
                        )
                    }

                    // Daily Reminder Settings button
                    IconButton(
                        onClick = { viewModel.setReminderDialogOpen(true) },
                        modifier = Modifier.testTag("top_bar_reminder_button")
                    ) {
                        Icon(
                            imageVector = if (state.isReminderEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                            contentDescription = "Reminders",
                            tint = if (state.isReminderEnabled) StarlightGold else CosmicTextSecondary
                        )
                    }

                    // Profile Switcher button
                    Surface(
                        color = Color(0x33F5D061),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .clickable { viewModel.setProfileEditorOpen(true) }
                            .padding(end = 8.dp)
                            .testTag("profile_switcher_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = StarlightGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = state.currentProfileName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = StarlightGold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CelestialNavy,
                    titleContentColor = StarlightGold
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CosmicSurface,
                contentColor = StarlightGold,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("main_bottom_nav_bar")
            ) {
                AppTab.entries.forEach { tab ->
                    val isSelected = state.activeTab == tab
                    val icon = when (tab) {
                        AppTab.CHART -> Icons.Default.AutoGraph
                        AppTab.VARGAS -> Icons.Default.GridView
                        AppTab.YOGAS_DASHA -> Icons.Default.Timeline
                        AppTab.DAILY -> Icons.Default.Today
                        AppTab.CONSULT -> Icons.Default.Psychology
                    }

                    val localizedTitle = LocalizationManager.getString(tab.titleKey, state.currentLanguage)

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(tab) },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = localizedTitle,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = localizedTitle,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1E1500),
                            selectedTextColor = StarlightGold,
                            indicatorColor = StarlightGold,
                            unselectedIconColor = CosmicTextSecondary,
                            unselectedTextColor = CosmicTextSecondary
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        },
        containerColor = CelestialNavy,
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.activeTab) {
                AppTab.CHART -> {
                    ChartScreen(
                        chartData = state.chartData,
                        chartStyle = state.chartStyle,
                        selectedHouse = state.selectedHouse,
                        moonProfile = state.moonProfile,
                        aspects = state.aspects,
                        onHouseSelected = { viewModel.selectHouse(it) },
                        onToggleChartStyle = { viewModel.toggleChartStyle() },
                        onSelectPlanet = {
                            viewModel.selectPlanet(it)
                            viewModel.selectDashaTab(3)
                            viewModel.selectTab(AppTab.YOGAS_DASHA)
                        }
                    )
                }
                AppTab.VARGAS -> {
                    VargaScreen(
                        chartData = state.chartData,
                        chartStyle = state.chartStyle,
                        selectedGroup = state.vargaGroup,
                        selectedVarga = state.selectedVarga,
                        vaisheshikamsaReports = state.vaisheshikamsaReports,
                        onSelectGroup = { viewModel.selectVargaGroup(it) },
                        onSelectVarga = { viewModel.selectVarga(it) },
                        onSelectPlanet = {
                            viewModel.selectPlanet(it)
                            viewModel.selectDashaTab(3)
                            viewModel.selectTab(AppTab.YOGAS_DASHA)
                        },
                        onToggleChartStyle = { viewModel.toggleChartStyle() }
                    )
                }
                AppTab.YOGAS_DASHA -> {
                    YogasDashaScreen(
                        yogas = state.detectedYogas,
                        dashaSystem = state.dashaSystem,
                        remedies = state.lalKitabRemedies,
                        selectedRemedyPlanet = state.selectedRemedyPlanet,
                        chartData = state.chartData,
                        selectedPlanet = state.selectedPlanet,
                        currentInsight = state.currentPlanetInsight,
                        selectedSubTab = state.selectedDashaTab,
                        currentLanguage = state.currentLanguage,
                        onSelectSubTab = { viewModel.selectDashaTab(it) },
                        onSelectRemedyPlanet = { viewModel.selectRemedyPlanet(it) },
                        onSelectPlanet = { viewModel.selectPlanet(it) }
                    )
                }
                AppTab.DAILY -> {
                    DailyScreen(
                        chartData = state.chartData,
                        dailyPrediction = state.dailyPrediction,
                        currentLanguage = state.currentLanguage,
                        onOpenReminderDialog = { viewModel.setReminderDialogOpen(true) },
                        dashaSystem = state.dashaSystem
                    )
                }
                AppTab.CONSULT -> {
                    ConsultScreen(
                        question = state.consultQuestion,
                        answer = state.consultAnswer,
                        isLoading = state.isConsultLoading,
                        error = state.consultError,
                        onQuestionChange = { viewModel.updateConsultQuestion(it) },
                        onAskQuestion = { viewModel.askAstrologer(it) }
                    )
                }
            }
        }

        // Profile Switcher & Add Dialog
        ProfileDialog(
            isOpen = state.isProfileEditorOpen,
            savedProfiles = state.savedProfiles,
            currentProfileName = state.currentProfileName,
            onDismiss = { viewModel.setProfileEditorOpen(false) },
            onSelectProfile = { viewModel.switchProfile(it) },
            onSelectFamousProfile = { viewModel.loadFamousProfile(it) },
            onSaveProfile = { name, y, m, d, h, min, city, country, lat, lon, tz ->
                viewModel.saveNewProfile(name, y, m, d, h, min, city, country, lat, lon, tz, true)
            }
        )

        // Daily Reminder Dialog
        ReminderDialog(
            isOpen = state.isReminderDialogOpen,
            isReminderEnabled = state.isReminderEnabled,
            reminderHour24 = state.reminderHour24,
            reminderMinute = state.reminderMinute,
            onDismiss = { viewModel.setReminderDialogOpen(false) },
            onSaveReminder = { enabled, h24, min ->
                viewModel.saveReminderSettings(enabled, h24, min)
            },
            onTriggerTestNotification = {
                viewModel.triggerTestNotification()
            }
        )

        // Multi-Language Dialog
        LanguageDialog(
            isOpen = state.isLanguageDialogOpen,
            currentLanguage = state.currentLanguage,
            onDismiss = { viewModel.setLanguageDialogOpen(false) },
            onSelectLanguage = { viewModel.setLanguage(it) }
        )
    }
}
