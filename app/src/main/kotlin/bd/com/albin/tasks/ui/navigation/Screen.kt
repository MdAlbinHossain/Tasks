package bd.com.albin.tasks.ui.navigation

sealed class Screen(val route: String) {
    data object Onboard : Screen("onboard")
    data object TasksHome : Screen("tasks_home")
    data object TaskEdit : Screen("task_edit") {
        const val TASK_ID: String = "taskId"
    }

    data object TaskDetails : Screen("task_details") {
        const val TASK_ID: String = "taskId"
    }

    data object TaskEntry : Screen("task_entry")
    data object Settings : Screen("settings")
}