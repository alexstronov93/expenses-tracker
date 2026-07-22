package com.stronov.expensetracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.stronov.expensetracker.ExpenseApplication
import com.stronov.expensetracker.data.Category
import com.stronov.expensetracker.data.Expense
import com.stronov.expensetracker.data.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * UI state and actions for the expense screens.
 */
class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val expenses: StateFlow<List<Expense>> = repository.expenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalCents: StateFlow<Long> = repository.totalCents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)

    fun addExpense(title: String, amountCents: Long, category: Category, note: String) {
        viewModelScope.launch {
            repository.add(
                Expense(
                    title = title.trim(),
                    amountCents = amountCents,
                    category = category,
                    note = note.trim()
                )
            )
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch { repository.update(expense) }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { repository.delete(expense) }
    }

    suspend fun getExpense(id: Long): Expense? = repository.getById(id)

    companion object {
        /** Factory that pulls the repository from [ExpenseApplication]. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as ExpenseApplication
                ExpenseViewModel(app.repository)
            }
        }
    }
}
