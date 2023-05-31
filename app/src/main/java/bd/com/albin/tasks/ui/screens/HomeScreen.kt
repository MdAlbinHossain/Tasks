package bd.com.albin.tasks.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bd.com.albin.tasks.R
import bd.com.albin.tasks.data.Task
import bd.com.albin.tasks.ui.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTaskEntry: () -> Unit,
    navigateToTaskUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(Screen.Home.titleRes)) },
                navigationIcon = {
                    Icon(
                        Icons.Default.GridView,
                        tint = MaterialTheme.colorScheme.primaryContainer,
                        modifier = modifier.size(40.dp),
                        contentDescription = "Icon"
                    )
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToTaskEntry, modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.task_entry_title),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        HomeBody(
            taskList = homeUiState.taskList,
            onTaskClick = navigateToTaskUpdate,
            onTaskIconClick = viewModel::updateTask,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeBody(
    taskList: List<Task>, onTaskClick: (Int) -> Unit,onTaskIconClick:(Task)->Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (taskList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_task_description),
                style = MaterialTheme.typography.displayMedium
            )
        } else {
            TasksList(taskList = taskList, onTaskClick = { onTaskClick(it.id) }, onTaskIconClick= onTaskIconClick)
        }
    }
}

@Composable
private fun TasksList(
    taskList: List<Task>, onTaskClick: (Task) -> Unit,onTaskIconClick:(Task)->Unit,  modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(items = taskList, key = { it.id }) { task ->
            TasksTask(task = task, onTaskClick = onTaskClick, onTaskIconClick=onTaskIconClick)
        }
    }
}

@Composable
private fun TasksTask(
    task: Task, onTaskClick: (Task) -> Unit,onTaskIconClick:(Task)->Unit,  modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(50),
        elevation = CardDefaults.cardElevation(10.dp),
    ) {
        Row(modifier = modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task) }
            .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                modifier = Modifier.alpha(if(task.completed) 0.5f else 1.0f))
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onTaskIconClick(task.copy(completed = !task.completed)) }) {
                Icon(
                    imageVector = if (task.completed) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).alpha(if(task.completed) 1.0f else 0.2f)
                )
            }
        }
    }
}