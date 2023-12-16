package bd.com.albin.tasks.ui

import android.os.Build
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import bd.com.albin.tasks.common.ext.RequestNotificationPermissionDialog
import bd.com.albin.tasks.ui.navigation.TasksNavHost

@Composable
fun TasksApp(
    windowSizeClass: WindowSizeClass,
    appState: TasksAppState = rememberTasksAppState(windowSizeClass = windowSizeClass),
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }
    Surface {
        TasksNavHost(appState = appState)
    }
}