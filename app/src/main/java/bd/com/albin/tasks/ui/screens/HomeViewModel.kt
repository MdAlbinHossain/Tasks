

package bd.com.albin.tasks.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.albin.tasks.data.Task
import bd.com.albin.tasks.data.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val tasksRepository: TasksRepository) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        tasksRepository.getAllTasksStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    fun updateTask(task: Task) {
        viewModelScope.launch {
            tasksRepository.updateTask(task)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val taskList: List<Task> = listOf())
