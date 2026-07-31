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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
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
import com.stronov.expensetracker.ui.theme.Duetly
import com.stronov.expensetracker.ui.theme.DuetlyType
import com.stronov.expensetracker.util.Money

private val PagePad = 20.dp

@Composable
fun HomeScreen(state: HomeState = SampleHome) {
    val c = Duetly.colors
    Box(modifier = Modifier.fillMaxSize().background(c.appBg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp),
        ) {
            Spacer(Modifier.statusBarsPadding())
            Header(state)
            Spacer(Modifier.height(16.dp))
            if (state.billsDueCount > 0) {
                AlertCard(state)
                Spacer(Modifier.height(20.dp))
            }
            GaugeBlock(state)
            Spacer(Modifier.height(24.dp))
            StatRow(state)
            Spacer(Modifier.height(28.dp))
            AccountsSection(state)
            Spacer(Modifier.height(28.dp))
            CategoriesSection(state)
        }

        BottomNav(modifier = Modifier.align(Alignment.BottomCenter))
        Fab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = PagePad, bottom = 72.dp),
        )
    }
}

/* ----------------------------- Header ----------------------------- */

@Composable
private fun Header(state: HomeState) {
    val c = Duetly.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = PagePad, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(state.monthLabel, style = DuetlyType.h1, color = c.textPrimary)
            Spacer(Modifier.height(2.dp))
            Text("${state.daysLeft} days left", style = DuetlyType.small, color = c.textSecondary)
        }
        Avatars(state)
    }
}

@Composable
private fun Avatars(state: HomeState) {
    val c = Duetly.colors
    Row {
        state.partners.forEachIndexed { i, p ->
            val bg = if (p.isA) c.partnerA else c.partnerB
            Box(
                modifier = Modifier
                    .offset(x = if (i == 0) 0.dp else (-10).dp)
                    .size(36.dp)
                    .border(2.dp, c.appBg, CircleShape)
                    .clip(CircleShape)
                    .background(bg),
                contentAlignment = Alignment.Center,
            ) {
                Text(p.initial, style = DuetlyType.bodyStrong, color = Color.White)
            }
        }
    }
}

/* --------------------------- Alert card --------------------------- */

@Composable
private fun AlertCard(state: HomeState) {
    val c = Duetly.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PagePad)
            .clip(RoundedCornerShape(16.dp))
            .background(c.warningSoft)
            .clickable { }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp))
                .background(c.warning.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.WarningAmber, null, tint = c.warning, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "${state.billsDueCount} bills due in ${state.billsDueInDays} days",
                style = DuetlyType.bodyStrong, color = c.textPrimary,
            )
            Text(state.billsDueNames, style = DuetlyType.small, color = c.warning)
        }
        Icon(Icons.Rounded.ChevronRight, null, tint = c.textSecondary)
    }
}

/* ----------------------------- Gauge ------------------------------ */

@Composable
private fun GaugeBlock(state: HomeState) {
    val c = Duetly.colors
    val fraction = state.safeToSpendCents.toFloat() / state.budgetCents.toFloat()
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SemiGauge(
            fraction = fraction,
            trackColor = c.gaugeTrack,
            fillColor = c.gaugeFill,
            thumbColor = c.gaugeThumb,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Safe to spend", style = DuetlyType.small, color = c.textSecondary)
                Spacer(Modifier.height(2.dp))
                Text(Money.formatPln(state.safeToSpendCents), style = DuetlyType.amountXl, color = c.positive)
                Spacer(Modifier.height(2.dp))
                Text("of ${Money.formatPln(state.budgetCents)}", style = DuetlyType.small, color = c.textSecondary)
            }
        }
    }
}

/* --------------------------- Stat tiles --------------------------- */

@Composable
private fun StatRow(state: HomeState) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = PagePad),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        StatTile(StatSpentIcon, "Spent", state.spentCents)
        StatTile(StatHeldIcon, "Held", state.heldCents)
        StatTile(StatFreeIcon, "Free", state.freeCents)
    }
}

