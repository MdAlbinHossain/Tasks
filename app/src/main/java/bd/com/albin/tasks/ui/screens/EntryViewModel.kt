package bd.com.albin.tasks.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.albin.tasks.data.Task
import bd.com.albin.tasks.data.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEntryViewModel @Inject constructor(private val tasksRepository: TasksRepository) :
    ViewModel() {


    var taskUiState by mutableStateOf(TaskUiState())
        private set


    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState =
            TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }


    fun saveTask() {
        if (validateInput()) {
            viewModelScope.launch{ tasksRepository.insertTask(taskUiState.taskDetails.toTask()) }
        }
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            title.isNotBlank()
//                    && description.isNotBlank() && date.isNotBlank()
        }
    }
}


data class TaskUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: Long =0,
    val time: Long = 0,
    val completed:Boolean=false,
)


fun TaskDetails.toTask(): Task = Task(
    id = id,
    title = title,
    description = description,
    time = time,
    date = date,
    completed = completed,
)


fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    taskDetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)


fun Task.toTaskDetails(): TaskDetails = TaskDetails(
    id = id,
    title = title,
    description = description,
    date = date,
    time = time,
    completed=completed,
)

