package com.stronov.expensetracker.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stronov.expensetracker.ui.bills.BillsScreen
import com.stronov.expensetracker.ui.budget.BudgetScreen
import com.stronov.expensetracker.ui.components.DuetlyCard
import com.stronov.expensetracker.ui.components.PageMargin
import com.stronov.expensetracker.ui.home.HomeScreen
import com.stronov.expensetracker.ui.model.DemoState
import com.stronov.expensetracker.ui.model.DuetlyViewModel
import com.stronov.expensetracker.ui.theme.Duetly
import com.stronov.expensetracker.ui.theme.DuetlyMotion
import com.stronov.expensetracker.ui.theme.DuetlyTheme
import com.stronov.expensetracker.ui.theme.DuetlyType

enum class Tab(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Outlined.Home),
    ACTIVITY("Activity", Icons.Outlined.Timeline),
    BILLS("Bills", Icons.Outlined.Description),
    BUDGET("Budget", Icons.Outlined.PieChart),
    SETTINGS("Settings", Icons.Outlined.Settings),
}

@Composable
fun DuetlyApp() {
    val vm: DuetlyViewModel = viewModel()
    val systemDark = androidx.compose.foundation.isSystemInDarkTheme()
    DuetlyTheme(darkTheme = vm.darkOverride ?: systemDark) {
        val c = Duetly.colors
        var tab by remember { mutableStateOf(Tab.HOME) }
        var showMoneySource by remember { mutableStateOf(false) }

        Box(Modifier.fillMaxSize().background(c.appBg)) {
            Column(Modifier.fillMaxSize()) {
                Spacer(Modifier.statusBarsPadding())
                Box(Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = tab,
                        transitionSpec = {
                            fadeIn(tween(DuetlyMotion.BASE)) togetherWith fadeOut(tween(DuetlyMotion.FAST))
                        },
                        label = "tab",
                    ) { current ->
                        when (current) {
                            Tab.HOME -> HomeScreen(
                                vm = vm,
                                onOpenBills = { vm.scopeBillsToImminent(true); tab = Tab.BILLS },
                                onOpenBudget = { tab = Tab.BUDGET },
                                onOpenMoneySource = { showMoneySource = true },
                            )
                            Tab.BILLS -> BillsScreen(vm)
                            Tab.BUDGET -> BudgetScreen(vm)
                            Tab.ACTIVITY -> Placeholder(
                                "Activity",
                                "The transaction log and trend review live here.",
                            )
                            Tab.SETTINGS -> SettingsScreen(vm)
                        }
                    }
                }
                BottomNav(current = tab, onSelect = { tab = it; if (it != Tab.BILLS) vm.scopeBillsToImminent(false) })
            }

            // FAB — present on Home, Activity and Bills per the IA.
            if (tab == Tab.HOME || tab == Tab.ACTIVITY || tab == Tab.BILLS) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .navigationBarsPadding()
                        .padding(end = PageMargin, bottom = 84.dp)
                        .size(56.dp).clip(CircleShape).background(c.actionPrimary)
                        .clickable { },
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Filled.Add, "Add", tint = c.onActionPrimary, modifier = Modifier.size(26.dp)) }
            }

            if (showMoneySource) {
                MoneySourceSheet(vm) { showMoneySource = false }
            }
        }
    }
}

/* ------------------------------ Bottom nav ----------------------------- */

@Composable
private fun BottomNav(current: Tab, onSelect: (Tab) -> Unit) {
    val c = Duetly.colors
    Column(Modifier.fillMaxWidth().background(c.surface)) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(c.border))
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Tab.entries.forEach { t ->
                val selected = t == current
                val tint = if (selected) c.textPrimary else c.textSecondary
                Column(
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable { onSelect(t) }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(t.icon, t.label, tint = tint, modifier = Modifier.size(23.dp))
                    Spacer(Modifier.height(4.dp))
                    Text(t.label, style = DuetlyType.navLabel, color = tint)
                }
            }
        }
    }
}

/* --------------------------- Money source sheet ------------------------ */

