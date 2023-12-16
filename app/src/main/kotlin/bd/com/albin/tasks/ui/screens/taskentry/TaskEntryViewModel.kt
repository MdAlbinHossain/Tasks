package bd.com.albin.tasks.ui.screens.taskentry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.domain.usecases.InsertTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskEntryViewModel @Inject constructor(private val insertTaskUseCase: InsertTaskUseCase) :
    ViewModel() {
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    fun updateUiState(task: Task) {
        taskUiState = task.toTaskUiState(validateInput(task))
    }

    suspend fun insertTask() {
        if (validateInput())
            insertTaskUseCase.invoke(taskUiState.task)
    }

    private fun validateInput(uiState: Task = taskUiState.task): Boolean {
        return with(uiState) {
            title.isNotBlank()
        }
    }
}


data class TaskUiState(
    val task: Task = Task(), val isEntryValid: Boolean = false
)

fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    task = this, isEntryValid = isEntryValid
)