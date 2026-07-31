package com.stronov.expensetracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.stronov.expensetracker.ui.components.DuetlyCard
import com.stronov.expensetracker.ui.components.IconTile
import com.stronov.expensetracker.ui.components.PageMargin
import com.stronov.expensetracker.ui.components.RowDivider
import com.stronov.expensetracker.ui.components.SectionLabel
import com.stronov.expensetracker.ui.components.SegmentedBar
import com.stronov.expensetracker.ui.components.SemiGauge
import com.stronov.expensetracker.ui.model.Account
import com.stronov.expensetracker.ui.model.AccountId
import com.stronov.expensetracker.ui.model.Bill
import com.stronov.expensetracker.ui.model.Category
import com.stronov.expensetracker.ui.model.DemoState
import com.stronov.expensetracker.ui.model.DuetlyViewModel
import com.stronov.expensetracker.ui.theme.Duetly
import com.stronov.expensetracker.ui.theme.DuetlyType
import com.stronov.expensetracker.util.Money

@Composable
fun HomeScreen(
    vm: DuetlyViewModel,
    onOpenBills: () -> Unit,
    onOpenBudget: () -> Unit,
    onOpenMoneySource: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
    ) {
        Header(vm)
        Spacer(Modifier.height(14.dp))

        if (vm.demoState == DemoState.PARTNER_NOT_JOINED) {
            PartnerInviteCard()
            Spacer(Modifier.height(14.dp))
        }

        val imminent = vm.imminentBills
        if (imminent.isNotEmpty()) {
            DueBillsAlert(
                count = imminent.size,
                names = imminent.joinToString(", ") { it.name },
                days = (imminent.minOf { it.dueDay } - vm.today).coerceAtLeast(0),
                onClick = onOpenBills,
            )
            Spacer(Modifier.height(20.dp))
        }

        GaugeBlock(vm, onOpenMoneySource)
        Spacer(Modifier.height(22.dp))
        StatRow(vm)
        Spacer(Modifier.height(28.dp))

        SectionLabel("Accounts", action = "See all", onActionClick = onOpenBudget)
        Spacer(Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = PageMargin),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(vm.accounts) { account -> AccountCard(vm, account) }
        }
        Spacer(Modifier.height(28.dp))

        SectionLabel("Categories")
        Spacer(Modifier.height(12.dp))
        SubHeading("Discretionary")
        Spacer(Modifier.height(8.dp))
        DuetlyCard(Modifier.padding(horizontal = PageMargin)) {
            vm.discretionary.forEachIndexed { i, cat ->
                if (i > 0) RowDivider()
                CategoryRow(cat)
            }
        }

        Spacer(Modifier.height(20.dp))
        SubHeading("Fixed")
        Spacer(Modifier.height(8.dp))
        // Home shows bill status only — marking paid belongs to the Bills tab.
        DuetlyCard(Modifier.padding(horizontal = PageMargin)) {
            vm.visibleBills.forEachIndexed { i, bill ->
                if (i > 0) RowDivider()
                FixedRow(bill, onClick = onOpenBills)
            }
        }
    }
}

/* ------------------------------- Header ------------------------------- */

@Composable
private fun Header(vm: DuetlyViewModel) {
    val c = Duetly.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = PageMargin, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(vm.monthLabel, style = DuetlyType.screenTitle, color = c.textPrimary)
            Text("${vm.daysLeft} days left", style = DuetlyType.small, color = c.textSecondary)
        }
        Avatars(partnerJoined = vm.demoState != DemoState.PARTNER_NOT_JOINED)
    }
}

@Composable
private fun Avatars(partnerJoined: Boolean) {
    val c = Duetly.colors
    Row {
        Avatar("O", c.partnerA)
        Box(Modifier.offset(x = (-10).dp)) {
            if (partnerJoined) {
                Avatar("A", c.partnerB)
            } else {
                Box(
                    modifier = Modifier.size(34.dp).clip(CircleShape)
                        .background(c.sunken).border(1.dp, c.borderStrong, CircleShape),
                    contentAlignment = Alignment.Center,
                ) { Text("?", style = DuetlyType.bodyStrong, color = c.textSecondary) }
            }
        }
    }
}

