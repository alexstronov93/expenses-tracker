package com.stronov.expensetracker.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/** Navigation routes for the app. */
object Routes {
    const val LIST = "list"
    const val ADD = "add"
    const val EDIT = "edit"
    const val EDIT_ARG_ID = "expenseId"
    const val EDIT_WITH_ARG = "edit/{$EDIT_ARG_ID}"

    fun edit(id: Long) = "edit/$id"
}

/**
 * Root composable. Owns the [ExpenseViewModel] and wires up navigation so the
 * same view model instance is shared across screens.
 */
@Composable
fun ExpenseApp() {
    val navController = rememberNavController()
    val viewModel: ExpenseViewModel = viewModel(factory = ExpenseViewModel.Factory)

    NavHost(navController = navController, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            ExpenseListScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate(Routes.ADD) },
                onExpenseClick = { id -> navController.navigate(Routes.edit(id)) }
            )
        }
        composable(Routes.ADD) {
            AddEditExpenseScreen(
                viewModel = viewModel,
                expenseId = null,
                onDone = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.EDIT_WITH_ARG,
            arguments = listOf(navArgument(Routes.EDIT_ARG_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Routes.EDIT_ARG_ID)
            AddEditExpenseScreen(
                viewModel = viewModel,
                expenseId = id,
                onDone = { navController.popBackStack() }
            )
        }
    }
}
