package com.example.astro.data

import com.example.astro.model.CityLocation

object CityDatabase {
    val CITIES = listOf(
        // India - Major Metros & States
        CityLocation("New Delhi", "India", 28.6139, 77.2090, 5.5),
        CityLocation("Mumbai", "India", 19.0760, 72.8777, 5.5),
        CityLocation("Bengaluru (Bangalore)", "India", 12.9716, 77.5946, 5.5),
        CityLocation("Kolkata", "India", 22.5726, 88.3639, 5.5),
        CityLocation("Chennai", "India", 13.0827, 80.2707, 5.5),
        CityLocation("Hyderabad", "India", 17.3850, 78.4867, 5.5),
        CityLocation("Ahmedabad", "India", 23.0225, 72.5714, 5.5),
        CityLocation("Pune", "India", 18.5204, 73.8567, 5.5),
        CityLocation("Jaipur", "India", 26.9124, 75.7873, 5.5),
        CityLocation("Lucknow", "India", 26.8467, 80.9462, 5.5),
        CityLocation("Kanpur", "India", 26.4499, 80.3319, 5.5),
        CityLocation("Nagpur", "India", 21.1458, 79.0882, 5.5),
        CityLocation("Indore", "India", 22.7196, 75.8577, 5.5),
        CityLocation("Bhopal", "India", 23.2599, 77.4126, 5.5),
        CityLocation("Patna", "India", 25.5941, 85.1376, 5.5),
        CityLocation("Vadodara", "India", 22.3072, 73.1812, 5.5),
        CityLocation("Surat", "India", 21.1702, 72.8311, 5.5),
        CityLocation("Ludhiana", "India", 30.9010, 75.8573, 5.5),
        CityLocation("Agra", "India", 27.1767, 78.0081, 5.5),
        CityLocation("Nashik", "India", 19.9975, 73.7898, 5.5),
        CityLocation("Varanasi (Kashi)", "India", 25.3176, 82.9739, 5.5),
        CityLocation("Ayodhya", "India", 26.7922, 82.1998, 5.5),
        CityLocation("Prayagraj (Allahabad)", "India", 25.4358, 81.8463, 5.5),
        CityLocation("Haridwar", "India", 29.9457, 78.1642, 5.5),
        CityLocation("Rishikesh", "India", 30.0869, 78.2676, 5.5),
        CityLocation("Dehradun", "India", 30.3165, 78.0322, 5.5),
        CityLocation("Shimla", "India", 31.1048, 77.1734, 5.5),
        CityLocation("Chandigarh", "India", 30.7333, 76.7794, 5.5),
        CityLocation("Amritsar", "India", 31.6340, 74.8723, 5.5),
        CityLocation("Jammu", "India", 32.7266, 74.8570, 5.5),
        CityLocation("Srinagar", "India", 34.0837, 74.7973, 5.5),
        CityLocation("Leh", "India", 34.1526, 77.5771, 5.5),
        CityLocation("Kochi (Cochin)", "India", 9.9312, 76.2673, 5.5),
        CityLocation("Thiruvananthapuram", "India", 8.5241, 76.9366, 5.5),
        CityLocation("Coimbatore", "India", 11.0168, 76.9558, 5.5),
        CityLocation("Madurai", "India", 9.9252, 78.1198, 5.5),
        CityLocation("Visakhapatnam", "India", 17.6868, 83.2185, 5.5),
        CityLocation("Vijayawada", "India", 16.5062, 80.6480, 5.5),
        CityLocation("Bhubaneswar", "India", 20.2961, 85.8245, 5.5),
        CityLocation("Cuttack", "India", 20.4625, 85.8830, 5.5),
        CityLocation("Ranchi", "India", 23.3441, 85.3096, 5.5),
        CityLocation("Jamshedpur", "India", 22.8046, 86.2029, 5.5),
        CityLocation("Guwahati", "India", 26.1445, 91.7362, 5.5),
        CityLocation("Shillong", "India", 25.5788, 91.8933, 5.5),
        CityLocation("Raipur", "India", 21.2514, 81.6296, 5.5),
        CityLocation("Udaipur", "India", 24.5854, 73.7125, 5.5),
        CityLocation("Jodhpur", "India", 26.2389, 73.0243, 5.5),
        CityLocation("Ujjain", "India", 23.1765, 75.7885, 5.5),
        CityLocation("Gwalior", "India", 26.2183, 78.1828, 5.5),
        CityLocation("Jabalpur", "India", 23.1815, 79.9864, 5.5),
        CityLocation("Tirupati", "India", 13.6288, 79.4192, 5.5),
        CityLocation("Rameswaram", "India", 9.2876, 79.3129, 5.5),
        CityLocation("Kanyakumari", "India", 8.0883, 77.5385, 5.5),
        CityLocation("Puri", "India", 19.8135, 85.8312, 5.5),
        CityLocation("Mysuru (Mysore)", "India", 12.2958, 76.6394, 5.5),
        CityLocation("Mangaluru", "India", 12.9141, 74.8560, 5.5),
        CityLocation("Panaji (Goa)", "India", 15.4909, 73.8278, 5.5),

        // Nepal & South Asia
        CityLocation("Kathmandu", "Nepal", 27.7172, 85.3240, 5.75),
        CityLocation("Pokhara", "Nepal", 28.2096, 83.9856, 5.75),
        CityLocation("Colombo", "Sri Lanka", 6.9271, 79.8612, 5.5),
        CityLocation("Dhaka", "Bangladesh", 23.8103, 90.4125, 6.0),
        CityLocation("Thimphu", "Bhutan", 27.4728, 89.6393, 6.0),
        CityLocation("Islamabad", "Pakistan", 33.6844, 73.0479, 5.0),
        CityLocation("Lahore", "Pakistan", 31.5204, 74.3587, 5.0),
        CityLocation("Karachi", "Pakistan", 24.8607, 67.0011, 5.0),

        // Middle East
        CityLocation("Dubai", "UAE", 25.2048, 55.2708, 4.0),
        CityLocation("Abu Dhabi", "UAE", 24.4539, 54.3773, 4.0),
        CityLocation("Sharjah", "UAE", 25.3463, 55.4209, 4.0),
        CityLocation("Doha", "Qatar", 25.2854, 51.5310, 3.0),
        CityLocation("Riyadh", "Saudi Arabia", 24.7136, 46.6753, 3.0),
        CityLocation("Jeddah", "Saudi Arabia", 21.4858, 39.1925, 3.0),
        CityLocation("Muscat", "Oman", 23.5880, 58.3829, 4.0),
        CityLocation("Kuwait City", "Kuwait", 29.3759, 47.9774, 3.0),
        CityLocation("Manama", "Bahrain", 26.2285, 50.5860, 3.0),

        // Southeast & East Asia
        CityLocation("Singapore", "Singapore", 1.3521, 103.8198, 8.0),
        CityLocation("Kuala Lumpur", "Malaysia", 3.1390, 101.6869, 8.0),
        CityLocation("Bangkok", "Thailand", 13.7563, 100.5018, 7.0),
        CityLocation("Jakarta", "Indonesia", -6.2088, 106.8456, 7.0),
        CityLocation("Bali (Denpasar)", "Indonesia", -8.6705, 115.2126, 8.0),
        CityLocation("Tokyo", "Japan", 35.6762, 139.6503, 9.0),
        CityLocation("Osaka", "Japan", 34.6937, 135.5023, 9.0),
        CityLocation("Seoul", "South Korea", 37.5665, 126.9780, 9.0),
        CityLocation("Hong Kong", "Hong Kong", 22.3193, 114.1694, 8.0),
        CityLocation("Taipei", "Taiwan", 25.0330, 121.5654, 8.0),
        CityLocation("Beijing", "China", 39.9042, 116.4074, 8.0),
        CityLocation("Shanghai", "China", 31.2304, 121.4737, 8.0),

        // Americas - USA & Canada
        CityLocation("New York", "USA", 40.7128, -74.0060, -5.0),
        CityLocation("San Francisco", "USA", 37.7749, -122.4194, -8.0),
        CityLocation("Los Angeles", "USA", 34.0522, -118.2437, -8.0),
        CityLocation("San Jose", "USA", 37.3382, -121.8863, -8.0),
        CityLocation("Seattle", "USA", 47.6062, -122.3321, -8.0),
        CityLocation("Chicago", "USA", 41.8781, -87.6298, -6.0),
        CityLocation("Houston", "USA", 29.7604, -95.3698, -6.0),
        CityLocation("Dallas", "USA", 32.7767, -96.7970, -6.0),
        CityLocation("Austin", "USA", 30.2672, -97.7431, -6.0),
        CityLocation("Atlanta", "USA", 33.7490, -84.3880, -5.0),
        CityLocation("Boston", "USA", 42.3601, -71.0589, -5.0),
        CityLocation("Miami", "USA", 25.7617, -80.1918, -5.0),
        CityLocation("Washington D.C.", "USA", 38.9072, -77.0369, -5.0),
        CityLocation("Denver", "USA", 39.7392, -104.9903, -7.0),
        CityLocation("Phoenix", "USA", 33.4484, -112.0740, -7.0),
        CityLocation("San Diego", "USA", 32.7157, -117.1611, -8.0),
        CityLocation("Toronto", "Canada", 43.6532, -79.3832, -5.0),
        CityLocation("Vancouver", "Canada", 49.2827, -123.1207, -8.0),
        CityLocation("Montreal", "Canada", 45.5017, -73.5673, -5.0),
        CityLocation("Calgary", "Canada", 51.0447, -114.0719, -7.0),
        CityLocation("Ottawa", "Canada", 45.4215, -75.6972, -5.0),

        // Europe
        CityLocation("London", "UK", 51.5074, -0.1278, 0.0),
        CityLocation("Manchester", "UK", 53.4808, -2.2426, 0.0),
        CityLocation("Birmingham", "UK", 52.4862, -1.8904, 0.0),
        CityLocation("Edinburgh", "UK", 55.9533, -3.1883, 0.0),
        CityLocation("Paris", "France", 48.8566, 2.3522, 1.0),
        CityLocation("Berlin", "Germany", 52.5200, 13.4050, 1.0),
        CityLocation("Munich", "Germany", 48.1351, 11.5820, 1.0),
        CityLocation("Frankfurt", "Germany", 50.1109, 8.6821, 1.0),
        CityLocation("Amsterdam", "Netherlands", 52.3676, 4.9041, 1.0),
        CityLocation("Brussels", "Belgium", 50.8503, 4.3517, 1.0),
        CityLocation("Dublin", "Ireland", 53.3498, -6.2603, 0.0),
        CityLocation("Rome", "Italy", 41.9028, 12.4964, 1.0),
        CityLocation("Milan", "Italy", 45.4642, 9.1900, 1.0),
        CityLocation("Madrid", "Spain", 40.4168, -3.7038, 1.0),
        CityLocation("Barcelona", "Spain", 41.3879, 2.1699, 1.0),
        CityLocation("Vienna", "Austria", 48.2082, 16.3738, 1.0),
        CityLocation("Zurich", "Switzerland", 47.3769, 8.5417, 1.0),
        CityLocation("Geneva", "Switzerland", 46.2044, 6.1432, 1.0),
        CityLocation("Stockholm", "Sweden", 59.3293, 18.0686, 1.0),
        CityLocation("Oslo", "Norway", 59.9139, 10.7522, 1.0),
        CityLocation("Copenhagen", "Denmark", 55.6761, 12.5683, 1.0),
        CityLocation("Helsinki", "Finland", 60.1699, 24.9384, 2.0),
        CityLocation("Athens", "Greece", 37.9838, 23.7275, 2.0),
        CityLocation("Warsaw", "Poland", 52.2297, 21.0122, 1.0),

        // Australia & New Zealand
        CityLocation("Sydney", "Australia", -33.8688, 151.2093, 10.0),
        CityLocation("Melbourne", "Australia", -37.8136, 144.9631, 10.0),
        CityLocation("Brisbane", "Australia", -27.4698, 153.0251, 10.0),
        CityLocation("Perth", "Australia", -31.9505, 115.8605, 8.0),
        CityLocation("Adelaide", "Australia", -34.9285, 138.6007, 9.5),
        CityLocation("Auckland", "New Zealand", -36.8485, 174.7633, 12.0),
        CityLocation("Wellington", "New Zealand", -41.2865, 174.7762, 12.0),

        // Africa & Others
        CityLocation("Johannesburg", "South Africa", -26.2041, 28.0473, 2.0),
        CityLocation("Cape Town", "South Africa", -33.9249, 18.4241, 2.0),
        CityLocation("Nairobi", "Kenya", -1.2921, 36.8219, 3.0),
        CityLocation("Cairo", "Egypt", 30.0444, 31.2357, 2.0),
        CityLocation("Port Louis", "Mauritius", -20.1609, 57.5012, 4.0)
    )

    fun searchCities(query: String): List<CityLocation> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return CITIES.take(20)

        // Prioritize exact prefix matches on city name, then substring matches
        val prefixMatches = CITIES.filter {
            it.name.startsWith(trimmed, ignoreCase = true)
        }
        val otherMatches = CITIES.filter {
            !it.name.startsWith(trimmed, ignoreCase = true) &&
                    (it.name.contains(trimmed, ignoreCase = true) || it.country.contains(trimmed, ignoreCase = true))
        }
        return (prefixMatches + otherMatches)
    }

    fun findCity(name: String): CityLocation? {
        return CITIES.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}
