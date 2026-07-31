package com.stronov.expensetracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.stronov.expensetracker.ui.components.DuetlyCard
import com.stronov.expensetracker.ui.components.IconTile
import com.stronov.expensetracker.ui.components.LegendItem
import com.stronov.expensetracker.ui.components.PageMargin
import com.stronov.expensetracker.ui.components.RowDivider
import com.stronov.expensetracker.ui.components.SectionLabel
import com.stronov.expensetracker.ui.components.SegmentedBar
import com.stronov.expensetracker.ui.model.Account
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
        Spacer(Modifier.height(16.dp))

        if (vm.demoState == DemoState.PARTNER_NOT_JOINED) {
            PartnerInviteCard()
            Spacer(Modifier.height(16.dp))
        }

        val imminent = vm.imminentBills
        if (imminent.isNotEmpty()) {
            DueBillsAlert(
                count = imminent.size,
                names = imminent.joinToString(", ") { it.name },
                days = imminent.minOf { it.dueDay - vm.today }.coerceAtLeast(0),
                onClick = onOpenBills,
            )
            Spacer(Modifier.height(16.dp))
        }

        SafeToSpendCard(vm, onOpenMoneySource)
        Spacer(Modifier.height(28.dp))

        SectionLabel("Status by account")
        Spacer(Modifier.height(10.dp))
        DuetlyCard(Modifier.padding(horizontal = PageMargin)) {
            vm.accounts.forEachIndexed { i, account ->
                if (i > 0) RowDivider(startInset = 16.dp)
                AccountRow(vm, account)
            }
        }
        Spacer(Modifier.height(28.dp))

        SectionLabel("Status by category", action = "See all", onActionClick = onOpenBudget)
        Spacer(Modifier.height(10.dp))
        DuetlyCard(Modifier.padding(horizontal = PageMargin)) {
            vm.discretionary.forEachIndexed { i, cat ->
                if (i > 0) RowDivider()
                CategoryRow(cat)
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
        Avatar("O", c.partnerA, Color.White)
        if (partnerJoined) {
            Box(Modifier.offset(x = (-10).dp)) { Avatar("A", c.partnerB, Color.White) }
        } else {
            // Not joined yet: a dashed-feeling neutral placeholder, never a partner color.
            Box(Modifier.offset(x = (-10).dp)) {
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
private fun Avatar(initial: String, bg: Color, fg: Color) {
    Box(
        modifier = Modifier.size(34.dp).clip(CircleShape).background(bg),
        contentAlignment = Alignment.Center,
    ) { Text(initial, style = DuetlyType.bodyStrong, color = fg) }
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
            val dayText = if (days <= 1) "$days day" else "$days days"
            Text("$count bills due in $dayText", style = DuetlyType.titleMd, color = c.textPrimary)
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
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)).background(c.actionPrimary)
                    .clickable { }.padding(horizontal = 16.dp, vertical = 10.dp),
            ) { Text("Send invite", style = DuetlyType.bodyStrong, color = c.onActionPrimary) }
        }
    }
}

/* ---------------------------- Safe to spend ---------------------------- */

@Composable
private fun SafeToSpendCard(vm: DuetlyViewModel, onClick: () -> Unit) {
    val c = Duetly.colors
    DuetlyCard(Modifier.padding(horizontal = PageMargin), onClick = onClick) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Safe to spend", style = DuetlyType.small, color = c.textSecondary, modifier = Modifier.weight(1f))
                Icon(Icons.Rounded.ChevronRight, null, tint = c.textSecondary, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(Money.formatPln(vm.safeToSpendCents), style = DuetlyType.amountHero, color = c.textPrimary)
                Spacer(Modifier.width(8.dp))
                Text(
                    "of ${Money.formatPln(vm.plannedCents)} planned",
                    style = DuetlyType.small, color = c.textSecondary,
                    modifier = Modifier.padding(bottom = 6.dp),
                )
            }
            Spacer(Modifier.height(14.dp))
            SegmentedBar(
                segments = listOf(
                    vm.spentCents to c.segmentSpent,
                    vm.heldCents to c.segmentHeld,
                    vm.safeToSpendCents to c.segmentSafe,
                ),
            )
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LegendItem(c.segmentSpent, "Spent", Money.formatPln(vm.spentCents))
                LegendItem(c.segmentHeld, "Held", Money.formatPln(vm.heldCents))
            }
        }
    }
}

/* ------------------------------- Rows ---------------------------------- */

@Composable
private fun AccountRow(vm: DuetlyViewModel, account: Account) {
    val c = Duetly.colors
    val spent = vm.accountSpent(account.id)
    val held = vm.accountHeld(account.id)
    val available = vm.accountAvailable(account.id)
    Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${account.name} •• ${account.maskedTail}",
                style = DuetlyType.bodyStrong, color = c.textPrimary,
                modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
            Text("Available ", style = DuetlyType.small, color = c.textSecondary)
            Text(Money.formatPln(available), style = DuetlyType.amountMd, color = c.textPrimary)
        }
        Spacer(Modifier.height(10.dp))
        SegmentedBar(
            segments = listOf(
                spent to c.segmentSpent,
                held to c.segmentHeld,
                available to c.segmentSafe,
            ),
            height = 6.dp,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Planned ${Money.formatPln(account.plannedCents)} · Spent ${Money.formatPln(spent)} · Held ${Money.formatPln(held)}",
            style = DuetlyType.caption, color = c.textSecondary,
            maxLines = 1, overflow = TextOverflow.Ellipsis,
        )
    }
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
                Money.formatPln(left),
                style = DuetlyType.amountSm,
                color = if (nearlyOut) c.warningInk else c.textPrimary,
            )
            Text(" left", style = DuetlyType.small, color = c.textSecondary)
        }
        Spacer(Modifier.height(10.dp))
        SegmentedBar(
            segments = listOf(
                cat.spentCents to if (nearlyOut) c.warning else c.segmentSpent,
                left to c.segmentSafe,
            ),
            height = 6.dp,
        )
    }
}
