package bd.com.albin.tasks.domain

import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.data.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskStreamUseCase @Inject constructor(private val tasksRepository: TasksRepository) {
    operator fun invoke(id: Int): Flow<Task?> {
        return tasksRepository.getTaskStream(id)
    }
}