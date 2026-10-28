package com.example.ui.components

import kotlin.math.abs
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.astro.model.ChartData
import com.example.astro.model.Planet
import com.example.astro.model.PlanetPosition
import com.example.astro.model.ZodiacSign

enum class ChartStyle {
    NORTH_INDIAN,
    SOUTH_INDIAN
}

@Composable
fun KundaliCanvas(
    chartData: ChartData,
    chartStyle: ChartStyle,
    selectedHouse: Int?,
    onHouseSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F1522))
            .pointerInput(chartStyle, chartData) {
                detectTapGestures { offset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    val tappedHouse = if (chartStyle == ChartStyle.NORTH_INDIAN) {
                        detectNorthIndianHouse(offset, w, h)
                    } else {
                        detectSouthIndianHouse(offset, w, h, chartData.ascendantSign)
                    }
                    if (tappedHouse in 1..12) {
                        onHouseSelected(tappedHouse)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            if (chartStyle == ChartStyle.NORTH_INDIAN) {
                drawNorthIndianChart(
                    scope = this,
                    w = w,
                    h = h,
                    chartData = chartData,
                    selectedHouse = selectedHouse,
                    textMeasurer = textMeasurer
                )
            } else {
                drawSouthIndianChart(
                    scope = this,
                    w = w,
                    h = h,
                    chartData = chartData,
                    selectedHouse = selectedHouse,
                    textMeasurer = textMeasurer
                )
            }
        }
    }
}