@Composable
private fun MoneySourceSheet(vm: DuetlyViewModel, onDismiss: () -> Unit) {
    val c = Duetly.colors
    // Scrim: a low-opacity ink wash, never a blur.
    Box(
        Modifier.fillMaxSize()
            .background(androidx.compose.ui.graphics.Color(0x66101115))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(c.surface)
                .navigationBarsPadding()
                .padding(PageMargin),
        ) {
            Text("Where it comes from", style = DuetlyType.titleMd, color = c.textPrimary)
            Spacer(Modifier.height(4.dp))
            Text(
                "Safe to spend is split across the money you each bring in.",
                style = DuetlyType.small, color = c.textSecondary,
            )
            Spacer(Modifier.height(18.dp))
            vm.accounts.forEach { a ->
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(a.name, style = DuetlyType.body, color = c.textPrimary, modifier = Modifier.weight(1f))
                    Text(
                        com.stronov.expensetracker.util.Money.formatPln(vm.accountAvailable(a.id)),
                        style = DuetlyType.amountMd, color = c.textPrimary,
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(c.border))
            Row(Modifier.fillMaxWidth().padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Yours to use", style = DuetlyType.bodyStrong, color = c.textPrimary, modifier = Modifier.weight(1f))
                Text(
                    com.stronov.expensetracker.util.Money.formatPln(vm.safeToSpendCents),
                    style = DuetlyType.amountLg, color = c.textPrimary,
                )
            }
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                    .background(c.actionPrimary).clickable(onClick = onDismiss).padding(vertical = 13.dp),
                contentAlignment = Alignment.Center,
            ) { Text("Close", style = DuetlyType.bodyStrong, color = c.onActionPrimary) }
        }
    }
}

/* ------------------------------ Placeholders --------------------------- */

@Composable
private fun Placeholder(title: String, body: String) {
    val c = Duetly.colors
    Column(Modifier.fillMaxSize().padding(horizontal = PageMargin, vertical = 4.dp)) {
        Text(title, style = DuetlyType.screenTitle, color = c.textPrimary)
        Spacer(Modifier.height(16.dp))
        DuetlyCard {
            Text(body, style = DuetlyType.small, color = c.textSecondary, modifier = Modifier.padding(16.dp))
        }
    }
}

/** Settings doubles as the prototype's state switcher (the harness chips). */
@Composable
private fun SettingsScreen(vm: DuetlyViewModel) {
    val c = Duetly.colors
    Column(
        Modifier.fillMaxSize().padding(horizontal = PageMargin, vertical = 4.dp),
    ) {
        Text("Settings", style = DuetlyType.screenTitle, color = c.textPrimary)
        Spacer(Modifier.height(20.dp))

        Text("PROTOTYPE STATE", style = DuetlyType.sectionLabel, color = c.textSecondary)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChoiceChip("Default", vm.demoState == DemoState.DEFAULT) { vm.selectDemoState(DemoState.DEFAULT) }
            ChoiceChip("No bills due", vm.demoState == DemoState.NO_BILLS_DUE) { vm.selectDemoState(DemoState.NO_BILLS_DUE) }
        }
        Spacer(Modifier.height(8.dp))
        ChoiceChip("Partner not joined", vm.demoState == DemoState.PARTNER_NOT_JOINED) {
            vm.selectDemoState(DemoState.PARTNER_NOT_JOINED)
        }

        Spacer(Modifier.height(28.dp))
        Text("THEME", style = DuetlyType.sectionLabel, color = c.textSecondary)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChoiceChip("System", vm.darkOverride == null) { vm.selectDarkOverride(null) }
            ChoiceChip("Light", vm.darkOverride == false) { vm.selectDarkOverride(false) }
            ChoiceChip("Dark", vm.darkOverride == true) { vm.selectDarkOverride(true) }
        }
    }
}

@Composable
private fun ChoiceChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val c = Duetly.colors
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = Modifier
            .clip(shape)
            .background(if (selected) c.actionPrimary else c.surface)
            .border(1.dp, if (selected) c.actionPrimary else c.border, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 9.dp),
    ) {
        Text(
            label,
            style = DuetlyType.chip,
            color = if (selected) c.onActionPrimary else c.textPrimary,
        )
    }
}
