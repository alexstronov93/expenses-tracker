package com.stronov.expensetracker.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Presentational models + the prototype's sample numbers (in cents / PLN).
 * This is static demo data so the Home screen matches the Figma prototype;
 * it will be replaced with real repository data in a later PR.
 */

data class Partner(val initial: String, val isA: Boolean)

data class AccountCard(
    val name: String,
    val maskedTail: String,
    val availableCents: Long,
    val plannedCents: Long,
    val spentCents: Long,
    val heldCents: Long,
    val isShared: Boolean,
)

data class CategorySpend(
    val name: String,
    val icon: ImageVector,
    val leftCents: Long,
    val budgetCents: Long,
)

enum class BillStatus { PAID, DUE }

data class Bill(
    val name: String,
    val icon: ImageVector,
    val status: BillStatus,
    val amountCents: Long,
    val plannedCents: Long,
    val dueLabel: String? = null,
)

data class HomeState(
    val monthLabel: String,
    val daysLeft: Int,
    val partners: List<Partner>,
    val billsDueCount: Int,
    val billsDueInDays: Int,
    val billsDueNames: String,
    val safeToSpendCents: Long,
    val budgetCents: Long,
    val spentCents: Long,
    val heldCents: Long,
    val freeCents: Long,
    val accounts: List<AccountCard>,
    val discretionary: List<CategorySpend>,
    val fixed: List<Bill>,
)

// Amounts are in cents. 124000 == 1 240 zł.
val SampleHome = HomeState(
    monthLabel = "July 2026",
    daysLeft = 12,
    partners = listOf(Partner("O", isA = true), Partner("A", isA = false)),
    billsDueCount = 2,
    billsDueInDays = 3,
    billsDueNames = "Utilities, Internet",
    safeToSpendCents = 124_000,
    budgetCents = 440_000,
    spentCents = 271_000,
    heldCents = 45_000,
    freeCents = 124_000,
    accounts = listOf(
        AccountCard("Own card", "1234", 52_000, 160_000, 99_000, 9_000, isShared = false),
        AccountCard("Shared account", "5678", 72_000, 280_000, 190_000, 18_000, isShared = true),
    ),
    discretionary = listOf(
        CategorySpend("Groceries", Icons.Outlined.ShoppingBasket, 32_100, 70_000),
        CategorySpend("Eating out", Icons.Outlined.Restaurant, 22_400, 40_000),
        CategorySpend("Transport", Icons.Outlined.DirectionsBus, 15_800, 30_000),
        CategorySpend("Fun", Icons.Outlined.ConfirmationNumber, 13_700, 30_000),
        CategorySpend("Household", Icons.Outlined.ShoppingBag, 2_000, 50_000),
    ),
    fixed = listOf(
        Bill("Rent", Icons.Outlined.Home, BillStatus.PAID, 120_000, 120_000),
        Bill("Phone", Icons.Outlined.Smartphone, BillStatus.PAID, 6_600, 6_000),
        Bill("Utilities", Icons.Outlined.Bolt, BillStatus.DUE, 36_000, 36_000, dueLabel = "Due 21 Jul"),
        Bill("Internet", Icons.Outlined.Wifi, BillStatus.DUE, 9_000, 9_000, dueLabel = "Due 22 Jul"),
        Bill("Insurance", Icons.Outlined.Shield, BillStatus.PAID, 10_400, 11_000),
    ),
)

// Icons reused by the Spent / Held / Free tiles.
val StatSpentIcon = Icons.Outlined.ReceiptLong
val StatHeldIcon = Icons.Outlined.Lock
val StatFreeIcon = Icons.Outlined.AccountBalanceWallet
