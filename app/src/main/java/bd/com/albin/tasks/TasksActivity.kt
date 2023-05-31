package bd.com.albin.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import bd.com.albin.tasks.ui.theme.TasksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasksTheme {
                TasksApp()
            }
        }
    }
}