private fun drawNorthIndianChart(
    scope: DrawScope,
    w: Float,
    h: Float,
    chartData: ChartData,
    selectedHouse: Int?,
    textMeasurer: TextMeasurer
) {
    val goldColor = Color(0xFFF5D061)
    val dimGold = Color(0x66F5D061)
    val accentCyan = Color(0xFF38BDF8)
    val textWhite = Color(0xFFF1F5F9)
    val retroRed = Color(0xFFF87171)
    val combustOrange = Color(0xFFFB923C)
    val highlightFill = Color(0x33F5D061)

    val strokeWidth = 2.5f

    // Outer border
    scope.drawRect(
        color = goldColor,
        topLeft = Offset(4f, 4f),
        size = Size(w - 8f, h - 8f),
        style = Stroke(strokeWidth)
    )

    // Two big diagonal lines
    scope.drawLine(goldColor, Offset(4f, 4f), Offset(w - 4f, h - 4f), strokeWidth)
    scope.drawLine(goldColor, Offset(w - 4f, 4f), Offset(4f, h - 4f), strokeWidth)

    // Inner diamond connecting midpoints of 4 outer edges
    val diamondPath = Path().apply {
        moveTo(w / 2f, 4f)
        lineTo(w - 4f, h / 2f)
        lineTo(w / 2f, h - 4f)
        lineTo(4f, h / 2f)
        close()
    }
    scope.drawPath(diamondPath, color = goldColor, style = Stroke(strokeWidth))

    // Optional highlight for selected house
    if (selectedHouse != null && selectedHouse in 1..12) {
        val poly = getNorthIndianHousePolygon(selectedHouse, w, h)
        scope.drawPath(poly, color = highlightFill)
    }

    // Coordinates for center of each of the 12 houses to place sign number & planets
    val houseCenters = listOf(
        Offset(w / 2f, h * 0.28f),        // House 1 (Top diamond)
        Offset(w * 0.25f, h * 0.12f),     // House 2 (Top-left triangle)
        Offset(w * 0.12f, h * 0.25f),     // House 3 (Upper-left triangle)
        Offset(w * 0.28f, h / 2f),        // House 4 (Left diamond)
        Offset(w * 0.12f, h * 0.75f),     // House 5 (Lower-left triangle)
        Offset(w * 0.25f, h * 0.88f),     // House 6 (Bottom-left triangle)
        Offset(w / 2f, h * 0.72f),        // House 7 (Bottom diamond)
        Offset(w * 0.75f, h * 0.88f),     // House 8 (Bottom-right triangle)
        Offset(w * 0.88f, h * 0.75f),     // House 9 (Lower-right triangle)
        Offset(w * 0.72f, h / 2f),        // House 10 (Right diamond)
        Offset(w * 0.88f, h * 0.25f),     // House 11 (Upper-right triangle)
        Offset(w * 0.75f, h * 0.12f)      // House 12 (Top-right triangle)
    )

    // Draw house numbers (Rashi index: 1-12) and planets for each house
    val ascSignNum = chartData.ascendantSign.number

    for (hIndex in 1..12) {
        val rashiNumber = ((ascSignNum + hIndex - 2) % 12) + 1
        val center = houseCenters[hIndex - 1]

        // Sign number tag
        val rashiText = "$rashiNumber"
        val rashiMeasured = textMeasurer.measure(
            rashiText,
            style = TextStyle(
                color = if (hIndex == selectedHouse) accentCyan else dimGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        )
        scope.drawText(
            rashiMeasured,
            topLeft = Offset(center.x - rashiMeasured.size.width / 2f, center.y - 28f)
        )

        // Planets in this house
        val planetsInHouse = chartData.getPlanetsInHouse(hIndex)
        if (planetsInHouse.isNotEmpty()) {
            val planetLabel = planetsInHouse.joinToString(" ") { p ->
                val mod = if (p.isRetrograde) "®" else if (p.isCombust) "©" else ""
                "${p.planet.symbol}$mod"
            }
            val planetMeasured = textMeasurer.measure(
                planetLabel,
                style = TextStyle(
                    color = textWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            scope.drawText(
                planetMeasured,
                topLeft = Offset(center.x - planetMeasured.size.width / 2f, center.y - 4f)
            )
        }

        // Mark House 1 with Lagna / Asc badge
        if (hIndex == 1) {
            val lagnaMeasured = textMeasurer.measure(
                "Lagna",
                style = TextStyle(color = accentCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            )
            scope.drawText(
                lagnaMeasured,
                topLeft = Offset(center.x - lagnaMeasured.size.width / 2f, center.y + 16f)
            )
        }
    }
}

private fun drawSouthIndianChart(
    scope: DrawScope,
    w: Float,
    h: Float,
    chartData: ChartData,
    selectedHouse: Int?,
    textMeasurer: TextMeasurer
) {
    val goldColor = Color(0xFFF5D061)
    val dimGold = Color(0x66F5D061)
    val accentCyan = Color(0xFF38BDF8)
    val textWhite = Color(0xFFF1F5F9)

    val cw = w / 4f
    val ch = h / 4f
    val strokeWidth = 2.5f

    // Outer grid (12 boxes around periphery)
    scope.drawRect(
        color = goldColor,
        topLeft = Offset(4f, 4f),
        size = Size(w - 8f, h - 8f),
        style = Stroke(strokeWidth)
    )

    // Internal grid lines
    // Vertical lines
    scope.drawLine(goldColor, Offset(cw, 0f), Offset(cw, h), strokeWidth)
    scope.drawLine(goldColor, Offset(cw * 2f, 0f), Offset(cw * 2f, ch), strokeWidth)
    scope.drawLine(goldColor, Offset(cw * 2f, ch * 3f), Offset(cw * 2f, h), strokeWidth)
    scope.drawLine(goldColor, Offset(cw * 3f, 0f), Offset(cw * 3f, h), strokeWidth)

    // Horizontal lines
    scope.drawLine(goldColor, Offset(0f, ch), Offset(w, ch), strokeWidth)
    scope.drawLine(goldColor, Offset(0f, ch * 2f), Offset(cw, ch * 2f), strokeWidth)
    scope.drawLine(goldColor, Offset(cw * 3f, ch * 2f), Offset(w, ch * 2f), strokeWidth)
    scope.drawLine(goldColor, Offset(0f, ch * 3f), Offset(w, ch * 3f), strokeWidth)

    // 12 Sign Boxes fixed in South Indian tradition:
    // (col, row) mapping for sign 1 (Aries) to 12 (Pisces):
    val signGrid = mapOf(
        ZodiacSign.PISCES to Pair(0, 0),
        ZodiacSign.ARIES to Pair(1, 0),
        ZodiacSign.TAURUS to Pair(2, 0),
        ZodiacSign.GEMINI to Pair(3, 0),
        ZodiacSign.CANCER to Pair(3, 1),
        ZodiacSign.LEO to Pair(3, 2),
        ZodiacSign.VIRGO to Pair(3, 3),
        ZodiacSign.LIBRA to Pair(2, 3),
        ZodiacSign.SCORPIO to Pair(1, 3),
        ZodiacSign.SAGITTARIUS to Pair(0, 3),
        ZodiacSign.CAPRICORN to Pair(0, 2),
        ZodiacSign.AQUARIUS to Pair(0, 1)
    )

    val ascSign = chartData.ascendantSign

    for ((sign, coords) in signGrid) {
        val (col, row) = coords
        val boxX = col * cw
        val boxY = row * ch

        // Calculate house number for this sign:
        val house = ((sign.number - ascSign.number + 12) % 12) + 1

        if (house == selectedHouse) {
            scope.drawRect(
                color = Color(0x33F5D061),
                topLeft = Offset(boxX + 2f, boxY + 2f),
                size = Size(cw - 4f, ch - 4f)
            )
        }

        // Draw Sign name abbreviation
        val signMeasured = textMeasurer.measure(
            sign.sanskritName.take(3),
            style = TextStyle(
                color = if (house == selectedHouse) accentCyan else dimGold,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        )
        scope.drawText(signMeasured, topLeft = Offset(boxX + 6f, boxY + 4f))

        // Draw Lagna marker if Ascendant is in this sign
        if (sign == ascSign) {
            // Draw double diagonal line in the top-left of this box
            scope.drawLine(
                accentCyan,
                Offset(boxX, boxY + 16f),
                Offset(boxX + 16f, boxY),
                strokeWidth = 2f
            )
            val ascMeasured = textMeasurer.measure(
                "ASC",
                style = TextStyle(color = accentCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            )
            scope.drawText(ascMeasured, topLeft = Offset(boxX + cw - 26f, boxY + 4f))
        }

        // Draw House Number (H1, H2...)
        val houseMeasured = textMeasurer.measure(
            "H$house",
            style = TextStyle(color = Color(0x99A0AEC0), fontSize = 9.sp)
        )
        scope.drawText(houseMeasured, topLeft = Offset(boxX + cw - 22f, boxY + ch - 16f))

        // Planets in this sign
        val planetsInSign = chartData.planets.filter { it.sign == sign }
        if (planetsInSign.isNotEmpty()) {
            val label = planetsInSign.joinToString(" ") { p ->
                val mod = if (p.isRetrograde) "®" else if (p.isCombust) "©" else ""
                "${p.planet.symbol}$mod"
            }
            val pMeasured = textMeasurer.measure(
                label,
                style = TextStyle(
                    color = textWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            scope.drawText(
                pMeasured,
                topLeft = Offset(boxX + 8f, boxY + ch / 2f - 8f)
            )
        }
    }

    // Center title
    val centerTitle = textMeasurer.measure(
        "South Indian\nKundali",
        style = TextStyle(
            color = dimGold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    )
    scope.drawText(
        centerTitle,
        topLeft = Offset(w / 2f - centerTitle.size.width / 2f, h / 2f - centerTitle.size.height / 2f)
    )
}

private fun detectNorthIndianHouse(offset: Offset, w: Float, h: Float): Int {
    val x = offset.x / w
    val y = offset.y / h

    // Approximate polygonal regions:
    return when {
        // House 1: Top diamond (0.25..0.75 x, 0.0..0.5 y)
        abs(x - 0.5f) + y < 0.5f -> 1
        // House 7: Bottom diamond
        abs(x - 0.5f) + (1.0f - y) < 0.5f -> 7
        // House 4: Left diamond
        abs(y - 0.5f) + x < 0.5f -> 4
        // House 10: Right diamond
        abs(y - 0.5f) + (1.0f - x) < 0.5f -> 10
        // Corner triangles:
        x < 0.5f && y < 0.5f -> if (x > y) 2 else 3
        x < 0.5f && y >= 0.5f -> if (x < (1.0f - y)) 5 else 6
        x >= 0.5f && y >= 0.5f -> if ((1.0f - x) < (1.0f - y)) 9 else 8
        else -> if ((1.0f - x) > y) 12 else 11
    }
}

private fun detectSouthIndianHouse(offset: Offset, w: Float, h: Float, ascSign: ZodiacSign): Int {
    val col = (offset.x / (w / 4f)).toInt().coerceIn(0, 3)
    val row = (offset.y / (h / 4f)).toInt().coerceIn(0, 3)

    val sign = when {
        row == 0 && col == 0 -> ZodiacSign.PISCES
        row == 0 && col == 1 -> ZodiacSign.ARIES
        row == 0 && col == 2 -> ZodiacSign.TAURUS
        row == 0 && col == 3 -> ZodiacSign.GEMINI
        row == 1 && col == 3 -> ZodiacSign.CANCER
        row == 2 && col == 3 -> ZodiacSign.LEO
        row == 3 && col == 3 -> ZodiacSign.VIRGO
        row == 3 && col == 2 -> ZodiacSign.LIBRA
        row == 3 && col == 1 -> ZodiacSign.SCORPIO
        row == 3 && col == 0 -> ZodiacSign.SAGITTARIUS
        row == 2 && col == 0 -> ZodiacSign.CAPRICORN
        row == 1 && col == 0 -> ZodiacSign.AQUARIUS
        else -> null
    } ?: return 1

    return ((sign.number - ascSign.number + 12) % 12) + 1
}

private fun getNorthIndianHousePolygon(house: Int, w: Float, h: Float): Path {
    val p = Path()
    when (house) {
        1 -> {
            p.moveTo(w / 2f, 4f)
            p.lineTo(w * 0.75f, h * 0.25f)
            p.lineTo(w / 2f, h / 2f)
            p.lineTo(w * 0.25f, h * 0.25f)
            p.close()
        }
        4 -> {
            p.moveTo(w * 0.25f, h * 0.25f)
            p.lineTo(w / 2f, h / 2f)
            p.lineTo(w * 0.25f, h * 0.75f)
            p.lineTo(4f, h / 2f)
            p.close()
        }
        7 -> {
            p.moveTo(w / 2f, h / 2f)
            p.lineTo(w * 0.75f, h * 0.75f)
            p.lineTo(w / 2f, h - 4f)
            p.lineTo(w * 0.25f, h * 0.75f)
            p.close()
        }
        10 -> {
            p.moveTo(w * 0.75f, h * 0.25f)
            p.lineTo(w - 4f, h / 2f)
            p.lineTo(w * 0.75f, h * 0.75f)
            p.lineTo(w / 2f, h / 2f)
            p.close()
        }
        2 -> {
            p.moveTo(4f, 4f)
            p.lineTo(w / 2f, 4f)
            p.lineTo(w * 0.25f, h * 0.25f)
            p.close()
        }
        3 -> {
            p.moveTo(4f, 4f)
            p.lineTo(w * 0.25f, h * 0.25f)
            p.lineTo(4f, h / 2f)
            p.close()
        }
        5 -> {
            p.moveTo(4f, h / 2f)
            p.lineTo(w * 0.25f, h * 0.75f)
            p.lineTo(4f, h - 4f)
            p.close()
        }
        6 -> {
            p.moveTo(4f, h - 4f)
            p.lineTo(w * 0.25f, h * 0.75f)
            p.lineTo(w / 2f, h - 4f)
            p.close()
        }
        8 -> {
            p.moveTo(w / 2f, h - 4f)
            p.lineTo(w * 0.75f, h * 0.75f)
            p.lineTo(w - 4f, h - 4f)
            p.close()
        }
        9 -> {
            p.moveTo(w - 4f, h - 4f)
            p.lineTo(w * 0.75f, h * 0.75f)
            p.lineTo(w - 4f, h / 2f)
            p.close()
        }
        11 -> {
            p.moveTo(w - 4f, h / 2f)
            p.lineTo(w * 0.75f, h * 0.25f)
            p.lineTo(w - 4f, 4f)
            p.close()
        }
        12 -> {
            p.moveTo(w - 4f, 4f)
            p.lineTo(w * 0.75f, h * 0.25f)
            p.lineTo(w / 2f, 4f)
            p.close()
        }
    }
    return p
}
