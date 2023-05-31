

package bd.com.albin.tasks.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import bd.com.albin.tasks.TasksTopAppBar
import bd.com.albin.tasks.ui.Screen


@Composable
fun TaskEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,taskId:Int,
    modifier: Modifier = Modifier,
    viewModel: EditViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit){
        viewModel.initialize(taskId)
    }
    Scaffold(
        topBar = {
            TasksTopAppBar(
                title = stringResource(Screen.Edit.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            onSaveClick = {
                    viewModel.updateTask()
                    navigateBack()
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

