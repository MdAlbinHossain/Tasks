package bd.com.albin.tasks.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import bd.com.albin.tasks.R
import bd.com.albin.tasks.ui.screens.HomeScreen
import bd.com.albin.tasks.ui.screens.TaskDetailsScreen
import bd.com.albin.tasks.ui.screens.TaskEditScreen
import bd.com.albin.tasks.ui.screens.TaskEntryScreen

sealed class Screen(val route: String, val titleRes: Int) {
    object Home : Screen(route = "home", titleRes = R.string.all_tasks)
    object Entry : Screen(route = "task_entry", titleRes = R.string.task_entry_title)
    object Edit : Screen(route = "task_edit", titleRes = R.string.edit_task_title) {
        const val taskIdArg = "taskId"
        val routeWithArgs = "$route/{$taskIdArg}"
    }

    object Details : Screen(route = "task_details", titleRes = R.string.task_detail_title) {
        const val taskIdArg = "taskId"
        val routeWithArgs = "$route/{$taskIdArg}"
    }
}

@Composable
fun TasksNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                navigateToTaskEntry = { navController.navigate(Screen.Entry.route) },
                navigateToTaskUpdate = {
                    navController.navigate("${Screen.Details.route}/${it}")
                }
            )
        }
        composable(route = Screen.Entry.route) {
            TaskEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = Screen.Details.routeWithArgs,
            arguments = listOf(navArgument(Screen.Details.taskIdArg) {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            TaskDetailsScreen(
                navigateToEditTask = { navController.navigate("${Screen.Edit.route}/$it") },
                navigateBack = { navController.navigateUp() },
                taskId = navBackStackEntry.arguments?.getInt(Screen.Edit.taskIdArg)?:0
            )
        }
        composable(
            route = Screen.Edit.routeWithArgs,
            arguments = listOf(navArgument(Screen.Edit.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                taskId = it.arguments?.getInt(Screen.Edit.taskIdArg) ?: 0
            )
        }
    }
}