@Composable
private fun Avatar(initial: String, bg: Color) {
    Box(
        modifier = Modifier.size(34.dp).clip(CircleShape).background(bg),
        contentAlignment = Alignment.Center,
    ) { Text(initial, style = DuetlyType.bodyStrong, color = Color.White) }
}

/* ---------------------------- Alert / invite --------------------------- */

@Composable
private fun DueBillsAlert(count: Int, names: String, days: Int, onClick: () -> Unit) {
    val c = Duetly.colors
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(horizontal = PageMargin)
            .clip(shape).background(c.warningSoft).clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(c.warningTile),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Outlined.WarningAmber, null, tint = c.warningInk, modifier = Modifier.size(20.dp)) }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "$count bills due in $days ${if (days == 1) "day" else "days"}",
                style = DuetlyType.titleMd, color = c.textPrimary,
            )
            Text(names, style = DuetlyType.small, color = c.warningInk)
        }
        Icon(Icons.Rounded.ChevronRight, null, tint = c.textSecondary)
    }
}

@Composable
private fun PartnerInviteCard() {
    val c = Duetly.colors
    DuetlyCard(Modifier.padding(horizontal = PageMargin)) {
        Column(Modifier.padding(16.dp)) {
            Text("Waiting for Alex to join", style = DuetlyType.titleMd, color = c.textPrimary)
            Spacer(Modifier.height(2.dp))
            Text("You're seeing your own money only.", style = DuetlyType.small, color = c.textSecondary)
            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(c.actionPrimary)
                    .clickable { }.padding(horizontal = 16.dp, vertical = 10.dp),
            ) { Text("Send invite", style = DuetlyType.bodyStrong, color = c.onActionPrimary) }
        }
    }
}

/* ------------------------------- Gauge -------------------------------- */

@Composable
private fun GaugeBlock(vm: DuetlyViewModel, onClick: () -> Unit) {
    val c = Duetly.colors
    Box(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        SemiGauge(
            fraction = vm.safeToSpendCents.toFloat() / vm.plannedCents.toFloat(),
            trackColor = c.gaugeTrack,
            fillColor = c.gaugeFill,
            thumbColor = c.gaugeThumb,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Safe to spend", style = DuetlyType.small, color = c.textSecondary)
                Spacer(Modifier.height(2.dp))
                Text(Money.formatPln(vm.safeToSpendCents), style = DuetlyType.amountHero, color = c.gaugeFill)
                Spacer(Modifier.height(2.dp))
                Text("of ${Money.formatPln(vm.plannedCents)}", style = DuetlyType.small, color = c.textSecondary)
            }
        }
    }
}

/* ----------------------------- Stat tiles ------------------------------ */

@Composable
private fun StatRow(vm: DuetlyViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = PageMargin),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        StatTile(Icons.Outlined.ReceiptLong, "Spent", vm.spentCents)
        StatTile(Icons.Outlined.Lock, "Held", vm.heldCents)
        StatTile(Icons.Outlined.AccountBalanceWallet, "Free", vm.safeToSpendCents)
    }
}

@Composable
private fun StatTile(icon: ImageVector, label: String, cents: Long) {
    val c = Duetly.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(42.dp).clip(CircleShape).background(c.sunken),
            contentAlignment = Alignment.Center,
        ) { Icon(icon, null, tint = c.textPrimary, modifier = Modifier.size(19.dp)) }
        Spacer(Modifier.height(8.dp))
        Text(label, style = DuetlyType.small, color = c.textSecondary)
        Spacer(Modifier.height(2.dp))
        Text(Money.formatPln(cents), style = DuetlyType.amountLg, color = c.textPrimary)
    }
}

