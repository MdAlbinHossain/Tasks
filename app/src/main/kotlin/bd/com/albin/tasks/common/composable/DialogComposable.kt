package bd.com.albin.tasks.common.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import bd.com.albin.tasks.R
import bd.com.albin.tasks.common.ext.alertDialog
import bd.com.albin.tasks.common.ext.textButton
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(openDialog: MutableState<Boolean>, onDateSet: (Long?) -> Unit) {
    if (openDialog.value) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(onDismissRequest = { openDialog.value = false }, confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                    onDateSet(datePickerState.selectedDateMillis)
                }, enabled = confirmEnabled.value
            ) {
                Text(stringResource(id = R.string.ok))
            }
        }, dismissButton = {
            TextButton(onClick = {
                openDialog.value = false
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDialog(openDialog: MutableState<Boolean>, onTimeSet: (Int, Int) -> Unit) {
    if (openDialog.value) {
        val initialTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val state = rememberTimePickerState(
            initialHour = initialTime.hour, initialMinute = initialTime.minute, is24Hour = false
        )
        AlertDialog(onDismissRequest = { openDialog.value = false }, confirmButton = {
            TextButton(onClick = {
                openDialog.value = false
                onTimeSet(state.hour, state.minute)
            }) {
                Text(stringResource(id = R.string.ok))
            }
        }, dismissButton = {
            TextButton(onClick = {
                openDialog.value = false
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        }, title = { Text("Set Time") }, text = {
            TimePicker(state = state)
        })
    }
}

@Composable
fun PermissionDialog(onRequestPermission: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(modifier = Modifier.alertDialog(),
            title = { Text(stringResource(R.string.notification_permission_title)) },
            text = { Text(stringResource(R.string.notification_permission_description)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRequestPermission()
                        showWarningDialog = false
                    },
                    modifier = Modifier.textButton(),
                ) { Text(text = stringResource(R.string.request_permission)) }
            },
            onDismissRequest = { })
    }
}

@Composable
fun RationaleDialog() {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(modifier = Modifier.alertDialog(),
            title = { Text(stringResource(id = R.string.notification_permission_title)) },
            text = { Text(stringResource(id = R.string.notification_permission_description)) },
            confirmButton = {
                TextButton(
                    onClick = { showWarningDialog = false },
                    modifier = Modifier.textButton(),
                ) { Text(text = stringResource(R.string.ok)) }
            },
            onDismissRequest = { showWarningDialog = false })
    }
}