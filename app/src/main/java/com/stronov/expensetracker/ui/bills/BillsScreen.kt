package com.stronov.expensetracker.ui.bills

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.stronov.expensetracker.ui.components.DuetlyCard
import com.stronov.expensetracker.ui.components.PageMargin
import com.stronov.expensetracker.ui.components.RowDivider
import com.stronov.expensetracker.ui.model.Bill
import com.stronov.expensetracker.ui.model.DuetlyViewModel
import com.stronov.expensetracker.ui.theme.Duetly
import com.stronov.expensetracker.ui.theme.DuetlyMotion
import com.stronov.expensetracker.ui.theme.DuetlyType
import com.stronov.expensetracker.util.Money

private val MONTHS = "Jul"

@Composable
fun BillsScreen(vm: DuetlyViewModel) {
    val c = Duetly.colors
    val scoped = vm.billsScopedToImminent
    val imminentIds = vm.imminentBills.map { it.id }.toSet()
    val shown = if (scoped && imminentIds.isNotEmpty()) {
        vm.visibleBills.filter { it.id in imminentIds }
    } else {
        vm.visibleBills
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
    ) {
        Column(Modifier.padding(horizontal = PageMargin, vertical = 4.dp)) {
            Text("Bills", style = DuetlyType.screenTitle, color = c.textPrimary)
            Text(
                "${vm.paidCount} of ${vm.visibleBills.size} bills paid",
                style = DuetlyType.small, color = c.textSecondary,
            )
        }
        Spacer(Modifier.height(16.dp))

        if (scoped && imminentIds.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = PageMargin),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Due soon", style = DuetlyType.bodyStrong, color = c.textPrimary, modifier = Modifier.weight(1f))
                Text(
                    "Show all",
                    style = DuetlyType.bodyStrong, color = c.textPrimary,
                    modifier = Modifier.clickable { vm.scopeBillsToImminent(false) },
                )
            }
            Spacer(Modifier.height(10.dp))
        }

        DuetlyCard(Modifier.padding(horizontal = PageMargin)) {
            shown.forEachIndexed { i, bill ->
                if (i > 0) RowDivider(startInset = 62.dp)
                BillRow(bill) { vm.toggleBillPaid(bill.id) }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            "Either of you can mark a bill paid. Marking one moves its amount out of " +
                "held and into spent on Home — safe to spend doesn't change.",
            style = DuetlyType.small, color = c.textSecondary,
            modifier = Modifier.padding(horizontal = PageMargin),
        )
    }
}

@Composable
private fun BillRow(bill: Bill, onToggle: () -> Unit) {
    val c = Duetly.colors
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PaidToggle(bill.paid)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(bill.name, style = DuetlyType.body, color = c.textPrimary)
            val sub = if (bill.paid) {
                "Paid ${bill.paidDay} $MONTHS"
            } else {
                "Due ${bill.dueDay} $MONTHS" + if (bill.dueDateIsDefaulted) " · default date" else ""
            }
            Text(sub, style = DuetlyType.small, color = c.textSecondary)
        }
        Text(Money.formatPln(bill.amountCents), style = DuetlyType.amountLg, color = c.textPrimary)
    }
}

/** Filled ink circle with a check when paid; hairline ring when not. */
@Composable
private fun PaidToggle(paid: Boolean) {
    val c = Duetly.colors
    val fill by animateFloatAsState(
        targetValue = if (paid) 1f else 0f,
        animationSpec = tween(DuetlyMotion.BASE),
        label = "paid",
    )
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier.size(30.dp).clip(CircleShape)
                .border(1.dp, c.borderStrong, CircleShape),
        )
        Box(
            modifier = Modifier.size(30.dp).clip(CircleShape).alpha(fill).background(c.actionPrimary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Rounded.Check, "Paid", tint = c.onActionPrimary, modifier = Modifier.size(17.dp))
        }
    }
}
