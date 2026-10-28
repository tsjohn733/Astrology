package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.astro.data.CityDatabase
import com.example.astro.data.FamousCharts
import com.example.astro.data.SampleProfile
import com.example.astro.model.CityLocation
import com.example.data.local.BirthProfileEntity
import com.example.ui.theme.*

@Composable
fun ProfileDialog(
    isOpen: Boolean,
    savedProfiles: List<BirthProfileEntity>,
    currentProfileName: String,
    onDismiss: () -> Unit,
    onSelectProfile: (BirthProfileEntity) -> Unit,
    onSelectFamousProfile: (SampleProfile) -> Unit,
    onSaveProfile: (
        name: String,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        cityName: String,
        country: String,
        lat: Double,
        lon: Double,
        tz: Double
    ) -> Unit
) {
    if (!isOpen) return

    var isAddingNew by remember { mutableStateOf(false) }

    // New profile form state
    var nameInput by remember { mutableStateOf("") }
    var yearInput by remember { mutableStateOf("1995") }
    var monthInput by remember { mutableStateOf("10") }
    var dayInput by remember { mutableStateOf("24") }

    // 12-Hour Time & AM/PM State
    var hour12Input by remember { mutableStateOf("02") }
    var minuteInput by remember { mutableStateOf("30") }
    var isPm by remember { mutableStateOf(true) } // true for PM, false for AM

    // City Search State
    var citySearchQuery by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<CityLocation>(CityDatabase.CITIES.first()) }
    var isCustomCoordinates by remember { mutableStateOf(false) }
    var customLatInput by remember { mutableStateOf("28.6139") }
    var customLonInput by remember { mutableStateOf("77.2090") }
    var customTzInput by remember { mutableStateOf("5.5") }

    val filteredCities = remember(citySearchQuery) {
        CityDatabase.searchCities(citySearchQuery)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CosmicSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0x33F5D061))),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAddingNew) "Create Birth Chart" else "Birth Chart Profiles",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StarlightGold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = CosmicTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (isAddingNew) {
                    // New Profile Creation Form
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            OutlinedTextField(
                                value = nameInput,
                                onValueChange = { nameInput = it },
                                label = { Text("Person Name / Title") },
                                placeholder = { Text("e.g., Arjun, Priya, Self") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("new_profile_name_input")
                            )
                        }

                        // Birth Date Row
                        item {
                            Text(
                                text = "Date of Birth",
                                style = MaterialTheme.typography.labelMedium,
                                color = SacredCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = dayInput,
                                    onValueChange = { dayInput = it },
                                    label = { Text("Day") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = monthInput,
                                    onValueChange = { monthInput = it },
                                    label = { Text("Month") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = yearInput,
                                    onValueChange = { yearInput = it },
                                    label = { Text("Year") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1.3f)
                                )
                            }
                        }

                        // Birth Time with 12-Hour format & AM/PM Selector
                        item {
                            Text(
                                text = "Time of Birth (12-Hour format with AM/PM)",
                                style = MaterialTheme.typography.labelMedium,
                                color = SacredCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = hour12Input,
                                    onValueChange = { hour12Input = it },
                                    label = { Text("Hour (1-12)") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("birth_hour_input")
                                )

                                Text(
                                    text = ":",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StarlightGold
                                )

                                OutlinedTextField(
                                    value = minuteInput,
                                    onValueChange = { minuteInput = it },
                                    label = { Text("Min (0-59)") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("birth_minute_input")
                                )

                                // AM / PM Segmented Toggle Buttons
                                Surface(
                                    color = CosmicSurfaceVariant,
                                    shape = RoundedCornerShape(12.dp),
                                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0x33F5D061))),
                                    modifier = Modifier.height(56.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // AM Button
                                        Surface(
                                            color = if (!isPm) StarlightGold else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .clickable { isPm = false }
                                                .padding(horizontal = 2.dp)
                                                .testTag("time_am_button")
                                        ) {
                                            Text(
                                                text = "AM",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = if (!isPm) Color(0xFF1E1500) else CosmicTextSecondary,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp)
                                            )
                                        }

                                        // PM Button
                                        Surface(
                                            color = if (isPm) StarlightGold else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .clickable { isPm = true }
                                                .padding(horizontal = 2.dp)
                                                .testTag("time_pm_button")
                                        ) {
                                            Text(
                                                text = "PM",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = if (isPm) Color(0xFF1E1500) else CosmicTextSecondary,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // City Search with Dynamic Suggestions
                        item {
                            Text(
                                text = "Birth Place (Start typing to search city)",
                                style = MaterialTheme.typography.labelMedium,
                                color = SacredCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = citySearchQuery,
                                onValueChange = { citySearchQuery = it },
                                placeholder = { Text("Search city (e.g. Mumbai, New York, London, Delhi)...") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = StarlightGold
                                    )
                                },
                                trailingIcon = {
                                    if (citySearchQuery.isNotEmpty()) {
                                        IconButton(onClick = { citySearchQuery = "" }) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear",
                                                tint = CosmicTextSecondary
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("city_search_input")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Selected City Confirmation Card
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0x33F5D061)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = StarlightGold,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${selectedCity.name}, ${selectedCity.country}",
                                                fontWeight = FontWeight.Bold,
                                                color = StarlightGold,
                                                fontSize = 13.sp
                                            )
                                        }
                                        Text(
                                            text = "Lat: ${String.format("%.4f", selectedCity.latitude)}, Lon: ${String.format("%.4f", selectedCity.longitude)} • Timezone: UTC ${if (selectedCity.timezoneOffsetHours >= 0) "+" else ""}${selectedCity.timezoneOffsetHours}h",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CosmicTextSecondary
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = StarlightGold
                                    )
                                }
                            }
                        }

                        // Suggested Cities List
                        item {
                            Text(
                                text = if (citySearchQuery.isNotBlank()) "Search Results (${filteredCities.size} found):" else "Popular Cities (or type above to filter 200+ cities):",
                                style = MaterialTheme.typography.labelSmall,
                                color = CosmicTextTertiary
                            )
                        }

                        items(filteredCities.take(15)) { city ->
                            val isChosen = city.name == selectedCity.name && city.country == selectedCity.country
                            Surface(
                                color = if (isChosen) Color(0x22F5D061) else CosmicSurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCity = city
                                        citySearchQuery = city.name
                                    }
                                    .testTag("suggested_city_${city.name.take(6).lowercase()}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${city.name}, ${city.country}",
                                            fontSize = 12.sp,
                                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isChosen) StarlightGold else CosmicTextPrimary
                                        )
                                        Text(
                                            text = "${String.format("%.2f", city.latitude)}°, ${String.format("%.2f", city.longitude)}°",
                                            fontSize = 10.sp,
                                            color = CosmicTextTertiary
                                        )
                                    }

                                    Surface(
                                        color = Color(0x3338BDF8),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "UTC ${if (city.timezoneOffsetHours >= 0) "+" else ""}${city.timezoneOffsetHours}h",
                                            fontSize = 10.sp,
                                            color = SacredCyan,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { isAddingNew = false }) {
                            Text("Cancel", color = CosmicTextSecondary)
                        }

                        Button(
                            onClick = {
                                val name = nameInput.ifBlank { "Personal Chart" }
                                val y = yearInput.toIntOrNull() ?: 1995
                                val m = monthInput.toIntOrNull() ?: 10
                                val d = dayInput.toIntOrNull() ?: 24
                                val raw12 = hour12Input.toIntOrNull() ?: 2
                                val min = minuteInput.toIntOrNull() ?: 30

                                // Convert 12-Hour format with AM/PM to 24-Hour format
                                val h24 = convert12To24(raw12, isPm)

                                onSaveProfile(
                                    name,
                                    y,
                                    m,
                                    d,
                                    h24,
                                    min,
                                    selectedCity.name,
                                    selectedCity.country,
                                    selectedCity.latitude,
                                    selectedCity.longitude,
                                    selectedCity.timezoneOffsetHours
                                )
                                isAddingNew = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StarlightGold,
                                contentColor = Color(0xFF1E1500)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("save_profile_button")
                        ) {
                            Text("Generate Chart", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Profile List View
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Button(
                                onClick = { isAddingNew = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0x33F5D061),
                                    contentColor = StarlightGold
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("add_new_profile_button")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Add Custom Birth Chart", fontWeight = FontWeight.Bold)
                            }
                        }

                        item {
                            Text(
                                text = "Saved Charts",
                                style = MaterialTheme.typography.labelMedium,
                                color = SacredCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(savedProfiles) { profile ->
                            val isActive = profile.name == currentProfileName
                            val formattedTime = format24To12(profile.hour, profile.minute)

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isActive) Color(0x22F5D061) else CosmicSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectProfile(profile)
                                        onDismiss()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = profile.name,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isActive) StarlightGold else CosmicTextPrimary
                                        )
                                        Text(
                                            text = "${profile.day}/${profile.month}/${profile.year} • $formattedTime • ${profile.cityName}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CosmicTextSecondary
                                        )
                                    }
                                    if (isActive) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Active",
                                            tint = StarlightGold
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Famous Historical Vedic Charts",
                                style = MaterialTheme.typography.labelMedium,
                                color = StarlightGold,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(FamousCharts.SAMPLES) { sample ->
                            val sampleFormattedTime = format24To12(sample.hour, sample.minute)
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectFamousProfile(sample)
                                        onDismiss()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = StarlightGold,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = sample.name,
                                                fontWeight = FontWeight.Bold,
                                                color = CosmicTextPrimary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${sample.day}/${sample.month}/${sample.year} • $sampleFormattedTime • ${sample.cityName}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SacredCyan
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = sample.description,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CosmicTextSecondary
                                        )
                                    }
                                    Text(
                                        text = "Load",
                                        color = SacredCyan,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
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

fun convert12To24(hour12: Int, isPm: Boolean): Int {
    val h = hour12.coerceIn(1, 12)
    return when {
        !isPm && h == 12 -> 0
        !isPm -> h
        isPm && h == 12 -> 12
        else -> h + 12
    }
}

fun format24To12(hour24: Int, minute: Int): String {
    val h = hour24.coerceIn(0, 23)
    val isPm = h >= 12
    val h12 = when {
        h == 0 -> 12
        h > 12 -> h - 12
        else -> h
    }
    val minStr = String.format("%02d", minute.coerceIn(0, 59))
    val amPmStr = if (isPm) "PM" else "AM"
    return "${String.format("%02d", h12)}:$minStr $amPmStr"
}
