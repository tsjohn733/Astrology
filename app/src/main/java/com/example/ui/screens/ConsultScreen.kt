package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ConsultScreen(
    question: String,
    answer: String,
    isLoading: Boolean,
    error: String?,
    onQuestionChange: (String) -> Unit,
    onAskQuestion: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val quickPrompts = listOf(
        "Why do I experience recurring career hurdles?",
        "Explain my Navamsha (D9) and relationships.",
        "What is the life lesson behind my Saturn placement?",
        "Which daily habits best harmonize my birth chart?",
        "How do current transits affect my mental clarity?"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Intro Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0x33F5D061))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = StarlightGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Vedic Astrologer Consultation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StarlightGold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ask any specific question about your birth chart, planetary aspects (Drishti), divisional charts (Vargas), or transits. Receive personalized guidance with root cause explanations and practical remedies.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Suggested Prompt Chips
        item {
            Text(
                text = "Suggested Questions",
                style = MaterialTheme.typography.labelMedium,
                color = SacredCyan,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(quickPrompts) { prompt ->
                    Surface(
                        color = CosmicSurface,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .clickable(enabled = !isLoading) {
                                onQuestionChange(prompt)
                                onAskQuestion(prompt)
                            }
                            .testTag("prompt_chip_${prompt.take(10).replace(" ", "_").lowercase()}")
                    ) {
                        Text(
                            text = prompt,
                            fontSize = 12.sp,
                            color = CosmicTextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Question Input Box
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = question,
                        onValueChange = onQuestionChange,
                        placeholder = {
                            Text(
                                "e.g., How does my Mercury placement influence my decision-making?",
                                fontSize = 13.sp,
                                color = CosmicTextTertiary
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StarlightGold,
                            unfocusedBorderColor = Color(0xFF334155),
                            cursorColor = StarlightGold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp)
                            .testTag("astrology_question_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { onAskQuestion(null) },
                            enabled = question.isNotBlank() && !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StarlightGold,
                                contentColor = Color(0xFF1E1500)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("ask_astrologer_submit_button")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color(0xFF1E1500),
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Consulting...")
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Ask Astrologer", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Error message if any
        if (error != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF361010)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Error: $error",
                        color = Color(0xFFFCA5A5),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // Astrological Guidance Answer Output
        if (answer.isNotBlank() || isLoading) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0x3338BDF8))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("consultation_answer_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = SacredCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Astrologer's Personalized Counsel",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SacredCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = answer.ifEmpty { "Contemplating your chart and analyzing planetary aspects..." },
                            style = MaterialTheme.typography.bodyMedium,
                            color = CosmicTextPrimary,
                            lineHeight = 22.sp
                        )

                        if (isLoading) {
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                color = SacredCyan,
                                trackColor = CosmicSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
