package com.stronov.expensetracker.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Prototype state for the Duetly demo.
 *
 * All money is integer grosze (PLN cents). The derived figures follow the rules
 * from the information architecture:
 *
 *   held           = sum of UNPAID bills
 *   spent          = discretionary spend + sum of PAID bills
 *   safe to spend  = planned - spent - held
 *
 * Because a bill's amount simply moves from `held` into `spent` when it is
 * marked paid, **safe to spend does not change** — which is what the Bills
 * screen tells the user.
 */

enum class AccountId { OWN, SHARED }

data class Account(
    val id: AccountId,
    val name: String,
    val maskedTail: String,
    val plannedCents: Long,
    /** Non-bill spending charged to this account. */
    val discretionarySpentCents: Long,
)

data class Bill(
    val id: String,
    val name: String,
    val iconKey: String,
    val amountCents: Long,
    val account: AccountId,
    val paid: Boolean,
    /** Day of month the bill is due. */
    val dueDay: Int,
    /** True when no explicit due date was set and the app defaulted it. */
    val dueDateIsDefaulted: Boolean = false,
    /** Day of month it was paid, when [paid]. */
    val paidDay: Int? = null,
)

data class Category(
    val name: String,
    val iconKey: String,
    val spentCents: Long,
    val limitCents: Long,
)

/** Demo states the prototype can be switched between. */
enum class DemoState { DEFAULT, NO_BILLS_DUE, PARTNER_NOT_JOINED }

class DuetlyViewModel : ViewModel() {

    var demoState by mutableStateOf(DemoState.DEFAULT)
        private set

    /** null = follow the system setting. */
    var darkOverride by mutableStateOf<Boolean?>(null)
        private set

    var bills by mutableStateOf(InitialBills)
        private set

    /** Set when navigating from Home's alert, so Bills can scope to what's imminent. */
    var billsScopedToImminent by mutableStateOf(false)
        private set

    val monthLabel = "July 2026"
    val daysLeft = 12
    val today = 18

    val accounts = listOf(
        Account(AccountId.OWN, "Own card", "1234", plannedCents = 160_000, discretionarySpentCents = 93_000),
        Account(AccountId.SHARED, "Shared account", "5678", plannedCents = 280_000, discretionarySpentCents = 41_000),
    )

    val discretionary = listOf(
        Category("Groceries", "basket", 37_900, 70_000),
        Category("Eating out", "restaurant", 17_600, 40_000),
        Category("Transport", "transport", 14_200, 30_000),
        Category("Fun", "fun", 16_300, 30_000),
        Category("Household", "household", 48_000, 50_000),
    )

    // ---- Derived totals -------------------------------------------------

    /** Bills are hidden entirely in the NO_BILLS_DUE demo state. */
    val visibleBills: List<Bill>
        get() = if (demoState == DemoState.NO_BILLS_DUE) bills.map { it.copy(paid = true) } else bills

    val plannedCents: Long get() = accounts.sumOf { it.plannedCents }

    val heldCents: Long get() = visibleBills.filterNot { it.paid }.sumOf { it.amountCents }

    val spentCents: Long
        get() = accounts.sumOf { it.discretionarySpentCents } +
            visibleBills.filter { it.paid }.sumOf { it.amountCents }

    val safeToSpendCents: Long get() = plannedCents - spentCents - heldCents

    /** Unpaid bills due within the next 3 days — drives the Home alert. */
    val imminentBills: List<Bill>
        get() = visibleBills.filter { !it.paid && (it.dueDay - today) in 0..3 }

    val paidCount: Int get() = visibleBills.count { it.paid }

    fun accountSpent(id: AccountId): Long =
        (accounts.first { it.id == id }.discretionarySpentCents) +
            visibleBills.filter { it.account == id && it.paid }.sumOf { it.amountCents }

    fun accountHeld(id: AccountId): Long =
        visibleBills.filter { it.account == id && !it.paid }.sumOf { it.amountCents }

    fun accountAvailable(id: AccountId): Long {
        val a = accounts.first { it.id == id }
        return a.plannedCents - accountSpent(id) - accountHeld(id)
    }

    /** Fixed categories are derived from bills: a paid bill has "spent" its amount. */
    val fixedCategories: List<Category>
        get() = visibleBills.map {
            Category(it.name, it.iconKey, if (it.paid) it.amountCents else 0L, it.amountCents)
        }

    // ---- Actions --------------------------------------------------------

    fun toggleBillPaid(id: String) {
        bills = bills.map {
            if (it.id == id) it.copy(paid = !it.paid, paidDay = if (!it.paid) today else null) else it
        }
    }

    fun selectDemoState(state: DemoState) {
        demoState = state
        if (state != DemoState.DEFAULT) billsScopedToImminent = false
        if (state == DemoState.DEFAULT) bills = InitialBills
    }

    fun selectDarkOverride(value: Boolean?) {
        darkOverride = value
    }

    fun scopeBillsToImminent(scoped: Boolean) {
        billsScopedToImminent = scoped
    }

    companion object {
        val InitialBills = listOf(
            Bill("rent", "Rent", "home", 120_000, AccountId.SHARED, paid = true, dueDay = 5, paidDay = 5),
            Bill("phone", "Phone", "phone", 6_000, AccountId.OWN, paid = true, dueDay = 12, paidDay = 12),
            Bill("utilities", "Utilities", "bolt", 36_000, AccountId.SHARED, paid = false, dueDay = 21),
            Bill("internet", "Internet", "wifi", 9_000, AccountId.OWN, paid = false, dueDay = 22),
            Bill(
                "insurance", "Insurance", "shield", 11_000, AccountId.SHARED,
                paid = true, dueDay = 31, dueDateIsDefaulted = true, paidDay = 28,
            ),
        )
    }
}
