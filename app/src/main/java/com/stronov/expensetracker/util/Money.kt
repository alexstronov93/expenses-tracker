package com.stronov.expensetracker.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Helpers for converting between user-entered amount strings and the integer
 * cent representation stored in the database, plus display formatting.
 */
object Money {

    /** Format a cent amount as localized currency, e.g. 1234 -> "$12.34". */
    fun format(cents: Long, locale: Locale = Locale.getDefault()): String {
        val formatter = NumberFormat.getCurrencyInstance(locale)
        return formatter.format(cents / 100.0)
    }

    /**
     * Parse a plain user string like "12.34" or "12" into cents.
     * Returns null when the input is not a valid, non-negative amount.
     */
    fun parseToCents(input: String): Long? {
        val normalized = input.trim().replace(',', '.')
        if (normalized.isEmpty()) return null
        val value = normalized.toDoubleOrNull() ?: return null
        if (value < 0) return null
        return Math.round(value * 100)
    }

    /** Convert cents back to a plain editable string, e.g. 1234 -> "12.34". */
    fun centsToPlainString(cents: Long): String =
        String.format(Locale.US, "%.2f", cents / 100.0)

    // Non-breaking space (U+00A0): keeps thousands groups and the "zł" suffix
    // from wrapping onto separate lines.
    private const val NBSP = '\u00A0'

    /**
     * Format PLN (Polish złoty) the local way: space-grouped thousands, "zł"
     * suffix, e.g. 124000 -> "1 240 zł". Decimals are shown only when non-zero
     * (1 240,50 zł). Whole-złoty amounts read cleanly, matching the design.
     */
    fun formatPln(cents: Long): String {
        val sign = if (cents < 0) "-" else ""
        val abs = kotlin.math.abs(cents)
        val whole = abs / 100
        val frac = (abs % 100).toInt()
        val grouped = groupThousands(whole)
        val amount = if (frac == 0) grouped else "$grouped,${frac.toString().padStart(2, '0')}"
        return "$sign$amount${NBSP}zł"
    }

    private fun groupThousands(value: Long): String {
        val digits = value.toString()
        val sb = StringBuilder()
        val firstGroup = digits.length % 3
        for (i in digits.indices) {
            if (i != 0 && (i - firstGroup) % 3 == 0) sb.append(NBSP)
            sb.append(digits[i])
        }
        return sb.toString()
    }
}
