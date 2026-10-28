package com.example.astro.data

data class SampleProfile(
    val name: String,
    val description: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val cityName: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double
)

object FamousCharts {
    val SAMPLES = listOf(
        SampleProfile(
            name = "Swami Vivekananda",
            description = "Spiritual luminary: Sagittarius Lagna with Sun in 1st house, Moon in 10th house (Virgo/Hasta), exalted Saturn in 11th.",
            year = 1863,
            month = 1,
            day = 12,
            hour = 6,
            minute = 33,
            cityName = "Kolkata",
            country = "India",
            latitude = 22.5726,
            longitude = 88.3639,
            timezoneOffsetHours = 5.89
        ),
        SampleProfile(
            name = "Albert Einstein",
            description = "Theoretical physicist: Gemini Lagna with exalted Venus in 10th house, Mercury in Pisces, Saturn in 10th creating Malavya/Neechabhanga yoga.",
            year = 1879,
            month = 3,
            day = 14,
            hour = 11,
            minute = 30,
            cityName = "Ulm",
            country = "Germany",
            latitude = 48.4011,
            longitude = 9.9876,
            timezoneOffsetHours = 1.0
        ),
        SampleProfile(
            name = "Steve Jobs",
            description = "Visionary innovator: Leo Lagna with Mars in 1st house, Jupiter in 11th house, Saturn in 3rd house.",
            year = 1955,
            month = 2,
            day = 24,
            hour = 19,
            minute = 15,
            cityName = "San Francisco",
            country = "USA",
            latitude = 37.7749,
            longitude = -122.4194,
            timezoneOffsetHours = -8.0
        ),
        SampleProfile(
            name = "Dr. APJ Abdul Kalam",
            description = "Beloved scientist & President: Scorpio Lagna, exalted Mercury in Virgo in 11th house, Sun and Ketu in 11th.",
            year = 1931,
            month = 10,
            day = 15,
            hour = 1,
            minute = 15,
            cityName = "Rameswaram",
            country = "India",
            latitude = 9.2876,
            longitude = 79.3129,
            timezoneOffsetHours = 5.5
        )
    )
}
