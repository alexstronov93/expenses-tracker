package com.stronov.expensetracker.data

import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for expense data. Wraps the DAO so the rest of the app
 * never talks to Room directly.
 */
class ExpenseRepository(private val dao: ExpenseDao) {

    val expenses: Flow<List<Expense>> = dao.observeAll()
    val totalCents: Flow<Long> = dao.observeTotalCents()

    suspend fun getById(id: Long): Expense? = dao.getById(id)

    suspend fun add(expense: Expense): Long = dao.insert(expense)

    suspend fun update(expense: Expense) = dao.update(expense)

    suspend fun delete(expense: Expense) = dao.delete(expense)
}
