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
}
