package com.stronov.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single expense record.
 *
 * @param amountCents the amount stored as an integer number of cents to avoid
 *        floating-point rounding issues with money.
 * @param date epoch milliseconds of when the expense occurred.
 */
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amountCents: Long,
    val category: Category,
    val note: String = "",
    val date: Long = System.currentTimeMillis()
)