@Composable
private fun StatTile(icon: ImageVector, label: String, cents: Long) {
    val c = Duetly.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(44.dp).clip(CircleShape).background(c.sunken),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, tint = c.textPrimary, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(label, style = DuetlyType.small, color = c.textSecondary)
        Spacer(Modifier.height(2.dp))
        Text(Money.formatPln(cents), style = DuetlyType.amountMd, color = c.textPrimary)
    }
}

/* --------------------------- Accounts ----------------------------- */

@Composable
private fun AccountsSection(state: HomeState) {
    SectionHeader("ACCOUNTS", trailing = "See all")
    Spacer(Modifier.height(12.dp))
    LazyRow(
        contentPadding = PaddingValues(horizontal = PagePad),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(state.accounts) { acc -> AccountCardView(acc) }
    }
}

@Composable
private fun AccountCardView(acc: AccountCard) {
    val c = Duetly.colors
    val amountColor = if (acc.isShared) c.partnerB else c.positive
    Column(
        modifier = Modifier
            .width(300.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(c.surface)
            .border(1.dp, c.border, RoundedCornerShape(20.dp))
            .padding(18.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(acc.name, style = DuetlyType.titleMd, color = c.textPrimary, modifier = Modifier.weight(1f))
            Text("•• ${acc.maskedTail}", style = DuetlyType.small, color = c.textSecondary)
        }
        Spacer(Modifier.height(14.dp))
        Text("Available", style = DuetlyType.small, color = c.textSecondary)
        Text(Money.formatPln(acc.availableCents), style = DuetlyType.amountLg, color = amountColor)
        Spacer(Modifier.height(14.dp))
        SegmentedBar(
            segments = listOf(
                acc.spentCents.toFloat() to c.gaugeTrack,
                acc.heldCents.toFloat() to c.gaugeThumb,
                acc.availableCents.toFloat() to c.positive,
            ),
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Planned ${Money.formatPln(acc.plannedCents)} · Spent ${Money.formatPln(acc.spentCents)} · Held ${Money.formatPln(acc.heldCents)}",
            style = DuetlyType.small, color = c.textFaint, maxLines = 1, overflow = TextOverflow.Ellipsis,
        )
    }
}

/* -------------------------- Categories ---------------------------- */

@Composable
private fun CategoriesSection(state: HomeState) {
    val c = Duetly.colors
    SectionHeader("CATEGORIES")
    Spacer(Modifier.height(10.dp))
    Text("Discretionary", style = DuetlyType.bodyStrong, color = c.textPrimary, modifier = Modifier.padding(horizontal = PagePad))
    Spacer(Modifier.height(10.dp))
    CardContainer {
        state.discretionary.forEachIndexed { i, cat ->
            if (i > 0) RowDivider()
            CategoryRow(cat)
        }
    }
    Spacer(Modifier.height(20.dp))
    Text("Fixed", style = DuetlyType.bodyStrong, color = c.textPrimary, modifier = Modifier.padding(horizontal = PagePad))
    Spacer(Modifier.height(10.dp))
    CardContainer {
        state.fixed.forEachIndexed { i, bill ->
            if (i > 0) RowDivider()
            BillRow(bill)
        }
    }
}

@Composable
private fun CategoryRow(cat: CategorySpend) {
    val c = Duetly.colors
    val leftFrac = cat.leftCents.toFloat() / cat.budgetCents.toFloat()
    val low = leftFrac < 0.1f
    val leftColor = if (low) c.warning else c.positive
    val remainingColor = if (low) c.warning else c.positive
    val spent = (cat.budgetCents - cat.leftCents).coerceAtLeast(0)

    Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconTile(cat.icon)
            Spacer(Modifier.width(12.dp))
            Text(cat.name, style = DuetlyType.body, color = c.textPrimary, modifier = Modifier.weight(1f))
            Row {
                Text(Money.formatPln(cat.leftCents) + " left", style = DuetlyType.bodyStrong, color = leftColor)
                Text(" of " + Money.formatPln(cat.budgetCents), style = DuetlyType.body, color = c.textSecondary)
            }
        }
        Spacer(Modifier.height(12.dp))
        SegmentedBar(
            segments = listOf(
                spent.toFloat() to c.gaugeTrack,
                cat.leftCents.toFloat() to remainingColor,
            ),
        )
    }
}

