package com.stronov.expensetracker

import android.app.Application
import com.stronov.expensetracker.data.ExpenseDatabase
import com.stronov.expensetracker.data.ExpenseRepository

/**
 * Application-scoped container for shared dependencies.
 *
 * This is a lightweight, hand-rolled service locator. As the app grows you may
 * want to replace it with Hilt/Dagger, but for a small app this keeps things simple.
 */
class ExpenseApplication : Application() {

    // Database and repository are created lazily and live for the app's lifetime.
    val database: ExpenseDatabase by lazy { ExpenseDatabase.getInstance(this) }
    val repository: ExpenseRepository by lazy { ExpenseRepository(database.expenseDao()) }
}
