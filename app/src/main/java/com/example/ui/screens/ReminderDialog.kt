package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.ui.theme.*

@Composable
fun ReminderDialog(
    isOpen: Boolean,
    isReminderEnabled: Boolean,
    reminderHour24: Int,
    reminderMinute: Int,
    onDismiss: () -> Unit,
    onSaveReminder: (enabled: Boolean, hour24: Int, minute: Int) -> Unit,
    onTriggerTestNotification: () -> Unit
) {
    if (!isOpen) return

    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(isReminderEnabled) }

    // Convert hour24 to hour12 + isPm
    val initialIsPm = reminderHour24 >= 12
    val initialH12 = when {
        reminderHour24 == 0 -> 12
        reminderHour24 > 12 -> reminderHour24 - 12
        else -> reminderHour24
    }

    var hour12Input by remember { mutableStateOf(String.format("%02d", initialH12)) }
    var minuteInput by remember { mutableStateOf(String.format("%02d", reminderMinute)) }
    var isPm by remember { mutableStateOf(initialIsPm) }
    var testFeedback by remember { mutableStateOf<String?>(null) }

    // Permission launcher for POST_NOTIFICATIONS
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onTriggerTestNotification()
            testFeedback = "Test notification sent! Check your notification tray."
        } else {
            testFeedback = "Notification permission denied. Please grant permission in settings."
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CosmicSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0x33F5D061))),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = StarlightGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Daily Transit Reminder",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = CosmicTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Receive a daily notification with your active Gochar transits, love/career forecasts, and the astrological tip of the day.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CosmicTextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Enable Switch Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Enable Daily Reminder",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = CosmicTextPrimary
                    )
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { isEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = StarlightGold,
                            checkedTrackColor = Color(0x66F5D061)
                        ),
                        modifier = Modifier.testTag("enable_reminder_switch")
                    )
                }

                if (isEnabled) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Reminder Time (12-Hour format)",
                        style = MaterialTheme.typography.labelMedium,
                        color = SacredCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = hour12Input,
                            onValueChange = { hour12Input = it },
                            label = { Text("Hour") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("reminder_hour_input")
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
                            label = { Text("Min") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("reminder_minute_input")
                        )

                        // AM/PM Segmented control
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
                                Surface(
                                    color = if (!isPm) StarlightGold else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .clickable { isPm = false }
                                        .padding(horizontal = 2.dp)
                                ) {
                                    Text(
                                        text = "AM",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (!isPm) Color(0xFF1E1500) else CosmicTextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
                                    )
                                }
                                Surface(
                                    color = if (isPm) StarlightGold else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .clickable { isPm = true }
                                        .padding(horizontal = 2.dp)
                                ) {
                                    Text(
                                        text = "PM",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isPm) Color(0xFF1E1500) else CosmicTextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Test notification button
                    OutlinedButton(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED
                                if (hasPermission) {
                                    onTriggerTestNotification()
                                    testFeedback = "Test notification sent! Check your notification tray."
                                } else {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            } else {
                                onTriggerTestNotification()
                                testFeedback = "Test notification sent! Check your notification tray."
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SacredCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("test_notification_button")
                    ) {
                        Text("Test Notification Now", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    if (testFeedback != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = testFeedback!!,
                            color = StarlightGold,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = CosmicTextSecondary)
                    }

                    Button(
                        onClick = {
                            val rawH12 = hour12Input.toIntOrNull() ?: 8
                            val min = minuteInput.toIntOrNull() ?: 0
                            val h24 = convert12To24(rawH12, isPm)
                            onSaveReminder(isEnabled, h24, min)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StarlightGold,
                            contentColor = Color(0xFF1E1500)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("save_reminder_button")
                    ) {
                        Text("Save Reminder", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
