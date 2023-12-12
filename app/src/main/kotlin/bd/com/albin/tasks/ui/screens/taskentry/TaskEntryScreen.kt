package bd.com.albin.tasks.ui.screens.taskentry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bd.com.albin.tasks.R
import bd.com.albin.tasks.common.composable.DateDialog
import bd.com.albin.tasks.common.composable.TimeDialog
import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.ui.TasksAppState
import bd.com.albin.tasks.ui.theme.TasksTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryScreen(appState: TasksAppState, viewModel: TaskEntryViewModel = hiltViewModel()) {
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
            title = { Text(stringResource(R.string.enter_new_task)) },
        )
    }) {
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                appState.coroutineScope.launch {
                    viewModel.insertTask()
                    appState.popUp()
                }
            },
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun TaskEntryBody(
    taskUiState: TaskUiState,
    onItemValueChange: (Task) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        TaskInputForm(
            task = taskUiState.task,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = taskUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInputForm(
    task: Task,
    modifier: Modifier = Modifier,
    onValueChange: (Task) -> Unit = {},
    enabled: Boolean = true
) {
    val openDateDialog = remember { mutableStateOf(false) }
    val openTimeDialog = remember { mutableStateOf(false) }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TaskProperty(
            label = "Task Name",
            value = task.title,
            onValueChange = { onValueChange(task.copy(title = it)) },
            enabled = enabled,
            singleLine = true,
        )
        TaskProperty(
            label = "Description",
            value = task.description,
            onValueChange = { onValueChange(task.copy(description = it)) },
        )
        Card(onClick = { openDateDialog.value = true }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.EditCalendar, contentDescription = "Set Date")
                Spacer(modifier = Modifier.width(16.dp))
                if (task.dueDate == 0L) Text(text = "Set Date")
                else {
                    val calendar = Calendar.getInstance(TimeZone.getDefault())
                    calendar.timeInMillis = task.dueDate
                    val date =
                        SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH).format(calendar.time)
                    Text(text = date.toString())
                }
                DateDialog(openDialog = openDateDialog) {
                    it?.let {
                        onValueChange(task.copy(dueDate = it))
                    }
                }
            }
        }
        Card(onClick = { openTimeDialog.value = true }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Set Time")
                Spacer(modifier = Modifier.width(16.dp))
                Text(if (task.dueTime == 0L) "Set Time" else "${task.dueTime / 60}:${task.dueTime % 60}")
                TimeDialog(openDialog = openTimeDialog) { a, b ->
                    onValueChange(task.copy(dueTime = (a.toLong() * 60L) + b.toLong()))
                }
            }
        }
    }
}

@Composable
fun TaskProperty(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = false,
) {
    TextField(
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = singleLine,
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskEntryBody() {
    TasksTheme {
        Surface {
            TaskEntryBody(taskUiState = TaskUiState(),
                onItemValueChange = {},
                onSaveClick = { /*TODO*/ })
        }
    }
}