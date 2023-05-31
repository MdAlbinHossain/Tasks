package bd.com.albin.tasks.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bd.com.albin.tasks.R
import bd.com.albin.tasks.TasksTopAppBar
import bd.com.albin.tasks.ui.Screen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TaskEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: TaskEntryViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TasksTopAppBar(
                title = stringResource(Screen.Entry.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            onSaveClick = {
                    viewModel.saveTask()
                    navigateBack()
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun TaskEntryBody(
    taskUiState: TaskUiState,
    onTaskValueChange: (TaskDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        TaskInputForm(taskDetails = taskUiState.taskDetails, onValueChange = onTaskValueChange)
        Button(
            onClick = onSaveClick,
            enabled = taskUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun TaskInputForm(
    taskDetails: TaskDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TaskDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextField(
            value = taskDetails.title,
            onValueChange = { onValueChange(taskDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.task_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        TextField(
            value = taskDetails.description,
            onValueChange = { onValueChange(taskDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.task_price_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
        )

        val context = LocalContext.current
        CardPicker(
            title = stringResource(R.string.date),
            icon = Icons.Default.EditCalendar,
            content = SimpleDateFormat(
                "EEE, d MMM yyyy",
                Locale.getDefault()
            ).format(taskDetails.date),
            onEditClick = { showDatePicker(context, taskDetails = taskDetails, onValueChange) },
            modifier = Modifier
        )
        CardPicker(
            title = stringResource(R.string.time),
            icon = Icons.Default.Schedule,
            content = SimpleDateFormat(
                "h:mm aa",
                Locale.getDefault()
            ).format(taskDetails.time),
            onEditClick = { showTimePicker(context, taskDetails = taskDetails, onValueChange) },
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardPicker(
    title: String,
    icon: ImageVector,
    content: String,
    onEditClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        onClick = onEditClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) { Text(title) }

            if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
            }

            Icon(icon, contentDescription = null)
        }
    }
}


fun showDatePicker(
    context: Context,
    taskDetails: TaskDetails,
    onValueChange: (TaskDetails) -> Unit = {}
) {
    val calendar = Calendar.getInstance()

    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context, { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
            onValueChange(taskDetails.copy(date = calendar.timeInMillis))
        }, year, month, dayOfMonth
    )
    datePicker.show()
}

fun showTimePicker(
    context: Context,
    taskDetails: TaskDetails,
    onValueChange: (TaskDetails) -> Unit = {},
) {
    val calendar = Calendar.getInstance()

    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val timePicker = TimePickerDialog(
        context, { _, selectedHour: Int, selectedMinute: Int ->
            calendar.set(0, 0, 0, selectedHour, selectedMinute)
            onValueChange(taskDetails.copy(time = calendar.timeInMillis))
        }, hour, minute, false
    )
    timePicker.show()
}
