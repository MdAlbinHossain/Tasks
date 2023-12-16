package bd.com.albin.tasks.ui.screens.taskshome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.domain.usecases.DeleteTaskUseCase
import bd.com.albin.tasks.domain.usecases.GetAllTasksStreamUseCase
import bd.com.albin.tasks.domain.usecases.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TasksHomeViewModel @Inject constructor(
    getAllTasksStreamUseCase: GetAllTasksStreamUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : ViewModel() {
    val tasksUiState: StateFlow<TasksHomeUiState> =
        getAllTasksStreamUseCase.invoke().map { TasksHomeUiState.Success(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = TasksHomeUiState.Loading
        )

    suspend fun updateTask(task: Task) {
        updateTaskUseCase.invoke(task)
    }

    suspend fun deleteTask(task: Task) = deleteTaskUseCase.invoke(task)
}

sealed interface TasksHomeUiState {
    data class Success(val tasksList: List<Task>) : TasksHomeUiState
    data object Loading : TasksHomeUiState
}