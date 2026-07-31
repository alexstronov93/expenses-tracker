package com.stronov.expensetracker.ui.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stronov.expensetracker.ui.components.DuetlyCard
import com.stronov.expensetracker.ui.components.IconTile
import com.stronov.expensetracker.ui.components.PageMargin
import com.stronov.expensetracker.ui.components.RowDivider
import com.stronov.expensetracker.ui.components.SectionLabel
import com.stronov.expensetracker.ui.model.Category
import com.stronov.expensetracker.ui.model.DuetlyViewModel
import com.stronov.expensetracker.ui.theme.Duetly
import com.stronov.expensetracker.ui.theme.DuetlyType
import com.stronov.expensetracker.util.Money

@Composable
fun BudgetScreen(vm: DuetlyViewModel) {
    val c = Duetly.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
    ) {
        Column(Modifier.padding(horizontal = PageMargin, vertical = 4.dp)) {
            Text("Budget", style = DuetlyType.screenTitle, color = c.textPrimary)
            Text("${vm.monthLabel} · all categories", style = DuetlyType.small, color = c.textSecondary)
        }
        Spacer(Modifier.height(20.dp))

        SectionLabel("Fixed")
        Spacer(Modifier.height(10.dp))
        DuetlyCard(Modifier.padding(horizontal = PageMargin)) {
            vm.fixedCategories.forEachIndexed { i, cat ->
                if (i > 0) RowDivider()
                BudgetRow(cat)
            }
        }

        Spacer(Modifier.height(28.dp))
        SectionLabel("Discretionary")
        Spacer(Modifier.height(10.dp))
        DuetlyCard(Modifier.padding(horizontal = PageMargin)) {
            vm.discretionary.forEachIndexed { i, cat ->
                if (i > 0) RowDivider()
                BudgetRow(cat)
            }
        }
    }
}

/** Budget shows "spent of limit" — Home shows what's left. */
@Composable
private fun BudgetRow(cat: Category) {
    val c = Duetly.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconTile(cat.iconKey)
        Spacer(Modifier.width(12.dp))
        Text(cat.name, style = DuetlyType.body, color = c.textPrimary, modifier = Modifier.weight(1f))
        Text(Money.formatPln(cat.spentCents), style = DuetlyType.amountSm, color = c.textPrimary)
        Text(" of ${Money.formatPln(cat.limitCents)}", style = DuetlyType.small, color = c.textSecondary)
    }
}
