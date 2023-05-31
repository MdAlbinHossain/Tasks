package bd.com.albin.tasks.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.albin.tasks.data.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
) : ViewModel() {
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    fun initialize(taskId:Int) {
        viewModelScope.launch {
            taskUiState = tasksRepository.getTaskStream(taskId)
                .filterNotNull()
                .first()
                .toTaskUiState(true)
        }
    }


    fun deleteTask() {
        viewModelScope.launch {
            tasksRepository.deleteTask(taskUiState.taskDetails.toTask())
        }
    }
}

