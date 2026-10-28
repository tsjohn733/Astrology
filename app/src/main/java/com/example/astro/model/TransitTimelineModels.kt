package com.example.astro.model

data class TransitTimelineItem(
    val planet: Planet,
    val currentSign: ZodiacSign,
    val currentDegree: Double,
    val progressPercent: Float, // 0.0 to 1.0
    val totalStayInSign: String,
    val nextSign: ZodiacSign,
    val daysToNextIngress: Int,
    val approximateIngressDate: String,
    val upcomingHouseActivated: Int,
    val chartImpactTitle: String,
    val chartImpactDescription: String,
    val preparationHabits: List<String>
)