@Composable
private fun BillRow(bill: Bill) {
    val c = Duetly.colors
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconTile(bill.icon)
        Spacer(Modifier.width(12.dp))
        Text(bill.name, style = DuetlyType.body, color = c.textPrimary, modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusPill(bill)
                Spacer(Modifier.width(10.dp))
                val amountColor = if (bill.status == BillStatus.DUE) c.textSecondary else c.textPrimary
                Text(Money.formatPln(bill.amountCents), style = DuetlyType.amountMd, color = amountColor)
            }
            if (bill.status == BillStatus.PAID) {
                Spacer(Modifier.height(2.dp))
                Text("Planned ${Money.formatPln(bill.plannedCents)}", style = DuetlyType.small, color = c.textFaint)
            }
        }
    }
}

@Composable
private fun StatusPill(bill: Bill) {
    val c = Duetly.colors
    val (bg, fg, label) = if (bill.status == BillStatus.PAID) {
        Triple(c.paidSoft, c.paidText, "Paid")
    } else {
        Triple(c.warningSoft, c.warning, bill.dueLabel ?: "Due")
    }
    Box(
        modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(bg).padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(label, style = DuetlyType.pill, color = fg)
    }
}

/* --------------------------- Bottom nav --------------------------- */

private data class NavItem(val icon: ImageVector, val label: String, val selected: Boolean)

@Composable
private fun BottomNav(modifier: Modifier = Modifier) {
    val c = Duetly.colors
    val items = listOf(
        NavItem(Icons.Outlined.Home, "Home", true),
        NavItem(Icons.Outlined.Timeline, "Activity", false),
        NavItem(Icons.Outlined.Description, "Bills", false),
        NavItem(Icons.Outlined.PieChart, "Budget", false),
        NavItem(Icons.Outlined.Settings, "Settings", false),
    )
    Column(modifier = modifier.fillMaxWidth().background(c.surface)) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(c.border))
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            items.forEach { item ->
                val tint = if (item.selected) c.textPrimary else c.textSecondary
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(item.icon, item.label, tint = tint, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.height(4.dp))
                    Text(item.label, style = DuetlyType.navLabel, color = tint)
                }
            }
        }
    }
}

@Composable
private fun Fab(modifier: Modifier = Modifier) {
    val c = Duetly.colors
    Box(
        modifier = modifier.size(56.dp).clip(CircleShape).background(c.inkSurface).clickable { },
        contentAlignment = Alignment.Center,
    ) {
        Icon(Icons.Filled.Add, "Add", tint = c.onInkSurface, modifier = Modifier.size(26.dp))
    }
}

/* ------------------------- Shared pieces -------------------------- */

@Composable
private fun SectionHeader(title: String, trailing: String? = null) {
    val c = Duetly.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = PagePad),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = DuetlyType.label, color = c.textSecondary, modifier = Modifier.weight(1f))
        if (trailing != null) {
            Text(trailing, style = DuetlyType.bodyStrong, color = c.textPrimary)
        }
    }
}

@Composable
private fun CardContainer(content: @Composable () -> Unit) {
    val c = Duetly.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PagePad)
            .clip(RoundedCornerShape(20.dp))
            .background(c.surface)
            .border(1.dp, c.border, RoundedCornerShape(20.dp)),
    ) { content() }
}

@Composable
private fun RowDivider() {
    val c = Duetly.colors
    Box(Modifier.fillMaxWidth().padding(start = 52.dp).height(1.dp).background(c.border))
}

@Composable
private fun IconTile(icon: ImageVector) {
    val c = Duetly.colors
    Box(
        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(c.sunken),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = c.textPrimary, modifier = Modifier.size(20.dp))
    }
}

/** A rounded, gapped, weighted progress bar. Zero-weight segments are dropped. */
@Composable
private fun SegmentedBar(segments: List<Pair<Float, Color>>, height: androidx.compose.ui.unit.Dp = 7.dp) {
    val visible = segments.filter { it.first > 0f }
    Row(
        modifier = Modifier.fillMaxWidth().height(height),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        visible.forEach { (weight, color) ->
            Box(
                modifier = Modifier
                    .weight(weight)
                    .height(height)
                    .clip(RoundedCornerShape(999.dp))
                    .background(color),
            )
        }
    }
}
