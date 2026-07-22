package com.stronov.expensetracker

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stronov.expensetracker.data.Category
import com.stronov.expensetracker.data.Expense
import com.stronov.expensetracker.data.ExpenseDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExpenseDaoTest {

    private lateinit var db: ExpenseDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ExpenseDatabase::class.java
        ).build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndTotal() = runBlocking {
        val dao = db.expenseDao()
        dao.insert(Expense(title = "Coffee", amountCents = 350, category = Category.FOOD))
        dao.insert(Expense(title = "Bus", amountCents = 150, category = Category.TRANSPORT))

        val total = dao.observeTotalCents().first()
        assertEquals(500L, total)

        val all = dao.observeAll().first()
        assertEquals(2, all.size)
    }
}
