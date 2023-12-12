package bd.com.albin.tasks.ui.screens.taskdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.domain.DeleteTaskUseCase
import bd.com.albin.tasks.domain.GetTaskStreamUseCase
import bd.com.albin.tasks.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTaskStreamUseCase: GetTaskStreamUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {
    private val taskId: Int = checkNotNull(savedStateHandle[Screen.TaskDetails.TASK_ID])
    val uiState: StateFlow<Task> = getTaskStreamUseCase.invoke(taskId).filterNotNull().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = Task()
    )

    suspend fun deleteTask() = deleteTaskUseCase.invoke(uiState.value)
}