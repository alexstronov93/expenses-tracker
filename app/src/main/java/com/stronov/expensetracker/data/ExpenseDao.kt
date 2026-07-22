package com.stronov.expensetracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    /** Observe all expenses, newest first. */
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun observeAll(): Flow<List<Expense>>

    /** Observe the running total of all expenses in cents. */
    @Query("SELECT COALESCE(SUM(amountCents), 0) FROM expenses")
    fun observeTotalCents(): Flow<Long>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getById(id: Long): Expense?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}