/* ------------------------------ Accounts ------------------------------- */

@Composable
private fun AccountCard(vm: DuetlyViewModel, account: Account) {
    val c = Duetly.colors
    val spent = vm.accountSpent(account.id)
    val held = vm.accountHeld(account.id)
    val available = vm.accountAvailable(account.id)
    // Shared money reads as "ours" — the one place a partner colour carries data.
    val amountColor = if (account.id == AccountId.SHARED) c.partnerB else c.gaugeFill

    Column(
        modifier = Modifier
            .width(272.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(c.surface)
            .border(1.dp, c.border, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                account.name, style = DuetlyType.titleMd, color = c.textPrimary,
                modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
            Text("•• ${account.maskedTail}", style = DuetlyType.caption, color = c.textSecondary)
        }
        Spacer(Modifier.height(12.dp))
        Text("Available", style = DuetlyType.small, color = c.textSecondary)
        Text(Money.formatPln(available), style = DuetlyType.amountHero, color = amountColor)
        Spacer(Modifier.height(12.dp))
        SegmentedBar(
            segments = listOf(
                spent to c.segmentSpent,
                held to c.segmentHeld,
                available to c.gaugeFill,
            ),
            height = 6.dp,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Planned ${Money.formatPln(account.plannedCents)} · Spent ${Money.formatPln(spent)} · Held ${Money.formatPln(held)}",
            style = DuetlyType.caption, color = c.textSecondary,
            maxLines = 1, overflow = TextOverflow.Ellipsis,
        )
    }
}

/* ------------------------------ Categories ----------------------------- */

@Composable
private fun SubHeading(text: String) {
    val c = Duetly.colors
    Text(
        text, style = DuetlyType.bodyStrong, color = c.textPrimary,
        modifier = Modifier.padding(horizontal = PageMargin),
    )
}

@Composable
private fun CategoryRow(cat: Category) {
    val c = Duetly.colors
    val left = (cat.limitCents - cat.spentCents).coerceAtLeast(0)
    val nearlyOut = left.toFloat() / cat.limitCents.toFloat() < 0.1f
    Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconTile(cat.iconKey)
            Spacer(Modifier.width(12.dp))
            Text(cat.name, style = DuetlyType.body, color = c.textPrimary, modifier = Modifier.weight(1f))
            Text(
                Money.formatPln(left) + " left",
                style = DuetlyType.amountSm,
                color = if (nearlyOut) c.warning else c.gaugeFill,
            )
            Text(" of ${Money.formatPln(cat.limitCents)}", style = DuetlyType.small, color = c.textSecondary)
        }
        Spacer(Modifier.height(10.dp))
        SegmentedBar(
            segments = listOf(
                cat.spentCents to c.segmentSpent,
                left to if (nearlyOut) c.warning else c.gaugeFill,
            ),
            height = 6.dp,
        )
    }
}

/** Fixed (bill) row on Home: status pill + amount, tapping goes to Bills. */
@Composable
private fun FixedRow(bill: Bill, onClick: () -> Unit) {
    val c = Duetly.colors
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconTile(bill.iconKey)
        Spacer(Modifier.width(12.dp))
        Text(bill.name, style = DuetlyType.body, color = c.textPrimary, modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusPill(bill)
                Spacer(Modifier.width(10.dp))
                Text(
                    Money.formatPln(bill.amountCents),
                    style = DuetlyType.amountSm,
                    color = if (bill.paid) c.textPrimary else c.textSecondary,
                )
            }
        }
    }
}

@Composable
private fun StatusPill(bill: Bill) {
    val c = Duetly.colors
    val bg = if (bill.paid) c.successSoft else c.warningSoft
    val fg = if (bill.paid) c.successInk else c.warningInk
    val label = if (bill.paid) "Paid" else "Due ${bill.dueDay} Jul"
    Box(
        modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) { Text(label, style = DuetlyType.caption, color = fg) }
}
