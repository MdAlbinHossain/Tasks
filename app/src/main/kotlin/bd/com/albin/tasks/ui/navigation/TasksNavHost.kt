package bd.com.albin.tasks.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import bd.com.albin.tasks.ui.TasksAppState
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
        composable(route = Screen.TaskEntry.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            },) {
            TaskEntryScreen(appState = appState)
        }
        composable(
            route = "${Screen.TaskEdit.route}/{${Screen.TaskEdit.TASK_ID}}",
            arguments = listOf(navArgument(Screen.TaskEdit.TASK_ID) { type = NavType.IntType }),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
        ) {
            TasksEditScreen(appState = appState)
        }
        composable(route = Screen.Settings.route) {

        }
    }
}