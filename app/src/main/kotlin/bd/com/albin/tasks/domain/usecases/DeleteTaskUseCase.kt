package bd.com.albin.tasks.domain.usecase

import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.data.repository.TasksRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val tasksRepository: TasksRepository) {
    suspend operator fun invoke(task: Task){tasksRepository.deleteTask(task)}
}