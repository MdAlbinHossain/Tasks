package bd.com.albin.tasks.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bd.com.albin.tasks.R
import bd.com.albin.tasks.TasksTopAppBar
import bd.com.albin.tasks.ui.Screen

@Composable
fun TaskDetailsScreen(
    taskId: Int,
    navigateToEditTask: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.initialize(taskId) }
    val uiState = viewModel.taskUiState
    Scaffold(
        topBar = {
            TasksTopAppBar(
                title = stringResource(Screen.Details.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->
        TaskDetailsBody(
            taskDetailsUiState = uiState.taskDetails,
            onDelete = {
                viewModel.deleteTask()
                navigateBack()
            }, onEdit = { navigateToEditTask(uiState.taskDetails.id) },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun TaskDetailsBody(
    taskDetailsUiState: TaskDetails,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        TaskInputForm(taskDetails = taskDetailsUiState, enabled = false)

        Button(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.edit))
        }

        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false }
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}
