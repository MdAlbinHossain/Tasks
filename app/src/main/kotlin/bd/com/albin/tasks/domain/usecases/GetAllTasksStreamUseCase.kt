package bd.com.albin.tasks.domain.usecases

import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.data.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksStreamUseCase @Inject constructor(private val tasksRepository: TasksRepository) {
    operator fun invoke(): Flow<List<Task>> {
        return tasksRepository.getAllTasksStream()
    }
}