package bd.com.albin.tasks.ui.screens.taskshome

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bd.com.albin.tasks.R
import bd.com.albin.tasks.common.composable.LoadingScreen
import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.ui.TasksAppState
import bd.com.albin.tasks.ui.navigation.Screen
import bd.com.albin.tasks.ui.theme.TasksTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksHomeScreen(
    appState: TasksAppState, viewModel: TasksHomeViewModel = hiltViewModel()
) {
    val tasksHomeUiState: TasksHomeUiState by viewModel.tasksUiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(R.string.tasks)) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { appState.navigate(Screen.TaskEntry.route) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) }) { innerPadding ->
        when (tasksHomeUiState) {
            is TasksHomeUiState.Success -> HomeBody(
                taskList = (tasksHomeUiState as TasksHomeUiState.Success).tasksList,
                onTaskClick = { task: Task ->
                    appState.navigate("${Screen.TaskDetails.route}/${task.id}")
                },
                updateTask = {
                    appState.coroutineScope.launch {
                        viewModel.updateTask(it)
                        appState.onShowSnackbar(
                            it.title + " is marked as " + (if (it.completed) "" else "not ") + "completed",
                            null
                        )
                    }
                },
                deleteTask = {
                    val task = it
                    appState.coroutineScope.launch {
                        viewModel.deleteTask(it)
                        appState.onShowSnackbar(task.title + " is deleted.", null)
                    }
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )

            TasksHomeUiState.Loading -> LoadingScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun HomeBody(
    taskList: List<Task>,
    updateTask: (Task) -> Unit,
    onTaskClick: (Task) -> Unit,
    deleteTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
    ) {
        if (taskList.isEmpty()) {
            Column(
                modifier = modifier
                    .padding(32.dp)
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            ) {
                Text(
                    text = stringResource(R.string.no_task_added),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            TasksList(
                taskList = taskList,
                updateTask = updateTask,
                onTaskClick = onTaskClick,
                deleteTask = deleteTask,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TasksList(
    taskList: List<Task>,
    updateTask: (Task) -> Unit,
    onTaskClick: (Task) -> Unit,
    deleteTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = taskList, key = { it.id }) { task ->
            TaskItem(task = task,
                onDeleteClick = { deleteTask(task) },
                onTaskComplete = { updateTask(task.copy(completed = it)) },
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { onTaskClick(task) }
                    .animateItemPlacement(tween(250)))
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onTaskComplete: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(headlineContent = {
        Text(text = task.title, style = MaterialTheme.typography.titleMedium)
    }, supportingContent = {
        Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
    }, leadingContent = {
        Checkbox(checked = task.completed, onCheckedChange = { onTaskComplete(it) })
    }, tonalElevation = 4.dp, shadowElevation = 4.dp, modifier = modifier, trailingContent = {
        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    })
}


@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    TasksTheme {
        HomeBody(listOf(
            Task(1, "Game", "Hello"), Task(2, "Pen"), Task(3, "TV")
        ), updateTask = {}, onTaskClick = {}, deleteTask = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    TasksTheme {
        HomeBody(listOf(), updateTask = {}, onTaskClick = {}, deleteTask = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TasksTaskPreview() {
    TasksTheme {
        TaskItem(Task(1, "Game"), onTaskComplete = {}, onDeleteClick = {})
    }
}