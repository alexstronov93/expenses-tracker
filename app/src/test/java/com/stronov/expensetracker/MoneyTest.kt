package com.stronov.expensetracker

import com.stronov.expensetracker.util.Money
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyTest {

    @Test
    fun parsesPlainDecimal() {
        assertEquals(1234L, Money.parseToCents("12.34"))
    }

    @Test
    fun parsesInteger() {
        assertEquals(1200L, Money.parseToCents("12"))
    }

    @Test
    fun parsesCommaDecimalSeparator() {
        assertEquals(1234L, Money.parseToCents("12,34"))
    }

    @Test
    fun rejectsNegative() {
        assertNull(Money.parseToCents("-5"))
    }

    @Test
    fun rejectsGarbage() {
        assertNull(Money.parseToCents("abc"))
        assertNull(Money.parseToCents(""))
    }

    @Test
    fun roundTripThroughPlainString() {
        val cents = 9999L
        assertEquals(cents, Money.parseToCents(Money.centsToPlainString(cents)))
    }

    // Non-breaking space used by the PLN formatter.
    private val nb = '\u00A0'

    @Test
    fun formatsWholeZlotyWithGrouping() {
        assertEquals("1${nb}240${nb}zł", Money.formatPln(124_000))
        assertEquals("2${nb}710${nb}zł", Money.formatPln(271_000))
    }

    @Test
    fun formatsSmallAmounts() {
        assertEquals("66${nb}zł", Money.formatPln(6_600))
        assertEquals("0${nb}zł", Money.formatPln(0))
    }

    @Test
    fun showsDecimalsOnlyWhenNonZero() {
        assertEquals("12,50${nb}zł", Money.formatPln(1_250))
        assertEquals("12${nb}zł", Money.formatPln(1_200))
    }
}
