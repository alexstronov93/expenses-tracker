package com.stronov.expensetracker.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Helpers for converting between user-entered amount strings and the integer
 * cent representation stored in the database.
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
}
