package bd.com.albin.tasks.ui.screens.taskedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.domain.GetTaskStreamUseCase
import bd.com.albin.tasks.domain.UpdateTaskUseCase
import bd.com.albin.tasks.ui.navigation.Screen
import bd.com.albin.tasks.ui.screens.taskentry.TaskUiState
import bd.com.albin.tasks.ui.screens.taskentry.toTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTaskStreamUseCase: GetTaskStreamUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {
    var taskUiState by mutableStateOf(TaskUiState())

    private val taskId: Int = checkNotNull(savedStateHandle[Screen.TaskEdit.TASK_ID])

    init {
        viewModelScope.launch {
            taskUiState =
                getTaskStreamUseCase.invoke(taskId).filterNotNull().first().toTaskUiState(true)
        }
    }

    suspend fun updateTask() {
        updateTaskUseCase.invoke(taskUiState.task)
    }

    fun updateUiState(task: Task) {
        taskUiState = task.toTaskUiState(validateInput(task))
    }

    private fun validateInput(uiState: Task = taskUiState.task): Boolean {
        return with(uiState) {
            title.isNotBlank()
        }
    }
}