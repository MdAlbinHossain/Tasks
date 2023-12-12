package bd.com.albin.tasks.ui.screens.taskedit

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import bd.com.albin.tasks.R
import bd.com.albin.tasks.ui.TasksAppState
import bd.com.albin.tasks.ui.screens.taskentry.TaskEntryBody
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksEditScreen(appState: TasksAppState, viewModel: TaskEditViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = { appState.popUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(
                            R.string.go_back
                        )
                    )
                }
            },
            title = { Text(stringResource(R.string.edit_task)) },
        )
    }) {
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                appState.coroutineScope.launch {
                    viewModel.updateTask()
                    appState.popUp()
                }
            },
            modifier = Modifier.padding(it)
        )
    }
}