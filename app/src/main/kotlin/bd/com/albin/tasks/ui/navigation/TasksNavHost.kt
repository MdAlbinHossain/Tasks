package bd.com.albin.tasks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import bd.com.albin.tasks.ui.TasksAppState
import bd.com.albin.tasks.ui.screens.taskdetails.TaskDetailsScreen
import bd.com.albin.tasks.ui.screens.taskedit.TasksEditScreen
import bd.com.albin.tasks.ui.screens.taskentry.TaskEntryScreen
import bd.com.albin.tasks.ui.screens.taskshome.TasksHomeScreen

@Composable
fun TasksNavHost(
    appState: TasksAppState,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.TasksHome.route,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        composable(route = Screen.Onboard.route) {

        }
        composable(route = Screen.TasksHome.route) {
            TasksHomeScreen(appState)
        }
        composable(route = Screen.TaskEntry.route) {
            TaskEntryScreen(appState = appState)
        }
        composable(
            route = "${Screen.TaskDetails.route}/{${Screen.TaskDetails.TASK_ID}}",
            arguments = listOf(navArgument(Screen.TaskDetails.TASK_ID) { type = NavType.IntType })
        ) {
            TaskDetailsScreen(appState = appState)
        }
        composable(
            route = "${Screen.TaskEdit.route}/{${Screen.TaskEdit.TASK_ID}}",
            arguments = listOf(navArgument(Screen.TaskEdit.TASK_ID) { type = NavType.IntType })
        ) {
            TasksEditScreen(appState = appState)
        }
        composable(route = Screen.Settings.route) {

        }
    }
}